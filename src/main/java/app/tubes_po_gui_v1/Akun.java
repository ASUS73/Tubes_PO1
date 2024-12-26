package app.tubes_po_gui_v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Akun implements Serializable {
    private static final long serialVersionUID = 1L;

    private String namaAkun;
    private double saldo;

    public Akun() {

    }

    @JsonCreator
    public Akun(@JsonProperty("namaAkun") String namaAkun, @JsonProperty("saldo") double saldo) {
        this.namaAkun = namaAkun;
        this.saldo = saldo;
    }

    public String getNamaAkun() {
        return namaAkun;
    }

    public void setNamaAkun(String namaAkun) {
        this.namaAkun = namaAkun;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return namaAkun + " - Rp. " + saldo;
    }
}
