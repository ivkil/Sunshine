package com.kiliian.sunshine.di.modules;

import android.content.Context;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.kiliian.sunshine.data.WeatherDbHelper;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;

@Module
public class AppModule {

	private Context context;

	public AppModule(Context context) {
		this.context = context;
	}

	@Provides
	@Singleton
	Context provideContext() {
		return context;
	}

	@Provides
	@Singleton
	static SqlBrite provideSqlBrite(){
		return new SqlBrite.Builder().build();
	}

	@Provides
	@Singleton
	static BriteDatabase provideDatabase(SqlBrite sqlBrite, WeatherDbHelper weatherDbHelper){
		return sqlBrite.wrapDatabaseHelper(weatherDbHelper, Schedulers.io());
	}

	@Provides
	@Singleton
	static GooglePlayDriver provideGooglePlayDriver(Context context){
		return new GooglePlayDriver(context);
	}

	@Provides
	@Singleton
	static FirebaseJobDispatcher provideFirebaseJobDispatcher(GooglePlayDriver googlePlayDriver){
		return new FirebaseJobDispatcher(googlePlayDriver);
	}
}