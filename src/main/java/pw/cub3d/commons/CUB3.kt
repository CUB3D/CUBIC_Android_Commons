package pw.cub3d.commons

import android.content.Context
import android.os.Build
import android.util.Log
import pw.cub3d.commons.configuration.CUB3Config
import pw.cub3d.commons.configuration.CUB3Variant
import pw.cub3d.commons.crashanalytics.CrashTrak
import pw.cub3d.commons.utils.Accounts
import pw.cub3d.commons.utils.SharedPrefs

class CUB3(ctx: Context) {
    val config = CUB3Config(ctx)

    companion object {
        private var instance : CUB3? = null

        fun initialise(ctx: Context) {
            instance = CUB3(ctx)
            CrashTrak.register()
            SharedPrefs.initialise(ctx)
            Accounts.initialise(ctx)

            Log.d("CUB3", "Variant: ${getInstance().config.variant}")
        }

        fun getInstance(): CUB3 {
            return instance!!
        }

        fun getConfig(): CUB3Config {
            return getInstance().config
        }

        fun getVariant(): CUB3Variant {
            return getConfig().variant
        }



        // Helpers - version
        inline fun ifOreo(callback: ()->Unit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
    }
}