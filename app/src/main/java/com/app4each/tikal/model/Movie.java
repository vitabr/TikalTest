package com.app4each.tikal.model;

import android.text.TextUtils;
import android.util.Log;

import com.app4each.tikal.Tikal;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Video;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Vito on 10/21/2017.
 */

public class Movie extends RealmObject{



    @PrimaryKey
    public int id;

    public int duration;
    public String title;
    public String year;
    public String posterPath;
    public String description;
    public RealmList<Trailer> videoUrls = new RealmList<>();

    public Movie(){  }

    public Movie(MovieDb movie){
        id = movie.getId();
        duration = movie.getRuntime();
        title = movie.getTitle();
        year = movie.getReleaseDate();
        posterPath = movie.getPosterPath();
        description = movie.getOverview();
        List<Video> videos = movie.getVideos();

        if( videos != null) {
            for (Video video : videos) {
                if(!TextUtils.isEmpty(video.getSite()) && !TextUtils.isEmpty(video.getId()) ) {
                    Trailer trailer = new Trailer();
                    trailer.name = video.getName();
                    trailer.trailerUrl = "https://www." + video.getSite().toLowerCase() + ".com/watch?v=" + video.getKey();
                    Log.e(" ", "         video url:" + trailer.trailerUrl);
                    videoUrls.add(trailer);
                }
            }
        }
        Log.e("Create Movie","id:"+id+", video urls:" + videoUrls);
    }

    public String getPosterUrl(){
        return Tikal.IMAGE_BASE_URL + posterPath;
    }


}
