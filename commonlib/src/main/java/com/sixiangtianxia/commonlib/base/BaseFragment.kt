package com.sixiangtianxia.commonlib.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.sixiangtianxia.commonlib.MyApplication
import com.sixiangtianxia.commonlib.permissions.AppSettingsDialog
import com.sixiangtianxia.commonlib.permissions.EasyPermissions
import com.sixiangtianxia.commonlib.utils.DialogFactory
import com.sixiangtianxia.commonlib.view.MultipleStatusView
import org.greenrobot.eventbus.EventBus

abstract class BaseFragment : Fragment(), EasyPermissions.PermissionCallbacks, View.OnClickListener {

    /**
     * 视图是否加载完毕
     */
    private var isViewPrepare = false
    /**
     * 数据是否加载过了
     */
    private var hasLoadData = false
    /**
     * 多种状态的 View 的切换
     */
    protected var mLayoutStatusView: MultipleStatusView? = null
    private var mLoadingDialog: Dialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), null)
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            lazyLoadDataIfPrepared()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewPrepare = true
        initView()
        init()
        lazyLoadDataIfPrepared()
        //多种状态切换的view 重试点击事件
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    fun init() {
        if (useEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this) // 初始化EventBUs.
        }
    }

    private fun lazyLoadDataIfPrepared() {
        if (userVisibleHint && isViewPrepare && !hasLoadData) {
            lazyLoad()
            hasLoadData = true
        }
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        lazyLoad()
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

    /**
     * 加载布局
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * 初始化 ViewI
     */
    abstract fun initView()

    /**
     * 懒加载
     */
    abstract fun lazyLoad()

    /**
     * 是否使用EventBus.
     */
    protected abstract fun useEventBus(): Boolean

    override fun onDestroy() {
        super.onDestroy()
        activity?.let { MyApplication.getRefWatcher(it)?.watch(activity) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (useEventBus() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }

        if (mLoadingDialog != null) {
            mLoadingDialog!!.dismiss()
            mLoadingDialog = null
        }
    }

    fun showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = DialogFactory.createLoadingDialog(activity)
        }
        mLoadingDialog!!.show()
    }

    fun dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog!!.isShowing()) {
            mLoadingDialog!!.dismiss()
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
        val activity = activity
        if (clazz != null && activity != null) {
            val intent = Intent()
            intent.setClass(activity, clazz)
            if (bundle != null) intent.putExtras(bundle)
            if (optionsCompat == null) {
                if (requestCode > 0) {
                    startActivityForResult(intent, requestCode)
                    if (inAnimId > 0 || outAnimId > 0)
                        activity.overridePendingTransition(inAnimId, outAnimId)
                } else {
                    startActivity(intent)
                    if (inAnimId > 0 || outAnimId > 0)
                        activity.overridePendingTransition(inAnimId, outAnimId)
                }
            } else {
                if (requestCode > 0) {
                    ActivityCompat.startActivityForResult(activity, intent, requestCode, optionsCompat.toBundle())
                } else {
                    ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle())
                }
            }
        }
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
            Toast.makeText(activity, "已拒绝权限" + sb + "并不再询问", Toast.LENGTH_SHORT).show()
            AppSettingsDialog.Builder(this)
                .setRationale("此功能需要" + sb + "权限，否则无法正常使用，是否打开设置")
                .setPositiveButton("好")
                .setNegativeButton("不行")
                .build()
                .show()
        }
    }
}
