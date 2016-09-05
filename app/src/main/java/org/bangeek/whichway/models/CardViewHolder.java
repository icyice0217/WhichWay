package org.bangeek.whichway.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.bangeek.whichway.R;

/**
 * Created by Bin on 2016/9/5.
 */

public class CardViewHolder extends RecyclerView.ViewHolder {
    private TextView mTvLine;
    private TextView mTvStop;
    private TextView mTvTime1;
    private TextView mTvTime2;

    public CardViewHolder(View itemView) {
        super(itemView);

        mTvLine = (TextView) itemView.findViewById(R.id.tvLine);
        mTvStop = (TextView) itemView.findViewById(R.id.tvStop);
        mTvTime1 = (TextView) itemView.findViewById(R.id.tvTime1);
        mTvTime2 = (TextView) itemView.findViewById(R.id.tvTime2);
    }

    public void setLine(CharSequence charSequence) {
        mTvLine.setText(charSequence);
    }

    public void setStop(CharSequence charSequence) {
        mTvStop.setText(charSequence);
    }

    public void setTime1(CharSequence charSequence) {
        mTvTime1.setText(charSequence);
    }

    public void setTime2(CharSequence charSequence) {
        mTvTime2.setText(charSequence);
    }

    public void setData(LineCard lineCard) {
        mTvLine.setText(lineCard.getLine());
        mTvStop.setText(lineCard.getStop());
        mTvTime1.setText(lineCard.getTime1());
        mTvTime2.setText(lineCard.getTime2());
    }
}
