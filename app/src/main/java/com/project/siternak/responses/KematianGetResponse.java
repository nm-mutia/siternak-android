package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.data.KematianModel;

import java.util.List;

public class KematianGetResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("kematian")
    @Expose
    private List<KematianModel> kematians;

    public List<KematianModel> getKematians() {
        return kematians;
    }

    public void setKematians(List<KematianModel> kematians) {
        this.kematians = kematians;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
