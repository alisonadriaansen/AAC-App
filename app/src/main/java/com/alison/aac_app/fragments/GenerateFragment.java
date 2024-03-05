package com.alison.aac_app.fragments;

import android.os.Bundle;
import java.util.concurrent.Future;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.alison.aac_app.DBHelper;
import com.alison.aac_app.R;
import com.alison.aac_app.SymbolGridRVAdapter;
import com.alison.aac_app.sentence_logic.AACOpenAIIntegration;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GenerateFragment extends Fragment {

    private final static String TAG = "GenerateFrag";
    SymbolGridRVAdapter myAdapter;
    DBHelper dbHelper = null;
    private ExecutorService executor = Executors.newSingleThreadExecutor();


    public GenerateFragment() {}


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_generate, container, false);
        super.onViewCreated(view, savedInstanceState);

        Button clear = view.findViewById(R.id.clr);
        Button generate = view.findViewById(R.id.gnrt);
        EditText tv = view.findViewById(R.id.textView3);
        RecyclerView rv = view.findViewById(R.id.gridRV);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 3));

        dbHelper = new DBHelper(getActivity(), getActivity().getFilesDir().getAbsolutePath());
        try {
            dbHelper.prepareDatabase();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        List<String> myList = dbHelper.getWords(3, 0, false);
        List<String> urlList = dbHelper.getImages(myList);

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
            // Update UI or take any necessary action with the response
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("GenerateResponseError", "Error occurred: " + e.getMessage());
        }
    }

    private class GenerateResponseCallable implements Callable<String> {

        private String words;

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

}