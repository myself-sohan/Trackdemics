// RoutineViewModel.kt
package com.example.trackdemics.screens.routine

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.trackdemics.data.AllCourseData

class RoutineViewModel : ViewModel() {

    var selectedBranch by mutableStateOf("CSE")
    var selectedSemester by mutableStateOf(6)
    var selectedDay by mutableStateOf("Monday")

    val availableBranches = listOf("CSE", "ECE", "ME", "CE", "EEE")
    val availableSemesters = (1..8).toList()

    /**
     * 1. Find the matching SemesterCourses
     * 2. Filter for selectedDay
     * 3. Sort by startTime ascending
     */
    fun getFilteredCourses(): List<AllCourseData.CourseWithSchedule> {
        val semesterCourses = AllCourseData.allSemesters.find {
            it.branch == selectedBranch && it.semester == selectedSemester
        } ?: return emptyList()

        return semesterCourses.courses
            .mapNotNull { course ->
                // extract timing for this course on selectedDay
                getTimingForDay(course.schedule, selectedDay)?.let { timing ->
                    course to timing
                }
            }
            .sortedBy { it.second.startTime }
            .map { it.first }
    }

    fun getTimingForDay(
        schedule: AllCourseData.WeeklySchedule,
        day: String
    ): AllCourseData.ClassTiming? = when (day) {
        "Monday"   -> schedule.monday
        "Tuesday"  -> schedule.tuesday
        "Wednesday"-> schedule.wednesday
        "Thursday" -> schedule.thursday
        "Friday"   -> schedule.friday
        else       -> null
    }
}
