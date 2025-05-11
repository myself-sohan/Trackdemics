package com.example.trackdemics.screens.attendance

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Locale
import javax.inject.Inject

data class AttendanceDayStat(
    val day: String,
    val percentage: Int
)

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    var totalStudents by mutableIntStateOf(0)
        private set

    var totalClasses by mutableIntStateOf(0)
        private set

    fun fetchCourseStats(courseCode: String) {
        // Get total classes from course document
        firestore.collection("courses")
            .document(courseCode)
            .get()
            .addOnSuccessListener { document ->
                totalClasses = document.getLong("classes_taken")?.toInt() ?: 0
            }

        // Count students enrolled in this course
        firestore.collection("students")
            .get()
            .addOnSuccessListener { result ->
                val enrolledCount = result.documents.count { doc ->
                    val courses = doc.get("enrolled_courses") as? List<*>
                    courses?.contains(courseCode) == true
                }
                totalStudents = enrolledCount
            }
    }

    var weeklyAttendanceStats by mutableStateOf<List<AttendanceDayStat>>(emptyList())
        private set

    fun fetchWeeklyAttendance(courseCode: String) {
        val now = LocalDate.now()
        val startOfWeek = now.with(DayOfWeek.MONDAY)

        firestore.collection("attendance_record")
            .whereEqualTo("course_code", courseCode)
            .get()
            .addOnSuccessListener { result ->
                val filtered = result.documents.filter {
                    val sessionDateStr = it.getString("session_date")
                    val sessionDate = LocalDate.parse(sessionDateStr)
                    !sessionDate.isBefore(startOfWeek) && !sessionDate.isAfter(now)
                }

                val dayStats = filtered.mapNotNull { doc ->
                    val sessionDate =
                        LocalDate.parse(doc.getString("session_date"))
                    val students =
                        doc.get("students") as? List<Map<String, Any>> ?: return@mapNotNull null

                    val total = students.size
                    val present = students.count { (it["present"] as? Boolean) == true }

                    val dayName = sessionDate.dayOfWeek.name.take(3).capitalize(Locale.ROOT) // e.g. MON → Mon
                    AttendanceDayStat(
                        day = dayName,
                        percentage = if (total > 0) (present * 100 / total) else 0
                    )
                }.sortedBy { it.day }

                weeklyAttendanceStats = dayStats
            }
    }
}
