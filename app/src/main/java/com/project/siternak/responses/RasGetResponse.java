package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.data.RasModel;

import java.util.List;

public class RasGetResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("ras")
    @Expose
    private List<RasModel> ras;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RasModel> getRas() {
        return ras;
    }

    public void setRas(List<RasModel> ras) {
        this.ras = ras;
    }
}
