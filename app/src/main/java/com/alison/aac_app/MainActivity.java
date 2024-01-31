package com.alison.aac_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button builderBtn = findViewById(R.id.builderBtn);
        Button generatorBtn = findViewById(R.id.generatorBtn);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_view,new BuildFragment());
        fragmentTransaction.commit();

        builderBtn.setOnClickListener(view -> {
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.replace(R.id.fragment_container_view,new BuildFragment());
            fragmentTransaction1.commit();
        });

        generatorBtn.setOnClickListener(view -> {
            FragmentManager fragmentManager12 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction12 = fragmentManager12.beginTransaction();
            fragmentTransaction12.replace(R.id.fragment_container_view,new GenerateFragment());
            fragmentTransaction12.commit();
        });

    }
}