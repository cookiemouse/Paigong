package com.tianyigps.dispatch2.utils;

import android.content.Context;
import android.text.ClipboardManager;

/**
 * Created by cookiemouse on 2017/9/8.
 */

public class ClipU {
    public static void clip(Context context, String str) {
        // 将文本内容放到系统剪贴板里。
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(str);
    }
}
