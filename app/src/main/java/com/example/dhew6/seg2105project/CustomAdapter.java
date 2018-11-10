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

    /**
     * initializes all values
     *
     * @param context
     * @param item_list
     */
    public CustomAdapter(Context context, ArrayList<Service> item_list) {
        super(context, R.layout.list_row_item, item_list);
        this.context = context;
        this.serviceArrayList = item_list;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(item_list);
    }

    /**
     * helper class
     */
    static class ViewHolder {
        public TextView rateRowTextView;
        public TextView serviceRowTextView;
    }

    /**
     * gets the size of values within the CustomAdapter
     * @return {int}
     */
    @Override
    public int getCount() {
        return serviceArrayList.size();
    }

    /**
     *
     * gets item within the CustomAdapter serviceArrayList given an index value : position
     *
     * @param position
     * @return
     */
    @Nullable
    @Override
    public Service getItem(int position) {
        return serviceArrayList.get(position);
    }

    /**
     * get's itemId, as of right now just returns the value, but this function may be of use later if
     * the indexer changes
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * removes value from the serviceArrayList
     *
     * @param position
     */
    public void remove(int position) {
        serviceArrayList.remove(position);
        notifyDataSetChanged();
    }


    /**
     * gets the view of the CustomAdapter in order to be rendered elsewhere
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView,  @NonNull ViewGroup parent) {

        Service service = serviceArrayList.get(position);
        ViewHolder viewHolder;
        final View result;

        if(convertView == null){
            //if convertView isn't provided create a viewholder and set convertView to an inflater with a layout of list_row_item

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_row_item, parent, false);
            viewHolder.rateRowTextView = convertView.findViewById(R.id.rateRowTextView);
            viewHolder.serviceRowTextView = convertView.findViewById(R.id.serviceRowTextView);

            result = convertView;
            convertView.setTag(viewHolder);
        }else{
            //gets viewHolder from convertView
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        //formats the rate into human readable form
        String format2 = new DecimalFormat("#,###.00").format(service.getRate());
        viewHolder.serviceRowTextView.setText("Service Name: " + service.getName());
        viewHolder.rateRowTextView.setText("Hourly Rate: $" + format2);

        return convertView;
    }

    /**
     * filters a String s
     *
     * @param s
     */
    public void filter(String s){

        String next = s.toLowerCase();
        serviceArrayList.clear();
        if(next.length() == 0){
            //if the search query is empty simply add searchList
            serviceArrayList.addAll(searchList);
        } else {
            //if its not empty iterate through searchlist and check which contains the String next
            //this is a very basic way of searching, but is sufficient for our application
            for(Service service : searchList){
                if(service.getName().toLowerCase().contains(next)){
                    serviceArrayList.add(service);
                }
            }
        }
        notifyDataSetChanged();
    }

}

