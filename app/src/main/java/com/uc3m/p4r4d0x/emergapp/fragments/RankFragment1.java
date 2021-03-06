package com.uc3m.p4r4d0x.emergapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.uc3m.p4r4d0x.emergapp.R;
import com.uc3m.p4r4d0x.emergapp.RankingActivity;

/**
 * Created by Alvaro Loarte on 19/07/16.
 * Desc: this class extends a fragment from a TabLayout of Ranking activity
 *       Gets the first fragment (tab) of the TabLayout: XP ranking
 *
 */
public class RankFragment1 extends Fragment {
    View mView;
    String [][] data;

    int arraySize=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view=inflater.inflate(R.layout.fragment_ranking1,container,false);
        this.mView = view;
        setArgumentsOntoViews();
        return view;
    }


    /*
    * Desc:  Set the data from Activity into the array
    * */
    public void setArgumentsToFragment(String [][] data, int arraySize){
        this.arraySize=arraySize;
        this.data=data;
    }
    /*
    * Desc:  Using the data obtained from the activity, change the TextViews with the data retrieved from DDBB
    * */
    public void setArgumentsOntoViews(){
        //Iterate the array and write on the propper position
        for(int i=0; i< arraySize && i< data.length;i++){
            switch(i){
                //First one
                case 0:
                    changeText(data[i][0], R.id.tvFirstName);
                    changeText(data[i][1], R.id.tvFirstTitle);
                    changeText(data[i][2], R.id.tvFirstLevel);
                    changeText(data[i][3], R.id.tvFirstPoints);
                    if(data[i][4].compareTo("1")==0) changeStyle(R.id.trFirst);
                    break;
                //Second one
                case 1:
                    changeText(data[i][0], R.id.tvSecondName);
                    changeText(data[i][1], R.id.tvSecondTitle);
                    changeText(data[i][2], R.id.tvSecondLevel);
                    changeText(data[i][3], R.id.tvSecondPoints);
                    if(data[i][4].compareTo("1")==0) changeStyle(R.id.trSecond);
                    break;
                //third one
                case 2:
                    changeText(data[i][0], R.id.tvThirdName);
                    changeText(data[i][1], R.id.tvThirdTitle);
                    changeText(data[i][2], R.id.tvThirdLevel);
                    changeText(data[i][3], R.id.tvThirdPoints);
                    if(data[i][4].compareTo("1")==0) changeStyle(R.id.trThird);
                    break;
                //Forth
                case 3:
                    changeText(data[i][0], R.id.tvFourthName);
                    changeText(data[i][1], R.id.tvFourthTitle);
                    changeText(data[i][2], R.id.tvFourthLevel);
                    changeText(data[i][3], R.id.tvFourthPoints);
                    if(data[i][4].compareTo("1")==0) changeStyle(R.id.trFourth);
                    break;
                //Fifth
                case 4:
                    changeText(data[i][0], R.id.tvFifthName);
                    changeText(data[i][1], R.id.tvFifthTitle);
                    changeText(data[i][2], R.id.tvFifthLevel);
                    changeText(data[i][3], R.id.tvFifthPoints);
                    if(data[i][4].compareTo("1")==0) changeStyle(R.id.trFifth);
                    break;

            }

        }

    }

    /*
    * Desc:  change the text on the text view on the fragment
    * Param: String with the text to change and a int with the id of the text view
    * */
    public void changeText(String text, int idTextView)
    {
        //Get the text view
        TextView fragmentTextView;
        fragmentTextView = (TextView) mView.findViewById(idTextView);
        //Set text to it
        fragmentTextView.setText(text);

    }

    public void changeStyle(int idTableRow){
        TableRow trAtPosition= (TableRow) mView.findViewById(idTableRow);
        trAtPosition.setBackgroundColor(Color.parseColor("#f2f2f2"));

    }
}
