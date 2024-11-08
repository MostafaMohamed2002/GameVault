package com.mostafadevo.freegames.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mostafadevo.freegames.R
import com.mostafadevo.freegames.ui.screens.deals_screen.DealsAndGiveawayScreenViewModel
import com.mostafadevo.freegames.ui.screens.deals_screen.DealsScreen
import com.mostafadevo.freegames.ui.screens.detailes_screen.FreeGameDetailesScreen
import com.mostafadevo.freegames.ui.screens.detailes_screen.FreeGameDetailesViewModel
import com.mostafadevo.freegames.ui.screens.home_screen.FreeGamesScreen
import com.mostafadevo.freegames.ui.screens.home_screen.FreeGamesScreenViewModel
import com.mostafadevo.freegames.ui.screens.settings_screen.SettingsScreen

@Composable
fun NavHostScreen() {
    val navController = rememberNavController()
    var bottomBarVisibility by remember { mutableStateOf(true) }

    // Scaffold with Bottom Navigation
    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarVisibility,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                NavigationBottomBar(navController = navController)
            }
        }
    ) { scaffoldPadding ->
        // Handle Navigation
        NavHost(
            navController = navController,
            startDestination = "/free",
            modifier = Modifier
                .padding(scaffoldPadding)
                .consumeWindowInsets(scaffoldPadding)
                .semantics {
                    testTagsAsResourceId = true
                },
            enterTransition = { slideInHorizontally { it } + fadeIn() },
            exitTransition = { slideOutHorizontally { -it } + fadeOut() },
            popEnterTransition = { slideInHorizontally { -it } + fadeIn() },
            popExitTransition = { slideOutHorizontally { it } + fadeOut() }
        ) {
            composable(route = "/deals") {
                bottomBarVisibility = true // Show bottom bar in the "deals" screen
                val dealsAndGiveawayScreenViewModel = hiltViewModel<DealsAndGiveawayScreenViewModel>()
                DealsScreen(
                    viewModel = dealsAndGiveawayScreenViewModel,
                )
            }
            composable(route = "/free") {
                bottomBarVisibility = true // Show bottom bar in the "free" screen
                val freeGamesScreenViewModel = hiltViewModel<FreeGamesScreenViewModel>()
                FreeGamesScreen(
                    navController = navController,
                    freeGamesScreenViewModel = freeGamesScreenViewModel
                )
            }
            composable(
                route = "/details/{gameId}",
                arguments = listOf(navArgument("gameId") { type = NavType.IntType })
            ) { backStackEntry ->
                bottomBarVisibility = false // Hide bottom bar in the "details" screen
                val freeGameDetailesViewModel = hiltViewModel<FreeGameDetailesViewModel>()

                FreeGameDetailesScreen(
                    gameId = backStackEntry.arguments?.getInt("gameId") ?: 0,
                    viewModel = freeGameDetailesViewModel
                )
            }
            composable(route = "/settings") {
                bottomBarVisibility = true
                SettingsScreen()
            }
        }
    }
}

@Composable
fun NavigationBottomBar(navController: NavController) {
    val items = listOf(
        NavItem(
            "/free",
            "Free",
            icon = {
                Icon(
                    painter = painterResource(R.drawable.game2_o_svgrepo_com),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        ),
        NavItem(
            "/deals",
            "Deals",
            icon = {
                Icon(
                    painter = painterResource(R.drawable.gift_svgrepo_com),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        ),
        NavItem(
            "/fav",
            "Favourite",
            icon = { Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = null) }
        ),
        NavItem(
            "/settings",
            "Settings",
            icon = { Icon(imageVector = Icons.Default.Settings, contentDescription = null) }
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomAppBar {
        items.forEach { item ->
            val animatedWeight by animateFloatAsState(
                targetValue = if (currentRoute == item.route) 1.5f else 1f
            )
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    item.icon()
                },
                label = {
                    Text(item.title)
                },
                alwaysShowLabel = true,
                modifier = Modifier.weight(animatedWeight)
            )
        }
    }
}

// TODO: add selected icon
data class NavItem(val route: String, val title: String, val icon: @Composable () -> Unit)
