package com.example.trackdemics.repository

import android.content.Context
import android.os.Environment
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

suspend fun generateExcelFromAttendance(
    context: Context,
    courseCode: String,
    courseName: String,
    firestore: FirebaseFirestore
): File? = withContext(Dispatchers.IO) {
    try {
        val workbook = XSSFWorkbook()

        // Fetch all attendance records for the course
        val snapshot = firestore.collection("attendance_record")
            .whereEqualTo("course_code", courseCode)
            .get()
            .await()

        val sortedDocs = snapshot.documents.sortedBy {
            it.getLong("timestamp") ?: 0L
        }

        for ((index, doc) in sortedDocs.withIndex()) {
            val sessionDate = doc.getString("session_date") ?: continue
            val timestamp = doc.getLong("timestamp") ?: continue
            val sessionTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(timestamp))
            val students = doc["students"] as? List<Map<String, Any>> ?: continue

            // Generate a safe sheet name
            val rawSheetName = "$sessionDate $sessionTime"
            val safeSheetName = rawSheetName
                .replace(Regex("[\\\\/*?:\\[\\]]"), "_") // Replace invalid characters
                .take(31) // Excel sheet name limit

            val sheet = workbook.createSheet(safeSheetName)

            // Header row
            val headerRow = sheet.createRow(0)
            listOf("Roll Number", "Full Name", "Present").forEachIndexed { col, title ->
                headerRow.createCell(col).setCellValue(title)
            }

            // Student rows
            students.forEachIndexed { rowIndex, student ->
                val row = sheet.createRow(rowIndex + 1)
                row.createCell(0).setCellValue(student["rollNumber"]?.toString() ?: "")
                row.createCell(1).setCellValue(student["fullName"]?.toString() ?: "")
                val isPresent = student["present"] as? Boolean ?: false
                row.createCell(2).setCellValue(if (isPresent) "Present" else "Absent")
            }

            // Set column widths
            sheet.setColumnWidth(0, 4000)
            sheet.setColumnWidth(1, 8000)
            sheet.setColumnWidth(2, 3000)
        }

        // Save the workbook to the Downloads directory
        val fileName = "${courseCode}_attendance.xlsx"
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, fileName)
        file.outputStream().use { workbook.write(it) }
        workbook.close()

        file
    } catch (e: Exception) {
        Log.e("Export", "Failed to create Excel", e)
        null
    }
}

