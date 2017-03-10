package com.uc3m.p4r4d0x.emergapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alvaro Loarte Rodríguez on 21/07/16.
 *
 */
public class SpinnerAdapter extends ArrayAdapter<ItemSpinnerData> {

    int groupId;
    ArrayList<ItemSpinnerData> list;
    LayoutInflater inflater;
    Activity context;

    /*
    * Desc: Regular Constructor
    * */
    public SpinnerAdapter(Activity context, int groupId,int id, ArrayList<ItemSpinnerData> list) {
        super(context, id, list);

        this.list=list;
        this. groupId=groupId;
        //Build the LayoutInflater using the service LAYOUT_INFLATER_SERVICE
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /*
     * Desc: this method get each view element of the spinner
     * Ret:  the requested view
     * */
    public View getView(int position, View convertView, ViewGroup parent){
        View itemView=inflater.inflate(groupId,parent,false);
        ImageView imageView= (ImageView)itemView.findViewById(R.id.ivSpinnerAccountConfiguration);
        imageView.setImageResource(list.get(position).getImageId());

        TextView textView=(TextView) itemView.findViewById(R.id.tvSpinnerAccountConfiguration);
        textView.setText(list.get(position).getText());

        return itemView;
    }

    /*
    * Desc: this method is called when the element is selected
    * Ret:  the requested view
    * */
    public View getDropDownView(int position, View convertView, ViewGroup parent){
        return getView(position,convertView,parent);
    }

}
