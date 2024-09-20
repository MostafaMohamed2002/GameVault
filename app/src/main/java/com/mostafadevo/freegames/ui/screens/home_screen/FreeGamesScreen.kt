package com.mostafadevo.freegames.ui.screens.home_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedIconButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mostafadevo.freegames.ui.components.BlurredBox
import com.mostafadevo.freegames.ui.components.FilterIcon
import com.mostafadevo.freegames.utils.extractDominantColor
import com.mostafadevo.freegames.utils.loadImage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FreeGamesScreen(
    navController: NavController,
    freeGamesScreenViewModel: FreeGamesScreenViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state by freeGamesScreenViewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true //this fixes the issue of the sheet not expanding fully
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(floatingActionButton = {
        FloatingActionButton  (
            onClick = {
                showBottomSheet = true
            },
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
            // TODO: replace with shimmer text
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }
        LazyColumn(contentPadding = PaddingValues(8.dp),
            content = {
            items(state.games, key = {it.id}) { game ->
                var dominantColor by remember { mutableStateOf<Color>(Color.Transparent) }

                LaunchedEffect(game.thumbnail) {
                    if (freeGamesScreenViewModel.colorCache.get(game.thumbnail) != null) {
                        dominantColor = freeGamesScreenViewModel.colorCache[game.thumbnail]!!
                    } else {
                        val drawable = loadImage(context, game.thumbnail)
                        val extractedColor =
                            drawable?.let { imgDrawable -> extractDominantColor(imgDrawable) }
                                ?: Color.Transparent
                        dominantColor = extractedColor
                        freeGamesScreenViewModel.colorCache.put(game.thumbnail, extractedColor)
                    }
                }
                var onImageLoading by remember { mutableStateOf(true) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surface, dominantColor
                                ), tileMode = TileMode.Clamp
                            )
                        )
                        .animateItem()
                        .clickable {
                            navController.navigate("/details/${game.id}")
                        }, verticalAlignment = Alignment.CenterVertically,

                ) {
                    Box {
                        AsyncImage(model = game.thumbnail,
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            onSuccess = {
                                onImageLoading = false
                            },
                            onError = {
                                onImageLoading = false
                            })
                        if (onImageLoading) {
                            CircularProgressIndicator(
                                color = Color.Blue,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(8.dp)
                            )
                        }
                    }
                    Column {
                        Text(
                            text = game.title,
                            style = MaterialTheme.typography.labelMedium,
                            fontSize = 16.sp,
                            maxLines = 1,
                            modifier = Modifier
                                .padding(8.dp)
                                .basicMarquee()
                        )
                        Text(
                            text = game.shortDescription,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 14.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(8.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            BlurredBox (
                                modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp))
                            ) {
                                Text(
                                    text = game.platform,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .basicMarquee()
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            BlurredBox (
                                modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp))
                            ) {
                                Text(
                                    text = game.genre,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(8.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,

                                    )
                            }
                        }

                    }
                }
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
                //sort by section
                Text(
                    text = "Sort by: ",
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                    , verticalAlignment = Alignment.CenterVertically
                ) {
                    sortByOptions.forEach { item->
                        FilterChip(
                            selected = state.sortBy == item,
                            modifier =  Modifier.padding(8.dp),
                            onClick = {
                                if (state.sortBy == item) {
                                    freeGamesScreenViewModel.onEvent(FreeGamesScreenEvents.onSortBySelected(null))
                                }else{
                                    freeGamesScreenViewModel.onEvent(FreeGamesScreenEvents.onSortBySelected(item))
                                }
                            },
                            label = { Text(item) },
                            leadingIcon = {
                                // TODO: replace with animatedvisibility
                                if (state.sortBy == item) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                    )
                                }
                            },
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(8.dp)
                )
                //platform section
                Text(
                    text = "Platform: ",
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                    , verticalAlignment = Alignment.CenterVertically
                ) {
                    platformOptions.forEach { item->
                        FilterChip(
                            selected = state.platform == item,
                            modifier =  Modifier.padding(8.dp),
                            onClick = {
                                if (state.platform == item) {
                                    freeGamesScreenViewModel.onEvent(FreeGamesScreenEvents.onPlatformSelected(null))
                                }else{
                                    freeGamesScreenViewModel.onEvent(FreeGamesScreenEvents.onPlatformSelected(item))
                                }
                            },
                            label = { Text(item) },
                            leadingIcon = {
                                // TODO: replace with animatedvisibility
                                if (state.platform == item) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                    )
                                }
                            }
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(8.dp)
                )
                //category section with exposed dropdown menu
                var expanded by remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ExposedDropdownMenuBox(modifier = Modifier.weight(2f),expanded = expanded, onExpandedChange = {
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
                                    expanded = expanded,
                                )
                            },
                            value = state.category ?: "Select Category",
                            onValueChange = {},
                            readOnly = true,
                            singleLine = true,
                            label = {
                                if (state.category != null) {
                                    Text("Category")
                                }
                            },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            gameGenres.forEach {
                                DropdownMenuItem(
                                    text = { Text(text = it) },
                                    onClick = {
                                        freeGamesScreenViewModel.onEvent(FreeGamesScreenEvents.onCategorySelected(it))
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                            }
                        }
                    }
                    IconButton(modifier = Modifier.weight(0.5f),onClick = {
                        freeGamesScreenViewModel.onEvent(FreeGamesScreenEvents.onCategorySelected(null))
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
                ElevatedButton(
                    onClick = {
                        freeGamesScreenViewModel.onEvent(FreeGamesScreenEvents.onSearchWithFilters)
                        showBottomSheet = false
                    }
                    , modifier = Modifier
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
