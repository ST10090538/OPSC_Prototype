package com.example.opscprototype

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

@Suppress("DEPRECATION")
class ProfilePage : AppCompatActivity() {
    companion object {
        const val PICK_IMAGE_REQUEST = 1
        const val REQUEST_IMAGE_CAPTURE = 2
        const val PROFILE_PICTURE_ACHIEVEMENT = "profilePictureAchievement"
        const val FIRST_SIGN_IN_ACHIEVEMENT = "firstSignInAchievement"
    }

    private var imgPicture: Bitmap? = null
    private val storageRef = FirebaseStorage.getInstance().reference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_page)

        val progressIcon = findViewById<ImageView>(R.id.profile_progress_button)
        val timesheetIcon = findViewById<ImageView>(R.id.profile_timesheet_button)
        val addPictureButton = findViewById<ImageView>(R.id.Profilepage_uploadimage)

        val profileImageRef = storageRef.child("${SharedData.currentUser}/profilePic/profile.jpg")
        val MAX_SIZE_BYTES: Long = 1024 * 1024 // Maximum size of the downloaded image data
        profileImageRef.getBytes(MAX_SIZE_BYTES).addOnSuccessListener { imageData ->
            val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
            // Use the bitmap as needed
            imgPicture = bitmap
            updateImageIcon()
        }.addOnFailureListener {
        }


        progressIcon.setOnClickListener {
            startActivity(Intent(this, ProgressPage::class.java))
        }

        timesheetIcon.setOnClickListener {
            startActivity(Intent(this, TimesheetPage::class.java))
        }

        addPictureButton.setOnClickListener {
            showPictureDialog()
        }



        val achievement2 = findViewById<ImageView>(R.id.achievement2)
        val showAchievement2 = intent.getBooleanExtra("showAchievement2", false)
        if (showAchievement2) {
            achievement2.visibility = View.VISIBLE
            // You can also set an animation or perform any other actions for the achievement here
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
        startActivityForResult(galleryIntent, NewTaskPage.PICK_IMAGE_REQUEST)
    }

    private fun takePhotoFromCamera() {
        // Check camera permission
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.CAMERA),
                NewTaskPage.CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted, proceed with camera intent
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, NewTaskPage.REQUEST_IMAGE_CAPTURE)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NewTaskPage.CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, proceed with camera intent
                takePhotoFromCamera()
            } else {
                // Camera permission denied, show an error message or handle it accordingly
            }
        }
    }

    @Deprecated("Deprecated in Java")
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
                    if (imgPicture != null) {
                        // Convert bitmap to byte array
                        val stream = ByteArrayOutputStream()
                        imgPicture?.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        val imageData = stream.toByteArray()

                        // Upload the byte array to Firebase Storage
                        val storageRef = FirebaseStorage.getInstance().reference
                        val taskImageRef =
                            storageRef.child("${SharedData.currentUser}/profilePic/profile.jpg")

                        taskImageRef.putBytes(imageData)
                    }
                    updateImageIcon()
                }
            }
        }
    }

    private fun updateImageIcon() {
        val addPictureButton = findViewById<ImageView>(R.id.Profilepage_uploadimage)
        addPictureButton.setImageBitmap(imgPicture)
        checkProfilePictureAchievement()
    }

    private fun checkProfilePictureAchievement() {
        if (isAchievementEarned(PROFILE_PICTURE_ACHIEVEMENT)) {
            val profilePictureAchievement = findViewById<ImageView>(R.id.achievement3)
            profilePictureAchievement.visibility = View.VISIBLE
        }
        if (isAchievementEarned(FIRST_SIGN_IN_ACHIEVEMENT)) {
            val firstSignInAchievement = findViewById<ImageView>(R.id.achievement1)
            firstSignInAchievement.visibility = View.VISIBLE
        }
    }



    private fun grantProfilePictureAchievement() {
        if (!isAchievementEarned(PROFILE_PICTURE_ACHIEVEMENT)) {
            grantAchievement(PROFILE_PICTURE_ACHIEVEMENT)
            val achievementMessage = getAchievementMessage(PROFILE_PICTURE_ACHIEVEMENT)
            showAchievementMessage(achievementMessage)
        }
        if (!isAchievementEarned(FIRST_SIGN_IN_ACHIEVEMENT)) {
            grantAchievement(FIRST_SIGN_IN_ACHIEVEMENT)
            val achievementMessage = getAchievementMessage(FIRST_SIGN_IN_ACHIEVEMENT)
            showAchievementMessage(achievementMessage)
        }
    }


    private fun isAchievementEarned(achievementId: String): Boolean {
        val sharedPreferences = getSharedPreferences("achievements", MODE_PRIVATE)
        return sharedPreferences.getBoolean(achievementId, false)
    }

    private fun grantAchievement(achievementId: String) {
        val sharedPreferences = getSharedPreferences("achievements", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(achievementId, true).apply()
    }

    private fun getAchievementMessage(achievementId: String): String {
        return when (achievementId) {
            PROFILE_PICTURE_ACHIEVEMENT -> "Congratulations! You earned an achievement for uploading a profile picture."
            FIRST_SIGN_IN_ACHIEVEMENT -> "Congratulations! You earned an achievement for signing in for the first time."
            else -> ""
        }
    }

    private fun showAchievementMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }




}

