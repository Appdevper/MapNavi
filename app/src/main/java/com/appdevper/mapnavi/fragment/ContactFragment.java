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
import com.appdevper.mapnavi.util.CallbackView;
import com.appdevper.mapnavi.util.NewsHolder;
import com.appdevper.mapnavi.util.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by worawit on 3/5/16.
 */
public class ContactFragment extends Fragment implements View.OnClickListener {

    private static ContactFragment instance;
    private CallbackView callback;
    private FloatingActionButton fabMenu;

    public static ContactFragment getInstance(CallbackView callback) {
        if (instance == null) {
            instance = new ContactFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        fabMenu = (FloatingActionButton) rootView.findViewById(R.id.fabMenu);
        fabMenu.setOnClickListener(this);

        return rootView;
    }


    public void setCallback(CallbackView callback) {
        this.callback = callback;
    }


    @Override
    public void onClick(View view) {
        callback.callback(view);
    }

}
