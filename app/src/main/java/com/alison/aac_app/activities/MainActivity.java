package com.alison.aac_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alison.aac_app.R;
import com.alison.aac_app.fragments.BuildFragment;
import com.alison.aac_app.fragments.GenerateFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button builderBtn = findViewById(R.id.builderBtn);
        Button generatorBtn = findViewById(R.id.generatorBtn);
        Button logOut = findViewById(R.id.logOutBtn);
        Button sentences = findViewById(R.id.savedSentences);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_view,new BuildFragment());
        fragmentTransaction.commit();

        builderBtn.setOnClickListener(view -> replaceFragments(BuildFragment.class));

        generatorBtn.setOnClickListener(view -> replaceFragments(GenerateFragment.class));

        logOut.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
        });

        sentences.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SavedSentenceActivity.class)));

    }

    public void replaceFragments(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container_view, Objects.requireNonNull(fragment))
                .commit();
    }
}