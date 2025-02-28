package ir.kasebvatan.domain.repository

import ir.kasebvatan.domain.network.ResultWrapper

interface CategoryRepository {
    suspend fun getCategories(): ResultWrapper<List<String>>
}