package com.example.android.ahmedcoachescorner.ui.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.ahmedcoachescorner.R;
import com.example.android.ahmedcoachescorner.common.Utils;
import com.example.android.ahmedcoachescorner.data.Game;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;



public class GameAddFragment extends Fragment {

    @BindView(R.id.et_add_game_date) EditText mAddGameDate;
    @BindView(R.id.et_add_game_time) EditText mAddGameTime;
    @BindView(R.id.et_add_game_opponent_name) EditText mOpponentName;
    @BindView(R.id.spr_add_game_home_or_away) Spinner mHomeOrAwaySpinner;
    @BindView(R.id.et_add_game_field_location) EditText mFieldLocation;
    @BindView(R.id.et_add_game_note) EditText mGameNote;
    @BindView(R.id.btn_add_game_submit) Button mSubmitBtn;

    private Context mContext;

    private Game mGame = new Game();
    private String mGameDate;
    private String mGameTime;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private String mHomeOrAway;

    public GameAddFragment() {
        //required public empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_game, container, false);
        ButterKnife.bind(this, view);

        mContext = view.getContext();

        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);




        mAddGameDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mGameDate = (dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        mGameDate = ((monthOfYear + 1) +"/" + dayOfMonth + "/" + year);
                        mAddGameDate.setText(mGameDate);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        mAddGameTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String minuteString = "00";
                        if (minute == 0) {
                            mGameTime = hourOfDay + ":00";
                        } else {
                            if (minute < 10) {
                                minuteString = "0" + minute;
                            } else {
                                minuteString = String.valueOf(minute);
                            }
                            mGameTime = hourOfDay + ":" + minuteString;
                        }
                        mAddGameTime.setText(mGameTime);
                    }
                },mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        mHomeOrAwaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mHomeOrAway = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGame.setGameDate(mGameDate);
                mGame.setGameTime(mGameTime);
                mGame.setOpponentName(mOpponentName.getText().toString());
                mGame.setHomeOrAway(mHomeOrAway);
                mGame.setFieldLocation(mFieldLocation.getText().toString());
                boolean loadSuccessfully = Utils.addGameToDatabase(mContext, mGame);
                if (loadSuccessfully) {
                    Toast.makeText(mContext, "Game Added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Failed to add game to schedule", Toast.LENGTH_LONG).show();
                }
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}
