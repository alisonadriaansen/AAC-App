package com.alison.aac_app.adapters;


import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alison.aac_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;


import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MySentenceRecyclerViewAdapter extends RecyclerView.Adapter<MySentenceRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;
    private static TextToSpeech tts;


    public MySentenceRecyclerViewAdapter(List<String> items, TextToSpeech tts) {
        mValues = items;
        MySentenceRecyclerViewAdapter.tts = tts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_rv_sentences, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mContentView.setText(mValues.get(position));
        if (Objects.equals(mValues.get(0), "No sentences found.")){
            holder.save.setVisibility(View.GONE);
            holder.speak.setVisibility(View.GONE);
        }

        holder.speak.setOnClickListener(v -> {
            if (tts != null && !TextUtils.isEmpty(mValues.get(position))) {
                tts.setLanguage(Locale.UK);
                tts.speak(mValues.get(position), TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mContentView;
        public String mItem;
        public Button save;
        public Button speak;


        private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        private final FirebaseFirestore db = FirebaseFirestore.getInstance();

        public ViewHolder(View itemView) {
            super(itemView);
            mContentView = itemView.findViewById(R.id.content);
            save = itemView.findViewById(R.id.saveBtn);
            speak = itemView.findViewById(R.id.speakBtn);

            save.setOnClickListener(view -> {

                Map<String, Object> map = new HashMap<>();
                map.put("saved-sentences", FieldValue.arrayUnion(mContentView.getText()));
                db.collection("user-data").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).set(map, SetOptions.merge());
            });

        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }


}
