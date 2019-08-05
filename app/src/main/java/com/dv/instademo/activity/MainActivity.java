package com.dv.instademo.activity;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dv.instademo.fragment.ProfileFragment;
import com.dv.instademo.R;
import com.dv.instademo.fragment.SearchFragment;
import com.dv.instademo.fragment.CreateFeedFragment;
import com.dv.instademo.fragment.FavouriteFragment;
import com.dv.instademo.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ViewPager viewpagerMain;
    private TabLayout tabsMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabsMain = findViewById(R.id.tabLayoutMain);
        viewpagerMain = findViewById(R.id.viewpagerMain);
        findViewById(R.id.layoutTab);
        setupViewPager(viewpagerMain);
        setUpMainScreen();

    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.setOffscreenPageLimit(5);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new SearchFragment());
        adapter.addFragment(new CreateFeedFragment());
        adapter.addFragment(new FavouriteFragment());
        adapter.addFragment(new ProfileFragment());
        viewPager.setAdapter(adapter);
        tabsMain.setupWithViewPager(viewPager);
    }

    private void setUpMainScreen() {
        final int[] arrayIcons = {R.drawable.ic_home_black_24dp, R.drawable.ic_search_black_24dp, R.drawable.ic_add_box_black_24dp,
                R.drawable.ic_favorite_black_24dp, R.drawable.ic_person_black_24dp};


        for (int i = 0; i < arrayIcons.length; i++) {

            LinearLayout tabLayout = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab_main, tabsMain, false);
            ImageView imageView = tabLayout.findViewById(R.id.imageTabIcon);
            imageView.setImageResource(arrayIcons[i]);

            tabsMain.getTabAt(i).setCustomView(tabLayout);

        }

        final int tabIconColorSelected = ContextCompat.getColor(this, R.color.colorBlack);
        final int tabIconColorDeselected = ContextCompat.getColor(this, R.color.colorGrY);

        tabsMain.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewpagerMain) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                if (tab.getCustomView() != null) {
                    changeTabColor(tabIconColorSelected, tab.getCustomView());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                if (tab.getCustomView() != null) {
                    changeTabColor(tabIconColorDeselected, tab.getCustomView());
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });
        int currentTab = 0;
        TabLayout.Tab tab = tabsMain.getTabAt(currentTab);
        if (tab != null) {
            if (tab.getCustomView() != null) {
                changeTabColor(tabIconColorSelected, tab.getCustomView());
            }
        }

    }


    private void changeTabColor(int color, View view) {
        ImageView imageView = view.findViewById(R.id.imageTabIcon);
        imageView.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onBackPressed() {

        try {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {

                if (viewpagerMain.getCurrentItem() == 0) {
                    super.onBackPressed();
                } else {
                    viewpagerMain.setCurrentItem(0);
                }
            } else {
                if (getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getBackStackEntryCount() + 4).getTag().equals(viewpagerMain.getCurrentItem() + "")) {
                    super.onBackPressed();
                } else {
                    viewpagerMain.setCurrentItem(Integer.parseInt(getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getBackStackEntryCount() + 4).getTag()));
                }
            }
        } catch (Exception e) {
            super.onBackPressed();
        }


    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }


    }
    public void onBack(){
        onBackPressed();
    }
}
