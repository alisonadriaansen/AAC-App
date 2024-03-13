package com.alison.aac_app.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alison.aac_app.R;
import com.alison.aac_app.activities.MainActivity;
import com.alison.aac_app.adapters.SymbolGridRVAdapter;
import com.alison.aac_app.database.DBHelper;
import com.alison.aac_app.sentence_logic.BuildSentenceV2;
import com.alison.aac_app.view_models.RVSharedViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BuildFragment extends Fragment {

    private final static String TAG = "BuildFrag";
    SymbolGridRVAdapter myAdapter;
    DBHelper dbHelper = null;
    BuildSentenceV2 buildSentenceV2 = null;

    public BuildFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.build_fragment, container, false);
        super.onViewCreated(view, savedInstanceState);

        Button build = view.findViewById(R.id.bld);
        TextView tv = view.findViewById(R.id.textView3);

        RecyclerView rv = view.findViewById(R.id.gridRV);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 3));

        dbHelper = new DBHelper(getActivity(), requireActivity().getFilesDir().getAbsolutePath());
        try {
            dbHelper.prepareDatabase();
        } catch (IOException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
        List<String> myList = dbHelper.getWordsSentences();
        List<String> urlList = dbHelper.getImages(myList);

        myAdapter = new SymbolGridRVAdapter((ArrayList<String>) myList, (ArrayList<String>) urlList, getActivity());
        rv.setAdapter(myAdapter);

        List<String> sen_list = dbHelper.getSentences();

        build.setOnClickListener(view1 -> {
            String userInput = String.valueOf(tv.getText());
            List<String> userList = Arrays.asList(userInput.split(" "));
            buildSentenceV2 = new BuildSentenceV2();
            ArrayList<String> sentencesMatch = buildSentenceV2.narrowDownSentences(sen_list, userList);
            System.out.println(sentencesMatch);
            RVSharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(RVSharedViewModel.class);
            viewModel.setData((sentencesMatch));
            ((MainActivity) requireActivity()).replaceFragments(RVSentencesFragment.class);
        });

        return view;
    }
}
