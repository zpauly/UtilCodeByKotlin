package com.zpauly.androidutilcodewithkotlin

import android.content.Context
import java.io.File

/**
 * Created by zpauly on 2017/1/18.
 */
/**
 * 清除内部缓存
 *
 * @param context 上下文
 * @return boolean
 */
fun cleanInternalCache(context: Context): Boolean =
        deleteFilesInDir(context.cacheDir)

/**
 * 清除内部文件
 *
 * @param context 上下文
 * @return boolean
 */
fun cleanInternalFiles(context: Context): Boolean =
        deleteFilesInDir(context.filesDir)

/**
 * 清除内部数据库
 *
 * @param context 上下文
 * @return boolean
 */
fun cleanInternalDbs(context: Context): Boolean =
        deleteFilesInDir("${context.filesDir.parent}${File.separator}databases")

/**
 * 根据名称删除数据库
 *
 * @param context 上下文
 * @param dbName 数据库名
 * @return boolean
 */
fun cleanInternalDbByName(context: Context, dbName: String): Boolean =
        context.deleteDatabase(dbName)

/**
 * 清除内部SP
 *
 * @param context 上下文
 * @return boolean
 */
fun cleanInternalSP(context: Context): Boolean =
        deleteFilesInDir("${context.filesDir.parent}${File.separator}shared_prefs")

/**
 * 清除外部储存
 *
 * @param context 上下文
 * @return boolean
 */
fun cleanExternalCache(context: Context): Boolean =
        isSDCardEnable() && deleteFilesInDir(context.externalCacheDir)

/**
 * 清除自定义目录下的文件
 *
 * @param dirPath 目录路径
 * @return boolean
 */
fun cleanCustomCache(dirPath: String): Boolean =
        deleteFilesInDir((dirPath))

/**
 * 清除自定义目录下的文件
 *
 * @param dir 目录
 * @return boolean
 */
fun cleanCustomCache(dir: File): Boolean =
        deleteFilesInDir(dir)

