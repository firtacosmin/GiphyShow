package com.fcc.giphyshow.di.details;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by firta on 8/5/2017.
 * the {@link Scope} for the {@link com.fcc.giphyshow.di.GifDetailsComponent}
 */

@Scope
@Retention(RetentionPolicy.CLASS)
public @interface GifDetailsScope {
}
