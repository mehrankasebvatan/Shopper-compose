package ir.kasebvatan.shopper.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kasebvatan.domain.model.Product
import ir.kasebvatan.domain.network.ResultWrapper
import ir.kasebvatan.domain.usecase.GetProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val etProductUseCase: GetProductUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenUIEvents>(HomeScreenUIEvents.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            _uiState.value = HomeScreenUIEvents.Loading
            etProductUseCase.execute().let { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        val data = (result as ResultWrapper.Success).value
                        _uiState.value = HomeScreenUIEvents.Success(data)
                    }

                    is ResultWrapper.Failure -> {
                        val error = (result as ResultWrapper.Failure).exception.message
                            ?: "An error occurred"
                        _uiState.value = HomeScreenUIEvents.Error(error)
                    }
                }
            }
        }
    }
}


sealed class HomeScreenUIEvents {
    data object Loading : HomeScreenUIEvents()
    data class Success(val data: List<Product>) : HomeScreenUIEvents()
    data class Error(val message: String) : HomeScreenUIEvents()
}