package com.tanshul.player.listener;

/**
 * Created by tansdeva on 20/12/17.
 * Web request response listener
 */

public interface WebRequestListener {

    void onSuccess(String response);

    void onError(String error);
}
