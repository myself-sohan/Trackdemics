package com.example.trackdemics.screens.routine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackdemics.data.AllCourseData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RoutineViewModel : ViewModel() {

    var selectedBranch by mutableStateOf("CSE")
    var selectedSemester by mutableIntStateOf(6)
    var selectedDay by mutableStateOf("Monday")

    val availableBranches = listOf("CSE", "CE", "ECE", "ME", "EEE")
    val availableSemesters = listOf("Sem 2", "Sem 4", "Sem 6", "Sem 8")

    private var allCourses by mutableStateOf<List<AllCourseData.CourseWithSchedule>>(emptyList())

    init {
        fetchCoursesFromFirestore()
    }

    private fun fetchCoursesFromFirestore() {
        viewModelScope.launch {
            try {
                val snapshot = FirebaseFirestore.getInstance()
                    .collection("routine_data")
                    .whereEqualTo("branch", selectedBranch)
                    .whereEqualTo("semester", selectedSemester)
                    .get()
                    .await()

                val fetched = snapshot.documents.flatMap { doc ->
                    val courses = doc["courses"] as? List<Map<String, Any>> ?: emptyList()
                    courses.mapNotNull { map ->
                        parseCourseFromMap(map)
                    }
                }

                allCourses = fetched

            } catch (e: Exception) {
                e.printStackTrace()
                allCourses = emptyList()
            }
        }
    }

    fun getFilteredCourses(): List<AllCourseData.CourseWithSchedule> {
        return allCourses.mapNotNull { course ->
            getTimingForDay(course.schedule, selectedDay)?.let { timing ->
                course to timing
            }
        }.sortedBy { it.second.startTime }
            .map { it.first }
    }

    fun getTimingForDay(
        schedule: AllCourseData.WeeklySchedule,
        day: String
    ): AllCourseData.ClassTiming? = when (day) {
        "Monday" -> schedule.monday
        "Tuesday" -> schedule.tuesday
        "Wednesday" -> schedule.wednesday
        "Thursday" -> schedule.thursday
        "Friday" -> schedule.friday
        else -> null
    }

    fun onBranchChanged(newBranch: String) {
        selectedBranch = newBranch
        fetchCoursesFromFirestore()
    }

    fun onSemesterChanged(newSemester: Int) {
        selectedSemester = newSemester
        fetchCoursesFromFirestore()
    }

    private fun parseCourseFromMap(map: Map<String, Any>): AllCourseData.CourseWithSchedule? {
        val code = map["code"] as? String ?: return null
        val name = map["name"] as? String ?: return null
        val location = map["location"] as? String
        val scheduleMap = map["schedule"] as? Map<String, Map<String, String>> ?: emptyMap()

        val schedule = AllCourseData.WeeklySchedule(
            monday = parseClassTiming(scheduleMap["monday"]),
            tuesday = parseClassTiming(scheduleMap["tuesday"]),
            wednesday = parseClassTiming(scheduleMap["wednesday"]),
            thursday = parseClassTiming(scheduleMap["thursday"]),
            friday = parseClassTiming(scheduleMap["friday"])
        )

        return AllCourseData.CourseWithSchedule(
            code = code,
            name = name,
            location = location,
            schedule = schedule
        )
    }

    private fun parseClassTiming(data: Map<String, String>?): AllCourseData.ClassTiming? {
        return data?.let {
            val start = it["startTime"] ?: return null
            val end = it["endTime"] ?: return null
            AllCourseData.ClassTiming(start, end)
        }
    }
}
