package com.mostafadevo.freegames.ui.screens.deals_screen

import android.annotation.SuppressLint
import android.app.Activity
import android.util.LruCache
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Chip
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.mostafadevo.freegames.data.remote.cheapshark.dto.DealsDTOItem
import com.mostafadevo.freegames.ui.components.BlurredBox
import com.mostafadevo.freegames.ui.components.DealsListItem
import com.mostafadevo.freegames.ui.components.History
import com.mostafadevo.freegames.ui.screens.deals_screen.search_bar.SearchBarUiEvent
import com.mostafadevo.freegames.utils.extractDominantColor
import com.mostafadevo.freegames.utils.loadImage
import kotlin.math.roundToInt

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
)
@Composable
fun DealsScreen(
    viewModel: DealsScreenViewModel,
    navController: NavHostController
) {
    val searchBarUiState = viewModel.searchBarUiState.collectAsStateWithLifecycle().value
    val dealsScreenUiState = viewModel.dealsScreenUiState.collectAsStateWithLifecycle().value
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedIndex by remember { mutableStateOf(0) }
    val options = listOf("Deals \uD83E\uDD1D", "Giveaways \uD83C\uDF89")

    val window = (LocalContext.current as Activity).window
    SideEffect {
        window.statusBarColor = Color.Red.toArgb()
    }
    LaunchedEffect(searchBarUiState.SearchBarText) {
        if (searchBarUiState.SearchBarText.isEmpty() && searchBarUiState.deals?.isNotEmpty() == true) {
            viewModel.onEvent(SearchBarUiEvent.clearDeals)
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AnimatedVisibility(
                visible = searchBarUiState.isSearchBarActive.not(),
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { -it })
            ) {
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                ) {
                    options.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size
                            ),
                            onClick = { selectedIndex = index },
                            selected = index == selectedIndex
                        ) {
                            Text(label)
                        }
                    }
                }
            }

        }
    ) { innerpadding ->
        //deals content
        LaunchedEffect(true) {
            viewModel.uiEffect.collect {
                when (it) {
                    is DealsScreenUiEffect.ShowSnackBar -> {
                        //show snackbar
                        snackbarHostState.showSnackbar(it.message)
                    }
                }
            }
        }
        //animateddpasstate padding search bar
        val searchbarPaddingAnimation = animateDpAsState(
            targetValue = 8.dp,
        )


        // offers screen content
        // deals screen
        AnimatedVisibility(
            modifier = Modifier.padding(innerpadding),
            visible = selectedIndex == 0,
            enter = fadeIn(
                initialAlpha = 0.3f
            ),
            exit = fadeOut(
                targetAlpha = 0.3f
            )
        ) {
            if (dealsScreenUiState.isLoading) {
                // TODO: replace with shimmer
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

            }
            Column(
                modifier = Modifier
                    .fillMaxSize()

            ) {
                SearchBar(
                    placeholder = {
                        Text(
                            text = "Search for deals",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            if (searchBarUiState.isSearchBarActive) 0.dp else searchbarPaddingAnimation.value,
                        ),
                    query = searchBarUiState.SearchBarText,
                    onQueryChange = {
                        viewModel.onEvent(SearchBarUiEvent.onSearchBarTextChange(it))
                    },
                    onSearch = { searchQuery ->
                        searchQuery.let {
                            viewModel.onEvent(SearchBarUiEvent.onSearch)
                        }
                    },
                    active = searchBarUiState.isSearchBarActive,
                    onActiveChange = {
                        viewModel.onEvent(SearchBarUiEvent.onSearchBarActive(it))
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Favorite"
                        )
                    },
                    trailingIcon = {
                        if (searchBarUiState.isSearchBarActive) {
                            IconButton(
                                onClick = {
                                    if (searchBarUiState.SearchBarText.isNotEmpty()) {
                                        viewModel.onEvent(SearchBarUiEvent.onSearchBarTextChange(""))
                                    } else {
                                        viewModel.onEvent(SearchBarUiEvent.onSearchBarActive(false))
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close"
                                )
                            }
                        }
                    }
                ) {
                    // Search bar content
                    if (searchBarUiState.SearchBarText.isEmpty() || searchBarUiState.SearchBarText.isBlank()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .imePadding()
                                .imeNestedScroll(),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {


                            item {
                                Text(
                                    text = "Recent Searches",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            items(searchBarUiState.searchHistory ?: emptyList()) { deal ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.onEvent(
                                                SearchBarUiEvent.onSearchBarTextChange(
                                                    deal
                                                )
                                            )
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = History,
                                        contentDescription = "history"
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = deal,
                                    )
                                }

                            }
                        }

                    }

                    if (searchBarUiState.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }

                    }
                    searchBarUiState.deals?.let {
                    }
                }
                LazyColumn {
                    items(dealsScreenUiState.deals!!, key = { it.dealID }) { deal ->
                        DealsListItem(
                            modifier = Modifier.animateItem(),
                            onClickListener = {},
                            imageLink = deal.thumb,
                            string1 = deal.title,
                            string2 = deal.salePrice + "$",
                            string3LineThrough = "${deal.normalPrice}$",
                            string4 = "${deal.savings}% off",
                        )
                    }
                }
            }
        }

        // giveaways screen
        AnimatedVisibility(
            modifier = Modifier.padding(innerpadding),
            visible = selectedIndex == 1,
            enter = fadeIn(
                initialAlpha = 0.3f
            ),
            exit = fadeOut(
                targetAlpha = 0.3f
            )
        ) {
            Text(text = "Giveaways")

        }

        // TODO: add filters by shop , onSale , sortby

    }
}
