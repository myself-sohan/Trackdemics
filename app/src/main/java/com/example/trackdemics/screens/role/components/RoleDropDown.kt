package com.example.trackdemics.screens.role.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController

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
        Text(
            text = selectedRole.value,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.inverseOnSurface
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }

    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        properties = PopupProperties(focusable = true), // Ensures dropdown behaves like a menu
        offset = DpOffset(x = 120.dp, y = (220).dp), // Moves dropdown right below the button
        modifier = Modifier
            .background(MaterialTheme.colorScheme.onTertiaryContainer)
            .fillMaxWidth(0.4f)
    )
    {
        val roles = listOf("Professor", "Student", "Admin")
        roles.forEach { role ->
            DropdownMenuItem(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = {
                    Text(
                        text = role,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.surfaceBright
                        ),
                    )
                },
                onClick = {
                    selectedRole.value = role
                    expanded.value = false
                    when (role) {
                        "Professor" -> navController.navigate("SignUpScreen")
                        "Student" -> navController.navigate("SignUpScreen")
                        "Admin" -> navController.navigate("LoginScreen")
                    }
                }
            )
        }
    }
}