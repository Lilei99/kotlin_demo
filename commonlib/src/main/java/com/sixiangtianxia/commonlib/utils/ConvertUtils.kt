package com.sixiangtianxia.commonlib.utils

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

import java.io.*
import kotlin.experimental.or


class ConvertUtils private constructor() {

    init {
        throw UnsupportedOperationException("Can't instantiate me...")
    }

    /**
     * outputStream转inputStream
     *
     * @param out 输出流
     * @return inputStream子类
     */
    fun output2InputStream(out: OutputStream?): ByteArrayInputStream? {
        return if (out == null) null else ByteArrayInputStream((out as ByteArrayOutputStream).toByteArray())
    }

    companion object {

        private val hexDigits =
            charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

        /**
         * byteArr转hexString
         *
         * 例如：
         * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
         *
         * @param bytes 字节数组
         * @return 16进制大写字符串
         */

        /**
         * hexString转byteArr
         *
         * 例如：
         * hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }
         *
         * @param hexString 十六进制字符串
         * @return 字节数组
         */
        fun hexString2Bytes(hexString: String): ByteArray? {
            var hexString = hexString
            if (StringUtils.isSpace(hexString)) return null
            var len = hexString.length
            if (len % 2 != 0) {
                hexString = "0$hexString"
                len = len + 1
            }
            val hexBytes = hexString.toUpperCase().toCharArray()
            val ret = ByteArray(len shr 1)
            var i = 0
            while (i < len) {
                ret[i shr 1] = (hex2Dec(hexBytes[i]) shl 4 or hex2Dec(hexBytes[i + 1])).toByte()
                i += 2
            }
            return ret
        }

        /**
         * hexChar转int
         *
         * @param hexChar hex单个字节
         * @return 0..15
         */
        private fun hex2Dec(hexChar: Char): Int {
            return if (hexChar >= '0' && hexChar <= '9') {
                hexChar - '0'
            } else if (hexChar >= 'A' && hexChar <= 'F') {
                hexChar - 'A' + 10
            } else {
                throw IllegalArgumentException()
            }
        }

        /**
         * charArr转byteArr
         *
         * @param chars 字符数组
         * @return 字节数组
         */
        fun chars2Bytes(chars: CharArray?): ByteArray? {
            if (chars == null || chars.size <= 0) return null
            val len = chars.size
            val bytes = ByteArray(len)
            for (i in 0 until len) {
                bytes[i] = chars[i].toByte()
            }
            return bytes
        }

        /**
         * 以unit为单位的内存大小转字节数
         *
         * @param memorySize 大小
         * @param unit       单位类型
         *
         *  * [ConstUtils.MemoryUnit.BYTE]: 字节
         *  * [ConstUtils.MemoryUnit.KB]  : 千字节
         *  * [ConstUtils.MemoryUnit.MB]  : 兆
         *  * [ConstUtils.MemoryUnit.GB]  : GB
         *
         * @return 字节数
         */
        fun memorySize2Byte(memorySize: Long, unit: ConstUtils.MemoryUnit): Long {
            if (memorySize < 0) return -1
            when (unit) {
                ConstUtils.MemoryUnit.BYTE -> return memorySize
                ConstUtils.MemoryUnit.KB -> return memorySize * ConstUtils.KB
                ConstUtils.MemoryUnit.MB -> return memorySize * ConstUtils.MB
                ConstUtils.MemoryUnit.GB -> return memorySize * ConstUtils.GB
                else -> return memorySize
            }
        }

        /**
         * 字节数转以unit为单位的内存大小
         *
         * @param byteNum 字节数
         * @param unit    单位类型
         *
         *  * [ConstUtils.MemoryUnit.BYTE]: 字节
         *  * [ConstUtils.MemoryUnit.KB]  : 千字节
         *  * [ConstUtils.MemoryUnit.MB]  : 兆
         *  * [ConstUtils.MemoryUnit.GB]  : GB
         *
         * @return 以unit为单位的size
         */
        fun byte2MemorySize(byteNum: Long, unit: ConstUtils.MemoryUnit): Double {
            if (byteNum < 0) return -1.0
            when (unit) {
                ConstUtils.MemoryUnit.BYTE -> return byteNum.toDouble()
                ConstUtils.MemoryUnit.KB -> return byteNum.toDouble() / ConstUtils.KB
                ConstUtils.MemoryUnit.MB -> return byteNum.toDouble() / ConstUtils.MB
                ConstUtils.MemoryUnit.GB -> return byteNum.toDouble() / ConstUtils.GB
                else -> return byteNum.toDouble()
            }
        }

        /**
         * 字节数转合适内存大小
         *
         * 保留3位小数
         *
         * @param byteNum 字节数
         * @return 合适内存大小
         */
        @SuppressLint("DefaultLocale")
        fun byte2FitMemorySize(byteNum: Long): String {
            return if (byteNum < 0) {
                "0 K"
            } else if (byteNum < ConstUtils.KB) {
                String.format("%.1fB", byteNum + 0.0005)
            } else if (byteNum < ConstUtils.MB) {
                String.format("%.1fKB", byteNum / ConstUtils.KB + 0.0005)
            } else if (byteNum < ConstUtils.GB) {
                String.format("%.1fMB", byteNum / ConstUtils.MB + 0.0005)
            } else {
                String.format("%.1fGB", byteNum / ConstUtils.GB + 0.0005)
            }
        }

        /**
         * 以unit为单位的时间长度转毫秒时间戳
         *
         * @param timeSpan 毫秒时间戳
         * @param unit     单位类型
         *
         *  * [ConstUtils.TimeUnit.MSEC]: 毫秒
         *  * [ConstUtils.TimeUnit.SEC]: 秒
         *  * [ConstUtils.TimeUnit.MIN]: 分
         *  * [ConstUtils.TimeUnit.HOUR]: 小时
         *  * [ConstUtils.TimeUnit.DAY]: 天
         *
         * @return 毫秒时间戳
         */
        fun timeSpan2Millis(timeSpan: Long, unit: ConstUtils.TimeUnit): Long {
            when (unit) {
                ConstUtils.TimeUnit.MSEC -> return timeSpan
                ConstUtils.TimeUnit.SEC -> return timeSpan * ConstUtils.SEC
                ConstUtils.TimeUnit.MIN -> return timeSpan * ConstUtils.MIN
                ConstUtils.TimeUnit.HOUR -> return timeSpan * ConstUtils.HOUR
                ConstUtils.TimeUnit.DAY -> return timeSpan * ConstUtils.DAY
                else -> return timeSpan
            }
        }

        /**
         * 毫秒时间戳转以unit为单位的时间长度
         *
         * @param millis 毫秒时间戳
         * @param unit   单位类型
         *
         *  * [ConstUtils.TimeUnit.MSEC]: 毫秒
         *  * [ConstUtils.TimeUnit.SEC]: 秒
         *  * [ConstUtils.TimeUnit.MIN]: 分
         *  * [ConstUtils.TimeUnit.HOUR]: 小时
         *  * [ConstUtils.TimeUnit.DAY]: 天
         *
         * @return 以unit为单位的时间长度
         */
        fun millis2TimeSpan(millis: Long, unit: ConstUtils.TimeUnit): Long {
            when (unit) {
                ConstUtils.TimeUnit.MSEC -> return millis
                ConstUtils.TimeUnit.SEC -> return millis / ConstUtils.SEC
                ConstUtils.TimeUnit.MIN -> return millis / ConstUtils.MIN
                ConstUtils.TimeUnit.HOUR -> return millis / ConstUtils.HOUR
                ConstUtils.TimeUnit.DAY -> return millis / ConstUtils.DAY
                else -> return millis
            }
        }

        /**
         * 毫秒时间戳转合适时间长度
         *
         * @param millis    毫秒时间戳
         *
         * 小于等于0，返回null
         * @param precision 精度
         *
         *  * precision = 0，返回null
         *  * precision = 1，返回天
         *  * precision = 2，返回天和小时
         *  * precision = 3，返回天、小时和分钟
         *  * precision = 4，返回天、小时、分钟和秒
         *  * precision &gt;= 5，返回天、小时、分钟、秒和毫秒
         *
         * @return 合适时间长度
         */
        @SuppressLint("DefaultLocale")
        fun millis2FitTimeSpan(millis: Long, precision: Int): String? {
            var millis = millis
            var precision = precision
            if (millis <= 0 || precision <= 0) return null
            val sb = StringBuilder()
            val units = arrayOf("天", "小时", "分钟", "秒", "毫秒")
            val unitLen = intArrayOf(86400000, 3600000, 60000, 1000, 1)
            precision = Math.min(precision, 5)
            for (i in 0 until precision) {
                if (millis >= unitLen[i]) {
                    val mode = millis / unitLen[i]
                    millis -= mode * unitLen[i]
                    sb.append(mode).append(units[i])
                }
            }
            return sb.toString()
        }


        /**
         * byteArr转inputStream
         *
         * @param bytes 字节数组
         * @return 输入流
         */
        fun bytes2InputStream(bytes: ByteArray?): InputStream? {
            return if (bytes == null || bytes.size <= 0) null else ByteArrayInputStream(bytes)
        }

        /**
         * outputStream转byteArr
         *
         * @param out 输出流
         * @return 字节数组
         */
        fun outputStream2Bytes(out: OutputStream?): ByteArray? {
            return if (out == null) null else (out as ByteArrayOutputStream).toByteArray()
        }

        /**
         * outputStream转byteArr
         *
         * @param bytes 字节数组
         * @return 字节数组
         */
        fun bytes2OutputStream(bytes: ByteArray?): OutputStream? {
            if (bytes == null || bytes.size <= 0) return null
            var os: ByteArrayOutputStream? = null
            try {
                os = ByteArrayOutputStream()
                os.write(bytes)
                return os
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            } finally {
                CloseUtils.closeIO(os!!)
            }
        }

        /**
         * string转inputStream按编码
         *
         * @param string      字符串
         * @param charsetName 编码格式
         * @return 输入流
         */
        fun string2InputStream(string: String?, charsetName: String): InputStream? {
            if (string == null || StringUtils.isSpace(charsetName)) return null
            try {
                return ByteArrayInputStream(string.toByteArray(charset(charsetName)))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                return null
            }

        }

        /**
         * string转outputStream按编码
         *
         * @param string      字符串
         * @param charsetName 编码格式
         * @return 输入流
         */
        fun string2OutputStream(string: String?, charsetName: String): OutputStream? {
            if (string == null || StringUtils.isSpace(charsetName)) return null
            try {
                return bytes2OutputStream(string.toByteArray(charset(charsetName)))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                return null
            }

        }

        /**
         * bitmap转byteArr
         *
         * @param bitmap bitmap对象
         * @param format 格式
         * @return 字节数组
         */
        fun bitmap2Bytes(bitmap: Bitmap?, format: Bitmap.CompressFormat): ByteArray? {
            if (bitmap == null) return null
            val baos = ByteArrayOutputStream()
            bitmap.compress(format, 100, baos)
            return baos.toByteArray()
        }

        /**
         * byteArr转bitmap
         *
         * @param bytes 字节数组
         * @return bitmap
         */
        fun bytes2Bitmap(bytes: ByteArray?): Bitmap? {
            return if (bytes == null || bytes.size == 0) null else BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        /**
         * drawable转bitmap
         *
         * @param drawable drawable对象
         * @return bitmap
         */
        fun drawable2Bitmap(drawable: Drawable?): Bitmap? {
            return if (drawable == null) null else (drawable as BitmapDrawable).bitmap
        }

        /**
         * bitmap转drawable
         *
         * @param res    resources对象
         * @param bitmap bitmap对象
         * @return drawable
         */
        fun bitmap2Drawable(res: Resources, bitmap: Bitmap?): Drawable? {
            return if (bitmap == null) null else BitmapDrawable(res, bitmap)
        }

        /**
         * drawable转byteArr
         *
         * @param drawable drawable对象
         * @param format   格式
         * @return 字节数组
         */
        fun drawable2Bytes(drawable: Drawable?, format: Bitmap.CompressFormat): ByteArray? {
            return if (drawable == null) null else bitmap2Bytes(drawable2Bitmap(drawable), format)
        }

        /**
         * byteArr转drawable
         *
         * @param res   resources对象
         * @param bytes 字节数组
         * @return drawable
         */
        fun bytes2Drawable(res: Resources?, bytes: ByteArray): Drawable? {
            return if (res == null) null else bitmap2Drawable(res, bytes2Bitmap(bytes))
        }

        /**
         * dp转px
         *
         * @param dpValue dp值
         * @return px值
         */
        fun dp2px(dpValue: Float): Int {
            val scale = Utils.getContext().resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        /**
         * px转dp
         *
         * @param pxValue px值
         * @return dp值
         */
        fun px2dp(pxValue: Float): Int {
            val scale = Utils.getContext().resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }

        /**
         * sp转px
         *
         * @param spValue sp值
         * @return px值
         */
        fun sp2px(spValue: Float): Int {
            val fontScale = Utils.getContext().resources.displayMetrics.scaledDensity
            return (spValue * fontScale + 0.5f).toInt()
        }

        /**
         * px转sp
         *
         * @param pxValue px值
         * @return sp值
         */
        fun px2sp(pxValue: Float): Int {
            val fontScale = Utils.getContext().resources.displayMetrics.scaledDensity
            return (pxValue / fontScale + 0.5f).toInt()
        }
    }
}
