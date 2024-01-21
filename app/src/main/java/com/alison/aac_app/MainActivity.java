package com.alison.aac_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    DBHelper dbHelper= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this, getFilesDir().getAbsolutePath());
        try {
            dbHelper.prepareDatabase();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        showData();
    }

    private void showData() {
        List<String> list = dbHelper.getWords();
        TextView textView = findViewById(R.id.textView);
        textView.setText(list.toString());
    }
}