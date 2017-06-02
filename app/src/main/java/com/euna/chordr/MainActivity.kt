package com.euna.chordr

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    private var keyPicker: Spinner? = null
    private var modePicker: Spinner? = null
    private var updateButton: Button? = null
    private var chordsDisplay: TextView? = null

    internal var chords = Chords()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeFindByViews()
        initializeOnClickListeners()
    }


    private fun initializeFindByViews() {
        updateButton = findViewById(R.id.updateButton) as Button
        chordsDisplay = findViewById(R.id.chordsDisplay) as TextView

        keyPicker = findViewById(R.id.keyPicker) as Spinner
        val keyAdapt = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, chords.notes)
        keyPicker!!.adapter = keyAdapt

        modePicker = findViewById(R.id.modePicker) as Spinner
        val modeAdapt = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, chords.modes)
        modePicker!!.adapter = modeAdapt
    }


    private fun initializeOnClickListeners() {
        updateButton!!.setOnClickListener {
            var total = ""
            val key = keyPicker!!.selectedItem.toString()
            val chordsInKey: Array<String>

            when (modePicker!!.selectedItem.toString()) {
                "Major" -> chordsInKey = chords.getMajChords(key)

                "Minor" -> chordsInKey = chords.getMinChords(key)

                else -> chordsInKey = chords.getMajChords(key)
            }

            for (i in chordsInKey.indices) {
                total += chordsInKey[i] + " "
            }

            chordsDisplay!!.text = total
        }
    }
}
