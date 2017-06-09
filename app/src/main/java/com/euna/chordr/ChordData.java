package com.euna.chordr;

/**
 * This Class handles everything pertaining to playing
 * sound files
 */

public class ChordData {
    //private ChordData* prev;

    private String chord;
    private int beat;

    public ChordData(String x, int y){
        this.chord = x;
        this.beat = y;
    }

    public void setChord(String x){
        this.chord = x;
    }

    public void setBeat(int x){
        this.beat = x;
    }
}
