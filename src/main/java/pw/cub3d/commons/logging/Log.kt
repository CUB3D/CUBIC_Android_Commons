package pw.cub3d.commons.logging

object Log: Logger("Test") {
    fun getLogHistory(): List<String> {
        return logHistory
    }
}