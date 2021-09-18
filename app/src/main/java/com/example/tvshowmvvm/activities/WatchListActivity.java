package com.example.tvshowmvvm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tvshowmvvm.R;
import com.example.tvshowmvvm.adapters.WatchListAdapter;
import com.example.tvshowmvvm.databinding.ActivityWatchListBinding;
import com.example.tvshowmvvm.listeners.WatchListener;
import com.example.tvshowmvvm.models.TvShow;
import com.example.tvshowmvvm.utilities.TempDataHolder;
import com.example.tvshowmvvm.viewmodel.WatchListViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchListActivity extends AppCompatActivity implements WatchListener {

    private ActivityWatchListBinding activityWatchListBinding;
    private WatchListViewModel watchListViewModel;
    private WatchListAdapter watchListAdapter;
    private List<TvShow> watchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWatchListBinding = DataBindingUtil.setContentView(this,R.layout.activity_watch_list);
        watchList = new ArrayList<>();
        doInitialization();
    }

    private void doInitialization() {
        watchListViewModel = new ViewModelProvider(this).get(WatchListViewModel.class);
        activityWatchListBinding.imageBack.setOnClickListener(v -> {
            onBackPressed();
        });
        loadWatchlist();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(TempDataHolder.IS_WATCHLIST_UPDATED){
            loadWatchlist();
            TempDataHolder.IS_WATCHLIST_UPDATED = false;
        }
    }

    private void loadWatchlist() {
        activityWatchListBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(watchListViewModel.loadWatchList().subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(tvShows -> {
            activityWatchListBinding.setIsLoading(false);
            if(watchList.size() > 0){
                watchList.clear();
            }
            watchList.addAll(tvShows);
            watchListAdapter = new WatchListAdapter(watchList,this);
            activityWatchListBinding.watchlistRV.setAdapter(watchListAdapter);
            activityWatchListBinding.watchlistRV.setVisibility(View.VISIBLE);
            compositeDisposable.dispose();
        }));
    }

    @Override
    public void onTVShowClicked(TvShow tvShow) {

        Intent intent = new Intent(getApplicationContext(),TVshowDetailActivity.class);
        intent.putExtra("tvshow",tvShow);
        startActivity(intent);
    }

    @Override
    public void removeFromWatchList(TvShow tvShow, int position) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(watchListViewModel.removeFromWatchList(tvShow)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    watchList.remove(position);
                    watchListAdapter.notifyItemRemoved(position);
                    watchListAdapter.notifyItemRangeChanged(position,watchListAdapter.getItemCount());
                    compositeDisposable.dispose();
                }));
    }
}