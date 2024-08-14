package com.example.contactappmvvm

class Repository {
    private val listOfContact = mutableListOf<Contact>()

    fun getAllContact() = listOfContact

    fun addDataToContactList(contact: Contact) = listOfContact.add(contact)

}