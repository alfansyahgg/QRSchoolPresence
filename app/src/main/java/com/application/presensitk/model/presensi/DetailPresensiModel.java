package com.application.presensitk.model.presensi;

public class DetailPresensiModel {
    private Integer id_presensi;
    private Integer id_admin;
    private Integer id_guru;
    private String waktu;
    private String tipe;

    public Integer getId_presensi() {
        return id_presensi;
    }

    public void setId_presensi(Integer id_presensi) {
        this.id_presensi = id_presensi;
    }

    public Integer getId_admin() {
        return id_admin;
    }

    public void setId_admin(Integer id_admin) {
        this.id_admin = id_admin;
    }

    public Integer getId_guru() {
        return id_guru;
    }

    public void setId_guru(Integer id_guru) {
        this.id_guru = id_guru;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }
}
