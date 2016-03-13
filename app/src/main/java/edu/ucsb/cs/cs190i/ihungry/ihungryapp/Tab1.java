package edu.ucsb.cs.cs190i.ihungry.ihungryapp;

import android.Manifest;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

/**
 * Created by brand_000 on 3/6/2016.
 */
public class Tab1 extends Fragment {

    Restaurant mRestaurant;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRestaurant = (Restaurant) getArguments().getSerializable("Restaurant");
        View v =inflater.inflate(R.layout.tab1,container,false);



        if (mRestaurant != null) {
            Log.v("Restaurant", mRestaurant.toString());
            TextView addressTextView = (TextView) v.findViewById(R.id.text_address);
            addressTextView.setText(mRestaurant.getAddress());

            TextView phoneTextView = (TextView) v.findViewById(R.id.textNumber);
            phoneTextView.setText(mRestaurant.getDisplayPhoneNumber());

            TextView websiteTextView = (TextView) v.findViewById(R.id.website_link);
            websiteTextView.setText(mRestaurant.getUrl().split(Pattern.quote("?"))[0]);

            ImageButton websiteButton = (ImageButton) v.findViewById(R.id.website_button);
            websiteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mRestaurant.getUrl()));
                    startActivity(browserIntent);
                }
            });

            final ImageButton mapButton = (ImageButton) v.findViewById(R.id.imageView2);
            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mapIntent = new Intent(getContext(), MapsActivity.class);
                    mapIntent.putExtra("restaurant_key", mRestaurant);
                    getActivity().startActivity(mapIntent);
                }
            });

            ImageButton phoneButton = (ImageButton) v.findViewById(R.id.phone_button);
            phoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                23);
                    } else {
                        PackageManager pm = getContext().getPackageManager();
                        if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                            try {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + mRestaurant.getCallablePhoneNumber()));
                                startActivity(callIntent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(getContext(), "Calling not enabled on this device", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Calling not enabled on this device", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });

            GridLayout categoryGrid = (GridLayout) v.findViewById(R.id.category_grid);
            for (String category : mRestaurant.getCategories()) {
                TextView textView = new TextView(getContext());
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout
                        .LayoutParams.WRAP_CONTENT);
                textView.setPadding(15, 15, 15, 20);
                textView.setTextSize(15);
                textView.setLayoutParams(layoutParams);
                textView.setBackground(getResources().getDrawable(R.drawable.pill));
                textView.setText(category);
                textView.setTextColor(getResources().getColor(R.color.white));
                categoryGrid.addView(textView);
            }

            ImageButton pic1 = (ImageButton) v.findViewById(R.id.pic1);

            Picasso.with(getActivity().getApplicationContext()).load(mRestaurant.getFoodImageUrl()).transform(new CircleTransform()).into(pic1);


            pic1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppTheme_FlavoredMaterialLight3);
                    builder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogLayout = inflater.inflate(R.layout.image_dialog, null);
                    ImageView imageView = (ImageView) dialogLayout.findViewById(R.id.DialogImage);
                    String url1 = mRestaurant.getFoodImageUrl();
                    Picasso.with(getActivity().getApplicationContext()).load(url1).resize(500, 500).into(imageView);
                    dialog.setView(dialogLayout);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.show();
                }
            });
        }


        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 23: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        //get Android Studio compiler to shut up
                    }
                    // Success
                    PackageManager pm = getContext().getPackageManager();
                    if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                        try {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + mRestaurant.getCallablePhoneNumber()));
                            startActivity(callIntent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getContext(), "Calling not enabled on this device", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Calling not enabled on this device", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    // permission denied
                }
                return;
            }

        }
    }
}