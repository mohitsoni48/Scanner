package com.example.scanner.views

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.scanner.common.AppConstants
import com.example.scanner.databinding.ActivityMainBinding
import com.example.scanner.models.DataState
import com.example.scanner.viewmodels.MainViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var binding: ActivityMainBinding
    private val CAMERA_PERMISSION = 100
    private val mainViewModel: MainViewModel by viewModels()

    private var lastItemId = 1  //for randomItems

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        codeScanner = CodeScanner(this, binding.scannerView)

        checkCameraPermission()

        mainViewModel.getCartValue.observe(this){ cartList->
            binding.appBar.cartQty.apply {
                isVisible = cartList.isNotEmpty()
                text = cartList.size.toString()
            }

            binding.appBar.icCart.setOnClickListener {
                if(cartList.isNotEmpty()){
                    val productListIntent = Intent(this, ProductListActivity::class.java)
                    startActivity(productListIntent)
                }else{
                    Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
                }
            }
        }

        mainViewModel.getProductDetailsState.observe(this){
            when(it){
                is DataState.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                }

                is DataState.Success ->{
                    binding.progressBar.visibility = View.GONE
                    val productDetailIntent = Intent(this, ProductSummaryActivity::class.java)
                    productDetailIntent.putExtra(AppConstants.PRODUCT_DETAILS, Gson().toJson(it.data))
                    startActivity(productDetailIntent)
                }

                is DataState.Error ->{
                    binding.progressBar.visibility = View.GONE
                    codeScanner.startPreview()
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun initializeScanner(){
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                codeScanner.stopPreview()
                Log.d("OnScan: ", it.text)
                val randomId = if(lastItemId >=5) 1 else ++lastItemId
                //Toast.makeText(this, "Scan result: $randomId", Toast.LENGTH_SHORT).show()
                fetchProductDetails(randomId)
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private fun fetchProductDetails(productId: Int){
        mainViewModel.getProductDetailsFromId(productId)
    }

    private fun checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            initializeScanner()
        } else {
            // Fine Location Permission is not granted so ask for permission
            askForCameraPermission()
        }
    }

    private fun askForCameraPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            )
        ) {
            AlertDialog.Builder(this)
                .setTitle("Camera Permission Needed!")
                .setMessage("Camera permission is needed so you can scan barcodes and QR codes.")
                .setPositiveButton("OK",
                    DialogInterface.OnClickListener { dialog, which ->
                        ActivityCompat.requestPermissions(
                            this, arrayOf(
                                Manifest.permission.CAMERA
                            ), CAMERA_PERMISSION
                        )
                    })
                .setNegativeButton("CANCEL", DialogInterface.OnClickListener { dialog, which ->
                    // Permission is denied by the user
                    //finish()
                })
                .create().show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeScanner()
            } else {
                Toast.makeText(this, "Cannot scan codes due to permission error.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
        mainViewModel.getCartValue()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}