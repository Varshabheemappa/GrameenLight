package com.varsha.grameenlight.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.varsha.grameenlight.data.local.AppDatabase
import com.varsha.grameenlight.data.local.ReportEntity
import com.varsha.grameenlight.data.repository.PoleRepository
import com.varsha.grameenlight.data.repository.ReportRepository
import kotlinx.coroutines.launch

class PoleViewModel(application: Application) :
    AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val poleRepo = PoleRepository(db.poleDao())
    private val reportRepo = ReportRepository(db.reportDao())

    val poles = poleRepo.poles.asLiveData()
    val reports = reportRepo.reports.asLiveData()
    val daytimeCount = reportRepo.daytimeReportCount.asLiveData()

    init {
        viewModelScope.launch {
            poleRepo.seedDemoData()
        }
        poleRepo.startFirebaseSync()

        poles.observeForever { poleList ->
            android.util.Log.d("POLES", "Poles loaded: ${poleList.size}")
            poleList.forEach {
                android.util.Log.d("POLES", "${it.poleId} → ${it.status}")
            }
        }
    }

    fun submitReport(poleId: String, status: String) {
        viewModelScope.launch {
            val report = ReportEntity(
                poleId = poleId,
                reportedStatus = status
            )
            reportRepo.insertReport(report)
            poleRepo.updatePoleStatus(poleId, status)
        }
    }
}