package com.sixiangtianxia.commonlib.utils

import android.util.Log

class LogUtil {

    companion object {
        private val TAG = true
        val tags = "12345"
        fun v(tag: String, message: String) {
            if (TAG) {
                Log.v(tag, message)
            }
        }

        fun d(tag: String, message: String) {
            if (TAG) {
                Log.d(tag, message)
            }
        }

        fun d(message: String) {
            if (TAG) {
                Log.d(tags, message)
            }
        }

        fun i(tag: String, message: String) {
            if (TAG) {
                Log.i(tag, message)
            }
        }

        fun i(message: String) {
            if (TAG) {
                Log.i(tags, message)
            }
        }

        fun w(tag: String, message: String) {
            if (TAG) {
                Log.w(tag, message)
            }
        }

        fun e(tag: String, message: String) {
            if (TAG) {
                Log.e(tag, message)
            }
        }
    }

}