package com.mostafadevo.freegames.ui.screens.home_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mostafadevo.freegames.ui.components.BlurredBox
import com.mostafadevo.freegames.utils.extractDominantColor
import com.mostafadevo.freegames.utils.loadImage
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FreeGamesScreen(
    navController: NavController,
    freeGamesScreenViewModel: FreeGamesScreenViewModel = hiltViewModel()
) {
    println("freegamescreenviewmodel is ${freeGamesScreenViewModel.hashCode()}")
    val snackbarHostState = remember { SnackbarHostState() }
    val state by freeGamesScreenViewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    Scaffold(floatingActionButton = {
        Button (
            onClick = {
                freeGamesScreenViewModel.refreshGames()
            },
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.padding(8.dp)
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
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }
        LazyColumn(contentPadding = PaddingValues(8.dp), content = {
            items(state.games) { game ->
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
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            BlurredBox (
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = game.platform,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(8.dp).basicMarquee()
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            BlurredBox (
                                modifier = Modifier.weight(1f)
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
        })


    })
}
