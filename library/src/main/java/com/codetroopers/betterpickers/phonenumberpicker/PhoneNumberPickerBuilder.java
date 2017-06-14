package com.codetroopers.betterpickers.phonenumberpicker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.codetroopers.betterpickers.phonenumberpicker.PhoneNumberPickerDialogFragment.PhoneNumberPickerDialogHandler;

import java.math.BigDecimal;
import java.util.Vector;

/**
 * User: derek Date: 5/2/13 Time: 7:55 PM
 */
public class PhoneNumberPickerBuilder {

    private FragmentManager manager; // Required
    private Integer styleResId; // Required
    private Fragment targetFragment;
    private String labelText;
    private int mReference;
    private Vector<PhoneNumberPickerDialogHandler> mPhoneNumberPickerDialogHandlers = new Vector<PhoneNumberPickerDialogHandler>();
    private Integer currentNumberValue;

    /**
     * Attach a FragmentManager. This is required for creation of the Fragment.
     *
     * @param manager the FragmentManager that handles the transaction
     * @return the current Builder object
     */
    public PhoneNumberPickerBuilder setFragmentManager(FragmentManager manager) {
        this.manager = manager;
        return this;
    }

    /**
     * Attach a style resource ID for theming. This is required for creation of the Fragment. Two stock styles are
     * provided using R.style.BetterPickersDialogFragment and R.style.BetterPickersDialogFragment.Light
     *
     * @param styleResId the style resource ID to use for theming
     * @return the current Builder object
     */
    public PhoneNumberPickerBuilder setStyleResId(int styleResId) {
        this.styleResId = styleResId;
        return this;
    }

    /**
     * Attach a target Fragment. This is optional and useful if creating a Picker within a Fragment.
     *
     * @param targetFragment the Fragment to attach to
     * @return the current Builder object
     */
    public PhoneNumberPickerBuilder setTargetFragment(Fragment targetFragment) {
        this.targetFragment = targetFragment;
        return this;
    }

    /**
     * Attach a reference to this Picker instance. This is used to track multiple pickers, if the user wishes.
     *
     * @param reference a user-defined int intended for Picker tracking
     * @return the current Builder object
     */
    public PhoneNumberPickerBuilder setReference(int reference) {
        this.mReference = reference;
        return this;
    }

    /**
     * Set initial value to display
     */
    public PhoneNumberPickerBuilder setCurrentNumber(Integer number) {
        if (number != null) {
            this.currentNumberValue = number;
        }
        return this;
    }

    /**
     * Set initial value to display
     */
    public PhoneNumberPickerBuilder setCurrentNumber(Double number) {
        if (number != null) {
            BigDecimal[] numberInput = BigDecimal.valueOf(number).divideAndRemainder(BigDecimal.ONE);
            this.currentNumberValue = numberInput[0].intValue();
        }
        return this;
    }

    /**
     * Set the (optional) text shown as a label. This is useful if wanting to identify data with the number being
     * selected.
     *
     * @param labelText the String text to be shown
     * @return the current Builder object
     */
    public PhoneNumberPickerBuilder setLabelText(String labelText) {
        this.labelText = labelText;
        return this;
    }


    /**
     * Attach universal objects as additional handlers for notification when the Picker is set. For most use cases, this
     * method is not necessary as attachment to an Activity or Fragment is done automatically.  If, however, you would
     * like additional objects to subscribe to this Picker being set, attach Handlers here.
     *
     * @param handler an Object implementing the appropriate Picker Handler
     * @return the current Builder object
     */
    public PhoneNumberPickerBuilder addPhoneNumberPickerDialogHandler(PhoneNumberPickerDialogHandler handler) {
        this.mPhoneNumberPickerDialogHandlers.add(handler);
        return this;
    }

    /**
     * Remove objects previously added as handlers.
     *
     * @param handler the Object to remove
     * @return the current Builder object
     */
    public PhoneNumberPickerBuilder removePhoneNumberPickerDialogHandler(PhoneNumberPickerDialogHandler handler) {
        this.mPhoneNumberPickerDialogHandlers.remove(handler);
        return this;
    }

    /**
     * Instantiate and show the Picker
     */
    public void show(int roundWearableMargin, boolean vibrate) {
        if (manager == null || styleResId == null) {
            Log.e("PhonePickerBuilder", "setFragmentManager() and setStyleResId() must be called.");
            return;
        }
        final FragmentTransaction ft = manager.beginTransaction();
        final Fragment prev = manager.findFragmentByTag("number_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final PhoneNumberPickerDialogFragment fragment = PhoneNumberPickerDialogFragment
                .newInstance(mReference, styleResId, labelText, currentNumberValue, roundWearableMargin, vibrate);
        if (targetFragment != null) {
            fragment.setTargetFragment(targetFragment, 0);
        }
        fragment.setPhoneNumberPickerDialogHandlers(mPhoneNumberPickerDialogHandlers);
        fragment.show(ft, "number_dialog");
    }
}
