package com.uc3m.p4r4d0x.emergapp.receivers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uc3m.p4r4d0x.emergapp.EmergencyActivity;
import com.uc3m.p4r4d0x.emergapp.R;
import com.uc3m.p4r4d0x.emergapp.helpers.Constants;


/**
 * Created by Alvaro Loarte Rodriguez on 30/03/16.
 * Desc: Object that receive the result from the FetchAddress service and prints the result on TextViews
 */

public class ResultReceiverSentReady extends ResultReceiver {
        private Receiver mReceiver=null;
        private RelativeLayout rlSendingMessage,rlReloadInitialScreen;
        private LinearLayout llAfterReport;
        private ImageView ivRotate;
        private Context contextRR;
        private String resultMessage="";



    /*
    * Param: Handler and the TextViews for printing the address and the latitude and longitude
    * Desc: Main Constructor
    * */
    public ResultReceiverSentReady(android.os.Handler handler,LinearLayout layoutAfterSend,RelativeLayout layoutSendingMessage,RelativeLayout layoutReloadScreen,ImageView ivparam,Context context,int idToRotate) {
        super(handler);
        rlSendingMessage      = layoutSendingMessage;
        rlReloadInitialScreen = layoutReloadScreen;
        llAfterReport         = layoutAfterSend;
        contextRR             = context;
        ivRotate              = ivparam;

        //Create and load the animation
        Animation rotateAnimation;
        rotateAnimation= AnimationUtils.loadAnimation(contextRR, idToRotate);
        //Reset and start it
        rotateAnimation.reset();
        ivRotate.startAnimation(rotateAnimation);

    }

    /*
    * Desc: Neccesary interface for onReceiveResult method
    * */
    public interface Receiver {
            void onReceiveResult(int resultCode, Bundle resultData);
    }

   /*
   * Param: resultCode and resultData obtained from the operation from FetchAddressService
   * Desc:  Overrided method called when any result on the ResultReceiver is obtained
   *        Check the value and call setView function for printing the address
   * */
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {


        int ap=0,xp=0,achievementCompleted=-1;
        String username="";
        //Check if the method was called proper
        if(mReceiver != null) {
            mReceiver.onReceiveResult(resultCode,resultData);
        }
        else {

            // Get the address and the latitude and longitude from the resultData object
            resultMessage = resultData.getString(Constants.RESULT_DATA_KEY);
            ap=resultData.getInt(Constants.RESULT_AP_KEY);
            xp=resultData.getInt(Constants.RESULT_XP_KEY);
            achievementCompleted=resultData.getInt(Constants.RESULT_ACHIEVEMENT_COMPLETED);

            //Toast the result
            toastMessages(ap,xp,achievementCompleted);
            if(resultCode==2){
                //Stay for a new send
                //Hide the layer
                llAfterReport.setVisibility(View.INVISIBLE);
            }
            else if (resultCode==1){
                rlSendingMessage.setVisibility(View.INVISIBLE);
                rlReloadInitialScreen.setVisibility(View.VISIBLE);
            }
        }
    }

    public void toastMessages(int ap,int xp,int achievement){
        //Toast message status
        Toast.makeText(contextRR, resultMessage
                + " : "
                +xp + " XP", Toast.LENGTH_LONG).show();
        if(achievement==1){
            Toast.makeText(contextRR,"Achievement completed", Toast.LENGTH_LONG).show();
        }


    }


    }

