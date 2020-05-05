package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.data.KematianModel;

public class KematianResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("kematian")
    @Expose
    private KematianModel kematians;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public KematianModel getKematians() {
        return kematians;
    }

    public void setKematians(KematianModel kematians) {
        this.kematians = kematians;
    }
}
