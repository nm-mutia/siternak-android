package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.data.TernakModel;

import java.util.List;

public class TernakGetResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("ternak")
    @Expose
    private List<TernakModel> ternaks;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TernakModel> getTernaks() {
        return ternaks;
    }

    public void setTernaks(List<TernakModel> ternaks) {
        this.ternaks = ternaks;
    }
}
