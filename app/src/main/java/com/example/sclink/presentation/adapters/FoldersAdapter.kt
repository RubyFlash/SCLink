package com.example.sclink.presentation.adapters

import android.content.Context
import android.view.*
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sclink.R
import com.example.sclink.databinding.FoldersItemListBinding
import com.example.sclink.domain.model.Folder
import com.example.sclink.utils.FoldersDiffCallback

class FoldersAdapter(private val context: Context) :
    ListAdapter<Folder, FoldersAdapter.ViewHolder>(FoldersDiffCallback) {
    private lateinit var mItemListener: OnItemClickListener
    private lateinit var mMenuItemListener: OnFolderMenuItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            FoldersItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ViewHolder(private val binding: FoldersItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(folder: Folder) {
            binding.folderName.text = folder.folderName
            binding.folderOptionsMenuBtn.setOnClickListener { v ->
                popupMenu(v)
            }
        }

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    mItemListener.onItemClick(position)
                }
            }
        }

        private fun popupMenu(v: View) {
            val position = adapterPosition
            val popupMenu = PopupMenu(context, v)
            popupMenu.inflate(R.menu.folder_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.folder_menu_edit_btn -> {
                        mMenuItemListener.onFolderEditButtonClick(position = position)
                        true
                    }
                    R.id.folder_menu_delete_btn -> {
                        mMenuItemListener.onFolderDeleteButtonClick(position = position)
                        true
                    }
                    else -> true
                }
            }
            popupMenu.show()
        }
    }

    fun setOnItemClickListener(itemListener: OnItemClickListener) {
        mItemListener = itemListener
    }

    fun setOnMenuItemClickListener(menuItemListener: OnFolderMenuItemClickListener) {
        mMenuItemListener = menuItemListener
    }
}


