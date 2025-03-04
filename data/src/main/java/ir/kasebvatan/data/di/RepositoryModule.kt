package ir.kasebvatan.data.di

import ir.kasebvatan.data.repository.CategoryRepositoryImpl
import ir.kasebvatan.data.repository.ProductRepositoryImpl
import ir.kasebvatan.domain.repository.CategoryRepository
import ir.kasebvatan.domain.repository.ProductRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
}