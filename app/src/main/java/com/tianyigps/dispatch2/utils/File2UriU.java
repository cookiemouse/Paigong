package com.tianyigps.dispatch2.utils;

import android.net.Uri;

import java.io.File;

/**
 * Created by cookiemouse on 2017/8/1.
 */

public class File2UriU {

    public Uri getUriFromPath(String path) {
        File file = new File(path);
        if (file.exists()) {
            return Uri.fromFile(file);
        }
        return null;
    }

    @Deprecated
    public Uri getUriFromUri(String uri) {
        if (null == uri) {
            return null;
        }
        return Uri.parse(uri);
    }
}
