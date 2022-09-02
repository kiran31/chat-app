package com.example.mychatapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class NotificationActivity extends AppCompatActivity {

    Button btnNotify;
    public final String CHANNEL_ID = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        btnNotify = findViewById(R.id.btnNotify);
        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    startNotification();
                }

            }
        });



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startNotification() {

        Intent intent = new Intent(NotificationActivity.this, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(NotificationActivity.this, 0, intent, 0);


        Intent actionIntent = new Intent(this, Receiver.class);
        actionIntent.putExtra("toast","This is notification");
        PendingIntent actionPending = PendingIntent.getBroadcast(this, 0, actionIntent, 0);

        Notification.Action action = new Notification.Action.Builder(Icon.createWithResource(this, R.drawable.ic_baseline_person_24), "Toast message",
                actionPending).build();

        Intent dismiss = new Intent(this, DismissReceiver.class);
        PendingIntent dismissPending = PendingIntent.getBroadcast(this, 0, dismiss, 0);
        Notification.Action dismissAction = new Notification.Action.Builder(Icon.createWithResource(this, R.drawable.ic_baseline_person_24), "Dismiss",
                dismissPending).build();


        NotificationChannel channel = null;
        channel = new NotificationChannel(CHANNEL_ID, "1", NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_person_24);
        String text = getResources().getString(R.string.text);

        Notification.Builder builder = new Notification.Builder(NotificationActivity.this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_baseline_person_24)
                .setContentTitle("Title")
                .setContentText("msg")
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(action)
                .setColor(Color.BLUE)
                .setLargeIcon(icon)
                .setStyle(new Notification.BigPictureStyle().bigPicture(icon))
                .addAction(dismissAction);

        NotificationManagerCompat compat = NotificationManagerCompat.from(NotificationActivity.this);
        compat.notify(1, builder.build());
    }


    public void alrmNotification(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 35);
        calendar.set(Calendar.SECOND, 0);

        Intent i = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent intent = PendingIntent.getBroadcast(getApplicationContext(), 100, i, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, intent);
    }

}