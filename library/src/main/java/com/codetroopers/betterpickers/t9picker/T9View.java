package com.codetroopers.betterpickers.t9picker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.codetroopers.betterpickers.R;
import com.codetroopers.betterpickers.widget.ZeroTopPaddingTextView;

/**
 * User: andreomlopes Date: 8/7/15
 */
public class T9View extends LinearLayout {

    private ZeroTopPaddingTextView mNumber, mDecimal;
    private ZeroTopPaddingTextView mDecimalSeperator;
    private ZeroTopPaddingTextView mMinusLabel;
    private final Typeface mAndroidClockMonoThin;
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

        mAndroidClockMonoThin =
                Typeface.createFromAsset(context.getAssets(), "fonts/AndroidClockMono-Thin.ttf");

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
        if (mDecimal != null) {
            mDecimal.setTextColor(mTextColor);
        }
        if (mDecimalSeperator != null) {
            mDecimalSeperator.setTextColor(mTextColor);
        }
        if (mMinusLabel != null) {
            mMinusLabel.setTextColor(mTextColor);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mNumber = (ZeroTopPaddingTextView) findViewById(R.id.number);
        mDecimal = (ZeroTopPaddingTextView) findViewById(R.id.decimal);
        mDecimalSeperator = (ZeroTopPaddingTextView) findViewById(R.id.decimal_separator);
        mMinusLabel = (ZeroTopPaddingTextView) findViewById(R.id.minus_label);
        if (mNumber != null) {
            mOriginalNumberTypeface = mNumber.getTypeface();
        }
        // Set the lowest time unit with thin font
        if (mNumber != null) {
            mNumber.setTypeface(mAndroidClockMonoThin);
            mNumber.updatePadding();
        }
        if (mDecimal != null) {
            mDecimal.setTypeface(mAndroidClockMonoThin);
            mDecimal.updatePadding();
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
                // Set to -
                mNumber.setText("-");
                mNumber.setTypeface(mAndroidClockMonoThin);
                mNumber.setEnabled(false);
                mNumber.updatePadding();
                mNumber.setVisibility(View.VISIBLE);
            } else {

                // Set to thin
                mNumber.setText(text);
                mNumber.setTypeface(mOriginalNumberTypeface);
                mNumber.setEnabled(true);
                mNumber.updatePadding();
                mNumber.setVisibility(View.VISIBLE);
            }
        }
    }
}