package com.project.siternak.rest;

import com.project.siternak.responses.KematianDeleteResponse;
import com.project.siternak.responses.KematianResponse;
import com.project.siternak.responses.KematianGetResponse;
import com.project.siternak.responses.LoginResponse;
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
    @POST("login")
    @FormUrlEncoded
    Call<LoginResponse> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("profile")
    Call<UserDetailsResponse> userDetails(@Header("Authorization") String authToken);

    // ------------------------------------data kematian--------------------------------------------
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("kematian")
    Call<KematianGetResponse> getKematian(@Header("Authorization") String authToken);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("kematian/{id}")
    Call<KematianResponse> getKematianDetail(
            @Header("Authorization") String authToken,
            @Path("id") Integer id
    );

    @FormUrlEncoded
    @POST("kematian")
    Call<KematianResponse> addKematian(
            @Header("Authorization") String authToken,
            @Field("tgl_kematian") String tglKematian,
            @Field("waktu_kematian") String waktuKematian,
            @Field("penyebab") String penyebab,
            @Field("kondisi") String kondisi
    );

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @FormUrlEncoded
    @PUT("kematian/{id}")
    Call<KematianResponse> editKematian(
            @Header("Authorization") String authToken,
            @Path("id") Integer id,
            @Field("tgl_kematian") String tglKematian,
            @Field("waktu_kematian") String waktuKematian,
            @Field("penyebab") String penyebab,
            @Field("kondisi") String kondisi
        );

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @DELETE("kematian/{id}")
    Call<KematianDeleteResponse> delKematian(
            @Header("Authorization") String authToken,
            @Path("id") Integer id
    );


}
