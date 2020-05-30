package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.grafik.GrafikModel;

public class GrafikYearResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("data")
    @Expose
    private GrafikModel data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GrafikModel getData() {
        return data;
    }

    public void setData(GrafikModel data) {
        this.data = data;
    }
}
