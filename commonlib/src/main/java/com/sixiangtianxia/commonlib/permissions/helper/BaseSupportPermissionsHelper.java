package com.sixiangtianxia.commonlib.permissions.helper;

import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.sixiangtianxia.commonlib.permissions.RationaleDialogFragmentCompat;


/**
 * Implementation of {@link PermissionHelper} for Support Library host classes.
 */
public abstract class BaseSupportPermissionsHelper<T> extends PermissionHelper<T> {

    private static final String TAG = "BSPermissionsHelper";

    public BaseSupportPermissionsHelper(@NonNull T host) {
        super(host);
    }

    public abstract FragmentManager getSupportFragmentManager();

    @Override
    public void showRequestPermissionRationale(@NonNull String rationale,
                                               @NonNull String positiveButton,
                                               @NonNull String negativeButton,
                                               @StyleRes int theme,
                                               int requestCode,
                                               @NonNull String... perms) {

        FragmentManager fm = getSupportFragmentManager();

        // Check if fragment is already showing
        Fragment fragment = fm.findFragmentByTag(RationaleDialogFragmentCompat.Companion.getTAG());
        if (fragment instanceof RationaleDialogFragmentCompat) {
            return;
        }

        RationaleDialogFragmentCompat.Companion
                .newInstance(rationale, positiveButton, negativeButton, theme, requestCode, perms)
                .showAllowingStateLoss(fm, RationaleDialogFragmentCompat.Companion.getTAG());
    }
}
