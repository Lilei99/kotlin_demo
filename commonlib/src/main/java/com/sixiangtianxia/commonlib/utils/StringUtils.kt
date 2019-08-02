package com.sixiangtianxia.commonlib.utils

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan

import java.math.BigDecimal
import java.util.Arrays
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Description: 字符串相关工具类.
 */
class StringUtils private constructor() {

    init {
        throw UnsupportedOperationException("Can't instantiate me...")
    }

    companion object {

        /**
         * 判断字符串是否为null或长度为0
         *
         * @param s 待校验字符串
         * @return `true`: 空<br></br> `false`: 不为空
         */
        fun isEmpty(s: CharSequence?): Boolean {
            return s == null || s.length == 0
        }

        /**
         * 判断字符串是否为null或全为空格
         *
         * @param s 待校验字符串
         * @return `true`: null或全空格<br></br> `false`: 不为null且不全空格
         */
        fun isSpace(s: String?): Boolean {
            return s == null || s.trim { it <= ' ' }.length == 0
        }

        /**
         * 判断两字符串是否相等
         *
         * @param a 待校验字符串a
         * @param b 待校验字符串b
         * @return `true`: 相等<br></br>`false`: 不相等
         */
        fun equals(a: CharSequence?, b: CharSequence?): Boolean {
            if (a === b) return true
            val length: Int
            if (a != null && b != null) {
                length = a.length
                if (length == b.length) {
                    if (a is String && b is String) {
                        return a == b
                    } else {
                        for (i in 0 until length) {
                            if (a[i] != b[i]) return false
                        }
                        return true
                    }
                }
            }
            return false
        }

        /**
         * 判断两字符串忽略大小写是否相等
         *
         * @param a 待校验字符串a
         * @param b 待校验字符串b
         * @return `true`: 相等<br></br>`false`: 不相等
         */
        fun equalsIgnoreCase(a: String, b: String?): Boolean {
            return a === b || b != null && a.length == b.length && a.regionMatches(0, b, 0, b.length, ignoreCase = true)
        }

        /**
         * null转为长度为0的字符串
         *
         * @param s 待转字符串
         * @return s为null转为长度为0字符串，否则不改变
         */
        fun null2Length0(s: String?): String {
            return s ?: ""
        }

        /**
         * 返回字符串长度
         *
         * @param s 字符串
         * @return null返回0，其他返回自身长度
         */
        fun length(s: CharSequence?): Int {
            return s?.length ?: 0
        }

        /**
         * 首字母大写
         *
         * @param s 待转字符串
         * @return 首字母大写字符串
         */
        fun upperFirstLetter(s: String): String? {
            return if (isEmpty(s) || !Character.isLowerCase(s[0])) s else (s[0].toInt() - 32).toChar().toString() + s.substring(
                1
            )
        }

        /**
         * 首字母小写
         *
         * @param s 待转字符串
         * @return 首字母小写字符串
         */
        fun lowerFirstLetter(s: String): String? {
            return if (isEmpty(s) || !Character.isUpperCase(s[0])) s else (s[0].toInt() + 32).toChar().toString() + s.substring(
                1
            )
        }

        /**
         * 反转字符串
         *
         * @param s 待反转字符串
         * @return 反转字符串
         */
        fun reverse(s: String): String {
            val len = length(s)
            if (len <= 1) return s
            val mid = len shr 1
            val chars = s.toCharArray()
            var c: Char
            for (i in 0 until mid) {
                c = chars[i]
                chars[i] = chars[len - i - 1]
                chars[len - i - 1] = c
            }
            return String(chars)
        }

        /**
         * 转化为半角字符
         *
         * @param s 待转字符串
         * @return 半角字符串
         */
        fun toDBC(s: String): String? {
            if (isEmpty(s)) return s
            val chars = s.toCharArray()
            var i = 0
            val len = chars.size
            while (i < len) {
                if (chars[i].toInt() == 12288) {
                    chars[i] = ' '
                } else if (65281 <= chars[i].toInt() && chars[i].toInt() <= 65374) {
                    chars[i] = (chars[i].toInt() - 65248).toChar()
                } else {
                    chars[i] = chars[i]
                }
                i++
            }
            return String(chars)
        }

        /**
         * 转化为全角字符
         *
         * @param s 待转字符串
         * @return 全角字符串
         */
        fun toSBC(s: String): String? {
            if (isEmpty(s)) return s
            val chars = s.toCharArray()
            var i = 0
            val len = chars.size
            while (i < len) {
                if (chars[i] == ' ') {
                    chars[i] = 12288.toChar()
                } else if (33 <= chars[i].toInt() && chars[i].toInt() <= 126) {
                    chars[i] = (chars[i].toInt() + 65248).toChar()
                } else {
                    chars[i] = chars[i]
                }
                i++
            }
            return String(chars)
        }


        fun toString(`object`: Any?): String {
            if (`object` == null) {
                return "null"
            }
            if (!`object`.javaClass.isArray) {
                return `object`.toString()
            }
            if (`object` is BooleanArray) {
                return Arrays.toString(`object` as BooleanArray?)
            }
            if (`object` is ByteArray) {
                return Arrays.toString(`object` as ByteArray?)
            }
            if (`object` is CharArray) {
                return Arrays.toString(`object` as CharArray?)
            }
            if (`object` is ShortArray) {
                return Arrays.toString(`object` as ShortArray?)
            }
            if (`object` is IntArray) {
                return Arrays.toString(`object` as IntArray?)
            }
            if (`object` is LongArray) {
                return Arrays.toString(`object` as LongArray?)
            }
            if (`object` is FloatArray) {
                return Arrays.toString(`object` as FloatArray?)
            }
            if (`object` is DoubleArray) {
                return Arrays.toString(`object` as DoubleArray?)
            }
            return if (`object` is Any) {
                Arrays.deepToString(`object` as Array<Any>?)
            } else "Couldn't find a correct type for the object"
        }

        fun getDoubleNumber(number: Double): String {
            if (number < 10000) {
                return number.toString()
            } else {
                val num = number / 10000//1.将数字转换成以万为单位的数字
                val b = BigDecimal(num)
                val f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).toDouble()//2.转换后的数字四舍五入保留小数点后一位;
                return f1.toString() + "w"
            }
        }

        fun getIntNumber(number: Int): String {
            if (number < 10000) {
                return number.toString()
            } else {
                val num = (number / 10000).toDouble()//1.将数字转换成以万为单位的数字
                val b = BigDecimal(num)
                val f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).toDouble()//2.转换后的数字四舍五入保留小数点后一位;
                return f1.toString() + "w"
            }
        }

        /**
         * 字体颜色选中
         */
        fun matcherSearchText(color: Int, text: String, keyword: String): SpannableString {
            val ss = SpannableString(text)
            val pattern = Pattern.compile(keyword)
            val matcher = pattern.matcher(ss)
            while (matcher.find()) {
                val start = matcher.start()
                val end = matcher.end()
                ss.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            return ss
        }

        /**
         * 空值判断
         * @param data
         * @return
         */
        fun getValue(data: String): String {
            return if (isEmpty(data)) "" else data

        }
    }

}