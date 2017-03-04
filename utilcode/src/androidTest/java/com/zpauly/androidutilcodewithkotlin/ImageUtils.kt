package com.zpauly.androidutilcodewithkotlin

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import java.io.*

/**
 * Created by zpauly on 2017/2/25.
 */
val NO_MAX_VALUE = -1

/**
 * bitmap转字节数组
 *
 * @param bitmap bitmap资源对象
 * @param compressFormat 格式
 * @return 字节数组
 */
fun bitmap2Bytes(bitmap: Bitmap, compressFormat: Bitmap.CompressFormat): ByteArray {
    val byteArrOS = ByteArrayOutputStream()
    bitmap.compress(compressFormat, 100, byteArrOS)
    return byteArrOS.toByteArray()
}

/**
 * 字节数组转bitmap
 *
 * @param byteArr 字节数组
 * @return bitmap资源对象
 */
fun byte2Bitmap(byteArr: ByteArray): Bitmap =
        BitmapFactory.decodeByteArray(byteArr, 0, byteArr.size)

/**
 * bitmap转Drawable对象
 *
 * @param res resources对象
 * @param bitmap bitmap资源对象
 * @return Drawable对象
 */
fun bitmap2Drawable(res: Resources, bitmap: Bitmap): Drawable =
        BitmapDrawable(res, bitmap)

/**
 * Drawable对象转bitmap对象
 *
 * @param drawable Drawable对象
 * @return bitmap对象
 */
fun drawable2Bitmap(drawable: Drawable): Bitmap =
        (drawable as BitmapDrawable).bitmap

/**
 * Drawable对象转字节数组
 *
 * @param drawable Drawable对象
 * @return 字节数组
 */
fun drawable2Bytes(drawable: Drawable, compressFormat: Bitmap.CompressFormat): ByteArray =
        bitmap2Bytes(drawable2Bitmap(drawable), compressFormat)

/**
 * 字节数组转Drawable对象
 *
 * @param res resources对象
 * @param byteArr 字节数组
 * @return Drawable对象
 */
fun bytes2Drawable(res: Resources, byteArr: ByteArray): Drawable =
        bitmap2Drawable(res, byte2Bitmap(byteArr))

/**
 * view转bitmap对象
 *
 * @param view View对象
 * @return bitmap对象
 */
fun view2Bitmap(view: View): Bitmap {
    val ret = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(ret)
    val drawable = view.background
    drawable.draw(canvas)
    view.draw(canvas)
    return ret
}

/**
 * 计算采样率
 *
 * @param options 选项
 * @param maxHeight 最大高度
 * @param maxWidth 最大宽度
 * @return 采样率
 */
fun calculateInSampleSize(options: BitmapFactory.Options, maxWidth: Int, maxHeight: Int): Int {
    var width = options.outWidth
    var height = options.outHeight
    var inSampleSize = 1
    height = height shr 1
    width = width shr 1
    while (height >= maxHeight && width >= maxWidth) {
        inSampleSize = inSampleSize shr 1
    }
    return inSampleSize
}

/**
 * 获取bitmap
 *
 * @param file 文件
 * @return bitmap资源对象
 */
fun getBitmap(file: File?): Bitmap? {
    if (file == null) {
        return null
    }
    var ins: BufferedInputStream? = null
    try {
        if (file.exists()) {
            ins = BufferedInputStream(FileInputStream(file))
            return BitmapFactory.decodeStream(ins)
        } else {
            return null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    } finally {
        closeIO(ins)
    }
}

/**
 * 获取bitmap
 *
 * @param file 文件
 * @param maxHeight 最大高度
 * @param maxWidth 最大宽度
 * @return bitmap对象
 */
fun getBitmap(file: File?, maxHeight: Int, maxWidth: Int): Bitmap? {
    if (file == null) {
        return null
    }
    var ins: BufferedInputStream? = null
    try {
        if (!file.exists()) {
            return null
        }
        val options = BitmapFactory.Options()
        ins = BufferedInputStream(ins)
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(ins, null, options)
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeStream(ins, null, options)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    } finally {
        closeIO(ins)
    }
}

/**
 * 获取bitmap对象
 *
 * @param filePath 文件路径
 * @param maxHeight 最大高度
 * @param maxWidth 最大宽度
 * @return bitmap对象
 */
fun getBitmap(filePath: String, maxHeight: Int, maxWidth: Int): Bitmap? {
    if (isSpace(filePath)) {
        return null
    }
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(filePath)
    options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeFile(filePath)
}

/**
 * 获取bitmap对象
 *
 * @param inputStream 输入流
 * @param maxHeight 最大高度
 * @param maxWidth 最大宽度
 * @return bitmap对象
 */
fun getBitmap(inputStream: InputStream?, maxHeight: Int = NO_MAX_VALUE, maxWidth: Int = NO_MAX_VALUE): Bitmap? {
    if (inputStream == null) {
        return null
    }
    if (maxHeight == NO_MAX_VALUE || maxWidth == NO_MAX_VALUE) {
        return BitmapFactory.decodeStream(inputStream)
    }
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeStream(inputStream, null, options)
    options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeStream(inputStream, null, options)
}

/**
 * 获取bitmap
 *
 * @param byteArr 字节数组
 * @param maxHeight 最大高度
 * @param maxWidth 最大宽度
 */
fun getBitmap(byteArr: ByteArray?, offset: Int, maxHeight: Int = NO_MAX_VALUE, maxWidth: Int = NO_MAX_VALUE): Bitmap? {
    if (byteArr == null) {
        return null
    }
    if (maxHeight == null || maxWidth == null) {
        return BitmapFactory.decodeByteArray(byteArr, offset, byteArr.size)
    }
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeByteArray(byteArr, offset, byteArr.size, options)
    options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeByteArray(byteArr, offset, byteArr.size)
}

/**
 * 获取bitmap
 *
 * @param res Resources对象
 * @param id 资源id
 * @param maxHeight 最大高度
 * @param maxWidth 最大宽度
 * @return bitmap对象
 */
fun getBitmap(res: Resources?, id: Int, maxHeight: Int = NO_MAX_VALUE, maxWidth: Int = NO_MAX_VALUE): Bitmap? {
    if (res == null) {
        return null
    }
    if (maxHeight == null || maxWidth == null) {
        return BitmapFactory.decodeResource(res, id)
    }
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(res, id, options)
    options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeResource(res, id, options)
}

/**
 * 获取bitmap
 *
 * @param fd 文件描述符
 * @param maxHeight 最大高度
 * @param maxWidth 最大宽度
 * @return bitmap对象
 */
fun getBitmap(fd: FileDescriptor?, maxHeight: Int = NO_MAX_VALUE, maxWidth: Int = NO_MAX_VALUE): Bitmap? {
    if (fd == null) {
        return null
    }
    if (maxHeight == null || maxWidth == null) {
        return BitmapFactory.decodeFileDescriptor(fd)
    }
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFileDescriptor(fd, null, options)
    options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeFileDescriptor(fd, null, options)
}

/**
 * 缩放图片
 *
 * @param bitmap bitmap资源
 * @param newWidth 新的宽度
 * @param newHeight 新的高度
 * @param recycle 是否回收
 * @return bitmap对象
 */
fun scale(bitmap: Bitmap, newWidth: Int, newHeight: Int, recycle: Boolean = false): Bitmap {
    if (isEmptyBitmap(bitmap)) {
        return bitmap
    }
    val ret = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    if (recycle && !bitmap.isRecycled) {
        bitmap.recycle()
    }
    return ret
}

/**
 * 缩放图片
 *
 * @param bitmap bitmap资源
 * @param scaleWidth 宽度的缩放比例
 * @param scaleHeight 高度的缩放比例
 * @param  recycle 是否回收
 * @return bitmap对象
 */
fun scale(bitmap: Bitmap, scaleWidth: Float, scaleHeight: Float, recycle: Boolean = false): Bitmap {
    if (isEmptyBitmap(bitmap)) {
        return bitmap
    }
    val matrix = Matrix()
    matrix.setScale(scaleWidth, scaleHeight)
    val ret = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    if (recycle && !bitmap.isRecycled) {
        bitmap.recycle()
    }
    return ret
}

/**
 * 裁剪图片
 *
 * @param bitmap bitmap资源
 * @param x 开始的x坐标
 * @param y 开始的y坐标
 * @param recycle 是否回收
 * @return bitmap对象
 */
fun clip(bitmap: Bitmap, x: Int, y: Int, width: Int, height: Int, recycle: Boolean = false): Bitmap {
    if (isEmptyBitmap(bitmap)) {
        return bitmap
    }
    val ret = Bitmap.createBitmap(bitmap, x, y, width, height)
    if (recycle && !bitmap.isRecycled) {
        bitmap.recycle()
    }
    return ret
}

/**
 * 判断bitmap是否为空
 *
 * @param bitmap bitmap对象
 * @return boolean
 */
fun isEmptyBitmap(bitmap: Bitmap): Boolean =
        bitmap.width != 0 && bitmap.height != 0
