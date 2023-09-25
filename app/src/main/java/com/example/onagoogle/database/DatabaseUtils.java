package com.example.onagoogle.database;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseUtils {
    public static void copyDatabaseFromAssets(Context context, String dbName) {
        try {
            InputStream inputStream = context.getAssets().open(dbName);
            String outFileName = getDatabasePath(context, dbName);
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getDatabasePath(Context context, String dbName) {
        return context.getApplicationInfo().dataDir + "/databases/" + dbName;
    }
}