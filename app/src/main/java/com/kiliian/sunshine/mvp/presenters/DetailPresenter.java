package com.kiliian.sunshine.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.kiliian.sunshine.app.SunshineApp;
import com.kiliian.sunshine.data.Weather;
import com.kiliian.sunshine.mvp.views.DetailView;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqldelight.SqlDelightStatement;

import org.threeten.bp.LocalDate;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


@InjectViewState
public class DetailPresenter extends BasePresenter<DetailView> {

    private String dateStr;

    @Inject
    BriteDatabase database;

    public DetailPresenter() {
        SunshineApp.getAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        SqlDelightStatement query = Weather.FACTORY.select_weather(LocalDate.parse(dateStr));
        database.createQuery(Weather.TABLE_NAME, query.statement, query.args)
                .mapToOne(Weather.MAPPER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weather -> getViewState().showWeather(weather), Throwable::printStackTrace);
    }

    public void setDate(String dateStr) {
        this.dateStr = dateStr;
    }

}
