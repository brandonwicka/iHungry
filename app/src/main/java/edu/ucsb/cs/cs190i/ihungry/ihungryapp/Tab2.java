package edu.ucsb.cs.cs190i.ihungry.ihungryapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by brand_000 on 3/6/2016.
 */
public class Tab2 extends Fragment {
    private Restaurant mRestaurant;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRestaurant = (Restaurant) getArguments().getSerializable("Restaurant");
        View v = inflater.inflate(R.layout.tab2, container, false);

        if (mRestaurant != null) {
            ImageView profile_pic = (ImageView) v.findViewById(R.id.profile_pic);
            String url = mRestaurant.getRatingImageUrl();
            Picasso.with(getActivity().getApplicationContext()).load(url).transform(new CircleTransform()).into(profile_pic);
            TextView reviewTextView = (TextView) v.findViewById(R.id.review_text);
            reviewTextView.setText(mRestaurant.getRatingText());
        }


        return v;
    }
}