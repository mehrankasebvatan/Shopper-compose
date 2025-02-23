package ir.kasebvatan.data.repository

import ir.kasebvatan.domain.model.Product
import ir.kasebvatan.domain.network.NetworkService
import ir.kasebvatan.domain.network.ResultWrapper
import ir.kasebvatan.domain.repository.ProductRepository

class ProductRepositoryImpl(private val networkService: NetworkService) : ProductRepository {
    override suspend fun getProducts(category: String?): ResultWrapper<List<Product>> {
        return networkService.getProducts(category)
    }
}