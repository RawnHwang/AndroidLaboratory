package me.hwang.materialdesigntest.util;

import android.content.Context;

public class ScreenUtil {

    public static int dip2px(Context context,int dp){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (scale * dp + 0.5);
    }

    public static int px2dip(Context context,int px){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5);
    }
}
