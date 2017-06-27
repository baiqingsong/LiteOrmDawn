package com.dawn.liteormdawn;

import android.app.Application;
import android.content.Context;

/**
 * Created by 90449 on 2017/6/27.
 */

public class BaseApplication extends Application {

    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = getAppContext();
    }

    public static Context getAppContext(){
        return mContext;
    }
}
