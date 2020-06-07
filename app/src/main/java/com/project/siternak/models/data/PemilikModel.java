package com.project.siternak.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PemilikModel implements Serializable {
    @SerializedName("id")
    @Expose
    Integer id;
    @SerializedName("ktp")
    @Expose
    String ktp;
    @SerializedName("nama_pemilik")
    @Expose
    String nama_pemilik;
    @SerializedName("created_at")
    @Expose
    String created_at;
    @SerializedName("updated_at")
    @Expose
    String updated_at;

    public PemilikModel() {
    }

    public PemilikModel(String ktp, String nama_pemilik) {
        this.ktp = ktp;
        this.nama_pemilik = nama_pemilik;
    }

    public PemilikModel(Integer id, String ktp, String nama_pemilik) {
        this.id = id;
        this.ktp = ktp;
        this.nama_pemilik = nama_pemilik;
    }

    public PemilikModel(Integer id, String ktp, String nama_pemilik, String created_at, String updated_at) {
        this.id = id;
        this.ktp = ktp;
        this.nama_pemilik = nama_pemilik;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    public String getNama_pemilik() {
        return nama_pemilik;
    }

    public void setNama_pemilik(String nama_pemilik) {
        this.nama_pemilik = nama_pemilik;
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
