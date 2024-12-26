package app.tubes_po_gui_v1;

import app.tubes_po_gui_v1.kelolaAkunController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Optional;

public class ModalEditAkunController {
    @FXML
    private Button deleteButton;

    @FXML
    ChoiceBox<Akun> akunChoiceBox;

    @FXML
    private TextField namaAkunField;

    @FXML
    private TextField saldoField;

    private ObservableList<Akun> akunList = FXCollections.observableArrayList();

    private Akun selectedAkun;

    private kelolaAkunController mainController;
    kelolaAkunController controller = new kelolaAkunController();

    public void setAkunList(ObservableList<Akun> akunList) {
        this.akunList = akunList;
        akunChoiceBox.setItems(akunList);
    }

    public void setMainController(kelolaAkunController controller) {
        this.mainController = controller;
    }

    @FXML
    public void initialize() {
        akunChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedAkun = newValue;
                namaAkunField.setText(selectedAkun.getNamaAkun());
                saldoField.setText(String.valueOf(selectedAkun.getSaldo()));
            }
        });
    }

    @FXML
    public void editAkun() {
        if (selectedAkun == null) {
            showAlert("Peringatan", "Pilih akun terlebih dahulu.");
            return;
        }

        String namaAkun = namaAkunField.getText();
        String saldoText = saldoField.getText();

        if (namaAkun.isEmpty() || saldoText.isEmpty()) {
            showAlert("Peringatan", "Semua field harus diisi.");
            return;
        }

        try {
            double saldo = Double.parseDouble(saldoText);
            selectedAkun.setNamaAkun(namaAkun);
            selectedAkun.setSaldo(saldo);
            akunList.set(akunList.indexOf(selectedAkun), selectedAkun);
            kelolaAkunController controller = new kelolaAkunController();
            controller.saveDataToFile();

            Stage stage = (Stage) namaAkunField.getScene().getWindow();
            stage.close();
            refreshParentWindow();
        } catch (NumberFormatException e) {
            showAlert("Kesalahan", "Saldo harus berupa angka.");
        }
    }

    @FXML
    private void handleDelete() {
        Akun selectedAkun = akunChoiceBox.getValue();

        if (selectedAkun != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi Hapus");
            alert.setHeaderText("Hapus Akun");
            alert.setContentText("Apakah Anda yakin ingin menghapus akun " + selectedAkun.getNamaAkun() + "?"); // Ganti getNama() menjadi getNamaAkun()

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                akunList.remove(selectedAkun);
                kelolaAkunController controller = new kelolaAkunController();
                controller.saveDataToFile();
                Stage stage = (Stage) deleteButton.getScene().getWindow();
                stage.close();

                refreshParentWindow();
            }
        }
    }

    private void refreshParentWindow() {
        Stage parentStage = (Stage) akunChoiceBox.getScene().getWindow();
        for (Window window : Stage.getWindows()) {
            if (window instanceof Stage && window != parentStage) {
                Scene scene = ((Stage) window).getScene();
                if (scene.getRoot().lookup("#akunTable") instanceof TableView) {
                    TableView<Akun> tableView = (TableView<Akun>) scene.getRoot().lookup("#akunTable");
                    tableView.refresh();
                }
            }
        }
    }

    private boolean validateInput(String namaAkun, String saldoText) {
        if (namaAkun.isEmpty() || saldoText.isEmpty()) {
            showAlert("Error", "Semua field harus diisi!");
            return false;
        }

        try {
            double saldo = Double.parseDouble(saldoText);
            if (saldo < 0) {
                showAlert("Error", "Saldo tidak boleh negatif!");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Saldo harus berupa angka!");
            return false;
        }

        return true;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
