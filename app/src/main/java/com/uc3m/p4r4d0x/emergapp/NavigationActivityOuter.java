package com.uc3m.p4r4d0x.emergapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uc3m.p4r4d0x.emergapp.helpers.database.DBUserManager;


public class NavigationActivityOuter extends AppCompatActivity {

    //Info to use shared preferences to have a session
    final String MyPREFERENCES = "userPreferencesG1";
    SharedPreferences sharedpreferences;
    Toolbar toolbar ;

    protected void onCreate(Bundle savedInstanceState, int childLayout) {
        super.onCreate(savedInstanceState);

        setContentView(childLayout);

        //Check if there is any user logged into the aplication checking shared preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.toolbarOuter);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(getColor(R.color.colorStatusBar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }



    /*
     * Desc: method overrided from AppCompatActivity
     *       this method is called when activity starts
     *       Prepare the toolbar menu
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
    * Desc: method overrided from AppCompatActivity
    *       this method is called when activity starts
    *       Prepare the elements on the toolbar menu
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.changeEmail:
                onClickShowChangeEmail();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
* Desc: on click function to change email
* */
    public void onClickShowChangeEmail(){
        //Get the alert dialog based on the resource email_change_input
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(NavigationActivityOuter.this);
        View layView = (LayoutInflater.from(NavigationActivityOuter.this)).inflate(R.layout.email_change_input, null);
        alertBuilder.setView(layView);

        //Get the field
        final EditText userInput = (EditText) layView.findViewById(R.id.tvContentMessage);

        //Build the buttons on the alertbuilder
        alertBuilder.setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        //Set the email into the shared preferences
                        sharedpreferences.edit().putString("email_to_report", userInput.getText().toString()).commit();
                        Toast.makeText(getApplicationContext(), "Email to retrieve reports changed!", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
        ;

        Dialog dialog = alertBuilder.create();
        dialog.show();

    }


    @Override
    public void onBackPressed() {
    }


}
