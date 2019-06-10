package pw.cub3d.commons.crashanalytics

import android.util.Base64
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pw.cub3d.commons.CUB3
import retrofit2.Retrofit
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.RuntimeException
import java.nio.charset.Charset
import pw.cub3d.commons.api.ICrashLogger
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.Exception
import java.util.*


object CrashTrak {
    fun triggerCrash() {
        throw RuntimeException("Manually triggered crash")
    }

    private fun getCallerCallerClassName(): String {
        val stElements = Thread.currentThread().stackTrace
        var callerClassName: String? = null
        for (i in 1 until stElements.size) {
            val ste = stElements[i]
            if (ste.className != this::class.java.name && ste.className.indexOf("java.lang.Thread") != 0) {
                if (callerClassName == null) {
                    callerClassName = ste.className
                } else if (callerClassName != ste.className) {
                    return ste.className
                }
            }
        }
        return "<class: unknown>"
    }

    private fun getStorageDir(): File {
        val f = File(CUB3.getConfig().dataDirectory, "CrashTrak")
        f.mkdir()
        return f
    }

    private fun onExceptionCaught(exception: Throwable, thread: Thread) {
        // Convert stack trace to string
        val sw = StringWriter()
        exception.printStackTrace(PrintWriter(sw))
        val sStackTrace = sw.toString()

        val report = CrashReport(thread.name, getCallerCallerClassName(), exception.message ?: "", sStackTrace)

        // Print the std stack trace
        report.printStackTrace()

        // Generate the data report to save
        val msgByteArray = report.generateCrashReportString()
            .toByteArray(Charset.forName("UTF-8"))

        val crashStore = File(getStorageDir(), UUID.randomUUID().toString())
        crashStore.writeBytes(msgByteArray)
    }

    /**
     * Note: will override existing crash handlers
     */
    fun register() {
        if(!CUB3.getConfig().variant.uploadCrashes) {
            Log.d("CrashTrak", "Refusing to enable crashtrak - variant disables crash uploads")
            return
        }

        // Start uploading crashes from last time if we can
        uploadPendingCrashes()

       // val originalHandler = Thread.currentThread().uncaughtExceptionHandler

        val exceptionHandler = Thread.UncaughtExceptionHandler { thread, exception ->
            try {
                onExceptionCaught(exception, thread)
            } catch (e: Exception) {
                println("Unable to upload crash")
            }

            System.exit(-1)
        }

        // Attach to the current thread as it has already spawned
        Thread.currentThread().uncaughtExceptionHandler = exceptionHandler

        // Attach to any new threads
        //val originalGlobalHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler)
    }

    private fun uploadPendingCrashes() {
        val crashStore = getStorageDir()

        crashStore.listFiles().forEach {
            val contents = it.readBytes()

            val data =
                String(Base64.encode(contents, Base64.URL_SAFE or Base64.NO_WRAP), Charset.forName("UTF-8"))

            val rf = Retrofit.Builder()
                .baseUrl("https://auth.cub3d.pw")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = rf.create(ICrashLogger::class.java)

            GlobalScope.launch {
                // Remove the file if it was uploaded successfully
                try {
                    if (service.submitCrashLog(data).execute().code() == 200) {
                        it.delete()
                    }
                } catch (e: Exception) {
                    println("Failed to upload crashlog")
                    e.printStackTrace()
                }
            }
        }
    }
}