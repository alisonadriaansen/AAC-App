package com.alison.aac_app.fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alison.aac_app.R;
import com.alison.aac_app.activities.AuthActivity;
import com.alison.aac_app.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        super.onViewCreated(view, savedInstanceState);

        Button login = view.findViewById(R.id.logBtn);
        Button noAcc = view.findViewById(R.id.noAcc);
        EditText email = view.findViewById(R.id.logEmail);
        EditText password = view.findViewById(R.id.logPass);
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(view1 -> {
            String userEmail = String.valueOf(email.getText());
            String userPass = String.valueOf(password.getText());
            if (!userEmail.equals("") && !userPass.equals("")){
                signInUser(userEmail, userPass);
            }
            else {
                Toast.makeText(getActivity(), "Fields may not be blank.", Toast.LENGTH_SHORT).show();
            }
        });

        noAcc.setOnClickListener(view1 -> ((AuthActivity) requireActivity()).replaceFragments(RegisterFragment.class));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
            System.out.println(currentUser);
            startActivity(new Intent(this.requireActivity(), MainActivity.class));
        }
    }

    public void signInUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        Toast.makeText(getActivity(), "Login Successful.",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this.requireActivity(), MainActivity.class));

                        //FirebaseUser user = mAuth.getCurrentUser();
                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(getActivity(), "Login Failed.",
                                Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }
                });
    }

}