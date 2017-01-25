package com.zpauly.androidutilcodewithkotlin

import java.io.*

/**
 * Created by zpauly on 2017/1/13.
 */
private val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

/**
 * byteArr转hexString
 *
 * @param bytes 字节数组
 * @return 16进制大写字符数组
 */
fun byte2HexString(bytes: ByteArray?): String? {
    if (bytes == null) {
        return null
    }
    val len = bytes.size
    if (len <= 0) {
        return null
    }
    val ret = CharArray(len shl 1)
    var j = 0
    for (i in 0..len - 1) {
        ret[j++] = hexDigits[bytes[i].toInt() ushr 4 and 0x0f]
        ret[j++] = hexDigits[bytes[i].toInt() and 0x0f]
    }
    return String(ret)
}

/**
 * 字节数转合适的内存大小
 *
 * @param byteNum 字节数
 * @return 合适的内存大小
 */
fun byte2FitMemorySize(byteNum: Long): String
        = when {
    byteNum < 0 -> "shouldn't be less than zero"
    byteNum < Constants.KB -> String.format("%.3fB", byteNum + 0.0005)
    byteNum < Constants.MB -> String.format("%.3fKB", byteNum / Constants.KB)
    byteNum < Constants.GB -> String.format("%.3fMB", byteNum / Constants.MB)
    else -> String.format("%.3fGB", byteNum / Constants.GB)
}

/**
 * inputStream转outputStream
 *
 * @param inputStream 输入流
 * @return outputStream子类
 */
fun input2OutputStream(inputStream: InputStream?): ByteArrayOutputStream? {
    if (inputStream == null) {
        return null
    }
    try {
        val outputStream = ByteArrayOutputStream()
        val bytes = ByteArray(Constants.KB)
        var len = inputStream.read(bytes, 0, Constants.KB)
        while (len != -1) {
            outputStream.write(bytes, 0, len)
            len = inputStream.read(bytes, 0, Constants.KB)
        }
        return outputStream
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    } finally {
        closeIO(inputStream)
    }
}

/**
 * outputStream转inputStream
 *
 * @param outputStream
 * @return inputStream子类
 */
fun output2InputSteam(outputStream: OutputStream?): ByteArrayInputStream? {
    if (outputStream == null) {
        return null
    } else {
        return ByteArrayInputStream((outputStream as ByteArrayOutputStream).toByteArray())
    }
}

/**
 * inputStream转byteArr
 */
fun inputStream2Bytes(inputStream: InputStream?): ByteArray? {
    if (inputStream == null) {
        return null
    } else {
        return input2OutputStream(inputStream)?.toByteArray()
    }
}

/**
 * inputStream转byteArr
 *
 * @param out 输出流
 * @return 字节数组
 */
fun outputStream2Bytes(out: OutputStream?): ByteArray? {
    if (out == null) {
        return null
    } else {
        return (out as ByteArrayOutputStream).toByteArray()
    }
}