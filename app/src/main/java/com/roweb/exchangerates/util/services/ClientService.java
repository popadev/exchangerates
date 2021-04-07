package com.roweb.exchangerates.util.services;

import com.roweb.exchangerates.util.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ClientService {

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    static <S> S create(Class<S> service) {
        return retrofit.create(service);
    }
}
