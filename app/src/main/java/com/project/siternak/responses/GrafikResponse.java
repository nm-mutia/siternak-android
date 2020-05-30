package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.grafik.GrafikModel;

public class GrafikResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("ras")
    @Expose
    private GrafikModel ras;

    @SerializedName("umur")
    @Expose
    private GrafikModel umur;

    @SerializedName("lahir")
    @Expose
    private GrafikModel lahir;

    @SerializedName("mati")
    @Expose
    private GrafikModel mati;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GrafikModel getRas() {
        return ras;
    }

    public void setRas(GrafikModel ras) {
        this.ras = ras;
    }

    public GrafikModel getUmur() {
        return umur;
    }

    public void setUmur(GrafikModel umur) {
        this.umur = umur;
    }

    public GrafikModel getLahir() {
        return lahir;
    }

    public void setLahir(GrafikModel lahir) {
        this.lahir = lahir;
    }

    public GrafikModel getMati() {
        return mati;
    }

    public void setMati(GrafikModel mati) {
        this.mati = mati;
    }
}
