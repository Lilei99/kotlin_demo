package com.sixiangtianxia.commonlib.utils

import android.annotation.SuppressLint
import android.app.Application


/**
 * <pre>
 * author: Blankj
 * blog  : http://blankj.com
 * time  : 16/12/08
 * desc  : Utils初始化相关
</pre> *
 */
class MyUtils private constructor() {



    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var sApplication: Application? = null

        /**
         * 初始化工具类
         *
         * @param app 应用
         */
        fun init(app: Application) {
            MyUtils.sApplication = app
//            RudenessScreenHelper(app, 1080f).activate()

        }

        /**
         * 获取Application
         *
         * @return Application
         */
        val app: Application
            get() {
                if (sApplication != null) return sApplication!!
                throw NullPointerException("u should init first")
            }
    }

}
