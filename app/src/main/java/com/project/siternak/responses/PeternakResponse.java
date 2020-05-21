package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.peternak.PeternakModel;

import java.util.List;

public class PeternakResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("peternak")
    @Expose
    private PeternakModel peternaks;

    @SerializedName("error")
    @Expose
    private List errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PeternakModel getPeternaks() {
        return peternaks;
    }

    public void setPeternaks(PeternakModel peternaks) {
        this.peternaks = peternaks;
    }

    public List getErrors() {
        return errors;
    }

    public void setErrors(List errors) {
        this.errors = errors;
    }
}
