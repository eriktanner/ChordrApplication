package com.euna.chordr

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import java.util.*
import kotlin.collections.ArrayList

//Log.v("ChordsIn/Key: ", chordsInCurrentlyPickedKey!!.get(0) )

/**
 * This Class Handles Key and Scale Selection
 */
class MainActivity : AppCompatActivity(), ProgressionManager.ProgressionFragmentInteractionListener {

    internal var savedState: SharedPreferences? = null
    internal val chords = Chords()
    val soundManager = SoundManager()

    internal var bGoToLogin: Button? = null
    internal var tvNotesDisplay: TextView? = null
    internal var bAddButtonChordSelector: TextView? = null
    internal var bPlayChordProgression: ImageView? = null

    internal var tvTitle: TextView? = null

    //Notes
    /*internal var cbNoteQuarter: CheckBox? = null
    internal var cbNoteHalf: CheckBox? = null
    internal var cbNoteDottedHalf: CheckBox? = null
    internal var cbNoteWhole: CheckBox? = null */
    internal var numOfBeatsSelected = 4

    //BPM
    internal var tvBPM: TextView? = null
    internal var tvBPMText: TextView? = null
    internal var bBPMSpace: Button? = null


    //Key & Scale Picker
    internal var pickerManager: Pickers? = null;





    //ChordPicker
    internal var spChordPicker: Spinner? = null
    internal var chordAdapt: ArrayAdapter<String>? = null
    internal var chordListForDropDown: List<String>? = null //Needed for dynamic changing
    internal var chordsInCurrentlyPickedKey: Array<String>? = null
    internal var chordPickerIndexSelected = 0

    internal var chordsInProgression = LinkedList<ChordData>()
    internal var nextInputIndex = 0

    //Progression
    internal var progressionManager: ProgressionManager? = null
    internal var startingPlaybackIndex: Int = 0

    var threadProgressionBarPlayer: Thread? = null

    //Extras
    internal var bTranspose: Button? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedState = getPreferences(Activity.MODE_PRIVATE);
        pickerManager = Pickers(this)
        setContentView(R.layout.activity_main)
        initializeFindByViews()
        initializeOnClickListeners()
        progressionManager = ProgressionManager()
        getSupportFragmentManager().beginTransaction().replace(R.id.container_progressionbar, progressionManager).commit();
    }

    override fun onStart() {
        super.onStart()
        selectNote(numOfBeatsSelected)
    }


    override fun onPause() {
        super.onPause()
        val editor = savedState!!.edit()
        pickerManager!!.savePickerIndices();

        editor.putInt("chordPickerIndex", chordPickerIndexSelected)
        editor.putInt("numberOfBeatsSelected", numOfBeatsSelected)
        editor.putInt("barSelectedIndex", nextInputIndex)
        editor.commit()
    }

    override fun onResume() {
        super.onResume()
        chordPickerIndexSelected = savedState!!.getInt("chordPickerIndex", chordPickerIndexSelected)

        numOfBeatsSelected = savedState!!.getInt("numberOfBeatsSelected", numOfBeatsSelected)
        nextInputIndex = savedState!!.getInt("chordPickerIndex", nextInputIndex)



        spChordPicker!!.setSelection(chordPickerIndexSelected)
        selectNote(numOfBeatsSelected)


        updateChordsInCurrentlyPickedKey() //These actions are same as updateOnKeyOrScale change, but instead we must init the chordSpinner
        initializeChordSpinner()
    }




    /********************************* Initialization Specific ************************************/

    private fun initializeFindByViews() {
        initializeButtonViews()
        initializeListViews()
    }

    private fun initializeOnClickListeners() {
        initializeButtonListeners()
        initializeListListeners()
    }


                            /************* Views ***************/

    private fun initializeButtonViews() {
        //tvNotesDisplay = findViewById(R.id.chordsDisplay) as TextView
        bAddButtonChordSelector = findViewById(R.id.AddButtonChordSelection) as TextView
        bPlayChordProgression = findViewById(R.id.bPlayChordProgression) as ImageView
        bGoToLogin = findViewById(R.id.bGoToLogin) as Button

        /* cbNoteQuarter = findViewById(R.id.ivNoteQuarter) as CheckBox
        cbNoteHalf = findViewById(R.id.ivNoteHalf) as CheckBox
        cbNoteDottedHalf = findViewById(R.id.ivNoteDottedHalf) as CheckBox
        cbNoteWhole = findViewById(R.id.ivNoteWhole) as CheckBox */

        pickerManager?.initPickerListViews()

        tvBPM = findViewById(R.id.tvBPMnum) as TextView
        tvBPMText = findViewById(R.id.tvBPMtext) as TextView
        bBPMSpace = findViewById(R.id.bBPMSpace) as Button

        tvTitle = findViewById(R.id.tvTitle) as TextView

        bTranspose = findViewById(R.id.bTranspose) as Button

    }

    private fun initializeListViews() {
        updateChordsInCurrentlyPickedKey()
        initializeChordSpinner()
    }

    /*Creates a dynamic spinner, call in onCreate and onResume*/
    private fun initializeChordSpinner() {
        spChordPicker = findViewById(R.id.chordPicker) as Spinner
        chordListForDropDown = chordsInCurrentlyPickedKey!!.toCollection(ArrayList<String>())
        chordAdapt = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, chordListForDropDown)
        spChordPicker!!.adapter = chordAdapt
        spChordPicker!!.setSelection(chordPickerIndexSelected)
    }


                            /************* Listeners *************/

    private fun initializeButtonListeners() {

        bAddButtonChordSelector!!.setOnClickListener {

            if (chordsInProgression!!.size < 8) {
                //Out of bounds check
                if (nextInputIndex > chordsInProgression.size) //TODO:We need this check on the progressionbar onclicklistener
                    nextInputIndex = chordsInProgression.size //TODO: We also need a delete function where the user can slide the progression bar to remove the view

                chordsInProgression.add(nextInputIndex, ChordData(chordsInCurrentlyPickedKey!![chordPickerIndexSelected], numOfBeatsSelected, chordPickerIndexSelected + 1))
                Log.v("ChordsIn/Key: ", printChordProgression())

                nextInputIndex++ //Sets next input to end of bar just created

                sendChordsToProgressionBar()
            }
        }

        bPlayChordProgression!!.setOnClickListener {
            if (threadProgressionBarPlayer != null && threadProgressionBarPlayer!!.isAlive)
                stopChordProgressionPlayback()
            else playEntireChordProgression()
        }

        bGoToLogin!!.setOnClickListener {
            val PageTransition = Intent(this, LoginActivity::class.java)
            startActivity(PageTransition)
        }



        /* cbNoteQuarter!!.setOnClickListener {deselectNote(numOfBeatsSelected); numOfBeatsSelected = 1; selectNote(numOfBeatsSelected)}
        cbNoteHalf!!.setOnClickListener {deselectNote(numOfBeatsSelected); numOfBeatsSelected = 2; selectNote(numOfBeatsSelected)}
        cbNoteDottedHalf!!.setOnClickListener {deselectNote(numOfBeatsSelected); numOfBeatsSelected = 3; selectNote(numOfBeatsSelected)}
        cbNoteWhole!!.setOnClickListener {deselectNote(numOfBeatsSelected); numOfBeatsSelected = 4; selectNote(numOfBeatsSelected)} */

        tvBPM!!.setOnClickListener {promptBPMInput()}
        tvBPMText!!.setOnClickListener {tvBPM!!.performClick()}
        bBPMSpace!!.setOnClickListener {tvBPM!!.performClick()}

        tvTitle!!.setOnClickListener {promptTitleInput()}

        /*bTranspose!!.setOnClickListener {
            var halfSteps = 1
            transpose(halfSteps)

            var nextKeyPickerVal = spKeyPicker!!.selectedItemPosition + halfSteps
            if (nextKeyPickerVal > 11) nextKeyPickerVal = 0
            spKeyPicker!!.setSelection(nextKeyPickerVal)
            updateOnKeyOrScaleChange()
        }*/


    }


    private fun initializeListListeners() {
        pickerManager?.initPickerOnClickListeners()

        spChordPicker!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                chordPickerIndexSelected = pos
                playChord(ChordData(chordsInCurrentlyPickedKey!![chordPickerIndexSelected], numOfBeatsSelected, chordPickerIndexSelected + 1), 500)
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
        }
    }





    /***************************************** Updates ********************************************/

    /*Call whenever key or scale is changed*/
    fun updateOnKeyOrScaleChange() {
        updateChordsInCurrentlyPickedKey()
        updateChordSpinner()
        updateIntervals();
        //updateNotesInKey()
    }

    /*Updates the data array of the chords in the currently selected key/scale of the scale selection spinners*/
    private fun updateChordsInCurrentlyPickedKey() {
        val key = pickerManager?.spKeyPicker?.selectedItem.toString()
        val scale = pickerManager?.spScalePicker?.selectedItem.toString()
        chordsInCurrentlyPickedKey = chords.findChordsInKey(chords, key, scale)
    }

    /*Updates the chord spinner after the scale has been changed, must be called after updateChordsInCurrentlyPickedKey()*/
    private fun updateChordSpinner() {
        chordAdapt!!.clear()
        for(str in chordsInCurrentlyPickedKey!!) {
           chordAdapt!!.add(str)
        }
        chordAdapt!!.notifyDataSetChanged()

        chordPickerIndexSelected = 0
        spChordPicker!!.setSelection(chordPickerIndexSelected)
    }

    private fun updateIntervals() {
        var newIntervalStrings: Array<String>? = null
        newIntervalStrings = chords.findIntervalsOfChords(pickerManager?.spKeyPicker?.selectedItem.toString(), pickerManager?.spScalePicker?.selectedItem.toString(), chordsInProgression)
        progressionManager!!.updateIntervals(newIntervalStrings)
    }

    /*Prints Notes in Key*/
    private fun updateNotesInKey() {
        val key = pickerManager?.spKeyPicker?.selectedItem.toString()
        val scale = pickerManager?.spScalePicker?.selectedItem.toString()
        val chordsInKey = chords.findChordsInKey(chords, key, scale)
        printNotesInKey(chordsInKey)
    }

    /* Selection of note - quarter, half, dotted half, whole */
    private fun selectNote(pos: Int) {
        /* when (pos) {
            1 -> cbNoteQuarter!!.setChecked(true)
            2 -> cbNoteHalf!!.setChecked(true)
            3 -> cbNoteDottedHalf!!.setChecked(true)
            4 -> cbNoteWhole!!.setChecked(true)
            else -> {}
        } */
    }

    /* Deselection of note - quarter, half, dotted half, whole */
    private fun deselectNote(pos: Int) {
        /* when (pos) {
            1 -> cbNoteQuarter!!.setChecked(false)
            2 -> cbNoteHalf!!.setChecked(false)
            3 -> cbNoteDottedHalf!!.setChecked(false)
            4 -> cbNoteWhole!!.setChecked(false)
            else -> {}
        } */
    }


    /********************************** Progression Functions ************************************/

    var ProgressionIsPlaying = false

    /*Updates every chord's BPM in the progression*/
    private fun setBPM(newBPM: Int) {
        soundManager.setBPM(newBPM)
    }

    /*Gets and uses ChordData to be used with the SoundManager to play the entire progression*/
    private fun playEntireChordProgression() {

        stopChordProgressionPlayback()
        bPlayChordProgression!!.setImageResource(R.drawable.selectbutton)


        threadProgressionBarPlayer = Thread() {
                                                                /*Steps: 1. Calculate Time of Lock      */
                                                                /*     2. Play Chord                    */
                                                                /*     3. Wait real time of chord in (2)*/
            try {
                var counter = 0

                for (chord in chordsInProgression) {

                    if (counter >= startingPlaybackIndex) {
                        var timeToHoldNote: Long = (60000 / soundManager.getBPM() * chord.numberOfBeats).toLong()
                        soundManager.playChordByStringName(this, chord.toString())
                        Thread.sleep(timeToHoldNote) //in milliseconds
                        soundManager.releaseMediaPlayer()
                    }
                    counter++
                }

                runOnUiThread(Runnable { //Switches Stop Button for Play Button
                    bPlayChordProgression!!.setImageResource(R.drawable.playbutton)
                })

            } catch(e: Exception) {
                e.printStackTrace()
            }



        }
        threadProgressionBarPlayer!!.start()

    }





    /* Plays chord */
    fun playChord(chord: ChordData, timeToHoldNote: Long) {

        val thread = Thread() {

            try {
                if (threadProgressionBarPlayer != null && !threadProgressionBarPlayer!!.isAlive) {
                    soundManager.playChordByStringName(this, chord.toString())
                    Thread.sleep(timeToHoldNote) //in milliseconds
                    soundManager.releaseMediaPlayer()
                }
            } catch(e: Exception) {
                e.printStackTrace()
            }

        }
        thread!!.start()
    }

    /* Stops the chord progression from playing */
    fun stopChordProgressionPlayback() {

        if (threadProgressionBarPlayer != null) {
            soundManager!!.immediateReleaseAllMediaPlayers()
            threadProgressionBarPlayer!!.interrupt()
        }
        bPlayChordProgression!!.setImageResource(R.drawable.playbutton) //Switches Play Button for Stop Button
    }

    /* Sends the needed data to Progression Fragment, call whenever an update is needed, ALSO HANDLES INTERVAL UPDATES*/
    fun sendChordsToProgressionBar() {
        progressionManager!!.setChordsToBeConvertedToBars(chordsInProgression)
        updateIntervals()
    }

    /* Transpose the progression bar by number of half steps*/
    fun transpose(numOfHalfSteps: Int) {
        chordsInProgression = chords.transpose(numOfHalfSteps, chordsInProgression)
        sendChordsToProgressionBar()
    }





    //Callbacks
    var lastClickedBar: Int = 0

    /* (Callback) Called whenever the user deletes a bar*/
    override fun onBarRemoved(pos: Int) {
        chordsInProgression.removeAt(pos)
    }

    /* (Callback) Called whenever the user selects a bar*/
    override fun onBarClicked(pos: Int) {
        //lastClickedBar = pos
        playChord(chordsInProgression!![pos], 500)
    }

    /* (Callback) Called whenever the user long clicks a bar*/
    override fun onBarLongClicked(pos: Int) {
        ///nextInputIndex = lastClickedBar + 1//Resets input index upon long click
        //startingPlaybackIndex = pos
        nextInputIndex = pos + 1
    }

    /************************************ General Functions **************************************/


    private fun promptBPMInput() {

        var builder: AlertDialog.Builder? = AlertDialog.Builder(this)
        builder!!.setTitle("Beats Per Minute")

        var bpmInput: EditText? = EditText(this);

        bpmInput!!.setInputType(InputType.TYPE_CLASS_NUMBER)
        builder!!.setView(bpmInput!!);


        builder!!.setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
            if (bpmInput!!.text.toString().trim().length == 0)
                dialogInterface.cancel()
            else {
                var rangeCheckedInput = bpmInput.text.toString().toInt()
                if (rangeCheckedInput > 200)
                    rangeCheckedInput = 200
                else if (rangeCheckedInput < 60)
                    rangeCheckedInput = 60
                setBPM(rangeCheckedInput)
                tvBPM!!.text = rangeCheckedInput.toString()
            }
        });

        builder!!.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.cancel();
        });

        builder.show();
    }


    private fun promptTitleInput() {

        var builder: AlertDialog.Builder? = AlertDialog.Builder(this)
        builder!!.setTitle("Song Title")

        var Input: EditText? = EditText(this)
        Input!!.setText(tvTitle!!.text)
        Input!!.selectAll()

        builder!!.setView(Input!!);


        builder!!.setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
            var inputText = Input!!.text.toString().trim()
            if (inputText!!.length > 25) {
                dialogInterface.cancel()
                Toast.makeText(this, "Song Title Too Long!", Toast.LENGTH_LONG).show()
            }
            else {
                tvTitle!!.text = inputText
            }
        });

        builder!!.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.cancel();
        });

        builder.show();
    }





    /*Prints the notes in the current key to the TextView tvNotesDisplay*/
    private fun printNotesInKey(arrayOfNotesInKey: Array<String>) {
        var chordString = ""
        for (i in arrayOfNotesInKey.indices) {
            chordString += arrayOfNotesInKey[i] + " "
        }

        tvNotesDisplay!!.text = chordString
    }

    private fun printChordProgression() : String {
        var chordProgressionString = ""
        for (chord in chordsInProgression){
            chordProgressionString += chord.toString()
        }
        return chordProgressionString
    }
}