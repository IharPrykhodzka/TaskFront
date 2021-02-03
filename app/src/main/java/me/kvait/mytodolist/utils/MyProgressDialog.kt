package me.kvait.mytodolist.utils

import android.app.ProgressDialog
import android.content.Context
import me.kvait.mytodolist.R

inline fun Context.myProgressDialog(
        cancelable: Boolean = false,
        init: (ProgressDialog.() -> Unit) = {}
) = progressDialog(
        indeterminate = true,
        message = getString(R.string.messagePD),
        title = getString(R.string.titlePD),
        cancelable = cancelable,
        init = init
)

inline fun Context.progressDialog(
        indeterminate: Boolean,
        message: CharSequence? = null,
        title: CharSequence? = null,
        cancelable: Boolean = false,
        init: (ProgressDialog.() -> Unit) = {}
) = ProgressDialog(this).apply {
    isIndeterminate = indeterminate
    if (!indeterminate) setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
    if (message != null) setMessage(message)
    if (title != null) setTitle(title)
    setCancelable(cancelable)
    init()
    show()
}