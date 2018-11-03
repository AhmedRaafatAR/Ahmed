package com.example.android.ahmedcoachescorner.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.database.Cursor;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.ahmedcoachescorner.R;
import com.example.android.ahmedcoachescorner.adapter.GameAdapter;
import com.example.android.ahmedcoachescorner.common.Utils;
import com.example.android.ahmedcoachescorner.data.CoachesCornerDBContract;
import com.example.android.ahmedcoachescorner.data.Game;
import com.example.android.ahmedcoachescorner.helpers.GameRecyclerItemTouchHelper;
import com.example.android.ahmedcoachescorner.ui.GameAddActivity;
import com.example.android.ahmedcoachescorner.ui.GameDayActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CoachesCornerScheduleFragment extends Fragment
    implements GameRecyclerItemTouchHelper.GameItemTouchHelperListener,
        GameAdapter.GameAdapterOnClickHandler {

    private static final int ID_GAME_LOADER = 3;

    private static final String PARCELABLEKEY = "Games";

    private GameAdapter mGameAdapter;
    private ArrayList<Game> mGames;
    @BindView(R.id.recyclerview_schedule) RecyclerView mGameRecyclerView;
    @BindView(R.id.tv_no_games_added) TextView mNoGameMsgTextView;
    @BindView(R.id.fab_add_game) FloatingActionButton mAddGameFAB;
    @BindView(R.id.schedule_view) CoordinatorLayout mCoordinatorLayout;
    private Context mContext;
    private String mTeamName;
    private View mViewGames;

    public CoachesCornerScheduleFragment() {
        //Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("CoachCornerScheduleFragment", "in OnCreate");




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewGames = inflater.inflate(R.layout.fragment_coaches_corner_schedule, container, false);
        ButterKnife.bind(this, mViewGames);

        mContext = mViewGames.getContext();



        return mViewGames;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadGameData();
        Log.d("ScheduleFragment", "in onResume");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("CoachCornerScheduleFragment", "in OnSaveInstanceState ");
        outState.putParcelableArrayList(PARCELABLEKEY, mGames);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof GameAdapter.GameViewHolder) {

            String opponentName = mGames.get(viewHolder.getAdapterPosition()).getOpponentName();

            final Game deleteGame = mGames.get(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();

            boolean success = Utils.deleteGameFromDatabasebyID(mContext, deleteGame.getGameId());

            if (success) {
                mGameAdapter.removeGame(viewHolder.getAdapterPosition());
                Snackbar snackbar = Snackbar.make(mCoordinatorLayout, opponentName + " vs " + mTeamName + " removed from schedule!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mGameAdapter.restoreGame(deleteGame, deleteIndex);
                        boolean addGameSuccess = Utils.addGameToDatabase(mContext, deleteGame);
                        if (!addGameSuccess) {
                            Log.d("CoachesCornerScheduleFragment", "Failed to add game back from UNDO");
                        }
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        }
    }

    private void loadGameData() {

        mTeamName = Utils.getTeamName(mContext);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mGameRecyclerView.setLayoutManager(layoutManager);
        mGameRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mGameRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        mGameAdapter = new GameAdapter(mContext, this);
        mGameRecyclerView.setAdapter(mGameAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new GameRecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mGameRecyclerView);

        mAddGameFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addGameIntent = new Intent(mContext, GameAddActivity.class);
                startActivity(addGameIntent);
            }
        });


        mGames = new ArrayList<>();
        getLoaderManager().initLoader(ID_GAME_LOADER, null, loaderCallbackCursorLoader);
    }

    private void showGameData() {
        mGameRecyclerView.setVisibility(View.VISIBLE);
        mNoGameMsgTextView.setVisibility(View.INVISIBLE);
    }

    private void showAddGameMsg() {
        mGameRecyclerView.setVisibility(View.INVISIBLE);
        mNoGameMsgTextView.setVisibility(View.VISIBLE);
    }

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbackCursorLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            Uri gameUri = CoachesCornerDBContract.GameDBEntry.CONTENT_URI;

            return new CursorLoader(mContext,
                    gameUri,
                    null,
                    null,
                    null,
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

            if (cursor == null || cursor.getCount() < 1) {
                showAddGameMsg();
                return;
            }

            mGames.clear();

            while (cursor.moveToNext()) {
                int gameId = cursor.getInt(cursor.getColumnIndex(CoachesCornerDBContract.GameDBEntry._ID));
                String gameDate = cursor.getString(cursor.getColumnIndex(CoachesCornerDBContract.GameDBEntry.COLUMN_GAMEDATE));
                String gameTime = cursor.getString(cursor.getColumnIndex(CoachesCornerDBContract.GameDBEntry.COLUMN_GAMETIME));
                String opponentName = cursor.getString(cursor.getColumnIndex(CoachesCornerDBContract.GameDBEntry.COLUMN_OPPONENTNAME));
                int opponentScore = cursor.getInt(cursor.getColumnIndex(CoachesCornerDBContract.GameDBEntry.COLUMN_OPPONENTSCORE));
                String homeOrAway = cursor.getString(cursor.getColumnIndex(CoachesCornerDBContract.GameDBEntry.COLUMN_HOMEORAWAY));
                int teamScore = cursor.getInt(cursor.getColumnIndex(CoachesCornerDBContract.ScoreDBEntry.COLUMN_GOALCOUNT));
                String gameNote = cursor.getString(cursor.getColumnIndex(CoachesCornerDBContract.GameDBEntry.COLUMN_GAMENOTE));
                String fieldLocation = cursor.getString(cursor.getColumnIndex(CoachesCornerDBContract.GameDBEntry.COLUMN_FIELDLOCATION));

                mGames.add(new Game(gameId, gameDate, gameTime, opponentName, opponentScore, teamScore, homeOrAway, gameNote, fieldLocation));

            }

            if (!mGames.isEmpty()) {
                showGameData();
                mGameAdapter.setGames(mGames);
            } else {
                showAddGameMsg();
            }

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    @Override
    public void onClick(Game game) {
        Intent intentGameDay = new Intent(mContext, GameDayActivity.class);
        intentGameDay.putExtra("Game", game);
        startActivity(intentGameDay);
    }

}
