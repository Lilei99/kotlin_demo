package com.sixiangtianxia.commonlib.model

class PublicBean<T> {
    var message: MessageBean? = null
    var data: T? = null
    override fun toString(): String {
        return "PublicBean(message=$message, data=$data)"
    }
}