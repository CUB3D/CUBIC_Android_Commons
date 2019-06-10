package pw.cub3d.commons.configuration

/**
 * Defines the
 */
enum class CUB3Variant(val shouldLog: Boolean, val uploadCrashes: Boolean) {
    RELEASE(false, true),
    DEBUG(true, true),
    EMULATOR(true, false)
}