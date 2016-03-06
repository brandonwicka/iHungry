package edu.ucsb.cs.cs190i.ihungry.ihungryapp;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Aaron on 3/6/2016.
 */
public class Restaurant implements Serializable {

    private static final long serialVersionUID = 1L;
    private double mLatitude;
    private double mLongitude;
    private String mName;

    public Restaurant(String name, LatLng latLng){
        mName = name;
        mLongitude = latLng.longitude;
        mLatitude = latLng.latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public String getName(){
        return mName;
    }
}
