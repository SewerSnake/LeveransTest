package com.example.leveranstest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, "DataBase", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE Highscores ( id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "points INTEGER);";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addHighScore(String name, int points) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cvs = new ContentValues();
        cvs.put("name", name);
        cvs.put("points", points);
        long id = db.insert("Highscores",null, cvs);
        Log.d("ITHS", "id of inserted high score: " + id);
        db.close();
    }

    public List<HighScore> getAllHighScores() {
        List<HighScore> highScoreList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query("Highscores", null, null,
                null, null, null, null);

        boolean success = c.moveToFirst();

        if (!success) {
            return highScoreList; //Returnerar en tom lista om det inte lyckas. Bättre än att returnera null!
        }

        //Loop for every row in the table
        do {

            HighScore highScore = new HighScore();

            highScore.id = c.getLong(0);
            highScore.name = c.getString(1);
            highScore.points = c.getInt(2);

            highScoreList.add(highScore);

        } while (c.moveToNext());

        db.close();
        return highScoreList;
    }
}
