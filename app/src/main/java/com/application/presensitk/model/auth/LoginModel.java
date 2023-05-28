package com.application.presensitk.model.auth;

public class LoginModel {
    private String message;
    private Boolean status;
    private ResponseModel data;
    private Boolean admin;

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

    public ResponseModel getData() {
        return data;
    }

    public void setData(ResponseModel data) {
        this.data = data;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
