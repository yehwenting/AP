package myandroidhello.com.ap_project.Util;

import android.Manifest;

/**
 * Created by jenny on 2018/2/3.
 */

public class Permissions {
    public static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA

    };
    public static final String[] CAMERA_PERMISSION = {
            Manifest.permission.CAMERA

    };

    public static final String[] WRITE_STORAGE_PERMISSION = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };

    public static final String[] READ_STORAGE_PERMISSION = {
            Manifest.permission.READ_EXTERNAL_STORAGE

    };
}
