package com.roweb.exchangerates.util.services;

import com.roweb.exchangerates.util.interfaces.ApiInterface;
import com.roweb.exchangerates.util.interfaces.StatusInterface;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExchangeRatesApi {

    private static ApiInterface apiInterface;
    private static ExchangeRatesApi exchangeRatesApi;

    public static ExchangeRatesApi getInstance() {
        if (exchangeRatesApi == null) {
            exchangeRatesApi = new ExchangeRatesApi();
        }
        return exchangeRatesApi;
    }

    private ExchangeRatesApi() {
        apiInterface = ClientService.create(ApiInterface.class);
    }

    // Get latest exchange rates
    public void getLatest(final StatusInterface statusInterface) {
        apiInterface.getLatestRates().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    statusInterface.success(response.body());
                } else {
                    statusInterface.fail("Internal Problem " + response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable erThrowable) {
                statusInterface.fail("Server Problem " + erThrowable.getMessage());
            }
        });
    }
}
