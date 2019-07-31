package com.sixiangtianxia.commonlib.network;

import android.widget.Toast;

import com.google.gson.JsonParseException;
import com.sixiangtianxia.commonlib.MyApplication;
import com.sixiangtianxia.commonlib.model.PublicBean;
import com.sixiangtianxia.commonlib.utils.ToastUtils;
import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;


public abstract class DefaultObserver<T extends PublicBean> implements Observer<T> {

    private boolean isAddInStop = false;

    public DefaultObserver() {

    }


    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T response) {
//        if (response.getResult() == 1) {
//
//        } else {
//
//        }
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {// HTTP错误
            onException(ExceptionReason.BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {// 连接错误
            onException(ExceptionReason.CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {// 连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {// 解析错误
            onException(ExceptionReason.PARSE_ERROR);
        } else {
            onException(ExceptionReason.UNKNOWN_ERROR);
        }
    }

    @Override
    public void onComplete() {

    }

    /**
     * 请求成功
     *
     * @param response 服务器返回的数据
     */
    public abstract void onSuccess(T response);

    public void onFail(T response) {
        String message = response.getMessage().getErrinfo();
        Toast.makeText(MyApplication.Companion.getInstance().getApplicationContext(),
                message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 请求异常
     */
    public void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:
                ToastUtils.show("网络连接失败,请检查网络");
                break;
            case CONNECT_TIMEOUT:
                ToastUtils.show("连接超时,请稍后再试", Toast.LENGTH_SHORT);
                break;
            case BAD_NETWORK:
                ToastUtils.show("服务器异常", Toast.LENGTH_SHORT);
                break;
            case PARSE_ERROR:
                ToastUtils.show("解析服务器响应数据失败", Toast.LENGTH_SHORT);
                break;
            case UNKNOWN_ERROR:
            default:
                ToastUtils.show("未知错误", Toast.LENGTH_SHORT);
                break;
        }
    }

    public enum ExceptionReason {
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
        UNKNOWN_ERROR,
    }
}
