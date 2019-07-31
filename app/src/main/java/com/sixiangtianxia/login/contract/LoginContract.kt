package com.sixiangtianxia.login.contract

import com.sixiangtianxia.commonlib.base.BasePresenter
import com.sixiangtianxia.commonlib.base.BaseView

interface LoginContract {

    interface View : BaseView {

        fun loginSuccess()
        fun loginFaild()
    }

    abstract class Presenter : BasePresenter<View>() {

        abstract fun login()
    }

}