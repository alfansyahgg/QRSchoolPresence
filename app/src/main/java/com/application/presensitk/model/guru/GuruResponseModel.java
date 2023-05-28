package com.application.presensitk.model.guru;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GuruResponseModel {
    private String message;
    private Boolean status;
    @Nullable
    private ArrayList<GuruModel> result;

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

    public ArrayList<GuruModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<GuruModel> result) {
        this.result = result;
    }
}
