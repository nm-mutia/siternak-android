package com.project.siternak.rest;

import com.project.siternak.responses.DataDeleteResponse;
import com.project.siternak.responses.KematianResponse;
import com.project.siternak.responses.KematianGetResponse;
import com.project.siternak.responses.LoginResponse;
import com.project.siternak.responses.PemilikGetResponse;
import com.project.siternak.responses.PemilikResponse;
import com.project.siternak.responses.PenyakitGetResponse;
import com.project.siternak.responses.PenyakitResponse;
import com.project.siternak.responses.PeternakanGetResponse;
import com.project.siternak.responses.PeternakanResponse;
import com.project.siternak.responses.RasGetResponse;
import com.project.siternak.responses.RasResponse;
import com.project.siternak.responses.TernakGetResponse;
import com.project.siternak.responses.TernakResponse;
import com.project.siternak.responses.UserDetailsResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    // ------------------------------------auth--------------------------------------------
    @POST("login")
    @FormUrlEncoded
    Call<LoginResponse> userLogin(@Field("email") String email, @Field("password") String password);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("profile")
    Call<UserDetailsResponse> userDetails(@Header("Authorization") String authToken);


    // ------------------------------------data kematian--------------------------------------------
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("kematian")
    Call<KematianGetResponse> getKematian(@Header("Authorization") String authToken);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("kematian/{id}")
    Call<KematianResponse> getKematianDetail(@Header("Authorization") String authToken, @Path("id") Integer id);

    @FormUrlEncoded
    @POST("kematian")
    Call<KematianResponse> addKematian(
            @Field("tgl_kematian") String tglKematian,
            @Field("waktu_kematian") String waktuKematian,
            @Field("penyebab") String penyebab,
            @Field("kondisi") String kondisi,
            @Header("Authorization") String authToken
    );

    @FormUrlEncoded
    @PUT("kematian/{id}")
    Call<KematianResponse> editKematian(
            @Path("id") Integer id,
            @Field("tgl_kematian") String tglKematian,
            @Field("waktu_kematian") String waktuKematian,
            @Field("penyebab") String penyebab,
            @Field("kondisi") String kondisi,
            @Header("Authorization") String authToken
    );

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @DELETE("kematian/{id}")
    Call<DataDeleteResponse> delKematian(@Header("Authorization") String authToken, @Path("id") Integer id);


    // ------------------------------------data pemilik--------------------------------------------
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("pemilik")
    Call<PemilikGetResponse> getPemilik(@Header("Authorization") String authToken);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("pemilik/{id}")
    Call<PemilikResponse> getPemilikDetail(@Header("Authorization") String authToken, @Path("id") Integer id);

    @FormUrlEncoded
    @POST("pemilik")
    Call<PemilikResponse> addPemilik(
            @Field("ktp") String ktp,
            @Field("nama_pemilik") String namaPemilik,
            @Header("Authorization") String authToken
    );

    @FormUrlEncoded
    @PUT("pemilik/{id}")
    Call<PemilikResponse> editPemilik(
            @Path("id") Integer id,
            @Field("ktp") String ktp,
            @Field("nama_pemilik") String namaPemilik,
            @Header("Authorization") String authToken
    );

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @DELETE("pemilik/{id}")
    Call<DataDeleteResponse> delPemilik(@Header("Authorization") String authToken, @Path("id") Integer id);


    // ------------------------------------data penyakit--------------------------------------------
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("penyakit")
    Call<PenyakitGetResponse> getPenyakit(@Header("Authorization") String authToken);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("penyakit/{id}")
    Call<PenyakitResponse> getPenyakitDetail(@Header("Authorization") String authToken, @Path("id") Integer id);

    @FormUrlEncoded
    @POST("penyakit")
    Call<PenyakitResponse> addPenyakit(
            @Field("nama_penyakit") String nama,
            @Field("ket_penyakit") String ket,
            @Header("Authorization") String authToken
    );

    @FormUrlEncoded
    @PUT("penyakit/{id}")
    Call<PenyakitResponse> editPenyakit(
            @Path("id") Integer id,
            @Field("nama_penyakit") String nama,
            @Field("ket_penyakit") String ket,
            @Header("Authorization") String authToken
    );

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @DELETE("penyakit/{id}")
    Call<DataDeleteResponse> delPenyakit(@Header("Authorization") String authToken, @Path("id") Integer id);


    // ------------------------------------data ras--------------------------------------------
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("ras")
    Call<RasGetResponse> getRas(@Header("Authorization") String authToken);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("ras/{id}")
    Call<RasResponse> getRasDetail(@Header("Authorization") String authToken, @Path("id") Integer id);

    @FormUrlEncoded
    @POST("ras")
    Call<RasResponse> addRas(
            @Field("jenis_ras") String jenisRas,
            @Field("ket_ras") String ketRas,
            @Header("Authorization") String authToken
    );

    @FormUrlEncoded
    @PUT("ras/{id}")
    Call<RasResponse> editRas(
            @Path("id") Integer id,
            @Field("jenis_ras") String jenisRas,
            @Field("ket_ras") String ketRas,
            @Header("Authorization") String authToken
    );

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @DELETE("ras/{id}")
    Call<DataDeleteResponse> delRas(@Header("Authorization") String authToken, @Path("id") Integer id);


    // ------------------------------------data peternakan--------------------------------------------
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("peternakan")
    Call<PeternakanGetResponse> getPeternakan(@Header("Authorization") String authToken);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("peternakan/{id}")
    Call<PeternakanResponse> getPeternakanDetail(@Header("Authorization") String authToken, @Path("id") Integer id);

    @FormUrlEncoded
    @POST("peternakan")
    Call<PeternakanResponse> addPeternakan(
            @Field("nama_peternakan") String namaPeternakan,
            @Field("keterangan") String ket,
            @Header("Authorization") String authToken
    );

    @FormUrlEncoded
    @PUT("peternakan/{id}")
    Call<PeternakanResponse> editPeternakan(
            @Path("id") Integer id,
            @Field("nama_peternakan") String namaPeternakan,
            @Field("keterangan") String ket,
            @Header("Authorization") String authToken
    );

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @DELETE("peternakan/{id}")
    Call<DataDeleteResponse> delPeternakan(@Header("Authorization") String authToken, @Path("id") Integer id);


    // ------------------------------------data ternak--------------------------------------------
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("ternak")
    Call<TernakGetResponse> getTernak(@Header("Authorization") String authToken);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("ternak/{necktag}")
    Call<TernakResponse> getTernakDetail(@Header("Authorization") String authToken, @Path("necktag") String necktag);

    @FormUrlEncoded
    @POST("ternak")
    Call<TernakResponse> addTernak(
            @Field("pemilik_id") Integer pemilikId,
            @Field("peternakan_id") Integer peternakanId,
            @Field("ras_id") Integer rasId,
            @Field("kematian_id") Integer kematianId,
            @Field("jenis_kelamin") String jk,
            @Field("tgl_lahir") String tglLahir,
            @Field("bobot_lahir") String bobotLahir,
            @Field("pukul_lahir") String pukulLahir,
            @Field("lama_dikandungan") String lamaDiKandungan,
            @Field("lama_laktasi") String lamaLaktasi,
            @Field("tgl_lepas_sapih") String tglLepasSapih,
            @Field("blood") String blood,
            @Field("necktag_ayah") String ayah,
            @Field("necktag_ibu") String ibu,
            @Field("bobot_tubuh") String bobotTubuh,
            @Field("panjang_tubuh") String panjangTubuh,
            @Field("tingggi_tubuh") String tinggiTubuh,
            @Field("cacat_fisik") String cacatFisik,
            @Field("ciri_lain") String ciriLain,
            @Field("status_ada") Boolean statusAda,
            @Header("Authorization") String authToken
    );

    @FormUrlEncoded
    @PUT("ternak/{necktag}")
    Call<TernakResponse> editTernak(
            @Path("necktag") String necktag,
            @Field("pemilik_id") Integer pemilikId,
            @Field("peternakan_id") Integer peternakanId,
            @Field("ras_id") Integer rasId,
            @Field("kematian_id") Integer kematianId,
            @Field("jenis_kelamin") String jk,
            @Field("tgl_lahir") String tglLahir,
            @Field("bobot_lahir") String bobotLahir,
            @Field("pukul_lahir") String pukulLahir,
            @Field("lama_dikandungan") String lamaDiKandungan,
            @Field("lama_laktasi") String lamaLaktasi,
            @Field("tgl_lepas_sapih") String tglLepasSapih,
            @Field("blood") String blood,
            @Field("necktag_ayah") String ayah,
            @Field("necktag_ibu") String ibu,
            @Field("bobot_tubuh") String bobotTubuh,
            @Field("panjang_tubuh") String panjangTubuh,
            @Field("tingggi_tubuh") String tinggiTubuh,
            @Field("cacat_fisik") String cacatFisik,
            @Field("ciri_lain") String ciriLain,
            @Field("status_ada") Boolean statusAda,
            @Header("Authorization") String authToken
    );

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @DELETE("ternak/{necktag}")
    Call<DataDeleteResponse> delTernak(@Header("Authorization") String authToken, @Path("necktag") String necktag);
}
