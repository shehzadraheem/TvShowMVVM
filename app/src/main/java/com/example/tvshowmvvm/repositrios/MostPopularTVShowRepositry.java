package com.example.tvshowmvvm.repositrios;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tvshowmvvm.network.ApiClient;
import com.example.tvshowmvvm.network.ApiService;
import com.example.tvshowmvvm.responses.TvShowResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostPopularTVShowRepositry {

    private ApiService apiService;

    public MostPopularTVShowRepositry(){
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TvShowResponse> getMostPopularTvShow(int page){
        MutableLiveData<TvShowResponse> data = new MutableLiveData<>();
        apiService.getMostPopularTvShow(page).enqueue(new Callback<TvShowResponse>() {
            @Override
            public void onResponse(Call<TvShowResponse> call, Response<TvShowResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<TvShowResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
