package edu.ucsb.cs.cs190i.ihungry.ihungryapp;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import java.io.IOException;
import java.util.ArrayList;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;


/**
 * Created by spencerprescott on 3/6/16.
 */

interface YelpServiceResponse {
    /**
     * If Yelp returns a successful response, passes back the Restuarant objects generated from JSON
     * @param restaurants
     */
    void onSuccess(ArrayList<Restaurant> restaurants);

    /**
     * If an error occurs while sending the request to Yelp, passes back the generated error message
     * @param errorMessage
     */
    void onError(String errorMessage);
}

public abstract class YelpService {
    private static final String consumerKey = "Oq0VM7nMlkRNd34CQCgZ7A";
    private static final String consumerSecret = "ZBt3qPTkdCfRL1tW074ph8N71uM";
    private static final String token = "__OE3ef3090onLLfZwvRlklH1IQXbvy_";
    private static final String tokenSecret = "cxBVm3ShLCdMTF0S6ch9fD8TgqY";

    /**
     * Sends network request to Yelp for restaurants
     * @param context
     * @param request
     * @param callback
     */
    public static void search(Context context, edu.ucsb.cs.cs190i.ihungry.ihungryapp.Request request, final YelpServiceResponse callback) {
        String url = generateURLString(request);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        consumer.setTokenWithSecret(token, tokenSecret);
        try {
            url = consumer.sign(url);
            StringRequest stringRequest = new StringRequest(
                    com.android.volley.Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONArray businesses = json.getJSONArray("businesses");
                                callback.onSuccess(Restaurant.restaurantsFromJSONArray(businesses));
                            } catch (JSONException e) {
                                // Malformed JSON
                                callback.onError(e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            callback.onError(error.getMessage());
                            error.printStackTrace();
                        }
                    });
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            callback.onError(e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    /**
     * Generate url for Yelp Api with selected search query
     * @param request
     * @return String
     */
    private static String generateURLString(edu.ucsb.cs.cs190i.ihungry.ihungryapp.Request request) {
        StringBuilder builder = new StringBuilder("https://api.yelp.com/v2/search?term=restaurants&");
        builder.append(String.format("ll=%f,%f&radius_filter=%f", request.latitude, request.longitude, request.radius));
        if (request.filter != null) builder.append(String.format("&category_filter=%s", request.filter));
        return builder.toString();
    }
}
