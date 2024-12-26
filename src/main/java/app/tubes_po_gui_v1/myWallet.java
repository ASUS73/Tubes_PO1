package app.tubes_po_gui_v1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class myWallet extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DataManager dataManager = DataManager.getInstance();
        System.out.println("DataManager initialized in myWallet");

        FXMLLoader fxmlLoader = new FXMLLoader(myWallet.class.getResource("beranda.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("scene1.css")).toExternalForm());

        berandaController controller = fxmlLoader.getController();

        stage.setTitle("My Wallet");
        stage.setScene(scene);
        stage.resizableProperty().setValue(false);

        stage.setOnCloseRequest(event -> {
            if (controller != null && controller.kelolaAkunController != null) {
                controller.kelolaAkunController.saveDataToFile();
            }
        });

        stage.show();

        if (controller != null) {
            controller.updateDashboard();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

