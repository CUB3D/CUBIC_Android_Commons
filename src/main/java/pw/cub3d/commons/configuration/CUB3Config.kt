package pw.cub3d.commons.configuration

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import java.io.File


class CUB3Config(ctx: Context) {

    fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || "google_sdk" == Build.PRODUCT)
    }

    private val isDebuggable = 0 != ctx.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    private val isEmulator = isEmulator()

    var variant = when {
        isEmulator -> CUB3Variant.EMULATOR
        isDebuggable -> CUB3Variant.DEBUG
        else -> CUB3Variant.RELEASE
    }

    val dataDirectory = File(ctx.filesDir, "CUB3")

    init {
        dataDirectory.mkdir()
    }

    fun overrideVariant(variant: CUB3Variant) {
        this.variant = variant
    }
}