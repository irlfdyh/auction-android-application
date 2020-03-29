package com.production.auctionapplication.repository.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "officer_account_data")
data class OfficerAccountData (

    @PrimaryKey(autoGenerate = false)
    val userId: Int?,
    @ColumnInfo(name = "officer_id")
    val officerId: Int?,
    @ColumnInfo(name = "level_id")
    val levelId: Int?,
    @ColumnInfo(name = "token")
    val token: String?,
    @ColumnInfo(name = "officer_name")
    var officerName: String?,
    @ColumnInfo(name = "status")
    var status: String?

)