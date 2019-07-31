package com.sixiangtianxia.commonlib.manager;


import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityManager {

    private static volatile ActivityManager sActivityManager;
    private List<Activity> mActivities;

    public static ActivityManager getInstance() {
        if (sActivityManager == null) {
            synchronized (ActivityManager.class) {
                if (sActivityManager == null) {
                    sActivityManager = new ActivityManager();
                }
            }
        }
        return sActivityManager;
    }

    private ActivityManager() {
        mActivities = new ArrayList<>();
    }

    public List<Activity> getActivities() {
        return mActivities;
    }

    /**
     * 得到第一个Activity
     */
    public Activity getLastActivity() {
        return mActivities.get(mActivities.size() - 1);
    }

    /**
     * 添加Activity.
     */
    public void addActivity(Activity activity) {
        mActivities.add(activity);
    }

    /**
     * 移除Activity.
     */
    public void removeActivity(Activity activity) {
        if (mActivities.contains(activity))
            mActivities.remove(activity);
    }

    /**
     * 结束一个Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (mActivities.contains(activity)) {
                removeActivity(activity);
            }
            activity.finish();
        }
    }

    /**
     * 移除所有的Activity.
     */
    public void removeAllActivity() {
        for (Activity activity : mActivities) {
            activity.finish();
        }
        mActivities.clear();
    }

    /**
     * 获取Activity名字.
     */
    public String getClassName(Activity activity) {
        return activity.getClass().getName().intern();
    }
}
