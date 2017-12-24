package com.tanshul.player.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tanshul.player.PlayerApp;
import com.tanshul.player.R;

/**
 * Created by tansdeva on 20/12/17.
 * Utility class to access common functions
 */

public class Utility {
    private static Context mContext = PlayerApp.getContext();
    public static final int BUILD_VERSION = Build.VERSION.SDK_INT;
    public static final int VERSION_LOLLIPOP = Build.VERSION_CODES.LOLLIPOP;
    public static final int VERSION_MARSHMALLOW = Build.VERSION_CODES.M;
    public static final int VERSION_NOUGAT = Build.VERSION_CODES.N;

    public static boolean isLollipop() {
        return BUILD_VERSION >= VERSION_LOLLIPOP;
    }

    public static boolean isMarshMallow() {
        return BUILD_VERSION >= VERSION_MARSHMALLOW;
    }

    public static boolean isNougat() {
        return BUILD_VERSION >= VERSION_NOUGAT;
    }

    public static boolean validateString(String value) {
        return value != null && !value.isEmpty();
    }

    public static int getInt(int resId) {
        return mContext.getResources().getInteger(resId);
    }

    public static String getString(int resId) {
        return mContext.getResources().getString(resId);
    }

    public static int getColor(int resId) {
        return mContext.getResources().getColor(resId);
    }

    public static int getDimen(int resId) {
        return (int) mContext.getResources().getDimension(resId);
    }

    public static Drawable getDrawable(int id) {
        return mContext.getResources().getDrawable(id);
    }

    public static boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void showMessage(int msgId, View view) {
        if (view == null) {
            return;
        }
        Snackbar.make(view, msgId, Snackbar.LENGTH_LONG).show();
    }

    public static void showMessage(String message, View view) {
        if (view == null) {
            return;
        }
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public static void hideKeyboard(EditText editText, boolean clear) {
        InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        if (clear) {
            editText.setText("");
        }
    }

    public static void showKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static String getImageUrl(int size, String imageUrl) {
        String baseUrl = Constants.URL_IMAGE_OPTIM + size + "x" + size + ",scale-down,quality=low/";
        return baseUrl + imageUrl;
    }

    public static String getImageUrl(int width, int height, String imageUrl) {
        String baseUrl = Constants.URL_IMAGE_OPTIM + width + "x" + height + ",scale-down,quality=low/";
        return baseUrl + imageUrl;
    }

    public static boolean isStoragePermission(final Context context) {
        if (isMarshMallow()) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(R.string.msg_permission_needed);
                    alertBuilder.setMessage(R.string.msg_permission_storage);
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.GALLERY_REQUEST);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.GALLERY_REQUEST);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static void getFileDownload(String url, String name, View view) {
        Uri uri = Uri.parse(url);
        String fileName = name + ".mp3";
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedOverRoaming(true);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        Utility.showMessage(R.string.msg_start_download, view);
        manager.enqueue(request);
    }
}
