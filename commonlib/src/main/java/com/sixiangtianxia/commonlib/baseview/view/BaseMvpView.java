package com.sixiangtianxia.commonlib.baseview.view;

import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;

/**
 * Creator: Gao MinMin.
 * Date: 2018/11/27.
 * Description: Mvp view 基类.
 */
public interface BaseMvpView {

    /**
     * 显示加载Loading.
     */
    void showLoading();

    /**
     * 隐藏加载Loading.
     */
    void dismissLoading();

    /**
     * Toast.
     */
    void showToast(String toastMsg);

    void intent2Activity(Class clazz);

    void intent2Activity(Class clazz, int requestCode);

    void intent2Activity(Class clazz, Bundle bundle);

    void intent2Activity(Class clazz, Bundle bundle, int inAnimId, int outAnimId);

    void intent2Activity(Class clazz, Bundle bundle, ActivityOptionsCompat optionsCompat);

    void intent2Activity(Class clazz, Bundle bundle, int requestCode, ActivityOptionsCompat optionsCompat);

    void intent2Activity(Class clazz, Bundle bundle, int requestCode, ActivityOptionsCompat optionsCompat, int inAnimId, int outAnimId);
}
