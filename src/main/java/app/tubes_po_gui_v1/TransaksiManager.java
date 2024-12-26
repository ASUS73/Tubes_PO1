package app.tubes_po_gui_v1;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TransaksiManager {
    private static final String FILE_NAME = "transaksiData.json";
    private static TransaksiManager instance;
    private ObservableList<Transaksi> transaksiList = FXCollections.observableArrayList();

    private TransaksiManager() {
        loadDataFromFile();
    }

    public static TransaksiManager getInstance() {
        if (instance == null) {
            instance = new TransaksiManager();
        }
        return instance;
    }

    public ObservableList<Transaksi> getTransaksiList() {
        return transaksiList;
    }

    public void addTransaksi(Transaksi transaksi) {
        transaksiList.add(transaksi);
        saveDataToFile();
    }

    public void saveDataToFile() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(FILE_NAME), transaksiList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDataFromFile() {
        try {
            File file = new File(FILE_NAME);
            if (file.exists()) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                List<Transaksi> loadedList = objectMapper.readValue(file,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Transaksi.class));
                transaksiList = FXCollections.observableArrayList(loadedList);
                System.out.println("Data transaksi berhasil dimuat: " + transaksiList.size() + " transaksi");

                transaksiList.forEach(t -> System.out.println("Transaksi: " + t.getNamaAkun() + " - " + t.getTransaksi() + " - " + t.getTanggal()));
            } else {
                System.out.println("File transaksi tidak ditemukan: " + FILE_NAME);
            }
        } catch (IOException e) {
            System.err.println("Error loading transaksi data: " + e.getMessage());
            e.printStackTrace();
            transaksiList = FXCollections.observableArrayList();
        }
    }


    public double getTotalPengeluaran() {
        return transaksiList.stream()
                .mapToDouble(t -> Double.parseDouble(t.getTransaksi().replace("Rp. ", "")))
                .sum();
    }
}
