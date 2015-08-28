package com.codetroopers.betterpickers.t9picker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
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

import java.util.ArrayList;
import java.util.List;

/**
 * User: andreomlopes Date: 8/7/15
 */
public class T9Picker extends LinearLayout implements Button.OnClickListener,
        Button.OnLongClickListener {

    protected int mInputSize = 20;
    protected final Button mNumbers[] = new Button[10];
    protected String mInput[] = new String[mInputSize];
    protected int mInputPointer = -1;
    protected ImageButton mLeft, mRight;
    protected ImageButton mDelete;
    protected com.codetroopers.betterpickers.t9picker.T9View mEnteredText;
    protected final Context mContext;

    private TextView mLabel;
    private T9PickerErrorTextView mError;
    private int mSign;
    private String mLabelText = "";
    private Button mSetButton;
    private static final int CLICKED_DECIMAL = 10;

    public static final int SIGN_POSITIVE = 0;
    public static final int SIGN_NEGATIVE = 1;

    protected View mDivider;
    private ColorStateList mTextColor;
    private int mKeyBackgroundResId;
    private int mButtonBackgroundResId;
    private int mDividerColor;
    private int mCheckDrawableSrcResId;
    private int mShiftDrawableSrcResId;
    private int mDeleteDrawableSrcResId;
    private int mTheme = -1;

    private Integer mMinNumber = null;
    private Integer mMaxNumber = null;

    private List<String> mKeys;
    private String mLastKey;
    private int mCurrentKey;
    private long mClickedTimestamp;

    /**
     * Instantiates a NumberPicker object
     *
     * @param context the Context required for creation
     */
    public T9Picker(Context context) {
        this(context, null);
    }

    /**
     * Instantiates a NumberPicker object
     *
     * @param context the Context required for creation
     * @param attrs additional attributes that define custom colors, selectors, and backgrounds.
     */
    public T9Picker(Context context, AttributeSet attrs) {
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
        mShiftDrawableSrcResId = R.drawable.sym_keyboard_shift;

        mClickedTimestamp = System.currentTimeMillis();
        mKeys = new ArrayList<>();
        mKeys.add(" ");
        mKeys.add(".,?");mKeys.add("ABC");mKeys.add("DEF");
        mKeys.add("GHI");mKeys.add("JKL");mKeys.add("MNO");
        mKeys.add("PQRS");mKeys.add("TUV");mKeys.add("WXYZ");
    }

    protected int getLayoutId() {
        return R.layout.t9_picker_view;
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
            mLeft.setBackgroundResource(mKeyBackgroundResId);
            mLeft.setImageDrawable(getResources().getDrawable(mShiftDrawableSrcResId));
        }
        if (mRight != null) {
            mRight.setBackgroundResource(mKeyBackgroundResId);
            mRight.setImageDrawable(getResources().getDrawable(mCheckDrawableSrcResId));
        }
        if (mDelete != null) {
            mDelete.setBackgroundResource(mButtonBackgroundResId);
            mDelete.setImageDrawable(getResources().getDrawable(mDeleteDrawableSrcResId));
        }
        if (mEnteredText != null) {
            mEnteredText.setTheme(mTheme);
        }
        if (mLabel != null) {
            mLabel.setTextColor(mTextColor);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mDivider = findViewById(R.id.divider);
        mError = (T9PickerErrorTextView) findViewById(R.id.error);

        for (int i = 0; i < mInput.length; i++) {
            mInput[i] = "";
        }

        View v1 = findViewById(R.id.first);
        View v2 = findViewById(R.id.second);
        View v3 = findViewById(R.id.third);
        View v4 = findViewById(R.id.fourth);
        mEnteredText = (T9View) findViewById(R.id.t9_text);
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

        mLeft = (ImageButton) v4.findViewById(R.id.key_left);
        mNumbers[0] = (Button) v4.findViewById(R.id.key_middle);
        mRight = (ImageButton) v4.findViewById(R.id.key_right);
        setLeftRightEnabled();

        for (int i = 0; i < 10; i++) {
            mNumbers[i].setOnClickListener(this);
            mNumbers[i].setText(mKeys.get(i));
            mNumbers[i].setTag(R.id.numbers_key, mKeys.get(i));
        }
        updateText();

        Resources res = mContext.getResources();
        mLeft.setBackgroundResource(mKeyBackgroundResId);
        mLeft.setImageDrawable(getResources().getDrawable(mShiftDrawableSrcResId));
        mRight.setBackgroundResource(mKeyBackgroundResId);
        mRight.setImageDrawable(getResources().getDrawable(mCheckDrawableSrcResId));
        mLeft.setOnClickListener(this);
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
    public void setPlusMinusVisibility(int visiblity) {
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
        boolean enabled = mInputPointer != -1;
        if (mDelete != null) {
            mDelete.setEnabled(enabled);
        }
    }

    /**
     * Expose the NumberView in order to set errors
     *
     * @return the NumberView
     */
    public T9PickerErrorTextView getErrorView() {
        return mError;
    }

    @Override
    public void onClick(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        mError.hideImmediately();
        doOnClick(v);
        updateDeleteButton();
    }

    protected void doOnClick(View v) {
        String val = (String) v.getTag(R.id.numbers_key);
        if (val != null) {
            // A number was pressed
            addClickedText(val);
        } else if (v == mDelete) {
            if (mInputPointer != -1) {
                mInput[mInputPointer] = "";
                mInputPointer--;

                // Make delete update mLastKey
                addClickedText(null);
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
        v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
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
        updateText();
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

        if (labelText != null && labelText.isEmpty()) {
            mInputPointer = 0;
        }
        for (int i = 0; i < labelText.length(); i++) {
            mInput[i] = "" + labelText.charAt(i);
            mInputPointer++;
        }
    }

    private void showLabel() {
        mEnteredText.setText(mLabelText);
    }

    /**
     * Reset all inputs.
     */
    public void reset() {
        for (int i = 0; i < mInputSize; i++) {
            mInput[i] = "";
        }
        mInputPointer = -1;
        updateText();
    }

    // Update the text displayed in the picker:
    protected void updateText() {
        mEnteredText.setText(getEnteredText());
    }

    protected void setLeftRightEnabled() {
        mLeft.setEnabled(true);
        mRight.setEnabled(false);
    }

    private void addClickedText(String val) {
        long now = System.currentTimeMillis();
        boolean nextLetter = false;

        if (now - mClickedTimestamp > 1500 || mLastKey != val) {
            nextLetter = true;
            mCurrentKey = 0;
        }
        mLastKey = val;
        mClickedTimestamp = now;

        if (val != null) {
            String letter = "" + val.charAt(mCurrentKey);

            if (mInputPointer < mInputSize - 1) {
                if (nextLetter) {
                    mInputPointer++;
                }
                mInput[mInputPointer] = letter;
            }

            mCurrentKey++;
            if (mCurrentKey == val.length() || mCurrentKey == 0) {
                mCurrentKey = 0;
            }
        }
    }

    /**
     * Clicking on the bottom left button will toggle the sign.
     */
    private void onLeftClicked() {
        if (mSign == SIGN_POSITIVE) {
            mSign = SIGN_NEGATIVE;
        } else {
            mSign = SIGN_POSITIVE;
        }
    }

    /**
     * Clicking on the bottom right button will add a decimal point.
     */
    private void onRightClicked() {
        if (mInputPointer != -1) {
            // TODO
        }
    }

    /**
     * Returns the text inputted by the user
     *
     * @return a String representing the entered text
     */
    public String getEnteredText() {
        StringBuilder text = new StringBuilder();
        int inputLength = mInput.length;
        for (int i = 0; i < inputLength; i ++) {
            text.append(mInput[i]);
        }
        return text.toString();
    }

    private void updateLeftRightButtons() {
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
        String numberString = getEnteredText();
        String[] split = numberString.split("\\.");
        return Integer.parseInt(split[0]);
    }

    /**
     * Returns whether the number is positive or negative
     *
     * @return true or false whether the number is positive or negative
     */
    public boolean getIsNegative() {
        return mSign == SIGN_NEGATIVE;
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
            mInput = new String[mInputSize];
            mInputPointer = -1;
        }
        mSign = savedState.mSign;
        updateKeypad();
    }

    public void setNumber(Integer integerPart, Double decimalPart, Integer mCurrentSign) {
        if (mCurrentSign != null) {
            mSign = mCurrentSign;
        } else {
            mSign = SIGN_POSITIVE;
        }

        updateKeypad();
    }

    private static class SavedState extends BaseSavedState {

        int mInputPointer;
        String[] mInput;
        int mSign;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            mInputPointer = in.readInt();
            in.readStringArray(mInput);
            mSign = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mInputPointer);
            dest.writeStringArray(mInput);
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
