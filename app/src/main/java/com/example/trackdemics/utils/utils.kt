package com.example.trackdemics.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
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



