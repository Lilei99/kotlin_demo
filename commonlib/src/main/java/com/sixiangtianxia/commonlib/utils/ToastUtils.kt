package com.sixiangtianxia.commonlib.utils

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.sixiangtianxia.commonlib.MyApplication
import com.sixiangtianxia.commonlib.R

object ToastUtils {


    private var toast: Toast? = null
    private var sView: TextView? = null

    fun show(resId: Int) {
        show(MyUtils.app!!.getResources().getText(resId), Toast.LENGTH_SHORT, -1)
    }

    fun show(text: CharSequence, duration: Int, gravity: Int) {
        toShow(text, duration, gravity)

    }

    private fun toShow(text: CharSequence, duration: Int, gravity: Int) {
        var text = text
        if (TextUtils.isEmpty(text)) {
            text = "操作失败"
        }
        if (toast != null) {
            toast!!.cancel()
            toast = null
        }

        if (toast == null) {
            toast = Toast(MyUtils.app)
            toast!!.duration = duration
            val inflate = LayoutInflater.from(MyUtils.app).inflate(R.layout.toast, null)
            sView = inflate.findViewById(R.id.toast_tvtext)
            toast!!.view = inflate
            sView!!.text = text
        } else {
            sView!!.text = text
        }
        if (gravity == Gravity.CENTER) {
            toast!!.setGravity(gravity, 0, 0)
        }
        if (gravity == Gravity.TOP) {
            toast!!.setGravity(gravity, 0, 0)
        }
        if (gravity == Gravity.BOTTOM) {
            toast!!.setGravity(gravity, 0, 0)
        }

        toast!!.show()
    }

    fun show(resId: Int, duration: Int) {
        show(MyUtils.app!!.getResources().getText(resId), duration, -1)
    }

    fun show(text: CharSequence) {
        show(text, Toast.LENGTH_SHORT, -1)
    }

    fun show(text: String) {
        show(text, Toast.LENGTH_SHORT, -1)
    }

    fun show(text: CharSequence, gravitys: Int) {
        show(text, Toast.LENGTH_SHORT, gravitys)
    }

}