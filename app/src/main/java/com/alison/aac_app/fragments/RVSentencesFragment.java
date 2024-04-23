package com.alison.aac_app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
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
import java.util.Locale;

public class RVSentencesFragment extends Fragment implements TextToSpeech.OnInitListener{

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private TextToSpeech tts;


    public RVSentencesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().runOnUiThread(() -> tts = new TextToSpeech(getActivity(), this, "com.google.android.tts"));
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rv_sentences_list, container, false);

        List<TextToSpeech.EngineInfo> engines = tts.getEngines();
        for (TextToSpeech.EngineInfo engine : engines) {
            Log.d("TTS", "Engine: " + engine.name);
        }

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
            recyclerView.setAdapter(new MySentenceRecyclerViewAdapter(newData, tts));

        }
        return view;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.ENGLISH);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported");
            }
        } else {
            Log.e("TTS", "Initialization failed");
        }
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}