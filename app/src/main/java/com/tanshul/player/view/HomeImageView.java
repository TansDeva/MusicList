package com.tanshul.player.view;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.tanshul.player.utils.Constants;
import com.tanshul.player.utils.PixelUtil;

/**
 * Created by tansdeva on 20/12/17.
 * Custom fixed aspect-ratio image-view
 */
public class HomeImageView extends AppCompatImageView {
    private Context mContext;

    public HomeImageView(final Context context) {
        this(context, null);
    }

    public HomeImageView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeImageView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = PixelUtil.getDisplayInfo().x;
        int height = (int) (width * Constants.ASPECT_RATIO);
        super.onMeasure(width, height);
        setMeasuredDimension(width, height);
    }
}
