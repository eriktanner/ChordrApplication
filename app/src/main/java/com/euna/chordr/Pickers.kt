package com.euna.chordr

import android.content.SharedPreferences
import android.view.View
import android.widget.*


/**
 * This class handles everything pertaining to selecting the key and scale we are in, and the
 * adding of the chords the users selects, and the saving of picker states
 */
class Pickers(mainActivityIn: MainActivity) {

    internal var mainActivity: MainActivity? = null
    internal var savedState: SharedPreferences? = null
    internal val chords = Chords()

    //Buttons
    internal var bAddButtonChordSelector: TextView? = null

    //Key and Scale Pickers
    internal var spKeyPicker: Spinner? = null
    internal var spScalePicker: Spinner? = null
    internal var keyPickerIndexSelected = 3 //C Major
    internal var scalePickerIndexSelected = 0


    //ChordPicker
    internal var spChordPicker: Spinner? = null
    internal var chordAdapt: ArrayAdapter<String>? = null
    internal var chordListForDropDown: List<String>? = null //Needed for dynamic changing
    internal var chordsInCurrentlyPickedKey: Array<String>? = null
    internal var chordPickerIndexSelected = 0



    init {
        mainActivity = mainActivityIn
        savedState = mainActivityIn.savedState
    }


    /*Saves the state of the Pickers, for use on OnPause()*/
    fun savePickerIndices() {
        val editor = savedState!!.edit()
        editor.putInt("keyPickerIndex", keyPickerIndexSelected)
        editor.putInt("scalePickerIndex", scalePickerIndexSelected)
        editor.putInt("chordPickerIndex", chordPickerIndexSelected)
    }

    /*Reinstates indices of our Pickers, for use on OnResume()*/
    fun reinstateSavedPickerIndices () {
        keyPickerIndexSelected = savedState!!.getInt("keyPickerIndex", keyPickerIndexSelected)
        scalePickerIndexSelected = savedState!!.getInt("scalePickerIndex", scalePickerIndexSelected)
        //chordPickerIndexSelected = savedState.getInt("chordPickerIndex", chordPickerIndexSelected)

        spKeyPicker?.setSelection(keyPickerIndexSelected)
        spScalePicker?.setSelection(scalePickerIndexSelected)
        spChordPicker?.setSelection(chordPickerIndexSelected)
    }


    /*Constructs Picker Views and sets to initial state*/
    fun initPickerListViews() {
        bAddButtonChordSelector = mainActivity?.findViewById(R.id.AddButtonChordSelection) as TextView

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
        bAddButtonChordSelector!!.setOnClickListener {

            if (mainActivity?.chordsInProgression!!.size < 8) {
                //Out of bounds check
                if (mainActivity?.nextInputIndex!! > mainActivity?.chordsInProgression!!.size) //TODO:We need this check on the progressionbar onclicklistener
                    mainActivity?.nextInputIndex = mainActivity?.chordsInProgression!!.size //TODO: We also need a delete function where the user can slide the progression bar to remove the view

                var chordIndex = chordPickerIndexSelected
                mainActivity?.chordsInProgression!!.add(mainActivity?.nextInputIndex!!, ChordData(chordsInCurrentlyPickedKey!![chordIndex!!], mainActivity?.numOfBeatsSelected!!, chordIndex + 1))
                //Log.v("ChordsIn/Key: ", printChordProgression())

                mainActivity!!.nextInputIndex++ //Sets next input to end of bar just created

                mainActivity?.sendChordsToProgressionBar()
            }
        }

        spKeyPicker?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                keyPickerIndexSelected = pos
                mainActivity?.updateOnKeyOrScaleChange()
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
        }

        spScalePicker?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                scalePickerIndexSelected = pos
                mainActivity?.updateOnKeyOrScaleChange()
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
        }

        spChordPicker?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                chordPickerIndexSelected = pos
                mainActivity?.playChord(ChordData(chordsInCurrentlyPickedKey!![chordPickerIndexSelected], mainActivity!!.numOfBeatsSelected, chordPickerIndexSelected + 1), 500)
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
        }
    }

    /*Creates a dynamic spinner, call in onCreate and onResume*/
    fun initializeChordSpinner() {
        spChordPicker = mainActivity?.findViewById(R.id.chordPicker) as Spinner
        chordListForDropDown = chordsInCurrentlyPickedKey!!.toCollection(ArrayList<String>())
        chordAdapt = ArrayAdapter(mainActivity, R.layout.support_simple_spinner_dropdown_item, chordListForDropDown)
        spChordPicker?.adapter = chordAdapt
        spChordPicker?.setSelection(chordPickerIndexSelected)
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
        chordsInCurrentlyPickedKey = chords.findChordsInKey(chords, key, scale)
    }

    /*Updates the ChordSpinner after the scale has been changed, must be called after updateChordsInCurrentlyPickedKey()*/
    fun updateChordSpinner() {
        chordAdapt!!.clear()
        for(str in chordsInCurrentlyPickedKey!!) {
            chordAdapt!!.add(str)
        }
        chordAdapt!!.notifyDataSetChanged()
        //Log.v("updateChordSpinner", "HERE")

        chordPickerIndexSelected = 0
        spChordPicker!!.setSelection(chordPickerIndexSelected)
    }


}