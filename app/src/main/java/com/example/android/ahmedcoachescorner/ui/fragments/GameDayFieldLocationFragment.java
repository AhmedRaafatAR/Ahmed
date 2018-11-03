package com.example.android.ahmedcoachescorner.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.ahmedcoachescorner.R;



public class GameDayFieldLocationFragment extends Fragment {

    public GameDayFieldLocationFragment() {
        //Required empty public Constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game_day_field_location, container, false);

        return view;
    }


}
