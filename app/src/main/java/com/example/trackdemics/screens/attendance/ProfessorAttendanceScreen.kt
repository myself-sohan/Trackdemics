package com.example.trackdemics.screens.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackdemics.screens.attendance.components.AddCourseCard
import com.example.trackdemics.screens.attendance.components.AddCourseForm
import com.example.trackdemics.screens.attendance.components.ProfessorAttendanceCard
import com.example.trackdemics.screens.attendance.model.Course
import com.example.trackdemics.screens.attendance.model.ProfessorCourse
import com.example.trackdemics.screens.attendance.model.SemesterCourses
import com.example.trackdemics.widgets.TrackdemicsAppBar

@Composable
fun ProfessorAttendanceScreen(
    navController: NavController
)
{
    val openDialog = remember { mutableStateOf(false) }
    // ✅ Maintain dynamic list of courses
    val courses = remember { mutableStateListOf<ProfessorCourse>() }
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
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.inversePrimary,
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            )
            {
                AddCourseCard {
                    openDialog.value = true
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if(openDialog.value)
                AddCourseForm(
                    openDialog = openDialog
                )
                {course, semester, branch ->
                    courses.add(
                        ProfessorCourse(
                            courseName = course.name,
                            courseCode = course.code,
                            branch = branch,
                            semester = semester,
                        )
                    )
                }
            if (courses.isNotEmpty())
            {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(courses.size) { index ->
                        ProfessorAttendanceCard(
                            course = courses[index],
                            navController = navController
                        )
                    }
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxHeight(0.1f)
                        .fillMaxWidth(0.5f),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No Courses Found",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

// Create the complete course list
val courseData = listOf(
    SemesterCourses(
        semester = 3,
        branch = "CSE",
        courses = listOf(
            Course("CS 201", "Data Structures"),
            Course("CS 203", "Digital Logic Design"),
            Course("CS 205", "Discrete Mathematical Structure"),
            Course("CS 251", "Data Structure Lab"),
            Course("CS 253", "Digital Logic Design Lab"),
            Course("CS 255", "Internet Web Technology Lab"),
            Course("MA 201", "Integral Transforms and PDEs"),
            Course("ME 291", "Safety Engineering")
        )
    ),
    SemesterCourses(
        semester = 4,
        branch = "CSE",
        courses = listOf(
            Course("CS 202", "Computer Organization"),
            Course("CS 204", "Object Oriented Programming and Design"),
            Course("CS 206", "Data Communication"),
            Course("CS 212", "Analysis and Design of Algorithms"),
            Course("CS 214", "Computational Models for Real Time Systems"),
            Course("CS 216", "Cyber Physical Systems"),
            Course("CS 218", "Computer Arithmetic"),
            Course("CS 220", "Principles of Programming Languages"),
            Course("CS 222", "Programming in Java"),
            Course("CS 224", "GUI Design and Programming"),
            Course("CS 226", "Python Programming"),
            Course("CS 252", "Computer Organization Lab"),
            Course("CS 254", "Object Oriented Programming and Design Lab"),
            Course("CS 256", "Data Communication Lab"),
            Course("CS 272", "Object Oriented Programming")
        )
    ),
    SemesterCourses(
        semester = 5,
        branch = "CSE",
        courses = listOf(
            Course("CS 301", "Operating Systems"),
            Course("CS 303", "Database Management Systems"),
            Course("CS 305", "Computer Networks"),
            Course("CS 311", "Microprocessor and Interfacing"),
            Course("CS 313", "Embedded Systems"),
            Course("CS 315", "E-commerce and Cyber Laws"),
            Course("CS 317", "Machine Vision"),
            Course("CS 319", "Automata and Formal Language"),
            Course("CS 321", "Formal Verification"),
            Course("CS 323", "Computational Geometry"),
            Course("CS 325", "Modern Digital Arithmetic"),
            Course("CS 351", "Operating Systems Lab"),
            Course("CS 353", "Database Management Systems Lab"),
            Course("CS 355", "Computer Networks Lab"),
            Course("CS 371", "Database System Concepts")
        )
    ),
    SemesterCourses(
        semester = 6,
        branch = "CSE",
        courses = listOf(
            Course("CS 302", "Software Engineering"),
            Course("CS 304", "Compiler Design"),
            Course("CS 312", "Computer Graphics"),
            Course("CS 314", "Shell Programming"),
            Course("CS 316", "Augmented and Virtual Reality"),
            Course("CS 318", "Information Theory and Coding"),
            Course("CS 320", "Machine Learning"),
            Course("CS 322", "Cryptography and Network Security"),
            Course("CS 324", "Data Analysis and Visualization"),
            Course("CS 326", "Multimedia"),
            Course("CS 328", "System Software"),
            Course("CS 352", "Software Engineering Lab"),
            Course("CS 354", "Compiler Design Lab"),
            Course("CS 372", "Introduction to Machine Learning"),
            Course("HS 392", "Corporate Communication")
        )
    ),
    SemesterCourses(
        semester = 7,
        branch = "CSE",
        courses = listOf(
            Course("CE 491", "Disaster Management"),
            Course("CS 411", "Soft Computing"),
            Course("CS 413", "Pattern Recognition"),
            Course("CS 415", "Complex Networks"),
            Course("CS 417", "Blockchain Technologies"),
            Course("CS 419", "High Performance Architecture"),
            Course("CS 421", "Image Processing"),
            Course("CS 423", "Artificial Intelligence"),
            Course("CS 425", "Advanced Web Technology"),
            Course("CS 427", "Software Defined Network"),
            Course("CS 429", "Robotics and Automation"),
            Course("CS 461", "Computational Intelligence Lab"),
            Course("CS 471", "Data Analytics using Python")
        )
    ),
    SemesterCourses(
        semester = 8,
        branch = "CSE",
        courses = listOf(
            Course("CS 412", "Mobile Computing"),
            Course("CS 414", "Cloud Computing"),
            Course("CS 416", "Wireless Sensor Network"),
            Course("CS 418", "Natural Language Processing"),
            Course("CS 420", "Cyber Forensics and Analysis"),
            Course("CS 422", "Data Mining"),
            Course("CS 424", "Distributed Computing"),
            Course("CS 426", "Bioinformatics"),
            Course("CS 428", "Internet of Things"),
            Course("CS 430", "Human Computer Interaction"),
            Course("HS 492", "Entrepreneurship")
        )
    )
)
