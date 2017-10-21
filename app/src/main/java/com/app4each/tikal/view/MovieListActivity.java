package com.app4each.tikal.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewTreeObserver;


import com.app4each.tikal.R;
import com.app4each.tikal.controller.MessageEvent;
import com.app4each.tikal.model.Movie;
import com.app4each.tikal.controller.services.GetMovieDetailesService;
import com.app4each.tikal.controller.services.GetMoviesService;
import com.app4each.tikal.utils.Constants;
import com.app4each.tikal.view.adapters.MoviesAdapter;
import com.app4each.tikal.view.fragments.MovieDetailFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * An activity representing a list of Movie. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity
        extends AppCompatActivity
        implements RealmChangeListener, Constants {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RecyclerView mRecyclerView;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        mRealm = Realm.getDefaultInstance();
        mRealm.addChangeListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.movie_list);
        assert mRecyclerView != null;

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
            RealmResults<Movie> movies = mRealm.where(Movie.class).findAll();
            if(!movies.isEmpty()) {
                Bundle arguments = new Bundle();
                arguments.putInt(EXTRA_ID, movies.first().id);
                MovieDetailFragment fragment = new MovieDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, fragment)
                        .commit();
            }
        }

        // Initialize recycle view with db records.
        setupRecyclerView(mRecyclerView);
        refreshDataAsync();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        recyclerView.setAdapter(new MoviesAdapter(mTwoPane));
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int viewWidth = recyclerView.getMeasuredWidth();
                        float cardViewWidth = getResources().getDimension(R.dimen.item_width);
                        int newSpanCount = (int) Math.floor(viewWidth / cardViewWidth);
                        ((GridLayoutManager)recyclerView.getLayoutManager()).setSpanCount(newSpanCount);
                        recyclerView.getLayoutManager().requestLayout();
                    }
                });
    }

    private void refreshDataAsync(){
        Intent intent = new Intent(this, GetMoviesService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        mRealm.removeAllChangeListeners();
        mRealm.close();

        super.onDestroy();
    }

    //*****************************************/
    ///  Event Bus Subscriber
    //*****************************************/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.eventId){
            case MessageEvent.EVENT_OPEN_MOVIE_ACTIVITY:
                Bundle arguments = new Bundle();
                arguments.putInt(EXTRA_ID, event.data.getInt(EXTRA_ID));
                MovieDetailFragment fragment = new MovieDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, fragment)
                        .commit();
        }
    }

    ///  Realm Listener
    //*****************************************/
    @Override
    public void onChange(Object element) {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }


}
