package com.zpauly.androidutilcodewithkotlin

/**
 * Created by zpauly on 2017/1/8.
 */
/**
 * 判断字符串是否为null或q全为空格
 */
fun isSpace(s: String?): Boolean
        = (s == null || s.trim().isEmpty())