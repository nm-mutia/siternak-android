package com.project.siternak.models.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("id")
    @Expose
    Integer id;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("username")
    @Expose
    String username;
    @SerializedName("role")
    @Expose
    String role;
    @SerializedName("email")
    @Expose
    String email;
    @SerializedName("register_from_admin")
    @Expose
    Boolean regAdmin;
    @SerializedName("email_verified_at")
    @Expose
    String email_verified_at;
    @SerializedName("created_at")
    @Expose
    String created_at;
    @SerializedName("updated_at")
    @Expose
    String updated_at;

    public UserModel() {
    }

    public UserModel(Integer id, String name, String username, String role, String email) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.role = role;
        this.email = email;
    }

    public UserModel(Integer id, String name, String username, String role, String email, Boolean regAdmin, String email_verified_at, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.role = role;
        this.email = email;
        this.regAdmin = regAdmin;
        this.email_verified_at = email_verified_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String usernamee) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(String email_verified_at) {
        this.email_verified_at = email_verified_at;
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

    public Boolean getRegAdmin() {
        return regAdmin;
    }

    public void setRegAdmin(Boolean regAdmin) {
        this.regAdmin = regAdmin;
    }
}
