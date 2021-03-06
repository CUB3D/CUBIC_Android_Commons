package pw.cub3d.commons

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import pw.cub3d.commons.configuration.CUB3Config
import pw.cub3d.commons.configuration.CUB3DConfiguration
import pw.cub3d.commons.configuration.CUB3Variant
import pw.cub3d.commons.crashanalytics.CrashTrak
import pw.cub3d.commons.identification.DeviceIdentification
import pw.cub3d.commons.utils.Accounts
import pw.cub3d.commons.utils.SharedPrefs

class CUB3(ctx: Context) {
    val config = CUB3Config(ctx)
    val configuration = CUB3DConfiguration(ctx)

    //TODO: permission check
    private val connectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    companion object {
        private var instance : CUB3? = null

        fun initialise(ctx: Context, enableCrashTrak: Boolean = true) {
            SharedPrefs.initialise(ctx)
            DeviceIdentification.initialise()

            instance = CUB3(ctx)
            instance!!.configuration.init()

            if(enableCrashTrak)
                CrashTrak.register()

            Accounts.initialise(ctx)

            Log.d("CUB3", "Variant: ${getInstance().config.variant}")
        }

        private fun getInstance() = instance!!

        fun initialised() = instance != null

        fun getConfig(): CUB3Config = getInstance().config

        fun getConfiguration() = getInstance().configuration

        fun getVariant(): CUB3Variant = getConfig().variant


        // Helpers - version
        inline fun ifMarshmallow(callback: ()->Unit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                callback()
            }
        }

        inline fun ifBelowMarshmallow(callback: ()->Unit) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                callback()
            }
        }

        inline fun ifOreo(callback: ()->Unit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                callback()
            }
        }

        inline fun ifAndroid10(callback: ()->Unit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                callback()
            }
        }


        // Helpers - config
        inline fun ifEmulator(callback: ()->Unit) {
            if(getVariant() == CUB3Variant.EMULATOR) {
                callback()
            }
        }

        inline fun ifNotEmulator(callback: ()->Unit) {
            if(getVariant() != CUB3Variant.EMULATOR) {
                callback()
            }
        }

        // Helpers - extra
        fun isNetworkAvailable(): Boolean {
            val activeNetworkInfo = getInstance().connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun isUsingMobileData(): Boolean {
            val cm = getInstance().connectivityManager
            return cm.getNetworkCapabilities(cm.activeNetwork)?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
        }
    }
}