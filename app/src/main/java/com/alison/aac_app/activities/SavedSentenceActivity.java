package com.alison.aac_app.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.alison.aac_app.R;
import com.alison.aac_app.adapters.SavedSentencesRVAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class SavedSentenceActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    ArrayList<String> myArray;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentences);
        this.runOnUiThread(() -> tts = new TextToSpeech(SavedSentenceActivity.this, SavedSentenceActivity.this, "com.google.android.tts"));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        RecyclerView recyclerView = findViewById(R.id.savedRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DocumentReference docRef = db.collection("user-data").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    myArray = (ArrayList<String>) document.get("saved-sentences");
                    SavedSentencesRVAdapter myAdapter = new SavedSentencesRVAdapter(myArray, tts);
                    recyclerView.setAdapter(myAdapter);

                } else {
                    Log.d(TAG, "No such document");
                    myArray = new ArrayList<>();
                    myArray.add("No sentences found.");
                    SavedSentencesRVAdapter myAdapter = new SavedSentencesRVAdapter(myArray, tts);
                    recyclerView.setAdapter(myAdapter);

                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    @Override
    public void onInit(int i) {

    }
}