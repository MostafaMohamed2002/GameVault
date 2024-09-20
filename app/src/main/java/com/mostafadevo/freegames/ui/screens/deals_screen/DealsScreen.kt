package com.mostafadevo.freegames.ui.screens.deals_screen

import android.annotation.SuppressLint
import android.util.LruCache
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.mostafadevo.freegames.data.remote.cheapshark.dto.DealsDTOItem
import com.mostafadevo.freegames.ui.components.BlurredBox
import com.mostafadevo.freegames.ui.components.History
import com.mostafadevo.freegames.ui.screens.deals_screen.search_bar.SearchBarUiEvent
import com.mostafadevo.freegames.utils.extractDominantColor
import com.mostafadevo.freegames.utils.loadImage
import kotlin.math.roundToInt

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun DealsScreen(
    viewModel: DealsScreenViewModel,
    navController: NavHostController
) {
    val searchBarUiState = viewModel.searchBarUiState.collectAsStateWithLifecycle().value
    val dealsScreenUiState = viewModel.dealsScreenUiState.collectAsStateWithLifecycle().value
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            LaunchedEffect(searchBarUiState.SearchBarText) {
                if (searchBarUiState.SearchBarText.isEmpty() && searchBarUiState.deals?.isNotEmpty() == true) {
                    viewModel.onEvent(SearchBarUiEvent.clearDeals)
                }
            }
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
                        if (searchBarUiState.isSearchBarActive) 0.dp else 8.dp
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
                    DealScreenContent(
                        PaddingValues(8.dp),
                        deals = it,
                        viewModel.colorCache,
                        navController
                    )
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
        if (dealsScreenUiState.isLoading) {
            // TODO: replace with shimmer
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        // TODO: add filters by shop , onSale , sortby
        dealsScreenUiState.deals?.let {
            DealScreenContent(
                innerpadding,
                deals = it,
                viewModel.colorCache,
                navController
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DealScreenContent(
    paddingValues: PaddingValues,
    deals: List<DealsDTOItem>,
    colorCache: LruCache<String, Color>,
    navController: NavHostController
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    ) {
        items(deals, key = { it.dealID }) { deal ->
            var dominantColor by remember { mutableStateOf<Color>(Color.Transparent) }

            LaunchedEffect(deal.thumb) {
                if (colorCache.get(deal.thumb) != null) {
                    dominantColor = colorCache[deal.thumb]!!
                } else {
                    val drawable = loadImage(context, deal.thumb)
                    val extractedColor =
                        drawable?.let { imgDrawable -> extractDominantColor(imgDrawable) }
                            ?: Color.Transparent
                    dominantColor = extractedColor
                    colorCache.put(deal.thumb, extractedColor)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .animateItem()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                dominantColor
                            )
                        )
                    )
                    .clickable {
                        navController.navigate("/dealsDetails/${deal.dealID}")
                    },
                verticalAlignment = Alignment.CenterVertically,

                ) {
                AsyncImage(
                    model = deal.thumb,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(100.dp, 100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = deal.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .padding(4.dp)
                            .basicMarquee()
                    )
                    Row {
                        BlurredBox(
                            Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            Text(
                                text =
                                "Deal Rate:${
                                    deal.dealRating.toDouble().toInt()
                                }",
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        BlurredBox(
                            Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            Text(
                                text =
                                "Save: ${
                                    deal.normalPrice.toDouble().minus(deal.salePrice.toDouble())
                                        .roundToInt()
                                }$",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
