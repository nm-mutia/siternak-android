package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.data.RiwayatPenyakitModel;

import java.util.List;

public class RiwayatPenyakitGetResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("riwayat")
    @Expose
    private List<RiwayatPenyakitModel> riwayats;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RiwayatPenyakitModel> getRiwayats() {
        return riwayats;
    }

    public void setRiwayats(List<RiwayatPenyakitModel> riwayats) {
        this.riwayats = riwayats;
    }
}
