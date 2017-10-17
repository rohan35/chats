package com.raydevelopers.sony.chatdetails.service;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by SONY on 16-10-2017.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationService extends NotificationListenerService{
    Context context;

    @Override

    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();

    }
    @Override

    public void onNotificationPosted(StatusBarNotification sbn) {


        String pack = sbn.getPackageName();
        if(pack.contains("com.whatsapp"))
        {
        String ticker = sbn.getNotification().tickerText.toString();
        Bundle extras = sbn.getNotification().extras;


       /* ArrayList<StatusBarNotification> groupedNotifications = new ArrayList<>();

        for(StatusBarNotification statusBarNotification : getActiveNotifications()) {
            if(statusBarNotification.getNotification().getGroup().equals(sbn.getGroupKey())) {
                groupedNotifications.add(statusBarNotification);
            }
        }

        CharSequence stackNotificationMultiLineText[]
                = groupedNotifications.get(0).getNotification()
                .extras.getCharSequenceArray(NotificationCompat.EXTRA_TEXT_LINES);
        System.out.println(stackNotificationMultiLineText);*/
        String title = extras.getString(Notification.EXTRA_TITLE).toString();
        String text = extras.getCharSequence(Notification.EXTRA_TEXT).toString();
        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("package", pack);
        msgrcv.putExtra("ticker", ticker);
        msgrcv.putExtra("title", title);
        msgrcv.putExtra("text", text);

        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);}
    }

    @Override

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");

    }
}
