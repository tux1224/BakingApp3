package com.example.salvadorelizarraras.bakingapp3;

import android.app.FragmentManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.salvadorelizarraras.bakingapp3.Recipe.Recipe;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "MainActivity";
    public FragmentHome fragmentHome;
    ArrayList<Fragment> frags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        Log.d(TAG, "onCreate: ");
        frags = new ArrayList<>();

        if(savedInstanceState == null) {
            fragmentHome = new FragmentHome();
            Log.d(TAG, "NewInstance");
            getSupportFragmentManager().beginTransaction().add(R.id.main_container, fragmentHome, "FragmentHome").commit();
        }
    }

    @Override
    public void onBackPressed() {
        boolean isThere = false;
        for (Fragment fragment :getSupportFragmentManager().getFragments()) {
            Log.d(TAG, "onBackPressed: "+fragment.getTag());
            for (Fragment fragment1 : frags){

                if(fragment1 == fragment){
                    isThere =true;
                }

            }
            if (!isThere){
                frags.add(fragment);
                Log.d(TAG, "added "+fragment.getTag());
            }
        }


       Fragment fragment = frags.get(frags.size()-1);
        frags.remove(fragment);
        if(frags.size()>0) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        Log.d(TAG, "size() "+frags.size());
        super.onBackPressed();

    }


    //#region lifecycle
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("instance","instance");

        Log.d(TAG, "onSaveInstanceState:");

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }


    //#endregion

}
