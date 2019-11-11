package pw.cub3d.commons.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import pw.cub3d.commons.R

class DialogBuilder(val ctx: Context) {
    val dialog: pw.cub3d.commons.ui.dialog.Dialog = pw.cub3d.commons.ui.dialog.Dialog(ctx)

    init {
        withText("")
        withTitle("")
    }

    fun withTitle(title: String) {
        dialog.title.text = title
    }

    fun withText(text: String) {
        dialog.contentText.text = text
    }

    fun withSpinner() {
        dialog.spinner.visibility = View.VISIBLE
    }

    fun loadIndefinitely() {
        dialog.setCancelable(false)
        withSpinner()
    }

    fun show(): Dialog {
        dialog.show()
        return dialog
    }
}

class Dialog(val ctx: Context) : Dialog(ctx) {

    val title: TextView
    val contentText: TextView
    val spinner: ProgressBar

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog)
        title = findViewById(R.id.dialog_Title)
        contentText = findViewById(R.id.dialog_Content)
        spinner = findViewById(R.id.dialog_spinner)
    }
}