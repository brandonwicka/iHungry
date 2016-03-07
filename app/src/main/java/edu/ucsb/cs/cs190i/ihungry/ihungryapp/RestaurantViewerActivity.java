package edu.ucsb.cs.cs190i.ihungry.ihungryapp;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class RestaurantViewerActivity extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    SlidingTabLayout tabs;
    CharSequence titles[]={"INFO", "REVIEWS"};
    int numOfTabs = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_viewer);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setColorFilter(Color.rgb(123, 123, 123), PorterDuff.Mode.MULTIPLY);
        String url = "https://static.pexels.com/photos/4309/city-restaurant-table-pavement.jpg";
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
    }
}
