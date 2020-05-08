package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.data.PeternakanModel;

import java.util.List;

public class PeternakanResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("peternakan")
    @Expose
    private PeternakanModel peternakans;

    @SerializedName("error")
    @Expose
    private List errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PeternakanModel getPeternakans() {
        return peternakans;
    }

    public void setPeternakans(PeternakanModel peternakans) {
        this.peternakans = peternakans;
    }

    public List getErrors() {
        return errors;
    }

    public void setErrors(List errors) {
        this.errors = errors;
    }
}
