package edu.ucsb.cs.cs190i.ihungry.ihungryapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import com.rey.material.widget.Slider;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ArrayList<String> mCheckedItems;
    private int mCheckedPriceIndex;
    private int mCheckedStarsIndex;
    private String mCheckedPrice;
    private String mCheckedStars;
    final String[] list = new String[]{"American", "Chinese", "Mexican", "Italian", "Indian", "Japanese", "Korean"};
    static final int PERMISSIONS_FINE_LOCATION_REQUEST = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("iHungry");

        ImageButton bigButton = (ImageButton)findViewById(R.id.big_button);
        ImageButton typeButton = (ImageButton)findViewById(R.id.typeButton);
        ImageButton priceButton = (ImageButton)findViewById(R.id.priceButton);
        ImageButton ratingButton = (ImageButton)findViewById(R.id.ratingButton);
        final TextView text_distance = (TextView) findViewById(R.id.text_distance);

        com.rey.material.widget.Slider s = (com.rey.material.widget.Slider) findViewById(R.id.seekBar);
        s.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                text_distance.setText("DISTANCE: " + newValue + " MILES");

            }
        });
        //slider.setProgress(25);
        mCheckedItems = new ArrayList<String>();
        mCheckedPriceIndex = 0;
        mCheckedStarsIndex = 0;

        bigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBigButton();
            }
        });

        typeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTypeDialog();
            }
        });

        priceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPriceDialog();
            }
        });

        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //currently, this opens a map to a dummy location.
    //This should be replaced by launching the adctivity that displays information.  That activity should have this method in it attached to a button.
    private void displayOnMap(){
        Restaurant restaurant = new Restaurant("Storke Tower", new LatLng(34.412612, -119.848411));
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(MapsActivity.RESTAURANT_KEY, restaurant);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void doBigButton(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_FINE_LOCATION_REQUEST);
        }
        else{
            displayRestaurantView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_FINE_LOCATION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                } else {
                    // permission denied
                    createAlertDialogBuilder()
                            .setTitle("Location Access Required")
                            .setMessage("\tiHungry requires you to enable access to this device's location in order to find restaurants near you." +
                                    "  Either enable location access for iHungry in your device's settings or choose \"ALLOW\" when prompted for permissions.")
                            .setNegativeButton("Close",null)
                    .show();
                }
                return;
            }

        }
    }

    private void displayRestaurantView() {
        Intent intent = new Intent(this, RestaurantViewerActivity.class);
        if (mCheckedItems != null) intent.putStringArrayListExtra("FoodTypes", mCheckedItems);
        if (mCheckedPrice != null) {
            if (mCheckedPrice.equals("$0-$10")) {
                intent.putExtra("MinPrice", 0);
                intent.putExtra("MaxPrice", 10);
            } else if (mCheckedPrice.equals("$10-$20")) {
                intent.putExtra("MinPrice", 10);
                intent.putExtra("MaxPrice", 20);
            } else if (mCheckedPrice.equals("$20-$50+")) {
                intent.putExtra("MinPrice", 20);
            }
        }
        if (mCheckedStars != null) {
            Integer starCount = Integer.parseInt(mCheckedStars.substring(0, 1));
            intent.putExtra("StarCount", starCount.intValue());
        }
        com.rey.material.widget.Slider s = (com.rey.material.widget.Slider) findViewById(R.id.seekBar);
        int radius = s.getValue();
        intent.putExtra("Radius", radius);
        startActivity(intent);
    }


    private AlertDialog.Builder createAlertDialogBuilder() {

        return new AlertDialog.Builder(this, R.style.AppTheme_FlavoredMaterialLight2);
    }

    private void showTypeDialog() {

        final boolean[] booleans = new boolean[] {false, false, false, false, false, false, false};
        for(int i = 0; i < mCheckedItems.size(); i++) {
            if((mCheckedItems.get(i)).equals("American"))
                booleans[0] = true;
            else if((mCheckedItems.get(i)).equals("Chinese"))
                booleans[1] = true;
            else if((mCheckedItems.get(i)).equals("Mexican"))
                booleans[2] = true;
            else if((mCheckedItems.get(i)).equals("Italian"))
                booleans[3] = true;
            else if((mCheckedItems.get(i)).equals("Indian"))
                booleans[4] = true;
            else if((mCheckedItems.get(i)).equals("Japanese"))
                booleans[5] = true;
            else if((mCheckedItems.get(i)).equals("Korean"))
                booleans[6] = true;
        }

        createAlertDialogBuilder()
                .setTitle("SELECT CATEGORIES")
                .setMultiChoiceItems(list,
                        booleans,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                //despite there being no code here, do not remove
                                //unless you know how to instantiate OnMultiChoiceClickerListener without this
                            }
                        })
                .setNegativeButton("CANCEL", null)
                .setPositiveButton(
                        "CONFIRM",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < booleans.length; i++) {
                                    if (booleans[i]) {
                                        if (!mCheckedItems.contains(list[i])) {
                                            mCheckedItems.add(list[i]);
                                        }
                                    } else {
                                        mCheckedItems.remove(list[i]);
                                    }
                                }
                            }
                        })

                .show();
    }

    private void showPriceDialog() {
        final String[] list = new String[]{"$0-$10", "$10-$20", "$20-$50+"};

        mCheckedPrice = list[mCheckedPriceIndex];

        final int checked = mCheckedPriceIndex;

        createAlertDialogBuilder()
                .setTitle("SELECT PRICE RANGE")
                .setSingleChoiceItems(list,
                        mCheckedPriceIndex,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mCheckedPrice = list[which];
                                mCheckedPriceIndex = which;
                            }
                        })
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mCheckedPrice = list[checked];
                                mCheckedPriceIndex = checked;
                            }
                        }
                )
                .setPositiveButton(
                        "CONFIRM",
                        null
                )
                .show();
    }

    private void showRatingDialog() {
        final String[] list = new String[]{"5 STARS", "4 STARS", "3 STARS", "2 STARS", "1 STAR"};

        mCheckedStars = list[mCheckedStarsIndex];

        final int checked = mCheckedStarsIndex;

        createAlertDialogBuilder()
                .setTitle("SELECT RATING")
                .setSingleChoiceItems(list,
                        mCheckedStarsIndex,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mCheckedStars = list[which];
                                mCheckedStarsIndex = which;
                            }
                        })
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mCheckedStars = list[checked];
                                mCheckedStarsIndex = checked;
                            }
                        }
                )
                .setPositiveButton(
                        "CONFIRM",
                        null
                )
                .show();
    }


}
