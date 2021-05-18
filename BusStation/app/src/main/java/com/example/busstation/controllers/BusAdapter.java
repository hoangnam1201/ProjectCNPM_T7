package com.example.busstation.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import com.example.busstation.R;
import com.example.busstation.models.Buses;

import java.util.ArrayList;
import java.util.List;

public class BusAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private int layout;
    private List<Buses> busList;
    private List<Buses> busListOld;

    public BusAdapter(Context context, int layout, List<Buses> busList) {
        this.context = context;
        this.layout = layout;
        this.busList = busList;
        this.busListOld = busList;
    }

    @Override
    public int getCount() {
        return busList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = layoutInflater.inflate(layout,null);
        TextView txtMaSo = (TextView) convertView.findViewById(R.id.textViewMaSo);
        TextView txtTuyenXe = (TextView) convertView.findViewById(R.id.textViewBus);
        ImageView imgBus = (ImageView) convertView.findViewById(R.id.imgHinh);
        Buses buses = busList.get(position);
        txtMaSo.setText(buses.getMaso());
        txtTuyenXe.setText(buses.getName());
        imgBus.setImageResource(buses.getImage());
        return convertView;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if(strSearch.isEmpty()){
                    busList = busListOld;
                }
                else {
                    List<Buses> list = new ArrayList<>();
                    for(Buses buses: busListOld){
                        if(buses.getName().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(buses);
                        }
                        if(buses.getMaso().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(buses);
                        }
                    }
                    busList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values=busList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                busList = (List<Buses>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
