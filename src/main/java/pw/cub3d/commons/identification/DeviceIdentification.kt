package pw.cub3d.commons.identification

import pw.cub3d.commons.utils.SharedPrefs
import java.util.*

object DeviceIdentification {
    private const val DEVICE_ID_KEY = "DEVICE_ID"
    private const val DEVICE_ID_RESET_TIME_KEY = "DEVICE_ID_TIMEOUT"

    // Reset the the device id after 7 days, needs to remain large enough for a/b testing
    private const val RESET_TIME_DELAY = 7 * 24 * 60 * 60 * 1000L

    private var deviceID: String = ""

    fun initialise() {
        // If this is the first time we launch or if we need to reset the id
        if(!SharedPrefs.contains(DEVICE_ID_KEY) && System.currentTimeMillis() > SharedPrefs.getLong(DEVICE_ID_RESET_TIME_KEY, 0)) {
            SharedPrefs.edit().apply {
                deviceID = UUID.randomUUID().toString()
                putString(DEVICE_ID_KEY, deviceID)
                putLong(DEVICE_ID_RESET_TIME_KEY, System.currentTimeMillis() + RESET_TIME_DELAY)
                apply()
            }
        } else {
            deviceID = SharedPrefs.getString(DEVICE_ID_KEY, "")!!
        }
    }

    fun getDeviceID(): String = deviceID
}