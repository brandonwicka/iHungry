package edu.ucsb.cs.cs190i.ihungry.ihungryapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private List<String> mCheckedItems;
    private int checkedItemIndex;
    private int checkedItemIndex2;
    private String mCheckedItem;
    private String mCheckedItem2;
    final String[] list = new String[]{"American", "Chinese", "Mexican", "Italian", "Indian", "Japanese", "Korean"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("iHungry");

        ImageButton typeButton = (ImageButton)findViewById(R.id.typeButton);
        ImageButton priceButton = (ImageButton)findViewById(R.id.priceButton);
        ImageButton ratingButton = (ImageButton)findViewById(R.id.ratingButton);
        final TextView text_distance = (TextView) findViewById(R.id.text_distance);
        SeekBar slider = (SeekBar) findViewById(R.id.seekBar);
        slider.setProgress(25);
        mCheckedItems = new ArrayList<String>();
        checkedItemIndex = 0;
        checkedItemIndex2 = 0;

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

        slider.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return false;
            }
        });
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text_distance.setText("DISTANCE: " + progress + " MILES");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private AlertDialog.Builder createAlertDialogBuilder() {

        return new AlertDialog.Builder(this, R.style.AppTheme_FlavoredMaterialLight2);
    }

    private void showTypeDialog() {

        boolean[] b = new boolean[] {false, false, false, false, false, false, false};
        for(int i = 0; i < mCheckedItems.size(); i++) {
            if((mCheckedItems.get(i)).equals("American"))
                b[0] = true;
            else if((mCheckedItems.get(i)).equals("Chinese"))
                b[1] = true;
            else if((mCheckedItems.get(i)).equals("Mexican"))
                b[2] = true;
            else if((mCheckedItems.get(i)).equals("Italian"))
                b[3] = true;
            else if((mCheckedItems.get(i)).equals("Indian"))
                b[4] = true;
            else if((mCheckedItems.get(i)).equals("Japanese"))
                b[5] = true;
            else if((mCheckedItems.get(i)).equals("Korean"))
                b[6] = true;
        }

        createAlertDialogBuilder()
                .setTitle("SELECT CATEGORIES")
                .setMultiChoiceItems(list,
                        b,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    mCheckedItems.add(list[which]);
                                } else {
                                    mCheckedItems.remove(list[which]);
                                }

                            }
                        })
                        //.setNeutralButton("CANCEL", null)
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(
                        "CONFIRM",
                        null)

                .show();
    }

    private void showPriceDialog() {
        final String[] list = new String[]{"$0-$10", "$10-$20", "$20-$50+"};

        mCheckedItem = list[checkedItemIndex];

        createAlertDialogBuilder()
                .setTitle("SELECT PRICE RANGE")
                .setSingleChoiceItems(list,
                        checkedItemIndex,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mCheckedItem = list[which];
                                checkedItemIndex = which;
                            }
                        })
                .setNegativeButton("CANCEL", null)
                .setPositiveButton(
                        "CONFIRM",
                        null
                )
                .show();
    }

    private void showRatingDialog() {
        final String[] list = new String[]{"5 STARS", "4 STARS", "3 STARS", "2 STARS", "1 STAR"};

        mCheckedItem2 = list[checkedItemIndex2];

        createAlertDialogBuilder()
                .setTitle("SELECT RATING")
                .setSingleChoiceItems(list,
                        checkedItemIndex2,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mCheckedItem2 = list[which];
                                checkedItemIndex2 = which;
                            }
                        })
                .setNegativeButton("CANCEL", null)
                .setPositiveButton(
                        "CONFIRM",
                        null
                )
                .show();
    }


}