package com.alison.aac_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class SymbolGridRVAdapter extends RecyclerView.Adapter<SymbolGridRVAdapter.RecyclerViewHolder> {

    private ArrayList<String> wordsArrayList;
    private ArrayList<String> imagesArrayList;
    private Context mcontext;


    public SymbolGridRVAdapter(ArrayList<String> recyclerDataArrayList, ArrayList<String> imageUrlArrayList, Context mcontext) {
        this.wordsArrayList = recyclerDataArrayList;
        this.imagesArrayList = imageUrlArrayList;
        this.mcontext = mcontext;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
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
        }

    }

    public int getItemCount() {
        // this method returns the size of recyclerview
        return wordsArrayList.size();
    }


    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView word;
        private ImageView image;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.gridWord);
            image = itemView.findViewById(R.id.gridImage);
        }
    }
}

