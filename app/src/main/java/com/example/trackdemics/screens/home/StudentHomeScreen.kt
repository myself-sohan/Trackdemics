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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackdemics.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Sample Data Class for a Course
data class Course(val name: String, val professor: String, val time: String)

// Sample Upcoming Classes Data
val upcomingClasses = listOf(
    Course("Mathematics", "Dr. A Sharma", "10:00 AM"),
    Course("Physics", "Prof. K Verma", "12:00 PM"),
    Course("Computer Science", "Dr. S Gupta", "2:00 PM"),
    Course("Chemistry", "Dr. B Rao", "3:30 PM"),
    Course("English", "Prof. M Kapoor", "4:30 PM"),
    Course("Biology", "Dr. T Mehta", "5:00 PM")
)

@Composable
fun StudentHomeScreen(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.35f) // Limits width to 35% of screen
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                SideNavigationPanel()
            }
        },
        scrimColor = MaterialTheme.colorScheme.primary
    ) {
        Scaffold(
            topBar = { DashboardTopBar(drawerState, coroutineScope) }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                Text(
                    text = "Upcoming Classes",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(upcomingClasses) { course ->
                        CourseCard(course, navController)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(drawerState: DrawerState, coroutineScope: CoroutineScope) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.nitm),
                    contentDescription = "College Logo",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Smart Attendance", fontSize = 20.sp)
            }
        },
        navigationIcon = {
            IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = { /* Handle notifications */ }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications")
            }
        }
    )
}

@Composable
fun SideNavigationPanel() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Navigation",
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(8.dp)
        )
        HorizontalDivider()
        Text(
            "Profile", modifier = Modifier
                .padding(8.dp)
                .clickable { /* Navigate to Profile */ })
        Text(
            "Settings", modifier = Modifier
                .padding(8.dp)
                .clickable { /* Navigate to Settings */ })
        Text(
            "Logout", modifier = Modifier
                .padding(8.dp)
                .clickable { /* Handle Logout */ })
    }
}

@Composable
fun CourseCard(course: Course, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .aspectRatio(1f)
            .clickable { navController.navigate("subjectDetails/${course.name}") },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            SubjectRow(course)
            ProfessorRow(course)
            ClassTimingRow(course)
        }
    }
}

@Composable
fun SubjectRow(
    course: Course
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = course.name,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.background,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 1,
            modifier = Modifier
                .padding(vertical = 16.dp)
        )
    }
}

@Composable
fun ProfessorRow(course: Course) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = course.professor,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.background,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 1,
            modifier = Modifier
                .padding(vertical = 12.dp)

        )
    }
}

@Composable
fun ClassTimingRow(course: Course) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.error),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = course.time,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.background,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 1,
            modifier = Modifier
                .padding(vertical = 12.dp)
        )
    }
}

