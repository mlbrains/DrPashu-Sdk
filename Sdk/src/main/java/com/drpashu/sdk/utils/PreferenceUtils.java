package com.drpashu.sdk.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

public class PreferenceUtils {
    private static final String TAG = "drPashuSdk";
    private Context context;

    public PreferenceUtils(Context context) {
        this.context = context;
    }

    private SharedPreferences getPrefrence() {
        return context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    public String getUserId() {
        return getPrefrence().getString("user-id", "");
    }

    public void setUserId(String token) {
        getPrefrence().edit().putString("user-id", token).apply();
    }

    public String getUsername(){
        return getPrefrence().getString("username","");
    }

    public void setUsername(String username){
        getPrefrence().edit().putString("username",username).apply();
    }
    public  void setBaseColor(int color){
        getPrefrence().edit().putInt("baseColor",color).apply();
    }
    public int getBaseColor(){
        return getPrefrence().getInt("baseColor", Color.BLACK);
    }
    public  void setDarkBaseColor(int color){ getPrefrence().edit().putInt("darkBaseColor",color).apply(); }
    public int getDarkBaseColor(){
        return getPrefrence().getInt("darkBaseColor", Color.BLACK);
    }

    public  void setLightBaseColor(int color){ getPrefrence().edit().putInt("lightBaseColor",color).apply(); }
    public int getLightBaseColor(){
        return getPrefrence().getInt("lightBaseColor", Color.BLACK);
    }

    public  void setLightCardColor(int color){ getPrefrence().edit().putInt("lightCardColor",color).apply(); }
    public int getLightCardColor(){ return getPrefrence().getInt("lightCardColor", Color.BLACK); }

    public Boolean getBlockNavigationStatus(){
        return getPrefrence().getBoolean("back_press", false);
    }

    public void setBlockNavigationStatus(Boolean navigationOption){
        getPrefrence().edit().putBoolean("back_press", navigationOption).apply();
    }

    public Boolean getCountStatus(){
        return getPrefrence().getBoolean("count", false);
    }

    public void setCountStatus(Boolean count){
        getPrefrence().edit().putBoolean("count", count).apply();
    }

    public String getFcmToken(){
        return getPrefrence().getString("fcm", "");
    }

    public void setFcmToken(String token){
        getPrefrence().edit().putString("fcm", token).apply();
    }

    public String getAnimal(){
        return getPrefrence().getString("animal", "Buffalo");
    }

    public void setAnimal(String animal){
        getPrefrence().edit().putString("animal", animal).apply();
    }
}