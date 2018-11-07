package com.example.dhew6.seg2105project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Service> {

    private ArrayList<Service> serviceArrayList;
    private ArrayList<Service> searchList;
    Context context;

    public CustomAdapter(Context context, ArrayList<Service> item_list) {
        super(context, R.layout.list_row_item, item_list);
        this.context = context;
        this.serviceArrayList = item_list;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(item_list);
    }

    static class ViewHolder {
        public TextView rateRowTextView;
        public TextView serviceRowTextView;
    }

    @Override
    public int getCount() {
        return serviceArrayList.size();
    }

    @Nullable
    @Override
    public Service getItem(int position) {
        return serviceArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(int position) {
        serviceArrayList.remove(position);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView,  @NonNull ViewGroup parent) {

        Service service = serviceArrayList.get(position);
        ViewHolder viewHolder;
        final View result;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_row_item, parent, false);
            viewHolder.rateRowTextView = convertView.findViewById(R.id.rateRowTextView);
            viewHolder.serviceRowTextView = convertView.findViewById(R.id.serviceRowTextView);

            result = convertView;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        String format2 = new DecimalFormat("#,###.00").format(service.getRate());
        viewHolder.serviceRowTextView.setText("Service Name: " + service.getName());
        viewHolder.rateRowTextView.setText("Hourly Rate: $" + format2);

        return convertView;
    }

    public void filter(String s){

        String next = s.toLowerCase();
        serviceArrayList.clear();
        if(next.length() == 0){
            serviceArrayList.addAll(searchList);
        } else {
            for(Service service : searchList){
                if(service.getName().toLowerCase().contains(next)){
                    serviceArrayList.add(service);
                }
            }
        }
        notifyDataSetChanged();
    }

}

