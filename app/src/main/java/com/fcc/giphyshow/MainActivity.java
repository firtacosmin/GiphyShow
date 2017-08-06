package com.fcc.giphyshow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.fcc.giphyshow.di.DaggerMainActivityComponent;
import com.fcc.giphyshow.di.MainActivityComponent;
import com.fcc.giphyshow.di.mainActivity.modules.RouterModule;
import com.fcc.giphyshow.ui.Navigator;
import com.fcc.giphyshow.ui.search.SearchListAdapter;
import com.fcc.giphyshow.ui.search.SearchViewController;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

//import com.fcc.giphyshow.di.mainActivity.DaggerMainActivityComponent;

public class MainActivity extends AppCompatActivity {

    MainActivityComponent diComponent;

    @BindView(R.id.main_container)
    ViewGroup container;


//    @Inject
//    Picasso picasso;

//    @Inject
//    SearchListAdapter adapter;

    @Inject
    Navigator navigator;

    private Router router;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        router = Conductor.attachRouter(this, container, savedInstanceState);

        /*init the dagger component*/
        diComponent = DaggerMainActivityComponent
                .builder()
                .mainAppComponent(((MainApp)getApplication()).getDiComponent())
                .routerModule(new RouterModule(router))
                .build();
        diComponent.injectMainActivity(this);


        navigator.navigateToLandingPage();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Not proud of it but no fast solution :)
        Controller controllerToAnnounce = router.getControllerWithTag(SearchViewController.TAG);
        if ( controllerToAnnounce != null && controllerToAnnounce.isAttached() ) {
            controllerToAnnounce.onActivityResult(requestCode, resultCode, data);
        }
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
