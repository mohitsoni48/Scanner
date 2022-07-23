package com.example.scanner.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.scanner.R
import com.example.scanner.common.AppConstants
import com.example.scanner.common.loadImageFromNetwork
import com.example.scanner.databinding.ActivityProductSummaryBinding
import com.example.scanner.models.DataState
import com.example.scanner.models.ProductModel
import com.example.scanner.viewmodels.ProductSummaryViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class ProductSummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductSummaryBinding
    private val productSummaryViewModel: ProductSummaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(AppConstants.PRODUCT_DETAILS)) {
            val prod = intent.getStringExtra(AppConstants.PRODUCT_DETAILS)
            val productModel: ProductModel = Gson().fromJson(prod, ProductModel::class.java)
            productSummaryViewModel.getProductDetailsState.observe(this){ dataState->
                when(dataState){
                    is DataState.Loading->{

                    }

                    is DataState.Success ->{
                        inflateProductDetails(dataState.data)
                    }

                    is DataState.Error->{
                        Toast.makeText(this, dataState.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }

            productSummaryViewModel.getCartDetailsOfProduct(productModel)
        } else {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.emptySpace.setOnClickListener {
            onBackPressed()
        }
    }

    private fun inflateProductDetails(productModel: ProductModel) {
        binding.apply {
            productImage.loadImageFromNetwork(productModel.id, productModel.productImageUrl)
            productTitle.text = productModel.productName
            productPrice.text = "\$${DecimalFormat("0.00").format(productModel.productPrice)}"

            if (productModel.qty == 0){
                addToCart.visibility = View.VISIBLE
                cartOp.visibility = View.GONE
            }else{
                addToCart.visibility = View.GONE
                cartOp.visibility = View.VISIBLE
                qty.text = productModel.qty.toString()
            }

            addToCart.setOnClickListener {
                productSummaryViewModel.addProductToCart(productModel)
            }

            minus.setOnClickListener {
                productSummaryViewModel.subtractProductToCart(productModel)
            }

            add.setOnClickListener {
                productSummaryViewModel.addProductToCart(productModel)
            }

            buyNow.setOnClickListener {
                productSummaryViewModel.addProductToCart(productModel)
                val productListIntent = Intent(this@ProductSummaryActivity, ProductListActivity::class.java)
                startActivity(productListIntent)
                finish()
            }
        }
    }
}