package com.application.presensitk.model.guru;

import androidx.annotation.Nullable;

public class GuruModel {
    @Nullable
    private Integer id_guru;
    private String nama, nohp, email, password;

    public GuruModel(Integer id_guru, String nsama, String nohp, String email, String password) {
        this.id_guru = id_guru;
        this.nama = nama;
        this.nohp = nohp;
        this.email = email;
        this.password = password;
    }

    public Integer getId_guru() {
        return id_guru;
    }

    public void setId_guru(int id_guru) {
        this.id_guru = id_guru;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
