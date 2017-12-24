package com.tanshul.player.web;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.tanshul.player.utils.Utility;

/**
 * Created by tansdeva on 20/12/17.
 * Volley web-request singleton class
 */
public class VolleySingleton {
    private static Context mContext;
    private RequestQueue mRequestQueue;
    private static VolleySingleton mInstance;

    private static final int MY_SOCKET_TIMEOUT_MS = 15000;
    public static final String TAG = VolleySingleton.class.getSimpleName();

    private VolleySingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public void setContext(Context context) {
        VolleySingleton.mContext = context;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(tag);
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (!Utility.isConnected())
            return;
        getRequestQueue().add(request);
    }

    /**
     * if you want to cancel your request, then use this method!
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public String getCacheFromUrl(String url) {
        Cache.Entry cacheEntry = getRequestQueue().getCache().get(url);
        String jsonResponse = "";
        if (cacheEntry != null)
            jsonResponse = new String(cacheEntry.data);
        return jsonResponse;
    }

    public void setCache(String url, String jsonResponse) {
        Cache.Entry cacheEntry = new Cache.Entry();
        cacheEntry.data = jsonResponse.getBytes();
        getRequestQueue().getCache().put(url, cacheEntry);
    }
}
