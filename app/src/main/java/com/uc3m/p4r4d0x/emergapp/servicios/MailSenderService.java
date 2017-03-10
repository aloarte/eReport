package com.uc3m.p4r4d0x.emergapp.servicios;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.uc3m.p4r4d0x.emergapp.helpers.Constants;
//import com.uc3m.p4r4d0x.emergapp.helpers.GMailSender;
import com.uc3m.p4r4d0x.emergapp.receivers.ResultReceiverSentReady;

/**
 * Created by Alvaro Loarte Rodriguez on 17/05/16..
 * Desc: Mail Sender service: gets the info from the EmergencyActivity (the main message, GPS position, pictures and videos)
 *       and send it by mail.
 *
 */

public class MailSenderService extends Service {
    private Context sContext;
    protected ResultReceiver mSender;
    private int xp,ap,achievement;
    final String MyPREFERENCES="userPreferencesG1";
    SharedPreferences sharedpreferences;
    public MailSenderService(Context c,ResultReceiverSentReady mReceiver,int ap,int xp,int achievement) {
        this.ap=ap;
        this.xp=xp;
        this.achievement=achievement;
        mSender=mReceiver;
    }


    @Override
    public void onCreate() {

        super.onCreate();
    }


    /*
    * desc: This method performs in an asyncr mode a call to GMailSender.sendMail method.
    *       Asynch is required because the operation may take a while
    * par: toSendPicturesLocation toSendvideosLocation : array strings with the location in the phone of the pictures/videos
    *       toSendMessage, toSendGPSStreet, toSendGPSCoord : strings with the values
    * */
    public int sendMessage(final String toSendMessage, final String[] toSendPicturesLocation,
                           final String[] toSendvideosLocation,final String toSendGPSCoord,
                           final String toSendGPSStreet, final String destinyMail){
            new AsyncTask<Void, Void, Void>() {
                String statusMessage = "";
                int errorCode =-1;

                @Override
                protected void onPreExecute()
                {}

                @Override
                protected Void doInBackground(Void... params)
                {
                    //TODO: Intercambiar lo eliminado de mensajes de google por peticiones a un servicio REST
                    //Create a new GMailSender with the sender address and its password
                    //GMailSender sender = new GMailSender("ereporteruc3m@gmail.com", "3r3p0rt3ruc3m");
                    //GMailSender sender = new GMailSender("eReporter@outlook.com", "3r3p0rt3ruc3m");

                    try {
                        //Send the mail, giving all the parameters to the method
                        //sender.sendMail("Emergency Report!", "The info sended is the next: \n", destinyMail, destinyMail,
                                //toSendMessage, toSendPicturesLocation, toSendvideosLocation, toSendGPSCoord, toSendGPSStreet);


                        errorCode=1;
                        statusMessage="Message sended";
                        deliverResultToReceiver(errorCode, statusMessage);


                    } catch (Exception e) {
                        errorCode=2;
                        statusMessage="Fail in sending";
                        deliverResultToReceiver(errorCode, statusMessage);
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void res)
                {}
            }.execute();


        return 0;
    }


    private void deliverResultToReceiver(int resultCode, String message) {

        //Put the result message or the address on a Bundle
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        bundle.putInt(Constants.RESULT_AP_KEY, ap);
        bundle.putInt(Constants.RESULT_XP_KEY,xp);
        bundle.putInt(Constants.RESULT_ACHIEVEMENT_COMPLETED,achievement);


        //Send the values
        mSender.send(resultCode, bundle);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}