package app.tubes_po_gui_v1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class ModalHapusAkunController {

    @FXML
    private ChoiceBox<Akun> akunChoiceBox;

    @FXML
    public void initialize() {
        akunChoiceBox.setItems(kelolaAkunController.getAkunList());
    }

    @FXML
    public void hapusAkun() {
        Akun selectedAkun = akunChoiceBox.getValue();
        if (selectedAkun != null) {
            kelolaAkunController.getAkunList().remove(selectedAkun);

            kelolaAkunController controller = new kelolaAkunController();
            controller.saveDataToFile();

            Stage stage = (Stage) akunChoiceBox.getScene().getWindow();
            stage.close();

        } else {
            System.out.println("Pilih akun yang ingin dihapus.");
        }
    }
}
