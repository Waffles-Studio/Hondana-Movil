package com.example.mylogin.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mylogin.model.HondanaUser;

import kotlin.contracts.Returns;

@Database(entities = {HondanaUser.class},
        version = 1,
        exportSchema = false
)

public abstract class AppDatabase extends RoomDatabase{
    public static AppDatabase INSTANCE;
    public abstract UserDao userDao();

    public static AppDatabase getInstance(Context context)
    {
        if (INSTANCE == null)
        {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "bd_hondana.db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }
}
