package com.example.trackdemics.screens.role

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackdemics.R
import com.example.trackdemics.screens.role.components.RoleButton
import com.example.trackdemics.widgets.LottieFromAssets
import com.example.trackdemics.widgets.TrackdemicsAppBar

@Composable
fun RoleScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val activity = context as? Activity
    Scaffold(
        topBar = {
            TrackdemicsAppBar(
                title = "Trackdemics",
                onBackClick = {
                    activity?.moveTaskToBack(true)
                },
                isEntryScreen = true
            )
        }
    )
    { innerPadding ->
        val gradientBrush = Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.background.copy(1f),
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradientBrush)
                .padding(horizontal = 16.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        )
        {
            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle
            Text(
                text = "Your personalized\neducational app",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily(Font(R.font.lobster_regular)),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 12.dp)
            )

            // Lottie with overlay image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f),
                contentAlignment = Alignment.TopCenter
            )
            {
                LottieFromAssets(
                    assetName = "role_screen.json",
                    modifier = Modifier
                        .fillMaxSize(1f),
                    speed = 1f
                )

                // Adjust this based on exact hand position visually
                Image(
                    painter = painterResource(id = R.drawable.appicon), // Replace with your uploaded image
                    contentDescription = "Overlay icon",
                    modifier = Modifier
                        .size(44.dp) // Slightly enlarged
                        .align(Alignment.TopEnd)
                        .offset(x = (-200).dp, y = 135.dp) // Closer to the raised hand
                )
                // Choose your role section - moved up
                Text(
                    text = "Choose your Role \uD83D\uDC65",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .offset(x = (0).dp, y = 335.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // First row: Student & Professor
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RoleButton(
                    image = painterResource(id = R.drawable.student_icon),
                    label = "Student",
                    onClick = { navController.navigate("SignUpScreen/Professor") }
                )
                RoleButton(
                    image = painterResource(id = R.drawable.professor_icon),
                    label = "Professor",
                    onClick = { navController.navigate("SignUpScreen/Professor") }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Second row: Admin centered
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            )
            {
                RoleButton(
                    image = painterResource(id = R.drawable.admin_icon),
                    label = "Admin",
                    onClick = { navController.navigate("SignUpScreen/Professor") }
                )
            }
        }
    }
}

