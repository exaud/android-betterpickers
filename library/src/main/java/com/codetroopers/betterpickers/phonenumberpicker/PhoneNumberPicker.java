package com.codetroopers.betterpickers.phonenumberpicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codetroopers.betterpickers.R;

import java.math.BigDecimal;

public class PhoneNumberPicker extends LinearLayout implements Button.OnClickListener,
        Button.OnLongClickListener {

    protected int mInputSize = 20;
    protected final Button mNumbers[] = new Button[10];
    protected int mInput[] = new int[mInputSize];
    protected int mInputPointer = -1;
    protected ImageButton mRight;
    protected ImageButton mDelete;
    protected PhoneNumberView mEnteredNumber;
    protected final Context mContext;

    private TextView mLabel;
    private PhoneNumberPickerErrorTextView mError;
    private int mSign;
    private String mLabelText = "";
    private Button mSetButton, mLeft;
    private static final int CLICKED_DECIMAL = 10;

    public static final int SIGN_POSITIVE = 0;
    public static final int SIGN_NEGATIVE = 1;

    protected View mDivider;
    private ColorStateList mTextColor;
    private int mKeyBackgroundResId;
    private int mButtonBackgroundResId;
    private int mDividerColor;
    private int mCheckDrawableSrcResId;
    private int mDeleteDrawableSrcResId;
    private int mTheme = -1;

    private Integer mMinNumber = null;
    private Integer mMaxNumber = null;

    private OnClickListener mSetClickListener;
    private boolean mVibrate = false;
    private boolean mLeftClicked = false;

    /**
     * Instantiates a PhoneNumberPicker object
     *
     * @param context the Context required for creation
     */
    public PhoneNumberPicker(Context context) {
        this(context, null);
    }

    /**
     * Instantiates a PhoneNumberPicker object
     *
     * @param context the Context required for creation
     * @param attrs additional attributes that define custom colors, selectors, and backgrounds.
     */
    public PhoneNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(getLayoutId(), this);

        // Init defaults
        mTextColor = getResources().getColorStateList(R.color.dialog_text_color_holo_dark);
        mKeyBackgroundResId = R.drawable.key_background_dark;
        mButtonBackgroundResId = R.drawable.button_background_dark;
        mDeleteDrawableSrcResId = R.drawable.ic_backspace_dark;
        mDividerColor = getResources().getColor(R.color.default_divider_color_dark);
        mCheckDrawableSrcResId = R.drawable.ic_check_dark;
    }

    protected int getLayoutId() {
        return R.layout.phonenumber_picker_view;
    }

    /**
     * Change the theme of the Picker
     *
     * @param themeResId the resource ID of the new style
     */
    public void setTheme(int themeResId) {
        mTheme = themeResId;
        if (mTheme != -1) {
            TypedArray a = getContext().obtainStyledAttributes(themeResId, R.styleable.BetterPickersDialogFragment);

            mTextColor = a.getColorStateList(R.styleable.BetterPickersDialogFragment_bpTextColor);
            mKeyBackgroundResId = a.getResourceId(R.styleable.BetterPickersDialogFragment_bpKeyBackground,
                    mKeyBackgroundResId);
            mButtonBackgroundResId = a.getResourceId(R.styleable.BetterPickersDialogFragment_bpButtonBackground,
                    mButtonBackgroundResId);
            mCheckDrawableSrcResId = a.getResourceId(R.styleable.BetterPickersDialogFragment_bpCheckIcon,
                    mCheckDrawableSrcResId);
            mDividerColor = a.getColor(R.styleable.BetterPickersDialogFragment_bpDividerColor, mDividerColor);
            mDeleteDrawableSrcResId = a.getResourceId(R.styleable.BetterPickersDialogFragment_bpDeleteIcon,
                    mDeleteDrawableSrcResId);
        }

        restyleViews();
    }

    private void restyleViews() {
        for (Button number : mNumbers) {
            if (number != null) {
                number.setTextColor(mTextColor);
                number.setBackgroundResource(mKeyBackgroundResId);
            }
        }
        if (mDivider != null) {
            mDivider.setBackgroundColor(mDividerColor);
        }
        if (mLeft != null) {
            mLeft.setTextColor(mTextColor);
            mLeft.setBackgroundResource(mKeyBackgroundResId);
        }
        if (mRight != null) {
            mRight.setBackgroundResource(mKeyBackgroundResId);
            mRight.setImageDrawable(getResources().getDrawable(mCheckDrawableSrcResId));
        }
        if (mDelete != null) {
            mDelete.setBackgroundResource(mButtonBackgroundResId);
            mDelete.setImageDrawable(getResources().getDrawable(mDeleteDrawableSrcResId));
        }
        if (mEnteredNumber != null) {
            mEnteredNumber.setTheme(mTheme);
        }
        if (mLabel != null) {
            mLabel.setTextColor(mTextColor);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mDivider = findViewById(R.id.divider);
        mError = (PhoneNumberPickerErrorTextView) findViewById(R.id.error);

        for (int i = 0; i < mInput.length; i++) {
            mInput[i] = -1;
        }

        View v1 = findViewById(R.id.first);
        View v2 = findViewById(R.id.second);
        View v3 = findViewById(R.id.third);
        View v4 = findViewById(R.id.fourth);
        mEnteredNumber = (PhoneNumberView) findViewById(R.id.number_text);
        mDelete = (ImageButton) findViewById(R.id.delete);
        mDelete.setOnClickListener(this);
        mDelete.setOnLongClickListener(this);

        mNumbers[1] = (Button) v1.findViewById(R.id.key_left);
        mNumbers[2] = (Button) v1.findViewById(R.id.key_middle);
        mNumbers[3] = (Button) v1.findViewById(R.id.key_right);

        mNumbers[4] = (Button) v2.findViewById(R.id.key_left);
        mNumbers[5] = (Button) v2.findViewById(R.id.key_middle);
        mNumbers[6] = (Button) v2.findViewById(R.id.key_right);

        mNumbers[7] = (Button) v3.findViewById(R.id.key_left);
        mNumbers[8] = (Button) v3.findViewById(R.id.key_middle);
        mNumbers[9] = (Button) v3.findViewById(R.id.key_right);

        mLeft = (Button) v4.findViewById(R.id.key_left);
        mNumbers[0] = (Button) v4.findViewById(R.id.key_middle);
        mRight = (ImageButton) v4.findViewById(R.id.key_right);
        setLeftRightEnabled();

        for (int i = 0; i < 10; i++) {
            mNumbers[i].setOnClickListener(this);
            mNumbers[i].setText(String.format("%d", i));
            mNumbers[i].setTag(R.id.numbers_key, new Integer(i));
        }
        updateNumber();

        mLeft.setBackgroundResource(mKeyBackgroundResId);
        mLeft.setText(getResources().getString(R.string.phone_number_picker_plus));
        mLeft.setOnClickListener(this);
        mRight.setBackgroundResource(mKeyBackgroundResId);
        mRight.setImageDrawable(getResources().getDrawable(mCheckDrawableSrcResId));
        mRight.setOnClickListener(this);
        mLabel = (TextView) findViewById(R.id.label);
        mSign = SIGN_POSITIVE;

        // Set the correct label state
        showLabel();

        restyleViews();
        updateKeypad();
    }

    /**
     * Using View.GONE, View.VISIBILE, or View.INVISIBLE, set the visibility of the plus/minus indicator
     *
     * @param visiblity an int using Android's View.* convention
     */
    public void setPlusVisibility(int visiblity) {
        if (mLeft != null) {
            mLeft.setVisibility(visiblity);
        }
    }

    /**
     * Using View.GONE, View.VISIBILE, or View.INVISIBLE, set the visibility of the decimal indicator
     *
     * @param visiblity an int using Android's View.* convention
     */
    public void setDecimalVisibility(int visiblity) {
        if (mRight != null) {
            mRight.setVisibility(visiblity);
        }
    }

    /**
     * Set a minimum required number
     *
     * @param min the minimum required number
     */
    public void setMin(int min) {
        mMinNumber = min;
    }

    /**
     * Set a maximum required number
     *
     * @param max the maximum required number
     */
    public void setMax(int max) {
        mMaxNumber = max;
    }

    /**
     * Update the delete button to determine whether it is able to be clicked.
     */
    public void updateDeleteButton() {
        boolean enabled = mInputPointer != -1 || mLeftClicked;
        if (mDelete != null) {
            mDelete.setEnabled(enabled);
        }
    }

    /**
     * Expose the PhoneNumberView in order to set errors
     *
     * @return the PhoneNumberView
     */
    public PhoneNumberPickerErrorTextView getErrorView() {
        return mError;
    }

    @Override
    public void onClick(View v) {
        if (mVibrate) {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        }
        mError.hideImmediately();
        doOnClick(v);
        updateDeleteButton();
    }

    protected void doOnClick(View v) {
        Integer val = (Integer) v.getTag(R.id.numbers_key);
        if (val != null) {
            // A number was pressed
            addClickedNumber(val);
        } else if (v == mDelete) {
            if (mInputPointer >= 0) {
                for (int i = 0; i < mInputPointer; i++) {
                    mInput[i] = mInput[i + 1];
                }
                mInput[mInputPointer] = -1;
                mInputPointer--;
            } else if (mInputPointer == -1 && mLeftClicked) {
                mLeftClicked = false;
            }
        } else if (v == mLeft) {
            onLeftClicked();
        } else if (v == mRight) {
            onRightClicked();
        }
        updateKeypad();
    }

    @Override
    public boolean onLongClick(View v) {
        if (mVibrate) {
            v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        }
        mError.hideImmediately();
        if (v == mDelete) {
            mDelete.setPressed(false);
            reset();
            updateKeypad();
            return true;
        }
        return false;
    }

    private void updateKeypad() {
        // Update state of keypad
        // Update the number
        updateLeftRightButtons();
        updateNumber();
        // enable/disable the "set" key
        enableSetButton();
        // Update the backspace button
        updateDeleteButton();
    }

    /**
     * Set the text displayed in the small label
     *
     * @param labelText the String to set as the label
     */
    public void setLabelText(String labelText) {
        mLabelText = labelText;
        showLabel();
    }

    private void showLabel() {
        if (mLabel != null) {
            mLabel.setText(mLabelText);
        }
    }

    /**
     * Reset all inputs.
     */
    public void reset() {
        for (int i = 0; i < mInputSize; i++) {
            mInput[i] = -1;
        }
        mInputPointer = -1;
        mLeftClicked = false;
        updateNumber();
    }

    // Update the number displayed in the picker:
    protected void updateNumber() {
        String numberString = getEnteredNumberString();
        numberString = numberString.replaceAll("\\-", "");
        String[] split = numberString.split("\\.");
        if (split.length >= 2) {
            if (split[0].equals("")) {
                mEnteredNumber.setNumber("0", split[1], containsDecimal(),
                        mSign == SIGN_NEGATIVE);
            } else {
                mEnteredNumber.setNumber(split[0], split[1], containsDecimal(),
                        mSign == SIGN_NEGATIVE);
            }
        } else if (split.length == 1) {
            mEnteredNumber.setNumber(split[0], "", containsDecimal(),
                    mSign == SIGN_NEGATIVE);
        } else if (numberString.equals(".")) {
            mEnteredNumber.setNumber("0", "", true, mSign == SIGN_NEGATIVE);
        }
    }

    protected void setLeftRightEnabled() {
        mLeft.setEnabled(true);
        mRight.setEnabled(false);
    }

    private void addClickedNumber(int val) {
        if (mInputPointer < mInputSize - 1) {
            // For 0 we need to check if we have a value of zero or not
            if (mInput[0] == 0 && mInput[1] == -1 && !containsDecimal() && val != CLICKED_DECIMAL) {
                mInput[0] = val;
            } else {
                for (int i = mInputPointer; i >= 0; i--) {
                    mInput[i + 1] = mInput[i];
                }
                mInputPointer++;
                mInput[0] = val;
            }
        }
    }

    /**
     * Clicking on the bottom left button will toggle the sign.
     */
    private void onLeftClicked() {
        mLeftClicked = true;
    }

    /**
     * Clicking on the bottom right button will accept the number.
     */
    private void onRightClicked() {
        if (mInputPointer != -1) {
            if (mSetClickListener != null) {
                mSetClickListener.onClick(null);
            }
        }
    }

    private boolean containsDecimal() {
        boolean containsDecimal = false;
        for (int i : mInput) {
            if (i == 10) {
                containsDecimal = true;
            }
        }
        return containsDecimal;
    }

    /**
     * Checks if the user allowed to click on the right button.
     *
     * @return true or false if the user is able to add a decimal or not
     */
    private boolean canAddDecimal() {
        return !containsDecimal();
    }

    public String getEnteredNumberString() {
        String value = "";
        if (mLeftClicked) {
            value += "+";
        }
        for (int i = mInputPointer; i >= 0; i--) {
            if (mInput[i] == -1) {
                // Don't add
            } else {
                value += mInput[i];
            }
        }
        return value;
    }

    /**
     * Returns the number inputted by the user
     *
     * @return a double representing the entered number
     */
    public double getEnteredNumber() {
        String value = "0";
        for (int i = mInputPointer; i >= 0; i--) {
            if (mInput[i] == -1) {
                break;
            } else if (mInput[i] == CLICKED_DECIMAL) {
                value += ".";
            } else {
                value += mInput[i];
            }
        }
        if (mSign == SIGN_NEGATIVE) {
            value = "-" + value;
        }
        return Double.parseDouble(value);
    }

    private void updateLeftRightButtons() {
        mLeft.setEnabled(mInputPointer == -1);
        mRight.setEnabled(mInputPointer != -1);
    }

    /**
     * Enable/disable the "Set" button
     */
    private void enableSetButton() {
        if (mSetButton == null) {
            return;
        }

        // Nothing entered - disable
        if (mInputPointer == -1) {
            mSetButton.setEnabled(false);
            return;
        }

        // If the user entered 1 digits or more
        mSetButton.setEnabled(mInputPointer >= 0);
    }

    /**
     * Expose the set button to allow communication with the parent Fragment.
     *
     * @param b the parent Fragment's "Set" button
     */
    public void setSetButton(Button b) {
        mSetButton = b;
        enableSetButton();
    }

    /**
     * Returns the number as currently inputted by the user
     *
     * @return an int representation of the number with no decimal
     */
    public int getNumber() {
        String numberString = Double.toString(getEnteredNumber());
        String[] split = numberString.split("\\.");
        return Integer.parseInt(split[0]);
    }

    /**
     * Returns the decimal following the number
     *
     * @return a double representation of the decimal value
     */
    public double getDecimal() {
        double decimal = BigDecimal.valueOf(getEnteredNumber()).divideAndRemainder(BigDecimal.ONE)[1].doubleValue();
        return decimal;
    }

    /**
     * Returns whether the number is positive or negative
     *
     * @return true or false whether the number is positive or negative
     */
    public boolean getIsNegative() {
        return mSign == SIGN_NEGATIVE;
    }

    public void setVibrate(boolean vibrate) {
        this.mVibrate = vibrate;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        final Parcelable parcel = super.onSaveInstanceState();
        final SavedState state = new SavedState(parcel);
        state.mInput = mInput;
        state.mSign = mSign;
        state.mInputPointer = mInputPointer;
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        final SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        mInputPointer = savedState.mInputPointer;
        mInput = savedState.mInput;
        if (mInput == null) {
            mInput = new int[mInputSize];
            mInputPointer = -1;
        }
        mSign = savedState.mSign;
        updateKeypad();
    }

    public void setNumber(Integer integerPart, Double decimalPart, Integer mCurrentSign) {
        if (integerPart != null) {
            if (integerPart.equals(Integer.valueOf(0))) {
                mInputPointer++;
                mInput[mInputPointer] = 0;
            } else {
                readAndRightDigits(integerPart);
            }
        }
        updateKeypad();
    }

    private void readAndRightDigits(Integer digitsToRead) {
        while (digitsToRead > 0) {
            int d = digitsToRead / 10;
            int k = digitsToRead - (d * 10);
            digitsToRead = d;
            mInputPointer++;
            mInput[mInputPointer] = k;
        }
    }

    public void setSetClickListener(OnClickListener setClickListener) {
        mSetClickListener = setClickListener;
    }

    private static class SavedState extends BaseSavedState {

        int mInputPointer;
        int[] mInput;
        int mSign;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            mInputPointer = in.readInt();
            in.readIntArray(mInput);
            mSign = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mInputPointer);
            dest.writeIntArray(mInput);
            dest.writeInt(mSign);
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
