package com.zpauly.androidutilcodewithkotlin

import android.annotation.TargetApi
import android.os.Build
import android.os.Environment
import android.os.StatFs
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * Created by zpauly on 2017/1/20.
 */
data class SDCardInfo(var isExist: Boolean = false,
                      var totalBlocks: Long = -1,
                      var freeBlocks: Long = -1,
                      var availableBlocks: Long = -1,
                      var blockByteSize: Long = -1,
                      var totalBytes: Long = -1,
                      var freeBytes: Long = -1,
                      var availableBytes: Long = -1)

/**
 * 判断SD卡是否可用
 *
 * @return boolean
 */
fun isSDCardEnable(): Boolean
        = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

/**
 * 获取SD卡的路径
 *
 * @return SD卡的路径
 */
fun getSDCardPath(): String? =
    if (!isSDCardEnable()) {
        null
    } else {
        val command = "car /proc/mounts"
        val runtime = Runtime.getRuntime()
        var bufferReader: BufferedReader? = null
        try {
            val p = runtime.exec(command)
            bufferReader = BufferedReader(InputStreamReader(p.inputStream))
            var line = bufferReader.readLine()
            while (line != null) {
                if (line.contains("sdcard") && line.contains(".android_secure")) {
                    val strArray = line.split(" ")
                    if (strArray.size >= 5) {
                        strArray[1].replace("/.android_secure", "") + File.separator
                    }
                }
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeIO(bufferReader)
        }
        Environment.getExternalStorageDirectory().path
    }

/**
 * 获取SD卡data路径
 *
 * @return SD卡data路径
 */
fun getDataPath(): String? =
        if (!isSDCardEnable()) null else Environment.getExternalStorageDirectory().path

/**
 * 获取SD卡的剩余空间
 *
 * @return SD卡的剩余空间
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun getSDCardFreeSpace(): String? =
    if (!isSDCardEnable()) {
        null
    } else {
        val statFs = StatFs(getSDCardPath())
        byte2FitMemorySize(statFs.availableBlocksLong * statFs.blockSizeLong)
    }

/**
 * 获取SD卡信息
 *
 * @return SD卡信息
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun getSDCardInfo(): SDCardInfo =
    if (!isSDCardEnable()) {
        SDCardInfo()
    } else {
        val statFs = StatFs(getSDCardPath())
        SDCardInfo(true,
                statFs.blockCountLong,
                statFs.freeBlocksLong,
                statFs.availableBlocksLong,
                statFs.blockSizeLong,
                statFs.totalBytes,
                statFs.freeBytes,
                statFs.availableBytes)
    }