package com.lelestacia.finsem_market.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.lelestacia.finsem_market.R
import com.lelestacia.finsem_market.data.local.PreferenceManager
import com.lelestacia.finsem_market.data.model.UserDto
import com.lelestacia.finsem_market.data.model.UserRegistrationDTO
import com.lelestacia.finsem_market.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val preferenceManager: PreferenceManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): String {
        auth.signInWithEmailAndPassword(email, password).await()
        val userModel =
            db.collection("user").document(auth.currentUser?.uid.orEmpty())
                .get()
                .await()
                .toObject(UserDto::class.java)

        preferenceManager.user = userModel

        return context.getString(R.string.info_login_success, userModel?.name.orEmpty())
    }

    override suspend fun register(
        email: String,
        password: String,
        registrationModel: UserRegistrationDTO
    ): String {
        auth.createUserWithEmailAndPassword(email, password).await()
        db.collection("user").document(auth.currentUser?.uid.orEmpty())
            .set(registrationModel)
            .await()

        preferenceManager.user = UserDto(
            name = registrationModel.name,
            role = registrationModel.role,
            email = registrationModel.email,
            githubUrl = registrationModel.githubUrl,
            linkedinURL = registrationModel.linkedinURL,
            whatsappNumber = registrationModel.whatsappNumber
        )

        return context.getString(R.string.info_account_created, registrationModel.name)
    }

    override fun checkUser(): String {
        return context.getString(
            R.string.info_login_success,
            preferenceManager.user?.name ?: throw IllegalArgumentException()
        )
    }

    override fun getUser(): UserDto {
        return preferenceManager.user ?: throw IllegalArgumentException()
    }

    override fun logout() {
        auth.signOut()
        preferenceManager.clearPreferences()
    }

    override suspend fun updateUser(userDto: UserDto): String {
        db.collection("user")
            .document(auth.currentUser?.uid.orEmpty())
            .set(userDto, SetOptions.merge())
            .await()

        preferenceManager.user = userDto

        return context.getString(R.string.info_profile_updated)
    }
}