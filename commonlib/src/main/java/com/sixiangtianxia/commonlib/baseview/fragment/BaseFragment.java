package com.sixiangtianxia.commonlib.baseview.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sixiangtianxia.commonlib.baseview.presenter.BasePresenter;
import com.sixiangtianxia.commonlib.baseview.view.BaseMvpView;
import com.sixiangtianxia.commonlib.interfaces.ViewClickListener;
import com.sixiangtianxia.commonlib.utils.DialogFactory;
import com.sixiangtianxia.commonlib.utils.StringUtils;
import com.sixiangtianxia.commonlib.utils.ToastUtils;
import org.greenrobot.eventbus.EventBus;

/**
 * Creator: Gao MinMin.
 * Date: 2018/11/29.
 * Description: Fragment 基类.
 */
public abstract class BaseFragment<V extends BaseMvpView, P extends BasePresenter<V>>
        extends Fragment implements BaseMvpView, ViewClickListener {

    private boolean isVisible = false;
    private boolean isInit = false;
    public Activity mActivity;
    public Bundle mBundle;
    public P mPresenter;
    public View mRootView;
    private String mClassName;
    private Dialog mLoadingDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        mClassName = getClass().getSimpleName().intern();
        mBundle = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutID(), container, false);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isInit = true;
        if (needLazyLoad()) {
            if (isVisible) {
                onVisible();
            }
        } else {
            onVisible();
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        if (isInit) {
            init();
            isInit = false;
        }
    }

    private void init() {
        if (useEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this); // 初始化EventBUs.
        }
        initView();
        initListener();
        initData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (StringUtils.isEmpty(mClassName)) {
            mClassName = getClass().getSimpleName().intern();
        }
        isVisible = isVisibleToUser;
        if (isVisible) {
            onVisible();
        }
    }

    protected void initView() {
    }

    protected void initListener() {
    }

    protected void initData() {
    }

    /**
     * 是否需要懒加载.
     */
    protected abstract boolean needLazyLoad();

    /**
     * 获取Layout ID.
     */
    protected abstract int getLayoutID();

    /**
     * 是否使用EventBus.
     */
    protected abstract boolean useEventBus();

    @Override
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = DialogFactory.createLoadingDialog(mActivity);
        }
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showToast(String toastMsg) {
        ToastUtils.show(toastMsg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (useEventBus() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
        mActivity = null;
        mRootView = null;

    }

    @Override
    public void intent2Activity(Class clazz) {
        intent2Activity(clazz, null, 0, null, 0, 0);
    }

    @Override
    public void intent2Activity(Class clazz, int requestCode) {
        intent2Activity(clazz, null, requestCode, null, 0, 0);
    }

    @Override
    public void intent2Activity(Class clazz, Bundle bundle) {
        intent2Activity(clazz, bundle, 0, null, 0, 0);
    }

    @Override
    public void intent2Activity(Class clazz, Bundle bundle, int inAnimId, int outAnimId) {
        intent2Activity(clazz, bundle, 0, null, inAnimId, outAnimId);
    }

    @Override
    public void intent2Activity(Class clazz, Bundle bundle, ActivityOptionsCompat optionsCompat) {
        intent2Activity(clazz, bundle, 0, optionsCompat, 0, 0);
    }

    @Override
    public void intent2Activity(Class clazz, Bundle bundle, int requestCode, ActivityOptionsCompat optionsCompat) {
        intent2Activity(clazz, bundle, requestCode, optionsCompat, 0, 0);
    }

    @Override
    public void intent2Activity(Class clazz, Bundle bundle, int requestCode, ActivityOptionsCompat optionsCompat, int inAnimId, int outAnimId) {
        FragmentActivity activity = getActivity();
        if (clazz != null && activity != null) {
            Intent intent = new Intent();
            intent.setClass(activity, clazz);
            if (bundle != null) intent.putExtras(bundle);
            if (optionsCompat == null) {
                if (requestCode > 0) {
                    startActivityForResult(intent, requestCode);
                    if (inAnimId > 0 || outAnimId > 0)
                        activity.overridePendingTransition(inAnimId, outAnimId);
                } else {
                    startActivity(intent);
                    if (inAnimId > 0 || outAnimId > 0)
                        activity.overridePendingTransition(inAnimId, outAnimId);
                }
            } else {
                if (requestCode > 0) {
                    ActivityCompat.startActivityForResult(activity, intent, requestCode, optionsCompat.toBundle());
                } else {
                    ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
                }
            }
        }
    }
}
