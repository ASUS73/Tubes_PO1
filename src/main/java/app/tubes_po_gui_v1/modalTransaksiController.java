package app.tubes_po_gui_v1;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class modalTransaksiController {
    @FXML
    private ChoiceBox<Akun> akunChoiceBox;
    @FXML
    private TextField jumlahTransaksiField;

    private kelolaAkunController kelolaAkunController;

    public void setKelolaAkunController(kelolaAkunController controller) {
        this.kelolaAkunController = controller;
    }

    @FXML
    public void initialize() {
        akunChoiceBox.setItems(DataManager.getInstance().getAkunList());
        System.out.println("Akun yang tersedia: " + DataManager.getInstance().getAkunList());

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

    @FXML
    public void handleTransaksi() {
        try {
            Akun selectedAkun = akunChoiceBox.getValue();
            if (selectedAkun == null) {
                showAlert("Error", "Pilih akun terlebih dahulu!");
                return;
            }

            String jumlahStr = jumlahTransaksiField.getText();
            if (jumlahStr.isEmpty()) {
                showAlert("Error", "Masukkan jumlah transaksi!");
                return;
            }

            double jumlah = Double.parseDouble(jumlahStr);
            if (jumlah <= 0) {
                showAlert("Error", "Jumlah transaksi harus lebih dari 0!");
                return;
            }

            if (selectedAkun.getSaldo() < jumlah) {
                showAlert("Error", "Saldo tidak mencukupi!");
                return;
            }

            selectedAkun.setSaldo(selectedAkun.getSaldo() - jumlah);
            DataManager.getInstance().saveDataToFile();

            String formattedDate = java.time.LocalDate.now().toString();
            Transaksi transaksi = new Transaksi(
                    selectedAkun.getNamaAkun(),
                    "Rp. " + jumlah,
                    formattedDate
            );
            TransaksiManager.getInstance().addTransaksi(transaksi);

            Stage stage = (Stage) akunChoiceBox.getScene().getWindow();
            stage.close();

            showSuccess("Transaksi berhasil!");
        } catch (NumberFormatException e) {
            showAlert("Error", "Masukkan jumlah yang valid!");
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukses");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
