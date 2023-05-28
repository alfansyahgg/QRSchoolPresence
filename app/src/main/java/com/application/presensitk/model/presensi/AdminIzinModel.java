package com.application.presensitk.model.presensi;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AdminIzinModel {

    private String message;
    private Boolean status;
    @Nullable
    private ArrayList<DetailAdminIzinModel> data;

    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }

    @Nullable
    public ArrayList<DetailAdminIzinModel> getData() {
        return data;
    }
}
