package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.data.PerkawinanModel;

import java.util.List;

public class PerkawinanResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("perkawinan")
    @Expose
    private PerkawinanModel perkawinans;

    @SerializedName("error")
    @Expose
    private List errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PerkawinanModel getPerkawinans() {
        return perkawinans;
    }

    public void setPerkawinans(PerkawinanModel perkawinans) {
        this.perkawinans = perkawinans;
    }

    public List getErrors() {
        return errors;
    }

    public void setErrors(List errors) {
        this.errors = errors;
    }
}
