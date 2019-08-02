package com.sixiangtianxia.commonlib.utils

import android.os.Handler
import android.os.Message
import android.os.SystemClock
import java.text.SimpleDateFormat
import java.util.*

/**
 * 倒计时操作
 */
abstract class CountDownTimer {
    /**
     * 执行的总时间
     */
    private var mMillisInFuture: Long = 0
    /**
     * 时间间隔
     */
    private var mCountdownInterval: Long = 0


    constructor(mmMillisInFuture: Long, mmCountdownInterval: Long) {
        mMillisInFuture = mmMillisInFuture
        mCountdownInterval = mmCountdownInterval
    }

    // 停止时间
    private var mStopTimeInFuture: Long = 0

    // handles counting down
    private val mHandler = object : Handler() {

        override fun handleMessage(msg: Message) {
            synchronized(this@CountDownTimer) {
                // 计算剩余总时间
                val millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime()
                // 小于等于 0 ，回调 onFinish
                if (millisLeft <= 0) {
                    onFinish()
                } else if (millisLeft < mCountdownInterval) { // 小于计时间隔 ，delayed 一个消息
                    // no tick, just delay until done
                    sendMessageDelayed(obtainMessage(MSG), millisLeft)
                } else {
                    val lastTickStart = SystemClock.elapsedRealtime()
                    onTick(millisLeft)
                    // take into account user's onTick taking time to execute
                    var delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime()
                    // special case: user's onTick took more than interval to
                    // complete, skip to next interval
                    while (delay < 0) delay += mCountdownInterval
                    sendMessageDelayed(obtainMessage(MSG), delay)
                }
            }
        }
    }

    /**
     * Cancel the countdown.
     * 取消到timer
     */
    fun cancel() {
        mHandler.removeMessages(MSG)
    }

    /**
     * Start the countdown.
     * 开始
     */
    @Synchronized
    fun start(): CountDownTimer {
        if (mMillisInFuture <= 0) {
            onFinish()
            return this
        }
        // 停止时间 = 系统启动时间 + 总计时间
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture
        mHandler.sendMessage(mHandler.obtainMessage(MSG))
        return this
    }


    /**
     * 倒计时进行中
     */
    abstract fun onTick(millisUntilFinished: Long)

    /**
     * 倒计时结束
     */
    abstract fun onFinish()

    companion object {

        private val MSG = 1
    }
}