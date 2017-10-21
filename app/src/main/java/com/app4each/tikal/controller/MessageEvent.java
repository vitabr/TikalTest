package com.app4each.tikal.controller;

/**
 * Created by Vito on 10/21/2017.
 */

public class MessageEvent {

    public static final int EVENT_NONE = 0;
    public static final int EVENT_INITIAL_MOVIE_INFO_RECEIVED = 1;

    public int eventId = EVENT_NONE;

    public MessageEvent(int eventId){
        this.eventId = eventId;
    }
}
