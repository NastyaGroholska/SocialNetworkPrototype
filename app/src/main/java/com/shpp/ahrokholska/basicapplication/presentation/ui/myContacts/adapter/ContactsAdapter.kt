package com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shpp.ahrokholska.basicapplication.databinding.ContactsItemBinding
import com.shpp.ahrokholska.basicapplication.databinding.ContactsItemMultiselectBinding
import com.shpp.ahrokholska.basicapplication.domain.model.Contact
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.adapter.diffCallback.ContactsDiffCallback
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.interfaces.ContactsNormalItemListener
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.interfaces.SelectionListener
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.viewHolders.ContactsMultiselectViewHolder
import com.shpp.ahrokholska.basicapplication.presentation.ui.myContacts.viewHolders.ContactsNormalViewHolder

class ContactsAdapter(
    private val itemListener: ContactsNormalItemListener,
    private val selectionListener: SelectionListener
) :
    ListAdapter<Contact, RecyclerView.ViewHolder>(ContactsDiffCallback()) {
    private var isMultiselectEnabled = false
    private var selectedPositions = listOf<Int>()

    private enum class ViewType(val code: Int) {
        Normal(0), Multiselect(1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (isMultiselectEnabled) {
            return ContactsMultiselectViewHolder(
                ContactsItemMultiselectBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), ::onMultiselectItemStateChange
            )
        }
        return ContactsNormalViewHolder(
            ContactsItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), selectionListener::addItemToSelection
        )
    }

    override fun getItemViewType(position: Int): Int {
        return if (isMultiselectEnabled) ViewType.Multiselect.code else ViewType.Normal.code
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ContactsNormalViewHolder -> holder.bindTo(getItem(position), itemListener)
            is ContactsMultiselectViewHolder -> holder.bindTo(
                getItem(position), selectedPositions.contains(position)
            )
        }
    }

    fun deleteMultiselectItems(): List<Pair<Int, Contact>> {
        val itemsToRemove = selectedPositions.sorted().map { it to getItem(it) }
        selectionListener.clearSelection()
        return itemsToRemove
    }

    fun changeMultiselectState(isMultiselectEnabled: Boolean) {
        this.isMultiselectEnabled = isMultiselectEnabled
    }

    fun changeMultiselectItems(selectedPositions: List<Int>) {
        this.selectedPositions = selectedPositions
    }

    private fun onMultiselectItemStateChange(isChecked: Boolean, adapterPosition: Int) {
        if (isChecked) {
            selectionListener.addItemToSelection(adapterPosition)
        } else {
            selectionListener.removeItemFromSelection(adapterPosition)
        }
    }
}