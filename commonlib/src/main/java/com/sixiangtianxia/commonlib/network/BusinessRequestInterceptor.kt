package com.sixiangtianxia.commonlib.network

import com.sixiangtianxia.commonlib.utils.InternetUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

import java.io.IOException

/**
 * Creator: Gao MinMin.
 * Date: 2019/1/7.
 * Description: 业务请求拦截器.
 */
class BusinessRequestInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalBuilder = original.newBuilder()
        //        String token = ZJApp.application.getSharedPreferences(SharedPreferencesManager.PRE_NAME, Context.MODE_PRIVATE)
        //                .getString(SharedPreferencesManager.PRE_ACCOUNT_TOKEN,"");
        originalBuilder.addHeader("token", "e163944d33847e1c5b1df5dc0d1c2cd5")
        originalBuilder.addHeader("ip", InternetUtils.ipAddress!!)
        originalBuilder.addHeader("appType", "0") //0专技天下版，1基地版
        return chain.proceed(originalBuilder.build())
    }
}
