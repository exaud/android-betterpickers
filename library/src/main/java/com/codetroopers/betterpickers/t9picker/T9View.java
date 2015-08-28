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

    private TextView mText;
    private Typeface mOriginalTextTypeface;
    private ColorStateList mTextColor;

    /**
     * Instantiate a T9View
     *
     * @param context the Context in which to inflate the View
     */
    public T9View(Context context) {
        this(context, null);
    }

    /**
     * Instantiate a T9View
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
        if (mText != null) {
            mText.setTextColor(mTextColor);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mText = (TextView) findViewById(R.id.text);
        if (mText != null) {
            mOriginalTextTypeface = mText.getTypeface();
        }

        restyleViews();
    }

    /**
     * Set the text shown
     *
     * @param text the letters
     */
    public void setText(String text) {

        if (mText != null) {
            if (text.isEmpty()) {
                // Set to empty
                mText.setText("");
                mText.setEnabled(false);
            } else {

                // Set to thin
                mText.setText(text);
                mText.setTypeface(mOriginalTextTypeface);
                mText.setEnabled(true);
            }
        }
    }
}
