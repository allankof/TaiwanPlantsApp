package com.example.taiwanplantapplication.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

public class BitmapReSize {

    public BitmapReSize() {
    }

    /**
        * @param imagePath
        *    影象的路徑
        * @param width
        *   指定輸出影象的寬度
        * @param height
        *   指定輸出影象的高度
        * @return 生成的縮圖
        */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 獲取這個圖片的寬和高，此處的bitmap為null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 設為 false
        // 計算縮放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        }
        else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新讀入圖片，讀取縮放後的bitmap，這次要把options.inJustDecodeBounds 設為 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils來建立縮圖，這裡要指定要縮放哪個Bitmap物件
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}
