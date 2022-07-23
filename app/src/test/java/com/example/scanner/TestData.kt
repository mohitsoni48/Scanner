package com.example.scanner

import com.example.scanner.models.ProductModel

class TestData {
    companion object{
        fun getTestProductData(): List<ProductModel> {

            return arrayListOf(
                ProductModel(
                    1,
                    "Smart Watch",
                    "",
                    200.0,
                    "https://unsplash.com/photos/2cFZ_FB08UM/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjU4NTc3MDg1&force=true",
                    qty = 1
                ),
                ProductModel(
                    2,
                    "Camera",
                    "",
                    150.0,
                    "https://unsplash.com/photos/KsLPTsYaqIQ/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjU4NTc5OTEw&force=true",
                    qty = 2
                ),
                ProductModel(
                    3,
                    "Headphones",
                    "",
                    29.99,
                    "https://unsplash.com/photos/PDX_a_82obo/download?force=true",
                    qty = 3
                ),
                ProductModel(
                    4,
                    "Shoes",
                    "",
                    19.99,
                    "https://unsplash.com/photos/164_6wVEHfI/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjU4NTc5MzQ2&force=true",
                    qty = 4
                )
            )
        }

        fun getTestProductDataWithZer0(): List<ProductModel> {

            return arrayListOf(
                ProductModel(
                    1,
                    "Smart Watch",
                    "",
                    200.0,
                    "https://unsplash.com/photos/2cFZ_FB08UM/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjU4NTc3MDg1&force=true",
                    qty = 0
                ),
                ProductModel(
                    2,
                    "Camera",
                    "",
                    150.0,
                    "https://unsplash.com/photos/KsLPTsYaqIQ/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjU4NTc5OTEw&force=true",
                    qty = 0
                ),
                ProductModel(
                    3,
                    "Headphones",
                    "",
                    29.99,
                    "https://unsplash.com/photos/PDX_a_82obo/download?force=true",
                    qty = 3
                ),
                ProductModel(
                    4,
                    "Shoes",
                    "",
                    19.99,
                    "https://unsplash.com/photos/164_6wVEHfI/download?ixid=MnwxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNjU4NTc5MzQ2&force=true",
                    qty = 4
                )
            )
        }
    }
}