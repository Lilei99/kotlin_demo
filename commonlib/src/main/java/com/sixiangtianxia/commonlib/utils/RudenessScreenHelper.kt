package com.sixiangtianxia.commonlib.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager

import java.lang.reflect.Field

import android.content.Context.WINDOW_SERVICE

//若存在webview导致适配失效的问题
//        可以先继承WebView并重写 setOverScrollMode(int mode) 方法，在方法中调用super之后调用一遍 RudenessScreenHelper.resetDensity(getContext(), designWidth) 规避
//
//        若存在dialog中适配失效的问题
//        可以在dialog的oncreate中调用一遍 RudenessScreenHelper.resetDensity(getContext(), designWidth) 规避
//
//        旋转屏幕之后适配失效
//        可以在onConfigurationChanged中调用 RudenessScreenHelper.resetDensity(getContext(), designWidth) 规避
//
//        特定国产机型ROM中偶先fragment失效
//        可以在fragment的onCreateView中调用 RudenessScreenHelper.resetDensity(getContext(), designWidth) 规避

class RudenessScreenHelper
/**
 *
 * @param application application
 * @param width 设计稿宽度
 */
    (private val mApplication: Application, width: Float) {


    private val activityLifecycleCallbacks: Application.ActivityLifecycleCallbacks
    private var designWidth = 720f

    init {
        designWidth = width

        activityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {
                //通常情况下application与activity得到的resource虽然不是一个实例，但是displayMetrics是同一个实例，只需调用一次即可
                //为了面对一些不可预计的情况以及向上兼容，分别调用一次较为保险
                resetDensity(mApplication, designWidth)
                resetDensity(activity, designWidth)
            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }
        }
    }

    /**
     * 激活本方案
     */
    fun activate() {
        resetDensity(mApplication, designWidth)
        mApplication.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    /**
     * 恢复系统原生方案
     */
    fun inactivate() {
        restoreDensity(mApplication)
        mApplication.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    companion object {

        /**
         * 重新计算displayMetrics.xhdpi, 使单位pt重定义为设计稿的相对长度
         * @see .activate
         * @param context
         * @param designWidth 设计稿的宽度
         */
        fun resetDensity(context: Context?, designWidth: Float) {
            if (context == null)
                return

            val size = Point()
            (context.getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(size)

            val resources = context.resources

            //        Log.e("dsfkldfjksdf","xdpi "+resources.getDisplayMetrics().xdpi);
            //        Log.e("dsfkldfjksdf","size.x "+size.x);
            //        Log.e("dsfkldfjksdf","size.x/designWidth*72f "+(size.x/designWidth*72f));
            resources.displayMetrics.xdpi = size.x / designWidth * 72f

            val metrics = getMetricsOnMiui(context.resources)
            if (metrics != null)
                metrics.xdpi = size.x / designWidth * 72f
        }

        /**
         * 恢复displayMetrics为系统原生状态，单位pt恢复为长度单位磅
         * @see .inactivate
         * @param context
         */
        private fun restoreDensity(context: Context) {
            context.resources.displayMetrics.setToDefaults()

            val metrics = getMetricsOnMiui(context.resources)
            metrics?.setToDefaults()
        }

        //解决MIUI更改框架导致的MIUI7+Android5.1.1上出现的失效问题(以及极少数基于这部分miui去掉art然后置入xposed的手机)
        private fun getMetricsOnMiui(resources: Resources): DisplayMetrics? {
            if ("MiuiResources" == resources.javaClass.simpleName || "XResources" == resources.javaClass.simpleName) {
                try {
                    val field = Resources::class.java.getDeclaredField("mTmpMetrics")
                    field.isAccessible = true
                    return field.get(resources) as DisplayMetrics
                } catch (e: Exception) {
                    return null
                }

            }
            return null
        }

        /**
         * 转换dp为px
         * @param context context
         * @param value 需要转换的dp值
         * @return px值
         */
        fun dp2px(context: Context, value: Float): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics)
        }

        /**
         * 转换pt为px
         * @param context context
         * @param value 需要转换的pt值，若context.resources.displayMetrics经过resetDensity()的修改则得到修正的相对长度，否则得到原生的磅
         * @return px值
         */
        fun pt2px(context: Context, value: Float): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, value, context.resources.displayMetrics)
        }
    }


}
