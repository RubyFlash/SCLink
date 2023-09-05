package com.example.sclink.presentation.ui.foldersActivity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sclink.R
import com.example.sclink.databinding.ActivityFoldersScreenBinding
import com.example.sclink.domain.model.Folder
import com.example.sclink.presentation.ui.adapters.FoldersAdapter
import com.example.sclink.presentation.ui.adapters.OnItemClickListener
import com.example.sclink.presentation.ui.adapters.OnFolderMenuItemClickListener
import com.example.sclink.presentation.ui.lessonsActivity.LessonsScreenActivity
import com.example.sclink.utils.Constants.DAY_OF_WEEK
import com.example.sclink.utils.Constants.FOLDER_ID
import com.example.sclink.utils.Constants.NO_POSITION
import com.example.sclink.utils.Constants.SHARED_PREFERENCES
import com.example.sclink.utils.Constants.SHARED_PREFERENCES_WEEK_KEY
import com.example.sclink.utils.attachFab
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoldersScreenActivity : AppCompatActivity(), OnFolderMenuItemClickListener, OnItemClickListener {

    private var _binding: ActivityFoldersScreenBinding? = null
    private val binding get() = _binding

    private val foldersViewModel: FoldersScreenViewModel by viewModels()
    private val foldersAdapter: FoldersAdapter by lazy { FoldersAdapter(applicationContext) }

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    private var folderList: List<Folder> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFoldersScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        sharedPreferences =
            this.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        val upperWeek: String = resources.getString(R.string.upper_week)
        val lowerWeek: String = resources.getString(R.string.lower_week)

        binding?.weekTextView?.text = sharedPreferences.getString(
            SHARED_PREFERENCES_WEEK_KEY,
            resources.getString(R.string.choose_type_of_week)
        )

        binding?.apply {
            rvFolders.apply {
                layoutManager = LinearLayoutManager(this@FoldersScreenActivity)
                adapter = foldersAdapter
                setHasFixedSize(true)
                attachFab(fabAddFolder)
            }
        }

        foldersViewModel.getAllFolders().observe(this) {
            foldersAdapter.submitList(it)
            folderList = it
        }

        binding?.fabAddFolder?.setOnClickListener {
            showFolderDialog(NO_POSITION)
        }

        binding?.switchWeekBtn?.setOnClickListener {
            if (binding?.weekTextView!!.text.equals(upperWeek)) {
                binding?.weekTextView!!.text = lowerWeek
                saveTypeOfWeek(SHARED_PREFERENCES_WEEK_KEY, lowerWeek)
            } else if (binding?.weekTextView!!.text.equals(lowerWeek)) {
                binding?.weekTextView!!.text = upperWeek
                saveTypeOfWeek(SHARED_PREFERENCES_WEEK_KEY, upperWeek)
            } else {
                binding?.weekTextView!!.text = upperWeek
                saveTypeOfWeek(SHARED_PREFERENCES_WEEK_KEY, upperWeek)
            }
        }

        foldersAdapter.setOnItemClickListener(this)

        foldersAdapter.setOnMenuItemClickListener(this)
    }

    private fun saveTypeOfWeek(key: String, typeOfWeek: String) {
        editor.putString(key, typeOfWeek)
        editor.apply()
    }

    private fun showFolderDialog(position: Int) {
        val view = View.inflate(this, R.layout.layout_custom_create_folder_dialog, null)

        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        builder.setCancelable(false)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        var folderId = 0

        val folderName = view.findViewById<EditText>(R.id.folderDialogEditText)

        if (position != NO_POSITION && folderList[position].folderName != "") {
            foldersViewModel.changeFolderEditingValue()
            folderName.setText(folderList[position].folderName)
            folderId = folderList[position].folderId
        }

        view.findViewById<Button>(R.id.folderDialogEnterBtn).setOnClickListener {
            when (foldersViewModel.isFolderEditing.value) {
                true -> updateFolder(folderId = folderId, folderName = folderName.text.toString())
                else -> addFolder(folderName = folderName.text.toString())
            }
            dialog.dismiss()
        }

        view.findViewById<Button>(R.id.folderDialogCancelBtn).setOnClickListener {
            when (foldersViewModel.isFolderEditing.value) {
                true -> foldersViewModel.changeFolderEditingValue()
                else -> dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun addFolder(folderName: String) {
        foldersViewModel.insertFolder(folder = Folder(folderName = folderName))
    }

    private fun updateFolder(folderId: Int, folderName: String) {
        foldersViewModel.updateFolder(folder = Folder(folderId, folderName))
        foldersViewModel.changeFolderEditingValue()
    }

    override fun onItemClick(position: Int) {
        val daysList = arrayOf(
            resources.getString(R.string.day_monday),
            resources.getString(R.string.day_tuesday),
            resources.getString(R.string.day_wednesday),
            resources.getString(R.string.day_thursday),
            resources.getString(R.string.day_friday),
            resources.getString(R.string.day_saturday),
            resources.getString(R.string.day_sunday)
        )

        val sChoiceDialogBuilder = MaterialAlertDialogBuilder(this)

        sChoiceDialogBuilder.setTitle(resources.getString(R.string.choose_day_title))

        sChoiceDialogBuilder.setSingleChoiceItems(daysList, -1) { dialog, i ->
            val currentFolderId = folderList[position].folderId
            val currentDay = daysList[i]

            val intent = Intent(this, LessonsScreenActivity::class.java)

            intent.putExtra(FOLDER_ID, currentFolderId)
            intent.putExtra(DAY_OF_WEEK, currentDay)

            startActivity(intent)

            dialog.dismiss()
        }

        sChoiceDialogBuilder.create().show()
    }

    override fun onFolderDeleteButtonClick(position: Int) {
        foldersViewModel.deleteFolder(folderList[position])
    }

    override fun onFolderEditButtonClick(position: Int) {
        showFolderDialog(position)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
