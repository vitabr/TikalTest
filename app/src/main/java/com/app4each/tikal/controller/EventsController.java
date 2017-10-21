package com.app4each.tikal.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.app4each.tikal.controller.services.GetMovieDetailesService;
import com.app4each.tikal.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Vito on 10/21/2017.
 *
 * Singleton to handle Events.
 */

public class EventsController implements Constants{

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
            case MessageEvent.EVENT_INITIAL_MOVIE_INFO_RECEIVED: {
                Intent intent = new Intent(mContext, GetMovieDetailesService.class);
                mContext.startService(intent);
            }
                break;

            case MessageEvent.EVENT_SHOW_TRAILER: {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.data.getString(EXTRA_URL)));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
                break;
        }
    }

}
