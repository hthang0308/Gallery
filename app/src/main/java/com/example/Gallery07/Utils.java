package com.example.Gallery07;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Utils {

    public static int photoNumber = 0;
    public static String trashFolder = "Trash";
    public static String defaultFolder = "Camera";
    public static final String galleryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DCIM" + File.separator + "Camera";
    public static Context mContext;
    public static Fragment1 utils_fragment1;

    public static void setmContext(Context _mContext) {
        mContext = _mContext;
    }

    public static void copyFile(String srcPath, String desPath) {
        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File(desPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            in = new FileInputStream(srcPath);
            out = new FileOutputStream(desPath + File.separator + createFileName());

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;


        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    public static String createFileName() {
        photoNumber++;
        if (photoNumber > 99)
            photoNumber = 0;
        return (new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + photoNumber + ".png");
    }

    public static void deleteImage(String path) {
        if (new File(path).getParentFile().getName().equals(trashFolder)) {
            File fi = new File(path);
            if (fi.exists())
                fi.delete();
        } else {
            copyFile(path, mContext.getFilesDir().getAbsolutePath() + File.separator + trashFolder);
            if (new File(path).getParentFile().getName().equals(defaultFolder)) {
                deleteGalleryImage(new File(path));
            } else {
                new File(path).delete();
            }
        }
    }

    public static void deleteGalleryImage(File file) {
        String[] projection = {MediaStore.Images.Media._ID};
        String selection = MediaStore.Images.Media.DATA + " = ?";
        String[] selectionArgs = new String[]{file.getAbsolutePath()};
        Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
        if (c.moveToFirst()) {
            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            contentResolver.delete(deleteUri, null, null);
        } else {
            // File not found in media store DB
        }
        c.close();
    }
}