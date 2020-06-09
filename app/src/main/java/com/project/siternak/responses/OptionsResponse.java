package com.project.siternak.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.siternak.models.data.KematianModel;
import com.project.siternak.models.data.PemilikModel;
import com.project.siternak.models.data.PenyakitModel;
import com.project.siternak.models.data.PeternakanModel;
import com.project.siternak.models.data.RasModel;
import com.project.siternak.models.data.TernakModel;

import java.util.List;

public class OptionsResponse {
    @SerializedName("kematian")
    @Expose
    private List<KematianModel> kematians;

    @SerializedName("pemilik")
    @Expose
    private List<PemilikModel> pemiliks;

    @SerializedName("penyakit")
    @Expose
    private List<PenyakitModel> penyakits;

    @SerializedName("peternakan")
    @Expose
    private List<PeternakanModel> peternakan;

    @SerializedName("ras")
    @Expose
    private List<RasModel> ras;

    @SerializedName("ternak")
    @Expose
    private List<TernakModel> ternaks;

    public List<KematianModel> getKematians() {
        return kematians;
    }

    public void setKematians(List<KematianModel> kematians) {
        this.kematians = kematians;
    }

    public List<PemilikModel> getPemiliks() {
        return pemiliks;
    }

    public void setPemiliks(List<PemilikModel> pemiliks) {
        this.pemiliks = pemiliks;
    }

    public List<PenyakitModel> getPenyakits() {
        return penyakits;
    }

    public void setPenyakits(List<PenyakitModel> penyakits) {
        this.penyakits = penyakits;
    }

    public List<PeternakanModel> getPeternakan() {
        return peternakan;
    }

    public void setPeternakan(List<PeternakanModel> peternakan) {
        this.peternakan = peternakan;
    }

    public List<RasModel> getRas() {
        return ras;
    }

    public void setRas(List<RasModel> ras) {
        this.ras = ras;
    }

    public List<TernakModel> getTernaks() {
        return ternaks;
    }

    public void setTernaks(List<TernakModel> ternaks) {
        this.ternaks = ternaks;
    }
}
