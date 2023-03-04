package com.drpashu.sdk.connection;

import android.app.Activity;
import android.content.Intent;


import org.json.JSONException;
import org.json.JSONObject;

public interface DrPashuSdk {

    Intent openSdk(Activity activity, JSONObject jsonObject) throws JSONException;
}
