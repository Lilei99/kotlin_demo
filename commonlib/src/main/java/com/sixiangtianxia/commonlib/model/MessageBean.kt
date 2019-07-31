package com.sixiangtianxia.commonlib.model

import android.os.Parcel
import android.os.Parcelable

class MessageBean() {

    var errinfo: String? = null
    var errcode: String? = null

    override fun toString(): String {
        return "MessageBean(errinfo=$errinfo, errcode=$errcode)"
    }
}