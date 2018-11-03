package com.example.android.ahmedcoachescorner.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.ahmedcoachescorner.R;
import com.example.android.ahmedcoachescorner.common.Utils;
import com.example.android.ahmedcoachescorner.data.CoachesCornerDBContract;
import com.example.android.ahmedcoachescorner.data.Game;
import com.example.android.ahmedcoachescorner.ui.dialogs.UpdatePlayerScoreDialogFragment;
import com.example.android.ahmedcoachescorner.ui.fragments.GameDayFieldLocationFragment;
import com.example.android.ahmedcoachescorner.ui.fragments.GameDayScoreCardFragment;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameDayActivity extends AppCompatActivity
        implements UpdatePlayerScoreDialogFragment.UpdatePlayerScoreDialogListener {

    private Game mGame;
    private String mTeamName;
    private int mGameTime;
    private int mSubTime;
    private long mRunningGameTimer;
    private CountDownTimer mGameCountDownTimer;
    private CountDownTimer mSubCountDownTimer;
    private Boolean isTimerRunning = false;
    private MediaPlayer mMediaPlayer;

    GameDayScoreCardFragment mScoreCardFragment;
    GameDayFieldLocationFragment mFieldLocationFragment;
    ViewPager mViewPager;
    GameDayViewPagerAdapter mGameDayPagerAdapter;
    TabLayout mTabLayout;

    private Context mContext;

    @BindView(R.id.tv_game_day_home_team_name) TextView mHomeTeamName;
    @BindView(R.id.tv_game_day_home_team_score) TextView mHomeTeamScore;
    @BindView(R.id.tv_game_day_away_team_name) TextView mAwayTeamName;
    @BindView(R.id.tv_game_day_away_team_score) TextView mAwayTeamScore;
    @BindView(R.id.ib_increase_opponent_score) ImageButton mIncreaseOpponentScore;
    @BindView(R.id.ib_decrease_opponent_score) ImageButton mDecreaseOpponentScore;
    @BindView(R.id.tv_timer) TextView mTimerTextView;
    @BindView(R.id.btn_start_timer) Button mBtnStartTimer;
    @BindView(R.id.tv_sub_timer) TextView mSubTimerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_day);
        ButterKnife.bind(this);

        mContext = getApplicationContext();

        mMediaPlayer = new MediaPlayer();

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra("Game")) {
            mGame = intentThatStartedThisActivity.getParcelableExtra("Game");
        }

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Utils.CC_PREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(Utils.PREF_TEAM_NAME)) {
            mTeamName = sharedPreferences.getString(Utils.PREF_TEAM_NAME, Utils.DEFAULT_TEAM_NAME);
            mGameTime = sharedPreferences.getInt(Utils.PREF_GAME_TIME, 30);
            mSubTime = sharedPreferences.getInt(Utils.PREF_SUB_TIME, 6);
        }

        mTimerTextView.setText(String.valueOf(mGameTime));
        mSubTimerTextView.setText(String.valueOf(mSubTime));

        if (mGame.getHomeOrAway().equals("Home")) {
            mHomeTeamName.setText(mTeamName);
            mHomeTeamScore.setText(String.valueOf(mGame.getTeamScore()));
            mAwayTeamName.setText(mGame.getOpponentName());
            mAwayTeamScore.setText(String.valueOf(mGame.getOpponentScore()));
        } else {
            mHomeTeamName.setText(mGame.getOpponentName());
            mHomeTeamScore.setText(String.valueOf(mGame.getOpponentScore()));
            mAwayTeamName.setText(mTeamName);
            mAwayTeamScore.setText(String.valueOf(mGame.getTeamScore()));
        }

        mIncreaseOpponentScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGame.addOpponentScore();
                updateOpponentScore();
            }
        });

        mDecreaseOpponentScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGame.subtractOpponentScore();
                updateOpponentScore();
            }
        });


        mBtnStartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTimerRunning = true;
                mBtnStartTimer.setEnabled(false);
                startTimer();
                startSubTimer(mSubTime);
            }
        });


        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            mScoreCardFragment = (GameDayScoreCardFragment) fragmentManager.getFragment(savedInstanceState, GameDayScoreCardFragment.class.getName());
            mFieldLocationFragment = (GameDayFieldLocationFragment) fragmentManager.getFragment(savedInstanceState, GameDayFieldLocationFragment.class.getName());
            FragmentTransaction remove = fragmentManager.beginTransaction();
            remove.remove(mScoreCardFragment);
            remove.remove(mFieldLocationFragment);
            if (!remove.isEmpty()) {
                remove.commit();
                fragmentManager.executePendingTransactions();
            }
        } else {
            mScoreCardFragment = new GameDayScoreCardFragment();
            mFieldLocationFragment = new GameDayFieldLocationFragment();
        }

        mViewPager = (ViewPager) findViewById(R.id.gamedaypager);
        mGameDayPagerAdapter = new GameDayViewPagerAdapter(getSupportFragmentManager());

        mGameDayPagerAdapter.addFragment(mScoreCardFragment, "Score Card");
        mGameDayPagerAdapter.addFragment(mFieldLocationFragment, "Field Location");
        mViewPager.setAdapter(mGameDayPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.gamedaytabs);
        mTabLayout.setupWithViewPager(mViewPager);



    }


    private void updateOpponentScore() {
        if (mGame.getHomeOrAway().equals("Home")) {
            mAwayTeamScore.setText(String.valueOf(mGame.getOpponentScore()));
        } else {
            mHomeTeamScore.setText(String.valueOf(mGame.getOpponentScore()));
        }
        updateGameDetails();

    }


    //This inflates the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gameday_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.gd_menu_reset_timer:
                if (isTimerRunning) {
                    mGameCountDownTimer.cancel();
                    mSubCountDownTimer.cancel();
                    mTimerTextView.setText(String.valueOf(mGameTime));
                    mSubTimerTextView.setText(String.valueOf(mSubTime));
                    mBtnStartTimer.setEnabled(true);
                    isTimerRunning = false;
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                    }
                    mMediaPlayer.release();
                }
                break;
            case R.id.gd_menu_share_score_card:
                break;
            case android.R.id.home:
                if (isTimerRunning) {
                    mGameCountDownTimer.cancel();
                    mSubCountDownTimer.cancel();
                }
                mMediaPlayer.release();
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void startTimer() {

        mGameCountDownTimer = new CountDownTimer(TimeUnit.MINUTES.toMillis(mGameTime), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mRunningGameTimer = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                String timer = String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                mTimerTextView.setText(timer);
            }

            @Override
            public void onFinish() {
                mBtnStartTimer.setEnabled(true);
                Toast.makeText(mContext, "Timer is up", Toast.LENGTH_SHORT).show();
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                }
            }
        }.start();



    }

    private void startSubTimer(int subTime) {
        mSubCountDownTimer = new CountDownTimer(TimeUnit.MINUTES.toMillis(subTime), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String subTimer = String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                mSubTimerTextView.setText(subTimer);
            }

            @Override
            public void onFinish() {
                playBeep();
                Toast.makeText(mContext, "Sub Timer is up", Toast.LENGTH_SHORT).show();
                if (mRunningGameTimer > mSubTime) {
                    startSubTimer(mSubTime);
                } else {
                    startSubTimer((int) mRunningGameTimer);
                }
            }
        }.start();
    }

    private void playBeep() {


        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = new MediaPlayer();
            }

            AssetFileDescriptor descriptor = getAssets().openFd("beep.mp3");
            mMediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mMediaPlayer.prepare();
            mMediaPlayer.setVolume(1f, 1f);
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean updateGameDetails() {

        ContentValues values = new ContentValues();
        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_GAMEDATE, mGame.getGameDate());
        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_GAMETIME, mGame.getGameTime());
        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_OPPONENTNAME, mGame.getOpponentName());
        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_OPPONENTSCORE, mGame.getOpponentScore());
        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_HOMEORAWAY, mGame.getHomeOrAway());
        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_GAMENOTE, mGame.getGameNote());
        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_FIELDLOCATION, mGame.getFieldLocation());
        String selection = Integer.toString(mGame.getGameId());
        Uri updateUri = CoachesCornerDBContract.GameDBEntry.buildGameUriWithId(mGame.getGameId());

        int count = getContentResolver().update(updateUri, values, selection, null);
        if (count == 0) {
            Toast.makeText(this, "Failed to update Game Table", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, GameDayScoreCardFragment.class.getName(), mScoreCardFragment);
        getSupportFragmentManager().putFragment(outState, GameDayFieldLocationFragment.class.getName(), mFieldLocationFragment);
    }

    @Override
    public void onFinishedUpdatePlayerScore(int playerId, int currentGoalCount, int goals, int assists, int saves) {

        int score;
        int newGoals;
        if (goals >= currentGoalCount) {
            newGoals = goals - currentGoalCount;
        } else {
            newGoals = currentGoalCount - goals;
        }

        if (playerId != 0 && mGame != null) {
            if (mGame.getHomeOrAway().equals("Home")) {
                score = Integer.parseInt(mHomeTeamScore.getText().toString());
                score = score + newGoals;
                mHomeTeamScore.setText(String.valueOf(score));
            } else {
                score = Integer.parseInt(mAwayTeamScore.getText().toString());
                score = score + newGoals;
                mAwayTeamScore.setText(String.valueOf(score));
            }
            boolean loadedSuccesfully = Utils.insertPlayerScore(this, mGame.getGameId(), playerId, goals, assists, saves);
            if (loadedSuccesfully) {
                mViewPager.getAdapter().notifyDataSetChanged();
//                mGameDayPagerAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Score Added to Database", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add score to Database", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
