package com.appdevper.mapnavi.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.appdevper.mapnavi.R;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by worawit on 2/13/16.
 */
public class DetailActivity extends AppCompatActivity {
    private StreetViewPanorama mStreetViewPanorama;
    private Button btnRequestDirection;
    private TextView tvDetail;
    private TextView tvLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tvLocation = (TextView) findViewById(R.id.tvLocation);

        SupportStreetViewPanoramaFragment streetViewPanoramaFragment = (SupportStreetViewPanoramaFragment) getSupportFragmentManager().findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {
            @Override
            public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                mStreetViewPanorama = panorama;
                LatLng dest = getIntent().getParcelableExtra("location");
                mStreetViewPanorama.setPosition(dest, 100);
                //tvLocation.setText("latitude: " + dest.latitude + " longitude: " + dest.longitude);
            }
        });
        btnRequestDirection = (Button) findViewById(R.id.button);
        btnRequestDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finishActivity(300);
                finish();
            }
        });

        String detail = getIntent().getStringExtra("detail");
        //String address = getIntent().getStringExtra("address");
        tvDetail = (TextView) findViewById(R.id.tvDetail);
        tvDetail.setText(detail);
    }
}
