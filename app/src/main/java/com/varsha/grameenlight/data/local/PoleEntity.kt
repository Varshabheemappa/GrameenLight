package com.varsha.grameenlight.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poles")
data class PoleEntity(
    @PrimaryKey val poleId: String,
    val latitude: Double,
    val longitude: Double,
    val status: String,
    val villageName: String = "Demo Village",
    val lastUpdated: Long = System.currentTimeMillis()
)