package com.sixiangtianxia.commonlib.utils

import android.widget.Toast

class ToastWrapper {

    var text: String? = null
    var res: String? = null
    var showSuccess: Boolean = false
    var showError: Boolean = false


    fun toast(init: ToastWrapper.() -> Unit) {

        val wrap = ToastWrapper()
        wrap.init()

        execute(wrap)
    }

    fun execute(wrap: ToastWrapper) {
//
//        var taost: Toast? = null
//        wrap.text?.let {
//            taost = toast(it)
//        }
//
//        wrap.res?.let {
//            taost = toast(it)
//        }
//
//        if (wrap.showSuccess) {
//
////            taost?.withSuccIcon()
//        } else if (wrap.showError) {
//
////            taost?.withErrorIcon()
//        }
    }
}