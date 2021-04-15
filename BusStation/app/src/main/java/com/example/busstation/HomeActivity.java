package com.example.busstation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    GoogleMap map;

    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.myMap);

        mapFragment.getMapAsync(this::onMapReady);

        ListView listView = (ListView) findViewById(R.id.my_list);
        List<String> mylist = new ArrayList<>();
        mylist.add("Bus 56");
        mylist.add("Bus 6");

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist);
        listView.setAdapter(arrayAdapter);
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

    public  void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;
        LatLng ute = new LatLng(10.856622070058867, 106.77260894553581);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ute,20));
        googleMap.addMarker(new MarkerOptions()
                .position(ute)
                .title("Đại học Sư Phạm Kỹ Thuật TP.HCM"));
    }
}