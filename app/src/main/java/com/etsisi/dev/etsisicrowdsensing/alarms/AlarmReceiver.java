package com.etsisi.dev.etsisicrowdsensing.alarms;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.etsisi.dev.etsisicrowdsensing.ParcelableUtil;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.CampusFragment;
import com.etsisi.dev.etsisicrowdsensing.model.Event;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    public static final int REMINDER_KIND = 0;
    public static final int COMPLETED_KIND = 1;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"Received intent in BroadcastReceiver.");

        int kind = intent.getIntExtra(CampusFragment.ALARM_KIND_EXTRA, -1);

        Log.i(TAG,"Control.");



        ComponentName comp;

        switch(kind){
            case REMINDER_KIND:
                //This will send a notification message and show notification in notification tray
                comp = new ComponentName(context.getPackageName(),
                        EventNotificationService.class.getName());
                startWakefulService(context, (intent.setComponent(comp)));
                break;
            case COMPLETED_KIND:
                comp = new ComponentName(context.getPackageName(),
                        CompletedEventAlarmService.class.getName());
                startWakefulService(context, (intent.setComponent(comp)));
        }





        // This is the Intent to deliver to our service.
        //Intent service = new Intent(context, EventNotificationService.class);
        //startWakefulService(context, service);
    }


}