package com.sixiangtianxia.commonlib.network

import android.util.SparseArray
import com.sixiangtianxia.commonlib.utils.AddressConstant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitNet2 {

    companion object {
        // 读取超时，单位:毫秒
        val READ_TIME_OUT: Long = 60 * 1000 // (10*1000)
        // 连接超时,单位:毫秒
        val CONNECT_TIME_OUT: Long = 60 * 1000 // (10*1000)

        private val sRetrofitManager = SparseArray<RetrofitNet2>(HostType.TYPE_COUNT * 2)

        fun getDefault(): NetWorkService {
            return getDefault(HostType.KOTLIN)
        }

        fun getDefault(hostType: Int): NetWorkService {
            var retrofitNet = sRetrofitManager.get(hostType)
            if (retrofitNet == null) {
                retrofitNet = RetrofitNet2()
                sRetrofitManager.put(hostType, retrofitNet)
            }
            return retrofitNet.networkService!!
        }
    }

    var retrofit: Retrofit? = null
    var networkService: NetWorkService? = null


    private constructor() {
        val okHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(MyTrustManager.createSSLSocketFactory())
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)// 设置读取超时时间
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)// 设置连接超时时间
            .addNetworkInterceptor(LoggingInterceptor(Logger()))// 设置网络拦截器  日志拦截器
            .addInterceptor(BusinessRequestInterceptor())// 设置请求头拦截器
            .build()


        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(AddressConstant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())// 添加解析器
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 添加调度器
            .build()
        networkService = retrofit?.create(NetWorkService::class.java)
    }
}