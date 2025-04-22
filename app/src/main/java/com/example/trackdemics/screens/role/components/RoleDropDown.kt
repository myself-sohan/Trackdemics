package com.example.trackdemics.screens.role.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Stream
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.example.trackdemics.R

@Composable
fun RoleDropDown(
    navController: NavController,
    expanded: MutableState<Boolean>,
    selectedRole: MutableState<String>
)
{
    Box(
        modifier = Modifier
            .width(250.dp)
            .background(
                MaterialTheme.colorScheme.secondary,
                shape = MaterialTheme.shapes.medium
            )
            .clickable { expanded.value = true }
            .padding(16.dp)
    )
    {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        )
        {
            Text(
                text = selectedRole.value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }

    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        properties = PopupProperties(focusable = true), // Ensures dropdown behaves like a menu
        offset = DpOffset(x = 120.dp, y = (220).dp), // Moves dropdown right below the button
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Red.copy(0.5f),
                        Color.White,
                        Color.Yellow.copy(0.5f),
                        Color.White,
                        Color.Green.copy(0.5f)
                    )
                )
            )
            .fillMaxWidth(0.4f)
    )
    {
        val roles = listOf("Admin", "Professor", "Student")
        roles.forEach { role ->
            DropdownMenuItem(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Stream,
                        contentDescription = null,
                        tint = Color.Black
                    )
                },
                text = {
                    Text(
                        text = role,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(
                                Font(R.font.abril_fatface_regular)
                            )     ,
                            color = Color(0,0,128)
                        ),
                    )
                },
                onClick = {
                    selectedRole.value = role
                    expanded.value = false
                    when (role) {
                        "Professor" -> navController.navigate("SignUpScreen/${role.uppercase()}")
                        "Student" -> navController.navigate("SignUpScreen/${role.uppercase()}")
                        "Admin" -> navController.navigate("LoginScreen/${role.uppercase()}")
                    }
                }
            )
        }
    }
}