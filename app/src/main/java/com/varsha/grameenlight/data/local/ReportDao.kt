package com.varsha.grameenlight.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports ORDER BY reportedAt DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Insert
    suspend fun insertReport(report: ReportEntity)

    @Query("SELECT COUNT(*) FROM reports WHERE reportedStatus = 'DAY_ON'")
    fun getDaytimeReportCount(): Flow<Int>
}