package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.data.PerkawinanModel;

import java.util.List;

public class PerkawinanGetResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("perkawinan")
    @Expose
    private List<PerkawinanModel> perkawinans;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PerkawinanModel> getPerkawinans() {
        return perkawinans;
    }

    public void setPerkawinans(List<PerkawinanModel> perkawinans) {
        this.perkawinans = perkawinans;
    }
}
