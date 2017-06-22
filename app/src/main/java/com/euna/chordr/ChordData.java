package com.euna.chordr;

/**
 * This Class handles everything pertaining to playing
 * sound files
 */

public class ChordData {

    private String chord;
    private int numberOfBeats;
    private int interval;


    public ChordData(String chord, int numberOfBeats, int interval){
        this.chord = chord;
        this.numberOfBeats = numberOfBeats;
        this.interval = interval;
    }

    /*Returns solely chord name*/
    public String toString() {
        return chord;
    }

    /*Returns more info than toString(), such as number of numberOfBeats*/
    public String toStringExtended() {
        return chord + ":" + numberOfBeats + " ";
    }

    /*************************************** Setters *********************************************/

    public void setChord(String chord){
        this.chord = chord;
    }

    public void setNumberOfBeats(int numberOfBeats){
        this.numberOfBeats = numberOfBeats;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }


    /*************************************** Getters *********************************************/

    public String getChord(){
        return chord;
    }

    public int getNumberOfBeats(){
        return numberOfBeats;
    }

    public int getInterval() {
        return interval;
    }

    public String getConcatChord() {
        String finalString = "";
        if (chord.charAt(1) == '#') {

            if (chord.charAt(chord.length() - 1) != 'j')
                finalString += chord.substring(0, 4);
            else finalString += chord.substring(0, 2);
        }
        else {
            if (chord.charAt(chord.length() - 1) != 'j')
                finalString += chord.substring(0, 3);
            else finalString += chord.substring(0, 1);
        }

        return finalString;
    }

    public String getNoteOfChord() {
        String finalString = "";
        if (chord.charAt(1) == '#') {
            finalString += chord.substring(0, 2);
        } else finalString += chord.substring(0, 1);

        return finalString;
    }

    public String getScaleOfChord() {
        if (chord.charAt(chord.length() - 1) == 'j')
            return "maj";
        else if (chord.charAt(chord.length() - 1) == 'n')
            return "min";
        else return "dim";
    }

}


