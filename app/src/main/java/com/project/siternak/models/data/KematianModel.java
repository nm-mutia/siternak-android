package com.project.siternak.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KematianModel implements Serializable {
    @SerializedName("id")
    @Expose
    Integer id;
    @SerializedName("tgl_kematian")
    @Expose
    String tgl_kematian;
    @SerializedName("waktu_kematian")
    @Expose
    String waktu_kematian;
    @SerializedName("penyebab")
    @Expose
    String penyebab;
    @SerializedName("kondisi")
    @Expose
    String kondisi;
    @SerializedName("created_at")
    @Expose
    String created_at;
    @SerializedName("updated_at")
    @Expose
    String updated_at;

    public KematianModel() {
    }

    public KematianModel(String tgl_kematian, String waktu_kematian, String penyebab, String kondisi) {
        this.tgl_kematian = tgl_kematian;
        this.waktu_kematian = waktu_kematian;
        this.penyebab = penyebab;
        this.kondisi = kondisi;
    }

    public KematianModel(Integer id, String tgl_kematian, String waktu_kematian, String penyebab, String kondisi) {
        this.id = id;
        this.tgl_kematian = tgl_kematian;
        this.waktu_kematian = waktu_kematian;
        this.penyebab = penyebab;
        this.kondisi = kondisi;
    }

    public KematianModel(Integer id, String tgl_kematian, String waktu_kematian, String penyebab, String kondisi, String created_at, String updated_at) {
        this.id = id;
        this.tgl_kematian = tgl_kematian;
        this.waktu_kematian = waktu_kematian;
        this.penyebab = penyebab;
        this.kondisi = kondisi;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTgl_kematian() {
        return tgl_kematian;
    }

    public void setTgl_kematian(String tgl_kematian) {
        this.tgl_kematian = tgl_kematian;
    }

    public String getWaktu_kematian() {
        return waktu_kematian;
    }

    public void setWaktu_kematian(String waktu_kematian) {
        this.waktu_kematian = waktu_kematian;
    }

    public String getPenyebab() {
        return penyebab;
    }

    public void setPenyebab(String penyebab) {
        this.penyebab = penyebab;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
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
