package com.example.scanner.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanner.localdatasource.CartDbModel
import com.example.scanner.models.DataState
import com.example.scanner.models.ProductModel
import com.example.scanner.repositories.GetProductDetailsRepo
import com.example.scanner.repositories.LocalCartRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getProductDetailsRepo: GetProductDetailsRepo,
    private val cartRepo: LocalCartRepo
): ViewModel() {

    private val _getProductDetailsState: MutableLiveData<DataState<ProductModel>> = MutableLiveData()

    val getProductDetailsState: LiveData<DataState<ProductModel>>
        get() = _getProductDetailsState

    fun getProductDetailsFromId(productId: Int){
        _getProductDetailsState.value = DataState.Loading
        viewModelScope.launch {
            getProductDetailsRepo.getProductDetailsFromId(productId).onEach {dataState ->
                _getProductDetailsState.value = dataState
            }
                .launchIn(viewModelScope)
        }
    }

    private val _getCartValue: MutableLiveData<List<CartDbModel>> = MutableLiveData()

    val getCartValue: LiveData<List<CartDbModel>>
        get() = _getCartValue

    fun getCartValue(){
        viewModelScope.launch {
            cartRepo.getAllCartItems().onEach { dataState ->
                if(dataState is DataState.Success){
                    _getCartValue.value = dataState.data
                }
            }.launchIn(viewModelScope)
        }
    }
}