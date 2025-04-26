package com.example.trackdemics.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.example.trackdemics.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

fun uploadImageToCloudinary(fileUri: Uri, context: Context, onSuccess: (String) -> Unit, onError: (Exception) -> Unit) {
    val cloudName = "cloud-image-ashad"
    val uploadPreset = "Student PFPs"

    val requestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("upload_preset", uploadPreset)
        .addFormDataPart("file", "profile.jpg",
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


