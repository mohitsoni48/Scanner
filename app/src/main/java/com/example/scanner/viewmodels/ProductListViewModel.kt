package com.example.scanner.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scanner.models.CheckoutModel
import com.example.scanner.models.DataState
import com.example.scanner.models.ProductModel
import com.example.scanner.repositories.GetCheckoutDetailsRepo
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getCheckoutDetailsRepo: GetCheckoutDetailsRepo
): ViewModel() {

    private val _getAllProductsInCart: MutableLiveData<DataState<CheckoutModel>> =
        MutableLiveData()

    val getAllProductsInCart: LiveData<DataState<CheckoutModel>>
        get() = _getAllProductsInCart

    fun getAllProductsInCart() {
        viewModelScope.launch {
            _getAllProductsInCart.value = DataState.Loading
            getCheckoutDetailsRepo.getCheckoutDetailsFromCart().onEach { dataState->
                _getAllProductsInCart.value = dataState
            }.launchIn(viewModelScope)
        }
    }

    private val _getCheckoutLiveData: MutableLiveData<DataState<Boolean>> =
        MutableLiveData()

    val getCheckoutLiveData: LiveData<DataState<Boolean>>
        get() = _getCheckoutLiveData

    fun checkout() {
        viewModelScope.launch {
            _getCheckoutLiveData.value = DataState.Loading
            getCheckoutDetailsRepo.checkOut().onEach { dataState->
                _getCheckoutLiveData.value = dataState
            }.launchIn(viewModelScope)
        }
    }
}