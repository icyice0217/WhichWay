package org.bangeek.whichway.activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import org.bangeek.whichway.R;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, View.OnClickListener {
    private TextView mTvExpectedArriveTime;
    private Button mBtnAdjustTime;
    private RecyclerView mRvLines;
    private Button mBtnGo;

    private TimePickerDialog mTimePickerDialog;

    private Calendar mExpectedArriveTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvExpectedArriveTime = (TextView) findViewById(R.id.tvExpectedArriveTime);
        mBtnAdjustTime = (Button) findViewById(R.id.btnAdjustTime);
        mRvLines = (RecyclerView) findViewById(R.id.rvLines);
        mBtnGo = (Button) findViewById(R.id.btnGo);

        mTimePickerDialog = new TimePickerDialog(this, this, mExpectedArriveTime.get(Calendar.HOUR_OF_DAY), mExpectedArriveTime.get(Calendar.MINUTE), true);

        mBtnAdjustTime.setOnClickListener(this);
        mBtnGo.setOnClickListener(this);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mExpectedArriveTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mExpectedArriveTime.set(Calendar.MINUTE, minute);

        mTvExpectedArriveTime.setText(String.format(getString(R.string.expected_arrive_time), hourOfDay, minute));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdjustTime:
                mTimePickerDialog.show();
                break;

            case R.id.btnGo:
                break;
        }
    }
}
