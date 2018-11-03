package com.example.android.ahmedcoachescorner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.ahmedcoachescorner.R;
import com.example.android.ahmedcoachescorner.data.Player;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



public class ScoreCardAdapter extends RecyclerView.Adapter<ScoreCardAdapter.ScoreCardViewHolder> {

    private static final String TAG = ScoreCardAdapter.class.getSimpleName();

    private ArrayList<Player> mPlayers;
    private Context mContext;

    private final ScoreCardAdapterOnClickHandler mClickHandler;

    public interface ScoreCardAdapterOnClickHandler {
        void onClick(Player player);
    }

    public ScoreCardAdapter(Context context, ScoreCardAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }


    public class ScoreCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_player_picture) ImageView mPlayerPicture;
        @BindView(R.id.tv_player_name) TextView mPlayerName;
        @BindView(R.id.tv_player_number) TextView mPlayerNum;
        @BindView(R.id.tv_goals_scored) TextView mGoalsScored;
        @BindView(R.id.tv_assits_made) TextView mAssitsMade;
        @BindView(R.id.tv_saves_made) TextView mSavesMade;

        public ScoreCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Player player = new Player(
                    mPlayers.get(adapterPosition).getPlayerId(),
                    mPlayers.get(adapterPosition).getPlayerName(),
                    mPlayers.get(adapterPosition).getPlayerNum(),
                    mPlayers.get(adapterPosition).getPlayerPic(),
                    mPlayers.get(adapterPosition).getGoalCount(),
                    mPlayers.get(adapterPosition).getAssitCount(),
                    mPlayers.get(adapterPosition).getSaveCount(),
                    mPlayers.get(adapterPosition).getPlayerNote());
            mClickHandler.onClick(player);
        }
    }
    public ScoreCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View scoreView = inflater.inflate(R.layout.player_card_item, parent, false);
        return new ScoreCardViewHolder(scoreView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreCardViewHolder holder, int position) {
        holder.mPlayerName.setText(mPlayers.get(position).getPlayerName());
        holder.mPlayerNum.setText(mPlayers.get(position).getPlayerNum());
        holder.mGoalsScored.setText(String.valueOf(mPlayers.get(position).getGoalCount()));
        holder.mAssitsMade.setText(String.valueOf(mPlayers.get(position).getAssitCount()));
        holder.mSavesMade.setText(String.valueOf(mPlayers.get(position).getSaveCount()));

    }

    @Override
    public int getItemCount() {
        if (mPlayers == null) {
            return 0;
        }
        return mPlayers.size();
    }

    public void setPlayers(ArrayList<Player> players) {
        mPlayers = players;
        notifyDataSetChanged();
    }
}
