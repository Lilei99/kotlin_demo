package com.sixiangtianxia.commonlib.baseview.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.*;
import com.sixiangtianxia.commonlib.R;
import com.sixiangtianxia.commonlib.interfaces.DialogShowHiddenListener;
import com.sixiangtianxia.commonlib.interfaces.ViewClickListener;
import com.sixiangtianxia.commonlib.utils.DialogFactory;

import java.lang.ref.WeakReference;

/**
 */
public abstract class BaseDialog extends Dialog implements ViewClickListener {

    public View mDialogView;
    public int mClickViewId;
    public Activity mActivity;
    public Window mWindow;
    private int mHeight;
    private int mGravity;
    private int mAnimation;
    private int mDialogType;
    private Dialog mLoadingDialog;
    private DialogShowHiddenListener mDialogShowHiddenListener;

    public BaseDialog(@NonNull Activity activity) {
        this(activity, -1, Gravity.CENTER, R.style.anim_bottom_bottom, -1);
    }

    public BaseDialog(@NonNull Activity activity, int dialogType) {
        this(activity, -1, Gravity.CENTER, R.style.anim_bottom_bottom, dialogType);
    }

    public BaseDialog(@NonNull Activity activity,
                      int height,
                      int gravity,
                      int animation,
                      int dialogType) {
        this(activity, R.style.CustomDialog, height, gravity, animation, dialogType);
    }

    public BaseDialog(@NonNull Activity activity,
                      int themeResId,
                      int height,
                      int gravity,
                      int animation,
                      int dialogType) {
        super(activity, themeResId);
        mWindow = getWindow();
        mActivity = activity;
        mHeight = height;
        mGravity = gravity;
        mAnimation = animation;
        mDialogType = dialogType;

        setOnCancelListener(new MyOnCancelListener(mDialogShowHiddenListener));
        setOnShowListener(new MyOnShowListener(mDialogShowHiddenListener));
        setCanceledOnTouchOutside(true); // 点击空白区域可以Dismiss对话框
        setCancelable(true); // 点击返回按键可以Dismiss对话框
    }

    public void setDialogShowHiddenListener(DialogShowHiddenListener dialogShowHiddenListener) {
        mDialogShowHiddenListener = dialogShowHiddenListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogView = LayoutInflater.from(mActivity).inflate(dialogLayoutId(), null, false);
        setContentView(mDialogView);
        initDialog();
        initView();
    }

    /**
     * 初始化Dialog.
     */
    private void initDialog() {
        if (mDialogType != -1) mWindow.setType(mDialogType);
        WindowManager.LayoutParams layoutParams = mWindow.getAttributes();
        // 设置对话框宽度.
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        // 设置对话框高度.
        if (mHeight != -1)
            layoutParams.height = mHeight;
        else
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        // 设置对话框位置.
        layoutParams.gravity = mGravity == -1 ? Gravity.CENTER : mGravity;
        mWindow.setAttributes(layoutParams);
        // 设置对话框动画.
        mWindow.setWindowAnimations(mAnimation);
    }

    /**
     * 从string.xml获取字符串.
     */
    public String getString(int resId, Object... formatArgs) {
        if (formatArgs == null) {
            return mActivity.getString(resId);
        } else {
            return mActivity.getString(resId, formatArgs);
        }
    }

    /**
     * 显示对话框.
     */
    public void showDialog() {
        if (!isShowing()) {
            show();
        }
    }

    /**
     * 隐藏对话框.
     */
    public void dismissDialog() {
        if (isShowing()) {
            dismiss();
        }

        if (mDialogShowHiddenListener != null)
            mDialogShowHiddenListener.dialogHidden();
    }

    /**
     * 显示加载动画
     */
    public void showLoading() {
        if (mLoadingDialog == null)
            mLoadingDialog = DialogFactory.createLoadingDialog(mActivity);
        mLoadingDialog.show();
        mLoadingDialog.setCancelable(false);
    }

    /**
     * 隐藏加载动画
     */
    public void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    /**
     * Dialog Layout id.
     */
    public abstract int dialogLayoutId();

    /**
     * 初始化View.
     */
    public abstract void initView();

    @Override
    protected void onStop() {
        super.onStop();
        mDialogView = null;
        mLoadingDialog = null;
        mDialogShowHiddenListener = null;
    }

    static class MyOnCancelListener implements OnCancelListener {
        WeakReference<DialogShowHiddenListener> mWeakReference;

        public MyOnCancelListener(DialogShowHiddenListener listener) {
            super();
            mWeakReference = new WeakReference<DialogShowHiddenListener>(listener);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            DialogShowHiddenListener listener = mWeakReference.get();
            if(listener != null){
                listener.dialogHidden();

            }
        }
    }

    static class MyOnShowListener implements OnShowListener {
        WeakReference<DialogShowHiddenListener> mWeakReference;

        public MyOnShowListener(DialogShowHiddenListener listener) {
            super();
            mWeakReference = new WeakReference<DialogShowHiddenListener>(listener);
        }

        @Override
        public void onShow(DialogInterface dialog) {
            DialogShowHiddenListener listener = mWeakReference.get();
            if(listener != null){
                listener.dialogShow();
            }
        }
    }
}


