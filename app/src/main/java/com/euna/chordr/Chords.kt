package com.euna.chordr;

import java.util.*

class Chords {
    val scale = arrayOf("Major", "Minor")
    val notes = arrayOf("A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#")
    val majChords = arrayOf("A maj", "A# maj", "B maj", "C maj", "C# maj", "D maj", "D# maj", "E maj", "F maj", "F# maj", "G maj", "G# maj")
    val minChords = arrayOf("A min", "A# min", "B min", "C min", "C# min", "D min", "D# min", "E min", "F min", "F# min", "G min", "G# min")
    val dimChords = arrayOf("A dim", "A# dim", "B dim", "C dim", "C# dim", "D dim", "D# dim", "E dim", "F dim", "F# dim", "G dim", "G# dim")


    fun getMajorScale(root: String): Array<String> {
        val root = notes.indexOf(root)
        val second = (root + 2) % notes.size
        val third = (root + 4) % notes.size
        val fourth = (root + 5) % notes.size
        val fifth = (root + 7) % notes.size
        val sixth = (root + 9) % notes.size
        val seventh = (root + 11) % notes.size

        val scale = arrayOf(notes[root], notes[second], notes[third], notes[fourth], notes[fifth],
                notes[sixth], notes[seventh])

        return scale
    }

    fun getMinorScale(root: String): Array<String> {
        val root = notes.indexOf(root)
        val second = (root + 2) % notes.size
        val third = (root + 3) % notes.size
        val fourth = (root + 5) % notes.size
        val fifth = (root + 7) % notes.size
        val sixth = (root + 8) % notes.size
        val seventh = (root + 10) % notes.size

        val scale = arrayOf(notes[root], notes[second], notes[third], notes[fourth], notes[fifth],
                notes[sixth], notes[seventh])

        return scale
    }

    fun getMajChords(root: String): Array<String> {
        val root = notes.indexOf(root)
        val second = (root + 2) % notes.size
        val third = (root + 4) % notes.size
        val fourth = (root + 5) % notes.size
        val fifth = (root + 7) % notes.size
        val sixth = (root + 9) % notes.size
        val seventh = (root + 11) % notes.size

        val chords = arrayOf(majChords[root], minChords[second], minChords[third], majChords[fourth], majChords[fifth],
                minChords[sixth], dimChords[seventh])

        return chords
    }

    fun getMinChords(root: String): Array<String> {
        val root = notes.indexOf(root)
        val second = (root + 2) % notes.size
        val third = (root + 4) % notes.size
        val fourth = (root + 5) % notes.size
        val fifth = (root + 7) % notes.size
        val sixth = (root + 9) % notes.size
        val seventh = (root + 11) % notes.size

        val chords = arrayOf(minChords[root], dimChords[second], majChords[third], minChords[fourth], minChords[fifth],
                majChords[sixth], majChords[seventh])

        return chords
    }


    fun transpose(numOfHalfSteps: Int, chordsInProgression: LinkedList<ChordData>) : LinkedList<ChordData> {
        var newChordsInProgression: LinkedList<ChordData> = LinkedList()

        for (chord in chordsInProgression){
            val transposedChord = getTransposedChord(chord, numOfHalfSteps)
            newChordsInProgression.add(transposedChord)
        }
        return newChordsInProgression
    }

    fun getTransposedChord(chordIn: ChordData, numOfHalfSteps: Int) : ChordData {

        var origChordName = chordIn.noteOfChord
        var origChordScale = chordIn.scaleOfChord

        var counter = 0
        var start: Int? = null
        for (note in notes) {
            if (origChordName.equals(note)) {
                start = counter
                break
            }
            counter++
        }

        val newChordNote = notes[(start!! + numOfHalfSteps) % 12]
        val finalChord = newChordNote + " " + origChordScale

        return ChordData(finalChord, chordIn.numberOfBeats, chordIn.interval)
    }

}