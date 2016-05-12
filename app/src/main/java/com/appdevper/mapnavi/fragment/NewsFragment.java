package com.appdevper.mapnavi.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdevper.mapnavi.R;
import com.appdevper.mapnavi.app.ServiceCallback;
import com.appdevper.mapnavi.app.ServiceRequest;
import com.appdevper.mapnavi.model.News;
import com.appdevper.mapnavi.model.NewsResponse;
import com.appdevper.mapnavi.model.Place;
import com.appdevper.mapnavi.model.PlaceResponse;
import com.appdevper.mapnavi.util.CallbackView;
import com.appdevper.mapnavi.util.NewsHolder;
import com.appdevper.mapnavi.util.PlaceHolder;
import com.appdevper.mapnavi.util.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by worawit on 3/5/16.
 */
public class NewsFragment extends Fragment implements View.OnClickListener, ServiceCallback {

    private static NewsFragment instance;
    private CallbackView callback;
    private FloatingActionButton fabMenu;
    private RecyclerView listView;
    private NewsHolder mAdapter;
    private List<News> newsList = new ArrayList<>();
    private SearchView searchView;
    private String url = "http://isarapanich.com/note/news";

    public static NewsFragment getInstance(CallbackView callback) {
        if (instance == null) {
            instance = new NewsFragment();
        }
        instance.setCallback(callback);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_place, container, false);
        listView = (RecyclerView) rootView.findViewById(R.id.listView);
        mAdapter = new NewsHolder(getActivity(), newsList);
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listView.setAdapter(mAdapter);
        fabMenu = (FloatingActionButton) rootView.findViewById(R.id.fabMenu);
        fabMenu.setOnClickListener(this);
        callService();
        return rootView;
    }

    private void callService() {
        new ServiceRequest(getActivity(), this).execute(url);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home, menu);
        initSearchView(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mAdapter.getFilter().filter("");
                return false;
            }
        });

    }

    public void setCallback(CallbackView callback) {
        this.callback = callback;
    }

    public void setPlaceList(List<News> list) {
        this.newsList = list;
    }

    @Override
    public void onClick(View view) {
        callback.callback(view);
    }

    @Override
    public void onSuccess(String s) {
        String response = "{\"news\":" + s + "}";
        Gson gson = new Gson();
        NewsResponse newsResponse = gson.fromJson(response, NewsResponse.class);
        newsList.clear();
        newsList.addAll(newsResponse.news);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFail(String s) {
        Utils.showDialog(getActivity(), "Error!", s);
    }
}
