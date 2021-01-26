package com.example.bus_tracking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Interpolator;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.bus_tracking.ui.home.HomeViewModel;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class DriverActivity extends FragmentActivity implements OnMapReadyCallback
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener,
//        LocationListener

{

    private GoogleMap mMap;


    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Object home_Screen;

    @Override
    protected void onDestroy() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onDestroy();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle SavedInstancesState) {

        home_Screen = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container );
        inti();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return root;
    }

    private void inti() {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(10f);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LatLng newposition = new LatLng(locationResult.getLastLocation().getLatitude(),
                        locationResult.getLastLocation().getLongitude());

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newposition, 18f));
            }
        };
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    //    private static final int MY_PERMISSION_REQUEST_CODE=7000;
//    private static final int PLAY_SERVICE_RES_REQUEST_CODE=7001;
//
//    private LocationRequest mlocationRequest;
//    private GoogleApiClient mGoogleApiClient;
//    private Location mLastLocation;
//    private static int  UPDATE_INTERVAL= 5000;
//    private static int  FATEST_INTERVAL= 3000;
//    private static int  DISPLACEMENT= 10;
//
//    ToggleButton t1;
//    DatabaseReference driver;
//    GeoFire geoFire;
//    Marker mCurrent;
    SupportMapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//       t1=findViewById(R.id.locationswitch);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    t1.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//            if (t1.isChecked()){
//                Toast.makeText(getApplicationContext(),"running code",Toast.LENGTH_SHORT).show();
//                StartLocation();
//                DislayLocation();
//                Snackbar.make(mapFragment.getView(),"you are online",Snackbar.LENGTH_SHORT)
//                        .show();
//                } else{
//                Toast.makeText(getApplicationContext(),"stop code",Toast.LENGTH_SHORT).show();
//            StopLocation();
//            mCurrent.remove();
//            Snackbar.make(mapFragment.getView(),"you are online",Snackbar.LENGTH_SHORT)
//                    .show();
//        }
//        }
//    });
//geo fire
//    driver =FirebaseDatabase.getInstance().getReference("Driver");
//    geoFire =new GeoFire(driver);
//    setUpdateLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //check permission
        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            return;
//                        }

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                            @Override
                            public boolean onMyLocationButtonClick() {

                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.

                                }
                                fusedLocationProviderClient.getLastLocation()
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        LatLng userLatling = new LatLng(location.getLatitude(), location.getLongitude());
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatling, 18f));
                                    }
                                });
                                return true;
                            }
                        });
//                            public void onMyLocationClick(@NonNull Location location) {
//                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                    // TODO: Consider calling
//                                    //    ActivityCompat#requestPermissions
//                                    // here to request the missing permissions, and then overriding
//                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                    //                                          int[] grantResults)
//                                    // to handle the case where the user grants the permission. See the documentation
//                                    // for ActivityCompat#requestPermissions for more details.
//                                    return;
//                                }
//                                fusedLocationProviderClient.getLastLocation()
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                                            }
//                                        })
//                                        .addOnSuccessListener(new OnSuccessListener<Location>() {
//                                            @Override
//                                            public void onSuccess(Location location) {
//
//                                            }
//                                        })
//                                        return true;
//                            }
//                        });

                          View loctionbutton= ((View)mapFragment.getView().findViewById(Integer.parseInt("1"))
                                  .getParent()).findViewById(Integer.parseInt("2"));
                        RelativeLayout.LayoutParams params =(RelativeLayout.LayoutParams) loctionbutton.getLayoutParams();
                        //Right button
                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP,0);
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
                        params.setMargins(0,0,0,50);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    Toast.makeText(getApplicationContext(),"Permission"+permissionDeniedResponse.getPermissionName()+""+"was denied!" ,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
                try{

                    boolean succes= googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(),R.raw.uber_maps_style));
                    if(!succes)
                        Log.e("Error_wrong", "Style parse error");
                }catch (Resources.NotFoundException e){
                    Log.e("Error_wrong",e.getMessage());
                }
    }

//    private void setUpdateLocation() {
//        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
//           //Request runtime Permission
//            ActivityCompat.requestPermissions(this , new String[]{
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                    },MY_PERMISSION_REQUEST_CODE);
//
//        }else{
//            if(checkPlayservices()){
//                buildGoogleApiClient();
//                createLocationRequest();
//                if(t1.isChecked())
//                    DislayLocation();
//
//            }
//
//        }
//
//    }
//
//    private void createLocationRequest() {
//        mlocationRequest = new LocationRequest();
//        mlocationRequest.setInterval(UPDATE_INTERVAL);
//        mlocationRequest.setFastestInterval(FATEST_INTERVAL);
//        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mlocationRequest.setSmallestDisplacement(DISPLACEMENT);
//    }
//
//    private void buildGoogleApiClient() {
//        mGoogleApiClient=new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        mGoogleApiClient.connect();
//    }
//
//    private boolean checkPlayservices() {
//     int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS){
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
//            GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICE_RES_REQUEST_CODE).show();
//        else {
//                Toast.makeText(this, "THIS DEVICE NOT SUPPORTED", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//            return false;
//
//        }
//        return true;
//    }
//
//
//    private void StopLocation() {
//        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
//            return;
//        }
//        LocationServices.FusedLocationApi.removeLocationUpdates( mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode)
//        {
//            case MY_PERMISSION_REQUEST_CODE:
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//            {
//                if(checkPlayservices()){
//                    buildGoogleApiClient();
//                    createLocationRequest();
//                    if(t1.isChecked())
//                        DislayLocation();
//
//                }
//            }
//        }
//    }
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//
//    private void DislayLocation(){
//        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
//            return;
//        }
//        mLastLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if(t1.isChecked())
//        {
//            final double latitude =mLastLocation.getLatitude();
//            final double longitude = mLastLocation.getLongitude();
/// /            geoFire.setLocation((FirebaseAuth.getInstance().getCurrentUser().getUid()), new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
////
//              @Override
//                public void onComplete(String key, com.google.firebase.database.DatabaseError error) {
//                    //Add marker
//                    if(mCurrent != null)
//                         mCurrent.remove();
//                    mCurrent =mMap.addMarker(new MarkerOptions().icon((BitmapDescriptorFactory.fromResource(R.drawable.bus_no_bg))).position(new LatLng(latitude,longitude)).title("you"));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),15.0f));
//                    rotateMarker(mCurrent,-360,mMap);
//                }
//            });
//
//        }
//        else
//            {
//                Log.d("ERROR","Cannot get your Location");
//            }
//
//    }
//
//    private void rotateMarker(final Marker mCurrent, final float i, GoogleMap mMap) {
//        final Handler hander =new Handler();
//        final long start = SystemClock.uptimeMillis();
//        final float startrotation =mCurrent.getRotation();
//        final long duration =1500;
//        final LinearInterpolator interpolator = new LinearInterpolator();
//        hander.post(new Runnable() {
//            @Override
//            public void run() {
//                long elaped =SystemClock.uptimeMillis() - start;
//                float t= interpolator.getInterpolation((float) elaped/duration);
//                float rot =t*i+(1 - t) * startrotation;
//                mCurrent.setRotation(-rot > 180 ?rot/2:rot) ;
//                if (t<1.0)
//                {
//                    hander.postDelayed(this,16);
//                }
//            }
//        });
//
//
//    }
//
//    private void StartLocation(){
////        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
////                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
////            return;
//////        }
////        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mlocationRequest, (com.google.android.gms.location.LocationListener) this);
//    }
//
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//    mLastLocation =location;
//    DislayLocation();
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        DislayLocation();
//        StartLocation();
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//    mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
}