package com.example.scanner.repositories

import com.example.scanner.models.DataState
import com.example.scanner.models.FakeProductList
import com.example.scanner.models.ProductModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class GetProductDetailsRepo {

    fun getProductDetailsFromId(productId: Int) = flow<DataState<ProductModel>> {
        val response = getFakeProduct(productId)    //call api instead of fake suspend function
        if(response!=null){
            emit(DataState.Success(response))
        }else{
            emit(DataState.Error("Unknown Product"))
        }
    }

    private suspend fun getFakeProduct(productId: Int): ProductModel? {
        delay(500)  //to mimic api call
        return FakeProductList.productsList.firstOrNull { productId == it.id }
    }
}