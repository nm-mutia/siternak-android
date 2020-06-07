package com.project.siternak.models.peternak;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PeternakModel implements Serializable {
    @SerializedName("id")
    @Expose
    Integer id;
    @SerializedName("peternakan_id")
    @Expose
    Integer peternakanId;
    @SerializedName("nama_peternak")
    @Expose
    String namaPeternak;
    @SerializedName("username")
    @Expose
    String username;
    @SerializedName("password")
    @Expose
    String password;
    @SerializedName("created_at")
    @Expose
    String created_at;
    @SerializedName("updated_at")
    @Expose
    String updated_at;

    // for add only
    String email;
    public PeternakModel(Integer peternakanId, String namaPeternak, String username, String email) {
        this.peternakanId = peternakanId;
        this.namaPeternak = namaPeternak;
        this.username = username;
        this.email = email;
    }

    public PeternakModel() {
    }

    public PeternakModel(Integer id, Integer peternakanId, String namaPeternak) {
        this.id = id;
        this.peternakanId = peternakanId;
        this.namaPeternak = namaPeternak;
    }

    public PeternakModel(Integer id, Integer peternakanId, String namaPeternak, String username, String password, String created_at, String updated_at) {
        this.id = id;
        this.peternakanId = peternakanId;
        this.namaPeternak = namaPeternak;
        this.username = username;
        this.password = password;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPeternakanId() {
        return peternakanId;
    }

    public void setPeternakanId(Integer peternakanId) {
        this.peternakanId = peternakanId;
    }

    public String getNamaPeternak() {
        return namaPeternak;
    }

    public void setNamaPeternak(String namaPeternak) {
        this.namaPeternak = namaPeternak;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
