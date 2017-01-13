package com.zpauly.androidutilcodewithkotlin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 * Created by zpauly on 2017/1/8.
 */

/**
 * 判断是否存在Activity
 *
 * @param context 上下文
 * @param packageName 包名
 * @param className activity全路径名
 * @return {@code true}是  {@code false}否
 */
fun isActivityExists(context: Context, packageName: String, className: String): Boolean {
    val intent = Intent()
    intent.setClassName(packageName, className)
    return !(context.packageManager.resolveActivity(intent, 0) == null ||
            intent.resolveActivity(context.packageManager) == null ||
            context.packageManager.queryIntentActivities(intent, 0) == null)
}

/**
 * 打开Activity
 *
 * @param context 上下文
 * @param packageName 包名
 * @param className activity全路径名
 */
fun launchActivity(context: Context, packageName: String, className: String)
        = launchActivity(context, packageName, className, null)


/**
 * 打开Activity
 *
 * @param context 上下文
 * @param packageName 包名
 * @param className activity全路径名
 * @param bundle      bundle
 */
fun launchActivity(context: Context, packageName: String, className: String, bundle: Bundle?)
        = context.startActivity(getComponentIntent(packageName, className, bundle))

/**
 * 获取LauncherActivity
 *
 * @param context 上下文
 * @param packageName 包名
 * @return launcher activity
 */
fun Activity.getLauncherActivity(context: Context, packageName: String): String {
    val intent = Intent(Intent.ACTION_MAIN, null)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    val pm = context.packageManager
    val infos = pm.queryIntentActivities(intent, 0)
    infos.filter { it.activityInfo.packageName == packageName }
            .forEach { return it.activityInfo.name }
    return "no $packageName"
}