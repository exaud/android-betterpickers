package com.codetroopers.betterpickers.phonenumberpicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.codetroopers.betterpickers.R;
import com.codetroopers.betterpickers.widget.ZeroTopPaddingTextView;

public class PhoneNumberView extends LinearLayout {

    private ZeroTopPaddingTextView mNumber;
    private final Typeface mAndroidClockMonoThin;

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
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mNumber = (ZeroTopPaddingTextView) findViewById(R.id.number);
        // Set the lowest time unit with thin font
        if (mNumber != null) {
            mNumber.setTypeface(mAndroidClockMonoThin);
            mNumber.updatePadding();
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
                mNumber.setTypeface(mAndroidClockMonoThin);
                mNumber.setEnabled(false);
                mNumber.updatePadding();
                mNumber.setVisibility(View.VISIBLE);
            } else {
                // Set to thin
                mNumber.setText(numbersDigit);
                mNumber.setTypeface(mAndroidClockMonoThin);
                mNumber.setEnabled(true);
                mNumber.updatePadding();
                mNumber.setVisibility(View.VISIBLE);
            }
        }
    }
}