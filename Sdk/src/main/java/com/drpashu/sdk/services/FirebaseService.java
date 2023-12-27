package com.drpashu.sdk.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.drpashu.sdk.utils.NotificationHelper;


import java.util.Random;

public class FirebaseService{
    public void onMessageReceived(Context context,String title, String description) {
        NotificationCompat.Builder notificationBuilder;
        NotificationHelper notificationHelper = new NotificationHelper(context);
            notificationBuilder = notificationHelper.getNotification(title, description, "", "chat", "");
            notificationHelper.notify(new Random().nextInt(900) + 100, notificationBuilder);
        }
}