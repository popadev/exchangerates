package com.roweb.exchangerates.util.interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("/latest?access_key=3d37713e478451fa4fc0be91f68efbc5")
    Call<JsonObject> getLatestRates();
}
