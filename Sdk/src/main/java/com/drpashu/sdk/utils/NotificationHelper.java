package com.drpashu.sdk.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.drpashu.sdk.R;
import com.drpashu.sdk.activity.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class NotificationHelper extends ContextWrapper {

    private NotificationManagerCompat notificationManager;
    private NotificationManager notificationDeviceManager;
    long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
                      500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
                      500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500};

    private static final String APP_CHANNEL_ID = "APP_NOTIFICATION";
    private static final String CALL_CHANNEL_ID = "CALL_NOTIFICATION";
    private static final String SILENT_CHANNEL_ID = "SILENT_NOTIFICATION";
    private static final String CALL_ONGOING_CHANNEL_ID = "CALL_ONGOING_NOTIFICATION";

    public NotificationHelper(Context context) {
        super(context);
        notificationManager = NotificationManagerCompat.from(context);
        notificationDeviceManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void createCallNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Uri sounduri;
            if (Build.MANUFACTURER.equalsIgnoreCase("realme") || Build.MANUFACTURER.equalsIgnoreCase("oneplus")) {
                sounduri = Uri.parse("android.resource://" +
                        getApplicationContext().getPackageName() +
                        "/" +
                        R.raw.call_ringtone);
            }
            else {
                sounduri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
            
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build();
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .build();

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CALL_CHANNEL_ID, "Call notifications", importance);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(pattern);
            notificationChannel.setSound(sounduri, audioAttributes);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void createCallOngoingNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CALL_ONGOING_CHANNEL_ID, "Ongoing call notifications", importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(APP_CHANNEL_ID, "Dr Pashu", importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public NotificationCompat.Builder getCallNotification(String title, String description, String callStatus, String callId, String notificationId, String channelId,
                                                          String firstName, String lastName, String profileImg, String unixNotificationTime, String language, String animal){
        createCallNotificationChannel();

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("call", callStatus);
        intent.putExtra("callId", callId);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("channelId", channelId);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        intent.putExtra("profile_picture", profileImg);
        intent.putExtra("unixNotificationTime", unixNotificationTime);
        intent.putExtra("language", language);
        intent.putExtra("animal", animal);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);  // Make click action send data, replace with 0

        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.call_notification_icon);

        return new NotificationCompat.Builder(getApplicationContext(), CALL_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setWhen(0)
                .setTimeoutAfter(45000) // after this time the notification auto dismiss
                .setAutoCancel(false) // will not dismiss notification on click
                .setOngoing(true) //notification will not be dismissed
                .setFullScreenIntent(pendingIntent, true);

//              .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//              .setStyle(new NotificationCompat.BigPictureStyle()       // used to display the expandable image inside notification
//              .bigPicture(icon))

        //Full screen intents allows us to show notification on the top of the screen
        //It will not remove until user's action
    }

    public NotificationCompat.Builder getNotification(String title, String description, String callStatus, String screen, String callId) {
        createNotificationChannel();

        if (screen.equalsIgnoreCase("call_history")){
            Intent intent = new Intent("missed_call");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("screen", screen);
        intent.putExtra("call", callStatus);
        intent.putExtra("callId", callId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(getApplicationContext(), APP_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(description)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
    }

    public NotificationCompat.Builder getSilentNotification(String title, String description) {
        Intent intent = new Intent("vet_call_decline");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        return new NotificationCompat.Builder(getApplicationContext(), SILENT_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(description)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    public void notify(int notificationId, NotificationCompat.Builder notification){ // notificationId is a unique int for each notification that you must define
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.notify(notificationId, notification.build());
        else
            notificationDeviceManager.notify(notificationId, notification.build());
    }

    public void triggerNotification(String title, String description, JSONObject jsonObject) {
        try {
            NotificationCompat.Builder notificationBuilder;

            Log.e("notificationInfo", "title " + title + ", desc " + description + ", Data - " + jsonObject.toString());
//        String title = jsonObject.getString()("title");
//        String description = jsonObject.getString()("description");
            String notificationId, callStatus;

            if (jsonObject.has("notificationId"))
                notificationId = jsonObject.getString("notificationId");
            else
                notificationId = "1";

            if (jsonObject.has("call"))
                callStatus = jsonObject.getString("call");
            else
                callStatus = "false";

            String channelId = "", firstName = "", lastName = "", callId = "", profileImg = "",
                    screen = "", unixTimeStamp = "", userLanguage = "", animal = "";
            callStatus += "";
            notificationId += "";

            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.ON_AFTER_RELEASE, "drpashu::WakeLock");

            if (callStatus.equalsIgnoreCase("true")) {
                wakeLock.acquire(45 * 1000L /*45 Seconds*/);

                callId = jsonObject.getString("callId");
                channelId = jsonObject.getString("channelId");
                firstName = jsonObject.getString("firstName");
                lastName = jsonObject.getString("lastName");
                userLanguage = jsonObject.getString("language");
                animal = jsonObject.getString("animal");
                unixTimeStamp = jsonObject.getString("unix_timestamp");

                long currentTime = System.currentTimeMillis();
                long notificationLongTime = Long.parseLong(unixTimeStamp);

                long differece = (currentTime - notificationLongTime) / 1000;

                Log.e("timeDifferenceLog", differece + "      " + unixTimeStamp);

                if (differece > 45)
                    return;

                if (jsonObject.has("profile_picture"))
                    profileImg = jsonObject.getString("profile_picture");
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("call", callStatus);
                intent.putExtra("callId", callId);
                intent.putExtra("notificationId", notificationId);
                intent.putExtra("channelId", channelId);
                intent.putExtra("firstName", firstName);
                intent.putExtra("lastName", lastName);
                intent.putExtra("profile_picture", profileImg);
                intent.putExtra("unixNotificationTime", unixTimeStamp);
                intent.putExtra("language", userLanguage);
                intent.putExtra("animal", animal);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                notificationBuilder = getCallNotification(title, description, callStatus,
                        callId, notificationId, channelId, firstName, lastName, profileImg, unixTimeStamp, userLanguage, animal);
            } else {
                wakeLock.acquire(10 * 1000L /*10 Seconds*/);

                screen = jsonObject.getString("screen");
                screen += "";

                if (screen.equalsIgnoreCase("vet_call_decline"))
                    notificationBuilder = getSilentNotification(title, description);
                else if (screen.equalsIgnoreCase("prescription")) {
                    callId = jsonObject.getString("callId");

                    notificationBuilder = getNotification(title, description, callStatus, screen, callId);
                } else if (screen.equalsIgnoreCase("call_history")) {
                    NotificationManagerCompat.from(getApplicationContext()).cancel(null, Integer.parseInt(notificationId));

                    notificationId = (Integer.parseInt(notificationId) + 1) + "";
                    notificationBuilder = getNotification(title, description, callStatus, screen, "");
                } else
                    notificationBuilder = getNotification(title, description, callStatus, screen, "");
            }

            notify(Integer.parseInt(notificationId), notificationBuilder);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}