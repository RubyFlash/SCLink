package com.example.sclink.presentation.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sclink.R
import com.example.sclink.databinding.LessonsItemListBinding
import com.example.sclink.domain.model.Lesson
import com.example.sclink.utils.LessonsDiffCallback
import com.example.sclink.utils.makeTextLink

class LessonsAdapter(private val context: Context) :
    ListAdapter<Lesson, LessonsAdapter.ViewHolder>(LessonsDiffCallback) {
    private lateinit var mMenuItemListener: OnLessonMenuItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LessonsItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ViewHolder(private val binding: LessonsItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(lesson: Lesson) {
            binding.lessonNameEditText.text = lesson.lessonName
            binding.lessonLinkEditText.text = lesson.lessonLink.makeTextLink(
                object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        val intent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(lesson.lessonLink))
                        startActivity(context, intent, null)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.color = Color.CYAN
                        ds.isUnderlineText = true
                    }
                })
            binding.lessonLinkEditText.movementMethod = LinkMovementMethod.getInstance()
            binding.lessonOptionsMenuBtn.setOnClickListener { view ->
                popupMenu(view)
            }
        }

        private fun popupMenu(view: View) {
            val position = adapterPosition
            val popupMenu = PopupMenu(context, view)
            popupMenu.inflate(R.menu.lesson_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.lesson_menu_send_btn -> {
                        mMenuItemListener.onLessonSendButtonClick(position = position)
                        true
                    }
                    R.id.lesson_menu_edit_btn -> {
                        mMenuItemListener.onLessonEditButtonClick(position = position)
                        true
                    }
                    R.id.lesson_menu_delete_btn -> {
                        mMenuItemListener.onLessonDeleteButtonClick(position = position)
                        true
                    }
                    else -> true
                }
            }
            popupMenu.show()
        }
    }

    fun setOnMenuItemClickListener(menuItemListener: OnLessonMenuItemClickListener) {
        mMenuItemListener = menuItemListener
    }
}
