package com.example.opscprototype

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Suppress("DEPRECATION")
class ProfilePage : AppCompatActivity() {
    companion object {
        const val PICK_IMAGE_REQUEST = 1
        const val REQUEST_IMAGE_CAPTURE = 2
        const val ACHIEVEMENT_UPLOAD_PROFILE_PICTURE = "achievement_upload_profile_picture"
        const val PREFS_FILE_NAME = "achievements_prefs"
        const val PREF_KEY_ACHIEVEMENT_PREFIX = "achievement_"
    }

    private var imgPicture: Bitmap? = null
    private var isProfilePictureUploaded = false
    private lateinit var achievementsPrefs: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_page)

        achievementsPrefs = getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE)


        val progressIcon = findViewById<ImageView>(R.id.profile_progress_button)
        val timesheetIcon = findViewById<ImageView>(R.id.profile_timesheet_button)
        val addPictureButton = findViewById<ImageView>(R.id.Profilepage_uploadimage)


        progressIcon.setOnClickListener {
            startActivity(Intent(this, ProgressPage::class.java))
        }

        timesheetIcon.setOnClickListener {
            startActivity(Intent(this, TimesheetPage::class.java))
        }

        addPictureButton.setOnClickListener {
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
                    updateImageIcon()

                    if (isAchievementUnlocked(ACHIEVEMENT_UPLOAD_PROFILE_PICTURE).not()) {
                        showAchievementDialog("Upload Profile Picture")
                        setAchievementUnlocked(ACHIEVEMENT_UPLOAD_PROFILE_PICTURE)
                    }
                }
            }
        }
    }

    private fun showAchievementDialog(achievementName: String) {
        val dialogTitle = "Achievement Unlocked"
        val dialogMessage = "Congratulations! You have unlocked the \"$achievementName\" achievement."

        AlertDialog.Builder(this)
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun updateImageIcon() {
        val addPictureButton = findViewById<ImageView>(R.id.Profilepage_uploadimage)
        addPictureButton.background = null
        addPictureButton.setImageBitmap(imgPicture)
        isProfilePictureUploaded = true
        checkAchievements()

    }

    private fun checkAchievements() {
        if (isProfilePictureUploaded) {
            val achievement1 = findViewById<ImageView>(R.id.achievement1)
            achievement1.visibility = View.VISIBLE
        }
    }

    private fun isAchievementUnlocked(achievementId: String): Boolean {
        return achievementsPrefs.getBoolean(getAchievementPrefKey(achievementId), false)
    }

    private fun setAchievementUnlocked(achievementId: String) {
        val editor = achievementsPrefs.edit()
        editor.putBoolean(getAchievementPrefKey(achievementId), true)
        editor.apply()
    }

    private fun getAchievementPrefKey(achievementId: String): String {
        return PREF_KEY_ACHIEVEMENT_PREFIX + achievementId
    }


}
