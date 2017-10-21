package com.app4each.tikal.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.app4each.tikal.model.Movie;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbDiscover;
import info.movito.themoviedbapi.model.Discover;
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

        TmdbDiscover api = new TmdbApi("8af268faa6bda7029a469419d1258a14").getDiscover();
        final List<MovieDb> movies = api.getDiscover(new Discover().sortBy("popularity.desc")).getResults();

        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (MovieDb movie: movies) {
                    Movie movieToAdd = new Movie(movie);
                    realm.copyToRealmOrUpdate(movieToAdd);
                }
            }
        });

    }
}
