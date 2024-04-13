package com.alison.aac_app.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.alison.aac_app.R;
import com.alison.aac_app.adapters.MySentenceRecyclerViewAdapter;
import com.alison.aac_app.adapters.SavedSentencesRVAdapter;
import com.alison.aac_app.adapters.SymbolGridRVAdapter;
import com.alison.aac_app.view_models.RVSharedViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SavedSentenceActivity extends AppCompatActivity {

    ArrayList<String> myArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentences);

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
                    SavedSentencesRVAdapter myAdapter = new SavedSentencesRVAdapter(myArray);
                    recyclerView.setAdapter(myAdapter);

                } else {
                    Log.d(TAG, "No such document");
                    myArray.set(0, "No sentences found.");
                    SavedSentencesRVAdapter myAdapter = new SavedSentencesRVAdapter(myArray);
                    recyclerView.setAdapter(myAdapter);

                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }
}