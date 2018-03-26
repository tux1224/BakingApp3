package com.example.salvadorelizarraras.bakingapp3;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.salvadorelizarraras.bakingapp3.Recipe.Ingredient;
import com.example.salvadorelizarraras.bakingapp3.Recipe.Recipe;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Salvador Elizarraras on 17/03/2018.
 */

public class FragmentHome extends Fragment  implements LoaderManager.LoaderCallbacks<String>, AdapterHome.Listeners{



    @BindView(R.id.m_recycler_home)
    RecyclerView mRecycler;
    @BindView(R.id.mLoading)
    ProgressBar mLoading;

    private AdapterHome adapterHome;
    private String BRING_DATA_URL = "bring_data_url";
    private int LOADER_CODE = 101;
    private int mScrollPosition;
    private static Parcelable savedRecyclerLayoutState;
    public static final String TAG = FragmentHome.class.getSimpleName();
    private int mPosition = 0;
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIdlingResource();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        // region recycler definition


             RecyclerView.LayoutManager linearLayout = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ?
                    new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) :
                    new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);

            mRecycler.setLayoutManager(linearLayout);
            adapterHome = new AdapterHome();
            adapterHome.setListener(this);
            mRecycler.setAdapter(adapterHome);
            mRecycler.setHasFixedSize(true);
            mRecycler.onChildAttachedToWindow(view);
            // endregion


            Bundle bundle = new Bundle();
            bundle.putString(BRING_DATA_URL, Utils.mUriBase);

            LoaderManager loaderManager = getLoaderManager();
            Loader<String> searchLoader = loaderManager.getLoader(LOADER_CODE);

            if (searchLoader == null) {

                loaderManager.initLoader(LOADER_CODE, bundle, this);

            } else {

                loaderManager.restartLoader(LOADER_CODE, bundle, this);

            }

        return view;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        if (mIdlingResource != null) {

            final Object object = new Object();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {

                        synchronized (object) {
                            object.wait(1000);
                            mIdlingResource.setIdleState(true);
                            object.notify();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
        }


    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(getContext()) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args == null)
                    return;

                mLoading.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                String mUrl = Utils.mUriBase;
                if(mUrl == null || mUrl.isEmpty()){
                    return null;
                }
                try {
                    URL url = new URL(Utils.mUriBase);
                    String data = Utils.getResponseFromHttpUrl(url);
                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }



        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(data != null && !data.equals("")){
            mLoading.setVisibility(View.GONE);
            adapterHome.swapData(Utils.getDataFromFile(getContext(),data));

            if (mIdlingResource != null) {
                Log.d(TAG, "onLoadFinished: ");
                mIdlingResource.setIdleState(true);
            }
            if(savedRecyclerLayoutState != null)
                mRecycler.getLayoutManager().onRestoreInstanceState((savedRecyclerLayoutState));



        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }


    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick()" );
        int id = Integer.parseInt( String.valueOf(view.getTag()));
        openWithFragment(id);
    }


    private void openWithFragment(final int id) {
        Recipe recipe = adapterHome.getItem(id);
        Fragment fragment = new RecipeDetail();
        Bundle bundle = new Bundle();
        bundle.putParcelable("recipe", recipe);
        fragment.setArguments(bundle);
            if(!Utils.isTablet(getContext())) {
                getFragmentManager().beginTransaction().replace(R.id.main_container, fragment, RecipeDetail.TAG).addToBackStack(RecipeDetail.TAG).commit();
            }else{
                getFragmentManager().beginTransaction().replace(R.id.main_container, fragment, RecipeDetail.TAG).addToBackStack(RecipeDetail.TAG).commit();

            }
        ArrayList<String> listIngredients = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
                listIngredients.add(ingredient.getIngredient());

        }
            Intent intent = new Intent(getContext(),WidgetService.class);
            intent.setAction(WidgetService.ACTION);
            bundle = new Bundle();
            bundle.putStringArrayList("list",listIngredients);
            intent.putExtra("bundle",bundle);
            getContext().startService(intent);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        return (mIdlingResource == null) ? (mIdlingResource = new SimpleIdlingResource()) : mIdlingResource;
    }

    //#region lifecycle
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            Log.d(TAG, "onActivityCreated: ");

        }
    }



    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");



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

    //#endregion
}
