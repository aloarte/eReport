package com.uc3m.p4r4d0x.emergapp;

/**
 * Created by Alvaro Loarte Rodríguez on 21/07/16.
 *
 */
public class ItemSpinnerData {
    String text;
    int imageId;

    //Constructor
    public ItemSpinnerData(String text, int imageId){
        this.text=text;
        this.imageId=imageId;
    }

    //return text value
    public String getText(){
        return text;

    }

    //return id value
    public int getImageId(){
        return imageId;
    }

}
