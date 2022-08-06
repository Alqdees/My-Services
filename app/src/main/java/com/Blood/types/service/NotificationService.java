package com.Blood.types.service;

import android.annotation.SuppressLint;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.Blood.types.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class NotificationService extends FirebaseMessagingService {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {

        String title = message.getNotification().getTitle();
        String text = message.getNotification().getBody();
        final String Channel_ID = "HEADS_UP_NITRIFICATION";
        NotificationChannel channel = new NotificationChannel(
                Channel_ID,
                "heads up notification",
                NotificationManager.IMPORTANCE_HIGH
        );
        getSystemService(NotificationManager.class)
                .createNotificationChannel(channel);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(
                this,Channel_ID).setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(1,notification.build());
        super.onMessageReceived(message);

    }
}
