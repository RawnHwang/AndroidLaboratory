package me.hwang.picprocessing.manager;


import android.app.Activity;
import android.view.Window;

public class WindowManager {

    /**
     * 设置窗口透明度
     * @param activity
     * @param bgAlpha
     */
    public static void setBackgroundAlpha(Activity activity,float bgAlpha) {
        Window window = activity.getWindow();
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        //0.0-1.0
        lp.alpha = bgAlpha;
        window.setAttributes(lp);
    }
}
