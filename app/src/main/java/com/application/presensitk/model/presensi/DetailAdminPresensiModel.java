package com.application.presensitk.model.presensi;

public class DetailAdminPresensiModel {
    private Integer id_presensi;
    private Integer id_admin;
    private Integer id_guru;
    private String waktu;
    private String tipe;
    private String nama, nohp, email, password;

    public Integer getId_presensi() {
        return id_presensi;
    }

    public Integer getId_admin() {
        return id_admin;
    }

    public Integer getId_guru() {
        return id_guru;
    }

    public String getWaktu() {
        return waktu;
    }

    public String getTipe() {
        return tipe;
    }

    public String getNama() {
        return nama;
    }

    public String getNohp() {
        return nohp;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
