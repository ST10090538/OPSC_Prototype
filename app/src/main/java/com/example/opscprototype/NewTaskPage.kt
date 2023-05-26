package com.example.opscprototype

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.opscprototype.SharedData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NewTaskPage: AppCompatActivity() {
    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val REQUEST_IMAGE_CAPTURE = 2
        private const val REQUEST_NEW_CATEGORY = 3
    }
    private var categories: Array<String> = arrayOf("Web Design", "App Development") // Initial categories
    private var newCat: String? = null
    private lateinit var categorySpinner: Spinner
    var imgPicture: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_task_page)
        newCat= intent.getStringExtra("newCat")
        val progressIcon = findViewById<ImageView>(R.id.newtask_progress_button)
        val profileIcon = findViewById<ImageView>(R.id.newtask_profile_button)
        val timesheetIcon = findViewById<ImageView>(R.id.newtask_timesheet_button)
        val newCategoryButton = findViewById<Button>(R.id.newtask_new_category_button)
        val submitButton = findViewById<Button>(R.id.newtask_submit_button)
        val addPictureButton = findViewById<ImageView>(R.id.newtask_uploadimage)
        var startDate = Date()
        var endDate = Date()
        var minHours = 0.00
        var maxHours = 0.00
        var startTime = ""
        var endTime = ""


        //Adds the new category to the array
        if(newCat != "")
            categories += newCat.toString()

        categorySpinner = findViewById(R.id.categorySpinner)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        //Allow the user to pick a start date
        findViewById<Button>(R.id.newtask_start_date).setOnClickListener(){
            val currentDate = Calendar.getInstance()
            val year = currentDate.get(Calendar.YEAR)
            val month = currentDate.get(Calendar.MONTH)
            val day = currentDate.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = String.format("%04d/%02d/%02d", selectedYear, selectedMonth + 1, selectedDay)
                    startDate = SimpleDateFormat("yyyy/MM/dd").parse(formattedDate)
                    val btnStartDate = findViewById<Button>(R.id.newtask_start_date)
                    btnStartDate.text = startDate.toString()
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }


        //Allow the user to pick an end date
        findViewById<Button>(R.id.newtask_end_date).setOnClickListener(){
            val currentDate = Calendar.getInstance()
            val year = currentDate.get(Calendar.YEAR)
            val month = currentDate.get(Calendar.MONTH)
            val day = currentDate.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = String.format("%04d/%02d/%02d", selectedYear, selectedMonth + 1, selectedDay)
                    endDate = SimpleDateFormat("yyyy/MM/dd").parse(formattedDate)
                    val btnEndDate = findViewById<Button>(R.id.newtask_end_date)
                    btnEndDate.text = endDate.toString()
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        //Set min daily goal
        findViewById<Button>(R.id.newtask_min_hours).setOnClickListener(){
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_input, null)
            val editText = dialogView.findViewById<EditText>(R.id.txtNewCatName)

            dialogBuilder.setView(dialogView)
            dialogBuilder.setTitle("Enter Minimum Daily Goal Hours")
            dialogBuilder.setPositiveButton("OK") { dialog, which ->
                val inputText = editText.text.toString()
                minHours = inputText.toDoubleOrNull() ?: 0.0
                val btnMinHours = findViewById<Button>(R.id.newtask_min_hours)
                btnMinHours.text = minHours.toString() + " Hours"
            }
            dialogBuilder.setNegativeButton("Cancel") { dialog, which ->
                // Handle cancellation if needed
            }
            val dialog = dialogBuilder.create()
            dialog.show()
        }

        //Set max daily goal
        findViewById<Button>(R.id.newtask_max_hours).setOnClickListener(){
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_input, null)
            val editText = dialogView.findViewById<EditText>(R.id.txtNewCatName)

            dialogBuilder.setView(dialogView)
            dialogBuilder.setTitle("Enter Maximum Daily Goal Hours")
            dialogBuilder.setPositiveButton("OK") { dialog, which ->
                val inputText = editText.text.toString()
                maxHours = inputText.toDoubleOrNull() ?: 0.0
                val btnMaxHours = findViewById<Button>(R.id.newtask_max_hours)
                btnMaxHours.text = maxHours.toString() + " Hours"
            }
            dialogBuilder.setNegativeButton("Cancel") { dialog, which ->
                // Handle cancellation if needed
            }
            val dialog = dialogBuilder.create()
            dialog.show()
        }

        //Set task start time
        findViewById<Button>(R.id.newtask_start_time).setOnClickListener(){
            val currentTime = Calendar.getInstance()
            val hour = currentTime.get(Calendar.HOUR_OF_DAY)
            val minute = currentTime.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                    // Store the selected time in a variable
                    startTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    val btnStartTime = findViewById<Button>(R.id.newtask_start_time)
                    btnStartTime.text = startTime
                },
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        }

        // Set task end time
        findViewById<Button>(R.id.newtask_end_time).setOnClickListener(){
            val currentTime = Calendar.getInstance()
            val hour = currentTime.get(Calendar.HOUR_OF_DAY)
            val minute = currentTime.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                    // Store the selected time in a variable
                    endTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    val btnEndTime = findViewById<Button>(R.id.newtask_end_time)
                    btnEndTime.text = endTime
                },
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        }

        submitButton.setOnClickListener {
            val newTaskName = findViewById<EditText>(R.id.txtNewTaskName).text.toString()
            val description = findViewById<EditText>(R.id.txtNewTaskDesc).text.toString()
            val category = categorySpinner.selectedItem.toString()
            val newTask = Tasks(newTaskName, category, description, startDate, endDate,startTime, endTime,
                minHours, maxHours, imgPicture)
            SharedData.lstTasks += newTask
            startActivity(Intent(this, TimesheetViewPage::class.java))
        }

        newCategoryButton.setOnClickListener {
            val intent = Intent(this, NewCategoryPage::class.java)
            startActivityForResult(intent, REQUEST_NEW_CATEGORY)
        }

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        progressIcon.setOnClickListener {
            startActivity(Intent(this, ProgressPage::class.java))
        }

        timesheetIcon.setOnClickListener {
            startActivity(Intent(this, TimesheetPage::class.java))
        }
        addPictureButton.setOnClickListener{
            showPictureDialog()
        }

    }
    //Allows the user to choose how to add a picture
    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> choosePhotoFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun choosePhotoFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    //Updates the categories
    private fun updateCategorySpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val selectedImage = data?.data
                    imgPicture = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
                    updateImageIcon()
                }
                REQUEST_IMAGE_CAPTURE -> {
                    imgPicture = data?.extras?.get("data") as Bitmap
                    updateImageIcon()
                }
                REQUEST_NEW_CATEGORY -> {
                    val newCat = data?.getStringExtra("newCat")
                    newCat?.let {
                        categories += it
                        updateCategorySpinner()
                    }
                }
            }
        }
    }

    private fun updateImageIcon() {
        val addPictureButton = findViewById<ImageView>(R.id.newtask_uploadimage)
        addPictureButton.background = null
        addPictureButton.setImageBitmap(imgPicture)
    }
}