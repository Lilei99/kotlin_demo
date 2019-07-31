package com.sixiangtianxia.commonlib.base

import android.text.TextUtils
import com.sixiangtianxia.commonlib.model.PublicBean

/**
 * @author ZhangBo on 2018/1/6.
 */
abstract class BasePresenter<T : BaseView> {

    var mView: T? = null
    fun setView(view: T) {
        mView = view
    }

    fun clearViews() {
        mView = null
    }

    open fun start() {

    }

    fun <T> judgeLoginStatus(bean: PublicBean<T>) {
//        if (bean.message!!.errcode == 0) {
//            if (bean.errorNo.equals("40090")) {
//                mView?.jumpToLoginPage()
//            } else {
//
//            }
//        }
    }
}