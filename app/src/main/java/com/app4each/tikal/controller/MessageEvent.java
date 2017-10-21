package com.app4each.tikal.controller;

import android.os.Bundle;

/**
 * Created by Vito on 10/21/2017.
 */

public class MessageEvent {

    public static final int EVENT_NONE = 0;
    public static final int EVENT_INITIAL_MOVIE_INFO_RECEIVED = 1;
    public static final int EVENT_OPEN_MOVIE_ACTIVITY = 2;
    public static final int EVENT_SHOW_TRAILER = 3;


    public int eventId = EVENT_NONE;
    public Bundle data;

    public MessageEvent(int eventId){
        this.eventId = eventId;
    }

    public MessageEvent(int eventId, Bundle data){
        this.eventId = eventId;
        this.data = data;
    }

}
