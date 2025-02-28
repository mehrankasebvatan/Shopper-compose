package ir.kasebvatan.domain.di

import ir.kasebvatan.domain.usecase.GetCategoryUseCase
import ir.kasebvatan.domain.usecase.GetProductUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetProductUseCase(get()) }
    factory { GetCategoryUseCase(get()) }
}