package com.example.scanner.repositories

import com.example.scanner.localdatasource.CartDao
import com.example.scanner.localdatasource.CartDbModel
import com.example.scanner.models.DataState
import com.example.scanner.models.ProductModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocalCartRepo(
    private val cartDao: CartDao
) {

    fun getAllCartItems(): Flow<DataState<List<CartDbModel>>> = flow {
        val allItems = cartDao.getAllProductsInCart().filter { it.qty !=0 }
        emit(DataState.Success(allItems))
    }

    fun getCartDetails(productModel: ProductModel): Flow<DataState<ProductModel>> = flow {
        val item = cartDao.getCartDetailOfProduct(productModel.id)
        if(item!=null)
            productModel.qty = item.qty
        else
            productModel.qty = 0
        emit(DataState.Success(productModel))
    }

    fun addItemToCart(productId: Int): Flow<DataState<Boolean>> = flow {
        val productInCart = cartDao.getCartDetailOfProduct(productId)
        try {
            if (productInCart == null) {
                cartDao.addProductToCart(
                    CartDbModel(
                        productId, 1
                    )
                )
            }else{
                productInCart.qty = productInCart.qty + 1
                cartDao.updateProductQty(productInCart)
            }

            emit(DataState.Success(true))
        }catch (e: Exception){
            e.printStackTrace()
            emit(DataState.Error(e.message?:"Unknown error"))
        }
    }

    fun reduceItemFromCart(productId: Int): Flow<DataState<Boolean>> = flow {
        val productInCart = cartDao.getCartDetailOfProduct(productId)
        try {
            if (productInCart == null) {
                emit(DataState.Error("Product not in cart"))
            }else{
                if(productInCart.qty > 0){
                    productInCart.qty = productInCart.qty - 1
                    cartDao.updateProductQty(productInCart)
                }else{
                    //cartDao.deleteProductFromCart(productInCart)
                }
                emit(DataState.Success(true))
            }

        }catch (e: Exception){
            e.printStackTrace()
            emit(DataState.Error(e.message?:"Unknown error"))
        }
    }

    fun clearCart(): Flow<DataState<Boolean>> = flow {
        val result = cartDao.clearCart()
        emit(DataState.Success(true))
    }
}