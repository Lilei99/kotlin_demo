package com.sixiangtianxia.commonlib.utils

import android.annotation.SuppressLint
import android.util.Log

import java.sql.Timestamp
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Creator: Gao MinMin.
 * Date: 2018/11/27.
 * Description: 时间相关工具类.
 */
class TimeUtils {

    init {
        throw UnsupportedOperationException("Can't instantiate me...")
    }

    companion object {

        /**
         *
         * 在工具类中经常使用到工具类的格式化描述，这个主要是一个日期的操作类，所以日志格式主要使用 SimpleDateFormat的定义格式.
         * 格式的意义如下： 日期和时间模式 <br></br>
         *
         * 日期和时间格式由日期和时间模式字符串指定。在日期和时间模式字符串中，未加引号的字母 'A' 到 'Z' 和 'a' 到 'z'
         * 被解释为模式字母，用来表示日期或时间字符串元素。文本可以使用单引号 (') 引起来，以免进行解释。"''"
         * 表示单引号。所有其他字符均不解释；只是在格式化时将它们简单复制到输出字符串，或者在分析时与输入字符串进行匹配。
         *
         * 定义了以下模式字母（所有其他字符 'A' 到 'Z' 和 'a' 到 'z' 都被保留）： <br></br>
         * <table border="1" cellspacing="1" cellpadding="1" summary="Chart shows pattern letters, date/time component,
        presentation, and examples.">
         * <tr>
         * <th align="left">字母</th>
         * <th align="left">日期或时间元素</th>
         * <th align="left">表示</th>
         * <th align="left">示例</th>
        </tr> *
         * <tr>
         * <td>`G`</td>
         * <td>Era 标志符</td>
         * <td>Text</td>
         * <td>`AD`</td>
        </tr> *
         * <tr>
         * <td>`y` </td>
         * <td>年 </td>
         * <td>Year </td>
         * <td>`1996`; `96` </td>
        </tr> *
         * <tr>
         * <td>`M` </td>
         * <td>年中的月份 </td>
         * <td>Month </td>
         * <td>`July`; `Jul`; `07` </td>
        </tr> *
         * <tr>
         * <td>`w` </td>
         * <td>年中的周数 </td>
         * <td>Number </td>
         * <td>`27` </td>
        </tr> *
         * <tr>
         * <td>`W` </td>
         * <td>月份中的周数 </td>
         * <td>Number </td>
         * <td>`2` </td>
        </tr> *
         * <tr>
         * <td>`D` </td>
         * <td>年中的天数 </td>
         * <td>Number </td>
         * <td>`189` </td>
        </tr> *
         * <tr>
         * <td>`d` </td>
         * <td>月份中的天数 </td>
         * <td>Number </td>
         * <td>`10` </td>
        </tr> *
         * <tr>
         * <td>`F` </td>
         * <td>月份中的星期 </td>
         * <td>Number </td>
         * <td>`2` </td>
        </tr> *
         * <tr>
         * <td>`E` </td>
         * <td>星期中的天数 </td>
         * <td>Text </td>
         * <td>`Tuesday`; `Tue` </td>
        </tr> *
         * <tr>
         * <td>`a` </td>
         * <td>Am/pm 标记 </td>
         * <td>Text </td>
         * <td>`PM` </td>
        </tr> *
         * <tr>
         * <td>`H` </td>
         * <td>一天中的小时数（0-23） </td>
         * <td>Number </td>
         * <td>`0` </td>
        </tr> *
         * <tr>
         * <td>`k` </td>
         * <td>一天中的小时数（1-24） </td>
         * <td>Number </td>
         * <td>`24` </td>
        </tr> *
         * <tr>
         * <td>`K` </td>
         * <td>am/pm 中的小时数（0-11） </td>
         * <td>Number </td>
         * <td>`0` </td>
        </tr> *
         * <tr>
         * <td>`h` </td>
         * <td>am/pm 中的小时数（1-12） </td>
         * <td>Number </td>
         * <td>`12` </td>
        </tr> *
         * <tr>
         * <td>`m` </td>
         * <td>小时中的分钟数 </td>
         * <td>Number </td>
         * <td>`30` </td>
        </tr> *
         * <tr>
         * <td>`s` </td>
         * <td>分钟中的秒数 </td>
         * <td>Number </td>
         * <td>`55` </td>
        </tr> *
         * <tr>
         * <td>`S` </td>
         * <td>毫秒数 </td>
         * <td>Number </td>
         * <td>`978` </td>
        </tr> *
         * <tr>
         * <td>`z` </td>
         * <td>时区 </td>
         * <td>General time zone </td>
         * <td>`Pacific Standard Time`; `PST`; `GMT-08:00` </td>
        </tr> *
         * <tr>
         * <td>`Z` </td>
         * <td>时区 </td>
         * <td>RFC 822 time zone </td>
         * <td>`-0800` </td>
        </tr> *
        </table> *
         * <pre>
         * HH:mm    15:44
         * h:mm a    3:44 下午
         * HH:mm z    15:44 CST
         * HH:mm Z    15:44 +0800
         * HH:mm zzzz    15:44 中国标准时间
         * HH:mm:ss    15:44:40
         * yyyy-MM-dd    2016-08-12
         * yyyy-MM-dd HH:mm    2016-08-12 15:44
         * yyyy-MM-dd HH:mm:ss    2016-08-12 15:44:40
         * yyyy-MM-dd HH:mm:ss zzzz    2016-08-12 15:44:40 中国标准时间
         * EEEE yyyy-MM-dd HH:mm:ss zzzz    星期五 2016-08-12 15:44:40 中国标准时间
         * yyyy-MM-dd HH:mm:ss.SSSZ    2016-08-12 15:44:40.461+0800
         * yyyy-MM-dd'T'HH:mm:ss.SSSZ    2016-08-12T15:44:40.461+0800
         * yyyy.MM.dd G 'at' HH:mm:ss z    2016.08.12 公元 at 15:44:40 CST
         * K:mm a    3:44 下午
         * EEE, MMM d, ''yy    星期五, 八月 12, '16
         * hh 'o''clock' a, zzzz    03 o'clock 下午, 中国标准时间
         * yyyyy.MMMMM.dd GGG hh:mm aaa    02016.八月.12 公元 03:44 下午
         * EEE, d MMM yyyy HH:mm:ss Z    星期五, 12 八月 2016 15:44:40 +0800
         * yyMMddHHmmssZ    160812154440+0800
         * yyyy-MM-dd'T'HH:mm:ss.SSSZ    2016-08-12T15:44:40.461+0800
         * EEEE 'DATE('yyyy-MM-dd')' 'TIME('HH:mm:ss')' zzzz    星期五 DATE(2016-08-12) TIME(15:44:40) 中国标准时间
        </pre> *
         * 注意：SimpleDateFormat不是线程安全的，线程安全需用`ThreadLocal<SimpleDateFormat>`
         */
        val DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss"
        val PATTERN1 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        val PATTERN_TAKE_PHOTO = "yyMMddHHmmss"
        // 国际时间格式.
        val PATTERN_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSS"
        val PATTERN_VIDEO_COMMENT = "yyyy-MM-dd HH:mm:ss.SSSZ"
        val PATTERN_MONTH_DAY = "MM-dd"
        val PATTERN_YEAR = "yyyy"
        val PATTERN_MONTH = "MM"
        val PATTERN_DAY = "dd"
        val PATTERN_YEAR_MONTH_DAY = "yyyy-MM-dd"
        val PATTERN_YEAR_MONTH = "yyyy-MM"

        /**
         * 将时间戳转为时间字符串
         *
         * 格式为yyyy-MM-dd HH:mm:ss
         *
         * @param millis 毫秒时间戳
         * @return 时间字符串
         */
        fun millis2String(millis: Long): String {
            return SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault()).format(Date(millis))
        }

        /**
         * 将时间戳转为时间字符串
         *
         * 格式为pattern
         *
         * @param millis  毫秒时间戳
         * @param pattern 时间格式
         * @return 时间字符串
         */
        fun millis2String(millis: Long, pattern: String): String {
            return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(millis))
        }

        /**
         * 将时间字符串转为时间戳
         *
         * time格式为pattern
         *
         * @param time    时间字符串
         * @param pattern 时间格式
         * @return 毫秒时间戳
         */
        @JvmOverloads
        fun string2Millis(time: String, pattern: String = DEFAULT_PATTERN): Long {
            try {
                return SimpleDateFormat(pattern, Locale.getDefault()).parse(time).time
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return -1
        }

        /**
         * 将时间字符串转为Date类型
         *
         * time格式为pattern
         *
         * @param time    时间字符串
         * @param pattern 时间格式
         * @return Date类型
         */
        @JvmOverloads
        fun string2Date(time: String, pattern: String = DEFAULT_PATTERN): Date {
            return Date(string2Millis(time, pattern))
        }

        fun string2String(time: String): Date {
            return Date(string2Millis(time, PATTERN1))
        }

        /**
         * 将国际时间转化为本地时间.
         *
         * @param utcTime    国际时间.
         * @param utcPattern 国际时间格式.
         * @param pattern    转化时间格式.
         */
        fun utcTime2LocalTile(utcTime: String, utcPattern: String, pattern: String): String? {
            try {
                val utcFormat = SimpleDateFormat(utcPattern)
                utcFormat.timeZone = TimeZone.getTimeZone("UTC")
                val localFormat = SimpleDateFormat(pattern)
                return localFormat.format(utcFormat.parse(utcTime))
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return null
        }

        /**
         * 将Date类型转为时间字符串
         *
         * 格式为pattern
         *
         * @param date    Date类型时间
         * @param pattern 时间格式
         * @return 时间字符串
         */
        @JvmOverloads
        fun date2String(date: Date, pattern: String = DEFAULT_PATTERN): String {
            return SimpleDateFormat(pattern, Locale.getDefault()).format(date)
        }

        /**
         * 将Date类型转为时间戳
         *
         * @param date Date类型时间
         * @return 毫秒时间戳
         */
        fun date2Millis(date: Date): Long {
            return date.time
        }

        /**
         * 将时间戳转为Date类型
         *
         * @param millis 毫秒时间戳
         * @return Date类型时间
         */
        fun millis2Date(millis: Long): Date {
            return Date(millis)
        }


        /**
         * 获取时间差
         */
        fun getTimeSpan(time1: String, time2: String, pattern: String): String {
            val df = SimpleDateFormat(pattern, Locale.CHINA)
            try {
                val d1 = df.parse(time1)
                val d2 = df.parse(time2)
                val diff = d2.time - d1.time//这样得到的差值是微秒级别
                val days = diff / (1000 * 60 * 60 * 24)
                val hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
                val minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60)
                val seconds = diff / 1000 - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60
                if (days != 0L) {
                    return "$days:$hours:$minutes:$seconds"
                } else if (hours != 0L) {
                    return "$hours:$minutes:$seconds"
                } else if (minutes != 0L) {
                    if (minutes >= 10 && seconds >= 10) {
                        return "$minutes:$seconds"
                    } else if (minutes >= 10 && seconds < 10) {
                        return "$minutes:0$seconds"
                    } else if (minutes < 10 && seconds < 10) {
                        return "0$minutes:0$seconds"
                    } else if (minutes < 10 && seconds >= 10) {
                        return "0$minutes:$seconds"
                    }
                } else if (seconds != 0L) {
                    return if (seconds < 10) {
                        "00:0$seconds"
                    } else {
                        "00:$seconds"

                    }
                }
            } catch (e: Exception) {
            }

            return ""
        }

        /**
         * 获取当前毫秒时间戳
         *
         * @return 毫秒时间戳
         */
        val nowTimeMills: Long
            get() = System.currentTimeMillis()

        /**
         * 获取当前时间字符串
         *
         * 格式为yyyy-MM-dd HH:mm:ss
         *
         * @return 时间字符串
         */
        val nowTimeString: String
            get() = millis2String(System.currentTimeMillis(), PATTERN_YEAR_MONTH_DAY)

        /**
         * 获取当前时间字符串
         *
         * 格式为pattern
         *
         * @param pattern 时间格式
         * @return 时间字符串
         */
        fun getNowTimeString(pattern: String): String {
            return millis2String(System.currentTimeMillis(), pattern)
        }

        /**
         * 获取当前Date
         *
         * @return Date类型时间
         */
        val nowTimeDate: Date
            get() = Date()


    }
}