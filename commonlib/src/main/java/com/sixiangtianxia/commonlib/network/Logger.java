package com.sixiangtianxia.commonlib.network;

import com.sixiangtianxia.commonlib.utils.LogUtil;

public class Logger implements LoggingInterceptor.Logger {

    @Override
    public void log(String message) {
        LogUtil.Companion.i("54321",message);
    }

    @Override
    public void logI(String message) {
        LogUtil.Companion.i("54321",message);
    }
}
