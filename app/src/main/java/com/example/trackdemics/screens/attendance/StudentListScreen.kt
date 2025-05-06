package com.example.trackdemics.screens.attendance

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackdemics.R
import com.example.trackdemics.widgets.TrackdemicsAppBar
import kotlinx.coroutines.delay

@Composable
fun StudentListScreen(
    navController: NavController,
    name: String = "Cryptography and Network Security ",
    code: String = "CS 322",
)
{
    Scaffold(
        topBar = {
            TrackdemicsAppBar(
                navController = navController,
                isEntryScreen = true,
                titleContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                titleTextColor = MaterialTheme.colorScheme.background,
                isActionScreen = true
            )
        }
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Top
        )
        {
            Box(
                modifier = Modifier
                    .weight(0.6f)
                    .background(
                        brush = Brush.Companion.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.inversePrimary
                            )
                        )
                    ),
                contentAlignment = Alignment.Companion.TopCenter
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 6.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                )
                {
                    // Animated entry
                    var visible = remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        delay(300)
                        visible.value = true
                    }

                    AnimatedVisibility(
                        visible = visible.value,
                        enter = slideInHorizontally(animationSpec = tween(1500)) + scaleIn(animationSpec = tween(1000))
                    )
                    {

                        Card(
                            shape = CircleShape,
                            elevation = CardDefaults.cardElevation(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .size(80.dp)
                                )
                        {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.background,
                                                MaterialTheme.colorScheme.onBackground
                                            ),
                                            center = Offset.Unspecified, radius = 150f
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            )
                            {
                                val codeLetters = code.take(2)
                                val codeNumbers = code.drop(2)

                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = MaterialTheme.typography.bodyLarge.toSpanStyle().copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )) {
                                            append("$codeLetters\n")
                                        }
                                        withStyle(style = MaterialTheme.typography.bodyLarge.toSpanStyle().copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            fontFamily = FontFamily(Font(R.font.notosans_variablefont))
                                        )) {
                                            append(codeNumbers)
                                        }
                                    },
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.align(Alignment.Center),
                                    lineHeight = 22.sp,
                                    textAlign = TextAlign.Center
                                )

                            }
                        }
                    }

                    // Course Name
                    AnimatedVisibility(
                        visible = visible.value,
                        enter = slideInHorizontally(animationSpec = tween(1500)){it} + scaleIn(animationSpec = tween(1000))
                    )
                    {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontSize = 20.sp,
                                lineHeight = 24.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            ),
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .fillMaxWidth(0.7f),
                            maxLines = 4
                        )
                    }
                }

            }
            Box(
                modifier = Modifier.weight(3f)
            )
            {

            }
        }
    }
}