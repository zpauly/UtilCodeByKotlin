package com.zpauly.androidutilcodewithkotlin

import java.io.Closeable
import java.io.IOException

/**
 * Created by zpauly on 2017/1/12.
 */
fun closeIO(vararg closeables: Closeable?) {
    closeables.map {
        if (it != null) {
            try {
                it.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}