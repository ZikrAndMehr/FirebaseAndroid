package com.zikrandmehr.firebaseandroid.utils

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.zikrandmehr.firebaseandroid.R

fun Fragment.showErrorAlert(message: CharSequence) {
    val builder = AlertDialog.Builder(requireContext())

    builder.setMessage(message)
    builder.setPositiveButton(getText(R.string.ok)) { dialog, _ ->
        dialog.dismiss()
    }
    builder.create().show()
}