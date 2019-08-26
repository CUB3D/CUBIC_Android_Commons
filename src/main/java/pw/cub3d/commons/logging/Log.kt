package pw.cub3d.commons.logging

object Log: Logger("Test") {
    fun getLogHistory(): List<String> {
        return logHistory
    }
}

fun debug(d: String) = Log.d(d)
fun debug(d: Any) = Log.d(d.toString())
