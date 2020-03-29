package com.production.auctionapplication.repository

import com.production.auctionapplication.repository.database.OfficerAuth
import com.production.auctionapplication.repository.database.OfficerDatabase
import com.production.auctionapplication.repository.database.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OfficerRepository (private val database: OfficerDatabase) {

    suspend fun saveAccountData(officerData: OfficerAuth) {
        withContext(Dispatchers.IO) {
            database.officerDatabaseDao
                .insertOfficerData(officerData.asDatabaseModel())
        }
    }

}