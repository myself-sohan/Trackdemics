package com.example.trackdemics.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.trackdemics.R

@Composable
fun WelcomeText(
    greet: String,
    role:String
)
{
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(0.5f)
        ),
        elevation = CardDefaults.cardElevation(18.dp), // Add shadow effect
        modifier = Modifier
            .padding(top = 5.dp)
            //.wrapContentSize()
            .width(200.dp) // Adjusted size for better spacing
            .height(120.dp)
            //.wrapContentHeight()
            .shadow(12.dp), // Extra shadow depth for a floating look
    )
    {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Transparent
                ),
        )
        {
            Image(
                painter = painterResource(R.drawable.resource_case),
                contentDescription = "holding Container",
                modifier = Modifier.
                    matchParentSize() // Adjust the size to wrap the text
            )
            Text(
                //modifier = Modifier.padding(16.dp),
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color(139,0,255),//MaterialTheme.colorScheme.tertiaryContainer, // Style for "Welcome"
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontWeight = FontWeight.ExtraLight
                        )
                    ) {
                        append("$greet, \n")
                    }

                    withStyle(
                        style = SpanStyle(
                            color = Color(139,0,255),//MaterialTheme.colorScheme.tertiaryContainer, // Style for role
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontFamily = FontFamily(Font(R.font.bodoni_moda)),
                            fontWeight = FontWeight.ExtraBold,
                            fontStyle = FontStyle.Italic
                        )
                    ) {
                        append(role)
                    }
                },
                modifier = Modifier
                    .padding(top=10.dp)
                    .align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
    }
}