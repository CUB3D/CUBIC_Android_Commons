package pw.cub3d.commons.utils

inline fun loop(runnable: ()->Unit) {
    while (true) {
        runnable()
    }
}