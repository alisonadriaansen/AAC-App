package com.alison.aac_app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alison.aac_app.R;
import com.alison.aac_app.activities.AuthActivity;

public class RegisterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_register, container, false);
        super.onViewCreated(view, savedInstanceState);

        Button haveAcc = view.findViewById(R.id.haveAcc);

        haveAcc.setOnClickListener(view1 -> ((AuthActivity) requireActivity()).replaceFragments(RegisterFragment.class));

        return view;
    }
}