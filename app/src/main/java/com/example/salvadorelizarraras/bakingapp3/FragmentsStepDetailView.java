package com.example.salvadorelizarraras.bakingapp3;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.salvadorelizarraras.bakingapp3.Recipe.Steps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * Created by Salvador Elizarraras on 17/03/2018.
 */

public class FragmentsStepDetailView extends Fragment implements ExoPlayer.EventListener, View.OnClickListener{

    public static final String TAG = FragmentsStepDetailView.class.getSimpleName();
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private int mPreviousPosition = 0;
    private ImageView left,right;

    private ArrayList<Steps> mSteps;
    private Steps mStep;
    private int mPosition;
    private Uri uriVieo;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {

            mSteps = getArguments().getParcelableArrayList("steps");
            mPosition = getArguments().getInt("position");
            mStep = mSteps.get(mPosition);
            uriVieo = Uri.parse(mStep.getVideoURL());
        }else{
            Log.d(TAG, "onCreate: " + savedInstanceState.getInt("sesion", 0));
            mSteps = savedInstanceState.getParcelableArrayList("steps");
            mPosition = savedInstanceState.getInt("position");
            mStep = mSteps.get(mPosition);
            uriVieo = Uri.parse(mStep.getVideoURL());
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        if(savedInstanceState != null) {
            mPreviousPosition = savedInstanceState.getInt("sesion",0);
            Log.d(TAG, "onCreateView: "+mPreviousPosition);
        }

        View view = inflater.inflate(R.layout.fragment_recipe_detail,container,false);
        mPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.player_view);
        left = (ImageView) view.findViewById(R.id.left);
        left.setOnClickListener(this);
        right = (ImageView) view.findViewById(R.id.right);
        right.setOnClickListener(this);

        return view;
    }

    private void initializePlayer(Uri mediaUri) {

        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveVideoTrackSelection.Factory(bandwidthMeter));
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity().getApplicationContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            if(mediaUri.toString().isEmpty()) {
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                                R.drawable.novideo);
                        mPlayerView.setDefaultArtwork(bitmap);
                        mPlayerView.setUseController(false);

            }else {
                // Set the ExoPlayer.EventListener to this activity.
                mExoPlayer.addListener(this);
                mPlayerView.setUseController(true);
                // Prepare the MediaSource.
                mExoPlayer.seekTo(mPreviousPosition);
                String userAgent = Util.getUserAgent(getContext(), "BakingApp3");
                MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                        getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(true);
            }

        }
    }

    private void releaseExoplayer(){

        if(mExoPlayer != null){
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.stop();
            mPlayerView.setDefaultArtwork(null);
            mExoPlayer.release();
        } mExoPlayer = null;
    }


    private void updateVisibility(){

        if (!Utils.isTablet(getContext()) && getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG, mPosition + "updateData: " + mSteps.size());
            left.setVisibility(View.VISIBLE);
            right.setVisibility(View.VISIBLE);

            if (mPosition <= 0) {
                left.setVisibility(View.INVISIBLE);

            } else if (mPosition >= mSteps.size() - 1) {
                right.setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
        switch (view.getId()){
            case R.id.left:
                if(mPosition > 0) {
                    mPosition = mPosition - 1;
                    mStep =  mSteps.get(mPosition);
                    uriVieo = Uri.parse(mStep.getVideoURL());
                }
                break;
            case R.id.right:
                if (mPosition < mSteps.size()-1) {
                    mPosition = mPosition + 1;
                    mStep =  mSteps.get(mPosition);
                    uriVieo = Uri.parse(mStep.getVideoURL());
                }
                break;
        }
        mPreviousPosition = 0;
        updateVisibility();
        releaseExoplayer();
        initializePlayer(uriVieo);

    }
    //#region lyfecycle
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mExoPlayer!= null){
            Log.d(TAG, "onSaveInstanceState: " + mExoPlayer.getCurrentPosition());
            outState.putInt("sesion", (int) mExoPlayer.getCurrentPosition());
            outState.putInt("position",mPosition);
            outState.putParcelableArrayList("steps", mSteps);
            mPreviousPosition = (int) mExoPlayer.getCurrentPosition();
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            Log.d(TAG, "onViewStateRestored: ");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            Log.d(TAG, "onActivityCreated: ");
            mPreviousPosition = savedInstanceState.getInt("sesion", 0);
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: "+mPreviousPosition);
        updateVisibility();
        initializePlayer(uriVieo);


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        releaseExoplayer();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
    }

    //#endregion

    //#region callbacks exoplayer
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
    //endregion
}
