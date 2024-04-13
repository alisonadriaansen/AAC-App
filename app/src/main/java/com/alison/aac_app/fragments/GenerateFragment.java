package com.alison.aac_app.fragments;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alison.aac_app.R;
import com.alison.aac_app.activities.MainActivity;
import com.alison.aac_app.adapters.SymbolGridRVAdapter;
import com.alison.aac_app.database.DBHelper;
import com.alison.aac_app.sentence_logic.AACOpenAIIntegration;
import com.alison.aac_app.view_models.RVSharedViewModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GenerateFragment extends Fragment {

    private final static String TAG = "GenerateFrag";
    SymbolGridRVAdapter myAdapter;
    List<String> myList;
    DBHelper dbHelper = null;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();


    public GenerateFragment() {}


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_generate, container, false);
        super.onViewCreated(view, savedInstanceState);

        Button clear = view.findViewById(R.id.clr);
        Button generate = view.findViewById(R.id.gnrt);
        EditText tv = view.findViewById(R.id.textView3);
        RecyclerView rv = view.findViewById(R.id.gridRV);
        SearchView sv = view.findViewById(R.id.searchView);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 4));

        sv.clearFocus();

        dbHelper = new DBHelper(getActivity(), requireActivity().getFilesDir().getAbsolutePath());
        try {
            dbHelper.prepareDatabase();
        } catch (IOException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
        myList = dbHelper.getWordsMinLength(3);
        List<String> urlList = dbHelper.getImages(myList);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s, myList);
                return true;
            }
        });

        myAdapter = new SymbolGridRVAdapter((ArrayList<String>) myList, (ArrayList<String>) urlList, getActivity());
        rv.setAdapter(myAdapter);
        tv.setMovementMethod(new ScrollingMovementMethod());

        clear.setOnClickListener(view1 -> tv.getText().clear());
        generate.setOnClickListener(view1 -> {
            String userInput = String.valueOf(tv.getText());
            generateResponseAsync(userInput);
        });

        return view;
    }

    private void generateResponseAsync(String words) {
        Future<String> future = executor.submit(new GenerateResponseCallable(words));
        try {
            String response = future.get(); // This will block until the task is completed
            // Handle the generated response here
            Log.d("GeneratedResponse", response);
            String[] sen_list = response.toLowerCase().split("\n");
            // Update UI or take any necessary action with the response
            RVSharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(RVSharedViewModel.class);
            viewModel.setData(Arrays.asList(sen_list));
            ((MainActivity) requireActivity()).replaceFragments(RVSentencesFragment.class);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("GenerateResponseError", "Error occurred: " + e.getMessage());
        }
    }

    private static class GenerateResponseCallable implements Callable<String> {

        private final String words;

        GenerateResponseCallable(String words) {
            this.words = words;
        }

        @Override
        public String call() throws Exception {
            try {
                // Call generateResponse method from AACOpenAIIntegration class
                return AACOpenAIIntegration.generateResponse(words);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                throw new Exception("Error occurred: " + e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Shutdown the executor when the activity is destroyed
        executor.shutdown();
    }

    private void filterList(String s, List<String> myList) {
        ArrayList<String> filteredList = new ArrayList<>();
        for (String word : myList){
            if (word.toLowerCase().contains(s.toLowerCase())){
                filteredList.add(word);
            }
        }

        if (filteredList.isEmpty()){
            Toast.makeText(this.getContext(), "No words found", Toast.LENGTH_SHORT).show();
        }
        else {
            myAdapter.setFilteredList(filteredList);
        }
    }
}