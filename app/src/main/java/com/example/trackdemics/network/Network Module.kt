package com.example.trackdemics.network

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class FirebaseAuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
)
{

    suspend fun signUpWithEmail(
        email: String,
        password: String,
        role: String,
        firstName: String,
        lastName: String
    ): Result<FirebaseUser?> {

        return try {
            val normalizedEmail = email.trim().lowercase()
            val normalizedRole = role.trim().uppercase()
            Log.d("ROLE_DEBUG", "Received role: $role → Normalized: $normalizedRole")

            val collectionName = when (normalizedRole) {
                "STUDENT" -> "students"
                "PROFESSOR" -> "professors"
                "ADMIN" -> "professors"
                else -> return Result.failure(Exception("Invalid role"))
            }

            val allDocs = firestore.collection(collectionName).get().await()
            var matchingDoc: Pair<String, Map<String, Any>>? = null
            Log.d("ALL_DOCS", "All documents: $allDocs")

            for (doc in allDocs.documents) {
                val docEmail = (doc.getString("email") ?: "").trim().lowercase()
                Log.d("EMAIL_CHECK", "Checking Firestore email: $docEmail against input: $normalizedEmail")

                if (docEmail == normalizedEmail) {
                    val data = doc.data ?: continue
                    matchingDoc = doc.id to data
                    Log.d("EMAIL_MATCH", "Email matched with docId: ${doc.id}")
                    break
                }
            }
            if (matchingDoc == null) {
                Log.d("EMAIL_NOT_FOUND", "No matching email found in $collectionName")
                return Result.failure(Exception("Email not found in $collectionName records."))
            }

            val (docId, data) = matchingDoc
            val isAlreadyRegistered = data["registered"] as? Boolean ?: false

            if (isAlreadyRegistered) {
                return Result.failure(Exception("This email has already been registered. Please log in."))
            }

            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user

            val updateData = mapOf(
                "registered" to true,
                "first_name" to firstName,
                "last_name" to lastName
            )

            firestore.collection(collectionName).document(docId)
                .update(updateData)
                .await()


            Result.success(user)

        } catch (e: Exception) {
            Log.e("SIGNUP_ERROR", "Error during sign up", e)
            Result.failure(e)
        }
    }





    suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser?> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}
