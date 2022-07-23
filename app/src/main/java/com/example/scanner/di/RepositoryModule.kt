package com.example.scanner.di

import com.example.scanner.localdatasource.CartDao
import com.example.scanner.repositories.GetCheckoutDetailsRepo
import com.example.scanner.repositories.GetProductDetailsRepo
import com.example.scanner.repositories.LocalCartRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGetProductDetailsRepo(): GetProductDetailsRepo{
        return GetProductDetailsRepo()
    }

    @Provides
    @Singleton
    fun provideLocalCartRepo(
        cartDao: CartDao
    ): LocalCartRepo{
        return LocalCartRepo(cartDao)
    }

    @Provides
    @Singleton
    fun provideGetCheckoutDetailsRepo(
        cartDao: CartDao
    ): GetCheckoutDetailsRepo{
        return GetCheckoutDetailsRepo(cartDao)
    }
}