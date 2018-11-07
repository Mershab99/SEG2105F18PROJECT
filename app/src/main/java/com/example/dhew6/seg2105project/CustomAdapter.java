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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Service> {

    private ArrayList<Service> serviceArrayList;
    Context context;

    public CustomAdapter(Context context, ArrayList<Service> item_list) {
        super(context, R.layout.list_row_item, item_list);
        this.context = context;
        this.serviceArrayList = item_list;
    }

    static class ViewHolder {
        public TextView rateRowTextView;
        public TextView serviceRowTextView;
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

        viewHolder.serviceRowTextView.setText("Service Name: " + service.getName());
        viewHolder.rateRowTextView.setText("Hourly Rate: $" + Double.toString(service.getRate()));

        return convertView;
    }
}

