package com.sixiangtianxia.commonlib.network

import android.util.SparseArray
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sixiangtianxia.commonlib.MyApplication
import com.sixiangtianxia.commonlib.network.parse.IntegerDefaultOAdapter
import com.sixiangtianxia.commonlib.utils.AddressConstant
import com.sixiangtianxia.commonlib.utils.InternetUtils
import com.sixiangtianxia.commonlib.utils.NetWorkUtil
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class RetrofitNet {

    companion object {
        // 读取超时，单位:毫秒
        val READ_TIME_OUT: Long = 60 * 1000 // (10*1000)
        // 连接超时,单位:毫秒
        val CONNECT_TIME_OUT: Long = 60 * 1000 // (10*1000)
        var mUseCache = true

        private val sRetrofitManager = SparseArray<RetrofitNet>(HostType.TYPE_COUNT * 2)
        /*************************
         * 缓存设置
         *********************/
        /*

        1. noCache 不使用缓存，全部走网络

        2. noStore 不使用缓存，也不存储缓存

        3. onlyIfCached 只使用缓存

        4. maxAge 设置最大失效时间，失效则不使用 需要服务器配合

        5. maxStale 设置最大失效时间，失效则不使用 需要服务器配合 感觉这两个类似 还没怎么弄清楚，清楚的同学欢迎留言

        6. minFresh 设置有效时间，依旧如上

        7. FORCE_NETWORK 只走网络

        8. FORCE_CACHE 只走缓存

        */
        //  缓存有效期为一天
        val CACHE_STALE_SEC: Long = 86400// (60*60*24*1)
        /**
         * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
         * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
         */
        private val CACHE_CONTROL_CACHE = "maxStale, max-stale=" + CACHE_STALE_SEC
        /**
         * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
         * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，
         * 浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
         */
        private val CACHE_CONTROL_AGE = "max-age=0"


        fun getCacheControl(): String {
            return if (true) CACHE_CONTROL_AGE
            else CACHE_CONTROL_CACHE
        }

        fun getDefault(): NetWorkService {
            mUseCache = true
            return getDefault(HostType.KOTLIN)
        }

        fun getDefault(useCache: Boolean): NetWorkService {
            mUseCache = useCache
            return getDefault(HostType.KOTLIN)
        }

        fun getDefault(hostType: Int): NetWorkService {
            var retrofitNet = sRetrofitManager.get(hostType)
            if (retrofitNet == null) {
                retrofitNet = RetrofitNet(hostType)
                sRetrofitManager.put(hostType, retrofitNet)
            }
            return retrofitNet.networkService!!
        }
    }

    var retrofit: Retrofit? = null
    var networkService: NetWorkService? = null


    private constructor(hostType: Int) {
        // 缓存
        val cacheFile = File(MyApplication.getInstance()!!.cacheDir, "cache")
        val cache = Cache(cacheFile, 1024 * 1024 * 50)// 50Mb
        // 增加头部信息
        val headerInterceptor = Interceptor { chain ->
            val oldRequest = chain!!.request()
            // 添加新的参数
            val authorizedUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host())
            // TODO
            val newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .addHeader("token", "c71b58e3471e61a02873afacb7dbe782")
                .addHeader("ip", InternetUtils.getIPAddress())
                .addHeader("appType", "0")
                .build()
            chain.proceed(newRequest)
        }


        val okHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(MyTrustManager.createSSLSocketFactory())
            .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)// 设置读取超时时间
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)// 设置连接超时时间
//            .addInterceptor(mRewriteCacheControlInterceptor)// 设置缓存拦截器
//            .addNetworkInterceptor(mRewriteCacheControlInterceptor)// 设置网络拦截器
            .addNetworkInterceptor(LoggingInterceptor(Logger()))// 设置网络拦截器  日志拦截器
            .addInterceptor(BusinessRequestInterceptor())// 设置请求头拦截器
//            .cache(cache)
            .build()
        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())// 添加解析器
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 添加调度器
            .baseUrl(AddressConstant.BASE_URL)
            .build()
        networkService = retrofit?.create(NetWorkService::class.java)
    }

    fun buildGson(): Gson {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .registerTypeAdapter(Int::class.java, IntegerDefaultOAdapter())
            .registerTypeAdapter(Int::class.javaPrimitiveType, IntegerDefaultOAdapter())
            .create()
        return gson
    }

    /**
     * 云端响应头拦截器，用来配置缓存策略
     */
    private val mRewriteCacheControlInterceptor = Interceptor { chain ->
        var request = chain.request()
        if (!NetWorkUtil.isNetConnected(MyApplication.getInstance()!!)) {
            if (mUseCache) {
                request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
            }
        }
        val originalResponse = chain.proceed(request)
        if (NetWorkUtil.isNetConnected(MyApplication.getInstance()!!)) {
            val cacheControl = request.cacheControl().toString()
            originalResponse.newBuilder()
                .header("Cache-Control", cacheControl)
                .removeHeader("Pragma")
                .build()
        } else {
            if (mUseCache) {
                originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                    .removeHeader("Pragma")
                    .build()
            } else {
                val cacheControl = request.cacheControl().toString()
                originalResponse.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build()
            }
        }
    }


}