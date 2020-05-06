package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.data.PenyakitModel;

import java.util.List;

public class PenyakitGetResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("penyakit")
    @Expose
    private List<PenyakitModel> penyakits;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PenyakitModel> getPenyakits() {
        return penyakits;
    }

    public void setPenyakits(List<PenyakitModel> penyakits) {
        this.penyakits = penyakits;
    }
}
