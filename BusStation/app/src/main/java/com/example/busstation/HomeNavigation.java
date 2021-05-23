package com.example.busstation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busstation.controllers.MapController;
import com.example.busstation.controllers.SharedPreferencesController;
import com.example.busstation.models.BusStop;
import com.example.busstation.models.Buses;
import com.example.busstation.models.Busstop_Detail;
import com.example.busstation.models.User;
import com.example.busstation.services.BusStopService;
import com.example.busstation.services.BusesService;
import com.example.busstation.services.GoogleLocationService;
import com.example.busstation.services.RetrofitService;
import com.example.busstation.services.UserService;
import com.example.busstation.services.directionService.FetchURL;
import com.example.busstation.services.directionService.TaskLoadedCallback;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeNavigation extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, TaskLoadedCallback {

    DrawerLayout drawerLayout;
    GoogleMap map;
    Location myLocation = null;
    Location destinationLocation = null;
    protected LatLng start = null;
    protected LatLng end = null;
    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission = false;

    public static SharedPreferences.Editor editor;
    public static Context context;

    ImageView imgSearch;
    //autocomplete view
    List<String> autocompleteStrings;
    ArrayAdapter<String> searchAdapter;
    AutoCompleteTextView searchView;

    //test
    Polyline currentPolyline;
    LatLng place1;
    LatLng place2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_navigation);
        context = this;

        //set animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation);
        findViewById(R.id.imageView).startAnimation(animation);
        //anh xa
        anhxa();
        info(this.findViewById(R.id.tvNameUser), this.findViewById(R.id.tvEmail));
        requestPermision();
        //test direction api
        place1 = new LatLng(10.850858365517317, 106.77197594527688);
        place2 = new LatLng(10.947964488200222, 106.82867741206697);
        //google map
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.myMap);
        mapFragment.getMapAsync(this::onMapReady);
        //set autocomplete
        autocompleteStrings = new ArrayList<>();
        uploadAutoComplete();

        imgSearch.setOnClickListener(v -> {
            String str = searchView.getText().toString();
            Log.d("kiemtra", "onCreate: " +str);
            RetrofitService.create(BusStopService.class).searchBusStop(str).enqueue(new Callback<Busstop_Detail>() {
                @Override
                public void onResponse(Call<Busstop_Detail> call, Response<Busstop_Detail> response) {
                    if (response.body() == null) {
                        Toast.makeText(getApplicationContext(), "not found", Toast.LENGTH_SHORT).show();
                        Log.d("kiemtra", "be: " + "null");
                        return;
                    }
                    Log.d("kiemtra", "onResponse: " + response.body().get_id());
                    SharedPreferencesController.setStringValue(getApplicationContext(), "modeFollow", "busstop");
                    SharedPreferencesController.setStringValue(getApplicationContext(), "followIdItem", response.body().get_id());
                    UpLoadMarker();
                }

                @Override
                public void onFailure(Call<Busstop_Detail> call, Throwable t) {

                }
            });
        });
    }

    //google map direction api
    @Override
    public void onTaskDone(Object... values){

        map.clear();
        map.addMarker(new MarkerOptions()
                .position(place1)
                .title("Bệnh viện ITO") );
        map.addMarker(new MarkerOptions()
                .position(place1)
                .title("Trường Đại Họ Sư Phạm Kỹ Thuật") );
        currentPolyline = map.addPolyline((PolylineOptions) values[0]);
        currentPolyline.setColor(R.color.black);
    }
    public String getUrl(LatLng origin, LatLng dest, String directionMode){
        // origin of router
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // dest of router
        String str_dest = "destination=" + dest.latitude +"," +dest.longitude;
        // mode
        String mode = "mode="+ directionMode;

        String parameter = str_origin +"&"+ str_dest +"&"+ mode;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/"+ output + "?" + parameter + "&key=" + getString(R.string.map_api_key);
        return url;
    }

    public void uploadAutoComplete() {
        RetrofitService.create(BusStopService.class).getName(searchView.getText().toString()).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.body() == null || response.body().size() == 0) {
                    autocompleteStrings.clear();
                    autocompleteStrings.add("err");
                    searchAdapter.notifyDataSetChanged();
                    return;
                }
                autocompleteStrings = response.body();
                searchAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, autocompleteStrings);
                searchView.setAdapter(searchAdapter);

            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
    }

    public static void info(TextView nameUser, TextView emailUser) {
        RetrofitService.create(UserService.class).getUser(SharedPreferencesController.getStringValueByKey(context, "userAuthId")).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() == null) {
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
        if (!SharedPreferencesController.getBooleanValueByKey(this, "myPosition")) return;
        getMyLocation();
    }

    private void UpLoadMarker() {
        String mode = SharedPreferencesController.getStringValueByKey(this, "modeFollow");
        String idSearch = SharedPreferencesController.getStringValueByKey(this, "followIdItem");
        Log.d("kiemtra: ", mode + "");
        if (mode == null) {
            RetrofitService.create(BusStopService.class).getAllBusStops().enqueue(new Callback<List<BusStop>>() {
                @Override
                public void onResponse(Call<List<BusStop>> call, Response<List<BusStop>> response) {
                    map.clear();
                    List<BusStop> busStopList = response.body();
                    for (BusStop busStop : busStopList) {
                        if (!busStop.getName().equals("point"))
                            MapController.AddMarker(getApplicationContext(), map, busStop.getName(), busStop.getLocationName(), new LatLng(busStop.getLatitude(), busStop.getLongitude()));
                    }
//                findViewById(R.id.loadingLayout).setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<List<BusStop>> call, Throwable t) {

                }
            });
            return;
        }

        if (mode.equals("buses")) {
            RetrofitService.create(BusesService.class).GetByID(idSearch).enqueue(new Callback<Buses>() {
                @Override
                public void onResponse(Call<Buses> call, Response<Buses> response) {
                    List<BusStop> busStops = response.body().getBusstops();
                    if(busStops.size() == 0) return;
                    map.clear();

                    List<LatLng> points = new ArrayList<>();
                    for (int i = 0; i < busStops.size(); i++) {
                        points.add(new LatLng(busStops.get(i).getLatitude(), busStops.get(i).getLongitude()));
                    }
                    Polyline polyline = map.addPolyline(new PolylineOptions().clickable(true).addAll(points));
                    MapController.CameraUpdate(map, new LatLng(busStops.get(0).getLatitude(), busStops.get(0).getLongitude()));
                    for (BusStop busStop : busStops) {
                        if (!busStop.getName().equals("point"))
                            MapController.AddMarker(getApplicationContext(), map, busStop.getName(), busStop.getLocationName(), new LatLng(busStop.getLatitude(), busStop.getLongitude()));
                    }
                }

                @Override
                public void onFailure(Call<Buses> call, Throwable t) {
                    Log.d("kiemtra", "onFailure: " + t.getMessage());
                }
            });
            return;
        }
        if(mode.equals("busstop")){
            RetrofitService.create(BusStopService.class).getByID(idSearch).enqueue(new Callback<Busstop_Detail>() {
                @Override
                public void onResponse(Call<Busstop_Detail> call, Response<Busstop_Detail> response) {
                    map.clear();
                    Busstop_Detail busstop = response.body();
                    MapController.CameraUpdate(map, new LatLng(busstop.getLatitude(), busstop.getLongitude()));
                    MapController.AddMarker(getApplicationContext(), map, busstop.getName(),busstop.getLocationName(), new LatLng(busstop.getLatitude(), busstop.getLongitude()));

                }

                @Override
                public void onFailure(Call<Busstop_Detail> call, Throwable t) {
                }
            });
        }
        if(mode.equals("router")){
            MapController.CameraUpdate(map,place1);
            GoogleLocationService.getAddress(SharedPreferencesController.getStringValueByKey(getApplicationContext(),"origin"),getApplicationContext(),new GeoHandler());
            String url = getUrl(place1,place2,"driving");
            new FetchURL(this).execute(url, "driving");
        }
    }

    private void anhxa() {
        imgSearch = this.findViewById(R.id.btnSearch);
        searchView = this.findViewById(R.id.searchView);
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
        Intent i = new Intent(this, SearchBus.class);
        startActivity(i);
    }

    public void clickSearchRoute(View view) {
        Intent i = new Intent(this, SearchRoute.class);
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

    private class GeoHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            Double latitude = null, longitude = null;
            switch(msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    latitude = bundle.getDouble("la");
                    longitude = bundle.getDouble("log");
                    break;
                default:
                    break;
            }
            LatLng latLng = new LatLng(latitude,longitude);
            Log.d("kiemtra4", "handleMessage: "+ latLng.longitude + latLng.latitude);;
        }
    }
}