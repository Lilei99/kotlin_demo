package com.sixiangtianxia.commonlib.network

import android.widget.Toast

import com.google.gson.JsonParseException
import com.sixiangtianxia.commonlib.MyApplication
import com.sixiangtianxia.commonlib.model.PublicBean
import com.sixiangtianxia.commonlib.utils.ToastUtils
import org.json.JSONException

import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import java.text.ParseException

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException


abstract class DefaultObserver<T : PublicBean<*>> : Observer<T> {

    private val isAddInStop = false


    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(response: T) {
        //        if (response.getResult() == 1) {
        //
        //        } else {
        //
        //        }
    }

    override fun onError(e: Throwable) {
        if (e is HttpException) {// HTTP错误
            onException(ExceptionReason.BAD_NETWORK)
        } else if (e is ConnectException || e is UnknownHostException) {// 连接错误
            onException(ExceptionReason.CONNECT_ERROR)
        } else if (e is InterruptedIOException) {// 连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT)
        } else if (e is JsonParseException
            || e is JSONException
            || e is ParseException
        ) {// 解析错误
            onException(ExceptionReason.PARSE_ERROR)
        } else {
            onException(ExceptionReason.UNKNOWN_ERROR)
        }
    }

    override fun onComplete() {

    }

    /**
     * 请求成功
     *
     * @param response 服务器返回的数据
     */
    abstract fun onSuccess(response: T)

    fun onFail(response: T) {
        val message = response.message!!.errinfo
        Toast.makeText(
            MyApplication.getInstance()!!.applicationContext,
            message, Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * 请求异常
     */
    fun onException(reason: ExceptionReason) {
        when (reason) {
            DefaultObserver.ExceptionReason.CONNECT_ERROR -> ToastUtils.show("网络连接失败,请检查网络")
            DefaultObserver.ExceptionReason.CONNECT_TIMEOUT -> ToastUtils.show("连接超时,请稍后再试", Toast.LENGTH_SHORT)
            DefaultObserver.ExceptionReason.BAD_NETWORK -> ToastUtils.show("服务器异常", Toast.LENGTH_SHORT)
            DefaultObserver.ExceptionReason.PARSE_ERROR -> ToastUtils.show("解析服务器响应数据失败", Toast.LENGTH_SHORT)
            DefaultObserver.ExceptionReason.UNKNOWN_ERROR -> ToastUtils.show("未知错误", Toast.LENGTH_SHORT)
            else -> ToastUtils.show("未知错误", Toast.LENGTH_SHORT)
        }
    }

    enum class ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR
    }
}
