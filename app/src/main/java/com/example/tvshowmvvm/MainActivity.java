package com.example.tvshowmvvm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.tvshowmvvm.activities.SearchActivity;
import com.example.tvshowmvvm.activities.TVshowDetailActivity;
import com.example.tvshowmvvm.activities.WatchListActivity;
import com.example.tvshowmvvm.adapters.TVShowAdapter;
import com.example.tvshowmvvm.databinding.ActivityMainBinding;
import com.example.tvshowmvvm.listeners.TVShowListener;
import com.example.tvshowmvvm.models.TvShow;
import com.example.tvshowmvvm.responses.TvShowResponse;
import com.example.tvshowmvvm.viewmodel.MostPopularTVShowViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TVShowListener {

    private MostPopularTVShowViewModel viewModel;
    private ActivityMainBinding activityMainBinding;
    private List<TvShow> list = new ArrayList<>();
    private TVShowAdapter adapter;
    private int curentPage = 1;
    private int totalAvailablePages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        //setContentView(R.layout.activity_main);
        doInitialization();
    }

    private void doInitialization(){
        activityMainBinding.tvShowRV.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowViewModel.class);
        adapter = new TVShowAdapter(list,this);
        activityMainBinding.tvShowRV.setAdapter(adapter);
        activityMainBinding.tvShowRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!activityMainBinding.tvShowRV.canScrollVertically(1)){
                    if(curentPage <= totalAvailablePages){
                        curentPage += 1;
                        getMostPopularTVShow();
                    }
                }
            }
        });
        activityMainBinding.imageWatchlist.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), WatchListActivity.class)));
        activityMainBinding.imageSearch.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        });
        getMostPopularTVShow();
    }

    private void getMostPopularTVShow() {
        toggleloading();
        viewModel.getMostPopularTvShow(curentPage).observe(this, mostPopularTVShowResponse->{
            toggleloading();
            if(mostPopularTVShowResponse != null){
                totalAvailablePages = mostPopularTVShowResponse.getPages();
                if(mostPopularTVShowResponse.getTv_shows()!=null){
                    int oldCount = list.size();
                    list.addAll(mostPopularTVShowResponse.getTv_shows());
                    adapter.notifyItemRangeInserted(oldCount,list.size());
                }
            }
        });

    }

    private void toggleloading(){
        if(curentPage == 1){
            if(activityMainBinding.getIsLoading()!=null && activityMainBinding.getIsLoading()){
                activityMainBinding.setIsLoading(false);
            }else{
                activityMainBinding.setIsLoading(true);
            }
        }else{
            if(activityMainBinding.getIsLoading() != null && activityMainBinding.getIsLoading()){
                activityMainBinding.setIsLoading(false);
            }else{
                activityMainBinding.setIsLoading(true);
            }
        }
    }

    @Override
    public void OnClickedTVShow(TvShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVshowDetailActivity.class);
        intent.putExtra("tvshow",tvShow);
        startActivity(intent);
    }
}