package com.drpashu.lite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.drpashu.lite.databinding.ActivityMainBinding;
import com.drpashu.sdk.connection.DrPashuApp;
import com.drpashu.sdk.connection.DrPashuSdk;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private DrPashuSdk drPashuSdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drPashuSdk = DrPashuApp.getDrPashuApp(this.getApplication(), "test_3105", "123456aabb");

        binding.button.setOnClickListener(v -> {
            JSONObject jsonObject = new JSONObject();
            try {
//                    jsonObject.put("api_key", "sdk_dehaat_key_0507");
                    jsonObject.put("api_key", "graamik_2111_test");
                    jsonObject.put("first_name", "Sahil");
                    jsonObject.put("last_name", "Chugh");
                    jsonObject.put("phone_number", "+918929329120");
                    jsonObject.put("language", "en");
                    jsonObject.put("gender", "0");

                    jsonObject.put("screen", "consultDoctor");
                    jsonObject.put("animal", "Cow");

//                jsonObject.put("device_id", "test");
//                jsonObject.put("gender", "0");
//                jsonObject.put("location", "India APP");
//                jsonObject.put("country", "India");
//                jsonObject.put("state", "Haryana");
//                jsonObject.put("district", "Palwal");
//                jsonObject.put("pincode", "121102");
                // Cat Dog Sheep Goat Pig Buffalo Cow Chicken

                startActivity(drPashuSdk.openSdk(this, jsonObject));
            } catch (JSONException e) {
                Toast.makeText(this, "Error Loading Sdk", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                drPashuSdk.triggerNotification("s", "d", new JSONObject());
            }

        });

        binding.buttonC.setOnClickListener(v -> {
            JSONObject jsonObject = new JSONObject();
            try {
//                jsonObject.put("api_key", "sdk_dehaat_key_0507");
                jsonObject.put("api_key", "graamik_2111_test");
                jsonObject.put("first_name", "Sahil");
                jsonObject.put("last_name", "Chugh");
                jsonObject.put("phone_number", "+918929329120");
                jsonObject.put("language", "en");
                jsonObject.put("gender", "0");

                jsonObject.put("screen", "callHistory");
                jsonObject.put("animal", "Cow");

//                jsonObject.put("device_id", "test");
//                jsonObject.put("gender", "0");
//                jsonObject.put("location", "India APP");
//                jsonObject.put("country", "India");
//                jsonObject.put("state", "Haryana");
//                jsonObject.put("district", "Palwal");
//                jsonObject.put("pincode", "121102");
                // Cat Dog Sheep Goat Pig Buffalo Cow Chicken

                startActivity(drPashuSdk.openSdk(this, jsonObject));
            } catch (JSONException e) {
                Toast.makeText(this, "Error Loading Sdk", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        });
    }
}