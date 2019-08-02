package com.sixiangtianxia.commonlib.network

import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate

/**
 * Name: MyTrustManager
 * Author: zl
 * Email:
 * Comment: //TODO
 * Date: 2019-05-24 10:13
 */
class MyTrustManager : X509TrustManager {
    @Throws(CertificateException::class)
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {

    }

    @Throws(CertificateException::class)
    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {

    }

    override fun getAcceptedIssuers(): Array<X509Certificate?> {
        return arrayOfNulls(0)
    }

    companion object {
        //支持所有 https协议
        fun createSSLSocketFactory(): SSLSocketFactory? {
            var ssfFactory: SSLSocketFactory? = null
            try {
                val sc = SSLContext.getInstance("SSL")
                sc.init(null, arrayOf<TrustManager>(MyTrustManager()), SecureRandom())
                ssfFactory = sc.socketFactory
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ssfFactory
        }
    }

}
