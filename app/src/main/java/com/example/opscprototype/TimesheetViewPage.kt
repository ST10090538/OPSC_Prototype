package com.example.opscprototype
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
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
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Locale


class TimesheetViewPage : AppCompatActivity() {
    private val taskStartTimeMap: MutableMap<Int, Long> = mutableMapOf()
    private val handler: Handler = Handler()
    private lateinit var runnable: Runnable

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timesheet_view_page)

        val progressIcon = findViewById<ImageView>(R.id.timesheetview_progress_button)
        val profileIcon = findViewById<ImageView>(R.id.timesheetview_profile_button)
        val timesheetIcon = findViewById<ImageView>(R.id.timesheetview_timesheet_button)
        val newTask = findViewById<ImageView>(R.id.timesheetview_newtask_button)
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

        // Get the reference to the TableLayout
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)

        // Get the list of tasks from SharedData
        val tasks = SharedData.lstTasks

        // Check if the tasks list is empty
        if (tasks.isEmpty()) {
            // Create a TextView to display a message
            val messageTextView = TextView(this)
            messageTextView.text = "No tasks found."
            messageTextView.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
            messageTextView.setPadding(16, 16, 16, 16)

            // Add the messageTextView to the TableLayout
            val tableRow = TableRow(this)
            tableRow.addView(messageTextView)
            tableLayout.addView(tableRow)
        } else {
            // Create a TableRow for the heading
            val headingRow = TableRow(this)

            // Create and add the headings
            val taskNumberHeading = createHeadingTextView("Task Number")
            val taskImageHeading = createHeadingTextView("Image")
            val taskNameHeading = createHeadingTextView("Task Name")
            val endDateHeading = createHeadingTextView("Deadline")
            val hoursWorkedHeading = createHeadingTextView("Hours worked")
            val stopwatchHeading = createHeadingTextView("Stopwatch")

            // Add the headings to the headingRow
            headingRow.addView(taskNumberHeading)
            headingRow.addView(taskImageHeading)
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
                taskImage.setOnClickListener {
                    showEnlargedImage(task.imgPicture)
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
                tableRow.addView(taskNumberTextView)
                tableRow.addView(taskImage)
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
                            startButton.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)

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
                stopButton.setOnClickListener(){
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
