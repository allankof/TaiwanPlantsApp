package com.example.taiwanplantapplication.utils;

import android.os.Environment;

import java.io.File;


public class UtilPath {

    public final static File STORAGE_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    public final static String STORAGE_PATH = STORAGE_DIR.getAbsolutePath();
}
