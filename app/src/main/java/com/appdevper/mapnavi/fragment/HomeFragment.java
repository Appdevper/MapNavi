package com.appdevper.mapnavi.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.appdevper.mapnavi.R;
import com.appdevper.mapnavi.util.CallbackView;


/**
 * Created by worawit on 2/20/16.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private static HomeFragment instance;
    private FloatingActionButton fabMenu;
    private CallbackView callback;
    private RelativeLayout lyProduct;
    private RelativeLayout lyPromotion;
    private RelativeLayout lyStore;
    private RelativeLayout lyCheckIn;
    private RelativeLayout lyPoint;
    private RelativeLayout lyPresent;
    private RelativeLayout lyContact;

    public static HomeFragment getInstance(CallbackView callback) {
        if (instance == null) {
            instance = new HomeFragment();
        }
        instance.setCallback(callback);
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        lyProduct = (RelativeLayout) rootView.findViewById(R.id.lyProduct);
        lyPromotion = (RelativeLayout) rootView.findViewById(R.id.lyPromotion);
        lyStore = (RelativeLayout) rootView.findViewById(R.id.lyStore);
        lyCheckIn = (RelativeLayout) rootView.findViewById(R.id.lyCheckIn);
        lyPoint = (RelativeLayout) rootView.findViewById(R.id.lyPoint);
        lyPresent = (RelativeLayout) rootView.findViewById(R.id.lyPresent);
        lyContact = (RelativeLayout) rootView.findViewById(R.id.lyContact);

        fabMenu = (FloatingActionButton) rootView.findViewById(R.id.fabMenu);

        fabMenu.setOnClickListener(this);
        lyProduct.setOnClickListener(this);
        lyPromotion.setOnClickListener(this);
        lyStore.setOnClickListener(this);
        lyCheckIn.setOnClickListener(this);
        lyPoint.setOnClickListener(this);
        lyPresent.setOnClickListener(this);
        lyContact.setOnClickListener(this);

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
