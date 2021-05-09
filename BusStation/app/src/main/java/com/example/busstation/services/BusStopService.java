package com.example.busstation.services;

import com.example.busstation.models.BusStop;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BusStopService {

    @GET("busstops")
    Call<List<BusStop>> getAllBusStops();

}
