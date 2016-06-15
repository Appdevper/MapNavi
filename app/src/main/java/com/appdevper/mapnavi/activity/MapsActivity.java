package com.appdevper.mapnavi.activity;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.appdevper.mapnavi.app.ServiceCallback;
import com.appdevper.mapnavi.app.ServiceRequest;
import com.appdevper.mapnavi.model.Place;
import com.appdevper.mapnavi.model.PlaceResponse;
import com.appdevper.mapnavi.util.Callback;
import com.appdevper.mapnavi.util.GPSTracker;
import com.appdevper.mapnavi.R;
import com.appdevper.mapnavi.util.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import android.support.design.widget.Snackbar;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionCallback, Callback, ServiceCallback {

    private GoogleMap mMap;
    private LatLng origin;
    private LatLng destination;
    private Button btnRequestDirection;
    private static final float ZOOM_LEVEL = 16.5f;
    private Toolbar toolbar;
    private RelativeLayout lyNavi;
    private TextView tvDetail;
    private String name;
    private String type;
    private String detail;
    private Marker mPositionMarker;
    private boolean navi = false;
    private CameraUpdate cu;
    private String url = "http://isarapanich.com/note/map";

    private PlaceResponse placeResponse;
    private boolean isAll;
    private BottomBar mBottomBar;
    private FloatingActionButton fabMenu;
    private TextView tvDes;
    private String distance = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        lyNavi = (RelativeLayout) findViewById(R.id.lyNavi);
        lyNavi.setVisibility(View.GONE);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.buttombar, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                lyNavi.setVisibility(View.GONE);
                if (menuItemId == R.id.action_all) {
                    filter("");
                } else if (menuItemId == R.id.action_store) {
                    filter("S");
                } else if (menuItemId == R.id.action_bundar) {
                    filter("B");
                } else if (menuItemId == R.id.action_hotel) {
                    filter("H");
                } else if (menuItemId == R.id.action_food) {
                    filter("F");
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.action_bundar) {

                }
            }
        });


        btnRequestDirection = (Button) findViewById(R.id.button);
        btnRequestDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (destination != null) {
                    if (btnRequestDirection.getText().toString().equals("Start")) {
                        btnRequestDirection.setText("Stop");
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin, ZOOM_LEVEL));
                        if (!checkPermission()) {
                            return;
                        }
                        mMap.setMyLocationEnabled(true);
                        navi = true;

                        tvDes.setVisibility(View.VISIBLE);
                        int dis = Integer.parseInt(distance) / 1000;
                        String ss = name + " ระยะทาง " + dis + " KM";
                        tvDes.setText(ss);
                    } else if (btnRequestDirection.getText().toString().equals("Stop")) {
                        btnRequestDirection.setText("Start");
                        mMap.animateCamera(cu);
                        if (!checkPermission()) {
                            return;
                        }
                        mMap.setMyLocationEnabled(false);

                        tvDes.setVisibility(View.GONE);
                        tvDes.setText("");
                    } else {
                        requestDirection();
                    }
                }
            }
        });

        tvDetail = (TextView) findViewById(R.id.tvDetail);
        tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MapsActivity.this, DetailActivity.class);
                i.putExtra("address", tvDetail.getText().toString());
                i.putExtra("detail", detail);
                i.putExtra("location", destination);
                startActivityForResult(i, 300);
            }
        });

        tvDes = (TextView) findViewById(R.id.tvDes);
        tvDes.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_menu) {
            startActivity(new Intent(this, HomeActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == 200) {
            double longitude = 0;
            double latitude = 0;
            if (data != null) {
                longitude = data.getDoubleExtra("lat", 0);
                latitude = data.getDoubleExtra("long", 0);
            }
            addMark(new LatLng(latitude, longitude));
        }

        if (resultCode == RESULT_OK && requestCode == 300) {
            requestDirection();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        new ServiceRequest(this).execute(url);
        origin = GPSTracker.newInstance(this, this).getLatLng();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, ZOOM_LEVEL));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mPositionMarker = marker;
                destination = marker.getPosition();
                name = marker.getTitle();
                for (Place place : placeResponse.placeList) {
                    if (place.name.equals(name)) {
                        detail = place.detail;
                    }
                }
                if (isAll) {
                    lyNavi.setVisibility(View.VISIBLE);
//                    new ReverseGeocodingTask(MapsActivity.this).execute(destination);
                }
                return false;
            }
        });

    }

    private void addMark(LatLng latLng) {
        destination = latLng;
        mMap.clear();
        MarkerOptions mp = new MarkerOptions();
        mp.position(latLng);
        mp.title(name);
        if (type.equals("S")) {
            mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_shop));
        } else if (type.equals("B")) {
            mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_bud));
        } else if (type.equals("H")) {
            mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_hotel));
        } else if (type.equals("F")) {
            mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_food));
        }

        mPositionMarker = mMap.addMarker(mp);
        mPositionMarker.showInfoWindow();

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom));
        lyNavi.setVisibility(View.VISIBLE);
//        new ReverseGeocodingTask(this).execute(latLng);
    }

    private Marker addMark(Place place) {

        MarkerOptions mp = new MarkerOptions();
        mp.position(place.getLocation());
        mp.title(place.name);

        if (place.type.equals("S")) {
            mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_shop));
        } else if (place.type.equals("B")) {
            mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_bud));
        } else if (place.type.equals("H")) {
            mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_hotel));
        } else if (place.type.equals("F")) {
            mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_food));
        }
        return mMap.addMarker(mp);

    }

    public void requestDirection() {
        mBottomBar.hide();
        GoogleDirection.withServerKey("AIzaSyDl3DcuGm1rGGF8cbCy5xKjixRsFmcR5_s")
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(this);
    }

    @Override
    public void onDirectionSuccess(Direction direction) {
        //Snackbar.make(btnRequestDirection, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        if (direction.isOK()) {
            if (!checkPermission()) {
                return;
            }
            mMap.clear();

            btnRequestDirection.setText("Start");
            mMap.setMyLocationEnabled(true);
            mMap.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));
            distance = direction.getRouteList().get(0).getLegList().get(0).getDistance().getValue();
            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            mMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            builder.include(origin);

            for (LatLng latLng : directionPositionList) {
                Place place = checkDistance(latLng);
                if (place != null) {
                    Marker mark = addMark(place);
                    if (mark.getTitle().equals(mPositionMarker.getTitle())) {
                        mark.showInfoWindow();
                    }
                    builder.include(place.getLocation());
                }
            }

            LatLngBounds bounds = builder.build();
            int padding = 100; // offset from edges of the map in pixels
            cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.moveCamera(cu);
            // btnRequestDirection.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Snackbar.make(btnRequestDirection, t.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void callback(final Location location) {
        if (location == null)
            return;

        if (navi) {
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));

            final Place place = checkDistance(location);
            if (place != null) {
                String msg = "คุณต้องการเดินทางไปที่ " + place.name + "หรือไม่";
                Utils.showDialog(this, "แจ้งเตือน", msg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        destination = place.getLocation();
                        requestDirection();
                    }
                });
            }
        }
    }

    private Place checkDistance(Location location) {

        for (int i = 0; i < placeResponse.placeList.size(); i++) {
            if (!placeResponse.placeList.get(i).isCheck) {
                if (Utils.distance(placeResponse.placeList.get(i).latitude, placeResponse.placeList.get(i).longitude, location.getLatitude(), location.getLongitude()) <= 1) {
                    placeResponse.placeList.get(i).isCheck = true;
                    return placeResponse.placeList.get(i);
                }
            }
        }
        return null;
    }


    private Place checkDistance(LatLng latLng) {

        for (int i = 0; i < placeResponse.placeList.size(); i++) {
            //if (!placeResponse.placeList.get(i).isCheck) {
                if (Utils.distance(placeResponse.placeList.get(i).latitude, placeResponse.placeList.get(i).longitude, latLng.latitude, latLng.longitude) <= 2) {
                    //placeResponse.placeList.get(i).isCheck = true;
                    return placeResponse.placeList.get(i);
                }
            //}
        }
        return null;
    }

    @Override
    public void onSuccess(String s) {
        String response = "{\"placeList\":" + s + "}";
        Gson gson = new Gson();
        placeResponse = gson.fromJson(response, PlaceResponse.class);
        try {
            destination = getIntent().getParcelableExtra("location");
            name = getIntent().getStringExtra("name");
            type = getIntent().getStringExtra("type");
            detail = getIntent().getStringExtra("detail");
            addMark(destination);
            isAll = false;
            mBottomBar.hide();
        } catch (Exception e) {
            mBottomBar.show();
            isAll = true;
            filter("");
        }
    }

    private void filter(String type) {
        if (mMap == null) {
            return;
        }
        mMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Place place : placeResponse.placeList) {
            if (type.equals(place.type) || type.equals("")) {
                addMark(place);
                builder.include(place.getLocation());
            }
        }

        LatLngBounds bounds = builder.build();
        int padding = 100;
        cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);
    }

    @Override
    public void onFail(String s) {

    }


    public boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
}
