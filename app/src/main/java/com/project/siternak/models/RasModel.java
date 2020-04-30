package com.project.siternak.models;


public class RasModel {
    Integer id;
    String jenis_ras;
    String ket_ras;
    String created_at;
    String updated_at;

    public RasModel(Integer id, String jenis_ras, String ket_ras, String created_at, String updated_at) {
        this.id = id;
        this.jenis_ras = jenis_ras;
        this.ket_ras = ket_ras;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJenis_ras() {
        return jenis_ras;
    }

    public void setJenis_ras(String jenis_ras) {
        this.jenis_ras = jenis_ras;
    }

    public String getKet_ras() {
        return ket_ras;
    }

    public void setKet_ras(String ket_ras) {
        this.ket_ras = ket_ras;
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
