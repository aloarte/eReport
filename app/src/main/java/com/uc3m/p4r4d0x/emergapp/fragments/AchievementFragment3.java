package com.uc3m.p4r4d0x.emergapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.uc3m.p4r4d0x.emergapp.R;

/**
 * Created by Alvaro Loarte on 19/07/16.
 * Desc: this class extends a fragment from a TabLayout of Achievements activity
 *       Gets the third fragment (tab) of the TabLayout: Secret Achievements
 *
 */
public class AchievementFragment3 extends Fragment {
    View mView;
    String [][] data;
    boolean achievements;

    int arraySize=0;
    int color=-1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_achievem_3,container,false);
        this.mView = view;
        setArgumentsOntoViews();
        return view;
    }

    /*
    * Desc:  Set the data from Activity into the array
    * */
    public void setArgumentsToFragment(String [][] data, int arraySize,int colorSelected,boolean achievements){
        this.arraySize=arraySize;
        this.color=colorSelected;
        this.data=data;
        this.achievements=achievements;

    }
    /*
    * Desc:  Using the data obtained from the activity, change the TextViews with the data retrieved from DDBB
    * */
    public void setArgumentsOntoViews(){

        setVisibilityWholeLayout();

        int displayed=-1;
        LinearLayout auxLL;
        //Iterate the array and write on the propper position
        for (int i = 0; i < arraySize && i < data.length; i++) {
            displayed = Integer.parseInt(data[i][3]);
            switch (i) {
                //Seeker of Truth
                case 0:
                    auxLL = (LinearLayout) mView.findViewById(R.id.llSecretAchievement1);
                    if (displayed != 0) {
                        auxLL.setVisibility(View.VISIBLE);
                        changeProgressText(data[i][0], data[i][1], R.id.tvProgressSecret1);
                        changeImageAchievement(data[i][2], R.id.ivCompletedSecret1);
                    } else {
                        auxLL.setVisibility(View.GONE);
                    }
                    setVisibilityLL(R.id.llSecretAchievement1);
                    break;
                //I give my best
                case 1:
                    auxLL = (LinearLayout) mView.findViewById(R.id.llSecretAchievement2);
                    if (displayed != 0) {
                        auxLL.setVisibility(View.VISIBLE);
                        changeImageAchievement(data[i][2], R.id.ivCompletedSecret2);
                    } else {
                        auxLL.setVisibility(View.GONE);
                    }
                    break;
                //An image is worth more than 1000 words
                case 2:
                    auxLL = (LinearLayout) mView.findViewById(R.id.llSecretAchievement3);
                    if (displayed != 0) {
                        auxLL.setVisibility(View.VISIBLE);
                        changeImageAchievement(data[i][2], R.id.ivCompletedSecret3);
                    } else {
                        auxLL.setVisibility(View.GONE);
                    }
                    break;
                //As fast as I can
                case 3:
                    auxLL = (LinearLayout) mView.findViewById(R.id.llSecretAchievement4);
                    if (displayed != 0) {
                        auxLL.setVisibility(View.VISIBLE);
                        changeImageAchievement(data[i][2], R.id.ivCompletedSecret4);
                    } else {
                        auxLL.setVisibility(View.GONE);
                    }
                    break;
                //Personal image is allways the first
                case 4:
                    auxLL = (LinearLayout) mView.findViewById(R.id.llSecretAchievement5);
                    if (displayed != 0) {
                        auxLL.setVisibility(View.VISIBLE);
                        changeImageAchievement(data[i][2], R.id.ivCompletedSecret5);
                    } else {
                        auxLL.setVisibility(View.GONE);
                    }
                    break;
                //First my neighborhood
                case 5:
                    auxLL = (LinearLayout) mView.findViewById(R.id.llSecretAchievement6);
                    if (displayed != 0) {
                        auxLL.setVisibility(View.VISIBLE);
                        changeProgressText(data[i][0], data[i][1], R.id.tvProgressSecret6);
                        changeImageAchievement(data[i][2], R.id.ivCompletedSecret6);
                    } else {
                        auxLL.setVisibility(View.GONE);
                    }
                    break;
            }
        }

    }

    /*
    * Desc:  change the text on the text view on the fragment
    * Param: String with the text to change and a int with the id of the text view
    * */
    public void changeProgressText(String progress,String maxProgress, int idTextView)
    {
        //Get the text view
        TextView fragmentTextView;
        fragmentTextView = (TextView) mView.findViewById(idTextView);
        String text= ""+progress+"/"+maxProgress;
        //Set text to it
        fragmentTextView.setText(text);

    }

    /*
    * Desc:  change the image achievement to completed
    * Param: id of the text view
    * */
    public void changeImageAchievement(String achievementUnlocked, int idTextView)
    {
        int isAchievementUnlocked=Integer.parseInt(achievementUnlocked);
        int resourceID;
        if(isAchievementUnlocked==0){
            resourceID=R.mipmap.notdoneicon;
        }
        else if(isAchievementUnlocked==1){
            switch(color){
                //DefaultColor
                case 0:
                    resourceID=R.mipmap.doneicon_ereporter;
                    break;
                //Red
                case 1:
                    resourceID=R.mipmap.doneicon_red;
                    break;
                //Blue
                case 2:
                    resourceID=R.mipmap.doneicon_blue;
                    break;
                //Green
                case 3:
                    resourceID=R.mipmap.doneicon_green;
                    break;
                //Purple
                case 4:
                    resourceID=R.mipmap.doneicon_purple;
                    break;
                //Yellow
                case 5:
                    resourceID=R.mipmap.doneicon_yellow;
                    break;
                //Pink
                case 6:
                    resourceID=R.mipmap.doneicon_pink;
                    break;
                //Grey
                case 7:
                    resourceID=R.mipmap.doneicon_grey;
                    break;
                default:
                    resourceID=R.mipmap.doneicon;
                    break;
            }
        }
        else{
            resourceID=R.mipmap.notdoneicon;
        }

        //Get the text view
        ImageView fragmentImageView;
        fragmentImageView = (ImageView) mView.findViewById(idTextView);
        //Set text to it
        fragmentImageView.setImageResource(resourceID);

    }

    /*
    * Desc:  change the visibility of the layout of each achievement
    * Param: int with the id of the achievement
    * */
    public void setVisibilityLL(int id) {
        LinearLayout tr = (LinearLayout) mView.findViewById(id);
        if (achievements) {
            tr.setVisibility(View.VISIBLE);
        }
        else{
            tr.setVisibility(View.GONE);
        }
    }

    /*
    * Desc: Change the whole visibility of the screen if the novel achievements are completed
    * */
    public void setVisibilityWholeLayout(){
        RelativeLayout rl   = (RelativeLayout) mView.findViewById(R.id.rlUnlockedSecretAchievements);
        ScrollView sv       = (ScrollView) mView.findViewById(R.id.svSecret);

        if (achievements) {
            rl.setVisibility(View.GONE);
            sv.setVisibility(View.VISIBLE);
        }
        else{
            rl.setVisibility(View.VISIBLE);
            sv.setVisibility(View.GONE);

        }

    }
}
