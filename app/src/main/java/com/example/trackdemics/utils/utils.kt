package com.example.trackdemics.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.bumptech.glide.Glide
import com.example.trackdemics.R
import com.example.trackdemics.data.AllCourseData
import com.example.trackdemics.screens.attendance.model.SemesterCourses
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

fun generateQrCodeBitmap(content: String, size: Int = 512): Bitmap {
    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size)
    val bmp = createBitmap(size, size, Bitmap.Config.RGB_565)

    for (x in 0 until size) {
        for (y in 0 until size) {
            bmp[x, y] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
        }
    }
    return bmp
}

fun decodeQrFromBitmap(bitmap: Bitmap): String? {
    val width = bitmap.width
    val height = bitmap.height
    val pixels = IntArray(width * height)
    bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

    val source: LuminanceSource = RGBLuminanceSource(width, height, pixels)
    val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

    return try {
        val result: Result = QRCodeReader().decode(binaryBitmap)
        result.text
    } catch (e: Exception) {
        null
    }
}

fun uploadImageToCloudinary(
    fileUri: Uri,
    context: Context,
    onSuccess: (String) -> Unit,
    onError: (Exception) -> Unit
) {
    val cloudName = "cloud-image-ashad"
    val uploadPreset = "Student PFPs"

    val requestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("upload_preset", uploadPreset)
        .addFormDataPart(
            "file", "profile.jpg",
            context.contentResolver.openInputStream(fileUri)?.use { inputStream ->
                inputStream.readBytes().toRequestBody()
            } ?: throw Exception("File not found")
        )
        .build()

    val request = Request.Builder()
        .url("https://api.cloudinary.com/v1_1/$cloudName/image/upload")
        .post(requestBody)
        .build()

    val client = OkHttpClient()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onError(e)
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseString = response.body?.string()
                val url = JSONObject(responseString).getString("secure_url")
                onSuccess(url)
            } else {
                onError(Exception("Upload failed with code ${response.code}"))
            }
        }
    })
}


@Composable
fun LoadImageWithGlide(
    imageUrl: String,
    modifier: Modifier = Modifier,
    loading: Boolean = false
) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        AndroidView(
            factory = { context ->
                ImageView(context).apply {
                    adjustViewBounds = true
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
            },
            update = { imageView ->
                Glide.with(imageView.context)
                    .load(if (imageUrl.isNotBlank()) imageUrl else R.drawable.img_profile)
                    .override(300, 300)
                    .circleCrop()
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .into(imageView)
            }
        )

        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                strokeWidth = 3.dp
            )
        }
    }
}

fun incrementClassesTaken(courseCode: String) {
    val db = FirebaseFirestore.getInstance()

    db.collection("courses")
        .document(courseCode)
        .update("classes_taken", FieldValue.increment(1))
        .addOnSuccessListener {
            println("Incremented classes_taken for $courseCode")
        }
        .addOnFailureListener {
            println("Failed to increment: ${it.message}")
        }
}


fun populateCoursesToFirestore(courseData: List<SemesterCourses>) {
    val db = FirebaseFirestore.getInstance()
    val coursesCollection = db.collection("courses")

    CoroutineScope(Dispatchers.IO).launch {
        courseData.forEach { semesterCourses ->
            semesterCourses.courses.forEach { course ->
                val courseMap = mapOf(
                    "code" to course.code,
                    "name" to course.name,
                    "branch" to semesterCourses.branch,
                    "semester" to semesterCourses.semester.toString(),
                    "enrolled_students" to emptyList<String>()
                )

                coursesCollection
                    .document(course.code.trim().replace(" ", "")) // custom doc ID (optional)
                    .set(courseMap)
                    .addOnSuccessListener { println("Added ${course.name}") }
                    .addOnFailureListener { e -> println("Failed: ${e.message}") }
            }
        }
    }
}

fun cleanUpCoursesCollection() {
    val db = FirebaseFirestore.getInstance()
    val coursesRef = db.collection("courses")

    coursesRef.get().addOnSuccessListener { querySnapshot ->
        for (doc in querySnapshot.documents) {
            val docRef = doc.reference

            // Prepare field updates
            val updates = mapOf(
                "enrolled_students" to FieldValue.delete(),
                "classes_taken" to 0
            )

            docRef.update(updates)
                .addOnSuccessListener {
                    println("Updated course: ${doc.id}")
                }
                .addOnFailureListener {
                    println("Failed to update ${doc.id}: ${it.message}")
                }
        }
    }.addOnFailureListener {
        println("Failed to fetch courses: ${it.message}")
    }
}

fun uploadCourseScheduleToFirestore() {
    val firestore = FirebaseFirestore.getInstance()

    AllCourseData.allSemesters.forEach { semesterCourses ->
        val courseList = semesterCourses.courses.map { course ->
            mapOf(
                "code" to course.code,
                "name" to course.name,
                "location" to course.location,
                "schedule" to mapOf(
                    "monday" to course.schedule.monday?.let {
                        mapOf("startTime" to it.startTime, "endTime" to it.endTime)
                    },
                    "tuesday" to course.schedule.tuesday?.let {
                        mapOf("startTime" to it.startTime, "endTime" to it.endTime)
                    },
                    "wednesday" to course.schedule.wednesday?.let {
                        mapOf("startTime" to it.startTime, "endTime" to it.endTime)
                    },
                    "thursday" to course.schedule.thursday?.let {
                        mapOf("startTime" to it.startTime, "endTime" to it.endTime)
                    },
                    "friday" to course.schedule.friday?.let {
                        mapOf("startTime" to it.startTime, "endTime" to it.endTime)
                    }
                )
            )
        }

        val data = mapOf(
            "semester" to semesterCourses.semester,
            "branch" to semesterCourses.branch,
            "courses" to courseList
        )

        firestore.collection("routine_data")
            .add(data)
            .addOnSuccessListener {
                println("✅ Uploaded: ${semesterCourses.branch} Sem ${semesterCourses.semester}")
            }
            .addOnFailureListener { e ->
                println("❌ Failed to upload ${semesterCourses.branch} Sem ${semesterCourses.semester}: ${e.localizedMessage}")
            }
    }
}

fun insertDummyStudentsToFirestore(count: Int = 10) {
    val firestore = FirebaseFirestore.getInstance()
    val firstNames = listOf(
        "Aditya", "Sneha", "Harshit", "Lavanya", "Pranav",
        "Tanya", "Yash", "Ira", "Rahul", "Kritika"
    )

    val lastNames = listOf(
        "Chatterjee", "Kumar", "Bhat", "Joshi", "Mukherjee",
        "Kapoor", "Iyer", "Tripathi", "Ghosh", "Kulkarni"
    )

    repeat(count) { index ->
        val firstName = firstNames.random()
        val lastName = lastNames.random()
        val rollSuffix = (150 + index).toString()
        val email = "b22cs$rollSuffix@nitm.ac.in"

        val studentData = mapOf(
            "first_name" to firstName,
            "last_name" to lastName,
            "email" to email,
            "semester" to "Semester 6",
            "registered" to true,
            "profile_pic_url" to "",
            "enrolled_courses" to listOf("CS302", "CS304")
        )

        firestore.collection("students")
            .add(studentData)
            .addOnSuccessListener {
                println("✅ Dummy student $email added")
            }
            .addOnFailureListener { e ->
                println("❌ Failed to add student $email: ${e.localizedMessage}")
            }
    }
}

fun bulkDeleteDummyStudents() {
    val firestore = FirebaseFirestore.getInstance()
    firestore.collection("students")
        .whereGreaterThanOrEqualTo("email", "b22cs40@nitm.ac.in")
        .whereLessThanOrEqualTo("email", "b22cs65@nitm.ac.in")
        .get()
        .addOnSuccessListener { snapshot ->
            val batch = firestore.batch()
            snapshot.documents.forEach { doc ->
                batch.delete(doc.reference)
            }
            batch.commit()
                .addOnSuccessListener {
                    Log.d("Cleanup", "Dummy students deleted.")
                }
                .addOnFailureListener {
                    Log.e("Cleanup", "Error: $it")
                }
        }
}

fun deleteAllAttendanceRecords() {
    val firestore = FirebaseFirestore.getInstance()
    firestore.collection("attendance_record")
        .get()
        .addOnSuccessListener { snapshot ->
            val batch = firestore.batch()
            snapshot.documents.forEach { doc ->
                batch.delete(doc.reference)
            }
            batch.commit()
                .addOnSuccessListener {
                    Log.d("Cleanup", "All attendance records deleted.")
                }
                .addOnFailureListener {
                    Log.e("Cleanup", "Batch delete failed: $it")
                }
        }
        .addOnFailureListener {
            Log.e("Cleanup", "Failed to fetch attendance records: $it")
        }
}

fun resetAllCourseClassCounts() {
    val firestore = FirebaseFirestore.getInstance()
    firestore.collection("courses")
        .get()
        .addOnSuccessListener { snapshot ->
            val batch = firestore.batch()

            snapshot.documents.forEach { doc ->
                val docRef = doc.reference
                batch.update(docRef, "classes_taken", 0)
            }

            batch.commit()
                .addOnSuccessListener {
                    Log.d("CourseReset", "Successfully reset classes_taken to 0 for all courses.")
                }
                .addOnFailureListener { e ->
                    Log.e("CourseReset", "Error during batch reset: ", e)
                }
        }
        .addOnFailureListener { e ->
            Log.e("CourseReset", "Failed to fetch course documents: ", e)
        }
}





