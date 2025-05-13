package com.example.trackdemics.screens.transport.components

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HeaderArea(
    navController: NavController,
    routineclick: () -> Unit = {},
) {
    // gradient colors and header shape
    val headerGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.inversePrimary,
        )
    )

    // Use an outer Box so we can draw the pill overlapping the Card (half above, half inside)
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top=25.dp))
    {

        // Main card (the blue gradient background)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        )
        {
            // Inside the card we add top padding so the overlapping pill doesn't push content down
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(headerGradient)
                    .padding(start = 16.dp, end = 16.dp, top = 56.dp, bottom = 16.dp)
            )
            {
                // Left action buttons (Routes & Special Bus)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    // Routes button
                    SmallHeaderButton(
                        label = "Routes",
                        trailing = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "info",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        modifier = Modifier.weight(0.4f)
                    )
                    {
                        routineclick()
                    }

                    Spacer(modifier = Modifier
                        .weight(0.2f)
                    )

                    // Special Bus button
                    SmallHeaderButton(
                        label = "Special Bus",
                        trailing = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                contentDescription = "special",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        modifier = Modifier.weight(0.4f)
                    )
                    {
                        navController.navigate("SpecialBusScreen")
                    }
                }
            }
        }

        // Overlapping pill: placed after the Card so it appears on top. It's centered and offset upwards
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-22).dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color(0xFFF6F9FB))
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.onPrimaryContainer,
                    RoundedCornerShape(50.dp)
                )
                .padding(horizontal = 18.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // bus icon (using android built-in as placeholder)
                Icon(
                    imageVector = Icons.Default.DirectionsBus,
                    contentDescription = "bus",
                    tint = Color.Black,
                    modifier = Modifier.size(30.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Bus Schedule",
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun SmallHeaderButton(
    label: String,
    trailing: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    onclick: () -> Unit = {}
)
{
    Card(
        colors = CardDefaults.cardColors(Color(0xFFF6F9FB)),
        modifier = modifier
            .height(50.dp)
            .clickable{
                onclick.invoke()
            }
            .padding(vertical = 2.dp, horizontal = 2.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        )
        {
            Text(
                text = label,
                fontWeight = FontWeight.W200,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(10.dp))
            if (trailing != null)
                trailing()
        }
    }
}
