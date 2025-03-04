package ir.kasebvatan.domain.usecase

import ir.kasebvatan.domain.repository.ProductRepository

class GetProductUseCase(private val repository: ProductRepository) {
    suspend fun execute(category: String?) = repository.getProducts(category)
}