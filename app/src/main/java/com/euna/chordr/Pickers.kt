package com.euna.chordr

import android.content.SharedPreferences
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner


/**
 * This Class handles everything pertaining to playing
 * sound files
 */
class Pickers(mainActivityIn: MainActivity) {

    internal var mainActivity: MainActivity? = null
    internal var savedState: SharedPreferences? = null
    internal val chords = Chords()

    //Key and Scale Pickers
    internal var spKeyPicker: Spinner? = null
    internal var spScalePicker: Spinner? = null
    internal var keyPickerIndexSelected = 3 //C Major
    internal var scalePickerIndexSelected = 0





    init {
        mainActivity = mainActivityIn
        savedState = mainActivityIn.savedState
    }


    /*Saves the state of the Pickers, for use on OnPause()*/
    fun savePickerIndices() {
        val editor = savedState!!.edit()
        editor.putInt("keyPickerIndex", keyPickerIndexSelected)
        editor.putInt("scalePickerIndex", scalePickerIndexSelected)
        //editor.putInt("chordPickerIndex", chordPickerIndexSelected)
    }

    /*Reinstates indices of our Pickers, for use on OnResume()*/
    fun reinstateSavedPickerIndices () {
        keyPickerIndexSelected = savedState!!.getInt("keyPickerIndex", keyPickerIndexSelected)
        scalePickerIndexSelected = savedState!!.getInt("scalePickerIndex", scalePickerIndexSelected)
        //chordPickerIndexSelected = savedState!!.getInt("chordPickerIndex", chordPickerIndexSelected)

        spKeyPicker!!.setSelection(keyPickerIndexSelected)
        spScalePicker!!.setSelection(scalePickerIndexSelected)
        //spChordPicker!!.setSelection(chordPickerIndexSelected)
    }


    /*Constructs Picker Views and sets to initial state*/
    fun initPickerListViews() {
        spKeyPicker = mainActivity?.findViewById(R.id.keyPicker) as Spinner
        val keyAdapt = ArrayAdapter(mainActivity, R.layout.support_simple_spinner_dropdown_item, chords.notes)
        spKeyPicker!!.adapter = keyAdapt
        spKeyPicker!!.setSelection(keyPickerIndexSelected)

        spScalePicker = mainActivity?.findViewById(R.id.scalePicker) as Spinner
        val scaleAdapt = ArrayAdapter(mainActivity, R.layout.support_simple_spinner_dropdown_item, chords.scale)
        spScalePicker?.adapter = scaleAdapt
        spScalePicker?.setSelection(scalePickerIndexSelected)

        //initializeChordSpinner()
    }

    /*Constructs Picker OnClickListeners*/
    fun initPickerOnClickListeners() {
        spKeyPicker!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                keyPickerIndexSelected = pos
                mainActivity?.updateOnKeyOrScaleChange()
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
        }

        spScalePicker!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                scalePickerIndexSelected = pos
                mainActivity?.updateOnKeyOrScaleChange()
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
        }

        /*spChordPicker!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                chordPickerIndexSelected = pos
                mainActivity?.playChord(ChordData(chordsInCurrentlyPickedKey!![chordPickerIndexSelected], mainActivity!!.numOfBeatsSelected, chordPickerIndexSelected + 1), 500)
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
        }*/
    }

    /*Creates a dynamic spinner, call in onCreate and onResume*/
    public fun initializeChordSpinner() {
        /*spChordPicker = mainActivity?.findViewById(R.id.chordPicker) as Spinner
        chordListForDropDown = chordsInCurrentlyPickedKey!!.toCollection(ArrayList<String>())
        chordAdapt = ArrayAdapter(mainActivity, R.layout.support_simple_spinner_dropdown_item, chordListForDropDown)
        spChordPicker!!.adapter = chordAdapt
        spChordPicker!!.setSelection(chordPickerIndexSelected)*/
    }



    /*Chords are dependent upon both the Key and the Scale we are in, there for
    * we must update the ChordSpinner every time either Key or Scale is changed*/
    fun updatePickersOnKeyOrScaleChange() {
        updateChordsInCurrentlyPickedKey()
        updateChordSpinner()
    }

    /*Updates the data array of the chords(used by ChordSpinner) to reflect the current Key and Scale we are in*/
    fun updateChordsInCurrentlyPickedKey() {
        val key = spKeyPicker?.selectedItem.toString()
        val scale = spScalePicker?.selectedItem.toString()
        //chordsInCurrentlyPickedKey = chords.findChordsInKey(chords, key, scale)
    }

    /*Updates the ChordSpinner after the scale has been changed, must be called after updateChordsInCurrentlyPickedKey()*/
    fun updateChordSpinner() {
        /*chordAdapt!!.clear()
        for(str in chordsInCurrentlyPickedKey!!) {
            chordAdapt!!.add(str)
        }
        chordAdapt!!.notifyDataSetChanged()

        chordPickerIndexSelected = 0
        spChordPicker!!.setSelection(chordPickerIndexSelected)*/
    }


}