package com.application.presensitk.model.presensi;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AdminPresensiModel {
    private String message;
    private Boolean status;
    @Nullable
    private ArrayList<DetailAdminPresensiModel> data;

    @Nullable
    public ArrayList<DetailAdminPresensiModel> getData() {
        return data;
    }

    public void setData(@Nullable ArrayList<DetailAdminPresensiModel> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
