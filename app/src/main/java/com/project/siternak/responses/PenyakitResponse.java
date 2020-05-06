package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.data.PenyakitModel;

import java.util.List;

public class PenyakitResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("penyakit")
    @Expose
    private PenyakitModel penyakits;

    @SerializedName("error")
    @Expose
    private List errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PenyakitModel getPenyakits() {
        return penyakits;
    }

    public void setPenyakits(PenyakitModel penyakits) {
        this.penyakits = penyakits;
    }

    public List getErrors() {
        return errors;
    }

    public void setErrors(List errors) {
        this.errors = errors;
    }
}
