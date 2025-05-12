import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.trackdemics.R
import kotlinx.coroutines.delay

@Composable
fun WelcomeText(
    greet: String,
    role: String,
    greetSpeedMillis: Long = 110L,
    roleAnimationDuration: Int = 500, // in milliseconds
    roleInitialScale: Float = 0.5f    // new parameter: scale to start from (e.g., 0.8 = 80% size)
) {
    val greetChars = remember { mutableStateListOf<Char>() }
    var showRole by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        for (char in greet) {
            greetChars.add(char)
            delay(greetSpeedMillis)
        }
        showRole = true // Show role text only after greeting finishes typing
    }

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

        Text(
            text = greetChars.joinToString(""),
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )

        AnimatedVisibility(
            visible = showRole,
            enter = fadeIn() + scaleIn(
                initialScale = roleInitialScale,
                animationSpec = tween(durationMillis = roleAnimationDuration)
            )
        ) {
            Text(
                text = role.lowercase().replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }

    Card(
        modifier = Modifier
            .size(150.dp)
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 50.dp
        )
    )
    {
        Image(
            painter = painterResource(id = R.drawable.appicon),
            contentDescription = "Add",
            modifier = Modifier.fillMaxSize()
        )
    }
}
