package com.tanshul.player.web;

import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.tanshul.player.listener.WebRequestListener;

/**
 * Created by tansdeva on 20/12/17.
 * Error handler to check server or connectivity issues
 */
public class ErrorHandling implements Response.ErrorListener {
    public WebRequestListener mListener;

    public ErrorHandling(WebRequestListener webRequestListener) {
        this.mListener = webRequestListener;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        String x = "";
        try {
            x = new String(error.networkResponse.data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            mListener.onError("Time out occured");
        } else if (error instanceof AuthFailureError) {
            mListener.onError(x);
        } else if (error instanceof ServerError) {
            mListener.onError("Error at the server " + x);
        } else if (error instanceof NetworkError) {
            mListener.onError("Network error");
        } else if (error instanceof ParseError) {
            mListener.onError("Parse error");
        } else {
            mListener.onError(x);
        }
    }
}
