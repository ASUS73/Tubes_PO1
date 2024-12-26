package app.tubes_po_gui_v1;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

public class ModalIsiSaldoController {
    @FXML
    private ChoiceBox<Akun> akunChoiceBox;
    @FXML
    private TextField jumlahField;

    private kelolaAkunController kelolaAkunController;
    private berandaController berandaController;

    @FXML
    public void initialize() {
        kelolaAkunController = new kelolaAkunController();

        ObservableList<Akun> akunList = kelolaAkunController.getAkunList();
        akunChoiceBox.setItems(akunList);

        akunChoiceBox.setConverter(new javafx.util.StringConverter<Akun>() {
            @Override
            public String toString(Akun akun) {
                return akun != null ? akun.getNamaAkun() : "";
            }

            @Override
            public Akun fromString(String string) {
                return null;
            }
        });
    }

    public void setBerandaController(berandaController controller) {
        this.berandaController = controller;
    }

    @FXML
    public void isiSaldo() {
        try {
            Akun selectedAkun = akunChoiceBox.getValue();
            if (selectedAkun == null) {
                showAlert("Error", "Pilih akun terlebih dahulu!");
                return;
            }

            String jumlahText = jumlahField.getText();
            if (jumlahText.isEmpty()) {
                showAlert("Error", "Masukkan jumlah saldo!");
                return;
            }

            double jumlah = Double.parseDouble(jumlahText);
            if (jumlah <= 0) {
                showAlert("Error", "Jumlah saldo harus lebih dari 0!");
                return;
            }

            selectedAkun.setSaldo(selectedAkun.getSaldo() + jumlah);
            kelolaAkunController.saveDataToFile();

            if (berandaController != null) {
                berandaController.refreshData();
            }

            Stage stage = (Stage) jumlahField.getScene().getWindow();
            stage.close();

            showAlert("Sukses", "Saldo berhasil ditambahkan!");
        } catch (NumberFormatException e) {
            showAlert("Error", "Masukkan jumlah saldo yang valid!");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
