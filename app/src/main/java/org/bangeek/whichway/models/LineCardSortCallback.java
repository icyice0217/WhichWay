package org.bangeek.whichway.models;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;

import org.bangeek.whichway.utils.CommonUtil;

/**
 * Created by BinGan on 2016/9/7.
 */
public class LineCardSortCallback extends SortedListAdapterCallback<LineCard> {
    /**
     * Creates a {@link SortedList.Callback} that will forward data change events to the provided
     * Adapter.
     *
     * @param adapter The Adapter instance which should receive events from the SortedList.
     */
    public LineCardSortCallback(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    @Override
    public int compare(LineCard item1, LineCard item2) {
        if (item1 == null || item2 == null) return 0;
        int i = Integer.compare(item1.getTime1Value(), item2.getTime1Value());
        return i == 0 ? Integer.compare(item1.getTime2Value(), item2.getTime2Value()) : i;
    }

    @Override
    public boolean areContentsTheSame(LineCard item1, LineCard item2) {
        if (item1 == item2) return true;
        if (item1 == null || item2 == null) return false;
        if (!CommonUtil.isEqual(item1.getLine(), item2.getLine())) return false;
        if (!CommonUtil.isEqual(item1.getTime1(), item2.getTime1())) return false;
        if (!CommonUtil.isEqual(item1.getTime2(), item2.getTime2())) return false;
        if (!CommonUtil.isEqual(item1.getStop(), item2.getStop())) return false;
        return true;
    }

    @Override
    public boolean areItemsTheSame(LineCard item1, LineCard item2) {
        return item1 == item2 || !(item1 == null || item2 == null)
                && CommonUtil.isEqual(item1.getLine(), item2.getLine());
    }
}
