package com.lelestacia.finsem_market.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.lelestacia.finsem_market.data.model.UserDto
import com.lelestacia.finsem_market.domain.repository.ServiceRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

class ServiceRepositoryImpl(
    private val db: FirebaseFirestore
) : ServiceRepository {

    override fun getServices(): Flow<List<UserDto>> = channelFlow {
        val snapshotListener = EventListener<QuerySnapshot> { value, error ->
            error?.printStackTrace()
            if (error != null) {
                close(error)
            } else {
                value?.let { snapshot ->
                    Log.d(TAG, "getServices: ${snapshot.documents}")
                    trySend(snapshot.toObjects(UserDto::class.java))
                }
            }
        }
        db.collection("user")
            .where(Filter.equalTo("role", 1))
            .addSnapshotListener(snapshotListener)
        awaitClose()
    }
}