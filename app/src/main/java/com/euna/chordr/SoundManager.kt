package com.euna.chordr

import android.content.Context
import android.media.MediaPlayer

/**
 * This Class handles everything pertaining to playing
 * sound files
 */
class SoundManager {

    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private var instrumentSelected = Instrument.SawWave


    /******************************* Base Functions **********************************/

    /* We need to release the media player every time we hit a chord or else we'll run
   * out of memory because they don't ever release themselves */
    fun releaseMediaPlayer() {
        if (isPlaying)
            mediaPlayer!!.release()
        isPlaying = false
    }

    /* Starts playing the media player */
    protected fun playMediaPlayer() {
        mediaPlayer!!.start()
        isPlaying = true
    }

    /* Plays the sound file corresponding to the resource id */
    fun playInstrument(context: Context, resID: Int) {
        mediaPlayer = MediaPlayer.create(context, resID)
        playMediaPlayer()
    }


    /*********************************** Chords **************************************/

    fun playCMajor(context: Context) {
        when (instrumentSelected) {
            Instrument.SawWave -> playInstrument(context, R.raw.catsound)
            Instrument.Guitar -> playInstrument(context, R.raw.catsound)
            Instrument.Piano -> playInstrument(context, R.raw.catsound)
            else -> {}
        }
    }

    fun playCSharpMajor(context: Context) {
        when (instrumentSelected) {
            Instrument.SawWave -> playInstrument(context, R.raw.dogsound)
            Instrument.Guitar -> playInstrument(context, R.raw.dogsound)
            Instrument.Piano -> playInstrument(context, R.raw.dogsound)
            else -> {}
        }
    }
}