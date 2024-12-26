package app.tubes_po_gui_v1;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModalBuatAkunController {

    @FXML
    private TextField namaAkunField;

    @FXML
    private TextField saldoField;

    @FXML
    public void buatAkun() {
        String namaAkun = namaAkunField.getText();
        String saldoText = saldoField.getText();

        if (!namaAkun.isEmpty() && !saldoText.isEmpty()) {
            try {
                double saldo = Double.parseDouble(saldoText);
                Akun akun = new Akun(namaAkun, saldo);

                kelolaAkunController.getAkunList().add(akun);
                kelolaAkunController controller = new kelolaAkunController();
                controller.saveDataToFile();

                Stage stage = (Stage) namaAkunField.getScene().getWindow();
                stage.close();
            } catch (NumberFormatException e) {
                System.out.println("Saldo harus berupa angka.");
            }
        } else {
            System.out.println("Semua field harus diisi.");
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
