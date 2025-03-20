package  com.example.trackdemics.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackdemics.R
import com.example.trackdemics.widgets.TrackdemicsAppBar
import kotlinx.coroutines.launch

@Composable
fun StudentHomeScreen(
    navController: NavController
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(top = 48.dp)
            ) {
                SideNavigationPanel()
            }
        },
        scrimColor = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TrackdemicsAppBar(
                    navController = navController,
                    drawerState = drawerState,
                    isEntryScreen = false
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                ProfileSection()
                FeatureGrid()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(drawerState: DrawerState, coroutineScope: kotlinx.coroutines.CoroutineScope) {
    TopAppBar(
        title = { Text("Trackdemics", fontSize = 20.sp) },
        navigationIcon = {
            IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = { /* Handle notifications */ }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Notifications")
            }
        }
    )
}

@Composable
fun ProfileSection() {
    Box(
        modifier = Modifier
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
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_profile),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .scale(.50f)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary)
            )
            StudentCard()
        }
    }
}

@Composable
fun StudentCard() {
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )

    ) {
        Text(
            "Welcome, Student!",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp
            ),
            modifier = Modifier
                .padding(12.dp)
        )
    }
}

@Composable
fun FeatureGrid() {
    val features = listOf(
        FeatureItem(
            "Attendance",
            Icons.AutoMirrored.Filled.Assignment
        ) { /* Navigate to Attendance */ },
        FeatureItem("Results", Icons.Default.Grade) { /* Navigate to Results */ },
        FeatureItem("College Routine", Icons.Default.Schedule) { /* Navigate to Routine */ },
        FeatureItem("Reminders", Icons.Default.Notifications) { /* Navigate to Reminders */ },
        FeatureItem("Events", Icons.Default.Event) { /* Navigate to Events */ },
        FeatureItem("Settings", Icons.Default.Settings) { /* Navigate to Settings */ }
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(features.size) { index ->
            FeatureCard(features[index])
        }
    }
}

@Composable
fun FeatureCard(feature: FeatureItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .aspectRatio(1f)
            .clickable(onClick = feature.onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                feature.icon,
                contentDescription = feature.label,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                feature.label,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun SideNavigationPanel() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.nitm),
            contentDescription = "College Logo",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimary)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Trackdemics",
            style = MaterialTheme.typography.displaySmall.copy(
                color = MaterialTheme.colorScheme.onPrimary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f))

        Spacer(modifier = Modifier.height(16.dp))

        NavItem(
            "Dashboard",
            Icons.Default.Home
        ) { /* Navigate to Dashboard */ }
        NavItem(
            "Profile",
            Icons.Default.Person
        ) { /* Navigate to Profile */ }
        NavItem(
            "Settings",
            Icons.Default.Settings
        ) { /* Navigate to Settings */ }
        NavItem(
            "Logout",
            Icons.AutoMirrored.Filled.Logout
        ) { /* Handle Logout */ }
    }
}

@Composable
fun NavItem(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = text, tint = MaterialTheme.colorScheme.onPrimary)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 16.sp
        )
    }
}

data class FeatureItem(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val onClick: () -> Unit
)
