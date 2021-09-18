package com.example.tvshowmvvm.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshowmvvm.repositrios.MostPopularTVShowRepositry;
import com.example.tvshowmvvm.responses.TvShowResponse;

public class MostPopularTVShowViewModel extends ViewModel {

    private MostPopularTVShowRepositry mostPopularTVShowRepositry;

    public MostPopularTVShowViewModel(){
        mostPopularTVShowRepositry = new MostPopularTVShowRepositry();
    }
    public LiveData<TvShowResponse> getMostPopularTvShow(int page){
        return mostPopularTVShowRepositry.getMostPopularTvShow(page);
    }
}
