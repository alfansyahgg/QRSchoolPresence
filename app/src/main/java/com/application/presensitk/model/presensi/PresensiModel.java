package com.application.presensitk.model.presensi;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PresensiModel {
    private String message;
    private Boolean status;
    @Nullable
    private ArrayList<DetailPresensiModel> data;

    @Nullable
    public ArrayList<DetailPresensiModel> getData() {
        return data;
    }

    public void setData(@Nullable ArrayList<DetailPresensiModel> data) {
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
