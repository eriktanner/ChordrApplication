package com.euna.chordr;

class Chords {
    val modes = arrayOf("Major", "Minor")
    val notes = arrayOf("A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#")
    val majChords = arrayOf("A maj", "A# maj", "B maj", "C maj", "C# maj", "D maj", "D# maj", "E maj", "F maj", "F# maj", "G maj", "G# maj")
    val minChords = arrayOf("A min", "A# min", "B min", "C min", "C# min", "D min", "D# min", "E min", "F min", "F# min", "G min", "G# min")
    val dimChords = arrayOf("A dim", "A# dim", "B dim", "C dim", "C# dim", "D dim", "D# dim", "E dim", "F dim", "F# dim", "G dim", "G# dim")

    fun getMajorScale(root: String): Array<String> {
        val root = notes.indexOf(root)
        val first = (root + 2) % notes.size
        val second = (root + 4) % notes.size
        val third = (root + 5) % notes.size
        val fourth = (root + 7) % notes.size
        val fifth = (root + 9) % notes.size
        val sixth = (root + 11) % notes.size
        val seventh = (root + 12) % notes.size

        val scale = arrayOf(notes[root], notes[first], notes[second], notes[third], notes[fourth],
                notes[fifth], notes[sixth], notes[seventh])

        return scale
    }

    fun getMinorScale(root: String): Array<String> {
        val root = notes.indexOf(root)
        val first = (root + 2) % notes.size
        val second = (root + 3) % notes.size
        val third = (root + 5) % notes.size
        val fourth = (root + 7) % notes.size
        val fifth = (root + 8) % notes.size
        val sixth = (root + 10) % notes.size
        val seventh = (root + 12) % notes.size

        val scale = arrayOf(notes[root], notes[first], notes[second], notes[third], notes[fourth],
                notes[fifth], notes[sixth], notes[seventh])

        return scale
    }

    fun getMajChords(root: String): Array<String> {
        val root = notes.indexOf(root)
        val first = (root + 2) % notes.size
        val second = (root + 4) % notes.size
        val third = (root + 5) % notes.size
        val fourth = (root + 7) % notes.size
        val fifth = (root + 9) % notes.size
        val sixth = (root + 11) % notes.size
        val seventh = (root + 12) % notes.size

        val chords = arrayOf(majChords[root], minChords[first], minChords[second], majChords[third], majChords[fourth],
                minChords[fifth], dimChords[sixth], majChords[seventh])

        return chords
    }

    fun getMinChords(root: String): Array<String> {
        val root = notes.indexOf(root)
        val first = (root + 2) % notes.size
        val second = (root + 4) % notes.size
        val third = (root + 5) % notes.size
        val fourth = (root + 7) % notes.size
        val fifth = (root + 9) % notes.size
        val sixth = (root + 11) % notes.size
        val seventh = (root + 12) % notes.size

        val chords = arrayOf(minChords[root], dimChords[first], majChords[second], minChords[third], minChords[fourth],
                majChords[fifth], majChords[sixth], minChords[seventh])

        return chords
    }
}