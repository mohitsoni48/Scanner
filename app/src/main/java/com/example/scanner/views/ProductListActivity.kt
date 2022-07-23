package com.example.scanner.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.scanner.adapter.ProductCartAdapter
import com.example.scanner.databinding.ActivityProductListBinding
import com.example.scanner.models.CheckoutModel
import com.example.scanner.models.DataState
import com.example.scanner.viewmodels.ProductListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private val productListViewModel: ProductListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appBar.backBt.visibility = View.VISIBLE
        binding.appBar.backBt.setOnClickListener {
            onBackPressed()
        }

        productListViewModel.getAllProductsInCart.observe(this){
            when(it){
                is DataState.Loading->{
                    binding.progressBar.visibility = View.VISIBLE
                }

                is DataState.Success->{
                    binding.progressBar.visibility = View.GONE
                    inflateData(checkoutModel = it.data)

                }

                is DataState.Error->{
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }

        productListViewModel.getAllProductsInCart()
    }

    private fun inflateData(checkoutModel: CheckoutModel){
        binding.apply {
            appBar.cartQty.apply {
                visibility = View.VISIBLE
                text = "\$${DecimalFormat("0.00").format(checkoutModel.listOfProducts.size)}"
            }
            binding.productList.apply {
                layoutManager = GridLayoutManager(this@ProductListActivity, 1)
                adapter = ProductCartAdapter(checkoutModel.listOfProducts)
            }

            cartTotal.text = "\$${DecimalFormat("0.00").format(checkoutModel.totalCartValue)}"
            checkout.setOnClickListener {
                productListViewModel.checkout()
            }
        }

        productListViewModel.getCheckoutLiveData.observe(this){
            when(it){
                is DataState.Loading->{
                    binding.progressBar.visibility = View.VISIBLE
                }

                is DataState.Success->{
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Order placed successfully", Toast.LENGTH_LONG).show()
                    finish()
                }

                is DataState.Error->{
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }
}