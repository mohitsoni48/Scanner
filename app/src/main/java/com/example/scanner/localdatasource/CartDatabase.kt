package com.example.scanner.localdatasource

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CartDbModel::class],
    version = 1
)
abstract class CartDatabase: RoomDatabase() {

    abstract fun getCartDao(): CartDao

    companion object{
        const val CART_DB_NAME = "cart_database"
    }
}