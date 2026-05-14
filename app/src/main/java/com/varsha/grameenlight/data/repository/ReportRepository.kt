package com.varsha.grameenlight.data.repository

import com.varsha.grameenlight.data.local.ReportDao
import com.varsha.grameenlight.data.local.ReportEntity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow

class ReportRepository(private val reportDao: ReportDao) {
    private val firestore = Firebase.firestore
    val reports: Flow<List<ReportEntity>> = reportDao.getAllReports()
    val daytimeReportCount: Flow<Int> =
        reportDao.getDaytimeReportCount()

    suspend fun insertReport(report: ReportEntity) {
        reportDao.insertReport(report)
        firestore.collection("reports")
            .document(report.complaintId)
            .set(
                mapOf(
                    "complaintId" to report.complaintId,
                    "poleId" to report.poleId,
                    "reportedStatus" to report.reportedStatus,
                    "reportedAt" to report.reportedAt,
                    "repairStatus" to report.repairStatus
                )
            )
    }
}