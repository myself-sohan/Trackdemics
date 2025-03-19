package com.example.trackdemics.widgets

import androidx.compose.foundation.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController
import com.example.trackdemics.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackdemicsAppBar(
    modifier: Modifier = Modifier,
    title: String = "Trackdemics",
    navController: NavController,
    isEntryScreen: Boolean = true
)
{
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    Font
                )
            )
        },
        navigationIcon = {
            if(isEntryScreen)
            {
                Image(
                    painter = painterResource(R.drawable.nitm),
                    contentDescription = "NIT Logo"
                )
            }
            else
            {
                Surface {  }
            }
        }
    )
}