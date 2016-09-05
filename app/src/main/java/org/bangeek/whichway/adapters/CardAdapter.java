package org.bangeek.whichway.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.bangeek.whichway.R;
import org.bangeek.whichway.models.CardViewHolder;
import org.bangeek.whichway.models.LineCard;

import java.util.List;

/**
 * Created by Bin on 2016/9/5.
 */

public class CardAdapter extends RecyclerView.Adapter {
    private List<LineCard> mCardList;

    public CardAdapter(List<LineCard> list) {
        mCardList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.card_way, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CardViewHolder cardViewHolder = (CardViewHolder) holder;
        cardViewHolder.setData(mCardList.get(position));
    }

    @Override
    public int getItemCount() {
        return mCardList != null ? mCardList.size() : 0;
    }

    public void updateData(List<LineCard> list) {
        mCardList = list;
        notifyDataSetChanged();
    }
}
