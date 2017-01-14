package com.zpauly.androidutilcodewithkotlin

/**
 * Created by zpauly on 2017/1/8.
 */
/**
 * 判断字符串是否为null或q全为空格
 */
fun isSpace(s: String?): Boolean
        = (s == null || s.trim().isEmpty())

/**
 * 转化为半角字符
 *
 * @return 半角字符串
 */
fun String.toDBC(): String {
    if (isEmpty()) {
        return this
    }
    val chars = toCharArray()
    for (i in 0..chars.size) {
        if (chars[i].toInt() == 12288) {
            chars[i] = ' '
        } else if (65281 <= chars[i].toInt() && chars[i].toInt() <= 65374) {
            chars[i] = chars[i] - 65248
        }
    }

    return String(chars)
}

/**
 * 转化为全角字符串
 *
 * @return 全角字符串
 */
fun String.toSBC(): String {
    if (isEmpty()) {
        return this
    }
    val chars = toCharArray()
    for (i in 0..chars.size - 1) {
        if (chars[i] == ' ') {
            chars[i] = 12288.toChar()
        } else if (33 <= chars[i].toInt() && chars[i].toInt() <= 126) {
            chars[i] = chars[i] + 65248
        }
    }

    return String(chars)
}