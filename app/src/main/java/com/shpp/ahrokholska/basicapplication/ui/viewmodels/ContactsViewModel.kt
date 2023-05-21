package com.shpp.ahrokholska.basicapplication.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.shpp.ahrokholska.basicapplication.data.Contact
import com.shpp.ahrokholska.basicapplication.data.ContactsDB
import kotlinx.coroutines.flow.StateFlow

class ContactsViewModel : ViewModel() {
    private val contactsDB = ContactsDB
    val contacts: StateFlow<List<Contact>> = contactsDB.contacts

    fun getContactWithId(id: Long): Contact {
        return contactsDB.getContactWithId(id)
    }

    fun deleteContact(contact: Contact) {
        contactsDB.removeWithId(contact.id)
    }

    fun addContact(name: String, career: String) {
        contactsDB.addContact(name, career)
    }

    fun insertContact(contact: Contact, position: Int) {
        contactsDB.insertAt(contact, position)
    }
}