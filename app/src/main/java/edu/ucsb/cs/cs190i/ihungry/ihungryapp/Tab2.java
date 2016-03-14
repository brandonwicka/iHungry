package edu.ucsb.cs.cs190i.ihungry.ihungryapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by brand_000 on 3/6/2016.
 */
public class Tab2 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab2,container,false);

        ImageView profile_pic = (ImageView) v.findViewById(R.id.profile_pic);
        String url = "https://s3-media2.fl.yelpcdn.com/photo/8QQLxyC1eRrW4p5bH53Tag/ls.jpg";
        Picasso.with(getActivity().getApplicationContext()).load(url).transform(new CircleTransform()).into(profile_pic);

        return v;
    }
}