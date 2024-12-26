package app.tubes_po_gui_v1;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class berandaController {
    @FXML private Label totalSaldoLabel;
    @FXML private Label topAkunLabel;
    @FXML private Label topSaldoLabel;
    @FXML private Label totalPengeluaranLabel;
    @FXML private Label jumlahAkunLabel;
    @FXML private Label rightTopAkunLabel;
    @FXML private Label rightTopSaldoLabel;


    // Table components
    @FXML private TableView<Transaksi> transaksiTable;
    @FXML private TableColumn<Transaksi, String> namaAkunColumn;
    @FXML private TableColumn<Transaksi, String> transaksiColumn;
    @FXML private TableColumn<Transaksi, String> tanggalColumn;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private DataManager dataManager;

    kelolaAkunController kelolaAkunController;

    @FXML
    public void initialize() {
        System.out.println("Initializing berandaController");

        // Initialize DataManager
        dataManager = DataManager.getInstance();
        kelolaAkunController = new kelolaAkunController();
        setupInitialState();
        setupListeners();
    }

    private void setupInitialState() {
        System.out.println("Setting up initial state");
        setupTable();
        updateDashboard();
    }

    private void setupListeners() {
        dataManager.getAkunList().addListener((ListChangeListener<Akun>) change -> {
            while (change.next()) {
                updateDashboard();
            }
        });
    }


    private void setupTable() {
        namaAkunColumn.setCellValueFactory(new PropertyValueFactory<>("namaAkun"));
        transaksiColumn.setCellValueFactory(new PropertyValueFactory<>("transaksi"));
        tanggalColumn.setCellValueFactory(new PropertyValueFactory<>("tanggal"));

        transaksiTable.setItems(TransaksiManager.getInstance().getTransaksiList());
        transaksiTable.refresh();
    }

    public void updateDashboard() {
        try {
            final ObservableList<Akun> akunList = dataManager.getAkunList();
            System.out.println("Updating dashboard with " + akunList.size() + " accounts");

            double totalSaldo = 0;
            String akunTerbanyak = "-";
            double saldoTerbanyak = 0;

            for (Akun akun : akunList) {
                System.out.println("Akun: " + akun.getNamaAkun() + " Saldo: " + akun.getSaldo());
                totalSaldo += akun.getSaldo();
                if (akun.getSaldo() > saldoTerbanyak) {
                    saldoTerbanyak = akun.getSaldo();
                    akunTerbanyak = akun.getNamaAkun();
                }
            }

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            final String formattedSaldo = currencyFormat.format(totalSaldo)
                    .replace("IDR", "Rp.")
                    .replace(",00", "");

            final String formattedSaldoTerbanyak = currencyFormat.format(saldoTerbanyak)
                    .replace("IDR", "Rp.")
                    .replace(",00", "");

            System.out.println("Total saldo dihitung: " + formattedSaldo);
            System.out.println("Akun terbanyak: " + akunTerbanyak + " dengan saldo " + formattedSaldoTerbanyak);

            final String finalAkunTerbanyak = akunTerbanyak;
            final int finalSize = akunList.size();

            Platform.runLater(() -> {
                // Update total saldo
                if (totalSaldoLabel != null) {
                    totalSaldoLabel.setText(formattedSaldo);
                }

                // Update jumlah akun
                if (jumlahAkunLabel != null) {
                    jumlahAkunLabel.setText("Jumlah Akun: " + finalSize);
                }

                // Update informasi akun terbanyak
                if (topAkunLabel != null) {
                    topAkunLabel.setText(finalAkunTerbanyak);
                }
                if (topSaldoLabel != null) {
                    topSaldoLabel.setText(formattedSaldoTerbanyak);
                }

                if (rightTopAkunLabel != null) {
                    rightTopAkunLabel.setText(finalAkunTerbanyak);
                }
                if (rightTopSaldoLabel != null) {
                    rightTopSaldoLabel.setText(formattedSaldoTerbanyak);
                }

                // Update total pengeluaran
                if (totalPengeluaranLabel != null) {
                    double totalPengeluaran = hitungTotalPengeluaran();
                    String formattedPengeluaran = currencyFormat.format(totalPengeluaran)
                            .replace("IDR", "Rp.")
                            .replace(",00", "");
                    totalPengeluaranLabel.setText(formattedPengeluaran);
                }

                updateTransaksiTable();
            });
        } catch (Exception e) {
            System.err.println("Error updating dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }





    private double hitungTotalSaldo() {
        ObservableList<Akun> akunList = kelolaAkunController.getAkunList();
        return akunList.stream()
                .mapToDouble(Akun::getSaldo)
                .sum();
    }

    private void updateTopAkun() {
        try {
            ObservableList<Akun> akunList = kelolaAkunController.getAkunList();

            if (akunList != null && !akunList.isEmpty()) {
                Akun topAkun = akunList.stream()
                        .max((a1, a2) -> Double.compare(a1.getSaldo(), a2.getSaldo()))
                        .orElse(null);

                if (topAkun != null) {
                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                    String formattedSaldo = currencyFormat.format(topAkun.getSaldo())
                            .replace("IDR", "Rp.")
                            .replace(",00", "");

                    Platform.runLater(() -> {
                        topAkunLabel.setText(topAkun.getNamaAkun());
                        topSaldoLabel.setText(formattedSaldo);

                        if (rightTopAkunLabel != null) {
                            rightTopAkunLabel.setText(topAkun.getNamaAkun());
                        }
                        if (rightTopSaldoLabel != null) {
                            rightTopSaldoLabel.setText(formattedSaldo);
                        }
                    });
                }
            } else {
                Platform.runLater(() -> {
                    topAkunLabel.setText("-");
                    topSaldoLabel.setText("Rp. 0");
                    if (rightTopAkunLabel != null) {
                        rightTopAkunLabel.setText("-");
                    }
                    if (rightTopSaldoLabel != null) {
                        rightTopSaldoLabel.setText("Rp. 0");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private double hitungTotalPengeluaran() {
        double total = 0;
        ObservableList<Transaksi> transaksiList = TransaksiManager.getInstance().getTransaksiList();

        if (transaksiList != null) {
            for (Transaksi transaksi : transaksiList) {
                String transaksiStr = transaksi.getTransaksi()
                        .replace("Rp.", "")
                        .replace(".", "")
                        .trim();

                try {
                    double nominal = Double.parseDouble(transaksiStr);
                    total += nominal;
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing transaksi: " + transaksiStr);
                }
            }
        }

        return total;
    }

    private void updateTransaksiTable() {
        ObservableList<Transaksi> transaksiList = TransaksiManager.getInstance().getTransaksiList();
        transaksiTable.setItems(transaksiList);
        transaksiTable.refresh();
    }

    @FXML
    public void changeKelolaAkun(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("kelolaAkun.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void handleIsiSaldo(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("modalIsiSaldo.fxml"));
        Parent root = fxmlLoader.load();

        ModalIsiSaldoController modalController = fxmlLoader.getController();
        modalController.setBerandaController(this);

        Stage stage = new Stage();
        stage.setTitle("Isi Saldo");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void handleTransaksi(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("modalTransaksi.fxml"));
        Parent root = fxmlLoader.load();

        modalTransaksiController modalController = fxmlLoader.getController();
        kelolaAkunController akunController = new kelolaAkunController();
        modalController.setKelolaAkunController(akunController);

        Stage stage = new Stage();
        stage.setTitle("Transaksi");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void updateTotalPengeluaran() {
        double total = TransaksiManager.getInstance().getTotalPengeluaran();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String formattedTotal = currencyFormat.format(total)
                .replace("IDR", "Rp.")
                .replace(",00", "");
        totalPengeluaranLabel.setText(formattedTotal);
    }

    public void refreshData() {
        Platform.runLater(() -> {
            updateDashboard();
            transaksiTable.refresh();
        });
    }
}
