package ir.kasebvatan.shopper.ui.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import ir.kasebvatan.domain.model.Product
import ir.kasebvatan.shopper.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = koinViewModel()) {
    val uiState = viewModel.uiState.collectAsState().value

    var loading by remember {
        mutableStateOf(false)
    }
    var error by remember {
        mutableStateOf<String?>(null)
    }

    var featured by remember {
        mutableStateOf<List<Product>>(emptyList())
    }

    var popular by remember {
        mutableStateOf<List<Product>>(emptyList())
    }
    var categories by remember {
        mutableStateOf<List<String>>(emptyList())
    }


    Scaffold {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when (uiState) {

                is HomeScreenUIEvents.Loading -> {
                    loading = true
                    error = null
                }

                is HomeScreenUIEvents.Success -> {
                    val data = (uiState as HomeScreenUIEvents.Success)
                    featured = data.featured
                    popular = data.popularProducts
                    categories = data.categories
                    loading = false
                    error = null
                }


                is HomeScreenUIEvents.Error -> {
                    val errorMsg = (uiState as HomeScreenUIEvents.Error).message
                    loading = false
                    error = errorMsg
                }
            }

            HomeContent(
                featured,
                popular,
                categories,
                loading,
                error
            )
        }
    }


}

@Composable
fun HomeContent(
    featured: List<Product>,
    popularProducts: List<Product>,
    categories: List<String>,
    loading: Boolean = false,
    errorMsg: String? = null
) {
    var hint by remember {
        mutableStateOf("")
    }
    LazyColumn {
        item {
            ProfileHeader()
            Spacer(Modifier.size(16.dp))
            SearchBar(hint) { cb ->
                hint = cb
            }
            Spacer(Modifier.size(16.dp))

        }
        item {

            if (loading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(50.dp))
                    Text("Loading...", style = MaterialTheme.typography.bodyMedium)
                }
            }

            errorMsg?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium)
            }

            if (categories.isNotEmpty()) {
                LazyRow {

                    items(categories) { category ->
                        Text(
                            color = MaterialTheme.colorScheme.onPrimary,
                            text = category.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(8.dp)
                        )
                    }
                }
                Spacer(Modifier.size(16.dp))

            }
            if (featured.isNotEmpty()) {
                HomeProductRow(featured, "Featured")
                Spacer(Modifier.size(16.dp))
            }
            if (popularProducts.isNotEmpty()) {
                HomeProductRow(
                    popularProducts,
                    "Popular"
                )
                Spacer(Modifier.size(16.dp))
            }


        }
    }

}


@Composable
fun HomeProductRow(products: List<Product>, title: String) {

    Column {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterStart),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "View All",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
        Spacer(Modifier.size(8.dp))
        LazyRow {
            items(products) { item ->
                ProductItem(item)
            }
        }
    }

}

@Composable
fun ProductItem(product: Product) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(126.dp),
        //.size(width = 126.dp, height = 144.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = product.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
            )
            Spacer(Modifier.size(8.dp))

            Text(
                text = product.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = product.priceString,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

        }
    }
}

@Composable
fun SearchBar(value: String, onTextChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onTextChange,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        maxLines = 1,
        leadingIcon = {
            Image(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.LightGray.copy(0.3f),
            unfocusedContainerColor = Color.LightGray.copy(0.3f)
        ),
        placeholder = {
            Text(
                text = "Search for products",
                style = MaterialTheme.typography.bodySmall
            )
        }
    )
}

@Composable
fun ProfileHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_profile),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Inside
            )
            Spacer(Modifier.size(8.dp))
            Column {

                Text(text = "Hello, ", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "John Doe",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Image(
            painter = painterResource(R.drawable.ic_notifications),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterEnd)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = 0.3f))
                .padding(8.dp),
            contentScale = ContentScale.Inside
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomePreview() {
    /*val list = mutableListOf<Product>()
    for (i in 1..10) {
        list.add(
            Product(
                id = i.toLong(),
                title = "title $i",
                price = Random.nextDouble(10_000.0, 50_000.0),
                category = "category $i",
                description = "description $i",
                image = "https://www.nike.com/de/u/custom-nike-air-max-97-shoes-by-you-10001609"
            )
        )
    }*/
    Scaffold {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            var hint by remember {
                mutableStateOf("search")
            }
            SearchBar(hint) { cb ->
                hint = cb
            }
        }
    }
}

