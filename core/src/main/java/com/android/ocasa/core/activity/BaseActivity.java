package com.android.ocasa.core.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.android.ocasa.core.R;

/**
 * Created by ignacio on 21/05/2015.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
            return;
        }

        super.onBackPressed();
        overridePendingTransition(R.anim.activity_scale_in, R.anim.activity_translatex_out);
    }

    public void startNewActivity(Intent subActivity){

        startActivity(subActivity);
        overridePendingTransition(R.anim.activity_translatex_in, R.anim.activity_scale_out);
    }

    public void startNewActivityForResutl(Intent subActivity, int requestCode){

        startActivityForResult(subActivity, requestCode);
        overridePendingTransition(R.anim.activity_translatex_in, R.anim.activity_scale_out);
    }


    public void pushFragment(Fragment frag, String tag, int containerId){
        pushFragment(frag, tag, containerId, false);
    }

    public void pushFragment(Fragment frag, String tag, int containerId, boolean addToBackStack){

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(containerId, frag, tag);

        if (addToBackStack){
            transaction.addToBackStack(tag);
        }

        transaction.commit();
    }

    public void showFragment(Fragment frag, String tag, int containerId, boolean addToBackStack){

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.hide(getFragmentManager().findFragmentById(containerId));
        transaction.add(containerId, frag, tag);
        transaction.addToBackStack(null);

        if (addToBackStack){
            transaction.addToBackStack(tag);
        }

        transaction.commit();
    }

}
