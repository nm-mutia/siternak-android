package com.project.siternak.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RasModel implements Serializable {
    @SerializedName("id")
    @Expose
    Integer id;
    @SerializedName("jenis_ras")
    @Expose
    String jenisRas;
    @SerializedName("ket_ras")
    @Expose
    String ketRas;
    @SerializedName("created_at")
    @Expose
    String created_at;
    @SerializedName("updated_at")
    @Expose
    String updated_at;

    public RasModel() {
    }

    public RasModel(Integer id, String jenisRas, String ketRas, String created_at, String updated_at) {
        this.id = id;
        this.jenisRas = jenisRas;
        this.ketRas = ketRas;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJenisRas() {
        return jenisRas;
    }

    public void setJenisRas(String jenisRas) {
        this.jenisRas = jenisRas;
    }

    public String getKetRas() {
        return ketRas;
    }

    public void setKetRas(String ketRas) {
        this.ketRas = ketRas;
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
