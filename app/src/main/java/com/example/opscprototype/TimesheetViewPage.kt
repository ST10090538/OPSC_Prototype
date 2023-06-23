package com.example.opscprototype
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Calendar
import java.util.Date
import java.util.Locale


class TimesheetViewPage : AppCompatActivity() {
    private val taskStartTimeMap: MutableMap<Int, Long> = mutableMapOf()
    private val handler: Handler = Handler()
    private lateinit var runnable: Runnable
    private var filterStart: Date? = null
    private var filterEnd: Date? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timesheet_view_page)

        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
        val progressIcon = findViewById<ImageView>(R.id.timesheetview_progress_button)
        val profileIcon = findViewById<ImageView>(R.id.timesheetview_profile_button)
        val timesheetIcon = findViewById<ImageView>(R.id.timesheetview_timesheet_button)
        val newTask = findViewById<ImageView>(R.id.timesheetview_newtask_button)
        findViewById<TextView>(R.id.colo6).text = SharedData.selectedTimeSheet
        BackButton_TimesheetViewPage()

        progressIcon.setOnClickListener {
            startActivity(Intent(this, ProgressPage::class.java))
        }

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        timesheetIcon.setOnClickListener {
            startActivity(Intent(this, TimesheetPage::class.java))
        }

        newTask.setOnClickListener {
            val intent = Intent(this, NewTaskPage::class.java)
            intent.putExtra("newCat", "")
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnStartDate).setOnClickListener() {
            val currentDate = Calendar.getInstance()
            val year = currentDate.get(Calendar.YEAR)
            val month = currentDate.get(Calendar.MONTH)
            val day = currentDate.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val calendar = Calendar.getInstance()
                    calendar.set(
                        selectedYear,
                        selectedMonth,
                        selectedDay,
                        0,
                        0,
                        0
                    ) // Set time to 23:59:59
                    filterStart = calendar.time

                    val formattedDate = SimpleDateFormat("yyyy/MM/dd").format(filterStart)
                    val btnEndDate = findViewById<Button>(R.id.btnStartDate)
                    btnEndDate.text = formattedDate
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        findViewById<Button>(R.id.btnEndDate).setOnClickListener() {
            val currentDate = Calendar.getInstance()
            val year = currentDate.get(Calendar.YEAR)
            val month = currentDate.get(Calendar.MONTH)
            val day = currentDate.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                    val calendar = Calendar.getInstance()
                    calendar.set(
                        selectedYear,
                        selectedMonth,
                        selectedDay,
                        23,
                        59,
                        59
                    ) // Set time to 23:59:59
                    filterEnd = calendar.time

                    val formattedDate = SimpleDateFormat("yyyy/MM/dd").format(filterEnd)
                    val btnEndDate = findViewById<Button>(R.id.btnEndDate)
                    btnEndDate.text = formattedDate
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        findViewById<Button>(R.id.btnApplyFilter).setOnClickListener(){
            var tasks = emptyList<Tasks>()
            for(task in SharedData.lstTasks){
                if(((task.dateCreated.after(filterStart) || task.dateCreated == filterStart) &&
                            (task.dateCreated.before(filterEnd) || task.dateCreated == filterEnd)) && task.timeSheet.equals(SharedData.selectedTimeSheet)){
                    tasks += task
                }
            }
            populateTable(tasks)
        }

        val placeholderRow = TableRow(this@TimesheetViewPage)
        val placeholderTextView = TextView(this@TimesheetViewPage)
        placeholderTextView.text = "Loading..."
        placeholderTextView.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        placeholderTextView.setPadding(16, 16, 16, 16)

// Create layout parameters for the placeholder TextView
        val placeholderTextViewParams = TableRow.LayoutParams()
        placeholderTextViewParams.span = 7 // Set the span to the number of columns in your table
        placeholderTextView.layoutParams = placeholderTextViewParams

        placeholderRow.addView(placeholderTextView)
        tableLayout.addView(placeholderRow)
        val database = Firebase.database("https://opsc-prototype-v2-default-rtdb.europe-west1.firebasedatabase.app/")

        val tasksReference = database.getReference(SharedData.currentUser).child("tasks")
        val taskListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val calendar = Calendar.getInstance()
                val tasks = mutableListOf<Tasks>()
                for (taskSnapshot in dataSnapshot.children) {
                    val taskMap = taskSnapshot.value as? Map<*, *>
                    taskMap?.let {
                        val strTaskName = it["strTaskName"] as? String
                        val strCategory = it["strCategory"] as? String
                        val strDescription = it["strDescription"] as? String
                        val strStartTime = it["strStartTime"] as? String
                        val strEndTime = it["strEndTime"] as? String
                        val dblMinGoal = it["dblMinGoal"] as? Double
                        val dblMaxGoal = it["dblMaxGoal"]?.toString()?.toDoubleOrNull()
                        val timeSheet = it["timeSheet"] as? String
                        val duration = taskSnapshot.child("durTimeWorked").child("seconds").value.toString().toLong()

                        val yearCreated = taskSnapshot.child("dateCreated").child("year").value.toString().toInt()
                        val monthCreated = taskSnapshot.child("dateCreated").child("month").value.toString().toInt()
                        val dayCreated =  taskSnapshot.child("dateCreated").child("date").value.toString().toInt()
                        calendar.set(Calendar.YEAR, (yearCreated + 1900))
                        calendar.set(Calendar.MONTH, monthCreated)
                        calendar.set(Calendar.DAY_OF_MONTH, dayCreated)
                        val dateCreated = calendar.time

                        val yearStart = taskSnapshot.child("dtStartDate").child("year").value.toString().toInt()
                        val monthStart = taskSnapshot.child("dtStartDate").child("month").value.toString().toInt()
                        val dayStart =  taskSnapshot.child("dtStartDate").child("date").value.toString().toInt()
                        calendar.set(Calendar.YEAR, (yearStart + 1900))
                        calendar.set(Calendar.MONTH, monthStart)
                        calendar.set(Calendar.DAY_OF_MONTH, dayStart)
                        val dtStartDate = calendar.time

                        val yearEnd = taskSnapshot.child("dtEndDate").child("year").value.toString().toInt()
                        val monthEnd = taskSnapshot.child("dtEndDate").child("month").value.toString().toInt()
                        val dayEnd =  taskSnapshot.child("dtEndDate").child("date").value.toString().toInt()
                        calendar.set(Calendar.YEAR, (yearEnd + 1900))
                        calendar.set(Calendar.MONTH, monthEnd)
                        calendar.set(Calendar.DAY_OF_MONTH, dayEnd)
                        val dtEndDate = calendar.time



                        // Create a new Tasks object with the retrieved data
                        val task = Tasks(
                            strTaskName ?: "",
                            strCategory ?: "",
                            strDescription ?: "",
                            dtStartDate ?: Date(),
                            dtEndDate ?: Date(),
                            strStartTime ?: "",
                            strEndTime ?: "",
                            dblMinGoal ?: 0.0,
                            dblMaxGoal ?: 0.0,
                            timeSheet ?: "",
                        )
                        task.dateCreated = dateCreated
                        task.durTimeWorked = Duration.ofSeconds(duration)
                        val defaultImageResource = R.drawable.uploadicon
                        task.imgPicture = BitmapFactory.decodeResource(resources, defaultImageResource)
                        if (task != null && task.timeSheet == SharedData.selectedTimeSheet) {
                            tasks.add(task)
                            SharedData.lstTasks += task
                        }
                    }
                }
                if (tasks.isEmpty()) {
                    // Remove the placeholder row
                    tableLayout.removeView(placeholderRow)

                    // Create a TextView for the "No tasks found" message
                    val messageTextView = TextView(this@TimesheetViewPage)
                    messageTextView.text = "No tasks found."
                    messageTextView.layoutParams = TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                    )
                    messageTextView.setPadding(16, 16, 16, 16)

                    // Create a TableRow for the message
                    val messageRow = TableRow(this@TimesheetViewPage)
                    messageRow.addView(messageTextView)
                    tableLayout.addView(messageRow)
                } else {
                    // Remove the placeholder row
                    tableLayout.removeView(placeholderRow)
                    populateTable(tasks)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
            }
        }
        tasksReference.addValueEventListener(taskListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun populateTable(tasks: List<Tasks>) {
        val database = Firebase.database("https://opsc-prototype-v2-default-rtdb.europe-west1.firebasedatabase.app/")
        val storageRef = FirebaseStorage.getInstance().reference
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
        // Clear existing table rows
        tableLayout.removeAllViews()

            // Create a TableRow for the heading
            val headingRow = TableRow(this)

            // Create and add the headings
            val taskImageHeading = createHeadingTextView("Image")
            val taskNumberHeading = createHeadingTextView("Task Number")
            val taskNameHeading = createHeadingTextView("Task Name")
            val endDateHeading = createHeadingTextView("Deadline")
            val hoursWorkedHeading = createHeadingTextView("Hours worked")
            val stopwatchHeading = createHeadingTextView("Stopwatch")

            // Add the headings to the headingRow
            headingRow.addView(taskImageHeading)
            headingRow.addView(taskNumberHeading)
            headingRow.addView(taskNameHeading)
            headingRow.addView(endDateHeading)
            headingRow.addView(hoursWorkedHeading)
            headingRow.addView(stopwatchHeading)

            // Add the headingRow to the TableLayout
            tableLayout.addView(headingRow)

            // Iterate over each task and create the necessary views


            for (i in tasks.indices) {
                val task = tasks[i]

                // Create a TableRow to hold the task information
                val tableRow = TableRow(this)

                // Create and add the views for task information
                val taskImage = ImageView(this)
                val maxWidth = 10
                val maxHeight = 10
                val resizedBitmap = task.imgPicture?.let { resizeBitmap(it, 80, 80) }
                taskImage.setImageBitmap(resizedBitmap)
                taskImage.setImageBitmap(task.imgPicture)
                taskImage.setOnClickListener {
                    showEnlargedImage(task.imgPicture)
                }

                    val taskImageRef = storageRef.child("${SharedData.currentUser}/task_images/${task.strTaskName}.jpg")
                    val MAX_SIZE_BYTES: Long = 1024 * 1024 // Maximum size of the downloaded image data
                    taskImageRef.getBytes(MAX_SIZE_BYTES).addOnSuccessListener { imageData ->
                        val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                        // Use the bitmap as needed
                        task.imgPicture = bitmap
                        val resizedBitmap = task.imgPicture?.let { resizeBitmap(it, 80, 80) }
                        taskImage.setImageBitmap(resizedBitmap)
                    }.addOnFailureListener {
                    }


                val taskNumberTextView = createTextView("${i + 1}")
                val taskNameTextView = createTextView(task.strTaskName)
                val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormatter.format(task.dtEndDate)
                val endDateTextView = createTextView(formattedDate + " at " + task.strEndTime)
                val totalDuration = task.durTimeWorked
                val hours = totalDuration.toHours()
                val minutes = (totalDuration.toMinutes() % 60)
                val seconds = (totalDuration.seconds % 60)

                val formattedDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                val hoursWorkedTextView = createTextView(formattedDuration)

                // Add the task information views to the tableRow
                tableRow.addView(taskImage)
                tableRow.addView(taskNumberTextView)
                tableRow.addView(taskNameTextView)
                tableRow.addView(endDateTextView)
                tableRow.addView(hoursWorkedTextView)

                val startButton = Button(this)
                startButton.text = "Start"
                val startButtonLayoutParams = TableRow.LayoutParams(
                    resources.getDimensionPixelSize(R.dimen.button_width),
                    resources.getDimensionPixelSize(R.dimen.button_height)
                )
                startButtonLayoutParams.marginEnd = 8
                startButton.layoutParams = startButtonLayoutParams
                startButton.setPadding(16, 8, 16, 8)
                startButton.setBackgroundResource(R.drawable.rounded_button_green)
                startButton.setOnClickListener() {
                    taskStartTimeMap[i] = System.currentTimeMillis()

                    // Start the stopwatch by scheduling a periodic update of the button text
                    runnable = object : Runnable {
                        override fun run() {
                            val startTime = taskStartTimeMap[i] ?: return
                            val elapsedTime = System.currentTimeMillis() - startTime

                            // Calculate the hours, minutes, and seconds from the elapsed time
                            val hours = elapsedTime / (1000 * 60 * 60)
                            val minutes = (elapsedTime % (1000 * 60 * 60)) / (1000 * 60)
                            val seconds = (elapsedTime % (1000 * 60)) / 1000

                            // Format the time as a string and set it as the button text
                            startButton.text =
                                String.format("%02d:%02d:%02d", hours, minutes, seconds)

                            // Schedule the next update after 1 second
                            handler.postDelayed(this, 1000)
                        }
                    }
                    // Start the initial update
                    handler.post(runnable)
                }

                tableRow.addView(startButton)

                val stopButton = Button(this)
                stopButton.text = "Stop"
                val stopButtonLayoutParams = TableRow.LayoutParams(
                    resources.getDimensionPixelSize(R.dimen.button_width),
                    resources.getDimensionPixelSize(R.dimen.button_height)
                )
                stopButtonLayoutParams.marginStart = 8
                stopButton.layoutParams = stopButtonLayoutParams
                stopButton.setPadding(16, 8, 16, 8)
                stopButton.setBackgroundResource(R.drawable.rounded_button_red)
                stopButton.setOnClickListener() {
                    val startTime = taskStartTimeMap[i] ?: return@setOnClickListener
                    // Calculate the elapsed time in milliseconds
                    val elapsedTime = System.currentTimeMillis() - startTime

                    // Stop the stopwatch by removing the scheduled updates
                    handler.removeCallbacks(runnable)
                    val elapsedTimeDuration = Duration.ofMillis(elapsedTime)

                    // Calculate the hours worked
                    val totalDuration = task.durTimeWorked.plus(elapsedTimeDuration)
                    // Update the hours worked attribute of the respective task
                    task.durTimeWorked = totalDuration
                    val durationSeconds = totalDuration.seconds.toInt()
                    database.getReference(SharedData.currentUser).child("tasks").child(task.strTaskName).child("durTimeWorked").child("seconds").setValue(durationSeconds)
                    task.updateCategoryHoursWorked(SharedData.lstCategories)
                    val hours = totalDuration.toHours()
                    val minutes = (totalDuration.toMinutes() % 60)
                    val seconds = (totalDuration.seconds % 60)

                    val formattedDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds)


                    // Update the start button text back to "Start"
                    startButton.text = "Start"
                    hoursWorkedTextView.text = formattedDuration

                }

                tableRow.addView(stopButton)

                // Add the tableRow to the TableLayout
                tableLayout.addView(tableRow)
            }
    }

    private fun showEnlargedImage(image: Bitmap?) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_enlarged_image)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val enlargedImageView = dialog.findViewById<ImageView>(R.id.enlargedImageView)
        enlargedImageView.setImageBitmap(image)

        dialog.show()
    }
    fun isTaskWithinFilter(task: Tasks): Boolean {
        return (task.dateCreated >= filterStart || task.dateCreated == filterStart) &&
                (task.dateCreated <= filterEnd || task.dateCreated == filterEnd) &&
                task.timeSheet == SharedData.selectedTimeSheet
    }

    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val scaleWidth = maxWidth.toFloat() / width
        val scaleHeight = maxHeight.toFloat() / height

        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false)
    }

    private fun createHeadingTextView(text: String): TextView {
        val textView = TextView(this)
        textView.text = text
        textView.gravity = Gravity.LEFT
        textView.setPadding(16, 16, 16, 16)
        textView.setTypeface(null, Typeface.BOLD) // Set the heading text to bold
        return textView
    }

    private fun createTextView(text: String): TextView {
        val textView = TextView(this)
        textView.text = text
        textView.setPadding(16, 16, 16, 16)
        return textView
    }

    private fun BackButton_TimesheetViewPage() {
        val click = findViewById<View>(R.id.BackButton_TimesheetViewPage)
        click.setOnClickListener {
            startActivity(Intent(this, TimesheetPage::class.java))
        }
    }
}
