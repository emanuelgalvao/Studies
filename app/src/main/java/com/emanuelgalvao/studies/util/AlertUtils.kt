package com.emanuelgalvao.studies.util

import android.app.AlertDialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.emanuelgalvao.studies.R
import com.google.android.material.snackbar.Snackbar

class AlertUtils {

    companion object {

        private lateinit var dialog: AlertDialog

        fun showSnackbar(view: View, message: String, color: Int) {
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            snackbar.setBackgroundTint(color)
            snackbar.show()
        }

        fun showInfoDialog(context: Context, title: String, message: String) {
            val builder = AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok") { _, _ -> run {} }

            builder.show()
        }

        fun showProgressDialog(context: Context, message: String) {
            val builder = AlertDialog.Builder(context)
            val customLayout: View = View.inflate(context, R.layout.dialog_progress, null)
            customLayout.findViewById<TextView>(R.id.text_progress).text = message
            builder.setView(customLayout)

            dialog = builder.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
        }

        fun closeProgressDialog() {
            dialog.dismiss()
        }
    }
}