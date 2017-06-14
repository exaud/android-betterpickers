package com.codetroopers.betterpickers.phonenumberpicker;

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
 * Dialog to set alarm time.
 */
public class PhoneNumberPickerDialogFragment extends DialogFragment {

    private static final String REFERENCE_KEY = "PhoneNumberPickerDialogFragment_ReferenceKey";
    private static final String THEME_RES_ID_KEY = "PhoneNumberPickerDialogFragment_ThemeResIdKey";
    private static final String PLUS_VISIBILITY_KEY = "PhoneNumberPickerDialogFragment_PlusVisibilityKey";
    private static final String LABEL_TEXT_KEY = "PhoneNumberPickerDialogFragment_LabelTextKey";
    private static final String CURRENT_NUMBER_KEY = "PhoneNumberPickerDialogFragment_CurrentNumberKey";
    private static final String ROUND_WEARABLE_MARGIN_KEY = "T9PickerDialogFragment_RoundWearableMarginKey";
    private static final String VIBRATE_KEY = "T9PickerDialogFragment_VibrateKey";

    private Button mSet, mCancel;
    private PhoneNumberPicker mPicker;

    private View mDividerOne, mDividerTwo;
    private int mReference = -1;
    private int mTheme = -1;
    private int mDividerColor;
    private ColorStateList mTextColor;
    private String mLabelText = "";
    private int mButtonBackgroundResId;
    private int mDialogBackgroundResId;
    private int roundWearableMargin;

    private Integer mCurrentNumber = null;
    private int mPlusVisibility = View.VISIBLE;
    private Vector<PhoneNumberPickerDialogHandler> mPhoneNumberPickerDialogHandlers = new Vector<PhoneNumberPickerDialogHandler>();

    private boolean mVibrate;

    /**
     * Create an instance of the Picker (used internally)
     *
     * @param reference an (optional) user-defined reference, helpful when tracking multiple Pickers
     * @param themeResId the style resource ID for theming
     * @param labelText (optional) text to add as a label
     * @return a Picker!
     */
    public static PhoneNumberPickerDialogFragment newInstance(int reference, int themeResId, String labelText,
                                                              Integer currentNumberValue, int roundWearableMargin, boolean vibrate) {
        final PhoneNumberPickerDialogFragment frag = new PhoneNumberPickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(REFERENCE_KEY, reference);
        args.putInt(THEME_RES_ID_KEY, themeResId);
        if (labelText != null) {
            args.putString(LABEL_TEXT_KEY, labelText);
        }
        if (currentNumberValue != null) {
            args.putInt(CURRENT_NUMBER_KEY, currentNumberValue);
        }
        if (vibrate) {
            args.putBoolean(VIBRATE_KEY, vibrate);
        }
        args.putInt(ROUND_WEARABLE_MARGIN_KEY, roundWearableMargin);

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
        if (args != null && args.containsKey(PLUS_VISIBILITY_KEY)) {
            mPlusVisibility = args.getInt(PLUS_VISIBILITY_KEY);
        }
        if (args != null && args.containsKey(LABEL_TEXT_KEY)) {
            mLabelText = args.getString(LABEL_TEXT_KEY);
        }
        if (args != null && args.containsKey(CURRENT_NUMBER_KEY)) {
            mCurrentNumber = args.getInt(CURRENT_NUMBER_KEY);
        }
        if (args != null && args.containsKey(ROUND_WEARABLE_MARGIN_KEY)) {
            roundWearableMargin = args.getInt(ROUND_WEARABLE_MARGIN_KEY);
        }
        if (args != null && args.containsKey(VIBRATE_KEY)) {
            mVibrate = args.getBoolean(VIBRATE_KEY);
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

        View v = inflater.inflate(R.layout.phonenumber_picker_dialog, null);
        v.setPadding(roundWearableMargin, roundWearableMargin, roundWearableMargin, roundWearableMargin);

        mSet = (Button) v.findViewById(R.id.set_button);
        mCancel = (Button) v.findViewById(R.id.cancel_button);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mPicker = (PhoneNumberPicker) v.findViewById(R.id.phonenumber_picker);
        View.OnClickListener setClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = mPicker.getEnteredNumberString();
                for (PhoneNumberPickerDialogFragment.PhoneNumberPickerDialogHandler handler : mPhoneNumberPickerDialogHandlers) {
                    handler.onDialogNumberSet(mReference, number);
                }
                final Activity activity = getActivity();
                final Fragment fragment = getTargetFragment();
                if (activity instanceof PhoneNumberPickerDialogFragment.PhoneNumberPickerDialogHandler) {
                    final PhoneNumberPickerDialogFragment.PhoneNumberPickerDialogHandler act =
                            (PhoneNumberPickerDialogFragment.PhoneNumberPickerDialogHandler) activity;
                    act.onDialogNumberSet(mReference, number);
                } else if (fragment instanceof PhoneNumberPickerDialogFragment.PhoneNumberPickerDialogHandler) {
                    final PhoneNumberPickerDialogFragment.PhoneNumberPickerDialogHandler frag = (PhoneNumberPickerDialogFragment.PhoneNumberPickerDialogHandler) fragment;
                    frag.onDialogNumberSet(mReference, number);
                }
                dismiss();
            }
        };
        mSet.setOnClickListener(setClickListener);
        mPicker.setSetButton(mSet);

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

        mPicker.setPlusVisibility(mPlusVisibility);
        mPicker.setSetClickListener(setClickListener);
        mPicker.setLabelText(mLabelText);
        mPicker.setNumber(mCurrentNumber);

        if (mVibrate) {
            mPicker.setVibrate(true);
        }
        return v;
    }

    /**
     * This interface allows objects to register for the Picker's set action.
     */
    public interface PhoneNumberPickerDialogHandler {

        void onDialogNumberSet(int reference, String number);
    }

    /**
     * Attach a Vector of handlers to be notified in addition to the Fragment's Activity and target Fragment.
     *
     * @param handlers a Vector of handlers
     */
    public void setPhoneNumberPickerDialogHandlers(Vector<PhoneNumberPickerDialogHandler> handlers) {
        mPhoneNumberPickerDialogHandlers = handlers;
    }
}