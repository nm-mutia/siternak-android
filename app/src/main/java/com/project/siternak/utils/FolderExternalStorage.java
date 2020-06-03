package com.project.siternak.utils;

import android.os.Environment;

import java.io.File;

public class FolderExternalStorage {
    public static final String FOLDER_NAME = "SITERNAK";

    public static boolean checkFolder(){
        File f = new File(Environment.getExternalStorageDirectory(), FOLDER_NAME);
        if (!f.exists()) {
            f.mkdirs();
            return true;
        }
        return true;
    }

}
