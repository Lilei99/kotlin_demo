package com.sixiangtianxia.commonlib.permissions

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RestrictTo
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class AppSettingsDialogHolderActivity : AppCompatActivity(), DialogInterface.OnClickListener {

    private var mDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDialog = AppSettingsDialog.`fromIntent`(intent, this).`showDialog`(this, this)
        //        mDialog = AppSettingsDialog.Companion.fromIntent(getIntent(), this).showDialog(this, this);
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mDialog != null && mDialog!!.isShowing) {
            mDialog!!.dismiss()
        }
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        if (which == Dialog.BUTTON_POSITIVE) {
            startActivityForResult(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.fromParts("package", packageName, null)),
                APP_SETTINGS_RC
            )/*.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)*/
        } else if (which == Dialog.BUTTON_NEGATIVE) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        } else {
            throw IllegalStateException("Unknown button type: $which")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        setResult(resultCode, data)
        finish()
    }

    companion object {
        private val APP_SETTINGS_RC = 7534

        fun createShowDialogIntent(context: Context, dialog: AppSettingsDialog): Intent {
            return Intent(context, AppSettingsDialogHolderActivity::class.java)
                .putExtra(AppSettingsDialog.EXTRA_APP_SETTINGS, dialog)
            //                .putExtra(AppSettingsDialog.Companion.getEXTRA_APP_SETTINGS(), dialog);
        }
    }
}
