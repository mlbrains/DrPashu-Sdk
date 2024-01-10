package com.drpashu.sdk.activity;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.drpashu.sdk.R;
import com.drpashu.sdk.databinding.ActivityHomeBinding;

public class HomeActivity extends BaseActivity {
    private ActivityHomeBinding binding;
    private NavController navController;

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

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.nav_home, true)
                .build();

        preferenceUtils.setBlockNavigationStatus(false);
        if (getIntent() != null) {
            if (getIntent().getExtras().getString("sdk") != null) {
                showLoading();
                networking.addUserFromSdk(getIntent().getExtras().getString("sdk"));
            } else if (getIntent().getExtras().getString("call").equalsIgnoreCase("true")) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("callIncoming", true);
                bundle.putBoolean("callRedial", false);
                bundle.putString("callInitiated", "Vet");
                bundle.putString("screen", "is_caller=");
                bundle.putString("callId", getIntent().getExtras().getString("callId") + "");
                bundle.putString("channelId", getIntent().getExtras().getString("channelId") + "");
                bundle.putString("firstName", getIntent().getExtras().getString("firstName") + "");
                bundle.putString("lastName", getIntent().getExtras().getString("lastName") + "");
                bundle.putInt("notificationId", Integer.parseInt(getIntent().getExtras().getString("notificationId") + ""));

                bundle.putString("profile_picture", getIntent().getExtras().getString("profile_picture") + "");
                bundle.putString("unixNotificationTime", getIntent().getExtras().getString("unixNotificationTime") + "");
                bundle.putString("language", getIntent().getExtras().getString("language"));
                bundle.putString("animal", getIntent().getExtras().getString("animal"));
                navController.navigate(R.id.callFragment, bundle, navOptions);
            } else if (getIntent().getExtras().getString("call").equalsIgnoreCase("false")) {
                if (getIntent().getExtras().getString("screen").equalsIgnoreCase("call_history"))
                    navController.navigate(R.id.nav_call_history, null, navOptions);
                else if (getIntent().getExtras().getString("screen").equalsIgnoreCase("prescription")
                        || getIntent().getExtras().getString("screen").equalsIgnoreCase("chat")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("callId", getIntent().getExtras().getString("callId") + "");
                    bundle.putString("screen", getIntent().getExtras().getString("screen") + "");
                    navController.navigate(R.id.nav_call_history, bundle, navOptions);
                }
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