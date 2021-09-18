package com.example.tvshowmvvm.models;

import com.google.gson.annotations.SerializedName;

public class Episode {

    @SerializedName("episode")
    private String episode;

    @SerializedName("season")
    private String season;

    @SerializedName("name")
    private String name;

    @SerializedName("air_date")
    private String airDate;

    public String getEpisode() {
        return episode;
    }

    public String getSeason() {
        return season;
    }

    public String getName() {
        return name;
    }

    public String getAirDate() {
        return airDate;
    }
}
