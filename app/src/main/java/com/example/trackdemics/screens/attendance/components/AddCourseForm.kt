package com.example.trackdemics.screens.attendance.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackdemics.screens.attendance.courseData
import com.example.trackdemics.screens.attendance.model.Course

// Function to filter based on semester and branch
fun getCoursesForSemester(semester: Int, branch: String): List<Course> {
    return courseData.firstOrNull { it.semester == semester && it.branch == branch }?.courses ?: emptyList()
}

// Composable for the Add Course Dialog
@Composable
fun AddCourseForm(
    openDialog: MutableState<Boolean> = remember { mutableStateOf(false) },
    onAddCourse: (Course, String , String) -> Unit = { _, _, _ -> }
)
{
    var selectedBranch = remember { mutableStateOf("") }
    var selectedSemester = remember { mutableStateOf<Int?>(null) }
    var selectedCourse = remember { mutableStateOf<Course?>(null) }

    var showBranchMenu = remember { mutableStateOf(false) }
    var showSemesterMenu = remember { mutableStateOf(false) }
    var showCourseMenu = remember { mutableStateOf(false) }

    val branches = listOf("CSE")
    val semesters = (3..8).toList()
    val filteredCourses = if (selectedBranch.value.isNotEmpty() && selectedSemester.value != null) {
        getCoursesForSemester(selectedSemester.value!!, selectedBranch.value)
    } else emptyList()

    AlertDialog(
        onDismissRequest = { openDialog.value = false },
        title = { Text("Add Course") },
        text = {
            Column {
                // Branch Dropdown
                Box {
                    OutlinedTextField(
                        value = selectedBranch.value,
                        onValueChange = {
                            selectedBranch.value = it
                        },
                        label = { Text("Branch") },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                modifier = Modifier.Companion
                                    .clickable { showBranchMenu.value = true }
                            )
                        },
                        modifier = Modifier.Companion
                            .fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = showBranchMenu.value,
                        onDismissRequest = { showBranchMenu.value = false }) {
                        branches.forEach { branch ->
                            DropdownMenuItem(
                                text = { Text(branch) },
                                onClick = {
                                    selectedBranch.value = branch
                                    showBranchMenu.value = false
                                    selectedCourse.value = null
                                })
                        }
                    }
                }
                Spacer(modifier = Modifier.Companion.height(8.dp))

                // Semester Dropdown
                Box {
                    OutlinedTextField(
                        value = selectedSemester.value?.let { "Semester $it" } ?: "",
                        onValueChange = {},
                        label = { Text("Semester") },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                modifier = Modifier.Companion
                                    .clickable { showSemesterMenu.value = true }
                            )
                        },
                        modifier = Modifier.Companion.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = showSemesterMenu.value,
                        onDismissRequest = { showSemesterMenu.value = false }) {
                        semesters.forEach { semester ->
                            DropdownMenuItem(
                                text = {
                                    Text("Semester $semester")
                                },
                                onClick = {
                                    selectedSemester.value = semester
                                    showSemesterMenu.value = false
                                    selectedCourse.value = null
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.Companion.height(8.dp))

                // Course Dropdown (only if branch and semester selected)
                if (filteredCourses.isNotEmpty()) {
                    Box {
                        OutlinedTextField(
                            value = selectedCourse.value?.name ?: "",
                            onValueChange = {},
                            label = { Text("Course") },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Dropdown",
                                    modifier = Modifier.Companion
                                        .clickable { showCourseMenu.value = true }
                                )
                            },
                            modifier = Modifier.Companion.fillMaxWidth()
                        )
                        DropdownMenu(
                            expanded = showCourseMenu.value,
                            onDismissRequest = { showCourseMenu.value = false }) {
                            filteredCourses.forEach { course ->
                                DropdownMenuItem(
                                    text = {
                                        Text(course.name)
                                    },
                                    onClick = {
                                        selectedCourse.value = course
                                        showCourseMenu.value = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onAddCourse(selectedCourse.value!!, selectedBranch.value, selectedSemester.value.toString())
                    openDialog.value = false
                },
                enabled = selectedCourse.value != null
            ) {
                Text("Add")
            }
        }
    )
}