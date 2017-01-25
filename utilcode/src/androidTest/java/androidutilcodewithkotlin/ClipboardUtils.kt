package com.zpauly.androidutilcodewithkotlin

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Created by zpauly on 2017/1/25.
 */
/**
 * 复制文本到剪贴板
 *
 * @param context 上下文
 * @param text 文本内容
 */
fun copyTextToClipboard(context: Context, text: CharSequence) {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.primaryClip = ClipData.newPlainText("text", text)
}

/**
 * 获取剪贴本的文本
 *
 * @param context 上下文
 * @return 文本内容
 */
fun getTextFromClipboard(context: Context): CharSequence? {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = clipboardManager.primaryClip
    if (clip.itemCount > 0) {
        return clip.getItemAt(0).coerceToText(context)
    } else {
        return null
    }
}

/**
 * 复制uri到剪贴板
 *
 * @param context 上下文
 * @param uri uri
 */
fun copyUriToClipboard(context: Context, uri: Uri) {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.primaryClip = ClipData.newUri(context.contentResolver, "uri", uri)
}

/**
 * 获取剪贴板的uri
 *
 * @param context 上下文
 * @return 剪贴板的uri
 */
fun getUriFromClipboard(context: Context): Uri? {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = clipboardManager.primaryClip
    if (clip.itemCount > 0) {
        return clip.getItemAt(0).uri
    } else {
        return null
    }
}

/**
 * 复制意图到剪贴板
 *
 * @param context 上下文
 * @param intent 意图
 */
fun copyIntentToClipboard(context: Context, intent: Intent) {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.primaryClip = ClipData.newIntent("intent", intent)
}

/**
 * 获取剪贴板的意图
 *
 * @param context 上下文
 * @return 剪贴板的意图
 */
fun getIntentFromClipboard(context: Context): Intent? {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = clipboardManager.primaryClip
    if (clip.itemCount > 0) {
        return clip.getItemAt(0).intent
    } else {
        return null
    }
}
