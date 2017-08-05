package com.fcc.giphyshow.di.application;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by firta on 8/5/2017.
 * A qualifier to differentiate the {@link retrofit2.Retrofit} instance configured with Giphy base url
 */

@Qualifier
@Documented
@Retention(RUNTIME)
public @interface ForGiphy {
}
