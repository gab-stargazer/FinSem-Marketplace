package com.lelestacia.finsem_market.data.repository

import com.google.firebase.firestore.EventListener
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
            if (error != null) {
                close(error)
            } else {
                value?.let { snapshot ->
                    val services = snapshot.toObjects(UserDto::class.java)
                    val filtered = services.filter { it.role == 1 }
                    trySend(filtered)
                }
            }
        }
        db.collection("user")
            .addSnapshotListener(snapshotListener)
        awaitClose()
    }
}