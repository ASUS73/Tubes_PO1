package app.tubes_po_gui_v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaksi {
    private String namaAkun;
    private String transaksi;
    private String tanggal;

    @JsonCreator
    public Transaksi(
            @JsonProperty("namaAkun") String namaAkun,
            @JsonProperty("transaksi") String transaksi,
            @JsonProperty("tanggal") String tanggal) {
        this.namaAkun = namaAkun;
        this.transaksi = transaksi;
        this.tanggal = tanggal;
    }

    public String getNamaAkun() {
        return namaAkun;
    }

    public void setNamaAkun(String namaAkun) {
        this.namaAkun = namaAkun;
    }

    public String getTransaksi() {
        return transaksi;
    }

    public void setTransaksi(String transaksi) {
        this.transaksi = transaksi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
