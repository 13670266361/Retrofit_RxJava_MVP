package com.example.y.mvp.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.y.mvp.R;
import com.example.y.mvp.mvp.Bean.NewsDetailInfo;
import com.example.y.mvp.mvp.presenter.BasePresenter;
import com.example.y.mvp.mvp.presenter.NewsDetailPresenterImpl;
import com.example.y.mvp.mvp.presenter.ToolBarItemPresenterImpl;
import com.example.y.mvp.mvp.view.BaseView;
import com.example.y.mvp.network.Api;
import com.example.y.mvp.utils.ActivityUtils;
import com.example.y.mvp.utils.ImageLoaderUtils;
import com.example.y.mvp.utils.UIUtils;
import com.example.y.mvp.utils.theme.widget.ThemeCollapsingToolbarLayout;
import com.example.y.mvp.utils.theme.widget.ThemeImageView;
import com.example.y.mvp.utils.theme.widget.ThemeTextView;
import com.example.y.mvp.utils.theme.widget.ThemeToolbar;

/**
 * by 12406 on 2016/5/30.
 */
public class NewsDetailActivity extends BaseActivity
        implements BaseView.NewsDetailView, BaseView.ToolBarItemView {

    private ThemeImageView image;
    private ThemeCollapsingToolbarLayout collapsingToolbar;
    private ProgressBar progressBar;
    private ThemeTextView content;
    private ThemeToolbar toolbar;

    private int id;
    private String message;
    private BasePresenter.ToolBarItemPresenter toolBarItemPresenter;


    public static void startIntent(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        ActivityUtils.startActivity(NewsDetailActivity.class, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getBundle();
        init();
    }

    @Override
    protected void initById() {
        image = getView(R.id.image);
        collapsingToolbar = getView(R.id.collapsing_toolbar);
        progressBar = getView(R.id.progressBar);
        content = getView(R.id.content);
        toolbar = getView(R.id.toolbar);
    }

    private void init() {
        BasePresenter.NewsDetailPresenter newsDetailPresenter = new NewsDetailPresenterImpl(this);
        toolBarItemPresenter = new ToolBarItemPresenterImpl(this);
        newsDetailPresenter.requestNetWork(id);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                toolBarItemPresenter.switchId(item.getItemId());
                return true;
            }
        });

    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()) {
            id = bundle.getInt("id");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    @Override
    public void setData(NewsDetailInfo datas) {
        ImageLoaderUtils.display(getApplicationContext(), image, Api.IMAGER_URL + datas.getImg());
        content.setText(Html.fromHtml(datas.getMessage()));
        collapsingToolbar.setTitle(datas.getTitle());
        message = String.valueOf(Html.fromHtml(datas.getMessage()));
    }

    @Override
    public void switchShare() {
        ActivityUtils.share(message);
    }

    @Override
    public void netWorkError() {
        ActivityUtils.Toast(UIUtils.getString(R.string.network_error));
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_detail;
    }
}
