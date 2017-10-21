package com.app4each.tikal.model;

import android.util.Log;

import com.app4each.tikal.Tikal;

import info.movito.themoviedbapi.model.MovieDb;
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

    public Movie(){  }

    public Movie(MovieDb movie){
        id = movie.getId();
        duration = movie.getRuntime();
        title = movie.getTitle();
        year = movie.getReleaseDate();
        posterPath = movie.getPosterPath();
        description = movie.getOverview();
        Log.e("Create Movie","id:"+id+", poster:"+posterPath + ", title:"+ title + ", description:"+description);
    }

    public String getPosterUrl(){
        return Tikal.IMAGE_BASE_URL + posterPath;
    }


}
