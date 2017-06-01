package com.euna.chordr;
import android.content.Context;
import android.media.MediaPlayer;


/**
 * This Class handles everything pertaining to playing sound files
 */
public class SoundManager {

   private MediaPlayer mediaPlayer;
   private boolean isPlaying = false;
   private Instrument instrumentSelected = Instrument.SawWave;


   /****************************** Base Functions ********************************/

   /* We need to release the media player every time we hit a chord or else we'll run
   * out of memory because they don't ever release themselves */
   public void releaseMediaPlayer() {
      if (isPlaying)
         mediaPlayer.release();
      isPlaying = false;
   }

   /* Starts playing the media player */
   protected void playMediaPlayer() {
      mediaPlayer.start();
      isPlaying = true;
   }

   /* Plays the sound file corresponding to the resource id */
   public void playInstrument(Context context, int resID) {
      mediaPlayer = MediaPlayer.create(context, resID);
      playMediaPlayer();
   }



   /********************************* Chords ************************************/

   public void playCMajor(Context context) {
      switch (instrumentSelected) {
         case SawWave: playInstrument(context, R.raw.catsound); break;
         case Guitar: playInstrument(context, R.raw.catsound); break;
         case Piano: playInstrument(context, R.raw.catsound); break;
         default: break;
      }
   }

   public void playCSharpMajor(Context context) {
      switch (instrumentSelected) {
         case SawWave: playInstrument(context, R.raw.dogsound); break;
         case Guitar: playInstrument(context, R.raw.dogsound); break;
         case Piano: playInstrument(context, R.raw.dogsound); break;
         default: break;
      }
   }






}
