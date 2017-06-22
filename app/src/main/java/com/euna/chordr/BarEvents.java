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
        super(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.UP | ItemTouchHelper.DOWN);
        this.barAdapter = barAdapter;
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        barAdapter.removeBarFromProgression(viewHolder.getAdapterPosition());
    }


}
