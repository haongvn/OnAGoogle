package com.example.onagoogle.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.onagoogle.dao.DictionaryDao;
import com.example.onagoogle.entity.DictionaryWord;

@Database(entities = {DictionaryWord.class}, version = 1)
public abstract class DictionaryDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "englishdictionary.db";
    private static final String DATABASE_PATH = "/data/data/com.example.onagoogle/databases/";

    private static DictionaryDatabase instance;

    public static synchronized DictionaryDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            DictionaryDatabase.class, getDatabasePath(context))
                    .createFromAsset(DATABASE_NAME)
                    .build();
        }
        return instance;
    }

    public abstract DictionaryDao dictionaryDao();

    private static String getDatabasePath(Context context) {
        return DATABASE_PATH + DATABASE_NAME;
    }
}