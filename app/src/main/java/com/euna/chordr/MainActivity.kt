package com.euna.chordr

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import java.util.*


/**
 * This Class Handles Key and Scale Selection
 */
class MainActivity : AppCompatActivity() {

    internal var savedState: SharedPreferences? = null
    internal val chords = Chords()
    internal val soundManager = SoundManager()

    internal var tvNotesDisplay: TextView? = null
    internal var bPlayButtonChordSelector: ImageView? = null
    internal var bSelectChord: Button? = null

    internal var spKeyPicker: Spinner? = null
    internal var spScalePicker: Spinner? = null
    internal var keyPickerIndexSelected = 3 //C Major
    internal var scalePickerIndexSelected = 0

    internal var spChordPicker: Spinner? = null
    internal var chordsInCurrentlyPickedKey: Array<String>? = null
    internal var chordPickerIndexSelected = 0
    
    internal var chordsInProgression = LinkedList<ChordData>()
    internal var barSelected = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedState = getPreferences(Activity.MODE_PRIVATE);
        setContentView(R.layout.activity_main)
        initializeFindByViews()
        initializeOnClickListeners()
    }

    override fun onStart() {
        super.onStart()
        updateNotesInKey()
    }

    override fun onPause() {
        super.onPause()
        val editor = savedState!!.edit()
        editor.putInt("keyPickerIndex", keyPickerIndexSelected)
        editor.putInt("scalePickerIndex", scalePickerIndexSelected)
        editor.putInt("chordPickerIndex", chordPickerIndexSelected)
        editor.putInt("barSelectedIndex", barSelected)
        editor.commit()
    }

    override fun onResume() {
        super.onResume()
        spKeyPicker!!.setSelection(savedState!!.getInt("keyPickerIndex", keyPickerIndexSelected))
        spScalePicker!!.setSelection(savedState!!.getInt("scalePickerIndex", scalePickerIndexSelected))
        spChordPicker!!.setSelection(savedState!!.getInt("chordPickerIndex", chordPickerIndexSelected))
        barSelected = savedState!!.getInt("chordPickerIndex", barSelected)
        updateNotesInKey()
    }


    private fun initializeFindByViews() {
        tvNotesDisplay = findViewById(R.id.chordsDisplay) as TextView
        bPlayButtonChordSelector = findViewById(R.id.playButtonChordSelection) as ImageView
        bSelectChord = findViewById(R.id.bSelectChord) as Button

        spKeyPicker = findViewById(R.id.keyPicker) as Spinner
        val keyAdapt = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, chords.notes)
        spKeyPicker!!.adapter = keyAdapt
        spKeyPicker!!.setSelection(keyPickerIndexSelected)

        spScalePicker = findViewById(R.id.scalePicker) as Spinner
        val scaleAdapt = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, chords.scale)
        spScalePicker!!.adapter = scaleAdapt
        spScalePicker!!.setSelection(scalePickerIndexSelected)
        updateChordsInChordSelector()

        spChordPicker = findViewById(R.id.chordPicker) as Spinner
        val chordAdapt = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, chordsInCurrentlyPickedKey)
        spChordPicker!!.adapter = chordAdapt
        spChordPicker!!.setSelection(chordPickerIndexSelected)
    }

    private fun initializeOnClickListeners() {

        bPlayButtonChordSelector!!.setOnClickListener {soundManager.playCSharpMajor(this)}

        bSelectChord!!.setOnClickListener {
            //TODO: Add Selected Chord from spinner to ArrayList
            barSelected++ //TODO: Comprehensive bar selection program
        }

        spKeyPicker!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                keyPickerIndexSelected = pos
                updateChordsInChordSelector()
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
        }

        spScalePicker!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                scalePickerIndexSelected = pos
                updateChordsInChordSelector()
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
        }

        spChordPicker!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                chordPickerIndexSelected = pos
                updateNotesInKey()
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
        }

    }

    private fun updateChordsInChordSelector() {
        val key = spKeyPicker!!.selectedItem.toString()
        chordsInCurrentlyPickedKey = findChordsInKey(key)
    }

    private fun updateNotesInKey() {
        val key = spKeyPicker!!.selectedItem.toString()
        val chordsInKey = findNotesInKey(key)
        printNotesInKey(chordsInKey)
    }

    private fun findChordsInKey(key: String) : Array<String> {
        val chordsInKey: Array<String>
        when (spScalePicker!!.selectedItem.toString()) {
            "Major" -> chordsInKey = chords.getMajChords(key)

            "Minor" -> chordsInKey = chords.getMinChords(key)

            else -> chordsInKey = chords.getMajChords(key)
        }
        return chordsInKey
    }

    private fun findNotesInKey(key: String) : Array<String> {
        val notesInKey: Array<String>
        when (spScalePicker!!.selectedItem.toString()) {
            "Major" -> notesInKey = chords.getMajorScale(key)

            "Minor" -> notesInKey = chords.getMinorScale(key)

            else -> notesInKey = chords.getMajChords(key)
        }
        return notesInKey
    }


    private fun printNotesInKey(arrayOfNotesInKey: Array<String>) {
        var chordString = ""
        for (i in arrayOfNotesInKey.indices) {
            chordString += arrayOfNotesInKey[i] + " "
        }

        tvNotesDisplay!!.text = chordString
    }

}