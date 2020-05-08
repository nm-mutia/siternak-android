package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.data.PeternakanModel;

import java.util.List;

public class PeternakanGetResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("peternakan")
    @Expose
    private List<PeternakanModel> peternakans;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PeternakanModel> getPeternakans() {
        return peternakans;
    }

    public void setPeternakans(List<PeternakanModel> peternakans) {
        this.peternakans = peternakans;
    }
}
