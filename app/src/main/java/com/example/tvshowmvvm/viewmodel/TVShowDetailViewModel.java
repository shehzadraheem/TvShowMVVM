package com.example.tvshowmvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshowmvvm.database.TVShowDatabase;
import com.example.tvshowmvvm.models.TvShow;
import com.example.tvshowmvvm.repositrios.TVShowDetailRepositry;
import com.example.tvshowmvvm.responses.TVShowDetailResponse;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class TVShowDetailViewModel extends AndroidViewModel {

    private TVShowDetailRepositry tvShowDetailRepositry;
    private TVShowDatabase tvShowDatabase;

    public TVShowDetailViewModel(@NonNull Application application){
        super(application);
        tvShowDetailRepositry = new TVShowDetailRepositry();
        tvShowDatabase = TVShowDatabase.getTvShowDatabase(application);
    }
    public LiveData<TVShowDetailResponse> getTVShowDetail(String tvShowId){
        return tvShowDetailRepositry.getTVShowDetail(tvShowId);
    }

    public Completable addtoWatchList(TvShow tvShow){
        return tvShowDatabase.tvShowDao().addWatchList(tvShow);
    }

    public Flowable<TvShow> getTvShowFromWatchList(String tvShowId){
        return tvShowDatabase.tvShowDao().getTvShowFromWatchList(tvShowId);
    }

    public Completable removeTvShowfromWatchList(TvShow tvShow){
        return tvShowDatabase.tvShowDao().removeFromWatchList(tvShow);
    }
}
