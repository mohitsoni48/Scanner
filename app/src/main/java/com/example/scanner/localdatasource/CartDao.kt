package com.example.scanner.localdatasource

import androidx.room.*

@Dao
interface CartDao {

    @Insert
    suspend fun addProductToCart(cartDbModel: CartDbModel): Long

    @Update
    suspend fun updateProductQty(cartDbModel: CartDbModel)

    @Delete
    suspend fun deleteProductFromCart(cartDbModel: CartDbModel): Int

    @Query("SELECT * from cartdbmodel")
    suspend fun getAllProductsInCart(): List<CartDbModel>

    @Query("DELETE from cartdbmodel")
    suspend fun clearCart(): Int

    @Query("SELECT * from cartdbmodel where product_id = :product_id")
    suspend fun getCartDetailOfProduct(product_id: Int): CartDbModel?
}