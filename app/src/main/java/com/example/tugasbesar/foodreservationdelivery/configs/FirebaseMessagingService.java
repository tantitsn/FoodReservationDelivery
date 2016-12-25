package com.example.tugasbesar.foodreservationdelivery.configs;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.tugasbesar.foodreservationdelivery.MainActivity;
import com.example.tugasbesar.foodreservationdelivery.R;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by amarullah87 on 04/07/16.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        showNotification (remoteMessage.getData().get("message"), remoteMessage.getData().get("meja"));
    }

    private void showNotification(String message, String meja) {

        Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Uri notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Customer Request")
                .setContentText("Meja " + meja + " membutuhkan: " + message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon1);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(message)
                .setBigContentTitle("Customer Request");
        builder.setStyle(bigText);
        builder.setSound(notifSound);


        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("notif", message);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(i);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
