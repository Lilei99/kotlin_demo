package com.sixiangtianxia.commonlib.baseview.presenter;


import com.sixiangtianxia.commonlib.baseview.view.BaseMvpView;

/**
 * Creator: Gao MinMin.
 * Date: 2018/11/27.
 * Description: Presenter 基类.
 */
public interface Presenter<V extends BaseMvpView> {

    void attachView(V mvpView);

    void detachView();
}