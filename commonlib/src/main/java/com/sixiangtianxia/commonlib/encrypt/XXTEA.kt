package com.sixiangtianxia.commonlib.encrypt

import java.util.HashMap

object XXTEA {
    private val KEY = "[zHanG!Can#Mou%)"

    fun encrypt64(value: String): String {
        var enVid = ""
        val v = value.toByteArray()
        enVid = Base64.encode(XXTEA.encrypt(Base64.encode(v).toByteArray(), KEY.toByteArray())!!)
        enVid = enVid.replace('+', '-')
        enVid = enVid.replace('/', '_')
        enVid = enVid.replace('=', '.')
        return enVid
    }

    fun encrypt(value: String): String {
        var enVid = ""
        val v = value.toByteArray()
        enVid = Base64.encode(XXTEA.encrypt(v, KEY.toByteArray())!!)
        enVid = enVid.replace('+', '-')
        enVid = enVid.replace('/', '_')
        enVid = enVid.replace('=', '.')
        return enVid
    }

    fun encrypt(mapData: HashMap<String, String>): String {
        val stringBuffer = StringBuffer()
        val iterator = mapData.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            stringBuffer.append(entry.key + "=" + entry.value + "&")
        }
        val sign = stringBuffer.substring(0, stringBuffer.length - 1)
        return encrypt(sign)
    }

    /**
     * 先Base64解密，后 XXXTEA 解密
     *
     * @param value
     * @return
     */
    fun decrypt(value: String): String {
        var value = value
        val deVid: String
        value = value.replace('-', '+')
        value = value.replace('_', '/')
        value = value.replace('.', '=')
        val v = Base64.decode(value)
        deVid = String(XXTEA.decrypt(v)!!)
        return deVid
    }

    fun decrypt64(value: String): String {
        var value = value
        val deVid: String
        value = value.replace('-', '+')
        value = value.replace('_', '/')
        value = value.replace('.', '=')
        val v = Base64.decode(value)
        deVid = String(XXTEA.decrypt(v)!!)
        return String(Base64.decode(deVid))
    }

    /**
     * Encrypt data with key.
     *
     * @param data
     * @param key
     * @return
     */
    fun encrypt(data: ByteArray, key: ByteArray): ByteArray? {
        return if (data.size == 0) {
            data
        } else toByteArray(encrypt(toIntArray(data, true), toIntArray(key, false)), false)
    }

    /**
     * Decrypt data with key.
     *
     * @param data
     * @return
     */
    fun decrypt(data: ByteArray): ByteArray? {
        return if (data.size == 0) {
            data
        } else toByteArray(
            decrypt(
                toIntArray(data, false),
                toIntArray(KEY.toByteArray(), false)
            ), true
        )
    }

    /**
     * Encrypt data with key.
     *
     * @param v
     * @param k
     * @return
     */
    fun encrypt(v: IntArray, k: IntArray): IntArray {
        var k = k
        val n = v.size - 1
        if (n < 1) {
            return v
        }
        if (k.size < 4) {
            val key = IntArray(4)

            System.arraycopy(k, 0, key, 0, k.size)
            k = key
        }
        var z = v[n]
        var y = v[0]
        val delta = -0x61c88647
        var sum = 0
        var e: Int
        var p: Int
        var q = 6 + 52 / (n + 1)
        while (q-- > 0) {
            sum = sum + delta
            e = sum.ushr(2) and 3
            p = 0
            while (p < n) {
                y = v[p + 1]
                v[p] += (z.ushr(5) xor (y shl 2)) + (y.ushr(3) xor (z shl 4)) xor (sum xor y) + (k[p and 3 xor e] xor z)
                z = v[p]
                p++
            }
            y = v[0]
            v[n] += (z.ushr(5) xor (y shl 2)) + (y.ushr(3) xor (z shl 4)) xor (sum xor y) + (k[p and 3 xor e] xor z)
            z = v[n]
        }
        return v
    }

    /**
     * Decrypt data with key.
     *
     * @param v
     * @param k
     * @return
     */
    fun decrypt(v: IntArray, k: IntArray): IntArray {
        var k = k
        val n = v.size - 1

        if (n < 1) {
            return v
        }
        if (k.size < 4) {
            val key = IntArray(4)

            System.arraycopy(k, 0, key, 0, k.size)
            k = key
        }
        var z = v[n]
        var y = v[0]
        val delta = -0x61c88647
        var sum: Int
        var e: Int
        var p: Int
        val q = 6 + 52 / (n + 1)

        sum = q * delta
        while (sum != 0) {
            e = sum.ushr(2) and 3
            p = n
            while (p > 0) {
                z = v[p - 1]
                v[p] -= (z.ushr(5) xor (y shl 2)) + (y.ushr(3) xor (z shl 4)) xor (sum xor y) + (k[p and 3 xor e] xor z)
                y = v[p]
                p--
            }
            z = v[n]
            v[0] -= (z.ushr(5) xor (y shl 2)) + (y.ushr(3) xor (z shl 4)) xor (sum xor y) + (k[p and 3 xor e] xor z)
            y = v[0]
            sum = sum - delta
        }
        return v
    }

    /**
     * Convert byte array to int array.
     *
     * @param data
     * @param includeLength
     * @return
     */
    private fun toIntArray(data: ByteArray, includeLength: Boolean): IntArray {
        var n = if (data.size and 3 == 0)
            data.size.ushr(2)
        else
            data.size.ushr(2) + 1
        val result: IntArray

        if (includeLength) {
            result = IntArray(n + 1)
            result[n] = data.size
        } else {
            result = IntArray(n)
        }
        n = data.size
        for (i in 0 until n) {
            result[i.ushr(2)] = result[i.ushr(2)] or (0x000000ff and data[i].toInt() shl (i and 3 shl 3))
        }
        return result
    }

    /**
     * Convert int array to byte array.
     *
     * @param data
     * @param includeLength
     * @return
     */
    private fun toByteArray(data: IntArray, includeLength: Boolean): ByteArray? {
        var result = ByteArray(0)
        try {
            var n = data.size shl 2
            if (includeLength) {
                val m = data[data.size - 1]
                if (m > n) {
                    return null
                } else {
                    n = m
                }
            }
            result = ByteArray(n)

            for (i in 0 until n) {
                result[i] = (data[i.ushr(2)].ushr(i and 3 shl 3) and 0xff).toByte()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }
}
