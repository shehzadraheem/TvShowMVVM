package com.example.tvshowmvvm.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.tvshowmvvm.R;
import com.example.tvshowmvvm.adapters.TVShowAdapter;
import com.example.tvshowmvvm.databinding.ActivityEarchBinding;
import com.example.tvshowmvvm.listeners.TVShowListener;
import com.example.tvshowmvvm.models.TvShow;
import com.example.tvshowmvvm.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity implements TVShowListener {

    private ActivityEarchBinding activityEarchBinding;
    private SearchViewModel viewModel;
    private List<TvShow> tvShows = new ArrayList<>();
    private TVShowAdapter adapter ;
    private int currentPage = 1;
    private int totalAvailablepage = 1;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEarchBinding = DataBindingUtil.setContentView(this,R.layout.activity_earch);
        doInitialization();
    }

    private void doInitialization(){
        activityEarchBinding.imageBack.setOnClickListener(v -> onBackPressed());
        activityEarchBinding.searchRV.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        adapter = new TVShowAdapter(tvShows,this);
        activityEarchBinding.searchRV.setAdapter(adapter);
        activityEarchBinding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(timer != null)
                    timer.cancel();
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!s.toString().trim().isEmpty()){
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                currentPage = 1;
                                totalAvailablepage = 1;
                                searchTVShow(s.toString());
                            });
                        }
                    },800);
                }else{
                    tvShows.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        activityEarchBinding.searchRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!activityEarchBinding.searchRV.canScrollVertically(1)){
                    if(!activityEarchBinding.inputSearch.getText().toString().isEmpty()) {
                        if (currentPage < totalAvailablepage) {
                            currentPage += 1;
                            searchTVShow(activityEarchBinding.inputSearch.getText().toString());
                        }
                    }
                }
            }
        });
        activityEarchBinding.inputSearch.requestFocus();
    }

    @Override
    public void OnClickedTVShow(TvShow tvShow) {
        Intent intent = new Intent(getApplicationContext(),TVshowDetailActivity.class);
        intent.putExtra("tvshow",tvShow);
        startActivity(intent);
    }

    private void toggleloading(){
        if(currentPage == 1){
            if(activityEarchBinding.getIsLoading()!=null && activityEarchBinding.getIsLoading()){
                activityEarchBinding.setIsLoading(false);
            }else{
                activityEarchBinding.setIsLoading(true);
            }
        }else{
            if(activityEarchBinding.getIsLoading() != null && activityEarchBinding.getIsLoading()){
                activityEarchBinding.setIsLoading(false);
            }else{
                activityEarchBinding.setIsLoading(true);
            }
        }
    }

    private void searchTVShow(String query){
        toggleloading();
        viewModel.searchTVShow(query,currentPage).observe(this,tvShowResponse -> {
            toggleloading();
            if(tvShowResponse!=null){
                totalAvailablepage = tvShowResponse.getPages();
                if(tvShowResponse.getTv_shows()!=null){
                    int oldCount = tvShows.size();
                    tvShows.addAll(tvShowResponse.getTv_shows());
                    adapter.notifyItemRangeInserted(oldCount,tvShows.size());
                }
            }
        });
    }
}