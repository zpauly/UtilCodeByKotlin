package com.zpauly.androidutilcodewithkotlin

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import java.io.File
import java.net.NetworkInterface
import java.util.*

/**
 * Created by zpauly on 2017/1/25.
 */
/**
 * 判断设备是否root
 *
 * @return boolean
 */
fun isDeviceRooted(): Boolean {
    val su = "su"
    val locations = arrayOf("/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/")
    locations.map {
        if (File(it + su).exists()) {
            return true
        }
    }
    return false
}

/**
 * 获取设备系统版本号
 *
 * @return 系统版本号
 */
fun getSDKVersion(): Int =
        Build.VERSION.SDK_INT

/**
 * 获取设备的AndroidID
 *
 * @return AndroidID
 */
fun getAndroidID(context: Context): String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

/**
 * 获取设备的MAC地址
 *
 * @param context 上下文
 * @return MAC地址
 */
fun getMacAddressByWifiInfo(context: Context): String {
    try {
        val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifi.connectionInfo.macAddress
    } catch (e: Exception) {
        e.printStackTrace()
        return "02:00:00:00:00:00"
    }
}

/**
 * 获取设备MAC地址
 *
 * @param context 上下文
 * @return MAC地址
 */
fun getMacAddressByNetworkInterface(context: Context): String {
    try {
        val nis = Collections.list(NetworkInterface.getNetworkInterfaces())
        nis.map {
            val res = StringBuilder()
            if (it.name.equals("wlan0", true)) {
                it.hardwareAddress.map {
                    res.append(String.format("%02x:", it))
                }
            }
            return res.deleteCharAt(res.length - 1).toString()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "02:00:00:00:00:00"
}

/**
 * 获取设备厂商
 *
 * @return 设备厂商
 */
fun getManufacturer() =
        Build.MANUFACTURER;

/**
 * 获取设备型号
 *
 * @return 设备型号
 */
fun getModel(): String {
    val model = Build.MODEL;
    model?.let {
        return model.trim().replace("\\s", "")
    }
    return "";
}

/**
 * 关机
 * 需要root权限或者系统权限 <android:sharedUserId="android.uid.system"/>
 *
 * @param context 上下文
 */
fun shutdown(context: Context) {
    execCmd("reboot -p", true)
    val intent = Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN")
    intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
    context.startActivity(intent);
}

/**
 * 重启
 * 需要root权限或者系统权限 <android:sharedUserId="android.uid.system"/>
 *
 * @param context 上下文
 */
fun reboot(context: Context) {
    execCmd("reboot", true)
    val intent = Intent(Intent.ACTION_REBOOT)
    intent.putExtra("nowait", 1)
    intent.putExtra("interval", 1)
    intent.putExtra("window", 0)
    context.sendBroadcast(intent)
}

/**
 * 重启
 * 需要root权限或者系统权限 <android:sharedUserId="android.uid.system"/>
 *
 * @param reason 传递给内核来请求特殊的引导模式，如"recovery"
 * @param context 上下文
 */
fun reboot(reason: String, context: Context) {
    val mPowerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    try {
        mPowerManager.reboot(reason)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 重启至recovery
 * 需要root权限
 */
fun reboot2Recovery() =
        execCmd("reboot recovery", true)

/**
 * 重启至bootloader
 * 需要root权限
 */
fun reboot2Bootloader() =
        execCmd("reboot bootloader", true)