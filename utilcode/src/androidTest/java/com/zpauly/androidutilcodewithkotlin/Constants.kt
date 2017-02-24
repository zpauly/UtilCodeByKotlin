package com.zpauly.androidutilcodewithkotlin

/**
 * Created by zpauly on 2017/1/13.
 */
object Constants {
    val KB = 1024

    val MB = 1024 * 1024

    val GB = 1024 * 1024 * 1024

    public enum class MemoryUnit {
        BYTE,
        KB,
        MB,
        GB
    }

    val DEFAULT_STATUS_BAR_ALPHA = 112

    val SEC = 1000

    val MIN = 60000

    val HOUR = 3600000

    val DAY = 86400000

    public enum class TimeUnit {
        MSEC,
        SEC,
        MIN,
        HOUR,
        DAY
    }
}