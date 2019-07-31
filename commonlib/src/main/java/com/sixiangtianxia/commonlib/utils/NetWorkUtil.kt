package com.sixiangtianxia.commonlib.utils

import android.content.Context
import android.net.ConnectivityManager

object NetWorkUtil {

    /**
     * 检查网络是否可用
     */
    fun isNetConnected(paramContext: Context): Boolean {
        val localNetworkInfo = (paramContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        if (localNetworkInfo != null && localNetworkInfo.isAvailable){
            return true
        }
        return false
    }

    /**
     * 检测wifi是否连接
     */
    fun isWifiConnected(context: Context): Boolean {
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connManager != null) {
            val networkInfo = connManager.activeNetworkInfo
            if (networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                return true
            }
        }
        return false
    }

    /**
     * 检测3G是否连接
     */
    fun is3gConnected(context: Context): Boolean {
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connManager != null) {
            val networkInfo = connManager.activeNetworkInfo
            if (networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                return true
            }
        }
        return false
    }

}