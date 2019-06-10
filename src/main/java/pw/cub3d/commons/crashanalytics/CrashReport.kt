package pw.cub3d.commons.crashanalytics

import pw.cub3d.commons.CUB3
import pw.cub3d.commons.logging.Log

data class CrashReport(val threadName: String,
                       val callingClassName: String,
                       val exceptionMessage: String,
                       val stackTrace: String) {

    fun generateCrashReportString(): String {
        var baseReportString = """
Crash detected @ ${System.currentTimeMillis()}ms in $threadName
Crashing class: $callingClassName
Exception: $exceptionMessage
Stack trace:
===
$stackTrace
---
Logs leading up to this crash:
===
""".trimIndent()

        Log.getLogHistory().map {
            baseReportString += "$it\n"
        }

        return baseReportString
    }

    fun printStackTrace() {
        println("Application crashed on thread '$threadName' in class $callingClassName")
        println("Exception was $exceptionMessage")
        println("-------------")
        System.err.println(stackTrace)
    }
}