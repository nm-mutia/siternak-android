package com.project.siternak.models.scan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Instance {
    @SerializedName("necktag")
    @Expose
    String necktag;
    @SerializedName("jenis_kelamin")
    @Expose
    String jenisKelamin;
    @SerializedName("ras")
    @Expose
    String ras;
    @SerializedName("tgl_lahir")
    @Expose
    String tglLahir;
    @SerializedName("blood")
    @Expose
    String blood;
    @SerializedName("peternakan")
    @Expose
    String peternakan;
    @SerializedName("ayah")
    @Expose
    String ayah;
    @SerializedName("ibu")
    @Expose
    String ibu;

    public Instance(String necktag, String jenisKelamin, String ras, String tglLahir, String blood, String peternakan, String ayah, String ibu) {
        this.necktag = necktag;
        this.jenisKelamin = jenisKelamin;
        this.ras = ras;
        this.tglLahir = tglLahir;
        this.blood = blood;
        this.peternakan = peternakan;
        this.ayah = ayah;
        this.ibu = ibu;
    }

    public String getNecktag() {
        return necktag;
    }

    public void setNecktag(String necktag) {
        this.necktag = necktag;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getRas() {
        return ras;
    }

    public void setRas(String ras) {
        this.ras = ras;
    }

    public String getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(String tglLahir) {
        this.tglLahir = tglLahir;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getPeternakan() {
        return peternakan;
    }

    public void setPeternakan(String peternakan) {
        this.peternakan = peternakan;
    }

    public String getAyah() {
        return ayah;
    }

    public void setAyah(String ayah) {
        this.ayah = ayah;
    }

    public String getIbu() {
        return ibu;
    }

    public void setIbu(String ibu) {
        this.ibu = ibu;
    }
}
