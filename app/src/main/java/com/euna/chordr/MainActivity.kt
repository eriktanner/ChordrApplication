package com.euna.chordr

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*


/**
 * This Class Handles Key and Scale Selection
 */
class MainActivity : AppCompatActivity() {

    internal var savedState: SharedPreferences? = null
    internal val chords = Chords()
    internal val soundManager = SoundManager()

    internal var tvChordsDisplay: TextView? = null
    internal var bGoToMainButton: Button? = null
    internal var bPlayButton: Button? = null

    internal var spKeyPicker: Spinner? = null
    internal var spScalePicker: Spinner? = null
    internal var firstChord: Spinner? = null
    internal var secondChord: Spinner? = null
    internal var thirdChord: Spinner? = null
    internal var fourthChord: Spinner? = null
    internal var keyPickerIndexSelected = 3
    internal var scalePickerIndexSelected = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedState = getPreferences(Activity.MODE_PRIVATE);
        setContentView(R.layout.activity_main)
        initializeFindByViews()
        initializeOnClickListeners()
    }

    override fun onStart() {
        super.onStart()
        updateChordsInKey()
    }

    override fun onPause() {
        super.onPause()
        val editor = savedState!!.edit()
        editor.putInt("keyPickerIndex", keyPickerIndexSelected)
        editor.putInt("scalePickerIndex", scalePickerIndexSelected)
        editor.commit()
    }

    override fun onResume() {
        super.onResume()
        spKeyPicker!!.setSelection(savedState!!.getInt("keyPickerIndex", keyPickerIndexSelected));
        spScalePicker!!.setSelection(savedState!!.getInt("scalePickerIndex", scalePickerIndexSelected));
        updateChordsInKey()
    }

    private fun initializeFindByViews() {
        tvChordsDisplay = findViewById(R.id.chordsDisplay) as TextView
        bPlayButton = findViewById(R.id.playButtonKeySelection) as Button

        spKeyPicker = findViewById(R.id.keyPicker) as Spinner
        val keyAdapt = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, chords.notes)
        spKeyPicker!!.adapter = keyAdapt
        spKeyPicker!!.setSelection(keyPickerIndexSelected)

        spScalePicker = findViewById(R.id.scalePicker) as Spinner
        val scaleAdapt = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, chords.scale)
        spScalePicker!!.adapter = scaleAdapt
        spScalePicker!!.setSelection(scalePickerIndexSelected)
    }

    private fun initializeOnClickListeners() {

        bPlayButton!!.setOnClickListener {soundManager.playCMajor(this)}

        spKeyPicker!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                keyPickerIndexSelected = pos
                updateChordsInKey()
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
        }

        spScalePicker!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                scalePickerIndexSelected = pos
                updateChordsInKey()
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
        }

    }

    private fun updateChordsInKey() {
        val key = spKeyPicker!!.selectedItem.toString()
        val chordsInKey = findChordsInKey(key)
        writeChordsInKey(chordsInKey)

        val scaleAdapt = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, chordsInKey)


        firstChord?.adapter = scaleAdapt
        secondChord?.adapter = scaleAdapt
        thirdChord?.adapter = scaleAdapt
        fourthChord?.adapter = scaleAdapt
    }

    private fun findChordsInKey(scale: String) : Array<String> {
        val chordsInKey: Array<String>
        when (spScalePicker!!.selectedItem.toString()) {
            "Major" -> chordsInKey = chords.getMajChords(scale)

            "Minor" -> chordsInKey = chords.getMinChords(scale)

            else -> chordsInKey = chords.getMajChords(scale)
        }
        return chordsInKey
    }

    private fun writeChordsInKey(arrayOfChordsInKey: Array<String>) {
        var chordString = ""
        for (i in arrayOfChordsInKey.indices) {
            chordString += arrayOfChordsInKey[i] + " "
        }

        tvChordsDisplay!!.text = chordString
    }
}