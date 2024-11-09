package com.mostafadevo.freegames.ui.screens.deals_screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.mostafadevo.freegames.ui.components.DealsListItem
import com.mostafadevo.freegames.ui.components.FilterIcon
import com.mostafadevo.freegames.ui.components.GiveawayListItem
import com.mostafadevo.freegames.ui.components.History
import com.mostafadevo.freegames.ui.components.ShimmeringText
import com.mostafadevo.freegames.utils.openUrl

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun DealsScreen(
    viewModel: DealsAndGiveawayScreenViewModel,
) {
    val state = viewModel.dealsAndGiveawayScreenUiState.collectAsStateWithLifecycle().value
    val snackbarHostState = remember { SnackbarHostState() }
    val options = listOf("Deals \uD83E\uDD1D", "Giveaways \uD83C\uDF89")
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val sheetStateGiveaways = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(state.dealsSearchBarText) {
        if (state.dealsSearchBarText.isEmpty() && state.dealsSearchBardata?.isNotEmpty() == true) {
            viewModel.onEvent(DealsAndGiveawayScreenUiEvent.OnClearSearchBarDeals) // clear list of deals inside search bar
        }
    }
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }, topBar = {
        AnimatedVisibility(
            visible = state.isDealsSearchBarActive.not(),
            enter = scaleIn(transformOrigin = TransformOrigin.Center),
            exit = scaleOut(transformOrigin = TransformOrigin.Center)
        ) {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        onClick = {
                            viewModel.onEvent(DealsAndGiveawayScreenUiEvent.onTabSelected(index))
                        },
                        selected = index == state.selectedTab
                    ) {
                        Text(label)
                    }
                }
            }
        }
    }, floatingActionButton = {
            if (
                state.selectedTab == 0 && state.isDealsSearchBarActive.not()
            ) {
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(DealsAndGiveawayScreenUiEvent.OnToggleBottomSheet(true))
                    }
                ) {
                    Icon(
                        imageVector = FilterIcon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            if (state.selectedTab == 1) {
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(
                            DealsAndGiveawayScreenUiEvent.OnToggleGiveawaysBottomSheet(
                                true
                            )
                        )
                    }
                ) {
                    Icon(
                        imageVector = FilterIcon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }) { innerpadding ->
        // deals content
        LaunchedEffect(true) {
            viewModel.uiEffect.collect {
                when (it) {
                    is DealsAndGiveawayScreenUiEffect.ShowSnackBar -> {
                        // show snackbar
                        snackbarHostState.showSnackbar(it.message)
                    }
                }
            }
        }
        // animateddpasstate padding search bar
        val searchbarPaddingAnimation = animateDpAsState(
            targetValue = 8.dp
        )

        // offers screen content
        // deals screen
        AnimatedVisibility(
            modifier = Modifier.padding(innerpadding),
            visible = state.selectedTab == 0,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            Column(
                modifier = Modifier.fillMaxSize()

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
                            if (state.isDealsSearchBarActive) 0.dp else searchbarPaddingAnimation.value
                        ),
                    query = state.dealsSearchBarText, onQueryChange = {
                        viewModel.onEvent(DealsAndGiveawayScreenUiEvent.OnSearchBarTextChanged(it))
                    }, onSearch = { searchQuery ->
                        searchQuery.let {
                            viewModel.onEvent(
                                DealsAndGiveawayScreenUiEvent.OnSearchBarTextSubmit(it)
                            )
                            // hide keyboard
                            keyboardController?.hide()
                        }
                    }, active = state.isDealsSearchBarActive, onActiveChange = {
                        viewModel.onEvent(DealsAndGiveawayScreenUiEvent.OnToggleSearchBar(it))
                    }, leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Favorite"
                        )
                    }, trailingIcon = {
                        if (state.isDealsSearchBarActive) {
                            IconButton(onClick = {
                                if (state.dealsSearchBarText.isNotEmpty()) {
                                    viewModel.onEvent(
                                        DealsAndGiveawayScreenUiEvent.OnSearchBarTextChanged(
                                            ""
                                        )
                                    )
                                } else {
                                    viewModel.onEvent(
                                        DealsAndGiveawayScreenUiEvent.OnToggleSearchBar(
                                            false
                                        )
                                    )
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close"
                                )
                            }
                        }
                    }
                ) {
                    // TODO:change the status bar color to be surface container color
                    // Search bar content
                    if (state.dealsSearchBarText.isEmpty() || state.dealsSearchBarText.isBlank()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .imePadding()
                                .imeNestedScroll()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Recent Searches",
                                style = MaterialTheme.typography.bodySmall
                            )
                            state.dealsSearchHistory?.forEach { deal ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.onEvent(
                                                DealsAndGiveawayScreenUiEvent.OnSearchBarTextChanged(
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
                                        text = deal
                                    )
                                }
                            }
                        }
                    }

                    if (state.isDealsSearchBarLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    state.dealsSearchBardata?.let {
                        LazyColumn() {
                            if (state.dealsSearchBardata.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "Found ${state.dealsSearchBardata.size} Search results !",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .align(
                                                Alignment.CenterHorizontally
                                            )
                                    )
                                }
                            }
                            items(it, key = { it.dealID }) { deal ->
                                DealsListItem(
                                    modifier = Modifier.animateItem(),
                                    onClickListener = {
                                        openUrl(
                                            context,
                                            "https://www.cheapshark.com/redirect?dealID=${deal.dealID}"
                                        )
                                    },
                                    imageLink = deal.thumb,
                                    string1 = deal.title,
                                    string2 = deal.salePrice + "$",
                                    string3LineThrough = "${deal.normalPrice}$",
                                    string4 = "${deal.savings}% off"
                                )
                            }
                        }
                    }
                }
                if (state.isDealsLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ShimmeringText(
                            text = "Loading Deals",
                            shimmerColor = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier.testTag("deals_list")
                ) {
                    items(state.deals!!, key = { it.dealID }) { deal ->
                        DealsListItem(
                            modifier = Modifier.animateItem(),
                            onClickListener = {
                                openUrl(
                                    context,
                                    "https://www.cheapshark.com/redirect?dealID=${deal.dealID}"
                                )
                            },
                            imageLink = deal.thumb,
                            string1 = deal.title,
                            string2 = deal.salePrice + "$",
                            string3LineThrough = "${deal.normalPrice}$",
                            string4 = "${deal.savings}% off"
                        )
                    }
                }
            }
        }

        // giveaways screen
        AnimatedVisibility(
            modifier = Modifier.padding(innerpadding),
            visible = state.selectedTab == 1,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            if (state.isGiveawaysLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ShimmeringText(
                        text = "Loading Giveaways",
                        shimmerColor = MaterialTheme.colorScheme.primary
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.testTag("giveaways_list")
            ) {
                items(state.giveaways!!, key = { it.id }) { giveaway ->
                    GiveawayListItem(
                        modifier = Modifier.animateItem(),
                        onClickListener = {
                            openUrl(context, giveaway.open_giveaway_url)
                        },
                        imageLink = giveaway.thumbnail,
                        title = giveaway.title,
                        shortDescription = giveaway.description,
                        timeRemaining = giveaway.end_date,
                        users = giveaway.users
                    )
                }
            }

            // TODO: add filters by shop , onSale , sortby
        }
        if (state.isBottomSheetVisible) {
            val listOfSortByOptions = listOf(
                "DealRating",
                "Title",
                "Savings",
                "Price",
                "Metacritic",
                "Reviews",
                "Release",
                "Store",
                "Recent"
            )
            ModalBottomSheet(
                onDismissRequest = {
                    viewModel.onEvent(DealsAndGiveawayScreenUiEvent.OnToggleBottomSheet(false))
                },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Filtering options",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            top = 8.dp,
                            bottom = 8.dp
                        )
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Sort by: ",
                            style = MaterialTheme.typography.titleSmall,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .weight(1f)
                        )

                        Spacer(modifier = Modifier.weight(1f))
                        Text("Descending", modifier = Modifier.padding(end = 8.dp))
                        Switch(
                            state.filterDesc ?: false,
                            onCheckedChange = {
                                viewModel.onEvent(
                                    DealsAndGiveawayScreenUiEvent.OnDescFilterChanged(
                                        it
                                    )
                                )
                            }
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOfSortByOptions.forEachIndexed { index, it ->
                            FilterChip(
                                onClick = {
                                    if (state.filterSortBy == it) {
                                        viewModel.onEvent(
                                            DealsAndGiveawayScreenUiEvent.OnSortByFilterChanged(
                                                ""
                                            )
                                        )
                                    } else {
                                        viewModel.onEvent(
                                            DealsAndGiveawayScreenUiEvent.OnSortByFilterChanged(
                                                it
                                            )
                                        )
                                    }
                                },
                                label = {
                                    Text(it)
                                },
                                selected = state.filterSortBy == it,
                                leadingIcon = if (state.filterSortBy == it) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = "Done icon",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null
                                }
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(
                            top = 8.dp,
                            bottom = 8.dp
                        )
                    )

                    val store = listOf(
                        1 to "Steam",
                        2 to "GamersGate",
                        3 to "GreenManGaming",
                        4 to "Amazon",
                        5 to "GameStop",
                        6 to "Direct2Drive",
                        7 to "GOG",
                        8 to "Origin",
                        9 to "Get Games",
                        10 to "Shiny Loot",
                        11 to "Humble Store",
                        12 to "Desura",
                        13 to "Uplay",
                        14 to "IndieGameStand",
                        15 to "Fanatical",
                        16 to "Gamesrocket",
                        17 to "Games Republic",
                        18 to "SilaGames",
                        19 to "Playfield",
                        20 to "ImperialGames",
                        21 to "WinGameStore",
                        22 to "FunStockDigital",
                        23 to "GameBillet",
                        24 to "Voidu",
                        25 to "Epic Games Store",
                        26 to "Razer Game Store",
                        27 to "Gamesplanet",
                        28 to "Gamesload",
                        29 to "2Game",
                        30 to "IndieGala",
                        31 to "Blizzard Shop",
                        32 to "AllYouPlay",
                        33 to "DLGamer",
                        34 to "Noctre",
                        35 to "DreamGame"
                    )
                    Text(
                        text = "Store: ",
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        store.forEach { (id, name) ->
                            FilterChip(
                                onClick = {
                                    if (state.filterStoreId == id.toString()) {
                                        viewModel.onEvent(
                                            DealsAndGiveawayScreenUiEvent.OnStoreFilterChanged(
                                                ""
                                            )
                                        )
                                    } else {
                                        viewModel.onEvent(
                                            DealsAndGiveawayScreenUiEvent.OnStoreFilterChanged(
                                                id.toString()
                                            )
                                        )
                                    }
                                },
                                label = {
                                    Text(name)
                                },
                                selected = state.filterStoreId == id.toString(),
                                leadingIcon = if (state.filterStoreId == id.toString()) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = "Done icon",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null
                                }
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(
                            top = 8.dp,
                            bottom = 8.dp
                        )
                    )
                    Text(
                        text = "Price: ",
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    // range slider
                    RangeSlider(
                        modifier = Modifier.padding(8.dp),
                        value = (
                            state.filterLowerPrice
                                ?: 0f
                            )..(state.filterUpperPrice ?: 50f),
                        onValueChange = { range ->
                            viewModel.onEvent(
                                DealsAndGiveawayScreenUiEvent.OnLowerPriceFilterChanged(
                                    range.start.toInt()
                                )
                            )
                            viewModel.onEvent(
                                DealsAndGiveawayScreenUiEvent.OnUpperPriceFilterChanged(
                                    range.endInclusive.toInt()
                                )
                            )
                        },
                        valueRange = 0f..50f,
                        steps = 50
                    )
                    val rangeStart = "%.2f".format(state.filterLowerPrice ?: 0f)
                    val rangeEnd = "%.2f".format(state.filterUpperPrice ?: 50f)
                    Text(
                        text = "$rangeStart .. $rangeEnd",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            top = 8.dp,
                            bottom = 8.dp
                        )
                    )
                    // filter games onSale
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "On Sale Games :",
                            style = MaterialTheme.typography.titleSmall,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(state.filterOnSale ?: false, onCheckedChange = {
                            viewModel.onEvent(
                                DealsAndGiveawayScreenUiEvent.OnOnSaleFilterChanged(it)
                            )
                        })
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            top = 8.dp,
                            bottom = 8.dp
                        )
                    )
                    Button(
                        onClick = {
                            viewModel.onEvent(DealsAndGiveawayScreenUiEvent.OnApplyDealsFilters)
                            viewModel.onEvent(
                                DealsAndGiveawayScreenUiEvent.OnToggleBottomSheet(false)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Apply Filters"
                        )
                    }
                }
            }
        }
        if (state.isGiveawaysBottomSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    viewModel.onEvent(
                        DealsAndGiveawayScreenUiEvent.OnToggleGiveawaysBottomSheet(
                            false
                        )
                    )
                },
                sheetState = sheetStateGiveaways
            ) {
                val giveawaysPlatform = listOf(
                    "pc",
                    "steam",
                    "epic-games-store",
                    "ubisoft",
                    "gog",
                    "itchio",
                    "ps4",
                    "ps5",
                    "xbox-one",
                    "xbox-series-xs",
                    "switch",
                    "android",
                    "ios",
                    "vr",
                    "battlenet",
                    "origin",
                    "drm-free",
                    "xbox-360"

                )
                val giveawaysSortOptions = listOf(
                    "date",
                    "value",
                    "popularity"
                )
                val giveawaysType = listOf(
                    "game",
                    "loot",
                    "beta"
                )

                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Filtering options",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            top = 8.dp,
                            bottom = 8.dp
                        )
                    )

                    Text(
                        text = "Sort by: ",
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        giveawaysSortOptions.forEachIndexed { index, it ->
                            FilterChip(
                                onClick = {
                                    if (state.giveawaysFilterSortBy == it) {
                                        viewModel.onEvent(
                                            DealsAndGiveawayScreenUiEvent.OnGiveawaysSortByFilterChanged(
                                                null
                                            )
                                        )
                                    } else {
                                        viewModel.onEvent(
                                            DealsAndGiveawayScreenUiEvent.OnGiveawaysSortByFilterChanged(
                                                it
                                            )
                                        )
                                    }
                                },
                                label = {
                                    Text(it.lowercase().capitalize().replace("_", " "))
                                },
                                selected = state.giveawaysFilterSortBy == it,
                                leadingIcon = if (state.giveawaysFilterSortBy == it) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = "Done icon",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null
                                }
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(
                            top = 8.dp,
                            bottom = 8.dp
                        )
                    )

                    Text(
                        text = "Type: ",
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        giveawaysType.forEach { it ->
                            FilterChip(
                                onClick = {
                                    if (state.giveawaysFilterType == it) {
                                        viewModel.onEvent(
                                            DealsAndGiveawayScreenUiEvent.OnGiveawaysTypeFilterChanged(
                                                null
                                            )
                                        )
                                    } else {
                                        viewModel.onEvent(
                                            DealsAndGiveawayScreenUiEvent.OnGiveawaysTypeFilterChanged(
                                                it
                                            )
                                        )
                                    }
                                },
                                label = {
                                    Text(it.lowercase().capitalize().replace("_", " "))
                                },
                                selected = state.giveawaysFilterType == it,
                                leadingIcon = if (state.giveawaysFilterType == it) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = "Done icon",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null
                                }
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            top = 8.dp,
                            bottom = 8.dp
                        )
                    )
                    Text(
                        text = "Platform: ",
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        giveawaysPlatform.forEach { it ->
                            FilterChip(
                                onClick = {
                                    if (state.giveawaysFilterPlatform == it) {
                                        viewModel.onEvent(
                                            DealsAndGiveawayScreenUiEvent.OnGiveawaysPlatformFilterChanged(
                                                null
                                            )
                                        )
                                    } else {
                                        viewModel.onEvent(
                                            DealsAndGiveawayScreenUiEvent.OnGiveawaysPlatformFilterChanged(
                                                it
                                            )
                                        )
                                    }
                                },
                                label = {
                                    Text(it.lowercase().capitalize().replace("_", " "))
                                },
                                selected = state.giveawaysFilterPlatform == it,
                                leadingIcon = if (state.giveawaysFilterPlatform == it) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = "Done icon",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null
                                }
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            top = 8.dp,
                            bottom = 8.dp
                        )
                    )
                    Button(
                        onClick = {
                            viewModel.onEvent(DealsAndGiveawayScreenUiEvent.OnApplyGiveawaysFilters)
                            viewModel.onEvent(
                                DealsAndGiveawayScreenUiEvent.OnToggleGiveawaysBottomSheet(false)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Apply Filters"
                        )
                    }
                }
            }
        }
    }
}
