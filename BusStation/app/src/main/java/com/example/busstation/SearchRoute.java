package com.example.busstation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class SearchRoute extends AppCompatActivity {
    EditText editText1, editText2;
    Button btnSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_route2);
        anhxa();
//        Log.d("kiemtra", "aaaaaa");
        Places.initialize(getApplicationContext(),"AIzaSyBEADdfs8ZHdOC7DnDs0liTZ154NpW-Hh0");
        editText1.setFocusable(false);
        editText1.setOnClickListener(v -> {
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                    Place.Field.LAT_LNG,Place.Field.NAME);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                    fieldList).build(SearchRoute.this);
            startActivityForResult(intent,100);
        });
        editText2.setFocusable(false);
        editText2.setOnClickListener(v -> {
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                    Place.Field.LAT_LNG,Place.Field.NAME);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                    fieldList).build(SearchRoute.this);
            startActivityForResult(intent,100);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            editText1.setText(place.getAddress());
            editText2.setText(place.getAddress());
        }
        else if (resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_SHORT).show();

        }
    }

    private void anhxa(){
        editText1 = (EditText) findViewById(R.id.searchAdress1);
        editText2 = (EditText) findViewById(R.id.searchAdress2);
        btnSearch = (Button) findViewById(R.id.btnTimDuong);

    }
}