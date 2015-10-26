package com.petbox.shop.Application;

import android.app.Application;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.petbox.shop.DB.Constants;
import com.petbox.shop.R;

import java.util.HashMap;

/**
 * Created by petbox on 2015-10-05.
 */
public class PetboxApplication extends Application {

    private Tracker mTracker;

    public enum TrackerName {
        APP_TRACKER,           // 앱 별로 트래킹
        GLOBAL_TRACKER,        // 모든 앱을 통틀어 트래킹
        ECOMMERCE_TRACKER,     // 아마 유료 결재 트래킹 개념 같음
    }

    @Override
    public void onCreate(){
        super.onCreate();

        //FlurryAgent.setLogEnabled(false);
        //FlurryAgent.init(this, Constants.FLURRY_APIKEY);

    }

    // 기본 구글 애널리틱스 코드
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);

            mTracker.enableAdvertisingIdCollection(true);
            mTracker.enableAutoActivityTracking(true);

        }

        return mTracker;
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(Constants.GOOGLE_TRACKING_CODE): (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker) : analytics.newTracker(R.xml.ecommerce_tracker);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }
}


    /*
    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public synchronized Tracker getTracker(TrackerName trackerId){
        if(!mTrackers.containsKey(trackerId)){
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(Constants.GOOGLE_TRACKING_CODE) :
                    (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker) :
                            analytics.newTracker(R.xml.ecommerce_tracker);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }
    */

