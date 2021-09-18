package com.example.tvshowmvvm.responses;

import com.example.tvshowmvvm.models.TVShowDeatil;
import com.google.gson.annotations.SerializedName;

public class TVShowDetailResponse {

    @SerializedName("tvShow")
    private TVShowDeatil tvShowDeatil;

    public TVShowDeatil getTvShowDeatil() {
        return tvShowDeatil;
    }
}
