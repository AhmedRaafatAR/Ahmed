package com.example.android.ahmedcoachescorner.ui.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.ahmedcoachescorner.R;
import com.example.android.ahmedcoachescorner.adapter.ScoreCardAdapter;
import com.example.android.ahmedcoachescorner.data.CoachesCornerDBContract;
import com.example.android.ahmedcoachescorner.data.Player;
import com.example.android.ahmedcoachescorner.ui.dialogs.UpdatePlayerScoreDialogFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;




public class GameDayScoreCardFragment extends Fragment
    implements ScoreCardAdapter.ScoreCardAdapterOnClickHandler {

    private static final int ID_PLAYER_LOADER = 3;

    private static final String PARCELABLEKEY = "Players";

    private ScoreCardAdapter mScoreCardAdapter;
    public ArrayList<Player> mPlayers;
    @BindView(R.id.recyclerview_scorecard) RecyclerView mScoreCardRecyclerView;
    private Context mContext;
    private CoordinatorLayout mCoordinatorLayout;


    public GameDayScoreCardFragment() {
        //Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View viewScoreCard = inflater.inflate(R.layout.fragment_game_day_score_card, container, false);
        ButterKnife.bind(this, viewScoreCard);

        mContext = viewScoreCard.getContext();

        mCoordinatorLayout = (CoordinatorLayout) viewScoreCard.findViewById(R.id.score_card_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mScoreCardRecyclerView.setLayoutManager(layoutManager);
        mScoreCardRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mScoreCardRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        mScoreCardAdapter = new ScoreCardAdapter(mContext, this);
        mScoreCardRecyclerView.setAdapter(mScoreCardAdapter);

        loadPlayerData();

        return viewScoreCard;

    }


    private void loadPlayerData() {
        mPlayers = new ArrayList<>();
        getLoaderManager().initLoader(ID_PLAYER_LOADER, null, loaderCallBackCursorLoader);
    }

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallBackCursorLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

            Uri playerUri = CoachesCornerDBContract.PlayerDBEntry.CONTENT_URI;

            return new CursorLoader(mContext,
                    playerUri,
                    null,
                    null,
                    null,
                    null);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

            if (cursor == null || cursor.getCount() < 1) {
                return;
            }

            mPlayers.clear();

            while (cursor.moveToNext()) {
                int playerId = cursor.getInt(cursor.getColumnIndex(CoachesCornerDBContract.PlayerDBEntry._ID));
                String playerName = cursor.getString(cursor.getColumnIndex(CoachesCornerDBContract.PlayerDBEntry.COLUMN_PLAYERNAME));
                String playerNum = cursor.getString(cursor.getColumnIndex(CoachesCornerDBContract.PlayerDBEntry.COLUMN_PLAYERNUM));
                String playerPic = cursor.getString(cursor.getColumnIndex(CoachesCornerDBContract.PlayerDBEntry.COLUMN_PLAYERPIC));
                int goalScored = cursor.getInt(cursor.getColumnIndex(CoachesCornerDBContract.ScoreDBEntry.COLUMN_GOALCOUNT));
                int assitsMade = cursor.getInt(cursor.getColumnIndex(CoachesCornerDBContract.ScoreDBEntry.COLUMN_ASSITCOUNT));
                int savesMade = cursor.getInt(cursor.getColumnIndex(CoachesCornerDBContract.ScoreDBEntry.COLUMN_SAVESCOUNT));
                String note = cursor.getString(cursor.getColumnIndex(CoachesCornerDBContract.PlayerDBEntry.COLUMN_PLAYERNOTE));

                mPlayers.add(new Player(playerId, playerName, playerNum, playerPic, goalScored, assitsMade, savesMade, note));
            }

            if (!mPlayers.isEmpty()) {
                mScoreCardAdapter.setPlayers(mPlayers);
            }

        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }
    };

    @Override
    public void onClick(Player player) {
        FragmentManager fragmentManager = getFragmentManager();
        UpdatePlayerScoreDialogFragment updatePlayerScoreDialog = new UpdatePlayerScoreDialogFragment();
        updatePlayerScoreDialog.setPlayer(player);
        updatePlayerScoreDialog.setDialogTitle("Update Player Score");
        updatePlayerScoreDialog.show(fragmentManager, "Update Score");

        Toast.makeText(mContext, "Clicked on Player - " + player.getPlayerName(), Toast.LENGTH_SHORT).show();

    }


}
