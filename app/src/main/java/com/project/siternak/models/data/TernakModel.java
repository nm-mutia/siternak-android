package com.project.siternak.models.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TernakModel implements Serializable {
    @SerializedName("necktag")
    @Expose
    String necktag;
    @SerializedName("pemilik_id")
    @Expose
    Integer pemilikId;
    @SerializedName("peternakan_id")
    @Expose
    Integer peternakanId;
    @SerializedName("ras_id")
    @Expose
    Integer rasId;
    @SerializedName("kematian_id")
    @Expose
    Integer kematianId;
    @SerializedName("jenis_kelamin")
    @Expose
    String jenisKelamin;
    @SerializedName("tgl_lahir")
    @Expose
    String tglLahir;
    @SerializedName("bobot_lahir")
    @Expose
    String bobotLahir;
    @SerializedName("pukul_lahir")
    @Expose
    String pukulLahir;
    @SerializedName("lama_dikandungan")
    @Expose
    String lamaDiKandungan;
    @SerializedName("lama_laktasi")
    @Expose
    String lamaLaktasi;
    @SerializedName("tgl_lepas_sapih")
    @Expose
    String tglLepasSapih;
    @SerializedName("blood")
    @Expose
    String blood;
    @SerializedName("necktag_ayah")
    @Expose
    String necktag_ayah;
    @SerializedName("necktag_ibu")
    @Expose
    String necktag_ibu;
    @SerializedName("bobot_tubuh")
    @Expose
    String bobotTubuh;
    @SerializedName("panjang_tubuh")
    @Expose
    String panjangTubuh;
    @SerializedName("tinggi_tubuh")
    @Expose
    String tinggiTubuh;
    @SerializedName("cacat_fisik")
    @Expose
    String cacatFisik;
    @SerializedName("ciri_lain")
    @Expose
    String ciriLain;
    @SerializedName("status_ada")
    @Expose
    Boolean statusAda;
    @SerializedName("created_at")
    @Expose
    String created_at;
    @SerializedName("updated_at")
    @Expose
    String updated_at;
    @SerializedName("deleted_at")
    @Expose
    String deleted_at;

    public TernakModel(String necktag, Integer rasId, String jenisKelamin, String tglLahir, String blood, String necktag_ayah, String necktag_ibu, Boolean statusAda) {
        this.necktag = necktag;
        this.rasId = rasId;
        this.jenisKelamin = jenisKelamin;
        this.tglLahir = tglLahir;
        this.blood = blood;
        this.necktag_ayah = necktag_ayah;
        this.necktag_ibu = necktag_ibu;
        this.statusAda = statusAda;
    }

    public TernakModel(String necktag, Integer pemilikId, Integer peternakanId, Integer rasId,
                       Integer kematianId, String jenisKelamin, String tglLahir, String bobotLahir,
                       String pukulLahir, String lamaDiKandungan, String lamaLaktasi, String tglLepasSapih,
                       String blood, String necktag_ayah, String necktag_ibu, String bobotTubuh,
                       String panjangTubuh, String tinggiTubuh, String cacatFisik, String ciriLain, Boolean statusAda,
                       String created_at, String updated_at, String deleted_at) {
        this.necktag = necktag;
        this.pemilikId = pemilikId;
        this.peternakanId = peternakanId;
        this.rasId = rasId;
        this.kematianId = kematianId;
        this.jenisKelamin = jenisKelamin;
        this.tglLahir = tglLahir;
        this.bobotLahir = bobotLahir;
        this.pukulLahir = pukulLahir;
        this.lamaDiKandungan = lamaDiKandungan;
        this.lamaLaktasi = lamaLaktasi;
        this.tglLepasSapih = tglLepasSapih;
        this.blood = blood;
        this.necktag_ayah = necktag_ayah;
        this.necktag_ibu = necktag_ibu;
        this.bobotTubuh = bobotTubuh;
        this.panjangTubuh = panjangTubuh;
        this.tinggiTubuh = tinggiTubuh;
        this.cacatFisik = cacatFisik;
        this.ciriLain = ciriLain;
        this.statusAda = statusAda;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
    }

    public String getNecktag() {
        return necktag;
    }

    public void setNecktag(String necktag) {
        this.necktag = necktag;
    }

    public Integer getPemilikId() {
        return pemilikId;
    }

    public void setPemilikId(Integer pemilikId) {
        this.pemilikId = pemilikId;
    }

    public Integer getPeternakanId() {
        return peternakanId;
    }

    public void setPeternakanId(Integer peternakanId) {
        this.peternakanId = peternakanId;
    }

    public Integer getRasId() {
        return rasId;
    }

    public void setRasId(Integer rasId) {
        this.rasId = rasId;
    }

    public Integer getKematianId() {
        return kematianId;
    }

    public void setKematianId(Integer kematianId) {
        this.kematianId = kematianId;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(String tglLahir) {
        this.tglLahir = tglLahir;
    }

    public String getBobotLahir() {
        return bobotLahir;
    }

    public void setBobotLahir(String bobotLahir) {
        this.bobotLahir = bobotLahir;
    }

    public String getPukulLahir() {
        return pukulLahir;
    }

    public void setPukulLahir(String pukulLahir) {
        this.pukulLahir = pukulLahir;
    }

    public String getLamaDiKandungan() {
        return lamaDiKandungan;
    }

    public void setLamaDiKandungan(String lamaDiKandungan) {
        this.lamaDiKandungan = lamaDiKandungan;
    }

    public String getLamaLaktasi() {
        return lamaLaktasi;
    }

    public void setLamaLaktasi(String lamaLaktasi) {
        this.lamaLaktasi = lamaLaktasi;
    }

    public String getTglLepasSapih() {
        return tglLepasSapih;
    }

    public void setTglLepasSapih(String tglLepasSapih) {
        this.tglLepasSapih = tglLepasSapih;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getNecktag_ayah() {
        return necktag_ayah;
    }

    public void setNecktag_ayah(String necktag_ayah) {
        this.necktag_ayah = necktag_ayah;
    }

    public String getNecktag_ibu() {
        return necktag_ibu;
    }

    public void setNecktag_ibu(String necktag_ibu) {
        this.necktag_ibu = necktag_ibu;
    }

    public String getBobotTubuh() {
        return bobotTubuh;
    }

    public void setBobotTubuh(String bobotTubuh) {
        this.bobotTubuh = bobotTubuh;
    }

    public String getPanjangTubuh() {
        return panjangTubuh;
    }

    public void setPanjangTubuh(String panjangTubuh) {
        this.panjangTubuh = panjangTubuh;
    }

    public String getTinggiTubuh() {
        return tinggiTubuh;
    }

    public void setTinggiTubuh(String tinggiTubuh) {
        this.tinggiTubuh = tinggiTubuh;
    }

    public String getCacatFisik() {
        return cacatFisik;
    }

    public void setCacatFisik(String cacatFisik) {
        this.cacatFisik = cacatFisik;
    }

    public String getCiriLain() {
        return ciriLain;
    }

    public void setCiriLain(String ciriLain) {
        this.ciriLain = ciriLain;
    }

    public Boolean getStatusAda() {
        return statusAda;
    }

    public void setStatusAda(Boolean statusAda) {
        this.statusAda = statusAda;
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

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }
}
