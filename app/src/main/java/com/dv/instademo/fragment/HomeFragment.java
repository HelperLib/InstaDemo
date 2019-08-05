package com.dv.instademo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dv.instademo.adapter.FeedListAdapter;
import com.dv.instademo.helper.CirclePageIndicator;
import com.dv.instademo.R;
import com.dv.instademo.helper.ItemClickInterface;
import com.dv.instademo.model.SampleFeed;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements ItemClickInterface {

    private View view;
    private RecyclerView recyclerView;
    private ArrayList<SampleFeed> sampleFeedArrayList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFunction();
        setupData();
    }

    private void setupData() {
       /* sampleFeedArrayList.add(new SampleFeed("0", "Popularity",R.drawable.img1,"Test Feed"));
        sampleFeedArrayList.add(new SampleFeed("1", "Popularity",R.drawable.img1,"Test Feed"));
        sampleFeedArrayList.add(new SampleFeed("2", "Popularity",R.drawable.img1,"Test Feed"));
        sampleFeedArrayList.add(new SampleFeed("3", "Popularity",R.drawable.img1,"Test Feed"));
        sampleFeedArrayList.add(new SampleFeed("4", "Popularity",R.drawable.img1,"Test Feed"));
        sampleFeedArrayList.add(new SampleFeed("5", "Popularity",R.drawable.img1,"Test Feed"));
        sampleFeedArrayList.add(new SampleFeed("6", "Popularity",R.drawable.img1,"Test Feed"));
        sampleFeedArrayList.add(new SampleFeed("7", "Popularity",R.drawable.img1,"Test Feed"));*/
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        FeedListAdapter feedListAdapter = new FeedListAdapter(sampleFeedArrayList, getActivity(),this);
        recyclerView.setAdapter(feedListAdapter);
    }

    private void initFunction() {
        ViewPager viewPagerSlider = view.findViewById(R.id.viewPager);
        recyclerView=view.findViewById(R.id.recyclerView);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity());

        viewPagerSlider.setAdapter(viewPagerAdapter);
        CirclePageIndicator circlePageIndicator =view.findViewById(R.id.indicator);
        circlePageIndicator.setViewPager(viewPagerSlider);
    }

    @Override
    public void passData(int position,View view) {

    }

    public class ViewPagerAdapter extends PagerAdapter {


        private LayoutInflater layoutInflater;
        private Integer[] images = {R.drawable.img1, R.drawable.img2, R.drawable.img_3,R.drawable.img_4,R.drawable.img5};
        private  Context context;

        ViewPagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.custom_image_view,null);
            ImageView imageView = view.findViewById(R.id.imageView);
            imageView.setImageResource(images[position]);

            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);
            return view;

        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

            ViewPager vp = (ViewPager) container;
            View view = (View) object;
            vp.removeView(view);

        }
    }
}
