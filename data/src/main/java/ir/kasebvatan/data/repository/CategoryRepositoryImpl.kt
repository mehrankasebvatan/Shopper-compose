package ir.kasebvatan.data.repository

import ir.kasebvatan.domain.network.NetworkService
import ir.kasebvatan.domain.network.ResultWrapper
import ir.kasebvatan.domain.repository.CategoryRepository

class CategoryRepositoryImpl(private val networkService: NetworkService) : CategoryRepository {
    override suspend fun getCategories(): ResultWrapper<List<String>> {
        return networkService.getCategories()
    }

}