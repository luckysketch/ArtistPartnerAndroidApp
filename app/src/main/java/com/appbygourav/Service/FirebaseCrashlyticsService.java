package com.appbygourav.Service;

import android.os.Build;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class FirebaseCrashlyticsService {

    private  final static String ENV_CUSTOM_KEY = "ENV";
    private  final static String ANDROID_VERSION_CUSTOM_KEY = "ANDROID_VERSION";
    private  final static String API_LEVEL_CUSTOM_KEY = "API_LEVEL";
    private  final static String ACTIVITY_NAME_CUSTOM_KEY = "ACTIVITY_NAME";
    private  final static String METHOD_NAME_CUSTOM_KEY = "METHOD_NAME";
    private  final static String ACTION_VALUE_CUSTOM_KEY = "METHOD_NAME";
    private  final static String ACTION_CATEGORY_CUSTOM_KEY  = "ACTION_CATEGORY";

    static public void initializeCrashlyticsCustomKey(){
        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
        crashlytics.setCustomKey(ENV_CUSTOM_KEY,"dev");
        crashlytics.setCustomKey(ANDROID_VERSION_CUSTOM_KEY, Build.VERSION.RELEASE);
        crashlytics.setCustomKey(API_LEVEL_CUSTOM_KEY, Build.VERSION.SDK_INT);
    }
    private static void setDefaultCustomKey(){
        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
        crashlytics.setCustomKey(ACTIVITY_NAME_CUSTOM_KEY, "");
        crashlytics.setCustomKey(METHOD_NAME_CUSTOM_KEY, "");
        crashlytics.setCustomKey(ACTION_VALUE_CUSTOM_KEY, "");
        crashlytics.setCustomKey(ACTION_CATEGORY_CUSTOM_KEY, "");
    }


    static public void fireExceptionEvent(Exception e,String activityName,String methodName,String actionValue, String actionCat){
        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
        crashlytics.setCustomKey(ACTIVITY_NAME_CUSTOM_KEY, activityName);
        crashlytics.setCustomKey(METHOD_NAME_CUSTOM_KEY, methodName);
        crashlytics.setCustomKey(ACTION_VALUE_CUSTOM_KEY, actionValue);
        crashlytics.setCustomKey(ACTION_CATEGORY_CUSTOM_KEY, actionCat);
        crashlytics.recordException(e);
        setDefaultCustomKey();

    }

}
