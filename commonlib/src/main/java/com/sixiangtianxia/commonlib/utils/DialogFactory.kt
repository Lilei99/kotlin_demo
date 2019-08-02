package com.sixiangtianxia.commonlib.utils

import android.app.Dialog
import android.content.Context
import android.view.View
import com.sixiangtianxia.commonlib.R

object DialogFactory {

    fun createDialog(context: Context, layoutId: Int, themeId: Int): Dialog {
        val view = View.inflate(context, layoutId, null)
        val dialog: Dialog
        dialog = Dialog(context, themeId)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        return dialog
    }

    fun createLoadingDialog(context: Context): Dialog {
        return createDialog(context, R.layout.layout_loading, R.style.CustomDialog_Translucent)
    }
}
