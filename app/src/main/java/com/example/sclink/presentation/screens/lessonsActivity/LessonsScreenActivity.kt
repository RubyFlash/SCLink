package com.example.sclink.presentation.screens.lessonsActivity

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.example.sclink.R
import com.example.sclink.databinding.ActivityLessonsScreenBinding
import com.example.sclink.domain.model.Lesson
import com.example.sclink.presentation.adapters.LessonsAdapter
import com.example.sclink.presentation.adapters.OnLessonMenuItemClickListener
import com.example.sclink.utils.*
import com.example.sclink.utils.Constants.DAY_OF_WEEK
import com.example.sclink.utils.Constants.FOLDER_ID
import com.example.sclink.utils.Constants.INTENT_TYPE_TEXT
import com.example.sclink.utils.Constants.NO_POSITION
import dagger.hilt.android.AndroidEntryPoint
import java.lang.StringBuilder

@AndroidEntryPoint
class LessonsScreenActivity : AppCompatActivity(), OnLessonMenuItemClickListener {

    private var _binding: ActivityLessonsScreenBinding? = null
    private val binding get() = _binding

    private val lessonsViewModel: LessonsScreenViewModel by viewModels()
    private val lessonsAdapter: LessonsAdapter by lazy { LessonsAdapter(this) }

    private val folderId: Int by lazy { intent.getIntExtra(FOLDER_ID, 0) }
    private val dayOfWeek: String? by lazy { intent.getStringExtra(DAY_OF_WEEK) }

    private var lessonList: List<Lesson> = emptyList()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLessonsScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val toolbar = findViewById<Toolbar>(R.id.lessonToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.lesson_screen_toolbar)

        binding?.apply {
            rvLessons.apply {
                adapter = lessonsAdapter
                setHasFixedSize(true)
                attachFab(fabAddLesson) {

                }
            }
        }

        lessonsViewModel.getLessonsByDayOfWeek(folderId, dayOfWeek!!).observe(this) {
            lessonsAdapter.submitList(it)
            lessonList = it
        }

        binding?.fabAddLesson?.setOnClickListener {
            showLessonDialog(NO_POSITION)
        }

        lessonsAdapter.setOnMenuItemClickListener(this)
    }

    private fun showLessonDialog(position: Int) {
        val view = View.inflate(this, R.layout.layout_custom_create_lesson_dialog, null)

        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        builder.setCancelable(false)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val lessonName = view.findViewById<EditText>(R.id.lessonDialogEditName)
        val lessonLink = view.findViewById<EditText>(R.id.lessonDialogEditLink)

        if (position != NO_POSITION && lessonList[position].lessonName != "") {
            lessonsViewModel.changeLessonEditingValue(isEditing = true)
            lessonName.setText(lessonList[position].lessonName)
            lessonLink.setText(lessonList[position].lessonLink)
        }

        view.findViewById<Button>(R.id.lessonDialogEnterBtn).setOnClickListener {
            if (lessonName.text.isNotEmpty() &&
                lessonLink.text.isNotEmpty() &&
                lessonLink.text.toString().isValidUrl()
            ) {
                when (lessonsViewModel.isLessonEditing.value) {
                    true -> updateLesson(
                        lessonName = lessonName.text.toString(),
                        lessonLink = lessonLink.text.toString(),
                        lessonId = lessonList[position].lessonId
                    )

                    else -> insertLesson(
                        lessonName = lessonName.text.toString(),
                        lessonLink = lessonLink.text.toString()
                    )
                }
                dialog.dismiss()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.lesson_empty_dialog_field),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
        }

        view.findViewById<Button>(R.id.lessonDialogCancelBtn).setOnClickListener {
            when (lessonsViewModel.isLessonEditing.value) {
                true -> lessonsViewModel.changeLessonEditingValue(isEditing = false)
                else -> dialog.dismiss()
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun insertLesson(lessonName: String, lessonLink: String) {
        lessonsViewModel.insertLessons(
            lesson = Lesson(
                folderId = folderId,
                dayOfWeek = dayOfWeek!!,
                lessonName = lessonName,
                lessonLink = lessonLink,
            )
        )
    }

    private fun updateLesson(lessonName: String, lessonLink: String, lessonId: Int) {
        lessonsViewModel.updateLessons(
            Lesson(
                lessonId = lessonId,
                folderId = folderId,
                dayOfWeek = dayOfWeek!!,
                lessonName = lessonName,
                lessonLink = lessonLink
            )
        )
        lessonsViewModel.changeLessonEditingValue(isEditing = false)
    }

    private fun sendSingleLesson(position: Int) {
        val blank = "\n" +
                resources.getString(R.string.lesson_name) + " " + lessonList[position].lessonName + "\n" +
                resources.getString(R.string.lesson_link) + " " + lessonList[position].lessonLink
        emailIntent(blank = blank)
    }

    private fun sendListOfLessons() {
        if (lessonList.isNotEmpty()) {
            val blank = stringBuilder()
            emailIntent(blank = blank)
        } else {
            Toast.makeText(this, resources.getString(R.string.nothing_to_send), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun emailIntent(blank: String) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = INTENT_TYPE_TEXT
        emailIntent.putExtra(Intent.EXTRA_TEXT, blank)
        try {
            startActivity(
                Intent.createChooser(
                    emailIntent,
                    resources.getString(R.string.send_to)
                )
            )
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this,
                resources.getString(R.string.nowhere_to_send),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @SuppressLint("ResourceType")
    private fun stringBuilder(): String {
        val builder = StringBuilder()
        for (i in lessonList.indices) {
            builder.append(
                "\n" + (i + 1) + ". " +
                        resources.getString(R.string.lesson_name) + " " + lessonList[i].lessonName + "\n" +
                        resources.getString(R.string.lesson_link) + " " + lessonList[i].lessonLink
            )
        }
        return builder.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.lesson_send_action, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.send_menu_btn -> sendListOfLessons()
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onLessonDeleteButtonClick(position: Int) {
        lessonsViewModel.deleteLessons(lessonList[position])
    }

    override fun onLessonEditButtonClick(position: Int) {
        showLessonDialog(position = position)
    }

    override fun onLessonSendButtonClick(position: Int) {
        sendSingleLesson(position)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

