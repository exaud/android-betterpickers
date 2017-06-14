package com.codetroopers.betterpickers.sample.activity.phonenumberpicker;

/**
 * Created by joao on 14/06/2017.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.phonenumberpicker.PhoneNumberPickerBuilder;
import com.codetroopers.betterpickers.phonenumberpicker.PhoneNumberPickerDialogFragment;
import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;

public class SamplePhoneNumberDefault extends BaseSampleActivity
        implements PhoneNumberPickerDialogFragment.PhoneNumberPickerDialogHandler {

    private TextView mText;
    private Button mButton;

    private int roundWearableMargin = 0;
    private boolean vibrate = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);

        mText = (TextView) findViewById(R.id.text);
        mButton = (Button) findViewById(R.id.button);

        mText.setText("--");
        mButton.setText("Set Phone Number");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneNumberPickerBuilder npb = new PhoneNumberPickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                npb.show(roundWearableMargin, vibrate);
            }
        });
    }

    @Override
    public void onDialogNumberSet(int reference, String number) {
        mText.setText(number);
    }
}
