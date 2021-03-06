package com.fcc.giphyshow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.fcc.giphyshow.di.DaggerMainActivityComponent;
import com.fcc.giphyshow.di.MainActivityComponent;
import com.fcc.giphyshow.di.mainActivity.modules.RouterModule;
import com.fcc.giphyshow.di.mainActivity.modules.SearchListControllerModule;
import com.fcc.giphyshow.ui.Navigator;
import com.fcc.giphyshow.ui.search.view.SearchViewController;
import com.fcc.giphyshow.utils.ExceptionHandler;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    MainActivityComponent diComponent;

    @BindView(R.id.main_container)
    ViewGroup container;



    @Inject
    Navigator navigator;

    private Router router;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        router = Conductor.attachRouter(this, container, savedInstanceState);

        /*init the dagger component*/
        diComponent = DaggerMainActivityComponent
                .builder()
                .mainAppComponent(((MainApp)getApplication()).getDiComponent())
                .searchListControllerModule(new SearchListControllerModule())
                .routerModule(new RouterModule(router))
                .build();
        diComponent.injectMainActivity(this);


        navigator.navigateToLandingPage();


    }



    @Override
    public void onBackPressed() {
        if (navigator.onBackPressed()) {
            super.onBackPressed();
        }
    }


    public MainActivityComponent getDiComponent() {
        return diComponent;
    }
}
