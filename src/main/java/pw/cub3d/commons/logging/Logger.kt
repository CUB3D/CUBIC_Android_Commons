package pw.cub3d.commons.logging

import android.util.Log
import pw.cub3d.commons.CUB3
import pw.cub3d.commons.crashanalytics.CrashTrak
import java.io.PrintWriter
import java.io.StringWriter

open class Logger(val tag: String) {
    val logHistory = ArrayList<String>()
    private val logCallbacks = mutableListOf<(Int, String)->Unit>()

    private fun logMsg(priority: Int, msg: String) {
        if(CUB3.initialised() && CUB3.getConfig().variant.shouldLog) {
            Log.println(priority, tag, msg)
            logHistory.add("[${System.currentTimeMillis()}] [$tag] [$priority] $msg")
        }

        logCallbacks.forEach {
            it.invoke(priority, msg)
        }
    }

    fun d(msg: Any?) = d(msg.toString())
    fun d(msg: String) = logMsg(Log.DEBUG, msg)

    fun e(s: String) {
        logMsg(Log.ERROR, s)
    }

    fun throwable(e: Throwable) {
        val sw = StringWriter()
        e.printStackTrace(PrintWriter(sw))
        logMsg(Log.ERROR, sw.toString())
        CrashTrak.logCrash(e)
    }

    fun logCallback(callback: (Int, String)->Unit) {
        logCallbacks.add(callback)
    }
}