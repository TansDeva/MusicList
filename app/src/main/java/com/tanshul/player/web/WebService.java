package com.tanshul.player.web;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.tanshul.player.listener.WebRequestListener;
import com.tanshul.player.utils.Constants;

import org.json.JSONArray;

/**
 * Created by tansdeva on 20/12/17.
 * Web-services list to connect to server
 */
public class WebService {
    private Context mContext;
    private VolleySingleton mVolleySingleton;

    public WebService(Context context) {
        mContext = context;
        mVolleySingleton = VolleySingleton.getInstance(mContext);
    }

    public void getSearchList(int page, final WebRequestListener listener) {
        int method = Request.Method.GET;
        ErrorHandling errorHandling = new ErrorHandling(listener);
        JsonWebRequest webRequest = new JsonWebRequest(method, Constants.SERVER_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        listener.onSuccess(response.toString());
                    }
                }, errorHandling);
        mVolleySingleton.addToRequestQueue(webRequest, VolleySingleton.TAG);
    }
}
