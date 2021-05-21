package com.example.busstation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.busstation.controllers.BusAdapter;
import com.example.busstation.models.Buses;

import java.util.ArrayList;

public class SearchBus extends AppCompatActivity {
    DrawerLayout drawerLayout;

    ListView lvBus;
    ArrayList<Buses> arrayBuses;
    BusAdapter adapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bus);
        anhxa();
        HomeNavigation.info(this.findViewById(R.id.tvNameUser),this.findViewById(R.id.tvEmail));
        drawerLayout=findViewById(R.id.drawer_layout);
        adapter =new BusAdapter(this,R.layout.activity_list_bus,arrayBuses);
        lvBus.setAdapter(adapter);
        filterSearchView();

    }
    private void filterSearchView(){
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    private void anhxa(){
        lvBus = (ListView) findViewById(R.id.lvBuses);
        arrayBuses = new ArrayList<>();
        arrayBuses.add(new Buses(R.drawable.bus_station,"06","GTVT-Chợ Lớn"));
        arrayBuses.add(new Buses(R.drawable.bus_station,"01","DHqg-Bến Thành"));
        arrayBuses.add(new Buses(R.drawable.bus_station,"02","BX miền tây-Chợ Lớn"));
        arrayBuses.add(new Buses(R.drawable.bus_station,"03","DH SPKT-Chợ Lớn"));
        arrayBuses.add(new Buses(R.drawable.bus_station,"04","KTX Khu B-BX Miền Đông"));
        arrayBuses.add(new Buses(R.drawable.bus_station,"05","GTVT-Chợ Lớn"));
    }



    public void ClickMenu(View view){
        HomeNavigation.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        HomeNavigation.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view){
        HomeNavigation.openDrawer(drawerLayout);
        HomeNavigation.redirectActivity(this,HomeNavigation.class);
    }

    public void ClickSearchBus(View view){
        recreate();
    }

    public void ClickDashBoard(View view){
        HomeNavigation.openDrawer(drawerLayout);
        HomeNavigation.redirectActivity(this,Dashboard.class);
    }

    public void ClickAboutUs(View view){
        HomeNavigation.openDrawer(drawerLayout);
        HomeNavigation.redirectActivity(this,AboutUs.class);
    }

    public void ClickSetUp(View view){
        HomeNavigation.openDrawer(drawerLayout);
        HomeNavigation.redirectActivity(this,AccountSetting.class);
    }

    public void ClickLogout(View view){
        HomeNavigation.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        HomeNavigation.closeDrawer(drawerLayout);
    }
}