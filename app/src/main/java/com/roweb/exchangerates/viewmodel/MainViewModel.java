package com.roweb.exchangerates.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.roweb.exchangerates.model.ExchangeRate;
import com.roweb.exchangerates.model.AllExchangeRates;
import com.roweb.exchangerates.util.Constants;
import com.roweb.exchangerates.util.interfaces.StatusInterface;
import com.roweb.exchangerates.util.services.ExchangeRatesApi;
import com.roweb.exchangerates.view.adapters.RateAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    public ObservableInt noInternet;
    public ObservableField<String> baseCurrency;
    public ObservableField<Integer> flagResource;
    public ObservableField<String> timestamp;
    private RateAdapter adapter;
    private AllExchangeRates allExchangeRates;
    private Context context;
    private Handler handler = new Handler();
    private Runnable runnable;

    public MainViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void init() {
        noInternet = new ObservableInt(View.GONE);
        baseCurrency = new ObservableField<>();
        flagResource = new ObservableField<>();
        timestamp = new ObservableField<>();
        adapter = new RateAdapter(context);

        // Check local cache
        JsonObject cachedData = getData();
        if (cachedData != null)
            updateView(cachedData);

        callApi();
    }

    public void startRefresh() {
        if (runnable != null)
            return;

        handler.post(runnable = () -> callApi());
    }

    public void stopRefresh() {
        handler.removeCallbacks(runnable);
        runnable = null;
    }

    /* Fetch data from API */
    private void callApi() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int refreshTime = sharedPreferences.getInt(Constants.PREF_REFRESH_TIME, Constants.DEFAULT_REFRESH_TIME) * 1000;

        // Check internet connection
        if (!isNetworkConnected()) {
            noInternet.set(View.VISIBLE);
            handler.postDelayed(runnable, refreshTime);
            return;
        }

        noInternet.set(View.GONE);

        ExchangeRatesApi exchangeRatesApi = ExchangeRatesApi.getInstance();
        exchangeRatesApi.getLatest(new StatusInterface() {
            @Override
            public void success(JsonObject jsonObject) {
                saveData(jsonObject);

                updateView(jsonObject);

                //Reload after few seconds
                handler.postDelayed(runnable, refreshTime);
            }

            @Override
            public void fail(String message) {
                Log.e("MAIN-CallApi", message);

            }
        });
    }

    /* Check if internet connection exist */
    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    /* Get data from local cache */
    private JsonObject getData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = sharedPreferences.getString(Constants.PREF_CACHED_DATA, null);
        if (jsonString == null)
            return null;
        return new Gson().fromJson(jsonString, JsonObject.class);
    }

    /* Save data to local */
    private void saveData(JsonObject jsonObject) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.PREF_CACHED_DATA, new Gson().toJson(jsonObject));
        editor.apply();
    }

    /* Refresh UI with given data */
    private void updateView(JsonObject jsonObject) {
        allExchangeRates = new Gson().fromJson(jsonObject, AllExchangeRates.class);

        // Set base currency name and flag image resource
        baseCurrency.set(allExchangeRates.getBase());
        flagResource.set(context.getResources().getIdentifier("ic_" + allExchangeRates.getBase().toLowerCase(),
                "drawable", context.getPackageName()));

        // Set timestamp
        timestamp.set(String.valueOf(allExchangeRates.getTimestamp()));

        // Get and set rate list
        AllExchangeRates.RatesBean ratesBean = allExchangeRates.getRates();
        setupExchangeList(ratesBean);
    }

    private void setupExchangeList(AllExchangeRates.RatesBean ratesBean) {
        List<ExchangeRate> exRateList = new ArrayList<>();
        exRateList.add(new ExchangeRate("AUD", ratesBean.getAUD()));
        exRateList.add(new ExchangeRate("BGN", ratesBean.getBGN()));
        exRateList.add(new ExchangeRate("BRL", ratesBean.getBRL()));
        exRateList.add(new ExchangeRate("CAD", ratesBean.getCAD()));
        exRateList.add(new ExchangeRate("CHF", ratesBean.getCHF()));
        exRateList.add(new ExchangeRate("CNY", ratesBean.getCNY()));
        exRateList.add(new ExchangeRate("CZK", ratesBean.getCZK()));
        exRateList.add(new ExchangeRate("DKK", ratesBean.getDKK()));
        exRateList.add(new ExchangeRate("GBP", ratesBean.getGBP()));
        exRateList.add(new ExchangeRate("HKD", ratesBean.getHKD()));
        exRateList.add(new ExchangeRate("HRK", ratesBean.getHRK()));
        exRateList.add(new ExchangeRate("HUF", ratesBean.getHUF()));
        exRateList.add(new ExchangeRate("IDR", ratesBean.getIDR()));
        exRateList.add(new ExchangeRate("ILS", ratesBean.getILS()));
        exRateList.add(new ExchangeRate("INR", ratesBean.getINR()));
        exRateList.add(new ExchangeRate("ISK", ratesBean.getISK()));
        exRateList.add(new ExchangeRate("JPY", ratesBean.getJPY()));
        exRateList.add(new ExchangeRate("KRW", ratesBean.getKRW()));
        exRateList.add(new ExchangeRate("MXN", ratesBean.getMXN()));
        exRateList.add(new ExchangeRate("MYR", ratesBean.getMYR()));
        exRateList.add(new ExchangeRate("NOK", ratesBean.getNOK()));
        exRateList.add(new ExchangeRate("PHP", ratesBean.getPHP()));
        exRateList.add(new ExchangeRate("PLN", ratesBean.getPLN()));
        exRateList.add(new ExchangeRate("RON", ratesBean.getRON()));
        exRateList.add(new ExchangeRate("RUB", ratesBean.getRUB()));
        exRateList.add(new ExchangeRate("SEK", ratesBean.getSEK()));
        exRateList.add(new ExchangeRate("SGD", ratesBean.getSGD()));
        exRateList.add(new ExchangeRate("THB", ratesBean.getTHB()));
        exRateList.add(new ExchangeRate("TRY", ratesBean.getTRY()));
        exRateList.add(new ExchangeRate("USD", ratesBean.getUSD()));
        exRateList.add(new ExchangeRate("ZAR", ratesBean.getZAR()));

        MutableLiveData<List<ExchangeRate>> liveDataExRateList = new MutableLiveData<>();
        liveDataExRateList.postValue(exRateList);

        if (exRateList.size() > 0) {
            adapter.setItemList(exRateList);
            adapter.notifyDataSetChanged();
        }
    }

    public RateAdapter getAdapter() {
        return adapter;
    }

}
