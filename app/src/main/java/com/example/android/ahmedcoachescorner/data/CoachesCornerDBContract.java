package com.example.android.ahmedcoachescorner.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;



public class CoachesCornerDBContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.ahmedcoachescorner";
    public static final String PATH_GAME = "game";
    public static final String PATH_PLAYER = "player";
    public static final String PATH_SCORE = "score";
    public static final Uri CC_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class GameDBEntry implements BaseColumns {

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAME;

        public static final String TABLE_NAME = "Games";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_GAMEDATE = "gameDate";
        public static final String COLUMN_GAMETIME = "gameTime";
        public static final String COLUMN_OPPONENTNAME = "opponentName";
        public static final String COLUMN_OPPONENTSCORE = "opponentScore";
        public static final String COLUMN_HOMEORAWAY = "homeOrAway";
        public static final String COLUMN_GAMENOTE = "gameNote";
        public static final String COLUMN_FIELDLOCATION = "fieldLocation";

        public static final Uri CONTENT_URI = CC_CONTENT_URI.buildUpon()
                .appendPath(PATH_GAME)
                .build();

        public static final Uri buildGameUriWithId(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }
    }

    public static final class PlayerDBEntry implements BaseColumns {

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYER;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLAYER;

        public static final String TABLE_NAME = "Players";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PLAYERNAME = "name";
        public static final String COLUMN_PLAYERNUM = "playernum";
        public static final String COLUMN_PLAYERPIC = "pic";
        public static final String COLUMN_PLAYERNOTE = "note";

        public static final Uri CONTENT_URI = CC_CONTENT_URI.buildUpon()
                .appendPath(PATH_PLAYER)
                .build();

        public static final Uri buildPlayerUriWithId(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }
    }

    public static final class ScoreDBEntry implements BaseColumns {

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCORE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCORE;

        public static final String TABLE_NAME = "Scores";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_GAMEID = "game_Id";
        public static final String COLUMN_PLAYERID = "player_Id";
        public static final String COLUMN_GOALCOUNT = "goals";
        public static final String COLUMN_ASSITCOUNT = "assist";
        public static final String COLUMN_SAVESCOUNT = "saves";

        public static final Uri CONTENT_URI = CC_CONTENT_URI.buildUpon()
                .appendPath(PATH_SCORE)
                .build();



        public static final Uri buildScoreUriWithId(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }

        // content://com.example.android.ahmedcoachescorner/score?game_Id=#
        public static final Uri buildScoreUriwithGameId(int gameId) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_GAMEID, Integer.toString(gameId))
                    .build();
        }

        // content://com.example.android.ahmedcoachescorner/score?player_Id=#
        public static final Uri buildScoreUriwithPlayerId(int playerId) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_PLAYERID, Integer.toString(playerId))
                    .build();
        }

        // content://com.example.android.ahmedcoachescorner/score?game_Id=#&player_Id=#
        public static final Uri buildScoreUriwithGameIdPlayerId(int gameId, int playerId) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_GAMEID, Integer.toString(gameId))
                    .appendQueryParameter(COLUMN_PLAYERID, Integer.toString(playerId))
                    .build();
        }


    }

}
