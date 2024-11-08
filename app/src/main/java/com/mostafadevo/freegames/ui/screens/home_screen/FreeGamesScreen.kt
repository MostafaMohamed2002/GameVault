package com.mostafadevo.freegames.ui.screens.home_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mostafadevo.freegames.ui.components.FilterIcon
import com.mostafadevo.freegames.ui.components.FreeGameListItem
import com.mostafadevo.freegames.ui.components.ShimmeringText
import java.util.Locale
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FreeGamesScreen(
    navController: NavController,
    freeGamesScreenViewModel: FreeGamesScreenViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state by freeGamesScreenViewModel.uiState.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // this fixes the issue of the sheet not expanding fully
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = {
                showBottomSheet = true
            }
        ) {
            Icon(
                imageVector = FilterIcon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }, snackbarHost = {
            SnackbarHost(snackbarHostState)
        }, topBar = {}, content = {
            LaunchedEffect(key1 = Unit) {
                freeGamesScreenViewModel.uiEffect.collectLatest {
                    when (it) {
                        is FreeGamesScreenUiEffect.ShowSnackbar -> {
                            snackbarHostState.showSnackbar(it.message)
                        }
                    }
                }
            }
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ShimmeringText(
                        text = "Loading...",
                        shimmerColor = MaterialTheme.colorScheme.primary
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.testTag("free_games_list"),
                contentPadding = PaddingValues(8.dp),
                content = {
                    items(state.games, key = { it.id }) { game ->
                        FreeGameListItem(
                            modifier = Modifier.animateItem().testTag("free_game"),
                            game = game,
                            onClickListener = {
                                navController.navigate("/details/$it")
                            }
                        )
                    }
                }
            )
            if (showBottomSheet) {
                val sortByOptions = listOf(
                    "release-date",
                    "popularity",
                    "alphabetical ",
                    "relevance"
                )
                val platformOptions = listOf(
                    "pc",
                    "browser",
                    "all"
                )
                val gameGenres = listOf(
                    "mmorpg", "shooter", "strategy", "moba", "racing", "sports", "social",
                    "sandbox", "open-world", "survival", "pvp", "pve", "pixel", "voxel",
                    "zombie", "turn-based", "first-person", "third-person", "top-down", "tank",
                    "space", "sailing", "side-scroller", "superhero", "permadeath", "card",
                    "battle-royale", "mmo", "mmofps", "mmotps", "3d", "2d", "anime", "fantasy",
                    "sci-fi", "fighting", "action-rpg", "action", "military", "martial-arts",
                    "flight", "low-spec", "tower-defense", "horror", "mmorts"
                )

                ModalBottomSheet(
                    modifier = Modifier,
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ) {
                    // Sheet content
                    Text(
                        text = "Filtering options",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(8.dp)
                    )
                    // sort by section
                    Text(
                        text = "Sort by: ",
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        sortByOptions.forEach { item ->
                            FilterChip(
                                selected = state.sortBy == item,
                                modifier = Modifier.padding(8.dp),
                                onClick = {
                                    if (state.sortBy == item) {
                                        freeGamesScreenViewModel.onEvent(
                                            FreeGamesScreenEvents.onSortBySelected(null)
                                        )
                                    } else {
                                        freeGamesScreenViewModel.onEvent(
                                            FreeGamesScreenEvents.onSortBySelected(item)
                                        )
                                    }
                                },
                                label = {
                                    Text(
                                        item.lowercase().replace("-", " ").capitalize(Locale.ROOT)
                                    )
                                },
                                leadingIcon = {
                                    // TODO: replace with animatedvisibility
                                    if (state.sortBy == item) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null
                                        )
                                    }
                                }
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(8.dp)
                    )
                    // platform section
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        platformOptions.forEach { item ->
                            FilterChip(
                                selected = state.platform == item,
                                modifier = Modifier.padding(8.dp),
                                onClick = {
                                    if (state.platform == item) {
                                        freeGamesScreenViewModel.onEvent(
                                            FreeGamesScreenEvents.onPlatformSelected(null)
                                        )
                                    } else {
                                        freeGamesScreenViewModel.onEvent(
                                            FreeGamesScreenEvents.onPlatformSelected(item)
                                        )
                                    }
                                },
                                label = { Text(item.capitalize(Locale.ROOT).replace("-", " ")) },
                                leadingIcon = {
                                    // TODO: replace with animatedvisibility
                                    if (state.platform == item) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null
                                        )
                                    }
                                }
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(8.dp)
                    )
                    // category section with exposed dropdown menu
                    var expanded by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ExposedDropdownMenuBox(modifier = Modifier.weight(2f), expanded = expanded, onExpandedChange = {
                            expanded = it
                        }) {
                            OutlinedTextField(
                                modifier =
                                Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = expanded
                                    )
                                },
                                value = state.category?.capitalize(Locale.ROOT)?.replace("-", " ") ?: "Select Category",
                                onValueChange = {},
                                readOnly = true,
                                singleLine = true,
                                label = {
                                    if (state.category != null) {
                                        Text("Category")
                                    }
                                },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                gameGenres.forEach {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = it.capitalize(Locale.ROOT).replace("-", " ")
                                            )
                                        },
                                        onClick = {
                                            freeGamesScreenViewModel.onEvent(
                                                FreeGamesScreenEvents.onCategorySelected(it)
                                            )
                                            expanded = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                    )
                                }
                            }
                        }
                        IconButton(modifier = Modifier.weight(0.5f), onClick = {
                            freeGamesScreenViewModel.onEvent(
                                FreeGamesScreenEvents.onCategorySelected(null)
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(8.dp)
                    )
                    Button(
                        onClick = {
                            freeGamesScreenViewModel.onEvent(
                                FreeGamesScreenEvents.onSearchWithFilters
                            )
                            showBottomSheet = false
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = "Search With Applied Filters",
                            style = MaterialTheme.typography.titleSmall,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        })
}
