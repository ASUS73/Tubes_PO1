package app.tubes_po_gui_v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class kelolaAkunController {

    private static ObservableList<Akun> akunList = FXCollections.observableArrayList(); // Tidak lagi static

    private Parent root;
    private Stage stage;
    private Scene scene;

    @FXML
    private Label jumlahAkunLabel;

    @FXML
    private Label namaAkunTertinggiLabel;

    @FXML
    private Label saldoTertinggiLabel;

    @FXML
    private TableView<Akun> akunTable;

    @FXML
    private TableColumn<Akun, String> namaAkunColumn;

    @FXML
    private TableColumn<Akun, Double> saldoColumn;

    @FXML
    private TableColumn<Akun, Void> actionColumn;

    private static final String FILE_NAME = "akunData.json";

    public static ObservableList<Akun> getAkunList() {
        if (akunList == null) {
            akunList = DataManager.getInstance().getAkunList();
        }
        return akunList;
    }

    @FXML
    public void initialize() {
        if (akunTable == null || namaAkunColumn == null || saldoColumn == null) {
            System.err.println("Elemen FXML belum terinisialisasi dengan benar!");
            return;
        }

        namaAkunColumn.setCellValueFactory(new PropertyValueFactory<>("namaAkun"));
        saldoColumn.setCellValueFactory(new PropertyValueFactory<>("saldo"));

        akunList.addListener((ListChangeListener<Akun>) c -> {
            while (c.next()) {
                if (c.wasUpdated()) {
                    akunTable.refresh();
                }
            }
        });

        loadDataFromFile();
        akunTable.setItems(akunList);
        updateStatistics();
        akunList.addListener((ListChangeListener<Akun>) c -> {
            updateStatistics();
        });
    }

    @FXML
    public void changeBeranda(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("beranda.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void modalBuatAkun() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("modalBuatAkun.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Buat Akun");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void modalEditAkun() throws IOException {
        Akun selectedAkun = akunTable.getSelectionModel().getSelectedItem();
        if (selectedAkun == null) {
            showAlert("Peringatan", "Tidak ada akun yang dipilih untuk diedit.");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("modalEditAkun.fxml"));
        Parent root = fxmlLoader.load();
        ModalEditAkunController controller = fxmlLoader.getController();
        controller.setAkunList(akunList);
        controller.akunChoiceBox.setValue(selectedAkun);

        Stage stage = new Stage();
        stage.setTitle("Edit Akun");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleDelete(Akun akun) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText("Hapus Akun");
        alert.setContentText("Apakah Anda yakin ingin menghapus akun " + akun.getNamaAkun() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            akunList.remove(akun);
            saveDataToFile();
            refreshTable();
        }
    }

    public void saveDataToFile() {
        try {
            File file = new File(FILE_NAME);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(file, akunList);

            updateStatistics();

            System.out.println("Data akun berhasil disimpan ke file JSON: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Gagal menyimpan data akun ke file JSON: " + e.getMessage());
        }
    }

    public void loadDataFromFile() {
        try {
            File file = new File(FILE_NAME);

            if (file.exists()) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<Akun> akunData = objectMapper.readValue(
                        file, objectMapper.getTypeFactory().constructCollectionType(List.class, Akun.class)
                );
                akunList = FXCollections.observableArrayList(akunData);

                updateStatistics();

                System.out.println("Data akun berhasil dimuat dari file JSON.");
            } else {
                System.out.println("File data akun tidak ditemukan. Membuat file baru...");
                akunList = FXCollections.observableArrayList();
            }
        } catch (IOException e) {
            System.err.println("Gagal membaca data akun dari file JSON: " + e.getMessage());
            akunList = FXCollections.observableArrayList();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void refreshTable() {
        akunTable.refresh();
        updateStatistics();
    }

    private void updateStatistics() {
        int jumlahAkun = akunList.size();
        if (jumlahAkunLabel != null) {
            jumlahAkunLabel.setText("Jumlah Akun: " + jumlahAkun);
        }

        if (!akunList.isEmpty()) {
            Akun akunTertinggi = akunList.stream()
                    .max((a1, a2) -> Double.compare(a1.getSaldo(), a2.getSaldo()))
                    .orElse(null);

            if (akunTertinggi != null && namaAkunTertinggiLabel != null && saldoTertinggiLabel != null) {
                namaAkunTertinggiLabel.setText(akunTertinggi.getNamaAkun());
                saldoTertinggiLabel.setText(String.format("Rp. %.0f", akunTertinggi.getSaldo()));
            }
        }
    }


}
