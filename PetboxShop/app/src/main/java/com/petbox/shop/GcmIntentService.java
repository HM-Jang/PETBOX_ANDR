package com.petbox.shop;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.petbox.shop.action.FOO";
    private static final String ACTION_BAZ = "com.petbox.shop.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.petbox.shop.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.petbox.shop.extra.PARAM2";

    public static final int NOTIFICATION_ID = 1;
    SharedPreferences sp;

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, GcmIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, GcmIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onHandleIntent(Intent intent)
    {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty())
        { // has effect of unparcelling Bundle
         /*
          * Filter messages based on message type. Since it is likely that GCM
          * will be extended in the future with new message types, just ignore
          * any message types you're not interested in, or that you don't
          * recognize.
          */
            String push_set = PreferenceUtil.instance(getApplicationContext()).push();

            sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            boolean start = false;

            if(sp.getBoolean("PUSH", false)) {

                //id , title , start , end , age , type
                if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                    sendNotification("Send error: " + extras.toString(), "");
                } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                    sendNotification("Deleted messages on server: " + extras.toString(), "");
                    // If it's a regular GCM message, do some work.
                } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                    String msg = intent.getStringExtra("msg");
                    String id_event = msg.split(",")[0];
                    String title = msg.split(",")[1];
                    String date = "기간 : " + msg.split(",")[2] + "~" + msg.split(",")[3];
                    int age =  Integer.parseInt(msg.split(",")[4]);
                    int type =  Integer.parseInt(msg.split(",")[5]);

                    if(age == 1 && sp.getBoolean("PUSH_EL", false) == true){
                        start = true;
                    }if(age == 2 && sp.getBoolean("PUSH_MI", false) == true){
                        start = true;
                    }if(age == 3 && sp.getBoolean("PUSH_HI", false) == true){
                        start = true;
                    }if(age == 4 && sp.getBoolean("PUSH_AD", false) == true){
                        start = true;
                    }if(age == 5 && sp.getBoolean("PUSH_ET", false) == true){
                        start = true;
                    }
                    // Post notification of received message.
                    // sendNotification("Received: " + extras.toString());
                    if(start == true){
                        sendNotification(title, date);
                    }
                    Log.i("GcmIntentService.java | onHandleIntent", "Received: " + title);
                }


            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadCastReceiver.WakefulIntent(intent);
    }
    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String title,String date)
    {

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("msg", title);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                .setContentText(date)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 500});

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
