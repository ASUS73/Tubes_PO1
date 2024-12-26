module org.example.tubes_po_gui_v1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.google.gson;


    opens app.tubes_po_gui_v1 to javafx.fxml, com.google.gson;
    exports app.tubes_po_gui_v1;
}