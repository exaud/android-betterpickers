package com.codetroopers.betterpickers.phonenumberpicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codetroopers.betterpickers.R;

public class PhoneNumberView extends LinearLayout {

    private TextView mNumber;
    private Typeface mOriginalTextTypeface;
    private ColorStateList mTextColor;

    /**
     * Instantiate a PhoneNumberView
     *
     * @param context the Context in which to inflate the View
     */
    public PhoneNumberView(Context context) {
        this(context, null);
    }

    /**
     * Instantiate a PhoneNumberView
     *
     * @param context the Context in which to inflate the View
     * @param attrs attributes that define the title color
     */
    public PhoneNumberView(Context context, AttributeSet attrs) {
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
            mOriginalTextTypeface = mNumber.getTypeface();
        }

        restyleViews();
    }

    /**
     * Set the number shown
     *
     * @param numbersDigit the non-decimal digits
     */
    public void setNumber(String numbersDigit) {
        if (mNumber != null) {
            if (numbersDigit.equals("")) {
                // Set to -
                mNumber.setText("-");
                mNumber.setTypeface(mOriginalTextTypeface);
                mNumber.setEnabled(false);
            } else {
                // Set to thin
                mNumber.setText(numbersDigit);
                mNumber.setTypeface(mOriginalTextTypeface);
                mNumber.setEnabled(true);
            }
        }
    }
}