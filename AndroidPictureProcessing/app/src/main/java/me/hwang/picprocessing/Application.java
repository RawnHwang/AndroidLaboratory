package me.hwang.picprocessing;

import android.content.Context;

public class Application extends android.app.Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
