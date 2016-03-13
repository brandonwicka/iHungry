package edu.ucsb.cs.cs190i.ihungry.ihungryapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
        ImageButton pic1 = (ImageButton) v.findViewById(R.id.pic1);
        ImageButton pic2 = (ImageButton) v.findViewById(R.id.pic2);
        ImageButton pic3 = (ImageButton) v.findViewById(R.id.pic3);
        String url1 = "http://s3-media3.fl.yelpcdn.com/bphoto/_CTFS9nG896h40jQ0zx2ew/o.jpg";
        String url2 = "http://s3-media2.fl.yelpcdn.com/bphoto/i_DclbF1xDqxRAFp86iEyw/o.jpg";
        String url3 = "http://s3-media2.fl.yelpcdn.com/bphoto/xE7f2Z-meKPruhA2ORN9Qw/o.jpg";

        Picasso.with(getActivity().getApplicationContext()).load(url1).transform(new CircleTransform()).into(pic1);
        Picasso.with(getActivity().getApplicationContext()).load(url2).transform(new CircleTransform()).into(pic2);
        Picasso.with(getActivity().getApplicationContext()).load(url3).transform(new CircleTransform()).into(pic3);


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
                String url1 = "http://s3-media3.fl.yelpcdn.com/bphoto/_CTFS9nG896h40jQ0zx2ew/o.jpg";
                Picasso.with(getActivity().getApplicationContext()).load(url1).resize(500, 500).into(imageView);
                dialog.setView(dialogLayout);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.show();
            }
        });


        pic2.setOnClickListener(new View.OnClickListener() {
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
                String url2 = "http://s3-media2.fl.yelpcdn.com/bphoto/i_DclbF1xDqxRAFp86iEyw/o.jpg";
                Picasso.with(getActivity().getApplicationContext()).load(url2).resize(500, 500).into(imageView);
                dialog.setView(dialogLayout);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        });


        pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppTheme_FlavoredMaterialLight3);
                builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                String url3 = "http://s3-media2.fl.yelpcdn.com/bphoto/xE7f2Z-meKPruhA2ORN9Qw/o.jpg";

                AlertDialog dialog = builder.create();
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.image_dialog, null);
                ImageView imageView = (ImageView) dialogLayout.findViewById(R.id.DialogImage);
                Picasso.with(getActivity().getApplicationContext()).load(url3).resize(500, 500).into(imageView);
                dialog.setView(dialogLayout);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.show();
            }
        });

        if (mRestaurant != null) {
            Log.v("Restaurant", mRestaurant.toString());
            TextView addressTextView = (TextView) v.findViewById(R.id.text_address);
            addressTextView.setText(mRestaurant.getAddress());

            TextView phoneTextView = (TextView) v.findViewById(R.id.textNumber);
            phoneTextView.setText(mRestaurant.getDisplayPhoneNumber());

            TextView websiteTextView = (TextView) v.findViewById(R.id.website_link);
            websiteTextView.setText(mRestaurant.getUrl().split(Pattern.quote("?"))[0]);

        }


        return v;
    }


}