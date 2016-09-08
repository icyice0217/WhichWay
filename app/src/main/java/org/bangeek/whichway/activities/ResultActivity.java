package org.bangeek.whichway.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.bangeek.shjt.models.Cars;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

import okio.BufferedSource;
import okio.Okio;
import okio.Source;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ResultActivity extends AppCompatActivity {
    private RecyclerView mRvResult;
    private CardAdapter mAdapter;
    private Observable<List<LineCard>> mRequestData;
    private Subscription mSubscription;
    private Toast mToast;
    private CompositeSubscription mCompositeSubscription;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_result);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRvResult = (RecyclerView) findViewById(R.id.rvResult);
        mAdapter = new CardAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRvResult.setLayoutManager(layoutManager);
        mRvResult.setAdapter(mAdapter);

        //Request data
        mRequestData = Observable
                .interval(5, TimeUnit.SECONDS)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        Log.d(App.TAG, "Request at " + System.currentTimeMillis());
                        return DataStore.STOP_LINE_JSON_ASSET;
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Func1<String, List<LineStop>>() {
                    @Override
                    public List<LineStop> call(String s) {
                        BufferedSource bufferedSource = null;
                        try {
                            InputStream inputStream = getAssets().open(s);
                            Source source = Okio.source(inputStream);
                            bufferedSource = Okio.buffer(source);
                            String json = bufferedSource.readUtf8();
                            return new Gson().fromJson(json,
                                    new TypeToken<List<LineStop>>() {
                                    }.getType());
                        } catch (Exception ex) {
                            Log.e(App.TAG, "ERROR: " + ex);
                            return null;
                        } finally {
                            CommonUtil.closeQuietly(bufferedSource);
                        }
                    }
                })
                .flatMap(new Func1<List<LineStop>, Observable<LineStop>>() {
                    @Override
                    public Observable<LineStop> call(List<LineStop> lineStops) {
                        return Observable.from(lineStops);
                    }
                })
                .map(new Func1<LineStop, List<LineCard>>() {
                    @Override
                    public List<LineCard> call(final LineStop lineStop) {
                        ServiceUtils.getCarsByLine(lineStop, new Action1<Object>() {
                            @Override
                            public void call(Object o) {
                                updateList(lineStop, (Cars) o);
                            }
                        });
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io());

        mToast = Toast.makeText(App.context(), "Toast", Toast.LENGTH_SHORT);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCompositeSubscription = new CompositeSubscription();
        if (mRequestData != null)
            mCompositeSubscription.add(mRequestData.subscribe());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed())
            mCompositeSubscription.unsubscribe();
        mToast.cancel();
    }

    private void updateList(LineStop lineStop, Cars cars) {
        LineCard lineCard = TransformUtil.toLineCard(lineStop, cars);
        if (lineCard == null) return;

        mCompositeSubscription.add(rx.Observable
                .just(lineCard)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<LineCard>() {
                            @Override
                            public void call(LineCard lineCard) {
                                mAdapter.addOrUpdateItem(lineCard);
                            }
                        }
                        ,
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                mToast.setText("Loading data failed.");
                                mToast.show();
                            }
                        },
                        new Action0() {
                            @Override
                            public void call() {
                                mToast.setText("Loading data finished.");
                                mToast.show();
                            }
                        }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
