package ir.kasebvatan.shopper.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kasebvatan.domain.model.Product
import ir.kasebvatan.domain.network.ResultWrapper
import ir.kasebvatan.domain.usecase.GetCategoryUseCase
import ir.kasebvatan.domain.usecase.GetProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getProductUseCase: GetProductUseCase,
    private val getCategoryUseCase: GetCategoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenUIEvents>(HomeScreenUIEvents.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getAllProducts()
    }

    private fun getAllProducts() {
        viewModelScope.launch {
            _uiState.value = HomeScreenUIEvents.Loading
            val featured = getProducts("electronics")
            val popular = getProducts("jewelery")
            val categories = getCategories()
            if (featured.isEmpty() || popular.isEmpty()) {
                _uiState.value = HomeScreenUIEvents.Error("Failed to load products")
                return@launch
            }
            _uiState.value = HomeScreenUIEvents.Success(featured, popular, categories)
        }
    }


    private suspend fun getCategories(): List<String> {
        getCategoryUseCase.execute().let { result ->
            return when (result) {
                is ResultWrapper.Success -> {
                    result.value
                }

                is ResultWrapper.Failure -> {
                    emptyList()
                }
            }
        }
    }


    private suspend fun getProducts(category: String?): List<Product> {
        getProductUseCase.execute(category).let { result ->
            return when (result) {
                is ResultWrapper.Success -> {
                    result.value
                }

                is ResultWrapper.Failure -> {
                    emptyList()
                }
            }
        }

    }
}


sealed class HomeScreenUIEvents {
    data object Loading : HomeScreenUIEvents()
    data class Success(
        val featured: List<Product>,
        val popularProducts: List<Product>,
        val categories: List<String>
    ) : HomeScreenUIEvents()

    data class Error(val message: String) : HomeScreenUIEvents()
}