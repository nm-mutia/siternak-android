package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.data.PemilikModel;

import java.util.List;

public class PemilikResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("pemilik")
    @Expose
    private PemilikModel pemiliks;

    @SerializedName("error")
    @Expose
    private List errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PemilikModel getPemiliks() {
        return pemiliks;
    }

    public void setPemiliks(PemilikModel pemiliks) {
        this.pemiliks = pemiliks;
    }

    public List getErrors() {
        return errors;
    }

    public void setErrors(List errors) {
        this.errors = errors;
    }
}
