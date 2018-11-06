package com.example.dhew6.seg2105project;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
        public ImageView deleteImageView;
        public ImageView editImageView;
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
            viewHolder.deleteImageView = convertView.findViewById(R.id.deleteImageView);
            viewHolder.editImageView = convertView.findViewById(R.id.editImageView);

            result = convertView;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.serviceRowTextView.setText(service.getName());
        viewHolder.rateRowTextView.setText(Double.toString(service.getRate()));

        viewHolder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceArrayList.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}

