package com.tapatuniforms.pos.helper;

import android.content.Context;

import androidx.room.Room;

import com.tapatuniforms.pos.R;

public class DatabaseHelper {
    public static DatabaseSingleton getDatabase(Context context) {
        return Room.databaseBuilder(context,
                DatabaseSingleton.class, context.getString(R.string.database))
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }
}
