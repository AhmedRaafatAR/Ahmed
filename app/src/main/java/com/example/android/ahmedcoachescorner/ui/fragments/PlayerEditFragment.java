package com.example.android.ahmedcoachescorner.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.android.ahmedcoachescorner.R;
import com.example.android.ahmedcoachescorner.common.Utils;
import com.example.android.ahmedcoachescorner.data.Player;

import butterknife.BindView;
import butterknife.ButterKnife;



public class PlayerEditFragment extends Fragment {


    private static final String PARCELABLEKEY = "Player";

    private Player mPlayer;
    private Context mContext;
    @BindView(R.id.iv_player_edit_pic) ImageView mPlayerPic;
    @BindView(R.id.btn_add_player_edit_pic) Button mBtnAddPlayerPic;
    @BindView(R.id.et_player_edit_add_name) EditText mPlayerNameEditText;
    @BindView(R.id.et_player_edit_number) EditText mPlayerNumber;
    @BindView(R.id.et_player_edit_mins_played) EditText mMinutesPlayed;
    @BindView(R.id.et_player_edit_notes) EditText mPlayerNotes;
    @BindView(R.id.btn_player_edit_submit) Button mSubmitBtn;
    @BindView(R.id.btn_player_edit_cancel) Button mCancelBtn;


    public PlayerEditFragment() {
        //Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(PARCELABLEKEY)) {
                mPlayer = savedInstanceState.getParcelable(PARCELABLEKEY);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_player_edit, container, false);
        ButterKnife.bind(this, view);

        mContext = view.getContext();

        mPlayerNameEditText.setText(mPlayer.getPlayerName());
        mPlayerNumber.setText(String.valueOf(mPlayer.getPlayerNum()));
        mPlayerNotes.setText(mPlayer.getPlayerNote());

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.setPlayerName(mPlayerNameEditText.getText().toString());
                mPlayer.setPlayerNumber(mPlayerNumber.getText().toString());
                mPlayer.setPlayerNote(mPlayerNotes.getText().toString());
                Boolean updatePlayerSuccess = Utils.updatePlayerToDatabase(mContext, mPlayer);
                if (!updatePlayerSuccess) {
                    Log.d("PlayerEditFragment", "Failed to update player");
                }
                getActivity().onBackPressed();
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PARCELABLEKEY, mPlayer);
    }


    public void setPlayer(Player player) {
        mPlayer = player;
    }

}
