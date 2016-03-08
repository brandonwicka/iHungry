package edu.ucsb.cs.cs190i.ihungry.ihungryapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.squareup.picasso.Picasso;

/**
 * Created by brand_000 on 3/6/2016.
 */
public class Tab1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        return v;
    }


}