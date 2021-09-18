package com.example.tvshowmvvm.repositrios;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tvshowmvvm.network.ApiClient;
import com.example.tvshowmvvm.network.ApiService;
import com.example.tvshowmvvm.responses.TVShowDetailResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowDetailRepositry {

    private ApiService apiService;

    public TVShowDetailRepositry() {
        this.apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TVShowDetailResponse> getTVShowDetail(String tvShowId){
        MutableLiveData<TVShowDetailResponse> data = new MutableLiveData<>();
        apiService.getTVShowDetail(tvShowId).enqueue(new Callback<TVShowDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<TVShowDetailResponse> call,@NonNull Response<TVShowDetailResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TVShowDetailResponse> call,@NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
