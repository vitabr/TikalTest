package com.app4each.tikal.controller.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.app4each.tikal.BuildConfig;
import com.app4each.tikal.controller.MessageEvent;
import com.app4each.tikal.model.Movie;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import io.realm.Realm;

/**
 * Created by Vito on 10/21/2017.
 */

public class GetMoviesService extends IntentService {


    public GetMoviesService() {
        super("GetMoviesService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        final TmdbApi api = new TmdbApi(BuildConfig.TMDB_API_KEY);
        final List<MovieDb> moviesDb = api.getMovies().getPopularMovies("en", 1).getResults();//getDiscover().getDiscover(new Discover().sortBy("popularity.desc")).getResults();

        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                // Fisrt save initial movies info
                for (MovieDb movieDb: moviesDb) {
                    Movie movieToAdd = new Movie(movieDb);
                    realm.copyToRealmOrUpdate(movieToAdd);
                }
                EventBus.getDefault().post(new MessageEvent(MessageEvent.EVENT_INITIAL_MOVIE_INFO_RECEIVED));
            }
        });


    }
}
