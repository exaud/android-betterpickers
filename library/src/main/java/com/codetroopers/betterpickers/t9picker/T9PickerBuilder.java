package com.codetroopers.betterpickers.t9picker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextWatcher;
import android.util.Log;

import java.util.Vector;

/**
 * User: andreomlopes Date: 8/7/15
 */
public class T9PickerBuilder {

    private FragmentManager manager; // Required
    private Integer styleResId; // Required
    private Fragment targetFragment;
    private String labelText;
    private int mReference;
    private Vector<T9PickerDialogFragment.T9PickerDialogHandler> mT9PickerDialogHandlers = new Vector<T9PickerDialogFragment.T9PickerDialogHandler>();
    private TextWatcher mTextWatcher;

    /**
     * Attach a FragmentManager. This is required for creation of the Fragment.
     *
     * @param manager the FragmentManager that handles the transaction
     * @return the current Builder object
     */
    public T9PickerBuilder setFragmentManager(FragmentManager manager) {
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
    public T9PickerBuilder setStyleResId(int styleResId) {
        this.styleResId = styleResId;
        return this;
    }

    /**
     * Attach a target Fragment. This is optional and useful if creating a Picker within a Fragment.
     *
     * @param targetFragment the Fragment to attach to
     * @return the current Builder object
     */
    public T9PickerBuilder setTargetFragment(Fragment targetFragment) {
        this.targetFragment = targetFragment;
        return this;
    }

    /**
     * Attach a reference to this Picker instance. This is used to track multiple pickers, if the user wishes.
     *
     * @param reference a user-defined int intended for Picker tracking
     * @return the current Builder object
     */
    public T9PickerBuilder setReference(int reference) {
        this.mReference = reference;
        return this;
    }

    /**
     * Set the (optional) text shown as a label. This is useful if wanting to identify data with the number being
     * selected.
     *
     * @param labelText the String text to be shown
     * @return the current Builder object
     */
    public com.codetroopers.betterpickers.t9picker.T9PickerBuilder setLabelText(String labelText) {
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
    public com.codetroopers.betterpickers.t9picker.T9PickerBuilder addT9PickerDialogHandler(T9PickerDialogFragment.T9PickerDialogHandler handler) {
        this.mT9PickerDialogHandlers.add(handler);
        return this;
    }

    /**
     * Remove objects previously added as handlers.
     *
     * @param handler the Object to remove
     * @return the current Builder object
     */
    public com.codetroopers.betterpickers.t9picker.T9PickerBuilder removeT9PickerDialogHandler(T9PickerDialogFragment.T9PickerDialogHandler handler) {
        this.mT9PickerDialogHandlers.remove(handler);
        return this;
    }

    public com.codetroopers.betterpickers.t9picker.T9PickerBuilder addTextWatcher(TextWatcher textWatcher) {
        this.mTextWatcher = textWatcher;
        return this;
    }

    /**
     * Instantiate and show the Picker
     */
    public void show(int roundWearableMargin) {
        if (manager == null || styleResId == null) {
            Log.e("T9PickerBuilder", "setFragmentManager() and setStyleResId() must be called.");
            return;
        }
        final FragmentTransaction ft = manager.beginTransaction();
        final Fragment prev = manager.findFragmentByTag("number_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final T9PickerDialogFragment fragment = T9PickerDialogFragment
                .newInstance(mReference, styleResId, labelText, mTextWatcher, roundWearableMargin);
        if (targetFragment != null) {
            fragment.setTargetFragment(targetFragment, 0);
        }
        fragment.setT9PickerDialogHandlers(mT9PickerDialogHandlers);
        fragment.show(ft, "number_dialog");
    }
}
