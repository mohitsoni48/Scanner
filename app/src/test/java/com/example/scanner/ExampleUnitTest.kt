package com.example.scanner

import android.os.Build
import app.cash.turbine.test
import com.example.scanner.localdatasource.CartDao
import com.example.scanner.models.DataState
import com.example.scanner.repositories.GetCheckoutDetailsRepo
import com.example.scanner.repositories.LocalCartRepo
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import kotlin.time.ExperimentalTime

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O], application = HiltTestApplication::class)
class ExampleUnitTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Inject
    lateinit var localCartRepo: LocalCartRepo

    @Inject
    lateinit var cartDao: CartDao

    @Inject
    lateinit var checkoutDetailsRepo: GetCheckoutDetailsRepo

    @Test
    fun addProductToCart(): Unit = runBlocking{
        val productList = TestData.getTestProductData()
        productList.onEach {
            for (i in 0 until it.qty){
                val flow = localCartRepo.addItemToCart(it.id)
                flow.onEach { dataState ->
                    assert(dataState is DataState.Success)
                }
            }
        }

        val flow = localCartRepo.getAllCartItems()
        flow.onEach { dataState ->
            assert(dataState is DataState.Success)
            val dbList = (dataState as DataState.Success).data
            assertEquals(productList, dbList)
        }

    }

    @Test
    fun subtractProductFromCart(): Unit = runBlocking{
        val flow = localCartRepo.getAllCartItems()
        flow.onEach { dataState ->
            val dbList = (dataState as DataState.Success).data
            dbList.forEach{ cartDbModel ->
                localCartRepo.reduceItemFromCart(cartDbModel.product_id).onEach {
                    assert(it is DataState.Success)
                    assert(cartDao.getCartDetailOfProduct(cartDbModel.product_id)!!.qty >=0 )
                }
            }
        }
    }

    @Test
    fun getProductFromCartForCheckout(): Unit = runBlocking{
        val productList = TestData.getTestProductDataWithZer0()
        productList.onEach {
            for (i in 0 until it.qty){
                localCartRepo.addItemToCart(it.id)
            }
        }

        val flow = checkoutDetailsRepo.getCheckoutDetailsFromCart()
        flow.onEach { dataState ->
            val checkoutModel = dataState.data
            assert(checkoutModel.totalCartValue > 0.0)

            checkoutModel.listOfProducts.forEach{ product ->
                assert(product.qty > 0)
            }
        }
    }
}