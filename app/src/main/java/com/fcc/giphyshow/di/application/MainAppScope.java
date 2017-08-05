package com.fcc.giphyshow.di.application;

import com.fcc.giphyshow.di.MainAppComponent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by firta on 8/5/2017.
 * The scope retained during the {@link MainAppComponent} lifetime
 */


@Scope
public @interface MainAppScope{

}
