package com.example.android.ahmedcoachescorner.data;

import android.os.Parcel;
import android.os.Parcelable;



public class Player implements Parcelable {

    private int mPlayerId;
    private String mPlayerName;
    private String mPlayerNum;
    private String mPlayerPic;
    private int mGoalCount = 0;
    private int mAssitCount = 0;
    private int mSaveCount = 0;
    private String mPlayerNote;

    public Player(int playerId, String playerName, String playerNum, String playerPic, int goalCount, int assitCount, int saveCount, String note) {
        mPlayerId = playerId;
        mPlayerName = playerName;
        mPlayerNum = playerNum;
        mPlayerPic = playerPic;
        mGoalCount = goalCount;
        mAssitCount = assitCount;
        mSaveCount = saveCount;
        mPlayerNote = note;
    }

    public Player() {

    }

    private Player(Parcel in) {
        mPlayerId = in.readInt();
        mPlayerName = in.readString();
        mPlayerNum = in.readString();
        mPlayerPic = in.readString();
        mGoalCount = in.readInt();
        mAssitCount = in.readInt();
        mSaveCount = in.readInt();
        mPlayerNote = in.readString();
    }

    public void setPlayerName(String name) {
        mPlayerName = name;
    }

    public void setPlayerNumber(String num) {
        mPlayerNum = num;
    }

    public void setPlayerPic(String pic) {
        mPlayerPic = pic;
    }

    public void setPlayerGoals(int goals) {
        mGoalCount = goals;
    }

    public void setPlayerAssits(int assits) {
        mAssitCount = assits;
    }

    public void setSaveCount(int saves) {
        mSaveCount = saves;
    }


    public void setPlayerNote(String note) {
        mPlayerNote = note;
    }


    public int getPlayerId() {
        return mPlayerId;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    public String getPlayerNum() {
        return mPlayerNum;
    }

    public String getPlayerPic() {
        return mPlayerPic;
    }

    public int getGoalCount() {
        return mGoalCount;
    }

    public int getAssitCount() {
        return mAssitCount;
    }

    public int getSaveCount() {
        return mSaveCount;
    }

    public String getPlayerNote() {
        return mPlayerNote;
    }

    public void addGoal() {
        mGoalCount++;
    }
    public void subtractGoal(){
        if (mGoalCount > 0) {
            mGoalCount--;
        }
    }
    public void addAssit() {
        mAssitCount++;
    }
    public void subtractAssit(){
        if (mAssitCount > 0) {
            mAssitCount--;
        }
    }
    public void addSaves() {
        mSaveCount++;
    }
    public void subtractSaves(){
        if (mSaveCount > 0) {
            mSaveCount--;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mPlayerId);
        parcel.writeString(mPlayerName);
        parcel.writeString(mPlayerNum);
        parcel.writeString(mPlayerPic);
        parcel.writeInt(mGoalCount);
        parcel.writeInt(mAssitCount);
        parcel.writeInt(mSaveCount);
        parcel.writeString(mPlayerNote);
    }

    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

}

