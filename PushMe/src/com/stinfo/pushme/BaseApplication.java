package com.stinfo.pushme;

import android.app.Application;
import android.content.pm.PackageManager.NameNotFoundException;

public class BaseApplication extends Application {
    private static BaseApplication mInstance;
    private static int mAppVer = 0;
    
    public int getAppVersion() {
        return mAppVer;
    }
    
    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        
        try {
            mAppVer = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
