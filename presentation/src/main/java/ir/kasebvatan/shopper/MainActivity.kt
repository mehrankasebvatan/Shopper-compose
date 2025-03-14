package ir.kasebvatan.shopper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ir.kasebvatan.shopper.ui.feature.home.HomeScreen
import ir.kasebvatan.shopper.ui.theme.ShopperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopperTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(navController)
                    }
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    ) {
                        NavHost(navController = navController, startDestination = "home") {
                            composable("home") {
                                HomeScreen(navController)
                            }
                            composable("cart") {
                                Text("CART")
                            }
                            composable("profile") {
                                Text("PROFILE")
                            }
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        var currentRoute =
            navController.currentBackStackEntryAsState().value?.destination?.route ?: "home"
        val items = listOf(
            BottomNavItems.Home,
            BottomNavItems.Cart,
            BottomNavItems.Profile,
        )

        items.forEach { item ->
            NavigationBarItem(
                selected = item.route == currentRoute,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { startRoute ->
                            popUpTo(startRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = {
                    Text(item.title)
                },
                icon = {
                    Image(
                        painter = painterResource(item.icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            if (item.route == currentRoute) MaterialTheme.colorScheme.primary
                            else Color.Gray
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors().copy(
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = Color.Gray
                )
            )

        }


    }
}


sealed class BottomNavItems(val route: String, val title: String, val icon: Int) {
    object Home : BottomNavItems("home", "Home", R.drawable.ic_home)
    object Cart : BottomNavItems("cart", "Cart", R.drawable.ic_cart)
    object Profile : BottomNavItems("profile", "Profile", R.drawable.ic_profile_bn)
}

