package com.sixiangtianxia.commonlib.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Creator: Gao MinMin.
 * Date: 2018/11/27.
 * Description: 工具类.
 */
public class Utils {

    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("Can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Utils.context = context.getApplicationContext();
//        LoggerUtils.initLogger(false);
//        ToastUtils.init(true, Gravity.CENTER);
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("Should init first");
    }

    /**
     * Copied from "android.util.Log.getStackTraceString()" in order to avoid usage of Android stack
     * in unit tests.
     *
     * @return Stack trace in form of String
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    /**
     * 获取设备ID.
     */
    public static String getUniquePsuedoID() {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10)
                + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10)
                + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        String serial = null;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial"; // some value
        }

        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * SDK >= 16.
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * SDK >= 21.
     */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * SDK >= 23.
     */
    public static boolean hasM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * SDK >= 24.
     */
    public static boolean hasN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    /**
     * SDK >= 26.
     */
    public static boolean hasO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    /**
     * 从资源中获取颜色.
     */
    public static int getColor(Context context, int colorResId) {
        return ContextCompat.getColor(context, colorResId);
    }

    /**
     * 当前版本是否需要启动引导页.
     */
    private static boolean currentVersionIsNeedLaunchNewGuide() {
        return true;
    }

    /**
     * 设置背景.
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void setBackgroundDrawableCompat(View view, Drawable drawable) {
        if (Utils.hasJellyBean()) {
            view.setBackground(drawable);
        } else {
            view.setBackground(drawable);
        }
    }

    /**
     * 设置字体颜色.
     */
    public static void setTextColor(TextView view, int color) {
        view.setTextColor(color);
    }

    /**
     * 设置文字高亮.
     *
     * @param context       上下文对象.
     * @param textView      需要设置的TextView.
     * @param highlightText 高亮文字.
     */
    public static void setTextHighlight(Context context, TextView textView, String highlightText, int colorResId) {
        String text = textView.getText().toString();
        if (StringUtils.isEmpty(text) || StringUtils.isEmpty(highlightText))
            return;

        SpannableString spannableString = new SpannableString(text);
        Pattern pattern = Pattern.compile(highlightText);
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            spannableString.setSpan(new ForegroundColorSpan(
                            ContextCompat.getColor(context, colorResId)),
                    start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(spannableString);
    }
}
