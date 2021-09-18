package com.example.tvshowmvvm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tvshowmvvm.R;
import com.example.tvshowmvvm.adapters.EpisodesAdapter;
import com.example.tvshowmvvm.adapters.ImageSliderAdapter;
import com.example.tvshowmvvm.databinding.ActivityMainBinding;
import com.example.tvshowmvvm.databinding.ActivityTVshowDetailBinding;
import com.example.tvshowmvvm.databinding.LayoutEpisodeBottomSheetBinding;
import com.example.tvshowmvvm.models.TvShow;
import com.example.tvshowmvvm.responses.TVShowDetailResponse;
import com.example.tvshowmvvm.utilities.TempDataHolder;
import com.example.tvshowmvvm.viewmodel.TVShowDetailViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TVshowDetailActivity extends AppCompatActivity {

    private ActivityTVshowDetailBinding activityMainBinding;
    private TVShowDetailViewModel tvShowDetailViewModel;
    private BottomSheetDialog episodeBottomSheetDialog;
    private LayoutEpisodeBottomSheetBinding layoutEpisodeBottomSheetBinding;
    private TvShow tvShow;
    private Boolean isTVShowAvailableInWatchList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_t_vshow_detail);
        activityMainBinding.imageBack.setOnClickListener(v -> onBackPressed());
        tvShow = (TvShow) getIntent().getSerializableExtra("tvshow");
        doinitialization();
        checkTVShowInWatchList();
    }

    private void doinitialization() {
        tvShowDetailViewModel = new ViewModelProvider(this).get(TVShowDetailViewModel.class);
        getTVShowDetail();
    }
    private void getTVShowDetail(){
        activityMainBinding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShow.getId());
        tvShowDetailViewModel.getTVShowDetail(tvShowId).observe(this, tvShowDetailResponse -> {
            activityMainBinding.setIsLoading(false);
            if(tvShowDetailResponse.getTvShowDeatil() != null){
                if(tvShowDetailResponse.getTvShowDeatil().getPictures() != null){
                    loadImageSlider(tvShowDetailResponse.getTvShowDeatil().getPictures());
                }
                activityMainBinding.setImageUrl(tvShowDetailResponse.getTvShowDeatil().getImagePath());
                activityMainBinding.imageTVShow.setVisibility(View.VISIBLE);
                activityMainBinding.setDescription(String.valueOf(
                        HtmlCompat.fromHtml(tvShowDetailResponse.getTvShowDeatil().getDescription()
                        , HtmlCompat.FROM_HTML_MODE_LEGACY)
                ));
                activityMainBinding.textDescription.setVisibility(View.VISIBLE);
                activityMainBinding.textreadMore.setVisibility(View.VISIBLE);
                activityMainBinding.textreadMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(activityMainBinding.textreadMore.getText().toString().equals("Read More")){
                            activityMainBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                            activityMainBinding.textDescription.setEllipsize(null);
                            activityMainBinding.textreadMore.setText("Read Less");
                        }else{
                            activityMainBinding.textDescription.setMaxLines(4);
                            activityMainBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                            activityMainBinding.textreadMore.setText("Read More");
                        }
                    }
                });
                activityMainBinding.setRating(
                        String.format(
                                Locale.getDefault(),"%.2f",
                                Double.parseDouble(tvShowDetailResponse.getTvShowDeatil().getRating())
                        )
                );
                if(tvShowDetailResponse.getTvShowDeatil().getGenres()!=null){
                    activityMainBinding.setGenre(tvShowDetailResponse.getTvShowDeatil().getGenres()[0]);
                }else{
                    activityMainBinding.setGenre("N/A");
                }
                activityMainBinding.setRuntime(tvShowDetailResponse.getTvShowDeatil().getRunTime() + " Min");
                activityMainBinding.viewDivider1.setVisibility(View.VISIBLE);
                activityMainBinding.layoutMusic.setVisibility(View.VISIBLE);
                activityMainBinding.viewDivider2.setVisibility(View.VISIBLE);
                activityMainBinding.buttonWebsite.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(tvShowDetailResponse.getTvShowDeatil().getUrl()));
                    startActivity(intent);
                });
                activityMainBinding.buttonWebsite.setVisibility(View.VISIBLE);
                activityMainBinding.buttonEpisodes.setVisibility(View.VISIBLE);
                activityMainBinding.buttonEpisodes.setOnClickListener(v -> {
                    if(episodeBottomSheetDialog == null){
                        episodeBottomSheetDialog = new BottomSheetDialog(TVshowDetailActivity.this);
                        layoutEpisodeBottomSheetBinding = DataBindingUtil.inflate(
                                LayoutInflater.from(TVshowDetailActivity.this),
                                R.layout.layout_episode_bottom_sheet,findViewById(R.id.episodesContainer)
                                ,false
                        );
                        episodeBottomSheetDialog.setContentView(layoutEpisodeBottomSheetBinding.getRoot());
                        layoutEpisodeBottomSheetBinding.episodeRV.setAdapter(
                                new EpisodesAdapter(tvShowDetailResponse.getTvShowDeatil().getEpisodes())
                        );
                        layoutEpisodeBottomSheetBinding.textTitle.setText(
                                String.format("Episodes | %s",tvShow.getName())
                        );
                        layoutEpisodeBottomSheetBinding.imageClose.setOnClickListener(v1 -> episodeBottomSheetDialog.dismiss());
                    }

                    // optional section //
                    FrameLayout frameLayout = episodeBottomSheetDialog.findViewById(
                            com.google.android.material.R.id.design_bottom_sheet
                    );
                    if(frameLayout!=null){
                        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                        bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }

                    // optional section end
                    episodeBottomSheetDialog.show();
                });
                activityMainBinding.imageWatchList.setOnClickListener(v ->
                {
                    CompositeDisposable compositeDisposable = new CompositeDisposable();
                    if(isTVShowAvailableInWatchList){
                        compositeDisposable.add(tvShowDetailViewModel.removeTvShowfromWatchList(tvShow)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() ->{
                                    isTVShowAvailableInWatchList = true;
                                    TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                    activityMainBinding.imageWatchList.setImageResource(R.drawable.ic_watchlist);
                                    Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show();
                                    compositeDisposable.dispose();
                                }));
                    }else{
                        compositeDisposable.add(tvShowDetailViewModel.addtoWatchList(tvShow)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(()->{
                                    TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                    activityMainBinding.imageWatchList.setImageResource(R.drawable.ic_added);
                                    Toast.makeText(TVshowDetailActivity.this, "added......", Toast.LENGTH_SHORT).show();
                                    compositeDisposable.dispose();
                                })
                        );
                    }
                });
                activityMainBinding.imageWatchList.setVisibility(View.VISIBLE);
                loadBasicTVShowDetail();
            }
        });
    }

    private void loadImageSlider(String[] imgSlider){
        activityMainBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityMainBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(imgSlider));
        activityMainBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityMainBinding.viewFadingPage.setVisibility(View.VISIBLE);
        setUpSliderIndicator(imgSlider.length);
        activityMainBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });

    }

    private void setUpSliderIndicator(int count){

        ImageView[] indicator = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8,0,8,0);
        for(int i=0 ; i<indicator.length ; i++){
            indicator[i] = new ImageView(getApplicationContext());
            indicator[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),R.drawable.background_slider_indicator_inactive
            ));
            indicator[i].setLayoutParams(layoutParams);
            activityMainBinding.layoutSliderIndicator.addView(indicator[i]);
        }
        activityMainBinding.layoutSliderIndicator.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int position){
        int childCount = activityMainBinding.layoutSliderIndicator.getChildCount();
        for (int i=0 ; i<childCount ; i++){
            ImageView imageView = (ImageView) activityMainBinding.layoutSliderIndicator.getChildAt(i);
            if(i == position){
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),R.drawable.background_slider_indicator_active));
            }else{
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(),R.drawable.background_slider_indicator_inactive));
            }
        }
    }

    private void loadBasicTVShowDetail(){
        activityMainBinding.setTvShowName(tvShow.getName());
        activityMainBinding.setNetWorkCountry(tvShow.getNetwork()
        + "(" +tvShow.getCountry() +")");
        activityMainBinding.setStatus(tvShow.getStatus());
        activityMainBinding.setStartedDate(tvShow.getStartDate());
        activityMainBinding.textName.setVisibility(View.VISIBLE);
        activityMainBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityMainBinding.textStatus.setVisibility(View.VISIBLE);
        activityMainBinding.textStarted.setVisibility(View.VISIBLE);
    }

    private void checkTVShowInWatchList(){
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(tvShowDetailViewModel.getTvShowFromWatchList(String.valueOf(tvShow.getId()))
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(tvShow1 -> {
            isTVShowAvailableInWatchList = true;
            activityMainBinding.imageWatchList.setImageResource(R.drawable.ic_added);
            compositeDisposable.dispose();
        }));
    }
}