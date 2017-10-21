package com.app4each.tikal.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app4each.tikal.R;
import com.app4each.tikal.Tikal;
import com.app4each.tikal.controller.MessageEvent;
import com.app4each.tikal.model.Movie;
import com.app4each.tikal.utils.Constants;
import com.app4each.tikal.view.MovieDetailActivity;

import org.greenrobot.eventbus.EventBus;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Vito on 10/21/2017.
 */


public class MovieRecyclerViewAdapter
        extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder>
        implements Constants{

    private RealmResults<Movie> mItems;
    private boolean mTwoPane;

    public MovieRecyclerViewAdapter(boolean isTwoPane) {
        mItems = Realm.getDefaultInstance().where(Movie.class).findAll();
        mTwoPane = isTwoPane;
    }

    @Override
    public MovieRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        return new MovieRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieRecyclerViewAdapter.ViewHolder holder, int position) {
        Log.e("onBindViewHolder", "position:" + position + ", id:" + mItems.get(position).id);
        holder.movie = mItems.get(position);
        Tikal.PICASSO.load( mItems.get(position).getPosterUrl()).placeholder(R.drawable.wait_placeholder).into(holder.mImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    Bundle data = new Bundle();
                    data.putInt(EXTRA_ID, holder.movie.id);
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.EVENT_OPEN_MOVIE_ACTIVITY,data));
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(EXTRA_ID, holder.movie.id);

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public Movie movie;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.imgPoster);
        }
    }
}