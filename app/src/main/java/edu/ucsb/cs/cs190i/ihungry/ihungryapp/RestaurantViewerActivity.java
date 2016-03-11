package edu.ucsb.cs.cs190i.ihungry.ihungryapp;

import android.Manifest;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.location.Location;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestaurantViewerActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, YelpServiceResponse {

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    SlidingTabLayout tabs;
    CharSequence titles[]={"INFO", "REVIEWS"};
    int numOfTabs = 2;
    static final int PERMISSIONS_FINE_LOCATION_REQUEST = 1234;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    int mStarCount;
    int mRadius;
    ArrayList<String> mFoodTypes;
    int mMinPrice;
    int mMaxPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_viewer);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setColorFilter(Color.rgb(123, 123, 123), PorterDuff.Mode.MULTIPLY);
        String url = "http://restaurantnews.com/wp-content/uploads/2013/03/East-Coast-Wings-Grill-Spreads-Wings-Outside-of-North-Carolina-Hub-Opens-First-of-Six-Local-Restaurants-in-San-Antonio.jpg";
        Picasso.with(getApplicationContext()).load(url).fit().centerCrop().into(imageView);

        viewPagerAdapter =  new ViewPagerAdapter(getSupportFragmentManager(),titles, numOfTabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(viewPagerAdapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }
        });

        tabs.setViewPager(viewPager);
        getRestaurants();
    }

    private void getRestaurants() {
        Bundle extras = getIntent().getExtras();
        mFoodTypes = extras.getStringArrayList("FoodTypes");
        mStarCount = extras.getInt("StarCount", 0);
        mMinPrice = extras.getInt("MinPrice", -1);
        mMaxPrice = extras.getInt("MaxPrice", -1);
        mRadius = extras.getInt("Radius", 25); // In Miles
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_FINE_LOCATION_REQUEST);
        } else {
            if (mLastLocation == null) {
                initializeGoogleApiClient();
            } else {
                sendRequestToYelp();
            }
        }

    }

    private void sendRequestToYelp() {
        Request r = new RequestBuilder()
                .latitude(mLastLocation.getLatitude())
                .longitude(mLastLocation.getLongitude())
                .radius(mRadius)
                .filter(mFoodTypes)
                .buildRequest();
        YelpService.search(getApplicationContext(), r, this);
    }

    @Override
    public void onSuccess(ArrayList<Restaurant> restaurants) {
        Log.v("YelpApiResponse", restaurants.toString());
    }

    @Override
    public void onError(String errorMessage) {
        Log.v("YelpApiError", errorMessage);
    }

    private void initializeGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        if (mGoogleApiClient == null) initializeGoogleApiClient();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient == null) initializeGoogleApiClient();
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        sendRequestToYelp();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_FINE_LOCATION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        //get Android Studio compiler to shut up
                    }
                    // Success
                    initializeGoogleApiClient();
                } else {

                    // permission denied
                }
                return;
            }

        }
    }
}
