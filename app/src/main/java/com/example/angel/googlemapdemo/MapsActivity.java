package com.example.angel.googlemapdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static android.R.id.empty;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private Button btnSearch, btnCurrentLocation;
    private EditText edtLocation;
    private double latitude, longitude;
    private LatLng latLng;
    private GoogleApiClient googleApiClient;
    String locationsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnCurrentLocation = (Button) findViewById(R.id.btnCurrentLocation);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        edtLocation = (EditText) findViewById(R.id.edtLocation);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        btnCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMap.clear();

                //Creating a location object
                if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission(MapsActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location location =
                        LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }

                //Moving the map to location
                moveMap();

            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();

                locationsearch = edtLocation.getText().toString();
                //

                Toast.makeText(getBaseContext(), locationsearch, Toast.LENGTH_LONG).show();
                List<Address> adddressList = null;
                if (locationsearch != null && !locationsearch.equals("")) {
                    Toast.makeText(getBaseContext(), "Location is not empty", Toast.LENGTH_LONG).show();
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        adddressList =
                                geocoder.getFromLocationName(locationsearch, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = adddressList.get(0);
                    latLng = new LatLng(address.getLatitude(),
                            address.getLongitude());
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                    mMap.addMarker(new
                            MarkerOptions().position(latLng).title(locationsearch));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    //  mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                    mMap.setOnMapClickListener(new
                                                       GoogleMap.OnMapClickListener() {
                                                           @Override
                                                           public void onMapClick(LatLng latLng) {
                                                               mMap.clear();
                                                               mMap.addMarker(new
                                                                       MarkerOptions().position(latLng));

                                                               mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                                               mMap.animateCamera(CameraUpdateFactory.zoomTo(50));
                                                           }

                                                       });
                    mMap.setOnMarkerClickListener(new
                                                          GoogleMap.OnMarkerClickListener() {
                                                              @Override
                                                              public boolean onMarkerClick(Marker marker) {
                                                                  Intent intent = new Intent(MapsActivity.this,
                                                                          LocationActivity.class);

                                                                  intent.putExtra("locationsearch", locationsearch);
                                                                  intent.putExtra("latitude", latitude);
                                                                  intent.putExtra("longitude", longitude);
                                                                  startActivity(intent);
                                                                  //currentmarker.remove();
                                                                  return true;
                                                              }
                                                          });
                } else {
                    Toast.makeText(getBaseContext(), "Please Enter Location", Toast.LENGTH_LONG).show();
                }


            }


            /**
             * Manipulates the map once available.
             * This callback is triggered when the map is ready to be used.
             * This is where we can add markers or lines, add listeners or move the camera. In this case,
             * we just add a marker near Sydney, Australia.
             * If Google Play services is not installed on the device, the user will be prompted to install
             * it inside the SupportMapFragment. This method will only be triggered once the user has
             * installed Google Play services and returned to the app.
             */


        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    public void moveMap() {

        LatLng latLng = new LatLng(latitude, longitude);

        //Adding marker
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Current Location").draggable(true));

        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(50));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new
                        MarkerOptions().position(latLng).title("hello"));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                return true;
            }


        });
    }
}
