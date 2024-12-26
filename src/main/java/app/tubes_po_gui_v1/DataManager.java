package app.tubes_po_gui_v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataManager {
    private static final String FILE_NAME = "akunData.json";

    private static DataManager instance;

    private ObservableList<Akun> akunList = FXCollections.observableArrayList();

    private DataManager() {
        System.out.println("DataManager initialized");
        loadDataFromFile();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public ObservableList<Akun> getAkunList() {
        return akunList;
    }

    public void updateAkun(Akun akun) {
        int index = akunList.indexOf(akun);
        if (index >= 0) {
            akunList.set(index, akun);
            saveDataToFile();
        }
    }

    public void saveDataToFile() {
        try {
            File file = new File(FILE_NAME);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(file, akunList);
        } catch (IOException e) {
            String errorMsg = "Gagal menyimpan data: " + e.getMessage();
            System.err.println(errorMsg);
            throw new RuntimeException(errorMsg, e);
        }
    }

    public void loadDataFromFile() {
        try {
            File file = new File(FILE_NAME);
            if (file.exists()) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<Akun> akunData = objectMapper.readValue(
                        file,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Akun.class)
                );
                akunList.clear();
                akunList.addAll(akunData);
                System.out.println("Data loaded: " + akunList.size() + " accounts");
            } else {
                System.out.println("No existing data file found");
            }
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
