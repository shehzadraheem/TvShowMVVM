package com.example.tvshowmvvm.network;

import com.example.tvshowmvvm.responses.TVShowDetailResponse;
import com.example.tvshowmvvm.responses.TvShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<TvShowResponse> getMostPopularTvShow(@Query("page") int page);

    @GET("show-details")
    Call<TVShowDetailResponse> getTVShowDetail(@Query("q") String tvShowId);

    @GET("search")
    Call<TvShowResponse> searchTVShow(@Query("q")String query , @Query("page") int page);
}
