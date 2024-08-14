package com.example.contactappmvvm

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var fab: FloatingActionButton

    private lateinit var adapter: ContactAdapter
//    private lateinit var contactList: MutableList<Contact>

    private lateinit var repository: Repository
    private lateinit var viewModel: ContactViewModel
    private lateinit var viewModelFactory: ContactViewModelFactory

    private lateinit var nameEdt: EditText
    private lateinit var phoneEdt: EditText
    private lateinit var previewImage: ImageView
    private lateinit var chooseImageBtn: Button
    private lateinit var saveContactBtn: Button

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        rv = findViewById(R.id.rv)
        fab = findViewById(R.id.fab)

        repository = Repository()
        viewModelFactory = ContactViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ContactViewModel::class.java]

//        contactList = mutableListOf()


        rv.layoutManager = LinearLayoutManager(this)


        viewModel.listOfContactLiveData.observe(this) {
            adapter = ContactAdapter(it)
            rv.adapter = adapter
        }

        fab.setOnClickListener {
            showDialog()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_dialog)

        nameEdt = dialog.findViewById(R.id.edt_name)
        phoneEdt = dialog.findViewById(R.id.edt_phone)
        previewImage = dialog.findViewById(R.id.imageView)
        chooseImageBtn = dialog.findViewById(R.id.btn_choose_image)
        saveContactBtn = dialog.findViewById(R.id.btn_contact)

        // Reset the image and imageUri when the dialog opens
        previewImage.setImageURI(null) // Clear the image view
        selectedImageUri = null // Reset the image URI

        chooseImageBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100)
        }

        saveContactBtn.setOnClickListener {
            val name = nameEdt.text.toString()
            val phone = phoneEdt.text.toString()

            if (name.isNotEmpty() && phone.isNotEmpty()) {
                val newContact = Contact(selectedImageUri, name, phone)
//                contactList.add(newContact)
                viewModel.addContact(newContact)
                rv.adapter?.notifyDataSetChanged()
                dialog.dismiss()
            } else {
                // Show error message if fields are empty
                if (name.isEmpty()) nameEdt.error = "Name is required"
                if (phone.isEmpty()) phoneEdt.error = "Phone is required"
            }
        }

        dialog.show()
    }

    @Deprecated("Deprecated")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            previewImage.visibility = View.VISIBLE
            selectedImageUri = data?.data
            previewImage.setImageURI(selectedImageUri)
        }
    }
}