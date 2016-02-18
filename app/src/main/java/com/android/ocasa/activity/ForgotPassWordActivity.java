package com.android.ocasa.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.ocasa.core.activity.BarActivity;
import com.android.ocasa.fragment.ForgotPasswordFragment;

/**
 * Created by ignacio on 04/02/16.
 */
public class ForgotPassWordActivity extends BarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null)
            return;

        pushFragment(Fragment.instantiate(this, ForgotPasswordFragment.class.getName()), "ForgotPassword");
    }
}