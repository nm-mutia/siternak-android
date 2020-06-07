package com.project.siternak.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PerkawinanModel implements Serializable {
    @SerializedName("id")
    @Expose
    Integer id;
    @SerializedName("necktag")
    @Expose
    String necktag;
    @SerializedName("necktag_psg")
    @Expose
    String necktag_psg;
    @SerializedName("tgl")
    @Expose
    String tgl;
    @SerializedName("created_at")
    @Expose
    String created_at;
    @SerializedName("updated_at")
    @Expose
    String updated_at;

    public PerkawinanModel() {
    }

    public PerkawinanModel(String necktag, String necktag_psg, String tgl) {
        this.necktag = necktag;
        this.necktag_psg = necktag_psg;
        this.tgl = tgl;
    }

    public PerkawinanModel(Integer id, String necktag, String necktag_psg, String tgl) {
        this.id = id;
        this.necktag = necktag;
        this.necktag_psg = necktag_psg;
        this.tgl = tgl;
    }

    public PerkawinanModel(Integer id, String necktag, String necktag_psg, String tgl, String created_at, String updated_at) {
        this.id = id;
        this.necktag = necktag;
        this.necktag_psg = necktag_psg;
        this.tgl = tgl;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNecktag() {
        return necktag;
    }

    public void setNecktag(String necktag) {
        this.necktag = necktag;
    }

    public String getNecktag_psg() {
        return necktag_psg;
    }

    public void setNecktag_psg(String necktag_psg) {
        this.necktag_psg = necktag_psg;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
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
