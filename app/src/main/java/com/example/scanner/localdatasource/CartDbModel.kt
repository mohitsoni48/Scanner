package com.example.scanner.localdatasource

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CartDbModel(
    @PrimaryKey(autoGenerate = false) val product_id: Int,
    @ColumnInfo(name = "qty") var qty: Int
)
