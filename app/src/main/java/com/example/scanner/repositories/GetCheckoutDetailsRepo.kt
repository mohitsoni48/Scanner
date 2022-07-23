package com.example.scanner.repositories

import com.example.scanner.localdatasource.CartDao
import com.example.scanner.models.CheckoutModel
import com.example.scanner.models.DataState
import com.example.scanner.models.FakeProductList
import com.example.scanner.models.ProductModel
import kotlinx.coroutines.flow.flow

class GetCheckoutDetailsRepo(
    private val cartDao: CartDao
) {
    fun getCheckoutDetailsFromCart() = flow {
        kotlinx.coroutines.delay(1000)  //mimic api
        val allItems = cartDao.getAllProductsInCart().filter { it.qty != 0 }
        val productsList = allItems.map { cartDbModel ->
            val product = FakeProductList.productsList.first { cartDbModel.product_id == it.id }
            product.qty = cartDbModel.qty
            product
        } //should get this list from backend

        emit(
            DataState.Success(
                CheckoutModel(
                    productsList.sumOf { it.productPrice.times(it.qty) },
                    productsList
                )
            )
        )
    }

    fun checkOut() = flow {
        val allItems = cartDao.getAllProductsInCart().filter { it.qty != 0 }
        kotlinx.coroutines.delay(1000)  //mimic api
        cartDao.clearCart()
        emit(DataState.Success(true))
    }
}