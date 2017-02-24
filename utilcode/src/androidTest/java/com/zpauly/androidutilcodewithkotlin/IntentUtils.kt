package com.zpauly.androidutilcodewithkotlin

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.io.File

/**
 * Created by zpauly on 2017/1/8.
 */
/**
 * 获取安装App的意图
 *
 * @param filePath 文件路径
 * @return intent
 */
fun getInstallAppIntent(filePath: String): Intent?
        = getInstallAppIntent(getFileByPath(filePath))

/**
 * 获取安装App的意图
 *
 * @param file 文件
 * @return intent
 */
fun getInstallAppIntent(file: File?): Intent? {
    if (file == null) {
        return null
    }
    val intent = Intent(Intent.ACTION_VIEW)
    val type: String
    if (Build.VERSION.SDK_INT > 23) {
        type = "application/vnd.android.package-archive"
    } else {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(getFileExtension(file))
    }
    intent.setDataAndType(Uri.fromFile(file), type)
    return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

/**
 * 获取卸载App的意图
 *
 * @param packageName 包名
 * @return intent
 */
fun getUninstallAppIntent(packageName: String): Intent {
    val intent = Intent(Intent.ACTION_DELETE)
    intent.data = Uri.parse("package:$packageName")
    return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

/**
 * 获取打开App的意图
 *
 * @param context 上下文
 * @param packageName 包名
 * @return intent
 */
fun getLaunchAppIntent(context: Context, packageName: String): Intent
        = context.packageManager.getLaunchIntentForPackage(packageName)

/**
 * 获取App具体设置的意图
 *
 * @param packageName 包名
 * @return intent
 */
fun getAppDetailsSettingsIntent(packageName: String): Intent {
    val intent = Intent("android.settings.DETAILS_SETTINGS")
    intent.data = Uri.parse("package:$packageName")
    return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

/**
 * 获取分享文本的意图
 *
 * @param content 分享文本
 * @return intent
 */
fun getShareTextIntent(content: String): Intent {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, content)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    return intent
}

/**
 * 获取分享图片的意图
 *
 * @param content 文本
 * @param imagePath 图片文件路径
 * @return intent
 */
fun getShareImageIntent(content: String?, imagePath: String): Intent? =
        getShareImageIntent(content, getFileByPath(imagePath))

/**
 * 获取分享图片的意图
 *
 * @param content 文本
 * @param image 图片
 * @return intent
 */
fun getShareImageIntent(content: String?, image: File?): Intent?
        = if (isFileExists(image)) null else getShareImageIntent(content, Uri.fromFile(image))

/**
 * 获取分享图片的意图
 * @param content 文本
 * @param uri 图片uri
 * @return intent
 */
fun getShareImageIntent(content: String?, uri: Uri): Intent {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.putExtra(Intent.EXTRA_TEXT, content)
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    intent.type = "image/*"
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    return intent
}

/**
 * 获取其他应用组件的意图
 *
 * @param packageName 包名
 * @param className 全类名
 * @return intent
 */
fun getComponentIntent(packageName: String, className: String): Intent
        = getComponentIntent(packageName, className, null)

/**
 * 获取其他应用组件的意图
 *
 * @param packageName 包名
 * @param className 全类名
 * @return intent
 */
fun getComponentIntent(packageName: String, className: String, bundle: Bundle?): Intent {
    val intent = Intent()
    intent.putExtras(bundle)
    val componentName = ComponentName(packageName, className)
    intent.component = componentName
    return intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

/**
 * 获取关机意图
 *
 * 需要权限 <uses-permission android:name="android.permission.SHUTDOWN"/>
 * @return intent
 */
fun getShutdownIntent(): Intent {
    val intent = Intent(Intent.ACTION_SHUTDOWN)
    return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

/**
 * 获取拍照意图
 *
 * @param outUri 输出的uri
 * @return intent
 */
fun getCaptureIntent(outUri: Uri): Intent {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri)
    return intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
}
