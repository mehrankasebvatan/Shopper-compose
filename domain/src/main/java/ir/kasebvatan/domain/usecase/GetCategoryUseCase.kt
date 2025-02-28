package ir.kasebvatan.domain.usecase

import ir.kasebvatan.domain.repository.CategoryRepository

class GetCategoryUseCase(private val repository: CategoryRepository) {
    suspend fun execute() = repository.getCategories()
} 