package com.project.siternak.rest;

import com.project.siternak.responses.KematianResponse;
import com.project.siternak.responses.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("kematian")
    Call<KematianResponse> getKematian();

    @POST("login")
    @FormUrlEncoded
    Call<LoginResponse> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );


}
