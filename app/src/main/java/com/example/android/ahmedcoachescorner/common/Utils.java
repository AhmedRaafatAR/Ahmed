package com.example.android.ahmedcoachescorner.common;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.example.android.ahmedcoachescorner.data.CoachesCornerDBContract;
import com.example.android.ahmedcoachescorner.data.Game;
import com.example.android.ahmedcoachescorner.data.Player;



public class Utils {

    public static final String CC_PREFERENCES = "coachesCornerPreferences";
    public static final String PREF_TEAM_NAME = "teamName";
    public static final String PREF_COACH_NAME = "coachName";
    public static final String PREF_ASSIST1_NAME = "assist1Name";
    public static final String PREF_ASSIST2_NAME = "assist2Name";
    public static final String PREF_AGE_GROUP = "ageGroup";
    public static final String PREF_GAME_FORMAT = "gameFormat";
    public static final String PREF_GAME_TIME = "gameTime";
    public static final String PREF_GAME_TYPE = "gameType";
    public static final String PREF_SUB_TIME = "subTime";
    public static final String DEFAULT_TEAM_NAME = "TeamName";


//    public static final String[] PLAYER_PROJECTION = {
//            CoachesCornerDBContract.PlayerDBEntry._ID,
//            CoachesCornerDBContract.PlayerDBEntry.COLUMN_PLAYERNAME,
//            CoachesCornerDBContract.PlayerDBEntry.COLUMN_PLAYERNUM,
//            CoachesCornerDBContract.PlayerDBEntry.COLUMN_PLAYERPIC,
//            CoachesCornerDBContract.PlayerDBEntry.COLUMN_PLAYERNOTE};
//
//    public static final String[] GAME_PROJECTION = {
//            CoachesCornerDBContract.GameDBEntry._ID,
//            CoachesCornerDBContract.GameDBEntry.COLUMN_GAMEDATE,
//            CoachesCornerDBContract.GameDBEntry.COLUMN_GAMETIME,
////            CoachesCornerDBContract.GameDBEntry.COLUMN_HOMETEAMNAME,
////            CoachesCornerDBContract.GameDBEntry.COLUMN_HOMETEAMSCORE,
////            CoachesCornerDBContract.GameDBEntry.COLUMN_AWAYTEAMNAME,
////            CoachesCornerDBContract.GameDBEntry.COLUMN_AWAYTEAMSCORE,
//            CoachesCornerDBContract.GameDBEntry.COLUMN_GAMENOTE,
//            CoachesCornerDBContract.GameDBEntry.COLUMN_FIELDLOCATION};


    public static boolean addPlayerToDatabase(Context context, Player player) {

        ContentValues values = new ContentValues();
        values.put(CoachesCornerDBContract.PlayerDBEntry.COLUMN_PLAYERNAME, player.getPlayerName());
        values.put(CoachesCornerDBContract.PlayerDBEntry.COLUMN_PLAYERNUM, player.getPlayerNum());
        values.put(CoachesCornerDBContract.PlayerDBEntry.COLUMN_PLAYERPIC, "");

        Uri newUri = context.getContentResolver().insert(CoachesCornerDBContract.PlayerDBEntry.CONTENT_URI, values);

        if (newUri == null) {
            return false;
        }
        return true;

    }

    public static boolean insertPlayerScore(Context context, int gameId, int playerId, int goals, int assists, int saves) {
        ContentValues values = new ContentValues();
        values.put(CoachesCornerDBContract.ScoreDBEntry.COLUMN_GAMEID, gameId);
        values.put(CoachesCornerDBContract.ScoreDBEntry.COLUMN_PLAYERID, playerId);
        values.put(CoachesCornerDBContract.ScoreDBEntry.COLUMN_GOALCOUNT, goals);
        values.put(CoachesCornerDBContract.ScoreDBEntry.COLUMN_ASSITCOUNT, assists);
        values.put(CoachesCornerDBContract.ScoreDBEntry.COLUMN_SAVESCOUNT, saves);

        Uri scoreUri = context.getContentResolver().insert(CoachesCornerDBContract.ScoreDBEntry.CONTENT_URI, values);

        if (scoreUri == null) {
            return false;
        }
        return true;
    }

    public static boolean addGameToDatabase(Context context, Game game) {

        ContentValues values = new ContentValues();
        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_GAMEDATE, game.getGameDate());
        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_GAMETIME, game.getGameTime());
        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_OPPONENTNAME, game.getOpponentName());
        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_OPPONENTSCORE, game.getOpponentScore());
        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_HOMEORAWAY, game.getHomeOrAway());
        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_GAMENOTE, game.getGameNote());
        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_FIELDLOCATION, game.getFieldLocation());

        Uri newUri = context.getContentResolver().insert(CoachesCornerDBContract.GameDBEntry.CONTENT_URI, values);

        if (newUri == null) {
            return false;
        }
        return true;
    }

    public static boolean deletePlayerFromDatabasebyID(Context context, int playerID) {
        String selection = Integer.toString(playerID);
        Uri playerUri = CoachesCornerDBContract.PlayerDBEntry.buildPlayerUriWithId(playerID);
        int rowsDeleted = context.getContentResolver().delete(playerUri, selection, null);
        if (rowsDeleted == 0) {
            return false;
        }
        return true;

    }

    public static boolean deleteGameFromDatabasebyID(Context context, int gameID) {
        String selection = Integer.toString(gameID);
        Uri gameUri = CoachesCornerDBContract.GameDBEntry.buildGameUriWithId(gameID);
        int rowsDeleted = context.getContentResolver().delete(gameUri, selection, null);
        if (rowsDeleted == 0) {
            return false;
        }
        return true;

    }

    public static boolean updatePlayerToDatabase(Context context, Player player) {

        ContentValues values = new ContentValues();
        String selection = Integer.toString(player.getPlayerId());
        values.put(CoachesCornerDBContract.PlayerDBEntry.COLUMN_PLAYERNAME, player.getPlayerName());
        values.put(CoachesCornerDBContract.PlayerDBEntry.COLUMN_PLAYERPIC, "");
        values.put(CoachesCornerDBContract.PlayerDBEntry.COLUMN_PLAYERNUM, player.getPlayerNum());
//        values.put(CoachesCornerDBContract.PlayerDBEntry.COLUMN_GOALCOUNT, player.getGoalCount());
//        values.put(CoachesCornerDBContract.PlayerDBEntry.COLUMN_ASSITCOUNT, player.getAssitCount());
//        values.put(CoachesCornerDBContract.PlayerDBEntry.COLUMN_SAVESCOUNT, player.getSaveCount());
//        values.put(CoachesCornerDBContract.PlayerDBEntry.COLUMN_MINPLAYED, player.getMinPlayed());
        values.put(CoachesCornerDBContract.PlayerDBEntry.COLUMN_PLAYERNOTE, player.getPlayerNote());
        Uri playerUri = CoachesCornerDBContract.PlayerDBEntry.buildPlayerUriWithId(player.getPlayerId());
        int count = context.getContentResolver().update(playerUri, values, selection, null);
        if (count == 0) {
            return false;
        }
        return true;
    }


    public static boolean updateGameToDatabase(Context context, Game game) {

        ContentValues values = new ContentValues();
        String selection = Integer.toString(game.getGameId());
        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_GAMEDATE, game.getGameDate());
        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_GAMETIME, game.getGameTime());
//        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_HOMETEAMNAME, game.getHomeTeamName());
//        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_HOMETEAMSCORE, game.getHomeTeamScore());
//        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_AWAYTEAMNAME,game.getAwayTeamName());
 //       values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_AWAYTEAMSCORE, game.getAwayTeamScore());
        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_GAMENOTE, game.getGameNote());
        values.put(CoachesCornerDBContract.GameDBEntry.COLUMN_FIELDLOCATION, game.getFieldLocation());
        Uri gameUri = CoachesCornerDBContract.GameDBEntry.buildGameUriWithId(game.getGameId());
        int count = context.getContentResolver().update(gameUri, values, selection, null);
        if (count == 0) {
            return false;
        }
        return true;
    }

    public static int getStringArrayIndex(String[] stringArray, String value) {

        int position = 0;

        for (int cntr = 0; cntr < stringArray.length; cntr++) {
            if (stringArray[cntr] == value) {
                position = cntr;
                break;
            }
        }
        return position;

    }

    public static String getTeamName(Context context) {

        String teamName = DEFAULT_TEAM_NAME;

        SharedPreferences sharedPreferences = context.getSharedPreferences(Utils.CC_PREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(Utils.PREF_TEAM_NAME)) {
            teamName = sharedPreferences.getString(Utils.PREF_TEAM_NAME, Utils.DEFAULT_TEAM_NAME);
        }

        return teamName;

    }


}
