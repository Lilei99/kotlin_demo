package com.sixiangtianxia.commonlib.manager


import android.app.Activity

import java.util.ArrayList

class ActivityManager private constructor() {
    private val mActivities: MutableList<Activity>

    val activities: List<Activity>
        get() = mActivities

    /**
     * 得到第一个Activity
     */
    val lastActivity: Activity
        get() = mActivities[mActivities.size - 1]

    init {
        mActivities = ArrayList()
    }

    /**
     * 添加Activity.
     */
    fun addActivity(activity: Activity) {
        mActivities.add(activity)
    }

    /**
     * 移除Activity.
     */
    fun removeActivity(activity: Activity) {
        if (mActivities.contains(activity))
            mActivities.remove(activity)
    }

    /**
     * 结束一个Activity
     */
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            if (mActivities.contains(activity)) {
                removeActivity(activity)
            }
            activity.finish()
        }
    }

    /**
     * 移除所有的Activity.
     */
    fun removeAllActivity() {
        for (activity in mActivities) {
            activity.finish()
        }
        mActivities.clear()
    }

    /**
     * 获取Activity名字.
     */
    fun getClassName(activity: Activity): String {
        return activity.javaClass.name.intern()
    }

    companion object {

        @Volatile
        private var sActivityManager: ActivityManager? = null

        val instance: ActivityManager?
            get() {
                if (sActivityManager == null) {
                    synchronized(ActivityManager::class.java) {
                        if (sActivityManager == null) {
                            sActivityManager = ActivityManager()
                        }
                    }
                }
                return sActivityManager
            }
    }
}
