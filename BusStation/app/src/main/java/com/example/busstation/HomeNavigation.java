package com.example.busstation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.busstation.controllers.MapController;
import com.example.busstation.controllers.SharedPreferencesController;
import com.example.busstation.models.BusStop;
import com.example.busstation.models.User;
import com.example.busstation.services.BusStopService;
import com.example.busstation.services.RetrofitService;
import com.example.busstation.services.UserService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeNavigation extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {

    DrawerLayout drawerLayout;
    GoogleMap map;
    Location myLocation = null;
    Location destinationLocation = null;
    protected LatLng start = null;
    protected LatLng end = null;
    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission = false;
    ArrayAdapter<String> arrayAdapter;

    public static SharedPreferences sharedpreferences;
    public static SharedPreferences.Editor editor;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_navigation);
        context = this;
        //shared preferences
        sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        //set animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation);
        findViewById(R.id.imageView).startAnimation(animation);
        //anh xa
        anhxa();
        info(this.findViewById(R.id.tvNameUser),this.findViewById(R.id.tvEmail));
        requestPermision();
        //google map
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.myMap);
        mapFragment.getMapAsync(this::onMapReady);

    }

    public static void info(TextView nameUser, TextView emailUser){
        RetrofitService.create(UserService.class).getUser(SharedPreferencesController.getStringValueByKey(context,"userAuthId")).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body()==null){
                    return;
                }
                nameUser.setText(response.body().getFullname());
                emailUser.setText(response.body().getEmail());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
    private void requestPermision() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {
            locationPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //if permission granted.
                    locationPermission = true;
                    getMyLocation();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    //to get user location
    private void getMyLocation() {
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
        map.setMyLocationEnabled(true);
        map.setOnMyLocationChangeListener(location -> {

            myLocation = location;
            LatLng ltlng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    ltlng, 16f);
            map.animateCamera(cameraUpdate);
        });
    }


    public void onMapReady(GoogleMap googleMap) {
        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        } catch (Resources.NotFoundException e) {

        }
        map = googleMap;
        LatLng ute = new LatLng(10.850811354434544, 106.77201747036543);
        UpLoadMarker();
        map.clear();
        getMyLocation();
    }

    private void UpLoadMarker() {
//        findViewById(R.id.loadingLayout).setVisibility(View.VISIBLE);
        RetrofitService.create(BusStopService.class).getAllBusStops().enqueue(new Callback<List<BusStop>>() {
            @Override
            public void onResponse(Call<List<BusStop>> call, Response<List<BusStop>> response) {
                map.clear();
                List<BusStop> busStopList = response.body();
                for (BusStop busStop : busStopList) {
                    MapController.AddMarker(getApplicationContext(), map, busStop.getName(),busStop.getLocationName(), new LatLng(busStop.getLatitude(),busStop.getLongitude()));
                }
//                findViewById(R.id.loadingLayout).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<BusStop>> call, Throwable t) {

            }
        });
    }

    private void anhxa() {
        drawerLayout = findViewById(R.id.drawer_layout);
    }

    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer((GravityCompat.START));
    }

    public void ClickLogo(View view) {
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen((GravityCompat.START))) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view) {
        recreate();
    }

    public void ClickSearchBus(View view) {
       Intent i = new Intent(this,SearchBus.class);
       startActivity(i);
    }
    public void clickSearchRoute(View view){
            Intent i = new Intent(this,SearchRoute.class);
        startActivity(i);
    }
    public void ClickDashBoard(View view) {
        redirectActivity(this, Dashboard.class);
    }

    public void ClickAboutUs(View view) {
        redirectActivity(this, AboutUs.class);

    }

    public void ClickSetUp(View view) {
        redirectActivity(this, AccountSetting.class);
    }

    public void ClickLogout(View view) {
        logout(this);
    }

    public static void logout(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Log out");
        builder.setMessage("Are you sure want to log out ?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            editor.clear();
            editor.commit();
            redirectActivity(activity, MainActivity.class);
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);

        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}