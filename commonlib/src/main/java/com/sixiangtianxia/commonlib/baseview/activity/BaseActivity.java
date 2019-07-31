package com.sixiangtianxia.commonlib.baseview.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import com.sixiangtianxia.commonlib.R;
import com.sixiangtianxia.commonlib.baseview.presenter.BasePresenter;
import com.sixiangtianxia.commonlib.baseview.view.BaseMvpView;
import com.sixiangtianxia.commonlib.interfaces.ViewClickListener;
import com.sixiangtianxia.commonlib.manager.ActivityManager;
import com.sixiangtianxia.commonlib.permissions.EasyPermissions;
import com.sixiangtianxia.commonlib.utils.DialogFactory;
import com.sixiangtianxia.commonlib.utils.TimeUtils;
import com.sixiangtianxia.commonlib.utils.ToastUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

public abstract class BaseActivity<V extends BaseMvpView, P extends BasePresenter<V>>
        extends AppCompatActivity implements BaseMvpView, EasyPermissions.PermissionCallbacks, ViewClickListener {

    public Activity mActivity;
    public P mPresenter;
    private long mExitAppTime;
    private Dialog mLoadingDialog;

    @Override
    public Resources getResources() {//还原字体大小
        Resources res = super.getResources();
        Configuration configuration = res.getConfiguration();
        if (configuration.fontScale != 1.0f) {
            configuration.fontScale = 1.0f;
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }
        return res;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        mActivity = this;
        ActivityManager.getInstance().addActivity(mActivity);
        if (useEventBus()) {
            EventBus.getDefault().register(mActivity); // 初始化EventBUs.
        }

        initView();
        initListener();
        initData();
    }

    protected void initView() {
    }

    protected void initListener() {
    }

    protected void initData() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && doubleBackExitApp()) {
            exitApp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 退出App
    private void exitApp() {
        if (TimeUtils.getNowTimeMills() - mExitAppTime > 2000) {
            showToast(getString(R.string.exit_app));
            mExitAppTime = TimeUtils.getNowTimeMills();
        } else {
            mExitAppTime = 0;
            ActivityManager.getInstance().removeAllActivity();
            System.exit(0);
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            startActivity(intent);
        }
    }

    /**
     * 获取Layout ID.
     */
    protected abstract int getLayoutID();

    /**
     * 双击是否退出app.
     */
    public boolean doubleBackExitApp() {
        return false;
    }

    /**
     * 使用EventBus.
     */
    public boolean useEventBus() {
        return false;
    }

    @Override
    public void showToast(String toastMsg) {
        ToastUtils.show(toastMsg);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // 拒绝权限.
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        // 权限开启.
    }

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
    protected void onDestroy() {
        ActivityManager.getInstance().removeActivity(mActivity);
        if (useEventBus() && EventBus.getDefault().isRegistered(mActivity)) {
            EventBus.getDefault().unregister(mActivity);
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }

        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
        mActivity = null;
        super.onDestroy();
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
        if (clazz != null) {
            Intent intent = new Intent();
            intent.setClass(this, clazz);
            if (bundle != null) intent.putExtras(bundle);
            if (optionsCompat == null) {
                if (requestCode > 0) {
                    startActivityForResult(intent, requestCode);
                    if (inAnimId > 0 || outAnimId > 0)
                        overridePendingTransition(inAnimId, outAnimId);
                } else {
                    startActivity(intent);
                    if (inAnimId > 0 || outAnimId > 0)
                        overridePendingTransition(inAnimId, outAnimId);
                }
            } else {
                if (requestCode > 0) {
                    ActivityCompat.startActivityForResult(this, intent, requestCode, optionsCompat.toBundle());
                } else {
                    ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
                }
            }
        }
    }

    public int getStatusBarHeight(Context context) {
        try {
            int result = 0;
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimens", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
            Log.d("getStatusBarHeight", "getStatusBarHeight==========>" + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
