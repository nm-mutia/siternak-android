package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.peternak.PeternakModel;

import java.util.List;

public class PeternakGetResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("peternak")
    @Expose
    private List<PeternakModel> peternaks;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PeternakModel> getPeternaks() {
        return peternaks;
    }

    public void setPeternaks(List<PeternakModel> peternaks) {
        this.peternaks = peternaks;
    }
}
