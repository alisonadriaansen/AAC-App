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

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SavedSentencesRVAdapter extends RecyclerView.Adapter<SavedSentencesRVAdapter.ViewHolder> {

    private final List<String> mValues;
    private static TextToSpeech tts;


    public SavedSentencesRVAdapter(List<String> mValues, TextToSpeech tts) {
        this.mValues = mValues;
        this.tts = tts;
    }

    @NonNull
    @Override
    public SavedSentencesRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_rv_sentences, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedSentencesRVAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position));
        holder.save.setVisibility(View.GONE);
        if (Objects.equals(mValues.get(0), "No sentences found.")){
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mContentView = itemView.findViewById(R.id.content);
            save = itemView.findViewById(R.id.saveBtn);
            speak = itemView.findViewById(R.id.speakBtn);

        }
    }
}
