package com.varsha.grameenlight.data.repository

import com.varsha.grameenlight.data.local.PoleDao
import com.varsha.grameenlight.data.local.PoleEntity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PoleRepository(private val poleDao: PoleDao) {
    private val firestore = Firebase.firestore
    val poles: Flow<List<PoleEntity>> = poleDao.getAllPoles()

    suspend fun seedDemoData() {
        val demoPoles = listOf(
            PoleEntity(
                poleId = "POLE-001",
                latitude = 20.5937,
                longitude = 78.9629,
                status = "WORKING",
                villageName = "Demo Village"
            ),
            PoleEntity(
                poleId = "POLE-002",
                latitude = 20.5942,
                longitude = 78.9638,
                status = "FUSED",
                villageName = "Demo Village"
            ),
            PoleEntity(
                poleId = "POLE-003",
                latitude = 20.5930,
                longitude = 78.9618,
                status = "DAY_ON",
                villageName = "Demo Village"
            ),
            PoleEntity(
                poleId = "POLE-004",
                latitude = 20.5948,
                longitude = 78.9645,
                status = "WORKING",
                villageName = "Demo Village"
            ),
            PoleEntity(
                poleId = "POLE-005",
                latitude = 20.5925,
                longitude = 78.9610,
                status = "WORKING",
                villageName = "Demo Village"
            )
        )
        demoPoles.forEach { poleDao.upsertPole(it) }
    }

    suspend fun updatePoleStatus(
        poleId: String,
        newStatus: String
    ) {
        poleDao.updateStatus(poleId, newStatus)
        try {
            firestore.collection("poles")
                .document(poleId)
                .update(
                    mapOf(
                        "status" to newStatus,
                        "lastUpdated" to System.currentTimeMillis()
                    )
                )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startFirebaseSync() {
        firestore.collection("poles")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null)
                    return@addSnapshotListener
                snapshot.documents.forEach { doc ->
                    val poleId = doc.id
                    val status = doc.getString("status")
                        ?: return@forEach
                    val latitude = doc.getDouble("latitude")
                        ?: return@forEach
                    val longitude = doc.getDouble("longitude")
                        ?: return@forEach
                    val villageName = doc.getString("villageName")
                        ?: "Demo Village"
                    CoroutineScope(Dispatchers.IO).launch {
                        poleDao.upsertPole(
                            PoleEntity(
                                poleId = poleId,
                                latitude = latitude,
                                longitude = longitude,
                                status = status,
                                villageName = villageName
                            )
                        )
                    }
                }
            }
    }
}