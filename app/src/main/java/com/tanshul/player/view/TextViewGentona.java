package com.tanshul.player.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tansdeva on 20/12/17.
 * Custom text-view for Ola-font
 */
public class TextViewGentona extends AppCompatTextView {
    private static Map<String, Typeface> mTypefaces;

    public TextViewGentona(final Context context) {
        this(context, null);
    }

    public TextViewGentona(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextViewGentona(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        if (mTypefaces == null) {
            mTypefaces = new HashMap<>();
        }
        // prevent exception in Android Studio / ADT interface builder
        if (this.isInEditMode()) {
            return;
        }

        final String typefaceAssetPath = "font/Gentona-Book.ttf";
        if (typefaceAssetPath != null) {
            Typeface typeface = null;
            if (mTypefaces.containsKey(typefaceAssetPath)) {
                typeface = mTypefaces.get(typefaceAssetPath);
            } else {
                AssetManager assets = context.getAssets();
                typeface = Typeface.createFromAsset(assets, typefaceAssetPath);
                mTypefaces.put(typefaceAssetPath, typeface);
            }
            setTypeface(typeface);
        }
    }
}
