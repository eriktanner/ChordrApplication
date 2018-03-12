package com.euna.chordr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * This Class handles everything pertaining to playing
 * sound files
 */

public class BarAdapter extends RecyclerView.Adapter<BarAdapter.BarViewHolder> {

    private LayoutInflater layoutInflater;
    List<Bar> listOfBars = Collections.emptyList();
    BarAdapterInterface barAdapterListener;

    public interface BarAdapterInterface {
        void barAdapterOnBarRemoved(int pos);
        void barAdapterOnBarClicked(int pos);
        void barAdapterOnBarLongClicked(int pos);
    }


    public BarAdapter(ProgressionManager FragmentContext, Context context, List<Bar> listOfBars) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.listOfBars = listOfBars;
        barAdapterListener = (BarAdapterInterface) FragmentContext;
    }

    @Override
    public BarViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.bar_layout, viewGroup, false);
        BarViewHolder barViewHolder = new BarViewHolder(view);
        return barViewHolder;
    }

    @Override
    public void onBindViewHolder(BarViewHolder viewHolder, int i) {
        Bar currentBar = listOfBars.get(i);
        viewHolder.barText.setText(currentBar.getMainText());
        viewHolder.barInterval.setText(currentBar.getIntervalText());
        //viewHolder.barImage.setImageResource(currentBar.getIconId());
    }



    /* Deletes Bar*/
    public void removeBarFromProgression(int pos) {
        listOfBars.remove(pos);
        notifyItemRemoved(pos);
        barAdapterListener.barAdapterOnBarRemoved(pos);
    }

    public void notifyBarClicked(int pos) {
        barAdapterListener.barAdapterOnBarClicked(pos);
    }

    public void notifyBarLongClicked(int pos) {
        barAdapterListener.barAdapterOnBarLongClicked(pos);
    }

    public void updateBarIntervals(String[] newIntervals) {
        for (int i = 0; i < listOfBars.size(); i++) {
            listOfBars.get(i).setIntervalText(newIntervals[i]);
        }
        notifyDataSetChanged();
    }




    @Override
    public int getItemCount() {
        return listOfBars.size();
    }

    class BarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView barImage;
        TextView barText;
        TextView barInterval;

        public BarViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setClickable(true);
            itemView.setOnLongClickListener(this);

            //barImage = (ImageView) itemView.findViewById(R.id.ivBarImage);
            barText = (TextView) itemView.findViewById(R.id.tvBarText);
            barInterval = (TextView) itemView.findViewById(R.id.tvBarInterval);
        }


        public void updateBarInterval(String newInterval) {
            barInterval.setText(newInterval);
        }


        @Override
        public void onClick(View view) {
            notifyBarClicked(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            notifyBarLongClicked(getAdapterPosition());
            return true;
        }
    }
}
