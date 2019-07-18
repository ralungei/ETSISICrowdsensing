package com.etsisi.dev.etsisicrowdsensing.alarms;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.etsisi.dev.etsisicrowdsensing.GlobalNotificationBuilder;
import com.etsisi.dev.etsisicrowdsensing.MainActivity;
import com.etsisi.dev.etsisicrowdsensing.NotificationUtil;
import com.etsisi.dev.etsisicrowdsensing.NotificationsDatabase;
import com.etsisi.dev.etsisicrowdsensing.ParcelableUtil;
import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.CampusFragment;
import com.etsisi.dev.etsisicrowdsensing.model.Event;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sonu on 10/04/17.
 */

public class EventNotificationService extends IntentService {

    private static final String TAG = "EventNotifService";

    private final int ENTREGA_KIND = 0;
    private final int EXAMEN_KIND = 1;
    private final int PRESENTACION_KIND = 2;

    private NotificationManagerCompat mNotificationManagerCompat;

    //Notification ID for Alarm
    public static final int NOTIFICATION_ID = 1;

    public EventNotificationService() {
        super("EventNotificationService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");

        mNotificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        // Send notification

        // Retrieve event
        Event event = ParcelableUtil.unmarshall(intent.getExtras().getByteArray(CampusFragment.EVENT_EXTRA), Event.CREATOR);

        showEventNotification(event);

    }

    //handle notification
    private void showEventNotification(Event event) {
        Log.d(TAG, "generateBigTextStyleNotification()");

        String kind = "";
        switch (event.getKind()){
            case ENTREGA_KIND:
                kind = "Entrega";
                break;
            case EXAMEN_KIND:
                kind = "Examen";
                break;
            case PRESENTACION_KIND:
                kind = "Presentación";
                break;

        }



        // Main steps for building a BIG_TEXT_STYLE notification:
        //      0. Get your data
        //      1. Create/Retrieve Notification Channel for O and beyond devices (26+)
        //      2. Build the BIG_TEXT_STYLE
        //      3. Set up main Intent for notification
        //      4. Create additional Actions for the Notification
        //      5. Build and issue the notification

        // 0. Get your data (everything unique per Notification).
        NotificationsDatabase.BigTextStyleReminderAppData bigTextStyleReminderAppData =
                NotificationsDatabase.getBigTextStyleData();

        // 1. Create/Retrieve Notification Channel for O and beyond devices (26+).
        String notificationChannelId =
                NotificationUtil.createNotificationChannel(this, bigTextStyleReminderAppData);


        // 2. Build the BIG_TEXT_STYLE.
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                // Overrides ContentText in the big form of the template.
                .bigText("¡Tienes " + kind.toLowerCase() + " de la asignatura de " + event.getSubject() + " en menos de 30 minutos!")
                // Overrides ContentTitle in the big form of the template.
                .setBigContentTitle("¡Tienes " + kind.toLowerCase() + " de la asignatura de " + event.getSubject() + " en menos de 30 minutos!" )
                .setBigContentTitle(kind + " de " + event.getSubject())
                // Summary line after the detail section in the big form of the template.
                // Note: To improve readability, don't overload the user with info. If Summary Text
                // doesn't add critical information, you should skip it.
                .setSummaryText(kind);


        // 3. Set up main Intent for notification.
        Intent mainIntent = new Intent(this, MainActivity.class);

        // When creating your Intent, you need to take into account the back state, i.e., what
        // happens after your Activity launches and the user presses the back button.

        // There are two options:
        //      1. Regular activity - You're starting an Activity that's part of the application's
        //      normal workflow.

        //      2. Special activity - The user only sees this Activity if it's started from a
        //      notification. In a sense, the Activity extends the notification by providing
        //      information that would be hard to display in the notification itself.

        // For the BIG_TEXT_STYLE notification, we will consider the activity launched by the main
        // Intent as a special activity, so we will follow option 2.

        // For an example of option 1, check either the MESSAGING_STYLE or BIG_PICTURE_STYLE
        // examples.

        // For more information, check out our dev article:
        // https://developer.android.com/training/notify-user/navigation.html


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack.
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent to the top of the stack.
        stackBuilder.addNextIntent(mainIntent);
        // Gets a PendingIntent containing the entire back stack.

        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        // 5. Build and issue the notification.

        // Because we want this to be a new notification (not updating a previous notification), we
        // create a new Builder. Later, we use the same global builder to get back the notification
        // we built here for the snooze action, that is, canceling the notification and relaunching
        // it several seconds later.

        // Notification Channel Id is ignored for Android pre O (26).
        NotificationCompat.Builder notificationCompatBuilder =
                new NotificationCompat.Builder(
                        getApplicationContext(), notificationChannelId);

        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);

        Notification notification = notificationCompatBuilder
                // BIG_TEXT_STYLE sets title and content for API 16 (4.1 and after).
                .setStyle(bigTextStyle)
                // Title for API <16 (4.0 and below) devices.
                 .setContentTitle(kind + " de " + event.getSubject())
                // Content for API <24 (7.0 and below) devices.
                .setContentText(kind + " de " + event.getSubject() + " en menos de 30 minutos.")
                .setSmallIcon(R.drawable.ic_campus_miniature)
                .setLargeIcon(BitmapFactory.decodeResource(
                        getResources(),
                        R.drawable.ic_campus_miniature))
                .setContentIntent(notifyPendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                // Set primary color (important for Wear 2.0 Notifications).
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))

                // SIDE NOTE: Auto-bundling is enabled for 4 or more notifications on API 24+ (N+)
                // devices and all Wear devices. If you have more than one notification and
                // you prefer a different summary notification, set a group key and create a
                // summary notification via
                // .setGroupSummary(true)
                // .setGroup(GROUP_KEY_YOUR_NAME_HERE)

                .setCategory(Notification.CATEGORY_REMINDER)

                // Sets priority for 25 and below. For 26 and above, 'priority' is deprecated for
                // 'importance' which is set in the NotificationChannel. The integers representing
                // 'priority' are different from 'importance', so make sure you don't mix them.
                .setPriority(bigTextStyleReminderAppData.getPriority())

                // Sets lock-screen visibility for 25 and below. For 26 and above, lock screen
                // visibility is set in the NotificationChannel.
                .setVisibility(bigTextStyleReminderAppData.getChannelLockscreenVisibility())

                .build();

        mNotificationManagerCompat.notify(GlobalNotificationBuilder.generateId(), notification);
    }


}