package com.example.bus_tracking.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import de.hdodenhof.circleimageview.CircleImageView;

import com.example.bus_tracking.Common;
import com.example.bus_tracking.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements OnMapReadyCallback {


    private GoogleMap mMap;
    FirebaseDatabase database;

    private DatabaseReference currentUserRef, driversLocationref;
    private DatabaseReference onlineref;
    private HomeViewModel homeViewModel;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    SupportMapFragment mapFragment;
   String CityName;
   private boolean isFirstime=true;

    //online databsaeSystem

    GeoFire geoFire;
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists() && currentUserRef != null)
                currentUserRef.onDisconnect().removeValue();


        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Snackbar.make(mapFragment.getView(), error.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    };

    @Override
    public void onDestroy() {

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

        onlineref.removeEventListener(valueEventListener);
        geoFire.removeLocation(FirebaseAuth.getInstance().getCurrentUser().getUid());
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        registeronlineSystem();
    }

    private void registeronlineSystem() {
        onlineref.addValueEventListener(valueEventListener);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        inti();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return root;
    }

    private void inti() {
        onlineref = FirebaseDatabase.getInstance().getReference().child(".info/connected");
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        database= FirebaseDatabase.getInstance();
        driversLocationref=database.getReference(Common.DRIVERS_LOCATION_REFERENCES);



//        driversLocationref = FirebaseDatabase.getInstance().getReference(Common.DRIVERS_LOCATION_REFERENCES);
//
//        currentUserRef =FirebaseDatabase.getInstance().getReference(Common.DRIVERS_LOCATION_REFERENCES)
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//
//        geoFire = new GeoFire(driversLocationref);

//
//        registeronlineSystem();



        locationRequest = new LocationRequest();
        locationRequest.setInterval(15000);//15sec
        locationRequest.setFastestInterval(10000);//10secf
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(50f);//50m

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LatLng newposition = new LatLng(locationResult.getLastLocation().getLatitude(),
                        locationResult.getLastLocation().getLongitude());

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newposition, 18f));

//                /// here ,after get location,we willl get adress name
//                Geocoder geocoder=new Geocoder(getContext(), Locale.getDefault());
//                List<Address> addressList;

                // here ,after get location,we willl get adress name
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                List<Address> addressList;

                try {
//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Common.DRIVERS_LOCATION_REFERENCES);



                    addressList = geocoder.getFromLocation(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), 1);
                     CityName = addressList.get(0).getLocality();
                    Snackbar.make(mapFragment.getView(),CityName,Snackbar.LENGTH_LONG).show();

//                    driversLocationref

//                driversLocationref.child("Lahore");
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(getContext(),"Register Successfully",Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
////                });
//                    driversLocationref= FirebaseDatabase.getInstance().getReference(Common.DRIVERS_LOCATION_REFERENCES).child(CityName);

                    currentUserRef= driversLocationref.child(FirebaseAuth.getInstance().getUid());


                    geoFire = new GeoFire(currentUserRef);


                    //update Location

                    geoFire.setLocation(CityName,
                            new GeoLocation(locationResult.getLastLocation().getLatitude(),
                                    locationResult.getLastLocation().getLongitude()),
                            (Key ,error)-> {
                                if (error != null)
                                    Snackbar.make(mapFragment.getView(),error.getMessage(),Snackbar.LENGTH_LONG).show();

                            });
                    registeronlineSystem();
                } catch (IOException e) {
                    Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                }









            }
        };

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(getView(),"location permission requrid",Snackbar.LENGTH_SHORT).show();
            return;
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
//        fusedLocationProviderClient.getLastLocation()
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//
//                try {
////                    FirebaseDatabase database = FirebaseDatabase.getInstance();
////                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Common.DRIVERS_LOCATION_REFERENCES);
//
//                    //                /// here ,after get location,we willl get adress name
//                    Geocoder geocoder=new Geocoder(getContext(), Locale.getDefault());
//                    List<Address> addressList;
//                    addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
//                    String CityName = addressList.get(0).getLocality();
//                    Snackbar.make(mapFragment.getView(), CityName, Snackbar.LENGTH_LONG).show();
//
//
//                    driversLocationref = FirebaseDatabase.getInstance().getReference(Common.DRIVERS_LOCATION_REFERENCES)
//                            .child(CityName);
//
//                    currentUserRef = driversLocationref.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//
//                    geoFire = new GeoFire(driversLocationref);
//                } catch (Exception e) {
//                    Snackbar.make(mapFragment.getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
//                }
//            }
//        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            Snackbar.make(getView(),"location permission requrid",Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                            @Override
                            public boolean onMyLocationButtonClick() {
                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return false;
                                }
                                fusedLocationProviderClient.getLastLocation()
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                                            @Override
                                            public void onSuccess(Location location) {
                                                LatLng userLatling = new LatLng(location.getLatitude(), location.getLongitude());
                                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatling, 18f));
                                            }
                                        });
                            return true;
                            }
                        });

                            //SET lAYOUT
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
                        Toast.makeText(getContext(),"Permission"+permissionDeniedResponse.getPermissionName()+""+"was denied!" ,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
        try{
        boolean success= googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(),R.raw.uber_maps_style));
        if(!success)
            Log.e("wrong_error,","Style parse error");
        }catch (Resources.NotFoundException e)
        {
            Log.e("wrong_error",e.getMessage());
        }
        Snackbar.make(mapFragment.getView(), "You r online", Snackbar.LENGTH_LONG).show();
    }

}