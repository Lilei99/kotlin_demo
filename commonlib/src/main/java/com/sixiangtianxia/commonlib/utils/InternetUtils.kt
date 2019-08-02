package com.sixiangtianxia.commonlib.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import com.sixiangtianxia.commonlib.utils.InternetUtils.intIP2StringIP

import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.Enumeration

/**
 * Creator: Gao MinMin.
 * Date: 2019/2/27.
 * Description: 网络工具类.
 */
object InternetUtils {

    /**
     * 获取IP地址.
     */
    //当前使用2G/3G/4G网络
    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
    //当前使用无线网络
    //得到IPV4地址
    //当前无网络连接,请在设置中打开网络
    val ipAddress: String?
        get() {
            val info = (Utils.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
            if (info != null && info.isConnected) {
                if (info.type == ConnectivityManager.TYPE_MOBILE) {
                    try {
                        val en = NetworkInterface.getNetworkInterfaces()
                        while (en.hasMoreElements()) {
                            val intf = en.nextElement()
                            val enumIpAddr = intf.inetAddresses
                            while (enumIpAddr.hasMoreElements()) {
                                val inetAddress = enumIpAddr.nextElement()
                                if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                                    return inetAddress.getHostAddress()
                                }
                            }
                        }
                    } catch (e: SocketException) {
                        e.printStackTrace()
                    }

                } else if (info.type == ConnectivityManager.TYPE_WIFI) {
                    val wifiManager =
                        Utils.getContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                    val wifiInfo = wifiManager.connectionInfo
                    return intIP2StringIP(wifiInfo.ipAddress)
                }
            } else {
            }
            return null
        }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    fun intIP2StringIP(ip: Int): String {
        return (ip and 0xFF).toString() + "." +
                (ip shr 8 and 0xFF) + "." +
                (ip shr 16 and 0xFF) + "." +
                (ip shr 24 and 0xFF)
    }
}

