package edu.ucsb.cs.cs190i.ihungry.ihungryapp;

import android.Manifest;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.location.Location;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

public class RestaurantViewerActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, YelpServiceResponse {

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    SlidingTabLayout tabs;
    ArrayList<Restaurant> mRestaurants;
    TextView distance_away;
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
    Restaurant mCurrentRestaurant;
    private final String RESTAURANT_KEY = "restaurant_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_viewer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.rsz_1backarrow);
        getSupportActionBar().setElevation(0);
        if(savedInstanceState != null) {
            mCurrentRestaurant = (Restaurant) savedInstanceState.getSerializable(RESTAURANT_KEY);
        }

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setColorFilter(Color.rgb(100, 100, 100), PorterDuff.Mode.MULTIPLY);

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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantViewerActivity.this, R.style.AppTheme_FlavoredMaterialLight3);
                builder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.image_dialog, null);
                ImageView touchView = (ImageView) dialogLayout.findViewById(R.id.DialogImage);
                if (mCurrentRestaurant != null) {
                    String url1 = mCurrentRestaurant.getFoodImageUrl();
                    String originalImage = url1.substring(0, url1.lastIndexOf("/")) + "/o.jpg";
                    Picasso.with(getApplicationContext()).load(originalImage).fit().centerInside().into(touchView);
                    dialog.setView(dialogLayout);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.show();
                }

            }
        });

        tabs.setViewPager(viewPager);
        getRestaurants();
    }

    @Override
    public void onSaveInstanceState(Bundle outState ){
        super.onSaveInstanceState(outState);
        outState.putSerializable(RESTAURANT_KEY, mCurrentRestaurant);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.next_restaurant:
                if (mRestaurants.size() > 1) {
                    mRestaurants.remove(mCurrentRestaurant);
                    mCurrentRestaurant = getRandomRestaurant(0);
                    updateUI();
                } else {
                    final Activity activity = this;
                    createAlertDialogBuilder()
                            .setTitle("Out of Restaurants")
                            .setMessage("No more restaurants fit your specifications.")
                            .setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            NavUtils.navigateUpFromSameTask(activity);
                                        }
                                    }).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.restaurant_menu, menu);
        return true;
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
        mRestaurants = restaurants;
        // Populate tab view here
        // Also filter based on star count here
        if(mCurrentRestaurant == null){
            mCurrentRestaurant = getRandomRestaurant(0);
        }
        updateUI();
    }

    @Override
    public void onError(String errorMessage) {
        Log.v("YelpApiError", errorMessage);
    }

    private Restaurant getRandomRestaurant(int attempts) {
        if (attempts == mRestaurants.size()) {
            return mRestaurants.get(0);
        }

        int random = (int) (Math.random() * ((mRestaurants.size() - 1) + 1));
        Restaurant r = mRestaurants.get(random);
        if (mStarCount <= 0) {
            return r;
        }
        if ((int)r.getRating() != mStarCount) {
            return getRandomRestaurant(++attempts);
        } else {
            return r;
        }
    }

    private void updateUI() {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        String foodImageUrl = mCurrentRestaurant.getFoodImageUrl();
        String originalImage = foodImageUrl.substring(0, foodImageUrl.lastIndexOf("/")) + "/o.jpg";
        Picasso.with(getApplicationContext()).load(originalImage).fit().centerCrop().into(imageView);

        Location rest_loc = new Location("");
        rest_loc.setLatitude(mCurrentRestaurant.getLatitude());
        rest_loc.setLongitude(mCurrentRestaurant.getLongitude());
        float distanceInMeters = mLastLocation.distanceTo(rest_loc);
        float distanceInMiles = (distanceInMeters / 1609);
        distance_away = (TextView) findViewById(R.id.text_distance_away);
        distance_away.setText("" + String.format("%.1f", distanceInMiles) + " MILES AWAY");

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(mCurrentRestaurant.getName());
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.starsView);
        int stars = (int) mCurrentRestaurant.getRating();
        int margin = 0;
        while (stars > 0) {
            ImageView starView = new ImageView(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(70, 70);
            layoutParams.setMargins(margin, 0, 0, 0);
            layoutParams.alignWithParent = true;
            starView.setImageResource(R.drawable.starfull);
            starView.setLayoutParams(layoutParams);
            layout.addView(starView);
            stars -= 1;
            margin += 95;
        }
        viewPagerAdapter.setRestaurant(mCurrentRestaurant);
        viewPagerAdapter.notifyDataSetChanged();
        viewPager.invalidate();
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

    private android.support.v7.app.AlertDialog.Builder createAlertDialogBuilder() {

        return new android.support.v7.app.AlertDialog.Builder(this, R.style.AppTheme_FlavoredMaterialLight2);
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
