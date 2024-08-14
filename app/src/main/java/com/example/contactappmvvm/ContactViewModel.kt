package com.example.contactappmvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactViewModel(private val repository: Repository) : ViewModel() {
    val listOfContactLiveData = MutableLiveData<List<Contact>>()

    init {
        getAllData()
    }

    private fun getAllData() {
        val allContact = repository.getAllContact()
        listOfContactLiveData.postValue(allContact)
    }

    fun addContact(contact: Contact) {
        repository.addDataToContactList(contact)
    }

}