package com.example.tvshowmvvm.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshowmvvm.repositrios.SearchTVShowRepository;
import com.example.tvshowmvvm.responses.TvShowResponse;

public class SearchViewModel extends ViewModel {

    private SearchTVShowRepository searchTVShowRepository;

    public SearchViewModel(){
        searchTVShowRepository = new SearchTVShowRepository();
    }

    public LiveData<TvShowResponse> searchTVShow(String query , int page){
        return searchTVShowRepository.searchTVShow(query,page);
    }
}
