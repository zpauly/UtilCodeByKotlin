package com.zpauly.androidutilcodewithkotlin

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by zpauly on 2017/1/14.
 */
/**
 * 是否在root权限下执行命令
 *
 * @param command 命令
 * @param isRoot 是否需要root权限执行
 * @param isNeedMsg 是否需要结果消息
 * @return CommandResult
 */
fun execCmd(command: String, isRoot: Boolean = true, isNeedMsg: Boolean = true): CommandResult
        = execCmd(command, isRoot, isNeedMsg)

/**
 * 是否在root下执行命令
 *
 * @param commands 命令链表
 * @param isRoot 是否需要root权限执行
 * @param isNeedMsg 是否需要结果消息
 * @return CommandResult
 */
fun execCmd(commands: List<String>, isRoot: Boolean = true, isNeedMsg: Boolean = true): CommandResult
        = execCmd(commands.toTypedArray(), isRoot, isNeedMsg)

/**
 * 是否在root下执行命令
 *
 * @param commands 命令数组
 * @param isRoot 是否需要root权限执行
 * @param isNeedMsg 是否需要结果消息
 * @return CommandResult
 */
fun execCmd(commands: Array<String>, isRoot: Boolean = true, isNeedMsg: Boolean = true): CommandResult {
    var result = -1
    if (commands.isEmpty()) {
        return CommandResult(result, null, null)
    }
    var process: Process? = null
    var successResult: BufferedReader? = null
    var errorResult: BufferedReader? = null
    var successMsg : StringBuilder? = null
    var errorMsg : StringBuilder? = null
    var outputStream: DataOutputStream? = null
    try {
        process = Runtime.getRuntime().exec(if (isRoot) "su" else "sh")
        outputStream = DataOutputStream(process.outputStream)
        commands.map {
            outputStream?.apply {
                write(it.toByteArray())
                writeBytes("\n")
                flush()
            }
        }
        outputStream.writeBytes("exit\n")
        outputStream.flush()
        result = process.waitFor()
        if (isNeedMsg) {
            successMsg = StringBuilder()
            errorMsg = StringBuilder()
            successResult = BufferedReader(InputStreamReader(process.inputStream, "UTF-8"))
            errorResult = BufferedReader(InputStreamReader(process.errorStream, "UTF-8"))
            var s = successResult.readLine()
            while (s != null) {
                successMsg.append(s)
                s = successResult.readLine()
            }
            s = errorResult.readLine()
            while (s != null) {
                errorMsg.append(s)
                s = errorResult.readLine()
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        closeIO(outputStream, successResult, errorResult)
        process?.destroy()
    }
    return CommandResult(result, successMsg?.toString(), errorMsg?.toString())
}
