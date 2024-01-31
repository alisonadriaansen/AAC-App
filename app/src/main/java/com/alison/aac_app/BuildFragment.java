package com.alison.aac_app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuildFragment extends Fragment {

    private final static String TAG = "BuildFrag";
    SymbolGridRVAdapter myAdapter;
    DBHelper dbHelper = null;


    public BuildFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.build_fragment, container, false);
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.gridRV);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 3));

        dbHelper = new DBHelper(getActivity(), getActivity().getFilesDir().getAbsolutePath());
        try {
            dbHelper.prepareDatabase();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        List<String> myList = dbHelper.getWords();

        myAdapter = new SymbolGridRVAdapter((ArrayList<String>) myList, getActivity());
        rv.setAdapter(myAdapter);

        return view;
    }
}