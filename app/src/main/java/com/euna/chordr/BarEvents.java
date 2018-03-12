package com.euna.chordr;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * This Class handles everything pertaining to playing
 * sound files
 */

public class BarEvents extends ItemTouchHelper.SimpleCallback {

    BarAdapter barAdapter;

    public BarEvents(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    public BarEvents(BarAdapter barAdapter) {
        super(ItemTouchHelper.ACTION_STATE_DRAG | ItemTouchHelper.DOWN, ItemTouchHelper.START | ItemTouchHelper.END |ItemTouchHelper.LEFT | ItemTouchHelper.ACTION_STATE_SWIPE | ItemTouchHelper.ANIMATION_TYPE_SWIPE_SUCCESS | 4);
        this.barAdapter = barAdapter;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
        barAdapter.removeBarFromProgression(viewHolder.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        barAdapter.removeBarFromProgression(viewHolder.getAdapterPosition());
    }


}
