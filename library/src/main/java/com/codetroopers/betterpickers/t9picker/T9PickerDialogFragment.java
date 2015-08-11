package com.codetroopers.betterpickers.t9picker;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codetroopers.betterpickers.R;

import java.util.Vector;

/**
 * User: andreomlopes Date: 8/7/15
 */
public class T9PickerDialogFragment extends DialogFragment {

    private static final String REFERENCE_KEY = "T9PickerDialogFragment_ReferenceKey";
    private static final String THEME_RES_ID_KEY = "T9PickerDialogFragment_ThemeResIdKey";
    private static final String MIN_NUMBER_KEY = "T9PickerDialogFragment_MinNumberKey";
    private static final String MAX_NUMBER_KEY = "T9PickerDialogFragment_MaxNumberKey";
    private static final String PLUS_MINUS_VISIBILITY_KEY = "T9PickerDialogFragment_PlusMinusVisibilityKey";
    private static final String DECIMAL_VISIBILITY_KEY = "T9PickerDialogFragment_DecimalVisibilityKey";
    private static final String LABEL_TEXT_KEY = "T9PickerDialogFragment_LabelTextKey";
    private static final String CURRENT_NUMBER_KEY = "T9PickerDialogFragment_CurrentNumberKey";
    private static final String CURRENT_DECIMAL_KEY = "T9PickerDialogFragment_CurrentDecimalKey";
    private static final String CURRENT_SIGN_KEY = "T9PickerDialogFragment_CurrentSignKey";

    private Button mSet, mCancel;
    private com.codetroopers.betterpickers.t9picker.T9Picker mPicker;

    private View mDividerOne, mDividerTwo;
    private int mReference = -1;
    private int mTheme = -1;
    private int mDividerColor;
    private ColorStateList mTextColor;
    private String mLabelText = "";
    private int mButtonBackgroundResId;
    private int mDialogBackgroundResId;

    private Integer mMinNumber = null;
    private Integer mMaxNumber = null;
    private Integer mCurrentNumber = null;
    private Double mCurrentDecimal = null;
    private Integer mCurrentSign = null;
    private int mPlusMinusVisibility = View.VISIBLE;
    private int mDecimalVisibility = View.VISIBLE;
    private Vector<T9PickerDialogHandler> mT9PickerDialogHandlers = new Vector<T9PickerDialogHandler>();

    /**
     * Create an instance of the Picker (used internally)
     *
     * @param reference an (optional) user-defined reference, helpful when tracking multiple Pickers
     * @param themeResId the style resource ID for theming
     * @param minNumber (optional) the minimum possible number
     * @param maxNumber (optional) the maximum possible number
     * @param plusMinusVisibility (optional) View.VISIBLE, View.INVISIBLE, or View.GONE
     * @param decimalVisibility (optional) View.VISIBLE, View.INVISIBLE, or View.GONE
     * @param labelText (optional) text to add as a label
     * @return a Picker!
     */
    public static com.codetroopers.betterpickers.t9picker.T9PickerDialogFragment newInstance(int reference, int themeResId, Integer minNumber,
                                                         Integer maxNumber, Integer plusMinusVisibility,
                                                         Integer decimalVisibility, String labelText,
                                                         Integer currentNumberValue, Double currentDecimalValue,
                                                         Integer currentNumberSign) {
        final com.codetroopers.betterpickers.t9picker.T9PickerDialogFragment frag = new com.codetroopers.betterpickers.t9picker.T9PickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(REFERENCE_KEY, reference);
        args.putInt(THEME_RES_ID_KEY, themeResId);
        if (minNumber != null) {
            args.putInt(MIN_NUMBER_KEY, minNumber);
        }
        if (maxNumber != null) {
            args.putInt(MAX_NUMBER_KEY, maxNumber);
        }
        if (plusMinusVisibility != null) {
            args.putInt(PLUS_MINUS_VISIBILITY_KEY, plusMinusVisibility);
        }
        if (decimalVisibility != null) {
            args.putInt(DECIMAL_VISIBILITY_KEY, decimalVisibility);
        }
        if (labelText != null) {
            args.putString(LABEL_TEXT_KEY, labelText);
        }
        if (currentNumberValue != null) {
            args.putInt(CURRENT_NUMBER_KEY, currentNumberValue);
        }
        if (currentDecimalValue != null) {
            args.putDouble(CURRENT_DECIMAL_KEY, currentDecimalValue);
        }
        if (currentNumberSign != null) {
            args.putInt(CURRENT_SIGN_KEY, currentNumberSign);
        }
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(REFERENCE_KEY)) {
            mReference = args.getInt(REFERENCE_KEY);
        }
        if (args != null && args.containsKey(THEME_RES_ID_KEY)) {
            mTheme = args.getInt(THEME_RES_ID_KEY);
        }
        if (args != null && args.containsKey(PLUS_MINUS_VISIBILITY_KEY)) {
            mPlusMinusVisibility = args.getInt(PLUS_MINUS_VISIBILITY_KEY);
        }
        if (args != null && args.containsKey(DECIMAL_VISIBILITY_KEY)) {
            mDecimalVisibility = args.getInt(DECIMAL_VISIBILITY_KEY);
        }
        if (args != null && args.containsKey(MIN_NUMBER_KEY)) {
            mMinNumber = args.getInt(MIN_NUMBER_KEY);
        }
        if (args != null && args.containsKey(MAX_NUMBER_KEY)) {
            mMaxNumber = args.getInt(MAX_NUMBER_KEY);
        }
        if (args != null && args.containsKey(LABEL_TEXT_KEY)) {
            mLabelText = args.getString(LABEL_TEXT_KEY);
        }
        if (args != null && args.containsKey(CURRENT_NUMBER_KEY)) {
            mCurrentNumber = args.getInt(CURRENT_NUMBER_KEY);
        }
        if (args != null && args.containsKey(CURRENT_DECIMAL_KEY)) {
            mCurrentDecimal = args.getDouble(CURRENT_DECIMAL_KEY);
        }
        if (args != null && args.containsKey(CURRENT_SIGN_KEY)) {
            mCurrentSign = args.getInt(CURRENT_SIGN_KEY);
        }

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        // Init defaults
        mTextColor = getResources().getColorStateList(R.color.dialog_text_color_holo_dark);
        mButtonBackgroundResId = R.drawable.button_background_dark;
        mDividerColor = getResources().getColor(R.color.default_divider_color_dark);
        mDialogBackgroundResId = R.drawable.dialog_full_holo_dark;

        if (mTheme != -1) {
            TypedArray a = getActivity().getApplicationContext()
                    .obtainStyledAttributes(mTheme, R.styleable.BetterPickersDialogFragment);

            mTextColor = a.getColorStateList(R.styleable.BetterPickersDialogFragment_bpTextColor);
            mButtonBackgroundResId = a.getResourceId(R.styleable.BetterPickersDialogFragment_bpButtonBackground,
                    mButtonBackgroundResId);
            mDividerColor = a.getColor(R.styleable.BetterPickersDialogFragment_bpDividerColor, mDividerColor);
            mDialogBackgroundResId = a
                    .getResourceId(R.styleable.BetterPickersDialogFragment_bpDialogBackground, mDialogBackgroundResId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.t9_picker_dialog, null);
        mSet = (Button) v.findViewById(R.id.set_button);
        mCancel = (Button) v.findViewById(R.id.cancel_button);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mPicker = (T9Picker) v.findViewById(R.id.t9_picker);
        mPicker.setSetButton(mSet);
        mSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mPicker.getEnteredText();
                if (text.isEmpty()) {
                    String errorText = "Please enter a Text";
                    mPicker.getErrorView().setText(errorText);
                    mPicker.getErrorView().show();
                    return;
                }
                for (T9PickerDialogHandler handler : mT9PickerDialogHandlers) {
                    handler.onDialogTextSet(mReference, text);
                }
                final Activity activity = getActivity();
                final Fragment fragment = getTargetFragment();
                if (activity instanceof T9PickerDialogHandler) {
                    final T9PickerDialogHandler act =
                            (T9PickerDialogHandler) activity;
                    act.onDialogTextSet(mReference, text);
                } else if (fragment instanceof T9PickerDialogHandler) {
                    final T9PickerDialogHandler frag = (T9PickerDialogHandler) fragment;
                    frag.onDialogTextSet(mReference, text);
                }
                dismiss();
            }
        });

        mDividerOne = v.findViewById(R.id.divider_1);
        mDividerTwo = v.findViewById(R.id.divider_2);
        mDividerOne.setBackgroundColor(mDividerColor);
        mDividerTwo.setBackgroundColor(mDividerColor);
        mSet.setTextColor(mTextColor);
        mSet.setBackgroundResource(mButtonBackgroundResId);
        mCancel.setTextColor(mTextColor);
        mCancel.setBackgroundResource(mButtonBackgroundResId);
        mPicker.setTheme(mTheme);
        getDialog().getWindow().setBackgroundDrawableResource(mDialogBackgroundResId);

        mPicker.setDecimalVisibility(mDecimalVisibility);
        mPicker.setPlusMinusVisibility(mPlusMinusVisibility);
        mPicker.setLabelText(mLabelText);
        if (mMinNumber != null) {
            mPicker.setMin(mMinNumber);
        }
        if (mMaxNumber != null) {
            mPicker.setMax(mMaxNumber);
        }
        mPicker.setNumber(mCurrentNumber, mCurrentDecimal, mCurrentSign);
        return v;
    }

    /**
     * This interface allows objects to register for the Picker's set action.
     */
    public interface T9PickerDialogHandler {

        void onDialogTextSet(int reference, String text);
    }

    /**
     * Attach a Vector of handlers to be notified in addition to the Fragment's Activity and target Fragment.
     *
     * @param handlers a Vector of handlers
     */
    public void setT9PickerDialogHandlers(Vector<T9PickerDialogHandler> handlers) {
        mT9PickerDialogHandlers = handlers;
    }
}