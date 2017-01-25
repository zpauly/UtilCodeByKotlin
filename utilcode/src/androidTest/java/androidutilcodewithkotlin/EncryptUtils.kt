package com.zpauly.androidutilcodewithkotlin

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by zpauly on 2017/1/14.
 */
/**
 * SHA1加密
 *
 * @param data 明文字节数组
 * @return 16进制密文
 */
fun encryptSHA1ToString(data: String?): String?
        = encryptSHA1ToString(data?.toByteArray())

/**
 * SHA1加密
 *
 * @param data 明文字节数组
 * @return 16进制密文
 */
fun encryptSHA1ToString(data: ByteArray?): String?
        = byte2HexString(encryptSHA1(data))

/**
 * SHA1加密
 *
 * @param data 明文字节数组
 * @return 密文字节数组
 */
fun encryptSHA1(data: ByteArray?): ByteArray?
        = hashTemplate(data, "SHA1")

/**
 * hash加密模版
 *
 * @param data 数据
 * @param algorithm 加密算法
 * @return 密文字节数组
 */
fun hashTemplate(data: ByteArray?, algorithm: String): ByteArray? {
    if (data == null || data.isEmpty()) {
        return null
    } else {
        try {
            val md = MessageDigest.getInstance(algorithm)
            md.update(data)
            return data
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        }
    }
}
