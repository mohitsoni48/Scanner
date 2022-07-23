package com.example.scanner.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanner.models.DataState
import com.example.scanner.models.ProductModel
import com.example.scanner.repositories.LocalCartRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductSummaryViewModel @Inject constructor(
    private val localCartRepo: LocalCartRepo
): ViewModel() {
    private val _getProductDetailsState: MutableLiveData<DataState<ProductModel>> = MutableLiveData()

    val getProductDetailsState: LiveData<DataState<ProductModel>>
        get() = _getProductDetailsState

    fun getCartDetailsOfProduct(product: ProductModel){
        _getProductDetailsState.value = DataState.Loading
        viewModelScope.launch {
            localCartRepo.getCartDetails(product).onEach {dataState ->
                _getProductDetailsState.value = dataState
            }
                .launchIn(viewModelScope)
        }
    }

    fun addProductToCart(product: ProductModel){
        viewModelScope.launch {
            localCartRepo.addItemToCart(product.id).onEach { dataState ->
                getCartDetailsOfProduct(product)
            }.launchIn(viewModelScope)
        }
    }

    fun subtractProductToCart(product: ProductModel){
        viewModelScope.launch {
            localCartRepo.reduceItemFromCart(product.id).onEach { dataState ->
                getCartDetailsOfProduct(product)
            }.launchIn(viewModelScope)
        }
    }
}