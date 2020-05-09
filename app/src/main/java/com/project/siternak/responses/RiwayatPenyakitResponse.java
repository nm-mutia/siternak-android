package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.data.RiwayatPenyakitModel;

import java.util.List;

public class RiwayatPenyakitResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("riwayat")
    @Expose
    private RiwayatPenyakitModel riwayats;

    @SerializedName("error")
    @Expose
    private List errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RiwayatPenyakitModel getRiwayats() {
        return riwayats;
    }

    public void setRiwayats(RiwayatPenyakitModel riwayats) {
        this.riwayats = riwayats;
    }

    public List getErrors() {
        return errors;
    }

    public void setErrors(List errors) {
        this.errors = errors;
    }
}
