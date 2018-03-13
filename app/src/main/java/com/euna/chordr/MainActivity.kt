package com.euna.chordr

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.*
import java.util.*

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

        editor.putInt("numberOfBeatsSelected", numOfBeatsSelected)
        editor.putInt("barSelectedIndex", nextInputIndex)
        editor.commit()
    }

    override fun onResume() {
        super.onResume()

        numOfBeatsSelected = savedState!!.getInt("numberOfBeatsSelected", numOfBeatsSelected)
        nextInputIndex = savedState!!.getInt("chordPickerIndex", nextInputIndex)



        selectNote(numOfBeatsSelected)


        pickerManager?.updateChordsInCurrentlyPickedKey() //These actions are same as updateOnKeyOrScale change, but instead we must init the chordSpinner
        pickerManager?.initializeChordSpinner()
    }




    /********************************* Initialization Specific ************************************/

    private fun initializeFindByViews() {
        initializeButtonViews()
        initializeListViews()
    }

    private fun initializeOnClickListeners() {
        initializeButtonListeners()
        pickerManager?.initPickerOnClickListeners()
    }


                            /************* Views ***************/

    private fun initializeButtonViews() {
        //tvNotesDisplay = findViewById(R.id.chordsDisplay) as TextView
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
        pickerManager?.updateChordsInCurrentlyPickedKey()
        pickerManager?.initializeChordSpinner()
    }



                            /************* Listeners *************/

    private fun initializeButtonListeners() {

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






    /***************************************** Updates ********************************************/

    /*Call whenever key or scale is changed*/
    fun updateOnKeyOrScaleChange() {
        pickerManager?.updatePickersOnKeyOrScaleChange()
        updateIntervals();
        //updateNotesInKey()
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