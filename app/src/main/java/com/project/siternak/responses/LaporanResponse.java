package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.data.PerkawinanModel;
import com.project.siternak.models.data.RiwayatPenyakitModel;
import com.project.siternak.models.data.TernakMatiModel;
import com.project.siternak.models.data.TernakModel;

import java.util.List;

public class LaporanResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("lahir")
    @Expose
    private List<TernakModel> lahir;

    @SerializedName("mati")
    @Expose
    private List<TernakMatiModel> mati;

    @SerializedName("kawin")
    @Expose
    private List<PerkawinanModel> kawin;

    @SerializedName("sakit")
    @Expose
    private List<RiwayatPenyakitModel> sakit;

    @SerializedName("ada")
    @Expose
    private List<TernakModel> ada;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TernakModel> getLahir() {
        return lahir;
    }

    public void setLahir(List<TernakModel> lahir) {
        this.lahir = lahir;
    }

    public List<TernakMatiModel> getMati() {
        return mati;
    }

    public void setMati(List<TernakMatiModel> mati) {
        this.mati = mati;
    }

    public List<PerkawinanModel> getKawin() {
        return kawin;
    }

    public void setKawin(List<PerkawinanModel> kawin) {
        this.kawin = kawin;
    }

    public List<RiwayatPenyakitModel> getSakit() {
        return sakit;
    }

    public void setSakit(List<RiwayatPenyakitModel> sakit) {
        this.sakit = sakit;
    }

    public List<TernakModel> getAda() {
        return ada;
    }

    public void setAda(List<TernakModel> ada) {
        this.ada = ada;
    }
}
