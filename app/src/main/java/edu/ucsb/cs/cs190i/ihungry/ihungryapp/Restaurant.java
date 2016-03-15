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
    private String mRatingImageUrl;
    private String mRatingText;
    private String mAddress;

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
            JSONArray array = location.optJSONArray("address");
            mAddress = String.format("%s, %s, %s", array.optString(0), location.optString("city"), location.optString("state_code"));
            JSONObject coords = location.optJSONObject("coordinate");
            if (coords != null) {
                mLatitude = coords.optDouble("latitude");
                mLongitude = coords.optDouble("longitude");
            }
        }
        mRating = json.optDouble("rating");
        String url2 = json.optString("snippet_image_url");
        if (url2 != null)
            mRatingImageUrl = url2.substring(0, url2.lastIndexOf("/")) + "/o.jpg";
        else
            mRatingImageUrl = "";
        mRatingText = json.optString("snippet_text");
        mDisplayPhoneNumber = json.optString("display_phone");
        mCallablePhoneNumber = json.optString("phone");
        String url1 = json.optString("image_url");
        if (url1 != null)
            mFoodImageUrl = url1.substring(0, url1.lastIndexOf("/")) + "/o.jpg";
        else
            mFoodImageUrl = "";

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

    public String getAddress() {
        return mAddress;
    }

    public String getRatingImageUrl() {
        return mRatingImageUrl;
    }

    public String getRatingText() {
        return mRatingText;
    }

    @Override
    public String toString() {
        return String.format("ID: %s\n" +
                "Name: %s\n" +
                "Lat: %f\n" +
                "Lon: %f\n" +
                "Rating: %f\n" +
                "Image Url: %s\n" +
                "Categories: %s\n" +
                "Phone: %s and %s\n" +
                "Url: %s\n" +
                "Address: %s\n" +
                        "Rating image url: %s\n" +
                        "Rating text: %s\n",
                mObjectId,
                mName,
                mLatitude,
                mLongitude,
                mRating,
                mFoodImageUrl,
                mCategories.toString(),
                mCallablePhoneNumber,
                mDisplayPhoneNumber,
                mUrl,
                mAddress,
                mRatingImageUrl,
                mRatingText);
    }
}
