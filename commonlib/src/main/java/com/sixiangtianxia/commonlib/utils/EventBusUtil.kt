package com.sixiangtianxia.commonlib.utils

import org.greenrobot.eventbus.EventBus

/**
 * EventBus工具类
 */

object EventBusUtil {
    //订阅事件
    fun register(subscriber: Any) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber)
        }
    }

    //取消订阅
    fun unregister(subscriber: Any) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber)
        }
    }

    //终止事件继续传递
    fun cancelDelivery(event: Any) {
        EventBus.getDefault().cancelEventDelivery(event)
    }

    //获取保存起来的粘性事件
    fun <T> getStickyEvent(classType: Class<T>): T {
        return EventBus.getDefault().getStickyEvent(classType)
    }

    //删除保存中的粘性事件
    fun removeStickyEvent(event: Any) {
        EventBus.getDefault().removeStickyEvent(event)
    }

    //发送事件
    fun postEvent(event: Any) {
        EventBus.getDefault().post(event)
    }

    //发送粘性事件
    fun postStickyEvent(event: Any) {
        EventBus.getDefault().postSticky(event)
    }

}
