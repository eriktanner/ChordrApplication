package com.euna.chordr

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*


/**
 * This Class handles everything pertaining to playing
 * sound files
 */
class LoginActivity : AppCompatActivity() {

    private var spKeyPicker: Spinner? = null
    private var spScalePicker: Spinner? = null

    private var tvChordsDisplay: TextView? = null
    private var GoToMainButton: Button? = null

    internal var chords = Chords()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initializeFindByViews()
        initializeOnClickListeners()
    }

    override fun onStart() {
        super.onStart()
        updateChordsInKey()
    }

    private fun initializeFindByViews() {
        tvChordsDisplay = findViewById(R.id.chordsDisplay) as TextView
        GoToMainButton = findViewById(R.id.bGoToMainActivity) as Button

        spKeyPicker = findViewById(R.id.keyPicker) as Spinner
        val keyAdapt = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, chords.notes)
        spKeyPicker!!.adapter = keyAdapt
        spKeyPicker!!.setSelection(3) //C Major


        spScalePicker = findViewById(R.id.scalePicker) as Spinner
        val scaleAdapt = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, chords.scale)
        spScalePicker!!.adapter = scaleAdapt
    }

    private fun initializeOnClickListeners() {
        GoToMainButton!!.setOnClickListener {
            val PageTransition = Intent(this, MainActivity::class.java)
            startActivity(PageTransition)
        }

        spKeyPicker!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                updateChordsInKey()
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
        }

        spScalePicker!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
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