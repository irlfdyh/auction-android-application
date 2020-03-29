package com.production.auctionapplication.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OfficerDatabaseDao {

    @Insert
    fun insertOfficerData(data: OfficerAccountData)

    @Query("DELETE FROM officer_account_data")
    fun deleteOfficerData()
}