package com.example.pluralsightnotekeeper

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private val tag = this::class.simpleName
    private var notePosition = POSITION_NOT_SET


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)
//        setSupportActionBar(findViewById(R.id.toolbar))

        val adapterCourses = ArrayAdapter<CourseInfo>(this,
        android.R.layout.simple_spinner_item,
        DataManager.courses.values.toList())
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinnerCourses = findViewById<Spinner>(R.id.spinnerCourses)
        spinnerCourses.adapter = adapterCourses

        notePosition = savedInstanceState?.getInt(NOTE_POSITION, POSITION_NOT_SET) ?:
            intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET)

        if (notePosition != POSITION_NOT_SET)
            displayNote()
        else {
            createNewNote()
        }
        Log.d(tag, "onCreate")
    }

    private fun createNewNote() {
        DataManager.notes.add(NoteInfo())
        notePosition = DataManager.notes.lastIndex
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(NOTE_POSITION, notePosition)
    }

    private fun displayNote() {
        if (notePosition > DataManager.notes.lastIndex) {
            val textNoteTitle = findViewById<TextView>(R.id.textNoteTitle)
            showMessage(textNoteTitle,"Note not found")
            Log.e(tag, "Invalid note posiotion $notePosition, max valid position ${DataManager.notes.lastIndex}")
            return
        }



        Log.i(tag, "Displaying note for position $notePosition")
        val note = DataManager.notes[notePosition]

        val textNoteTitle = findViewById<TextView>(R.id.textNoteTitle)
        val textNoteText = findViewById<TextView>(R.id.textNoteText)
        val spinnerCourses = findViewById<Spinner>(R.id.spinnerCourses)

        textNoteTitle.text = note.title
        textNoteText.text = note.text

        val coursePosition = DataManager.courses.values.indexOf(note.course)
        spinnerCourses.setSelection(coursePosition)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_next -> {
                if(notePosition < DataManager.notes.lastIndex) {
                    moveNext()
                } else {
                    val textNoteTitle = findViewById<TextView>(R.id.textNoteTitle)
                    val message = "No more notes"
                    showMessage(textNoteTitle, message)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showMessage(textNoteTitle: TextView, message: String) {
        Snackbar.make(textNoteTitle, message, Snackbar.LENGTH_LONG).show()
    }

    private fun moveNext() {
        ++notePosition
        displayNote()
        invalidateOptionsMenu()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if(notePosition >= DataManager.notes.lastIndex) {
            val menuItem = menu?.findItem(R.id.action_next )
            if (menuItem != null) {
                menuItem.icon = getDrawable(R.drawable.ic_baseline_block_24)
                menuItem.isEnabled = false
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onPause() {
        super.onPause()
        saveNote()
        Log.d(tag, "onPause")
    }

    private fun saveNote() {
        val textNoteTitle = findViewById<TextView>(R.id.textNoteTitle).text
        val textNoteText = findViewById<TextView>(R.id.textNoteText).text
        val spinnerCourses = findViewById<Spinner>(R.id.spinnerCourses)

        DataManager.notes[notePosition].title = textNoteTitle.toString()
        DataManager.notes[notePosition].text = textNoteText.toString()
        DataManager.notes[notePosition].course = spinnerCourses.selectedItem as CourseInfo
    }
}
