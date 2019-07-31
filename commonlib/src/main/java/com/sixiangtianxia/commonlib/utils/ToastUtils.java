package com.sixiangtianxia.commonlib.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.sixiangtianxia.commonlib.MyApplication;
import com.sixiangtianxia.commonlib.R;

public class ToastUtils {

    private static Context context = MyApplication.Companion.getInstance().getApplicationContext();
    private static Toast toast;
    private static TextView sView;

    public static void show(int resId) {
        show(context.getResources().getText(resId), Toast.LENGTH_SHORT, -1);
    }

    public static void show(final CharSequence text, final int duration, final int gravity) {
        toShow(text, duration, gravity);

    }

    private static void toShow(CharSequence text, int duration, int gravity) {
        if (TextUtils.isEmpty(text)) {
            text = "操作失败";
        }
        if (toast != null) {
            toast.cancel();
            toast = null;
        }

        if (toast == null) {
            toast = new Toast(context);
            toast.setDuration(duration);
            View inflate = LayoutInflater.from(context).inflate(R.layout.toast, null);
            sView = inflate.findViewById(R.id.toast_tvtext);
            toast.setView(inflate);
            sView.setText(text);
        } else {
            sView.setText(text);
        }
        if (gravity == Gravity.CENTER) {
            toast.setGravity(gravity, 0, 0);
        }
        if (gravity == Gravity.TOP) {
            toast.setGravity(gravity, 0, 0);
        }
        if (gravity == Gravity.BOTTOM) {
            toast.setGravity(gravity, 0, 0);
        }

        toast.show();
    }

    public static void show(int resId, int duration) {
        show(context.getResources().getText(resId), duration, -1);
    }

    public static void show(CharSequence text) {
        show(text, Toast.LENGTH_SHORT, -1);
    }

    public static void show(CharSequence text, int gravitys) {
        show(text, Toast.LENGTH_SHORT, gravitys);
    }

}
