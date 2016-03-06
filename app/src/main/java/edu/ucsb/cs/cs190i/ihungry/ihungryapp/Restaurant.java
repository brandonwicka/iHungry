package edu.ucsb.cs.cs190i.ihungry.ihungryapp;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Aaron on 3/6/2016.
 */
public class Restaurant implements Serializable {

    private static final long serialVersionUID = 1L;
    private double mLatitude;
    private double mLongitude;
    private String mName;
    private String mObjectId;
    private double mRating;
    private String mDisplayPhoneNumber;
    private String mCallablePhoneNumber;
    private String mFoodImageUrl;
    private ArrayList<String> mCategories;
    private String mUrl;

    public Restaurant(String name, LatLng latLng){
        mName = name;
        mLongitude = latLng.longitude;
        mLatitude = latLng.latitude;
    }

    /**
     * Constructs a Restaurant from a JSONObject
     * @param json
     */
    public Restaurant(JSONObject json) {
        mName = json.optString("name");
        mObjectId = json.optString("id");
        JSONObject location = json.optJSONObject("location");
        if (location != null) {
            JSONObject coords = location.optJSONObject("coordinate");
            if (coords != null) {
                mLatitude = coords.optDouble("latitude");
                mLongitude = coords.optDouble("longitude");
            }
        }
        mRating = json.optDouble("rating");
        mDisplayPhoneNumber = json.optString("display_phone");
        mCallablePhoneNumber = json.optString("phone");
        mFoodImageUrl = json.optString("image_url");
        JSONArray categories = json.optJSONArray("categories");
        mCategories = new ArrayList<String>();
        if (categories != null) {
            for (int i = 0; i < categories.length(); i++) {
                JSONArray category = categories.optJSONArray(i);
                mCategories.add(category.optString(0));
            }
        }
        mUrl = json.optString("mobile_url");
    }

    /**
     * Returns a list of Restaurant objects generated from a JSONArray
     * @param array
     * @return ArrayList<Restaurant>
     */
    public static ArrayList<Restaurant> restaurantsFromJSONArray(JSONArray array) {
        ArrayList<Restaurant> restuarants = new ArrayList<Restaurant>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.optJSONObject(i);
            if (json != null) {
                restuarants.add(new Restaurant(json));
            }
        }
        return restuarants;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public String getName() {
        return mName;
    }

    public double getRating() {
        return mRating;
    }

    public String getCallablePhoneNumber() {
        return mCallablePhoneNumber;
    }

    public String getDisplayPhoneNumber() {
        return mDisplayPhoneNumber;
    }

    public String getFoodImageUrl() {
        return mFoodImageUrl;
    }

    public String getObjectId() {
        return mObjectId;
    }

    public ArrayList<String> getCategories() {
        return mCategories;
    }

    public String getUrl() {
        return mUrl;
    }

    @Override
    public String toString() {
        return String.format("ID: %s\nName: %s\nLat: %f\nLon: %f\nRating: %f\nImage Url: %s\nCategories: %s\nPhone: %s and %s\nUrl: %s\n", mObjectId, mName, mLatitude, mLongitude, mRating, mFoodImageUrl, mCategories.toString(), mCallablePhoneNumber, mDisplayPhoneNumber, mUrl);
    }
}
