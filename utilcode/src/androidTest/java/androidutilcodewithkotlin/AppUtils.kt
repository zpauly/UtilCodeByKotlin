package com.zpauly.androidutilcodewithkotlin

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.graphics.drawable.Drawable
import java.io.File
import java.util.*

/**
 * Created by zpauly on 2017/1/13.
 */
/**
 * 判断APP是否安装
 *
 * @param context 上下文
 * @param packageName 包名
 * @return boolean
 */
fun isInstallApp(context: Context, packageName: String): Boolean
        = !isSpace(packageName) && getLaunchAppIntent(context, packageName) != null

/**
 * 安装APP
 *
 * @param context 上下文
 * @param filePath 文件路径
 */
fun installApp(context: Context, filePath: String?) {
    installApp(context, getFileByPath(filePath))
}

/**
 * 安装APP
 *
 * @param context 上下文
 * @param file 文件
 */
fun installApp(context: Context, file: File?) {
    if (isFileExists(file)) {
        context.startActivity(getInstallAppIntent(file))
    }
}

/**
 * 安装APP
 *
 * @param activity activity
 * @param filePath 文件路径
 * @param requestCode 请求值
 */
fun installApp(activity: Activity, filePath: String?, requestCode: Int)
        = installApp(activity, getFileByPath(filePath), requestCode)

/**
 * 安装APP
 *
 * @param activity activity
 * @param file 文件
 * @param requestCode 请求值
 */
fun installApp(activity: Activity, file: File?, requestCode: Int) {
    if (!isFileExists(file)) {
        return
    } else {
        activity.startActivityForResult(getInstallAppIntent(file), requestCode)
    }
}

/**
 * 静默安装APP
 * 非root需添加权限 <uses-permission android:name="android.permission.INSTALL_PACKAGES" />
 *
 * @param context 上下文
 * @param filePath 文件路径
 * @return boolean
 */
fun installAppSilent(context: Context, filePath: String?): Boolean {
    val file = getFileByPath(filePath)
    if (!isFileExists(file)) {
        return false
    }
    val command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install $filePath"
    val commandResult = execCmd(command, !isSystemApp(context))
    return commandResult.successMsg?.contains("success") ?: false
}

/**
 * 卸载APP
 *
 * @param context 上下文
 * @param packageName 包名
 * @return boolean
 */
fun uninstallApp(context: Context, packageName: String) {
    if (isSpace(packageName)) {
        return
    } else {
        context.startActivity(getUninstallAppIntent(packageName))
    }
}

/**
 * 卸载APP
 *
 * @param activity activity
 * @param packageName 包名
 * @return boolean
 */
fun uninstallApp(activity: Activity, packageName: String, requestCode: Int) {
    if (isSpace(packageName)) {
        return
    } else {
        activity.startActivityForResult(getUninstallAppIntent(packageName), requestCode)
    }
}

/**
 * 静默卸载APP
 *
 * @param context 上下文
 * @param packageName 包名
 * @param isKeepData 是否保留数据
 * @return boolean
 */
fun uninstallAppSilent(context: Context, packageName: String, isKeepData: Boolean): Boolean {
    if (isSpace(packageName)) {
        return false
    } else {
        val command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall" +
                " ${if (isKeepData) "-k " else ""}$packageName"
        val commandResult = execCmd(command, !isSystemApp(context), true)
        return commandResult.successMsg?.contains("success") ?: false
    }
}

/**
 * 判断App是否有root权限
 *
 * @return boolean
 */
fun isAppRoot(): Boolean
        = execCmd("echo root", true).result == 0

/**
 * 打开APP
 *
 * @param context 上下文
 * @param packageName 包名
 */
fun launchApp(context: Context, packageName: String) {
    if (isSpace(packageName)) {
        return
    } else {
        context.startActivity(getLaunchAppIntent(context, packageName))
    }
}

/**
 * 打开APP
 *
 * @param activity activity
 * @param packageName 包名
 * @param requestCode 请求值
 */
fun launchApp(activity: Activity, packageName: String, requestCode: Int) {
    if (isSpace(packageName)) {
        return
    } else {
        activity.startActivityForResult(getLaunchAppIntent(activity, packageName), requestCode)
    }
}

/**
 * 获取APP包名
 *
 * @param context 上下文
 * @return 包名
 */
fun getAppPackageName(context: Context)
        = context.packageName

/**
 * 获取APP具体设置
 *
 * @param context 上下文
 * @param packageName 包名
 */
fun getAppDetailsSettings(context: Context, packageName: String = context.packageName) {
    if (isSpace(packageName)) {
        return
    } else {
        context.startActivity(getAppDetailsSettingsIntent(packageName))
    }
}

/**
 * 获取APP名称
 *
 * @param context 上下文
 * @param packageName 包名
 * @return App名称
 */
fun getAppName(context: Context, packageName: String = context.packageName): String? {
    if (isSpace(packageName)) {
        return null
    } else {
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            return pi?.applicationInfo?.loadLabel(pm).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return null
        }
    }
}

/**
 * 获取APP图标
 *
 * @param context 上下文
 * @param packageName 包名
 * @return App图标
 */
fun getAppIcon(context: Context, packageName: String = context.packageName): Drawable? {
    if (isSpace(packageName)) {
        return null
    } else {
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            return pi?.applicationInfo?.loadIcon(pm)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return null
        }
    }
}

/**
 * 获取APP路径
 *
 * @param context 上下文
 * @param packageName 包名
 * @return App路径
 */
fun getAppPath(context: Context, packageName: String = context.packageName): String? {
    if (isSpace(packageName)) {
        return null
    } else {
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            return pi?.applicationInfo?.sourceDir
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return null
        }
    }
}

/**
 * 获取APP版本号
 *
 * @param context 上下文
 * @param packageName 包名
 * @return App版本号
 */
fun getAppVersionName(context: Context, packageName: String = context.packageName): String? {
    if (isSpace(packageName)) {
        return null
    } else {
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            return pi?.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return null
        }
    }
}

/**
 * 获取APP版本码
 *
 * @param context 上下文
 * @param packageName 包名
 * @return 版本码
 */
fun getAppVersionCode(context: Context, packageName: String = context.packageName): Int? {
    if (isSpace(packageName)) {
        return null
    } else {
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            return pi?.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return null
        }
    }
}

/**
 * 判断是否为系统APP
 *
 * @param context 上下文
 * @param packageName 包名
 * @return boolean
 */
fun isSystemApp(context: Context, packageName: String = context.packageName): Boolean {
    if (isSpace(packageName)) {
        return false
    } else {
        try {
            val pm = context.packageManager
            val ai = pm.getApplicationInfo(packageName, 0)
            return (ai.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}

/**
 * 判断是否为Debug版本
 *
 * @param context 上下文
 * @param packageName 包名
 * @return boolean
 */
fun isAppDebug(context: Context, packageName: String = context.packageName): Boolean {
    if (isSpace(packageName)) {
        return false
    } else {
        try {
            val pm = context.packageManager
            val ai = pm.getApplicationInfo(packageName, 0)
            return (ai.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return false
        }
    }
}

/**
 * 获取APP签名
 *
 * @param context 上下文
 * @param packageName 包名
 * @return App签名
 */
fun getAppSignature(context: Context, packageName: String = context.packageName): Array<Signature>? {
    if (isSpace(packageName)) {
        return null
    } else {
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            return pi?.signatures
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return null
        }
    }
}

/**
 * 获取应用签名的SHA1值
 *
 * @param context 上下文
 * @param packageName 包名
 * @return 应用签名的SHA1字符串
 */
fun getAppSignatureSHA1(context: Context, packageName: String = context.packageName): String? {
    val signatures = getAppSignature(context, packageName) ?: return null
    return encryptSHA1ToString(signatures[0].toByteArray())?.replace("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0")
}

/**
 * 判断APP是否处于前台
 *
 * @param context 上下文
 * @return boolean
 */
fun isAppForeground(context: Context): Boolean {
    val manager : ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    manager.runningAppProcesses.map {
        if (it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            it.processName == context.packageName
        }
    }
    return false
}

/**
 * 获取APP信息
 *
 * @param context 上下文
 * @param packageName 包名
 * @return App信息
 */
fun getAppInfo(context: Context, packageName: String = context.packageName): AppInfo? {
    try {
        val pm = context.packageManager
        val pi = pm.getPackageInfo(packageName, 0)
        return getBean(pm, pi)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

/**
 * 获取所有已安装的APP的信息
 *
 * @param context 上下文
 * @return 所有已安装的AppInfo列表
 */
fun getAppsInfo(context: Context): List<AppInfo> {
    val list = ArrayList<AppInfo>()
    val pm = context.packageManager
    pm.getInstalledPackages(0).map {
        list.add(getBean(pm, it))
    }
    return list
}

/**
 * 获取AppInfo的Bean
 *
 * @param pm 包管理器
 * @param pi 包的信息
 * @return AppInfo类
 */
private fun getBean(pm: PackageManager, pi: PackageInfo): AppInfo
        = AppInfo(pi.applicationInfo.loadLabel(pm).toString(),
        pi.applicationInfo.loadIcon(pm),
        pi.applicationInfo.packageName,
        pi.applicationInfo.sourceDir,
        pi.versionName,
        pi.versionCode,
        pi.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0)

