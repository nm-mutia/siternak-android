package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.data.TernakModel;

import java.util.List;

public class TernakResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("ternak")
    @Expose
    private TernakModel ternaks;

    @SerializedName("error")
    @Expose
    private List errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TernakModel getTernaks() {
        return ternaks;
    }

    public void setTernaks(TernakModel ternaks) {
        this.ternaks = ternaks;
    }

    public List getErrors() {
        return errors;
    }

    public void setErrors(List errors) {
        this.errors = errors;
    }
}
