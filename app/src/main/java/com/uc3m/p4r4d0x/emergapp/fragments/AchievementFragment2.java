package com.uc3m.p4r4d0x.emergapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import com.uc3m.p4r4d0x.emergapp.R;

/**
 * Created by Alvaro Loarte on 19/07/16.
 * Desc: this class extends a fragment from a TabLayout of Achievements activity
 *       Gets the second fragment (tab) of the TabLayout: Expert Achievements
 *
 */
public class AchievementFragment2 extends Fragment {
    View mView;
    String [][] data;
    boolean achievements=false;
    int color=-1;

    int arraySize=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_achievem_2,container,false);
        this.mView = view;
        setArgumentsOntoViews();
        return view;
    }

    /*
    * Desc:  Set the data from Activity into the array
    * */
    public void setArgumentsToFragment(String [][] data, int arraySize,int colorSelected,boolean achievements){
        this.achievements=achievements;
        this.color=colorSelected;
        this.arraySize=arraySize;
        this.data=data;
    }
    /*
    * Desc:  Using the data obtained from the activity, change the TextViews with the data retrieved from DDBB
    * */
    public void setArgumentsOntoViews(){

        setVisibilityWholeLayout();

        //Iterate the array and write on the propper position
        for (int i = 0; i < arraySize && i < data.length; i++) {
            switch (i) {
                //Community Helper
                case 0:
                    changeProgressText(data[i][0], data[i][1], R.id.tvProgressExpert1);
                    changeImageAchievement(data[i][2], R.id.ivCompletedExpert1);
                    setVisibilityLL(R.id.llExpertAchievement1);
                    break;
                //Pictures Lover
                case 1:
                    changeProgressText(data[i][0], data[i][1], R.id.tvProgressExpert2);
                    changeImageAchievement(data[i][2], R.id.ivCompletedExpert2);
                    setVisibilityLL(R.id.llExpertAchievement2);
                    break;
                //Videos Lover
                case 2:
                    changeProgressText(data[i][0], data[i][1], R.id.tvProgressExpert3);
                    changeImageAchievement(data[i][2], R.id.ivCompletedExpert3);
                    setVisibilityLL(R.id.llExpertAchievement3);
                    break;
                //Quests Lover
                case 3:
                    changeProgressText(data[i][0], data[i][1], R.id.tvProgressExpert4);
                    changeImageAchievement(data[i][2], R.id.ivCompletedExpert4);
                    setVisibilityLL(R.id.llExpertAchievement4);
                    break;
                //Expert Reporter
                case 4:
                    changeImageAchievement(data[i][2], R.id.ivCompletedExpert5);
                    setVisibilityLL(R.id.llExpertAchievement5);
                    break;
                //Hard Worker
                case 5:
                    changeImageAchievement(data[i][2], R.id.ivCompletedExpert6);
                    setVisibilityLL(R.id.llExpertAchievement6);
                    break;
                //Top Reporter
                case 6:
                    changeImageAchievement(data[i][2], R.id.ivCompletedExpert7);
                    setVisibilityLL(R.id.llExpertAchievement7);
                    break;
                //Reporting Anywhere
                case 7:
                    changeProgressText(data[i][0], data[i][1], R.id.tvProgressExpert8);
                    changeImageAchievement(data[i][2], R.id.ivCompletedExpert8);
                    setVisibilityLL(R.id.llExpertAchievement8);
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
        RelativeLayout rl   = (RelativeLayout) mView.findViewById(R.id.rlUnlockedExpertAchievements);
        ScrollView sv       = (ScrollView) mView.findViewById(R.id.svExpert);

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
