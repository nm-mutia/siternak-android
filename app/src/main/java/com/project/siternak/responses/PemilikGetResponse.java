package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.data.PemilikModel;

import java.util.List;

public class PemilikGetResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("pemilik")
    @Expose
    private List<PemilikModel> pemiliks;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PemilikModel> getPemiliks() {
        return pemiliks;
    }

    public void setPemiliks(List<PemilikModel> pemiliks) {
        this.pemiliks = pemiliks;
    }
}
