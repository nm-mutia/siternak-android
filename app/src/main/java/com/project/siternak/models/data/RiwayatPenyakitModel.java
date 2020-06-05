package com.project.siternak.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RiwayatPenyakitModel implements Serializable {
    @SerializedName("id")
    @Expose
    Integer id;
    @SerializedName("penyakit_id")
    @Expose
    Integer penyakitId;
    @SerializedName("necktag")
    @Expose
    String necktag;
    @SerializedName("tgl_sakit")
    @Expose
    String tglSakit;
    @SerializedName("obat")
    @Expose
    String obat;
    @SerializedName("lama_sakit")
    @Expose
    Integer lamaSakit;
    @SerializedName("keterangan")
    @Expose
    String keterangan;
    @SerializedName("created_at")
    @Expose
    String created_at;
    @SerializedName("updated_at")
    @Expose
    String updated_at;

    public RiwayatPenyakitModel() {
    }

    public RiwayatPenyakitModel(Integer id, Integer penyakitId, String necktag, String tglSakit, String obat, Integer lamaSakit, String keterangan, String created_at, String updated_at) {
        this.id = id;
        this.penyakitId = penyakitId;
        this.necktag = necktag;
        this.tglSakit = tglSakit;
        this.obat = obat;
        this.lamaSakit = lamaSakit;
        this.keterangan = keterangan;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPenyakitId() {
        return penyakitId;
    }

    public void setPenyakitId(Integer penyakitId) {
        this.penyakitId = penyakitId;
    }

    public String getNecktag() {
        return necktag;
    }

    public void setNecktag(String necktag) {
        this.necktag = necktag;
    }

    public String getTglSakit() {
        return tglSakit;
    }

    public void setTglSakit(String tglSakit) {
        this.tglSakit = tglSakit;
    }

    public String getObat() {
        return obat;
    }

    public void setObat(String obat) {
        this.obat = obat;
    }

    public Integer getLamaSakit() {
        return lamaSakit;
    }

    public void setLamaSakit(Integer lamaSakit) {
        this.lamaSakit = lamaSakit;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
