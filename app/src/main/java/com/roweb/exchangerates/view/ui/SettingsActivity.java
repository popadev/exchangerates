package com.roweb.exchangerates.view.ui;

import android.os.Bundle;
import android.view.MenuItem;

import com.roweb.exchangerates.R;
import com.roweb.exchangerates.databinding.ActivitySettingsBinding;
import com.roweb.exchangerates.viewmodel.SettingsViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setSubtitle("Settings");
    }

    private void initDataBinding(Bundle savedInstanceState) {
        ActivitySettingsBinding settingsBindingBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        SettingsViewModel settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        if (savedInstanceState == null) {
            settingsViewModel.init();
        }
        settingsBindingBinding.setSettingsVm(settingsViewModel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
