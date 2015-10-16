package com.petbox.shop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import static android.support.v4.content.WakefulBroadcastReceiver.completeWakefulIntent;
import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

public class GcmBroadCastReceiver extends BroadcastReceiver {
    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        for (String key : bundle.keySet())
        {
            Object value = bundle.get(key);
        }
        Log.i("GcmBroadcastReceiver.java | onReceive", "|" + "=================" + "|");

        ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
        startWakefulService(context, intent.setComponent(comp));
        setResultCode(Activity.RESULT_OK);
    }

    public static void WakefulIntent(Intent intent) {
        completeWakefulIntent(intent);
    }
}
