package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.data.RasModel;

import java.util.List;

public class RasResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("ras")
    @Expose
    private RasModel ras;

    @SerializedName("error")
    @Expose
    private List errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RasModel getRas() {
        return ras;
    }

    public void setRas(RasModel ras) {
        this.ras = ras;
    }

    public List getErrors() {
        return errors;
    }

    public void setErrors(List errors) {
        this.errors = errors;
    }
}
