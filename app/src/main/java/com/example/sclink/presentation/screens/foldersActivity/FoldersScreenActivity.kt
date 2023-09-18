package com.example.sclink.presentation.screens.foldersActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sclink.R
import com.example.sclink.databinding.ActivityFoldersScreenBinding
import com.example.sclink.domain.model.Folder
import com.example.sclink.presentation.adapters.FoldersAdapter
import com.example.sclink.presentation.adapters.OnItemClickListener
import com.example.sclink.presentation.adapters.OnFolderMenuItemClickListener
import com.example.sclink.presentation.screens.lessonsActivity.LessonsScreenActivity
import com.example.sclink.utils.Constants.DAY_OF_WEEK
import com.example.sclink.utils.Constants.FOLDER_ID
import com.example.sclink.utils.Constants.NO_POSITION
import com.example.sclink.utils.attachFab
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class FoldersScreenActivity : AppCompatActivity(), OnFolderMenuItemClickListener,
    OnItemClickListener {

    private var _binding: ActivityFoldersScreenBinding? = null
    private val binding get() = _binding

    private val foldersViewModel: FoldersScreenViewModel by viewModels()
    private val foldersAdapter: FoldersAdapter by lazy { FoldersAdapter(this) }

//    private lateinit var currentTypeOfWeek: String

    private var onExtendClicked: Boolean = false

    private var folderList: List<Folder> = emptyList()

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_anim
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFoldersScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.apply {
            rvFolders.apply {
                layoutManager = LinearLayoutManager(this@FoldersScreenActivity)
                adapter = foldersAdapter
                setHasFixedSize(true)
                attachFab(fabExtend) { onScrolled ->
                    if(onExtendClicked) {
                        onExtendFabClicked(onScrolled)
                    }
                }
            }
        }
        foldersViewModel.isFabClicked.observe(this, Observer {
            onExtendClicked = it
        })

        foldersViewModel.getAllFolders().observe(this) {
            foldersAdapter.submitList(it)
            folderList = it
        }

//        foldersViewModel.gettTypeOfWeek().observe(this) {
//            currentTypeOfWeek = it
//        }

//        binding?.weekTextView?.text = currentTypeOfWeek

        binding?.fabExtend?.setOnClickListener {
            onExtendFabClicked(clicked = onExtendClicked)
        }

        binding?.fabCreateFolder?.setOnClickListener {
            showFolderDialog(NO_POSITION)
        }

        binding?.fabDownloadExcel?.setOnClickListener {
            Toast.makeText(this, "Excel", Toast.LENGTH_LONG).show()
        }

        binding?.switchWeekBtn?.setOnClickListener {
            onSwitchWeekBtnClicked()
        }

        foldersAdapter.setOnItemClickListener(this)

        foldersAdapter.setOnMenuItemClickListener(this)
    }

    private fun onExtendFabClicked(clicked: Boolean) {
        setFabVisibility(clicked = clicked)
        setFabAnimation(clicked = clicked)
        setFabClickable(clicked = clicked)
        foldersViewModel.updateFabClickedValue(!onExtendClicked)
    }

    private fun setFabVisibility(clicked: Boolean) {
        if (!clicked) {
            binding?.fabCreateFolder?.visibility = View.VISIBLE
            binding?.fabDownloadExcel?.visibility = View.VISIBLE
        } else {
            binding?.fabCreateFolder?.visibility = View.INVISIBLE
            binding?.fabDownloadExcel?.visibility = View.INVISIBLE
        }
    }

    private fun setFabAnimation(clicked: Boolean) {
        if (!clicked) {
            binding?.fabExtend?.startAnimation(rotateOpen)
            binding?.fabCreateFolder?.startAnimation(fromBottom)
            binding?.fabDownloadExcel?.startAnimation(fromBottom)
        } else {
            binding?.fabExtend?.startAnimation(rotateClose)
            binding?.fabCreateFolder?.startAnimation(toBottom)
            binding?.fabDownloadExcel?.startAnimation(toBottom)
        }
    }

    private fun setFabClickable(clicked: Boolean) {
        if (!clicked) {
            binding?.fabCreateFolder?.isClickable = true
            binding?.fabDownloadExcel?.isClickable = true
        } else {
            binding?.fabCreateFolder?.isClickable = false
            binding?.fabDownloadExcel?.isClickable = false
        }
    }

    private fun onSwitchWeekBtnClicked() {

//        if (binding?.weekTextView!!.text.equals(upperWeek)) {
//            saveTypeOfWeek(lowerWeek)
//        } else if (binding?.weekTextView!!.text.equals(lowerWeek)) {
//            saveTypeOfWeek(upperWeek)
////        } else {
////            saveTypeOfWeek(upperWeek)
////        }
//        }
    }

    private fun saveTypeOfWeek(typeOfWeek: String) {
//        foldersViewModel.setTypeOfWeek(typeOfWeek)
////        binding?.weekTextView!!.text = typeOfWeek
////        editor.putString(SHARED_PREFERENCES_WEEK_KEY, typeOfWeek)
////        editor.apply()
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
            foldersViewModel.updateFolderEditingValue(isEditing = true)
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
                true -> foldersViewModel.updateFolderEditingValue(isEditing = false)
                else -> dialog.dismiss()
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun addFolder(folderName: String) {
        foldersViewModel.insertFolder(folder = Folder(folderName = folderName))
    }

    private fun updateFolder(folderId: Int, folderName: String) {
        foldersViewModel.updateFolder(folder = Folder(folderId, folderName))
        foldersViewModel.updateFolderEditingValue(isEditing = false)
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
