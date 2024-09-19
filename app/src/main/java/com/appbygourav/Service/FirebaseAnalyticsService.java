package com.appbygourav.Service;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseAnalyticsService {

    private FirebaseAnalytics firebaseAnalytics;

    private  final static String EVENT_NAME = "event_name";
    private  final static String EVENT_CATEGORY = "event_category";
    private  final static String EVENT_VALUE = "event_value";
    private  final static String EVENT_ACTION = "event_action";

    public FirebaseAnalyticsService(Context context){
        this.firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }
    public void logScreenView(String screenName, String screenClass) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }

    public void logEvent(String eventName, Bundle params) {
        firebaseAnalytics.logEvent(eventName, params);
    }

    public void logCustomEvent(String screenName,String ea,String ev){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(EVENT_ACTION, ea);
        bundle.putString(EVENT_VALUE, ev);

        firebaseAnalytics.logEvent("custom_event", bundle);
    }
}
