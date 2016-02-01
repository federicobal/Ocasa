package com.android.ocasa.activity;

import android.os.Bundle;

import com.android.ocasa.core.activity.BarActivity;
import com.android.ocasa.fragment.DetailRecordFragment;

/**
 * Created by ignacio on 28/01/16.
 */
public class DetailRecordActivity extends BarActivity {

    public static final String EXTRA_RECORD_ID = "record_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null)
            return;

        Bundle extras = getIntent().getExtras();

        pushFragment(DetailRecordFragment.newInstance((int) extras.getLong(EXTRA_RECORD_ID)), "Detail");
    }
}