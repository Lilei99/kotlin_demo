package com.sixiangtianxia.commonlib.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import com.sixiangtianxia.commonlib.R;

/**
 * Creator: Gao MinMin.
 * Date: 2018/12/6.
 * Description: 加载框工具类.
 */
public class DialogFactory {

    public static Dialog createDialog(Context context, int layoutId, int themeId) {
        View view = View.inflate(context, layoutId, null);
        Dialog dialog;
        dialog = new Dialog(context, themeId);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        return dialog;
    }

    public static Dialog createLoadingDialog(Context context) {
        return createDialog(context, R.layout.layout_loading, R.style.CustomDialog_Translucent);
    }
}
