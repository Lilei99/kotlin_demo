package com.sixiangtianxia.commonlib.permissions

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.support.v4.app.Fragment
import com.sixiangtianxia.commonlib.permissions.helper.PermissionHelper

import java.util.Arrays

/**
 * Click listener for either [RationaleDialogFragment] or [RationaleDialogFragmentCompat].
 */
internal class RationaleDialogClickListener : DialogInterface.OnClickListener {

    private var mHost: Any? = null
    private var mConfig: RationaleDialogConfig? = null
    private var mCallbacks: EasyPermissions.PermissionCallbacks? = null
    private var mRationaleCallbacks: EasyPermissions.RationaleCallbacks? = null

    constructor(
        compatDialogFragment: RationaleDialogFragmentCompat,
        config: RationaleDialogConfig,
        callbacks: EasyPermissions.PermissionCallbacks,
        rationaleCallbacks: EasyPermissions.RationaleCallbacks
    ) {

        mHost = if (compatDialogFragment.parentFragment != null)
            compatDialogFragment.parentFragment
        else
            compatDialogFragment.activity

        mConfig = config
        mCallbacks = callbacks
        mRationaleCallbacks = rationaleCallbacks

    }

    constructor(
        dialogFragment: RationaleDialogFragment,
        config: RationaleDialogConfig,
        callbacks: EasyPermissions.PermissionCallbacks,
        dialogCallback: EasyPermissions.RationaleCallbacks
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mHost = if (dialogFragment.parentFragment != null)
                dialogFragment.parentFragment
            else
                dialogFragment.activity
        } else {
            mHost = dialogFragment.activity
        }

        mConfig = config
        mCallbacks = callbacks
        mRationaleCallbacks = dialogCallback
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        val requestCode = mConfig!!.requestCode
        if (which == Dialog.BUTTON_POSITIVE) {
            val permissions = mConfig!!.permissions
            if (mRationaleCallbacks != null) {
                mRationaleCallbacks!!.onRationaleAccepted(requestCode)
            }
            if (mHost is Fragment) {
                PermissionHelper.newInstance(mHost as Fragment?).directRequestPermissions(requestCode, *permissions!!)
            } else if (mHost is android.app.Fragment) {
                PermissionHelper.newInstance(mHost as android.app.Fragment?)
                    .directRequestPermissions(requestCode, *permissions!!)
            } else if (mHost is Activity) {
                PermissionHelper.newInstance(mHost as Activity?).directRequestPermissions(requestCode, *permissions!!)
            } else {
                throw RuntimeException("Host must be an Activity or Fragment!")
            }
        } else {
            if (mRationaleCallbacks != null) {
                mRationaleCallbacks!!.onRationaleDenied(requestCode)
            }
            notifyPermissionDenied()
        }
    }

    private fun notifyPermissionDenied() {
        if (mCallbacks != null) {
            mCallbacks!!.onPermissionsDenied(mConfig!!.requestCode, (mConfig!!.permissions) as List<String>)
        }
    }
}
