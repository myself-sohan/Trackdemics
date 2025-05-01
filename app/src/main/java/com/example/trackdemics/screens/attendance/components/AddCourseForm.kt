package com.example.trackdemics.screens.attendance.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import com.example.trackdemics.R
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.trackdemics.screens.attendance.courseData
import com.example.trackdemics.screens.attendance.model.Course

// Function to filter based on semester and branch
fun getCoursesForSemester(semester: Int, branch: String): List<Course> {
    return courseData.firstOrNull { it.semester == semester && it.branch == branch }?.courses ?: emptyList()
}

// Composable for the Add Course Dialog
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCourseForm(
    openDialog: MutableState<Boolean> = remember { mutableStateOf(false) },
    isStudent: Boolean = false,
    onAddCourse: (Course, String ) -> Unit = { _, _ -> }
)
{
    var selectedBranch = remember { mutableStateOf("") }
    var selectedSemester = remember {
        if(isStudent)
            mutableStateOf<Int?>(6)
        else
        mutableStateOf<Int?>(null)
    }
    var selectedCourse = remember { mutableStateOf<Course?>(null) }

    var showBranchMenu = remember { mutableStateOf(false) }
    var showSemesterMenu = remember { mutableStateOf(false) }
    var showCourseMenu = remember { mutableStateOf(false) }

    val branches = listOf("CSE","ECE","ME","CE","EEE","Physics","Mathematics")
    val semesters = (1..8).toList()
    val filteredCourses = if (selectedBranch.value.isNotEmpty() && selectedSemester.value != null) {
        getCoursesForSemester(selectedSemester.value!!, selectedBranch.value)
    } else emptyList()

    BasicAlertDialog(
        onDismissRequest = { openDialog.value = false }
    )
    {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                // Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add Course",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Branch Dropdown
                Box {
                    OutlinedTextField(
                        value = selectedBranch.value,
                        onValueChange = {},
                        label = {
                            Text(
                                text = "🔁 Branch",
                                fontWeight = FontWeight.Bold
                            )
                        },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                modifier = Modifier.clickable { showBranchMenu.value = true }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = showBranchMenu.value,
                        onDismissRequest = { showBranchMenu.value = false
                        },
                        offset = DpOffset(49.dp, 8.dp),
                        properties = PopupProperties(focusable = false),
                        modifier = Modifier
                            .fillMaxWidth(0.35f)
                            .fillMaxHeight(0.32f)
                    )
                    {
                        branches.forEach { branch ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.CenterVertically
                                    )
                                    {
                                        Text(
                                            text = "📚 $branch",
                                            fontFamily = FontFamily(Font(R.font.notosans_variablefont)),
                                        )
                                    }
                                },
                                onClick = {
                                    selectedBranch.value = branch
                                    showBranchMenu.value = false
                                    selectedCourse.value = null
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Semester Dropdown
                Box {
                    OutlinedTextField(
                        value = selectedSemester.value?.let { "Semester $it" } ?: "",
                        onValueChange = {},
                        label = {
                            Text(
                                text = "🎓 Semester",
                                fontWeight = FontWeight.Bold
                            )
                        },
                        readOnly = true,
                        enabled = !isStudent,
                        trailingIcon = {
                            if (!isStudent) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Dropdown",
                                    modifier = Modifier.clickable {
                                        showSemesterMenu.value = true
                                    }
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = showSemesterMenu.value,
                        offset = DpOffset(49.dp, 8.dp),
                        onDismissRequest = { showSemesterMenu.value = false },
                        properties = PopupProperties(focusable = false),
                        modifier = Modifier
                            .fillMaxWidth(0.35f)
                            .fillMaxHeight(0.32f)
                    )
                    {
                        semesters.forEach { semester ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.CenterVertically
                                    )
                                    {
                                        Text(
                                            text = "🎯 Semester $semester",
                                            fontFamily = FontFamily(Font(R.font.notosans_variablefont)),
                                        )
                                    }
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

                Spacer(modifier = Modifier.height(12.dp))

                // Course Dropdown
                AnimatedVisibility(visible = filteredCourses.isNotEmpty()) {
                    Box {
                        OutlinedTextField(
                            value = selectedCourse.value?.name ?: "",
                            onValueChange = {},
                            label = {
                                Text(
                                    text = "📘 Course",
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Dropdown",
                                    modifier = Modifier.clickable {
                                        showCourseMenu.value = true
                                    }
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        DropdownMenu(
                            expanded = showCourseMenu.value,
                            onDismissRequest = { showCourseMenu.value = false },
                            offset = DpOffset(17.dp, 8.dp),
                            properties = PopupProperties(focusable = false),
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .fillMaxHeight(0.32f)
                        ) {
                            filteredCourses.forEach { course ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Start,
                                            verticalAlignment = Alignment.CenterVertically
                                        )
                                        {
                                            Text(
                                                text = "✅ ${course.name}",
                                                fontFamily = FontFamily(Font(R.font.notosans_variablefont)),
                                            )
                                        }
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

                Spacer(modifier = Modifier.height(24.dp))

                // Confirm Button
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .animateContentSize() // Smooth transition if size/content changes
                )
                {
                    Button(
                        onClick = {
                            onAddCourse(selectedCourse.value!!, selectedSemester.value.toString())
                            openDialog.value = false
                        },
                        enabled = selectedCourse.value != null,
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 12.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary, // Uses your app theme
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier
                            .height(52.dp)
                            .widthIn(min = 160.dp)
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Add",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Add Course",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

            }
        }
    }


}