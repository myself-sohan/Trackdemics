package com.example.trackdemics.screens.home.components

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackdemics.utils.LoadImageWithGlide
import com.example.trackdemics.utils.uploadImageToCloudinary
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun ProfileSection(
    collection: String,
    modifier: Modifier = Modifier,
    label: String
) {
    val auth = remember { FirebaseAuth.getInstance() }
    val firestore = remember { FirebaseFirestore.getInstance() }
    val coroutineScope = rememberCoroutineScope()
    var profileImageUrl by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                coroutineScope.launch {
                    try {
                        uploadImageToCloudinary(
                            it, context,
                            onSuccess = { url ->
                                coroutineScope.launch {
                                    val userEmail = auth.currentUser?.email?.trim()?.lowercase()
                                    if (userEmail != null) {
                                        val snapshot = firestore.collection(collection)
                                            .whereEqualTo("email", userEmail)
                                            .get()
                                            .await()

                                        val doc = snapshot.documents.firstOrNull()
                                        doc?.reference?.update("profile_pic_url", url)?.await()
                                    }
                                }
                                profileImageUrl = url
                            },
                            onError = { error ->
                                Toast.makeText(
                                    context,
                                    "Upload failed: ${error.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    LaunchedEffect(Unit) {
        auth.currentUser?.email?.let { email ->
            val normalizedEmail = email.trim().lowercase()
            val snapshot = firestore.collection(collection)
                .whereEqualTo("email", normalizedEmail)
                .get()
                .await()

            val doc = snapshot.documents.firstOrNull()
            profileImageUrl = doc?.getString("profile_pic_url")
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.inversePrimary
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LoadImageWithGlide(
                imageUrl = profileImageUrl ?: "",
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 16.dp)
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary, CircleShape)
                    .clickable {
                        launcher.launch("image/*")
                    }
                    .border(
                        3.dp,
                        MaterialTheme.colorScheme.primary,
                        CircleShape
                    )
            )
            WelcomeCard(
                label = label
            )
        }
    }
}

@Composable
fun WelcomeCard(
    modifier: Modifier = Modifier,
    label: String
) {
    Card(
        modifier = modifier
            .height(90.dp)
            .padding(bottom = 32.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                ),
                modifier = Modifier.padding(12.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}