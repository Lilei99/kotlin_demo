package com.sixiangtianxia.commonlib.network

import com.sixiangtianxia.commonlib.utils.LogUtil

class Logger : LoggingInterceptor.Logger {

    override fun log(message: String) {
        LogUtil.i("54321", message)
    }

    override fun logI(message: String) {
        LogUtil.i("54321", message)
    }
}
