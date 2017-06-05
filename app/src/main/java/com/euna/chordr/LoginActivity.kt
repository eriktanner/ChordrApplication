package com.euna.chordr

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*


/**
 * This Class Handles Key and Scale Selection
 */
class LoginActivity : AppCompatActivity() {

    internal var stateState: SharedPreferences? = null
    internal val chords = Chords()
    internal val soundManager = SoundManager()

    internal var tvChordsDisplay: TextView? = null
    internal var bGoToMainButton: Button? = null
    internal var bPlayButton: Button? = null

    internal var spKeyPicker: Spinner? = null
    internal var spScalePicker: Spinner? = null
    internal var keyPickerIndexSelected = 3
    internal var scalePickerIndexSelected = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stateState = getPreferences(Activity.MODE_PRIVATE);
        setContentView(R.layout.activity_login)
        initializeFindByViews()
        initializeOnClickListeners()
    }

    override fun onStart() {
        super.onStart()
        updateChordsInKey()
    }

    override fun onPause() {
        super.onPause()
        val editor = stateState!!.edit()
        editor.putInt("keyPickerIndex", keyPickerIndexSelected)
        editor.putInt("scalePickerIndex", scalePickerIndexSelected)
        editor.commit()
    }

    override fun onResume() {
        super.onResume()
        spKeyPicker!!.setSelection(stateState!!.getInt("keyPickerIndex", keyPickerIndexSelected));
        spScalePicker!!.setSelection(stateState!!.getInt("scalePickerIndex", scalePickerIndexSelected));
        updateChordsInKey()
    }

    private fun initializeFindByViews() {
        tvChordsDisplay = findViewById(R.id.chordsDisplay) as TextView
        bGoToMainButton = findViewById(R.id.bGoToMainActivity) as Button
        bPlayButton = findViewById(R.id.playButton) as Button

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
        bGoToMainButton!!.setOnClickListener {
            val PageTransition = Intent(this, MainActivity::class.java)
            startActivity(PageTransition)
        }

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
    }

    private fun findChordsInKey(scale: String) : Array<String> {
        val chordsInKey: Array<String>
        when (spScalePicker!!.selectedItem.toString()) {
            "Major" -> chordsInKey = chords.getMajorScale(scale)

            "Minor" -> chordsInKey = chords.getMinorScale(scale)

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