package com.example.salvadorelizarraras.bakingapp3;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Salvador Elizarraras on 23/03/2018.
 */

public class SimpleIdlingResource implements IdlingResource {

    @Nullable
    private volatile ResourceCallback mCallback;
    private AtomicBoolean idleNow = new AtomicBoolean(false);
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        return idleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
            mCallback = callback;
    }
    public void setIdleState(boolean isIdleNow){
        idleNow.set(isIdleNow);
        if (isIdleNow && mCallback != null){
            mCallback.onTransitionToIdle();
        }
    }

}
