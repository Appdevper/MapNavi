package com.appdevper.mapnavi.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.appdevper.mapnavi.R;
import com.appdevper.mapnavi.app.DataApp;
import com.appdevper.mapnavi.app.ServiceCallback;
import com.appdevper.mapnavi.app.ServiceRequest;
import com.appdevper.mapnavi.fragment.BudFragment;
import com.appdevper.mapnavi.fragment.ContactFragment;
import com.appdevper.mapnavi.fragment.FoodFragment;
import com.appdevper.mapnavi.fragment.HomeFragment;
import com.appdevper.mapnavi.fragment.HotelFragment;
import com.appdevper.mapnavi.fragment.NewsFragment;
import com.appdevper.mapnavi.fragment.PlaceFragment;
import com.appdevper.mapnavi.fragment.ShopFragment;
import com.appdevper.mapnavi.util.CallbackView;

public class HomeActivity extends AppCompatActivity {

    private Fragment fragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        supportInvalidateOptionsMenu();
        setFragment(HomeFragment.getInstance(homeCallback));
    }

    private CallbackView callback = new CallbackView() {
        @Override
        public void callback(View view) {
            setFragment(HomeFragment.getInstance(homeCallback));
        }
    };

    private CallbackView homeCallback = new CallbackView() {
        @Override
        public void callback(View view) {

            switch (view.getId()) {
                case R.id.fabMenu:
                    setFragment(fragment);
                    break;
                case R.id.lyProduct:
                    Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.lyPromotion:
                    setFragment(ShopFragment.getInstance(callback));
                    break;
                case R.id.lyStore:
                    setFragment(BudFragment.getInstance(callback));
                    break;
                case R.id.lyCheckIn:
                    setFragment(HotelFragment.getInstance(callback));
                    break;
                case R.id.lyPoint:
                    setFragment(FoodFragment.getInstance(callback));
                    break;
                case R.id.lyPresent:
                    setFragment(NewsFragment.getInstance(callback));
                    break;
                case R.id.lyContact:
                    setFragment(ContactFragment.getInstance(callback));
                    break;
            }

        }
    };

    private void setFragment(Fragment f) {
        fragment = f;
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout);
        mFragmentTransaction.replace(R.id.container, f);
        mFragmentTransaction.commit();
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        if (fragment instanceof HomeFragment) {
            finish();
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        } else {
            setFragment(HomeFragment.getInstance(homeCallback));
        }

    }
}
