package com.sixiangtianxia.login.presenter

import com.sixiangtianxia.commonlib.model.AddressItemModel
import com.sixiangtianxia.commonlib.model.PublicBean
import com.sixiangtianxia.commonlib.network.RetrofitNet2
import com.sixiangtianxia.commonlib.network.exception.ExceptionHandle
import com.sixiangtianxia.commonlib.utils.LogUtil
import com.sixiangtianxia.commonlib.utils.ToastUtils
import com.sixiangtianxia.login.contract.LoginContract
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginPresenter : LoginContract.Presenter() {

    private var observaLogin: Observable<PublicBean<AddressItemModel>>? = null
    private val observerLogin = object : Observer<PublicBean<AddressItemModel>> {
        override fun onNext(datas: PublicBean<AddressItemModel>) {
            judgeLoginStatus(datas)
            if (datas.message!!.errcode.equals("200")) {
                ToastUtils.show("aaaaaaaa")
            }
        }

        override fun onError(e: Throwable) {
            LogUtil.i(ExceptionHandle.handleException(e))
        }

        override fun onComplete() {
        }

        override fun onSubscribe(d: Disposable) {
        }
    }

    override fun login() {
        observaLogin = RetrofitNet2.getDefault().login("education")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observaLogin!!.subscribe(observerLogin)
    }
}