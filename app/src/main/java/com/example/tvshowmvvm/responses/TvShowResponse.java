package com.example.tvshowmvvm.responses;

import com.example.tvshowmvvm.models.TvShow;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int pages;

    @SerializedName("tv_shows")
    private List<TvShow> tv_shows;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<TvShow> getTv_shows() {
        return tv_shows;
    }

    public void setTv_shows(List<TvShow> tv_shows) {
        this.tv_shows = tv_shows;
    }
}
