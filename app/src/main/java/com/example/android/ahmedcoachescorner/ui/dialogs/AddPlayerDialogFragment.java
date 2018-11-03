package com.example.android.ahmedcoachescorner.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.android.ahmedcoachescorner.R;

public class AddPlayerDialogFragment extends DialogFragment {

    private ImageView mPlayerPic;
    private Button mAddPlayerPicButton;
    private EditText mPlayerAddNameEditText;
    private EditText mPlayerAddNumEditText;
    private Button mSubmitBtn;
    private Button mCancelBtn;

    private AddPlayerDialogListener mAddPlayerCallBack;

    private static String mDialogboxTitle;


    public interface AddPlayerDialogListener {
        void onFinishAddPlayerDialog(String playerName, String playerNum);
    }

    public AddPlayerDialogFragment() {
        //Required empty Constructor
    }


    public void setDialogTitle(String title) {
        mDialogboxTitle = title;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mAddPlayerCallBack = (AddPlayerDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement AddPlayerDialogListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.player_add_dialogfragment, container);
//        ButterKnife.bind(this, view);

        mPlayerPic = (ImageView) view.findViewById(R.id.iv_player_pic);
        mAddPlayerPicButton = (Button) view.findViewById(R.id.btn_add_player_pic);
        mPlayerAddNameEditText = (EditText) view.findViewById(R.id.et_player_add_name);
        mPlayerAddNumEditText = (EditText) view.findViewById(R.id.et_player_add_number);
        mSubmitBtn = (Button) view.findViewById(R.id.btn_submit_player);
        mCancelBtn = (Button) view.findViewById(R.id.btn_cancel_player);


        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AddPlayerDialogListener targetFragment = (AddPlayerDialogListener) getTargetFragment();
                Log.d("AddPlayerDialog", "This is the Player Name - " + mPlayerAddNameEditText.getText().toString());
                mAddPlayerCallBack.onFinishAddPlayerDialog(mPlayerAddNameEditText.getText().toString(), mPlayerAddNumEditText.getText().toString());
//                targetFragment.onFinishAddPlayerDialog(mPlayerAddNameEditText.getText().toString());

                dismiss();
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

//        mPlayerAddNameEditText.requestFocus();
        getDialog().setTitle(mDialogboxTitle);


        return view;
    }
}
