package com.example.ekz.network;

import com.example.ekz.models.FeelingsResponse;
import com.example.ekz.models.LoginModel;
import com.example.ekz.models.LoginResponse;
import com.example.ekz.models.QuotesResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.POST;

public interface JSONPlaceHolderApi {
    @POST("user/login")
    Call<LoginResponse> login(@Body LoginModel loginModel);

    @GET("feelings")
    Call<FeelingsResponse> feelings();

    @GET("quotes")
    Call<QuotesResponse> quotes();
}
