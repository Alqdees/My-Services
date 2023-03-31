package com.Blood.types.Controller;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.Blood.types.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class PushNotification extends FirebaseMessagingService {

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
               /*
                  https://medium.com/@info_67212/firebase-push-notification-with-action-button-in-flutter-a841da348097

                   this is url to show how to add action in fcm notification

                  */
        String title = message.getNotification().getTitle();
        String ms = message.getNotification().getBody();
        String click = message.getData().get("Action");

        NotificationChannel channel = new NotificationChannel(
                "Message",
                "message Notification",
                NotificationManager.IMPORTANCE_HIGH);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(
                this,
                "Message")
                .setContentTitle(title)
                .setContentText(ms)
                .setSmallIcon(R.drawable.ic_call)
                .setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(1, notification.build());

        super.onMessageReceived(message);

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

    }
}