import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
    roleInitialScale: Float = 0.5f,    // new parameter: scale to start from (e.g., 0.8 = 80% size)
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

    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {

        Text(
            text = greetChars.joinToString(""),
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.background,
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
                    color = MaterialTheme.colorScheme.background,
                    fontWeight = FontWeight.SemiBold
                ),
                fontFamily = FontFamily(Font(R.font.notosans_variablefont)),
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
