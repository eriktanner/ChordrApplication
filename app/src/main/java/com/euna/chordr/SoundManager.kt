package com.euna.chordr

import android.content.Context
import android.media.MediaPlayer

/**
 * This Class handles everything pertaining to playing
 * sound files
 */
class SoundManager {

    //We need two media players yo handle the blips between notes
    private var mediaPlayer1: MediaPlayer? = null
    private var mediaPlayer2: MediaPlayer? = null
    private var isPlaying1 = false
    private var isPlaying2 = false
    private var nextMediaPlayerToRelease = 1
    private var instrumentSelected = Instrument.Piano

    private var bpm = 128
    private val timeToReleaseMediaPlayer: Long  = 300


    /******************************* Base Functions **********************************/

    /* Starts playing the media player */
    protected fun playMediaPlayer() {
        if (!isPlaying1) {
            mediaPlayer1!!.start()
            isPlaying1 = true
            nextMediaPlayerToRelease = 1
        } else {
            mediaPlayer2!!.start()
            isPlaying2 = true
            nextMediaPlayerToRelease = 2
        }
    }


    /* We need to release the media player every time we hit a chord or else we'll run
   * out of memory because they don't ever release themselves */
    fun releaseMediaPlayer() {
        if (isPlaying1 || isPlaying2) {

            if(nextMediaPlayerToRelease == 1) {
                timedRelease(mediaPlayer1!!)
                isPlaying1 = false
            } else if (nextMediaPlayerToRelease == 2) {
                timedRelease(mediaPlayer2!!)
                mediaPlayer2!!.release()
                isPlaying2 = false
            }
        }
    }

    /* Releases after a certain amount of time*/
    private fun timedRelease(mp: MediaPlayer) {
        val threadMediaPlayerReleaser = Thread() {

            try {
                Thread.sleep(timeToReleaseMediaPlayer) //in milliseconds
                mp.release()
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }
        threadMediaPlayerReleaser!!.start()
    }

    fun immediateReleaseAllMediaPlayers() {
        if (isPlaying1)
            mediaPlayer1!!.release()
        if (isPlaying2)
            mediaPlayer2!!.release()
    }

    /* Plays the sound file corresponding to the resource id */
    fun playInstrument(context: Context, resID: Int) {
        releaseMediaPlayer()
        mediaPlayer1 = MediaPlayer.create(context, resID)
        playMediaPlayer()
    }

    fun setBPM(newBPM: Int) {
        bpm = newBPM
    }

    fun getBPM() : Int {
        return bpm
    }

    /*********************************** Caller **************************************/

    fun playChordByStringName(context: Context, chordName: String) {
        when (chordName) {
            "A maj" -> playCMajor(context)
            "A# maj" -> playCSharpMajor(context)
            "B maj" -> playCMajor(context)
            "B# maj" -> playCSharpMajor(context)
            "C maj" -> playCMajor(context)
            "C# maj" -> playCSharpMajor(context)
            "D maj" -> playCMajor(context)
            "D# maj" -> playCSharpMajor(context)
            "E maj" -> playCMajor(context)
            "E# maj" -> playCSharpMajor(context)
            "F maj" -> playFMajor(context)
            "F# maj" -> playCSharpMajor(context)
            "G maj" -> playGMajor(context)
            "G# maj" -> playCSharpMajor(context)

            "A min" -> playAMin(context)
            "A# min" -> playCSharpMajor(context)
            "B min" -> playCMajor(context)
            "B# min" -> playCSharpMajor(context)
            "C min" -> playCMajor(context)
            "C# min" -> playCSharpMajor(context)
            "D min" -> playDMin(context)
            "D# min" -> playCSharpMajor(context)
            "E min" -> playEMin(context)
            "E# min" -> playCSharpMajor(context)
            "F min" -> playCMajor(context)
            "F# min" -> playCSharpMajor(context)
            "G min" -> playCMajor(context)
            "G# min" -> playCSharpMajor(context)

            "B dim" -> playBDim(context)
            else -> {}
        }
    }

    /*********************************** Chords **************************************/

    fun playAMin(context: Context) {
        when (instrumentSelected) {
            Instrument.SawWave -> playInstrument(context, R.raw.amin)
            Instrument.Guitar -> playInstrument(context, R.raw.amin)
            Instrument.Piano -> playInstrument(context, R.raw.amin)
            else -> {}
        }
    }

    fun playBDim(context: Context) {
        when (instrumentSelected) {
            Instrument.SawWave -> playInstrument(context, R.raw.bdim)
            Instrument.Guitar -> playInstrument(context, R.raw.bdim)
            Instrument.Piano -> playInstrument(context, R.raw.bdim)
            else -> {}
        }
    }

    fun playCMajor(context: Context) {
        when (instrumentSelected) {
            Instrument.SawWave -> playInstrument(context, R.raw.cmaj)
            Instrument.Guitar -> playInstrument(context, R.raw.cmaj)
            Instrument.Piano -> playInstrument(context, R.raw.cmaj)
            else -> {}
        }
    }

    fun playCSharpMajor(context: Context) {
        when (instrumentSelected) {
            Instrument.SawWave -> playInstrument(context, R.raw.cmaj)
            Instrument.Guitar -> playInstrument(context, R.raw.cmaj)
            Instrument.Piano -> playInstrument(context, R.raw.cmaj)
            else -> {}
        }
    }

    fun playDMin(context: Context) {
        when (instrumentSelected) {
            Instrument.SawWave -> playInstrument(context, R.raw.dmin)
            Instrument.Guitar -> playInstrument(context, R.raw.dmin)
            Instrument.Piano -> playInstrument(context, R.raw.dmin)
            else -> {}
        }
    }

    fun playEMin(context: Context) {
        when (instrumentSelected) {
            Instrument.SawWave -> playInstrument(context, R.raw.emin)
            Instrument.Guitar -> playInstrument(context, R.raw.emin)
            Instrument.Piano -> playInstrument(context, R.raw.emin)
            else -> {}
        }
    }

    fun playFMajor(context: Context) {
        when (instrumentSelected) {
            Instrument.SawWave -> playInstrument(context, R.raw.fmaj)
            Instrument.Guitar -> playInstrument(context, R.raw.fmaj)
            Instrument.Piano -> playInstrument(context, R.raw.fmaj)
            else -> {}
        }
    }

    fun playGMajor(context: Context) {
        when (instrumentSelected) {
            Instrument.SawWave -> playInstrument(context, R.raw.gmaj)
            Instrument.Guitar -> playInstrument(context, R.raw.gmaj)
            Instrument.Piano -> playInstrument(context, R.raw.gmaj)
            else -> {}
        }
    }



}