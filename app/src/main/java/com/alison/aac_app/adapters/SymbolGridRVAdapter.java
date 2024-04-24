package com.alison.aac_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alison.aac_app.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;

public class SymbolGridRVAdapter extends RecyclerView.Adapter<SymbolGridRVAdapter.RecyclerViewHolder> {

    private ArrayList<String> wordsArrayList;
    private ArrayList<String> imagesArrayList;
    private final Context mcontext;
    private final ArrayList<String> originalWordsArrayList;
    private final ArrayList<String> originalImagesArrayList;


    public SymbolGridRVAdapter(ArrayList<String> recyclerDataArrayList, ArrayList<String> imageUrlArrayList, Context mcontext) {
        this.wordsArrayList = recyclerDataArrayList;
        this.imagesArrayList = imageUrlArrayList;
        this.originalWordsArrayList = new ArrayList<>(recyclerDataArrayList);
        this.originalImagesArrayList = new ArrayList<>(imageUrlArrayList);
        this.mcontext = mcontext;
    }

    public void setFilteredList(ArrayList<String> filteredList){
        ArrayList<String> filteredImagesList = new ArrayList<>();

        if (filteredList.isEmpty()) {
            // If the filtered list is empty, reset both lists to their original state
            this.wordsArrayList = originalWordsArrayList;
            this.imagesArrayList = originalImagesArrayList;
        } else {
            for (String word : filteredList) {
                int index = originalWordsArrayList.indexOf(word);
                if (index != -1 && index < originalImagesArrayList.size()) {
                    filteredImagesList.add(originalImagesArrayList.get(index));
                } else {
                    filteredImagesList.add("No hits"); // or some default image URL
                }
            }

            this.wordsArrayList = filteredList;
            this.imagesArrayList = filteredImagesList;
        }

        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String recyclerData = wordsArrayList.get(position);
        holder.word.setText(recyclerData);
        String url = imagesArrayList.get(position);
        if ((holder.image != null) && (!url.equals("No hits")))
        {
            Glide
                    .with(mcontext)
                    .load(url)
                    .transition(DrawableTransitionOptions.withCrossFade()) //for loading image smoothly and quickly.
                    .error(R.drawable.ic_launcher_foreground)
                    .fitCenter()
                    .into(holder.image);
        } else if (url.equals("No hits")) {
            assert holder.image != null;
            Glide
                    .with(mcontext)
                    .clear(holder.image);
        }

    }

    public int getItemCount() {
        return wordsArrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView word;
        private final ImageView image;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.gridWord);
            image = itemView.findViewById(R.id.gridImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            TextView sentenceField = itemView.getRootView().findViewById(R.id.textView3);
            sentenceField.append(word.getText() + " ");
        }
    }

    public void filterList(String query) {
        ArrayList<String> filteredWords = new ArrayList<>();
        ArrayList<String> filteredImages = new ArrayList<>();

        if (query.isEmpty()) {
            filteredWords.addAll(originalWordsArrayList);
            filteredImages.addAll(originalImagesArrayList);
        } else {
            for (int i = 0; i < originalWordsArrayList.size(); i++) {
                if (originalWordsArrayList.get(i).toLowerCase().contains(query.toLowerCase())) {
                    filteredWords.add(originalWordsArrayList.get(i));
                    filteredImages.add(originalImagesArrayList.get(i));
                }
            }
        }

        wordsArrayList = filteredWords;
        imagesArrayList = filteredImages;
        notifyDataSetChanged();
    }

}

