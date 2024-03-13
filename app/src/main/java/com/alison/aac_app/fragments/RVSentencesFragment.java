package com.alison.aac_app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alison.aac_app.R;
import com.alison.aac_app.adapters.MySentenceRecyclerViewAdapter;
import com.alison.aac_app.view_models.RVSharedViewModel;

import java.util.List;

public class RVSentencesFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    public RVSentencesFragment() {
    }

    public static RVSentencesFragment newInstance(int columnCount) {
        RVSentencesFragment fragment = new RVSentencesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rv_sentences_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            RVSharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(RVSharedViewModel.class);
            List<String> newData = viewModel.getData();
            recyclerView.setAdapter(new MySentenceRecyclerViewAdapter(newData));

        }
        return view;
    }
}