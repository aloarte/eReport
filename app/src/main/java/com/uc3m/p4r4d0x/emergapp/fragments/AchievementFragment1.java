package com.uc3m.p4r4d0x.emergapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uc3m.p4r4d0x.emergapp.R;

/**
 * Created by Alvaro Loarte on 19/07/16.
 * Desc: this class extends a fragment from a TabLayout of Achievements activity
 *       Gets the first fragment (tab) of the TabLayout: Novel Achievements
 *
 */
public class AchievementFragment1 extends Fragment {
    View mView;
    String [][] data;

    int arraySize=0;
    int color=-1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_achievem_1,container,false);
        this.mView = view;
        setArgumentsOntoViews();
        return view;
    }

    /*
    * Desc:  Set the data from Activity into the array
    * */
    public void setArgumentsToFragment(String [][] data, int arraySize,int colorSelected){
        this.arraySize=arraySize;
        this.data=data;
        this.color=colorSelected;
    }
    /*
    * Desc:  Using the data obtained from the activity, change the TextViews with the data retrieved from DDBB
    * */
    public void setArgumentsOntoViews(){
        //Iterate the array and write on the propper position
        for(int i=0; i< arraySize && i< data.length;i++){
            switch(i){
                //First Steps
                case 0:
                    changeProgressText(data[i][0],data[i][1], R.id.tvProgressNovel1);
                    changeImageAchievement(data[i][2], R.id.ivCompletedNovel1);
                    break;
                //Photo Editor
                case 1:
                    changeImageAchievement(data[i][2], R.id.ivCompletedNovel2);
                    break;
                //Video Editor
                case 2:
                    changeImageAchievement(data[i][2], R.id.ivCompletedNovel3);
                    break;
                //Message Editor
                case 3:
                    changeImageAchievement(data[i][2], R.id.ivCompletedNovel4);
                    break;
                //Ubication Editor
                case 4:
                    changeImageAchievement(data[i][2], R.id.ivCompletedNovel5);
                    break;
                //Reporter
                case 5:
                    changeImageAchievement(data[i][2], R.id.ivCompletedNovel6);
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
}
