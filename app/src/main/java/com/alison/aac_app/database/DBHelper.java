package com.alison.aac_app.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DBHelper extends SQLiteOpenHelper {
    private final static String TAG = "DatabaseHelper";
    private final Context myContext;
    private static final String DATABASE_NAME = "aacDB.sqlite3";
    private static final int DATABASE_VERSION = 8;
    private final String pathToSaveDBFile;

    public DBHelper(Context context, String filePath) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        pathToSaveDBFile = filePath + "/" + DATABASE_NAME;
    }

    public void prepareDatabase() throws IOException {
        boolean dbExist = checkDataBase();
        if(dbExist) {
            Log.d(TAG, "Database exists.");
            int currentDBVersion = getVersionId();
            if (DATABASE_VERSION > currentDBVersion) {
                Log.d(TAG, "Database version is higher than old.");
                deleteDb();
                try {
                    copyDataBase();
                } catch (IOException e) {
                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                }
            }
        } else {
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            }
        }
    }

    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            File file = new File(pathToSaveDBFile);
            checkDB = file.exists();
        } catch(SQLiteException e) {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
        }
        return checkDB;
    }

    private void copyDataBase() throws IOException {
        OutputStream os = Files.newOutputStream(Paths.get(pathToSaveDBFile));
        InputStream is = myContext.getAssets().open("databases/"+DATABASE_NAME);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.flush();
        os.close();
    }

    public void deleteDb() {
        File file = new File(pathToSaveDBFile);
        if(file.exists()) {
            file.delete();
            Log.d(TAG, "Database deleted.");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

        public List<String> getWordsSentences() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT DISTINCT word FROM words WHERE EXISTS (SELECT 1 FROM sentences WHERE INSTR(sentences.sentence, words.word) > 0);";
        Cursor cursor = db.rawQuery(query, null);
        List<String> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            String word;
            word = cursor.getString(0);
            list.add(word);
        }
        cursor.close();
        db.close();
        System.out.println(list);
        return list;
    }

    public List<String> getWordsMinLength(int minLength) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT DISTINCT word FROM words WHERE LENGTH(word) >= " + minLength;
        Cursor cursor = db.rawQuery(query, null);
        List<String> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            String word;
            word = cursor.getString(0);
            list.add(word);
        }
        cursor.close();
        db.close();
        System.out.println(list);
        return list;
    }

    public List<String> getImages(List<String> wordList) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        StringBuilder whereClauseBuilder = new StringBuilder();
        whereClauseBuilder.append(" WHERE ");
        for (int i = 0; i < wordList.size(); i++) {
            whereClauseBuilder.append("word = ?");
            if (i < wordList.size() - 1) {
                whereClauseBuilder.append(" OR ");
            }
        }

        String query = "SELECT image FROM words" + whereClauseBuilder;

        Cursor cursor = db.rawQuery(query, wordList.toArray(new String[0]));
        List<String> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            String image = cursor.getString(0);
            list.add(image);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getSentences() {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT * FROM sentences";
        Cursor cursor = db.rawQuery(query, null);
        List<String> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            String word;
            word = cursor.getString(0);
            list.add(word);
        }
        cursor.close();
        db.close();
        return list;

    }

    private int getVersionId() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT version_id FROM dbVersion";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int v =  cursor.getInt(0);
        cursor.close();
        db.close();
        return v;
    }
}