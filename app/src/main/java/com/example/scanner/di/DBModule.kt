package com.example.scanner.di

import android.content.Context
import androidx.room.Room
import com.example.scanner.localdatasource.CartDao
import com.example.scanner.localdatasource.CartDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    @Singleton
    fun provideCartDatabase(@ApplicationContext context: Context): CartDatabase{
        return Room.databaseBuilder(
            context,
            CartDatabase::class.java,
            CartDatabase.CART_DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCartDao(cartDatabase: CartDatabase): CartDao{
        return cartDatabase.getCartDao()
    }
}