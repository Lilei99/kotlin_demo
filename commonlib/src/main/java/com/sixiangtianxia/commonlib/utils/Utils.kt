package com.sixiangtianxia.commonlib.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.widget.TextView

import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException
import java.util.UUID
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Creator: Gao MinMin.
 * Date: 2018/11/27.
 * Description: 工具类.
 */
class Utils private constructor() {

    init {
        throw UnsupportedOperationException("Can't instantiate me...")
    }

    companion object {

        private var context: Context? = null

        /**
         * 初始化工具类
         *
         * @param context 上下文
         */
        fun init(context: Context) {
            Utils.context = context.applicationContext
            //        LoggerUtils.initLogger(false);
            //        ToastUtils.init(true, Gravity.CENTER);
        }

        /**
         * 获取ApplicationContext
         *
         * @return ApplicationContext
         */
        fun getContext(): Context {
            if (context != null) return context!!
            throw NullPointerException("Should init first")
        }

        /**
         * Copied from "android.util.Log.getStackTraceString()" in order to avoid usage of Android stack
         * in unit tests.
         *
         * @return Stack trace in form of String
         */
        fun getStackTraceString(tr: Throwable?): String {
            if (tr == null) {
                return ""
            }

            // This is to reduce the amount of log spew that apps do in the non-error
            // condition of the network being unavailable.
            var t = tr
            while (t != null) {
                if (t is UnknownHostException) {
                    return ""
                }
                t = t.cause
            }

            val sw = StringWriter()
            val pw = PrintWriter(sw)
            tr.printStackTrace(pw)
            pw.flush()
            return sw.toString()
        }

        /**
         * 获取设备ID.
         */
        // some value
        val uniquePsuedoID: String
            get() {
                val m_szDevIDShort = ("35" + Build.BOARD.length % 10 + Build.BRAND.length % 10
                        + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10
                        + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10)

                var serial: String? = null
                try {
                    serial = Build::class.java.getField("SERIAL").get(null).toString()
                    return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
                } catch (exception: Exception) {
                    serial = "serial"
                }

                return UUID(m_szDevIDShort.hashCode().toLong(), serial!!.hashCode().toLong()).toString()
            }

        /**
         * SDK >= 16.
         */
        fun hasJellyBean(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
        }

        /**
         * SDK >= 21.
         */
        fun hasLollipop(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        }

        /**
         * SDK >= 23.
         */
        fun hasM(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        }

        /**
         * SDK >= 24.
         */
        fun hasN(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        }

        /**
         * SDK >= 26.
         */
        fun hasO(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        }

        /**
         * 从资源中获取颜色.
         */
        fun getColor(context: Context, colorResId: Int): Int {
            return ContextCompat.getColor(context, colorResId)
        }

        /**
         * 当前版本是否需要启动引导页.
         */
        private fun currentVersionIsNeedLaunchNewGuide(): Boolean {
            return true
        }

        /**
         * 设置背景.
         */
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        fun setBackgroundDrawableCompat(view: View, drawable: Drawable) {
            if (Utils.hasJellyBean()) {
                view.background = drawable
            } else {
                view.background = drawable
            }
        }

        /**
         * 设置字体颜色.
         */
        fun setTextColor(view: TextView, color: Int) {
            view.setTextColor(color)
        }

        /**
         * 设置文字高亮.
         *
         * @param context       上下文对象.
         * @param textView      需要设置的TextView.
         * @param highlightText 高亮文字.
         */
        fun setTextHighlight(context: Context, textView: TextView, highlightText: String, colorResId: Int) {
            val text = textView.text.toString()
            if (StringUtils.isEmpty(text) || StringUtils.isEmpty(highlightText))
                return

            val spannableString = SpannableString(text)
            val pattern = Pattern.compile(highlightText)
            val matcher = pattern.matcher(spannableString)
            while (matcher.find()) {
                val start = matcher.start()
                val end = matcher.end()
                spannableString.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(context, colorResId)
                    ),
                    start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            textView.text = spannableString
        }
    }
}
