package com.example.busstation;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.busstation.models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class HomeNavigation extends AppCompatActivity {

    DrawerLayout drawerLayout;
    GoogleMap map;

    ArrayAdapter<String> arrayAdapter;

    public static SharedPreferences sharedpreferences;
    public static SharedPreferences.Editor editor;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_navigation);

        //shared preferences
        sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        //anh xa
        anhxa();

        //google map
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.myMap);
        mapFragment.getMapAsync(this::onMapReady);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here!!");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);

                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;
        LatLng ute = new LatLng(10.856622070058867, 106.77260894553581);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ute,20));
        googleMap.addMarker(new MarkerOptions()
                .position(ute)
                .title("Đại học Sư Phạm Kỹ Thuật TP.HCM"));
    }

    private void anhxa() {
        drawerLayout = findViewById(R.id.drawer_layout);
    }

    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer((GravityCompat.START));
    }

    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen((GravityCompat.START))){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        recreate();
    }

    public void ClickSearchBus(View view){ redirectActivity(this,SearchBus.class); }

    public void ClickDashBoard(View view){
        redirectActivity(this,Dashboard.class);
    }

    public void ClickAboutUs(View view){
        redirectActivity(this,AboutUs.class);

    }

    public void ClickSetUp(View view) {
        redirectActivity(this,AccountSetting.class);
    }

    public void ClickLogout(View view){
        logout(this);
    }

    public static void  logout(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Log out");
        builder.setMessage("Are you sure want to log out ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                activity.finishAffinity();
//                System.exit(0);
                editor.clear();
                editor.commit();
                redirectActivity(activity, MainActivity.class);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity,aClass);

        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}