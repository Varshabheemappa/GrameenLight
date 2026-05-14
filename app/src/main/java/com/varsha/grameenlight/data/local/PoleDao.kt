package com.varsha.grameenlight.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PoleDao {
    @Query("SELECT * FROM poles")
    fun getAllPoles(): Flow<List<PoleEntity>>

    @Query("SELECT * FROM poles WHERE poleId = :poleId")
    suspend fun getPoleById(poleId: String): PoleEntity?

    @Upsert
    suspend fun upsertPole(pole: PoleEntity)

    @Query("UPDATE poles SET status = :status, lastUpdated = :time WHERE poleId = :poleId")
    suspend fun updateStatus(poleId: String, status: String, time: Long = System.currentTimeMillis())
}