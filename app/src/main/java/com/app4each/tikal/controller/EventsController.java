package com.app4each.tikal.controller;

import android.content.Context;
import android.content.Intent;

import com.app4each.tikal.services.GetMovieDetailesService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Vito on 10/21/2017.
 *
 * Singleton to handle Events.
 */

public class EventsController {

    private static EventsController INSTANCE;
    private Context mContext;

    private EventsController(Context context){
        mContext = context;
        EventBus.getDefault().register(this);
    }

    public static synchronized EventsController init(Context context){
        if (INSTANCE == null){
            INSTANCE = new EventsController(context);
        }
        return INSTANCE;
    }

    public static void deinit(){
        EventBus.getDefault().unregister(INSTANCE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.eventId){
            case MessageEvent.EVENT_INITIAL_MOVIE_INFO_RECEIVED:
                Intent intent = new Intent(mContext, GetMovieDetailesService.class);
                mContext.startService(intent);
        }
    }

}
