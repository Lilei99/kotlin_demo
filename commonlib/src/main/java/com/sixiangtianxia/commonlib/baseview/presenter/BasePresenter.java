package com.sixiangtianxia.commonlib.baseview.presenter;

import com.sixiangtianxia.commonlib.baseview.view.BaseMvpView;

/**
 * Creator: Gao MinMin.
 * Date: 2018/11/27.
 * Description: Presenter 基类.
 */
public abstract class BasePresenter<T extends BaseMvpView> implements Presenter<T> {

    public T mMvpView;

    public BasePresenter(T mvpView) {
        mMvpView = mvpView;
        attachView(mvpView);
    }

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    private T getMvpView() {
        return mMvpView;
    }
}
