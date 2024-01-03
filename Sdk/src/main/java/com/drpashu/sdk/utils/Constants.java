package com.drpashu.sdk.utils;

import android.Manifest;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class Constants {
    public static final String PERMISSION_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERMISSION_ACCESS_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    public static final String REMOTE_CONFIG_ENABLE_PARAVET = "enable_paravet";
}
