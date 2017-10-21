package com.app4each.tikal.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app4each.tikal.R;
import com.app4each.tikal.model.Movie;
import com.app4each.tikal.model.RealmString;
import com.app4each.tikal.utils.Constants;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Vito on 10/21/2017.
 */


public class TrailersAdapter
        extends RecyclerView.Adapter<TrailersAdapter.ViewHolder>
        implements Constants{

    private RealmList<RealmString> mTrailers;
    private int mMovieId;

    public TrailersAdapter(int movieId) {
        Movie movie = Realm.getDefaultInstance().where(Movie.class).equalTo(RECORD_ID, movieId).findFirst();
        mTrailers = movie.videoUrls;
        mMovieId = movieId;
    }


    @Override
    public TrailersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list_item, parent, false);
        return new TrailersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrailersAdapter.ViewHolder holder, int position) {
        holder.trailer = mTrailers.get(position);
        holder.mTitle.setText(holder.trailer.value);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Play trailer externaly
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitle;
        public RealmString trailer;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.tvTitle);
        }
    }
}