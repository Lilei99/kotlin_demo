package com.sixiangtianxia.commonlib.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.sixiangtianxia.commonlib.MyApplication
import com.sixiangtianxia.commonlib.manager.ActivityManager
import com.sixiangtianxia.commonlib.permissions.AppSettingsDialog
import com.sixiangtianxia.commonlib.permissions.EasyPermissions
import com.sixiangtianxia.commonlib.utils.DialogFactory
import com.sixiangtianxia.commonlib.utils.TimeUtils
import com.sixiangtianxia.commonlib.utils.ToastUtils
import com.sixiangtianxia.commonlib.view.MultipleStatusView
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks, View.OnClickListener {
    /**
     * 多种状态的 View 的切换
     */
    protected var mLayoutStatusView: MultipleStatusView? = null
    private var mExitAppTime: Long = 0
    private var mLoadingDialog: Dialog? = null
    var activity: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        activity = this
        ActivityManager.instance!!.addActivity(this)
        if (useEventBus()) {
            EventBus.getDefault().register(activity) // 初始化EventBUs.
        }
        setContentView(layoutId())
        initData()
        initView()
        start()
        initListener()
    }

    private fun initListener() {
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        start()
    }

    /**
     *  加载布局
     */
    abstract fun layoutId(): Int

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化 View
     */
    abstract fun initView()

    /**
     * 开始请求
     */
    abstract fun start()

    /**
     * 使用EventBus.
     */
    fun useEventBus(): Boolean {
        return false
    }

    override fun getResources(): Resources {//还原字体大小
        val res = super.getResources()
        val configuration = res.configuration
        if (configuration.fontScale != 1.0f) {
            configuration.fontScale = 1.0f
            res.updateConfiguration(configuration, res.displayMetrics)
        }
        return res
    }


    fun showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = DialogFactory.createLoadingDialog(this)
        }
        mLoadingDialog!!.show()
    }

    fun dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog!!.isShowing()) {
            mLoadingDialog!!.dismiss()
        }
    }

    /**
     * 打开软键盘
     */
    fun openKeyBord(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    /**
     * 关闭软键盘
     */
    fun closeKeyBord(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        MyApplication.getRefWatcher(this)?.watch(activity)
        ActivityManager.instance!!.removeActivity(this)
        if (useEventBus() && EventBus.getDefault().isRegistered(activity)) {
            EventBus.getDefault().unregister(activity)
        }

        if (mLoadingDialog != null) {
            mLoadingDialog!!.dismiss()
            mLoadingDialog = null
        }
        activity = null
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && doubleBackExitApp()) {
            exitApp()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    // 退出App
    private fun exitApp() {
        if (TimeUtils.nowTimeMills - mExitAppTime > 2000) {
            ToastUtils.show("再按一次退出程序")
            mExitAppTime = TimeUtils.nowTimeMills
        } else {
            mExitAppTime = 0
            ActivityManager.instance!!.removeAllActivity()
            System.exit(0)
        }
    }

    /**
     * 双击是否退出app.
     */
    open fun doubleBackExitApp(): Boolean {
        return false
    }

    /**
     * 重写要申请权限的Activity或者Fragment的onRequestPermissionsResult()方法，
     * 在里面调用EasyPermissions.onRequestPermissionsResult()，实现回调。
     *
     * @param requestCode  权限请求的识别码
     * @param permissions  申请的权限
     * @param grantResults 授权结果
     */
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * 当权限被成功申请的时候执行回调
     *
     * @param requestCode 权限请求的识别码
     * @param perms       申请的权限的名字
     */
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Log.i("EasyPermissions", "获取成功的权限$perms")
    }

    /**
     * 当权限申请失败的时候执行的回调
     *
     * @param requestCode 权限请求的识别码
     * @param perms       申请的权限的名字
     */
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        //处理权限名字字符串
        val sb = StringBuffer()
        for (str in perms) {
            sb.append(str)
            sb.append("\n")
        }
        sb.replace(sb.length - 2, sb.length, "")
        //用户点击拒绝并不在询问时候调用
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Toast.makeText(this, "已拒绝权限" + sb + "并不再询问", Toast.LENGTH_SHORT).show()
            AppSettingsDialog.Builder(this)
                .setRationale("此功能需要" + sb + "权限，否则无法正常使用，是否打开设置")
                .setPositiveButton("好")
                .setNegativeButton("不行")
                .build()
                .show()
        }
    }

    fun intent2Activity(clazz: Class<*>) {
        intent2Activity(clazz, null, 0, null, 0, 0)
    }

    fun intent2Activity(clazz: Class<*>, requestCode: Int) {
        intent2Activity(clazz, null, requestCode, null, 0, 0)
    }

    fun intent2Activity(clazz: Class<*>, bundle: Bundle) {
        intent2Activity(clazz, bundle, 0, null, 0, 0)
    }

    fun intent2Activity(clazz: Class<*>, bundle: Bundle, inAnimId: Int, outAnimId: Int) {
        intent2Activity(clazz, bundle, 0, null, inAnimId, outAnimId)
    }

    fun intent2Activity(clazz: Class<*>, bundle: Bundle, optionsCompat: ActivityOptionsCompat) {
        intent2Activity(clazz, bundle, 0, optionsCompat, 0, 0)
    }

    fun intent2Activity(clazz: Class<*>, bundle: Bundle, requestCode: Int, optionsCompat: ActivityOptionsCompat) {
        intent2Activity(clazz, bundle, requestCode, optionsCompat, 0, 0)
    }

    fun intent2Activity(
        clazz: Class<*>?,
        bundle: Bundle?,
        requestCode: Int,
        optionsCompat: ActivityOptionsCompat?,
        inAnimId: Int,
        outAnimId: Int
    ) {
        if (clazz != null) {
            val intent = Intent()
            intent.setClass(this, clazz)
            if (bundle != null) intent.putExtras(bundle)
            if (optionsCompat == null) {
                if (requestCode > 0) {
                    startActivityForResult(intent, requestCode)
                    if (inAnimId > 0 || outAnimId > 0)
                        overridePendingTransition(inAnimId, outAnimId)
                } else {
                    startActivity(intent)
                    if (inAnimId > 0 || outAnimId > 0)
                        overridePendingTransition(inAnimId, outAnimId)
                }
            } else {
                if (requestCode > 0) {
                    ActivityCompat.startActivityForResult(this, intent, requestCode, optionsCompat.toBundle())
                } else {
                    ActivityCompat.startActivity(this, intent, optionsCompat.toBundle())
                }
            }
        }
    }

    fun getStatusBarHeight(context: Context): Int {
        try {
            var result = 0
            val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = context.resources.getDimensionPixelSize(resourceId)
            }
            Log.d("getStatusBarHeight", "getStatusBarHeight==========>$result")
            return result
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0
    }
}


