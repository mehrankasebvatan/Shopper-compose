package ir.kasebvatan.domain.repository

import ir.kasebvatan.domain.model.Product
import ir.kasebvatan.domain.network.ResultWrapper

interface ProductRepository {

    suspend fun getProducts(category: String?): ResultWrapper<List<Product>>
}