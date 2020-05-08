package com.project.siternak.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PeternakanModel implements Serializable {
    @SerializedName("id")
    @Expose
    Integer id;
    @SerializedName("nama_peternakan")
    @Expose
    String namaPeternakan;
    @SerializedName("keterangan")
    @Expose
    String keterangan;
    @SerializedName("created_at")
    @Expose
    String created_at;
    @SerializedName("updated_at")
    @Expose
    String updated_at;

    public PeternakanModel(Integer id, String namaPeternakan, String keterangan, String created_at, String updated_at) {
        this.id = id;
        this.namaPeternakan = namaPeternakan;
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

    public String getNamaPeternakan() {
        return namaPeternakan;
    }

    public void setNamaPeternakan(String namaPeternakan) {
        this.namaPeternakan = namaPeternakan;
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
