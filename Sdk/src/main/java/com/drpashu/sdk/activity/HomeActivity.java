package com.drpashu.sdk.activity;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.drpashu.sdk.databinding.ActivityHomeBinding;

public class HomeActivity extends BaseActivity {
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e){
            Log.e("set toolbar error", e.getMessage()+"");
        }

        preferenceUtils.setBlockNavigationStatus(false);
        if (getIntent() != null) {
            if (getIntent().getExtras().getString("sdk") != null) {
                showLoading();
                networking.addUserFromSdk(getIntent().getExtras().getString("sdk"));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!preferenceUtils.getBlockNavigationStatus()) {
            super.onBackPressed();
        }
    }

    public void dismissLoader() {
        dismissLoading();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public <T> void networkingRequest(@Nullable MethodType methodType, boolean status, @Nullable T error, Object o) {
        if (methodType == MethodType.addUserFromSdk && status) {
//            dismissLoading();
            Intent intent = new Intent("screenNavigation");
            intent.putExtra("screen", getIntent().getExtras().getString("screen"));
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else if (methodType == MethodType.addUserFromSdk && !status) {
            dismissLoading();
            utils.shortToast("Error Loading DrPashu App");
            onBackPressed();
        }
    }
}