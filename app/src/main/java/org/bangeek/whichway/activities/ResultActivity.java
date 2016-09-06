package org.bangeek.whichway.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import org.bangeek.whichway.R;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView mRvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_result);

        mRvResult = (RecyclerView) findViewById(R.id.rvResult);
    }
}
