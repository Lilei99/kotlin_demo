package com.sixiangtianxia

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.sixiangtianxia.commonlib.MyApplication
import com.sixiangtianxia.commonlib.utils.MyUtils
import com.sixiangtianxia.commonlib.utils.Utils

class Application : MyApplication() {

    override fun onCreate() {
        super.onCreate()
//        ARouter.openLog();     // 打印日志
//        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
//        ARouter.init( this ); // 尽可能早，推荐在Application中初始化
        Utils.init(this)
        MyUtils.init(this)
    }
}