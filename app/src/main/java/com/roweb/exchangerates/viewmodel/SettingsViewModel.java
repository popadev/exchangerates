package com.roweb.exchangerates.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.roweb.exchangerates.util.Constants;

public class SettingsViewModel extends AndroidViewModel implements AdapterView.OnItemSelectedListener {

    private static final Integer[] times = {3, 5, 15};

    private ArrayAdapter<Integer> adapter;
    private Context context;
    public ObservableField<Integer> selection;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void init() {
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, times);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Get saved settings
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int refreshTime = sharedPreferences.getInt(Constants.PREF_REFRESH_TIME, Constants.DEFAULT_REFRESH_TIME);
        selection = new ObservableField<>(adapter.getPosition(refreshTime));
    }

    /* Save Settings */
    public void saveSettings() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constants.PREF_REFRESH_TIME, adapter.getItem(selection.get()));
        editor.apply();
    }

    public ArrayAdapter<Integer> getAdapter() {
        return adapter;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        selection.set(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
