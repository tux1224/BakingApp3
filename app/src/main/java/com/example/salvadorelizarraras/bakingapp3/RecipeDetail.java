package com.example.salvadorelizarraras.bakingapp3;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.salvadorelizarraras.bakingapp3.Recipe.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Salvador Elizarraras on 11/03/2018.
 */

public class RecipeDetail extends Fragment implements AdapterSteps.Listeners {

    public static final String TAG = RecipeDetail.class.getSimpleName();
    private Recipe mRecipe;
    @BindView(R.id.mRecycler_ingredients)
    RecyclerView mRecicler;
    private AdapterSteps mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.recipe_detail_view,container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        mRecipe = bundle.getParcelable("recipe");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecicler.setLayoutManager(layoutManager);
        mAdapter = new AdapterSteps();
        mAdapter.setListener(this);
        mAdapter.setData(mRecipe.getSteps());
        mRecicler.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
    }



    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "onViewStateRestored: ");
    }

    @Override
    public void onClick(View view) {

        int id = ((int) view.getTag() - 1);
        Log.d(TAG, "onClick: " + id);
        Bundle bundle = new Bundle();

        if (id == -1) {
            Fragment fragment;
            fragment = new FragmentIngredients();
            bundle.putParcelableArrayList("ingredients", mRecipe.getIngredients());
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.main_container, fragment, FragmentIngredients.TAG).addToBackStack(FragmentIngredients.TAG).commit();
        } else {
            Fragment fragment = new FragmentsStepDetailView();
            bundle.putParcelableArrayList("steps", mRecipe.getSteps());
            bundle.putInt("position", id);
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.main_container, fragment, FragmentsStepDetailView.TAG).addToBackStack(FragmentsStepDetailView.TAG).commit();

        }
    }

    //#regionlifecycle
    @Override
    public void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();

    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");


    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: ");
        super.onDetach();
    }
    //#endregion

}
