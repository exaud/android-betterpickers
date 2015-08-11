package com.codetroopers.betterpickers.sample.activity.t9picker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;
import com.codetroopers.betterpickers.t9picker.T9PickerBuilder;
import com.codetroopers.betterpickers.t9picker.T9PickerDialogFragment;

/**
 * User: andreomlopes Date: 8/11/15
 */
public class SampleT9Default extends BaseSampleActivity
        implements T9PickerDialogFragment.T9PickerDialogHandler {

    private TextView mText;
    private Button mButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);

        mText = (TextView) findViewById(R.id.text);
        mButton = (Button) findViewById(R.id.button);

        mText.setText("--");
        mButton.setText("Set T9");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T9PickerBuilder npb = new T9PickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                npb.show();
            }
        });
    }

    @Override
    public void onDialogTextSet(int reference, String text) {
        mText.setText("Text: " + text);
    }
}
