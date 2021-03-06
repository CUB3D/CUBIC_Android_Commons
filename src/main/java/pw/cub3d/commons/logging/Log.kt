package pw.cub3d.commons.logging


object Log: Logger("BaseLogger") {
    fun getLogHistory(): List<String> {
        return logHistory
    }
}

fun Any.getLogger() = Logger(this.javaClass.simpleName)

fun Any.debug(msg: String) = getLogger().d(msg)
fun Any.debug(msg: Any) = debug(msg.toString())

fun Any.info(msg: String) = getLogger().i(msg)
fun Any.info(msg: Any) = getLogger().info(msg.toString())
