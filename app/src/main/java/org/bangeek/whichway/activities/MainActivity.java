package org.bangeek.whichway.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.bangeek.shjt.models.LineStop;
import org.bangeek.shjt.utils.ServiceUtils;
import org.bangeek.whichway.R;
import org.bangeek.whichway.adapters.CardAdapter;
import org.bangeek.whichway.app.App;
import org.bangeek.whichway.data.DataStore;
import org.bangeek.whichway.models.LineCard;
import org.bangeek.whichway.utils.CommonUtil;
import org.bangeek.whichway.utils.TransformUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okio.BufferedSource;
import okio.Okio;
import okio.Source;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, View.OnClickListener {
    private TextView mTvExpectedArriveTime;
    private Button mBtnAdjustTime;
    private RecyclerView mRvLines;
    private Button mBtnGo;
    private CardAdapter mAdapter;

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

        mTimePickerDialog = new TimePickerDialog(this, this,
                mExpectedArriveTime.get(Calendar.HOUR_OF_DAY),
                mExpectedArriveTime.get(Calendar.MINUTE), true);
        mTvExpectedArriveTime.setText(String.format(getString(R.string.expected_arrive_time),
                mExpectedArriveTime.get(Calendar.HOUR_OF_DAY),
                mExpectedArriveTime.get(Calendar.MINUTE)));

        mBtnAdjustTime.setOnClickListener(this);
        mBtnGo.setOnClickListener(this);

        mAdapter = new CardAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRvLines.setLayoutManager(layoutManager);
        mRvLines.setAdapter(mAdapter);

        //update bus lines data
        int version = DataStore.getBuslinesVersion(MainActivity.this);
        if (ServiceUtils.hasUpdateLinesData(version)) {
            ServiceUtils.getBusLineList(new Action1<Object>() {
                @Override
                public void call(Object o) {
                    DataStore.saveBusLines(MainActivity.this, o);
                    Log.d(App.TAG, "Bus Lines Data Updated!");
                }
            });
        }

        //Loading data
        rx.Observable
                .just(DataStore.STOP_LINE_JSON_ASSET)
                .observeOn(Schedulers.io())
                .map(new Func1<String, List<LineCard>>() {
                    @Override
                    public List<LineCard> call(String s) {
                        BufferedSource bufferedSource = null;
                        try {
                            InputStream inputStream = getAssets().open(s);
                            Source source = Okio.source(inputStream);
                            bufferedSource = Okio.buffer(source);
                            String json = bufferedSource.readUtf8();
                            List<LineStop> lineStops = new Gson().fromJson(json,
                                    new TypeToken<List<LineStop>>() {
                                    }.getType());
                            List<LineCard> lineCards = new ArrayList<>();
                            for (LineStop stop : lineStops) {
                                lineCards.add(TransformUtil.toLineCard(stop));
                            }
                            return lineCards;
                        } catch (Exception ex) {
                            Log.e(App.TAG, "ERROR: " + ex);
                            return null;
                        } finally {
                            CommonUtil.closeQuietly(bufferedSource);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<LineCard>>() {
                               @Override
                               public void call(List<LineCard> lineCards) {
                                   mAdapter.addItems(lineCards);
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Toast.makeText(MainActivity.this, "Loading lines failed.", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Action0() {
                            @Override
                            public void call() {
                                Toast.makeText(MainActivity.this, "Loading lines finished.", Toast.LENGTH_SHORT).show();
                            }
                        });
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
                startActivity(new Intent(this, ResultActivity.class));
                break;
        }
    }
}
