package com.example.busstation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.busstation.controllers.BusAdapter;
import com.example.busstation.models.Buses;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ListView lvFavo;
    ArrayList<Buses> arrayBuses;
    BusAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        HomeNavigation.info(this.findViewById(R.id.tvNameUser),this.findViewById(R.id.tvEmail));
        drawerLayout=findViewById(R.id.drawer_layout);
        anhxa();
        adapter =new BusAdapter(this,R.layout.activity_list_bus,arrayBuses);
        lvFavo.setAdapter(adapter);
    }

    private void anhxa(){
        lvFavo = (ListView) findViewById(R.id.lvFavo);
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
        HomeNavigation.openDrawer(drawerLayout);
        HomeNavigation.redirectActivity(this,SearchBus.class);
    }

    public void ClickDashBoard(View view){
        recreate();
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