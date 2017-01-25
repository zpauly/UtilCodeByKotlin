package com.zpauly.androidutilcodewithkotlin

import java.io.*
import java.security.DigestInputStream
import java.security.MessageDigest
import java.util.*

/**
 * Created by zpauly on 2017/1/8.
 */
/**
 * 根据文件路径获文件
 *
 * @param filePath 文件路径
 * @return 文件
 */
fun getFileByPath(filePath: String?): File?
        = if (isSpace(filePath)) null else File(filePath)

/**
 * 重命名文件
 *
 * @param filePath 文件路径
 * @return boolean
 */
fun rename(filePath: String?, newName: String?): Boolean
        = rename(getFileByPath(filePath), newName)

/**
 * 重命名文件
 *
 * @param file 文件
 * @param newName 新文件名
 * @return boolean
 */
fun rename(file: File?, newName: String?): Boolean {
    if (isFileExists(file)) {
        return false
    }
    if (isSpace(newName)) {
        return false
    }
    if (file!!.name == newName) {
        return false
    }
    val newFile = File(file.parent + File.separator + newName)
    return !newFile.exists() && file.renameTo(newFile)
}

/**
 * 判断文件是否为目录
 *
 * @param filePath 文件路径
 * @return boolean
 */
fun isDir(filePath: String?): Boolean
        = isDir(getFileByPath(filePath))

/**
 * 判断文件是否为目录
 *
 * @param file 文件
 * @return boolean
 */
fun isDir(file: File?): Boolean
        = isFileExists(file) && file?.isDirectory!!

/**
 * 判断是否为文件
 *
 * @param filePath 文件路径
 * @return boolean
 */
fun isFile(filePath: String?): Boolean
        = isFile(getFileByPath(filePath))

/**
 * 判断是否为文件
 *
 * @param file 文件
 * @return boolean
 */
fun isFile(file: File?): Boolean
        = isFileExists(file) && file?.isFile!!

/**
 * 判断目录是否存在，不存在则判断是否创建成功
 *
 * @param filePath 文件路径
 * @return boolean
 */
fun createOrExistsDir(filePath: String?): Boolean
        = createOrExistsDir(filePath)

/**
 * 判断目录是否存在，不存在则判断是否创建成功
 *
 * @param file 文件
 * @return boolean
 */
fun createOrExistsDir(file: File?): Boolean
        = (file != null) && if (file.exists()) file.isDirectory else file.mkdirs()

/**
 * 判断文件是否存在，不存在则判断是否创建成功
 *
 * @param filePath 文件路径
 * @return boolean
 */
fun createOrExistsFile(filePath: String?): Boolean
        = createOrExistsFile(getFileByPath(filePath))

/**
 * 判断文件是否存在，不存在则判断是否创建成功
 *
 * @param file 文件
 * @return boolean
 */
fun createOrExistsFile(file: File?): Boolean {
    if (file == null || !createOrExistsDir(file.parentFile)) {
        return false
    }
    if (file.exists()) {
        return file.isFile
    }
    try {
        return file.createNewFile()
    } catch (e: IOException) {
        e.printStackTrace()
        return false
    }
}

/**
 * 判断文件是否存在，存在则创建之前删除
 *
 * @param filePath 文件路径
 * @return boolean
 */
fun createFileByDeleteOldFile(filePath: String?): Boolean
        = createFileByDeleteOldFile(getFileByPath(filePath))

/**
 * 判断文件是否存在，存在则创建之前删除
 *
 * @param file 文件
 * @return boolean
 */
fun createFileByDeleteOldFile(file: File?): Boolean {
    if (file == null || !createOrExistsDir(file.parentFile)) {
        return false
    }
    if (file.exists() && file.isFile && !file.delete()) {
        return false
    }
    try {
        return file.createNewFile()
    } catch (e: IOException) {
        e.printStackTrace()
        return false
    }
}

/**
 * 复制或移动目录
 *
 * @param srcDirPath 源目录路径
 * @param destDirPath 目标目录路径
 * @param isMove 是否移动
 * @return boolean
 */
fun copyOrMoveDir(srcDirPath: String?, destDirPath: String?, isMove: Boolean): Boolean
        = copyOrMoveDir(getFileByPath(srcDirPath), getFileByPath(destDirPath), isMove)

/**
 * 复制或移动目录
 *
 * @param srcDir 源目录
 * @param destDir 目的目录
 * @param isMove 是否移动
 * @return boolean
 */
fun copyOrMoveDir(srcDir: File?, destDir: File?, isMove: Boolean): Boolean {
    if (srcDir == null || destDir == null) {
        return false
    }

    if (!srcDir.exists() || !srcDir.isDirectory) {
        return false
    }

    val srcPath = srcDir.path + File.separator
    val destPath = destDir.path + File.separator

    if (destPath.contains(srcPath)) {
        return false
    }

    if (!createOrExistsDir(destDir)) {
        return false
    }

    val files = srcDir.listFiles()
    files.map {
        val oneDestFile = File(destPath + it.name)
        if (it.isFile) {
            if (copyOrMoveFile(it, oneDestFile, isMove)) {
                return false
            }
        } else if (it.isDirectory) {
            if (!copyOrMoveDir(it, oneDestFile, isMove)) {
                return false
            }
        }
    }

    return !isMove || deleteDir(srcDir)
}

/**
 * 复制或移动文件
 *
 * @param srcFilePath 源文件路径
 * @param destFilePath 目标文件路径
 * @param isMove 是否移动
 * @return boolean
 */
fun copyOrMoveFile(srcFilePath: String?, destFilePath: String?, isMove: Boolean): Boolean
        = copyOrMoveFile(getFileByPath(srcFilePath), getFileByPath(destFilePath), isMove)

/**
 * 复制或移动文件
 *
 * @param srcFile 源文件
 * @param destFile 目标文件
 * @param isMove 是否移动
 * @return boolean
 */
fun copyOrMoveFile(srcFile: File?, destFile: File?, isMove: Boolean): Boolean {
    if (srcFile == null || destFile == null) {
        return false
    }
    if (!srcFile.exists() || !srcFile.isFile) {
        return false
    }
    if (destFile.exists() && destFile.isFile) {
        return false
    }
    if (!createOrExistsDir(destFile.parentFile)) {
        return false
    }
    return writeFileFromIS(destFile, FileInputStream(srcFile), false)
            && !(isMove && !deleteFile(srcFile))
}

/**
 *
 */
fun getFileExtension(file: File?): String? {
    if (file == null) {
        return null
    } else {
        return getFileExtension(file.path)
    }
}

fun getFileExtension(filePath: String): String {
    val lastPoi = filePath.lastIndexOf('.')
    val lastSep = filePath.lastIndexOf(File.separator)
    if (lastPoi == -1 || lastSep >= lastPoi) {
        return ""
    }
    return filePath.substring(lastPoi + 1)
}

/**
 * 删除目录及其下所有文件
 *
 * @param dirPath 目录路径
 * @return boolean
 */
fun deleteDir(dirPath: String?): Boolean
        = deleteDir(getFileByPath(dirPath))

/**
 * 删除目录及其下所有文件
 *
 * @param dir 目录
 * @return boolean
 */
fun deleteDir(dir: File?): Boolean {
    if (dir == null) {
        return false
    }
    if (!dir.exists()) {
        return true
    }
    if (!dir.isDirectory) {
        return false
    }
    dir.listFiles().map {
        if (it.isFile) {
            if (!deleteFile(it)) {
                return false
            }
        } else if (it.isDirectory) {
            if (!deleteDir(it)) {
                return false
            }
        }
    }
    return true
}

/**
 * 删除文件
 *
 * @param filePath 文件
 * @return
 */
fun deleteFile(filePath: String): Boolean
        = deleteFile(getFileByPath(filePath))

/**
 * 删除文件
 *
 * @param file 文件
 * @return boolean
 */
fun deleteFile(file: File?): Boolean
        = (file != null) && (!file.exists() || file.isFile && file.delete())

/**
 * 删除目录下的所有文件
 *
 * @param dirPath 目录路径
 * @return boolean
 */
fun deleteFilesInDir(dirPath: String?):Boolean
        = deleteFilesInDir(getFileByPath(dirPath))

/**
 * 删除目录下的所有文件
 *
 * @param dir 目录
 * @return boolean
 */
fun deleteFilesInDir(dir: File?): Boolean {
    if (dir == null || !dir.exists()) {
        return false
    }
    if (!dir.isDirectory) {
        return false
    }
    dir.listFiles().map {
        if (it.isFile) {
            if (!deleteFile(it)) {
                return false
            }
        } else if (it.isDirectory) {
            if (!deleteDir(it)) {
                return false
            }
        }
    }
    return true
}

/**
 * 获取目录下所有的文件包括子目录
 *
 * @param dirPath 目录
 * @return 文件列表
 */
fun listFilesInDir(dirPath: String?): List<File>?
        = listFilesInDir(getFileByPath(dirPath))

/**
 * 获取目录下所有的文件包括子目录
 *
 * @param dir 目录
 * @return 文件列表
 */
fun listFilesInDir(dir: File?): List<File>? {
    if (dir == null) {
        return null
    }
    if (!dir.isDirectory) {
        return null
    }
    val files = ArrayList<File>()
    dir.listFiles().map {
        files.add(it)
        if (it.isDirectory) {
            listFilesInDir(it)?.let { it1 -> files.addAll(it1) }
        }
    }
    return files
}

/**
 * 获取目录下所有后缀名为suffix的文件
 *
 * @param dirPath 目录路径
 * @param suffix 后缀名
 * @param needDir 是否需要包括文件夹
 * @return 文件列表
 */
fun listFilesInDirWithFilter(dirPath: String?, suffix: String?, needDir: Boolean): List<File>?
        = listFilesInDirWithFilter(getFileByPath(dirPath), suffix, needDir)

/**
 * 获取目录下所有后缀名为suffix的文件
 *
 * @param dir 文件目录
 * @param suffix 后缀名
 * @param needDir 是否需要包括文件夹
 * @return 文件列表
 */
fun listFilesInDirWithFilter(dir: File?, suffix: String?, needDir: Boolean): List<File>? {
    if (dir == null || suffix == null) {
        return null
    }
    val files = ArrayList<File>()
    if (!dir.isDirectory) {
        return null
    }
    dir.listFiles { files ->
        dir.name.toUpperCase().endsWith(suffix.toUpperCase()) || dir.isDirectory
    }.map {
        if (dir.isDirectory) {
            if (needDir) {
                listFilesInDirWithFilter(it, suffix, needDir)?.let { it1 -> files.addAll(it1) }
            }
        } else if (dir.isFile) {
            files.add(it)
        }
    }
    return files
}

/**
 * 获取目录下所有符合filter的文件
 *
 * @param dirPath 目录路径
 * @param filter 过滤器
 * @param needDir 是否允许文件夹
 * @return 文件列表
 */
fun listFilesInDirWithFilter(dirPath: String?, filter: FileFilter?, needDir: Boolean): List<File>?
        = listFilesInDirWithFilter(getFileByPath(dirPath), filter, needDir)

/**
 * 获取目录下所有符合filter的文件
 *
 * @param dir 文件目录
 * @param filter 过滤器
 * @param needDir 是否许文件夹
 * @return 文件列表
 */
fun listFilesInDirWithFilter(dir: File?, filter: FileFilter?, needDir: Boolean): List<File>? {
    if (dir == null || filter == null) {
        return null
    }

    val files = ArrayList<File>()
    if (!dir.isDirectory) {
        return null
    }
    dir.listFiles(filter).map {
        if (dir.isDirectory) {
            if (needDir) {
                listFilesInDirWithFilter(it, filter, needDir)?.let { it1 -> files.addAll(it1) }
            }
        } else if (dir.isFile) {
            files.add(it)
        }
    }
    return files
}

/**
 * 将输入流写入文件
 *
 * @param filePath 文件路径
 * @param inputStream 输入流
 * @param append 是否追加在文件末尾
 * @return boolean
 */
fun writeFileFromIS(filePath: String?, inputStream: InputStream?, append: Boolean): Boolean
        = writeFileFromIS(getFileByPath(filePath), inputStream, append)

/**
 * 将输入流写入文件
 *
 * @param file 文件
 * @param inputStream 输入流
 * @param append 是否追加在文件末尾
 * @return boolean
 */
fun writeFileFromIS(file: File?, inputStream: InputStream?, append: Boolean): Boolean {
    if (file == null || inputStream == null) {
        return false
    }
    if (!createOrExistsFile(file)) {
        return false
    }
    var outputStream: OutputStream? = null
    try {
        outputStream = BufferedOutputStream(FileOutputStream(file, append))
        val data = ByteArray(1024)
        var len: Int = inputStream.read(data, 0, 1024)
        while (len != -1) {
            outputStream.write(data, 0, len)
            len = inputStream.read(data, 0, 1024)
        }
        return true
    } catch (e : IOException) {
        e.printStackTrace()
        return false
    } finally {
        closeIO(outputStream)
    }
}

/**
 * 将字符串写入文件
 *
 * @param filePath 文件路径
 * @param content 写入内容
 * @param append 是否追加在文件末尾
 * @return boolean
 */
fun writeFileFromString(filePath: String?, content: String?, append: Boolean): Boolean
        = writeFileFromString(getFileByPath(filePath), content, append)

/**
 * 将字符串写入文件
 *
 * @param file 文件
 * @param content 写入内容
 * @param append 是否追加在文件末尾
 * @return boolean
 */
fun writeFileFromString(file: File?, content: String?, append: Boolean): Boolean {
    if (file == null || content == null) {
        return false
    }
    if (!createOrExistsFile(file)) {
        return false
    }
    var bw : BufferedWriter? = null
    try {
        bw = BufferedWriter(FileWriter(file, append))
        bw.write(content)
        return true
    } catch (e: IOException) {
        e.printStackTrace()
        return false
    } finally {
        closeIO(bw)
    }
}

/**
 * 指定编码按行读取到链表中
 *
 * @param filePath 文件路径
 * @param charsetName 编码格式
 * @return 文件链表
 */
fun readFile2List(filePath: String?, charsetName: String): List<String>?
        = readFile2List(getFileByPath(filePath), charsetName)

/**
 * 指定编码按行读取到链表中
 *
 * @param file 文件
 * @param charsetName 编码格式
 * @return 文件链表
 */
fun readFile2List(file: File?, charsetName: String): List<String>?
        = readFile2List(file, 0, 0x7FFFFFFF, charsetName)

/**
 * 指定编码按行读取到链表中
 *
 * @param filePath 文件路径
 * @param start 需要读取的开始行数
 * @param end 需要读取的结束行数
 * @param charsetName 编码格式
 * @return 包含从start到end行到list
 */
fun readFile2List(filePath: String?, start: Int, end: Int, charsetName: String): List<String>?
        = readFile2List(getFileByPath(filePath), start, end, charsetName)

/**
 * 指定编码按行读取文件到链表中
 *
 * @param file 文件
 * @param start 需要读取的开始行数
 * @param end 需要读取的结束行数
 * @param charsetName 编码格式
 * @return 包含从start到end行的list
 */
fun readFile2List(file: File?, start: Int, end: Int, charsetName: String): List<String>? {
    if (file == null || start > end) {
        return null
    }
    var br : BufferedReader? = null
    try {
        var line : String
        var curLine = 1
        val list = ArrayList<String>()
        if (isSpace(charsetName)) {
            br = BufferedReader(FileReader(file))
        } else {
            br = BufferedReader(InputStreamReader(FileInputStream(file), charsetName))
        }
        line = br.readLine()
        while (line != null) {
            if (curLine > end) {
                break
            }
            if (start <= curLine && curLine >= end) {
                list.add(line)
            }
            ++ curLine
            line = br.readLine()
        }
        return list
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    } finally {
        closeIO(br)
    }
}

/**
 * 指定编码按行读取到字符串中
 *
 * @param filePath 文件路径
 * @param charsetName 编码格式
 * @return 字符串
 */
fun readFile2String(filePath: String?, charsetName: String): String?
        = readFile2String(getFileByPath(filePath), charsetName)

/**
 * 指定编码按行读取到字符串中
 *
 * @param file 文件
 * @param charsetName 编码格式
 * @return 字符串
 */
fun readFile2String(file: File?, charsetName: String): String? {
    if (file == null) {
        return null
    }
    var br : BufferedReader? = null
    try {
        val sb = StringBuilder()
        if (isSpace(charsetName)) {
            br = BufferedReader(InputStreamReader(FileInputStream(file)))
        } else {
            br = BufferedReader(InputStreamReader(FileInputStream(file), charsetName))
        }
        var line : String? = br.readLine()
        while (line != null) {
            sb.append(line).append("\n")
            line = br.readLine()
        }

        return sb.delete(sb.length - 1, sb.length).toString()
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    } finally {
        closeIO(br)
    }
}

/**
 * 读取文件到字节数组中
 *
 * @param filePath 文件路径
 * @return 字节数组
 */
fun readFile2Bytes(filePath: String?): ByteArray?
        = readFile2Bytes(getFileByPath(filePath))

/**
 * 读取文件到字节数组中
 *
 * @param file 文件
 * @return 字节数组
 */
fun readFile2Bytes(file: File?): ByteArray? {
    if (file == null) {
        return null
    }
    try {
        return inputStream2Bytes(FileInputStream(file))
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
}

/**
 * 获取文件最后修改到到毫秒时间戳
 *
 * @param filePath 文件路径
 * @return 时间戳
 */
fun getFileLastModified(filePath: String?): Long
        = getFileLastModified(getFileByPath(filePath))

/**
 * 获取文件最后修改到毫秒时间戳
 *
 * @param file 文件
 * @return 时间戳
 */
fun getFileLastModified(file: File?): Long
        = file?.lastModified() ?: -1

/**
 * 简单获取文件编码格式
 *
 * @param filePath 文件路径
 * @return 文件编码
 */
fun getFileCharsetSimple(filePath: String?): String
        = getFileCharsetSimple(getFileByPath(filePath))

/**
 * 简单获取文件编码格式
 *
 * @param file 文件
 * @return 文件编码
 */
fun getFileCharsetSimple(file: File?): String {
    var p = 0
    var inputStream : InputStream? = null
    try {
        inputStream = BufferedInputStream(FileInputStream(file))
        p = (inputStream.read() shl 8) + inputStream.read()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        closeIO(inputStream)
    }
    return when (p) {
        0xefbb -> "UTF-8"
        0xfffe -> "Unicode"
        0xfeff -> "UTF-16BE"
        else -> "GBK"
    }
}

/**
 * 获取目录大小
 *
 * @param dirPath 目录路径
 * @return 目录大小
 */
fun getDirSize(dirPath: String?): String
        = getDirSize(getFileByPath(dirPath))

/**
 * 获取目录大小
 *
 * @param dir 目录
 * @return 目录大小
 */
fun getDirSize(dir: File?): String
        = if (getDirLength(dir) != -1L) "" else byte2FitMemorySize(getDirLength(dir))

/**
 * 获取文件大小
 *
 * @param filePath 文件路径
 * @return 文件大小
 */
fun getFileSize(filePath: String?): String
        = getFileSize(getFileByPath(filePath))

/**
 * 获取文件大小
 *
 * @param file 文件
 * @return 文件大小
 */
fun getFileSize(file: File?): String
        = if (getFileLength(file) != -1L) "" else byte2FitMemorySize(getFileLength(file))

/**
 * 获取目录长度
 *
 * @param dirPath 目录路径
 * @return 目录大小
 */
fun getDirLength(dirPath: String?): Long
        = getDirLength(getFileByPath(dirPath))

/**
 * 获取目录长度
 *
 * @param dir 目录
 * @return 目录大小
 */
fun getDirLength(dir: File?): Long {
    if (dir == null || !dir.isDirectory) {
        return -1
    }
    var len : Long = 0
    dir.listFiles().map {
        if (it.isDirectory) {
            len += getDirLength(it)
        } else {
            len += getFileLength(it)
        }
    }
    return len
}

/**
 * 获取文件的长度
 *
 * @param filePath 文件路径
 * @return 文件长度
 */
fun getFileLength(filePath: String?): Long
        = getFileLength(getFileByPath(filePath))

/**
 * 获取文件的长度
 *
 * @param file 文件
 * @return 文件长度
 */
fun getFileLength(file: File?): Long {
    if (file == null) {
        return -1
    } else {
        if (file.isFile) {
            return file.length()
        } else {
            return -1
        }
    }
}

/**
 * 获取文件的MD5校验码
 *
 * @param filePath 文件路径
 * @return 文件的MD5校验码
 */
fun getFileMD5ToString(filePath: String?): String?
        = getFileMD5ToString(getFileByPath(filePath))

/**
 * 获取文件的MD5校验码
 *
 * @param file 文件
 * @return 文件的MD5校验码
 */
fun getFileMD5ToString(file: File?): String?
        = byte2HexString(getFileMD5(file))

/**
 * 获取文件的MD5校验码
 *
 * @param filePath 文件路径
 * @return 文件的MD5校验码
 */
fun getFileMD5(filePath: String?): ByteArray?
        = getFileMD5(getFileByPath(filePath))

/**
 * 获取文件的MD5校验码
 *
 * @param file 文件
 * @return 文件的MD5校验码
 */
fun getFileMD5(file: File?): ByteArray? {
    if (file == null) {
        return null
    }
    var dis : DigestInputStream? = null
    try {
        var md5 = MessageDigest.getInstance("MD5")
        dis = DigestInputStream(FileInputStream(file), md5)
        val buffer = ByteArray(1024 * 256)
        while (dis.read(buffer) > 0) {
            md5 = dis.messageDigest
        }
        return md5.digest()
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    } finally {
        closeIO(dis)
    }
}

/**
 * 判断文件是否存在
 *
 * @param filePath 文件路径
 * @return boolean
 */
fun isFileExists(filePath: String?): Boolean
        = isFileExists(getFileByPath(filePath))

/**
 * 判断文件是否存在
 *
 * @param file 文件
 * @return boolean
 */
fun isFileExists(file: File?): Boolean
        = file?.exists() ?: false