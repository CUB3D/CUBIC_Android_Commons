package pw.cub3d.commons.utils.fragment

inline fun androidx.fragment.app.Fragment.runOnUiThread(crossinline r: ()->Unit) {
    activity?.runOnUiThread {
        r()
    }
}