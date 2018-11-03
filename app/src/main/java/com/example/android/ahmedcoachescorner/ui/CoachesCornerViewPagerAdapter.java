package com.example.android.ahmedcoachescorner.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;



public class CoachesCornerViewPagerAdapter extends FragmentStatePagerAdapter {


    private List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public CoachesCornerViewPagerAdapter(FragmentManager fragmentManager) {

        super(fragmentManager);

//        mFragmentList = new ArrayList<Fragment>();
    }

    @Override
    public Fragment getItem(int position) {

        return mFragmentList.get(position);
    }




    @Override
    public int getItemPosition(@NonNull Object object) {

        return POSITION_NONE;
    }


    @Override
    public int getCount() {

        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mFragmentTitleList.get(position);
    }

    public Fragment getFragment(int position) {
        Fragment fragment = mFragmentList.get(position);
        if (fragment != null) {
            return fragment;
        }

        return null;

    }

}
