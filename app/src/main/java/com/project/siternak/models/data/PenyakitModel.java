package com.project.siternak.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PenyakitModel implements Serializable {
    @SerializedName("id")
    @Expose
    Integer id;
    @SerializedName("nama_penyakit")
    @Expose
    String namaPenyakit;
    @SerializedName("ket_penyakit")
    @Expose
    String ketPenyakit;
    @SerializedName("created_at")
    @Expose
    String created_at;
    @SerializedName("updated_at")
    @Expose
    String updated_at;

    public PenyakitModel(Integer id, String namaPenyakit, String ketPenyakit, String created_at, String updated_at) {
        this.id = id;
        this.namaPenyakit = namaPenyakit;
        this.ketPenyakit = ketPenyakit;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamaPenyakit() {
        return namaPenyakit;
    }

    public void setNamaPenyakit(String namaPenyakit) {
        this.namaPenyakit = namaPenyakit;
    }

    public String getKetPenyakit() {
        return ketPenyakit;
    }

    public void setKetPenyakit(String ketPenyakit) {
        this.ketPenyakit = ketPenyakit;
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
