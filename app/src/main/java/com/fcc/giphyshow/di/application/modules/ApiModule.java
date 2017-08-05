package com.fcc.giphyshow.di.application.modules;

import com.fcc.giphyshow.data.search.request.SearchService;
import com.fcc.giphyshow.di.application.ForGiphy;
import com.fcc.giphyshow.di.application.MainAppScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by firta on 8/5/2017.
 * This is the module that will provide the instances to the APIs
 */

@Module
public class ApiModule {

    /**
     *
     * @param retrofit the {@link Retrofit} instance pointing to Giphy base URL
     * @return the {@link SearchService}. Service that offers access to the search path of the Giphy API
     */
    @MainAppScope
    @Provides
    public SearchService provideSearchService(@ForGiphy Retrofit retrofit){
        return retrofit.create(SearchService.class);
    }

}
