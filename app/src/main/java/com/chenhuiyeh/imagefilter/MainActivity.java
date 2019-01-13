package com.chenhuiyeh.imagefilter;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public static final String PICTURE_NAME = "finish.jpg";
    public static final int PROGRESSION_PICK_DONE = 1000;

    ImageView img_preview;
    TabLayout tab_layout;
    ViewPager view_pager;
    CoordinatorLayout coordinator_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
