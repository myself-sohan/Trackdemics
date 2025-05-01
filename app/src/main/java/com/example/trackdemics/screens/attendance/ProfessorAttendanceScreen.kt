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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackdemics.repository.AppFirestoreService
import com.example.trackdemics.screens.attendance.components.AddCourseCard
import com.example.trackdemics.screens.attendance.components.AddCourseForm
import com.example.trackdemics.screens.attendance.components.ProfessorAttendanceCard
import com.example.trackdemics.screens.attendance.model.Course
import com.example.trackdemics.screens.attendance.model.ProfessorCourse
import com.example.trackdemics.screens.attendance.model.SemesterCourses
import com.example.trackdemics.widgets.TrackdemicsAppBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun ProfessorAttendanceScreen(
    navController: NavController
) {
    val firestoreService = remember { AppFirestoreService }
    val auth = remember { FirebaseAuth.getInstance() }
    val coroutineScope = rememberCoroutineScope()

    val openDialog = remember { mutableStateOf(false) }
    val professorCourses = remember { mutableStateListOf<ProfessorCourse>() }
    val loading = remember { mutableStateOf(true) }

    // Professor UID state
    val professorUid = remember { mutableStateOf<String?>(null) }

    // Fetch UID + courses once
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                auth.currentUser?.email?.let { email ->
                    val professorSnapshot = FirebaseFirestore.getInstance()
                        .collection("professors")
                        .whereEqualTo("email", email)
                        .get()
                        .await()

                    val doc = professorSnapshot.documents.firstOrNull()
                    val uid = doc?.id
                    professorUid.value = uid

                    if (uid != null) {
                        val courses = firestoreService.getProfessorCourses(uid)
                        professorCourses.clear()
                        professorCourses.addAll(courses)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                loading.value = false
            }
        }
    }

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
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
            ) {
                AddCourseCard {
                    openDialog.value = true
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (openDialog.value) {
                AddCourseForm(openDialog = openDialog) { course, semester ->
                    coroutineScope.launch {
                        try {
                            professorUid.value?.let { uid ->
                                firestoreService.addCourseToProfessor(
                                    professorUid = uid,
                                    course = ProfessorCourse(
                                        courseName = course.name,
                                        courseCode = course.code,
                                        semester = semester,
                                    )
                                )
                                // Refresh courses
                                val updatedCourses = firestoreService.getProfessorCourses(uid)
                                professorCourses.clear()
                                professorCourses.addAll(updatedCourses)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            when {
                loading.value -> {
                    CircularProgressIndicator()
                }

                professorCourses.isNotEmpty() -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(professorCourses.size) { index ->
                            ProfessorAttendanceCard(
                                course = professorCourses[index],
                                coroutineScope = coroutineScope,
                                onCourseDeleted = { deletedCode ->
                                    professorCourses.removeAll { it.courseCode == deletedCode }
                                }
                            )
                        }
                    }
                }

                else -> {
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
}

val courseData = listOf(
    SemesterCourses(
        semester = 1,
        branch = "CSE",
        courses = listOf(
            Course("MA 101", "Differential Calculus and Linear Algebra"),
            Course("ME 101", "Engineering Mechanics"),
            Course("CY 101", "Chemistry"),
            Course("CE 101", "Engineering Drawing"),
            Course("EC 101", "Basic Electronics Engineering"),
            Course("HS 101", "English Language Skills"),
            Course("HS 151", "English Language Skills Lab"),
            Course("CY 151", "Chemistry Lab"),
            Course("EC 151", "Basic Electronics Lab")
        )
    ),
    SemesterCourses(
        semester = 2,
        branch = "CSE",
        courses = listOf(
            Course("CS 102", "Introduction to Computing"),
            Course("CS 152", "Computing Lab"),
            Course("MA 102", "Integral Calculus and Complex Variables"),
            Course("EE 101", "Basic Electrical Engineering"),
            Course("PH 101", "Physics"),
            Course("CY 102", "Environmental Science"),
            Course("ME 152", "Workshop Practice"),
            Course("EE 151", "Basic Electrical Lab"),
            Course("PH 151", "Physics Lab")
        )
    ),
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
            Course("CS302", "Software Engineering"),
            Course("CS304", "Compiler Design"),
            Course("CS312", "Computer Graphics"),
            Course("CS314", "Shell Programming"),
            Course("CS316", "Augmented and Virtual Reality"),
            Course("CS318", "Information Theory and Coding"),
            Course("CS320", "Machine Learning"),
            Course("CS322", "Cryptography and Network Security"),
            Course("CS324", "Data Analysis and Visualization"),
            Course("CS326", "Multimedia"),
            Course("CS328", "System Software"),
            Course("CS352", "Software Engineering Lab"),
            Course("CS354", "Compiler Design Lab"),
            Course("CS372", "Introduction to Machine Learning"),
            Course("HS392", "Corporate Communication")
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
    ),
    SemesterCourses(
        semester = 1,
        branch = "CE",
        courses = listOf(
            Course("CE 101", "ENGINEERING DRAWING"),
            Course("MA 101", "Differential Calculus and Linear Algebra"),
            Course("ME 101", "Engineering Mechanics"),
            Course("CY 101", "Chemistry"),
            Course("EC 101", "Basic Electronics Engineering"),
            Course("HS 101", "English Language Skills"),
            Course("HS 151", "English Language Skills Lab"),
            Course("CY 151", "Chemistry Lab"),
            Course("EC 151", "Basic Electronics Lab")
        )
    ),
    SemesterCourses(
        semester = 2,
        branch = "CE",
        courses = listOf(
            Course("MA 102", "Integral Calculus and Complex Variables"),
            Course("EE 101", "Basic Electrical Engineering"),
            Course("PH 101", "Physics"),
            Course("CY 102", "Environmental Science"),
            Course("CS 102", "Introduction to Computing"),
            Course("ME 152", "Workshop Practice"),
            Course("EE 151", "Basic Electrical Lab"),
            Course("PH 151", "Physics Lab"),
            Course("CS 152", "Computing Lab")
        )
    ),
    SemesterCourses(
        semester = 3,
        branch = "CE",
        courses = listOf(
            Course("MA 201", "INTEGRAL TRANSFORMS AND PDES"),
            Course("CE 201", "SOLID MECHANICS"),
            Course("CE 203", "SURVEYING"),
            Course("CE 205", "CIVIL ENGINEERING MATERIALS"),
            Course("ME 291", "SAFETY ENGINEERING"),
            Course("CE 251", "SOLID MECHANICS LAB"),
            Course("CE 253", "SURVEYING LAB"),
            Course("CE 255", "CIVIL ENGINEERING MATERIALS LAB")
        )
    ),
    SemesterCourses(
        semester = 4,
        branch = "CE",
        courses = listOf(
            Course("CE 202", "ENVIRONMENTAL ENGINEERING – I"),
            Course("CE 204", "FLUID MECHANICS"),
            Course("CE 206", "STRUCTURAL ANALYSIS – I"),
            Course("CE 212", "CONCRETE TECHNOLOGY"),
            Course("CE 214", "ADVANCE SURVEYING TECHNIQUES"),
            Course("CE 216", "EARTHQUAKE ENGINEERING"),
            Course("CE 218", "BUILDING MATERIAL AND CONSTRUCTION"),
            Course("CE 220", "ENVIRONMENTAL IMPACT ASSESSMENT"),
            Course("CE 222", "REMOTE SENSING AND GIS"),
            Course("CE 272", "BASIC CIVIL ENGINEERING"),
            Course("CE 252", "ENVIRONMENTAL ENGINEERING – I LAB"),
            Course("CE 254", "FLUID MECHANICS LAB"),
            Course("CE 256", "STRUCTURAL ANALYSIS – I LAB")
        )
    ),
    SemesterCourses(
        semester = 5,
        branch = "CE",
        courses = listOf(
            Course("CE 301", "GEOTECHNICAL ENGINEERING-I"),
            Course("CE 303", "TRANSPORTATION ENGINEERING-I"),
            Course("CE 305", "HYDROLOGY & WATER RESOURCES ENGINEERING"),
            Course("CE 311", "ENVIRONMENTAL ENGINEERING – II"),
            Course("CE 313", "GROUND IMPROVEMENT TECHNIQUE"),
            Course("CE 319", "COMPOSITE MATERIALS AND STRUCTURES"),
            Course("CE 315", "STRUCTURAL ANALYSIS- II"),
            Course("CE 317", "COMPUTATIONAL METHOD IN ENGINEERING"),
            Course("CE 321", "MATRIX METHOD OF STRUCTURAL ANALYSIS"),
            Course("CE 371", "SOLID WASTE MANAGEMENT"),
            Course("CE 351", "GEOTECHNICAL ENGINEERING LAB – I"),
            Course("CE 353", "TRANSPORTATION ENGINEERING LAB – I"),
            Course("CE 355", "HYDROLOGY & WATER RESOURCES ENGINEERING LAB")
        )
    ),
    SemesterCourses(
        semester = 6,
        branch = "CE",
        courses = listOf(
            Course("CE 302", "HYDRAULICS & HYDRAULIC STRUCTURES"),
            Course("CE 304", "GEOTECHNICAL ENGINEERING-II"),
            Course("CE 312", "REINFORCED CONCRETE DESIGN"),
            Course("CE 314", "DESIGN OF FOUNDATION AND RETAINING STRUCTURE"),
            Course("CE 316", "PAVEMENT DESIGN"),
            Course("CE 318", "FLUID DYNAMICS AND FLUID MACHINES"),
            Course("CE 320", "CONTINUUM MECHANICS"),
            Course("CE 322", "BRIDGE ENGINEERING"),
            Course("CE 372", "INTRODUCTION TO FINITE ELEMENT METHOD"),
            Course("HS 392", "CORPORATE COMMUNICATION"),
            Course("CE 352", "HYDRAULICS & HYDRAULIC STRUCTURES LAB"),
            Course("CE 354", "GEOTECHNICAL ENGINEERING-II LAB"),
            Course("CE 382", "MINOR PROJECT")
        )
    ),
    SemesterCourses(
        semester = 7,
        branch = "CE",
        courses = listOf(
            Course("CE 401", "PROJECT – I"),
            Course("CE 411", "TRANSPORTATION ENGINEERING II"),
            Course("CE 413", "MODEL OF AIR AND WATER QUALITY"),
            Course("CE 415", "GROUND WATER HYDROLOGY"),
            Course("CE 417", "DESIGN OF STEEL STRUCTURES"),
            Course("CE 419", "DYNAMICS OF SOIL AND FOUNDATION"),
            Course("CE 421", "RIVER ENGINEERING"),
            Course("CE 471", "ENGINEERING GEOLOGY"),
            Course("CE 491", "DISASTER MANAGEMENT"),
            Course("CE 451", "TRAFFIC ENGINEERING (LAB)"),
            Course("CE 481", "INTERNSHIP")
        )
    ),
    SemesterCourses(
        semester = 8,
        branch = "CE",
        courses = listOf(
            Course("CE 402", "PROJECT – II"),
            Course("CE 412", "PRE STRESSED CONCRETE AND INDUSTRIAL STRUCTURES"),
            Course("CE 414", "INDUSTRIAL POLLUTION PREVENTION"),
            Course("CE 416", "ESTIMATION, COSTING & VALUATION"),
            Course("CE 418", "DYNAMICS OF STRUCTURE"),
            Course("CE 420", "TRAFFIC ENGINEERING"),
            Course("CE 422", "IRRIGATION ENGINEERING"),
            Course("HS 492", "ENTREPRENEURSHIP")
        )
    )
)
