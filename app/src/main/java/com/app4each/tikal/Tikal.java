package com.app4each.tikal;

import android.app.Application;

import com.app4each.tikal.model.Movie;
import com.app4each.tikal.utils.PicassoCache;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Vito on 10/20/2017.
 */

public class Tikal extends Application {


    public static Picasso PICASSO;
    public static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/";


    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Realm database
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("Tikal.realm")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(config);

        // Init Picaso
        PICASSO = PicassoCache.INSTANCE.getPicassoCache(this);
    }
}
