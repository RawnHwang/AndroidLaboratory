package me.hwang.picprocessing.manager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

public class PopupWindowManager {

    public static PopupWindow createDefaultWinow(Context context, int layoutResID) {
        PopupWindow pw = new Builder(context)
                .setContentView(layoutResID)
                .setFocusable(true)
                .setTouchable(true)
                .setOutsideTouchable(true)
                .build();

        return pw;
    }

    public static class Builder {
        private PopupWindow mPopupWindow;
        private Context context;
        private View mContentView;
        private int mWindowWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        private int mWindowHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        private Drawable mBackground;
        private boolean mFocusable;
        private boolean mTouchable;
        private boolean mOutsideTouchable;

        public Builder(Context context) {
            this.context = context;
            this.mPopupWindow = new PopupWindow();
        }

        public Builder setContentView(View contentView) {
            if (contentView == null)
                throw new NullPointerException("contentView is null");

            this.mContentView = contentView;
            return this;
        }

        public Builder setContentView(int layoutResID) {
            mContentView = LayoutInflater.from(context).inflate(layoutResID, null);
            return this;
        }

        public Builder setWidth(int windowWidth) {
            if (windowWidth <= 0)
                throw new IllegalArgumentException("The witch must have to > 0");

            mWindowWidth = windowWidth;
            return this;
        }

        public Builder setHeight(int windowHeight) {
            if (windowHeight <= 0)
                throw new IllegalArgumentException("The height must have to > 0");

            mWindowHeight = windowHeight;
            return this;
        }

        public Builder setBackgroundDrawable(Drawable background) {
            if (background == null)
                throw new NullPointerException();

            mBackground = background;
            return this;
        }

        public Builder setFocusable(boolean focusable) {
            mFocusable = focusable;
            return this;
        }

        public Builder setTouchable(boolean touchable) {
            mTouchable = touchable;
            return this;
        }


        public Builder setOutsideTouchable(boolean outsideTouchable) {
            mOutsideTouchable = outsideTouchable;
            return this;
        }
        public PopupWindow build() {
            if (mContentView == null)
                throw new NullPointerException("contentView is null");

            mPopupWindow.setContentView(mContentView);
            mPopupWindow.setWidth(mWindowWidth);
            mPopupWindow.setHeight(mWindowHeight);

            if (mBackground != null)
                mPopupWindow.setBackgroundDrawable(mBackground);

            mPopupWindow.setFocusable(mFocusable);
            mPopupWindow.setTouchable(mTouchable);
            mPopupWindow.setOutsideTouchable(mOutsideTouchable);

            return mPopupWindow;
        }
    }

}
