package com.example.trackdemics.repository

import android.util.Log
import com.example.trackdemics.screens.attendance.model.ProfessorCourse
import com.example.trackdemics.screens.attendance.model.StudentCourse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object AppFirestoreService {

    private val firestore = FirebaseFirestore.getInstance()
    private val professorsCollection = firestore.collection("professors")
    private val coursesCollection = firestore.collection("courses")

    suspend fun addCourseToProfessor(professorUid: String, course: ProfessorCourse) {
        // 1. Update professor's handled_courses array
        val professorRef = professorsCollection.document(professorUid)
        professorRef.update(
            "handled_courses",
            com.google.firebase.firestore.FieldValue.arrayUnion(course.courseCode)
        ).await()

        // 2. Add course document if it doesn't exist
        val courseQuery = coursesCollection
            .whereEqualTo("code", course.courseCode)
            .get()
            .await()

        if (courseQuery.isEmpty) {
            coursesCollection.add(
                mapOf(
                    "name" to course.courseName,
                    "code" to course.courseCode,
                    //"branch" to course.branch,
                    "semester" to course.semester,
                    "enrolled_students" to emptyList<String>()
                )
            ).await()
        }
    }

    suspend fun getProfessorCourses(professorUid: String): List<ProfessorCourse> {
        val professorSnap = professorsCollection.document(professorUid).get().await()

        val courseCodes =
            professorSnap.get("handled_courses") as? List<String> ?: return emptyList()

        if (courseCodes.isEmpty() || courseCodes.size > 10) {
            Log.w("FirestoreService", "Handled courseCodes list is invalid: $courseCodes")
            return emptyList()
        }

        val coursesSnapshot = coursesCollection
            .whereIn("code", courseCodes)
            .get()
            .await()

        Log.d("FirestoreService", "Course codes: $courseCodes")

        return coursesSnapshot.documents.mapNotNull { doc ->
            val name = doc.getString("name")
            val code = doc.getString("code")
            val branch = doc.getString("branch")
            val semester = doc.getString("semester")

            if (name != null && code != null && branch != null && semester != null) {
                ProfessorCourse(
                    courseName = name,
                    courseCode = code,
                    semester = semester
                )
            } else null
        }
    }

    suspend fun loadStudentCourses(courses: MutableList<StudentCourse>) {
        courses.clear()

        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()

        auth.currentUser?.email?.let { email ->
            val studentSnapshot = firestore.collection("students")
                .whereEqualTo("email", email.trim().lowercase())
                .get()
                .await()

            val studentDoc = studentSnapshot.documents.firstOrNull()
            val enrolledCourses =
                studentDoc?.get("enrolled_courses") as? List<String> ?: emptyList()

            if (enrolledCourses.isNotEmpty()) {
                val coursesSnapshot = firestore.collection("courses")
                    .whereIn("code", enrolledCourses)
                    .get()
                    .await()

                for (doc in coursesSnapshot.documents) {
                    val name = doc.getString("name") ?: continue
                    val code = doc.getString("code") ?: continue
                    val branch = doc.getString("branch") ?: ""
                    val semester = doc.get("semester")?.toString() ?: ""
                    courses.add(
                        StudentCourse(
                            name = name,
                            code = code,
                            semester = semester,
                            branch = branch,
                            classesTaken = 0
                        )
                    )
                }
            }
        }
    }

    suspend fun removeCourseFromStudent(courseCode: String): Boolean {
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()

        val currentUserEmail = auth.currentUser?.email?.trim()?.lowercase() ?: return false

        val studentSnapshot = firestore.collection("students")
            .whereEqualTo("email", currentUserEmail)
            .get()
            .await()

        val studentDoc = studentSnapshot.documents.firstOrNull() ?: return false

        studentDoc.reference.update(
            "enrolled_courses",
            com.google.firebase.firestore.FieldValue.arrayRemove(courseCode)
        ).await()

        return true
    }

    suspend fun removeCourseFromProfessor(courseCode: String): Boolean {
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()

        val currentUserEmail = auth.currentUser?.email?.trim()?.lowercase() ?: return false

        val professorSnapshot = firestore.collection("professors")
            .whereEqualTo("email", currentUserEmail)
            .get()
            .await()

        val professorDoc = professorSnapshot.documents.firstOrNull() ?: return false

        professorDoc.reference.update(
            "handled_courses",
            com.google.firebase.firestore.FieldValue.arrayRemove(courseCode)
        ).await()

        return true
    }
}

