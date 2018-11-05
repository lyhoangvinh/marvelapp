package marvelapp.lyhoangvinh.com.marvelapp.di.scopes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * scope for instances which live as long as the activity
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {}
