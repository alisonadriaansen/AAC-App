package com.alison.aac_app.adapters;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alison.aac_app.R;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SavedSentencesRVAdapter extends RecyclerView.Adapter<SavedSentencesRVAdapter.ViewHolder> {

    private final List<String> mValues;

    public SavedSentencesRVAdapter(List<String> mValues) {
        this.mValues = mValues;
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

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mContentView;
        public TextToSpeech textToSpeech;
        public String mItem;
        public Button save;
        public Button speak;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mContentView = itemView.findViewById(R.id.content);
            save = itemView.findViewById(R.id.saveBtn);
            speak = itemView.findViewById(R.id.speakBtn);

            textToSpeech = new TextToSpeech(itemView.getContext(), status -> {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA) {
                        Intent installIntent = new Intent();
                        installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                        itemView.getContext().startActivity(installIntent);
                    } else if (result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(itemView.getContext(), "Language not supported", Toast.LENGTH_SHORT).show();
                        System.out.println("Language not supported");
                    }
                } else {
                    Toast.makeText(itemView.getContext(), "TTS initialization failed: " + status, Toast.LENGTH_SHORT).show();
                    System.out.println("TTS initialization failed: " + status);
                }
            });

            speak.setOnClickListener(view -> {
                if (textToSpeech != null && textToSpeech.isLanguageAvailable(Locale.ENGLISH) == TextToSpeech.LANG_AVAILABLE) {
                    CharSequence toSpeak = mContentView.getText().toString();
                    Toast.makeText(itemView.getContext(), toSpeak, Toast.LENGTH_SHORT).show();
                    textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    Toast.makeText(itemView.getContext(), "TTS engine not initialized or language not available", Toast.LENGTH_SHORT).show();
                    System.out.println("TTS engine not initialized or language not available");
                }
            });
        }
    }
}
