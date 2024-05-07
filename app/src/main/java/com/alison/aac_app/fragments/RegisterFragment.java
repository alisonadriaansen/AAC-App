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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        super.onViewCreated(view, savedInstanceState);

        Button haveAcc = view.findViewById(R.id.haveAcc);
        Button register = view.findViewById(R.id.regBtn);
        EditText email = view.findViewById(R.id.regEmail);
        EditText password = view.findViewById(R.id.regPass);
        EditText passwordConfirm = view.findViewById(R.id.regPassConfirm);

        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(view1 -> {
            String userEmail = String.valueOf(email.getText());
            String userPass = String.valueOf(password.getText());
            String userPassConfirm = String.valueOf(passwordConfirm.getText());

            if (!userEmail.equals("") && !userPass.equals("") && !userPassConfirm.equals("")){
                if ((userPass.length() < 8) || passwordComplexity(userPass)){
                    Toast.makeText(getActivity(), "Password length must be at least 8 characters & contain letters, numbers and symbols.", Toast.LENGTH_LONG).show();
                }
                else {
                    if (!userPass.equals(userPassConfirm)){
                        Toast.makeText(getActivity(), "Passwords must match.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        registerUser(userEmail, userPass);
                    }
                }
            }
            else {
                Toast.makeText(getActivity(), "Fields may not be blank.", Toast.LENGTH_SHORT).show();
            }
        });

        haveAcc.setOnClickListener(view1 -> ((AuthActivity) requireActivity()).replaceFragments(LoginFragment.class));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
            startActivity(new Intent(this.requireActivity(), MainActivity.class));
        }
    }


    public void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        Toast.makeText(getActivity(), "Sign-Up Successful.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this.requireActivity(), MainActivity.class));
                        //FirebaseUser user = mAuth.getCurrentUser();
                        // updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getActivity(), "Sign-Up Failed.", Toast.LENGTH_SHORT).show();
                        // updateUI(null);
                    }
                });
    }

    public static boolean passwordComplexity(String password) {
        String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[@_.]).*$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return password.matches(".*\\d.*") && matcher.matches();
    }
}