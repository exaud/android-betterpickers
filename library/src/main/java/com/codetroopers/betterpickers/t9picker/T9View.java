package com.codetroopers.betterpickers.t9picker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codetroopers.betterpickers.R;

/**
 * User: andreomlopes Date: 8/7/15
 */
public class T9View extends LinearLayout {

    private TextView  mNumber;
    private Typeface mOriginalNumberTypeface;
    private ColorStateList mTextColor;

    /**
     * Instantiate a NumberView
     *
     * @param context the Context in which to inflate the View
     */
    public T9View(Context context) {
        this(context, null);
    }

    /**
     * Instantiate a NumberView
     *
     * @param context the Context in which to inflate the View
     * @param attrs attributes that define the title color
     */
    public T9View(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Init defaults
        mTextColor = getResources().getColorStateList(R.color.dialog_text_color_holo_dark);
    }

    /**
     * Set a theme and restyle the views. This View will change its title color.
     *
     * @param themeResId the resource ID for theming
     */
    public void setTheme(int themeResId) {
        if (themeResId != -1) {
            TypedArray a = getContext().obtainStyledAttributes(themeResId, R.styleable.BetterPickersDialogFragment);

            mTextColor = a.getColorStateList(R.styleable.BetterPickersDialogFragment_bpTextColor);
        }

        restyleViews();
    }

    private void restyleViews() {
        if (mNumber != null) {
            mNumber.setTextColor(mTextColor);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mNumber = (TextView) findViewById(R.id.number);
        if (mNumber != null) {
            mOriginalNumberTypeface = mNumber.getTypeface();
        }

        restyleViews();
    }

    /**
     * Set the text shown
     *
     * @param text the letters
     */
    public void setText(String text) {

        if (mNumber != null) {
            if (text.isEmpty()) {
                // Set to empty
                mNumber.setText("");
                mNumber.setEnabled(false);
            } else {

                // Set to thin
                mNumber.setText(text);
                mNumber.setTypeface(mOriginalNumberTypeface);
                mNumber.setEnabled(true);
            }
        }
    }
}
