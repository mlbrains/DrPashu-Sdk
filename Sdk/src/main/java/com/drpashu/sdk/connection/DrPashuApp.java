package com.drpashu.sdk.connection;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.drpashu.sdk.activity.HomeActivity;
import com.drpashu.sdk.network.Networking;
import com.drpashu.sdk.utils.NotificationHelper;
import com.drpashu.sdk.utils.PreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class DrPashuApp implements DrPashuSdk{
    private static DrPashuApp drPashuApp;
    private Application application;
    private NotificationHelper notificationHelper;
    private PreferenceUtils preferenceUtils;

    public DrPashuApp(Application application) {
        this.application = application;
        notificationHelper = new NotificationHelper(application.getApplicationContext());
        preferenceUtils = new PreferenceUtils(application.getApplicationContext());

        if(!preferenceUtils.getCountStatus()){
            new Networking(application.getApplicationContext()).recordUser("test_3105", "1200");
        }

    }

    public static DrPashuSdk getDrPashuApp(Application application) {
        if (drPashuApp == null)
            drPashuApp = new DrPashuApp(application);
        return drPashuApp;
    }

    @Override
    public void triggerNotification(String title, String description, JSONObject jsonObject){
        notificationHelper.triggerNotification(title, description, jsonObject);
    }

    @Override
    public Intent openSdk(Activity activity, JSONObject jsonObject) throws JSONException {
        Intent intent = new Intent(activity, HomeActivity.class);
        intent.putExtra("sdk", jsonObject.toString());
        intent.putExtra("screen", jsonObject.getString("screen"));
        return intent;
    }
}
