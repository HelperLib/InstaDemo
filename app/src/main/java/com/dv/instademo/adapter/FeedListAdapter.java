package com.dv.instademo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dv.instademo.R;
import com.dv.instademo.model.SampleFeed;

import java.util.ArrayList;

public class FeedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<SampleFeed> sampleFeedArrayList;
    private Context context;

    public FeedListAdapter(ArrayList<SampleFeed> sampleFeedArrayList, Context context) {
        this.sampleFeedArrayList = sampleFeedArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        final SampleFeed sampleFeed = sampleFeedArrayList.get(position);

        holder.textViewDescription.setText(sampleFeed.getDescription());
        holder.textViewTitle.setText(sampleFeed.getTitle());
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .priority(Priority.HIGH);
        Glide.with(context)
                .load(sampleFeed.getImage())
                .apply(options)
                .thumbnail(0.9f)
                .into(holder.imageViewFeed);
    }


    @Override
    public int getItemCount() {
        return sampleFeedArrayList == null ? 0 : sampleFeedArrayList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private TextView textViewTitle, textViewDescription;
        private ImageView imageViewFeed;

        private ViewHolder(View view) {
            super(view);
            mView = view;
            textViewTitle = mView.findViewById(R.id.textViewTitle);
            textViewDescription = mView.findViewById(R.id.textViewDescription);
            imageViewFeed = mView.findViewById(R.id.imageViewFeed);

        }


    }

}
