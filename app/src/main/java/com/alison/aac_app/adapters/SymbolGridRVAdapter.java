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
import java.util.List;

public class SymbolGridRVAdapter extends RecyclerView.Adapter<SymbolGridRVAdapter.RecyclerViewHolder> {

    private ArrayList<String> wordsArrayList;
    private final ArrayList<String> imagesArrayList;
    private final Context mcontext;

    public SymbolGridRVAdapter(ArrayList<String> recyclerDataArrayList, ArrayList<String> imageUrlArrayList, Context mcontext) {
        this.wordsArrayList = recyclerDataArrayList;
        this.imagesArrayList = imageUrlArrayList;
        this.mcontext = mcontext;
    }

    public void setFilteredList(ArrayList<String> filteredList){
        this.wordsArrayList = filteredList;

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

}

