package org.bangeek.whichway.adapters;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.bangeek.whichway.R;
import org.bangeek.whichway.models.CardViewHolder;
import org.bangeek.whichway.models.LineCard;
import org.bangeek.whichway.models.LineCardSortCallback;

import java.util.List;

/**
 * Created by Bin on 2016/9/5.
 */

public class CardAdapter extends RecyclerView.Adapter {
    private SortedList<LineCard> mCardList;

    public CardAdapter() {
        mCardList = new SortedList<>(LineCard.class, new LineCardSortCallback(this));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.card_way, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(2, 1, 2, 1);
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

    /**
     * Add item
     *
     * @param item LineCard
     */
    public void addItem(LineCard item) {
        mCardList.add(item);
    }

    /**
     * Add item or update item
     *
     * @param item LineCard
     */
    public void addOrUpdateItem(LineCard item) {
        if (item == null || item.getLine() == null) return;
        //search index
        for (int i = 0; i < mCardList.size(); i++) {
            if (mCardList.get(i) != null && item.getLine().equals(mCardList.get(i).getLine())) {
                if (mCardList.get(i).getTime1Value() == item.getTime1Value())
                    break;
                mCardList.beginBatchedUpdates();
                mCardList.removeItemAt(i);
                mCardList.add(item);
                mCardList.endBatchedUpdates();
                return;
            }
        }
        mCardList.add(item);
    }

    /**
     * Add Items
     *
     * @param list list
     */
    public void addItems(List<LineCard> list) {
        mCardList.beginBatchedUpdates();
        mCardList.addAll(list);
        mCardList.endBatchedUpdates();
    }
}
