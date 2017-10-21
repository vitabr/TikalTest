package com.app4each.tikal.controller.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.app4each.tikal.BuildConfig;
import com.app4each.tikal.model.Movie;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Vito on 10/21/2017.
 */

public class GetMovieDetailesService extends IntentService {


    public GetMovieDetailesService() {
        super("GetMovieDetailesService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        final TmdbApi api = new TmdbApi(BuildConfig.TMDB_API_KEY);

                RealmResults<Movie> movies = Realm.getDefaultInstance().where(Movie.class).findAll();
                for (Movie movie : movies) {
                    final MovieDb movieDb = api.getMovies().getMovie(movie.id, "en", TmdbMovies.MovieMethod.videos);

                    // Save to db incoming records one by one is slower than in bulk
                    // but it will give us records updated on UI in real time
                    // In case of recognizable overhead, just move all records result saving to single transaction
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            Movie movieToAdd = new Movie(movieDb);
                            realm.copyToRealmOrUpdate(movieToAdd);
                        }
                    });
                }
    }
}
