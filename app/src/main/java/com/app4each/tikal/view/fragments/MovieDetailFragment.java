package com.app4each.tikal.view.fragments;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app4each.tikal.R;
import com.app4each.tikal.Tikal;
import com.app4each.tikal.model.Movie;
import com.app4each.tikal.utils.Constants;
import com.app4each.tikal.view.MovieDetailActivity;
import com.app4each.tikal.view.MovieListActivity;
import com.app4each.tikal.view.adapters.TrailersAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment
        extends Fragment
        implements RealmChangeListener, Constants {


    @BindView(R.id.tvYear) TextView mYear;
    @BindView(R.id.tvDuration) TextView mDuration;
    @BindView(R.id.tvRaiting) TextView mRaiting;
    @BindView(R.id.tvDescription) TextView mDescription;
    @BindView(R.id.ivPoster)  ImageView mPoster;
    @BindView(R.id.rvTrailersList) RecyclerView mTrailersList;

    /**
     * The movie content this fragment is presenting.
     */
    private Movie mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(EXTRA_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = Realm.getDefaultInstance().where(Movie.class).equalTo("id", getArguments().getInt(EXTRA_ID)).findFirst();

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.title);
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Realm.getDefaultInstance().addChangeListener(this);
    }

    @Override
    public void onStop() {
        Realm.getDefaultInstance().removeChangeListener(this);
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        ButterKnife.bind(this, rootView);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((CollapsingToolbarLayout) rootView.findViewById(R.id.toolbar_layout)).setTitle(mItem.title);
            Tikal.PICASSO.load( mItem.getPosterUrl()).placeholder(R.drawable.wait_placeholder).into(mPoster);

            mYear.setText(mItem.year);
            mDuration.setText(""+mItem.duration);
            mRaiting.setText("8.0/10");
            mDescription.setText(mItem.description);

        }

        setupRecyclerView(mTrailersList);
        return rootView;
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        recyclerView.setAdapter(new TrailersAdapter(mItem.id));
    }


    //*****************************************/
    ///  Realm Listener
    //*****************************************/
    @Override
    public void onChange(Object element) {
        mTrailersList.getAdapter().notifyDataSetChanged();
    }
}
