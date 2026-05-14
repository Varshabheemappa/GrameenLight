package com.varsha.grameenlight.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey
    val complaintId: String = "CMP-${UUID.randomUUID().toString().take(8).uppercase()}",
    val poleId: String,
    val reportedStatus: String,
    val reportedAt: Long = System.currentTimeMillis(),
    val repairStatus: String = "PENDING"
)