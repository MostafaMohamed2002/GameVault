package com.mostafadevo.freegames.ui.screens.detailes_screen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import com.mostafadevo.freegames.ui.components.BlurredBox
import com.mostafadevo.freegames.utils.extractDominantColor
import com.mostafadevo.freegames.utils.getContrastingTextColor
import com.mostafadevo.freegames.utils.loadImage
import com.mostafadevo.freegames.utils.openUrl

// TODO: this should be refactored
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FreeGameDetailesScreen(
    gameId: Int,
    viewModel: FreeGameDetailesViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var backgroundColor by remember { mutableStateOf(Color.Transparent) }
    var buttonBackground by remember { mutableStateOf(Color.Transparent) }
    val isReadMoreEnabled = remember { mutableStateOf(false) }
    val isRequirementsEnabled = remember { mutableStateOf(false) }
    val pagerState = rememberPagerState { state.game.screenshots.size }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.getGameById(gameId)
    }
    val animtedTextColor = remember {
        getContrastingTextColor(
            backgroundColor = backgroundColor
        )
    }
    Scaffold(snackbarHost = {
        SnackbarHost(
            hostState = snackbarHostState
        )
    }, floatingActionButton = {}, content = {
            LaunchedEffect(Unit) {
                viewModel.uiEffect.collect { effect ->
                    when (effect) {
                        is FreeGameDetailesUiEffect.ShowSnackBar -> {
                            snackbarHostState.showSnackbar(effect.message)
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .testTag("FreeGameDetailesScreen")
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.surfaceContainer,
                                backgroundColor
                            )
                        )
                    )
            ) {
                if (state.isLoading) {
                    // TODO: replace with shimmer text
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    LaunchedEffect(state.game.thumbnail) {
                        val drawable = loadImage(context, state.game.thumbnail)
                        backgroundColor =
                            drawable?.let { imgDrawable -> extractDominantColor(imgDrawable) }
                                ?: Color.Transparent
                        drawable?.let {
                            val bitmapimg = (drawable as BitmapDrawable).bitmap.copy(
                                Bitmap.Config.ARGB_8888,
                                true
                            )
                            Palette.from(bitmapimg).generate { palette ->
                                if (palette != null) {
                                    buttonBackground = palette.lightMutedSwatch.let { it1 ->
                                        it1?.let { it2 ->
                                            Color(
                                                it2.rgb
                                            )
                                        }
                                    } ?: Color.Transparent
                                }
                            }
                        }
                    }
                    AsyncImage(
                        model = state.game.thumbnail,
                        contentDescription = state.game.title,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        // title
                        Text(
                            text = state.game.title,
                            color = animtedTextColor,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .weight(2f)
                        )
                        // get the game button
                        Box(
                            modifier = Modifier
                                .border(
                                    2.dp,
                                    MaterialTheme.colorScheme.inversePrimary,
                                    RoundedCornerShape(8.dp)
                                )
                                .weight(1f)
                                .clickable {
                                    openUrl(
                                        context = context,
                                        url = state.game.freetogameProfileUrl
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Get",
                                color = animtedTextColor,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(8.dp)

                            )
                        }
                    }
                    // minimum requirements
                    Row {
                        Text(
                            text = "Minimum System Reuirements:",
                            color = animtedTextColor,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .weight(2f)
                        )
                        // read more button
                        IconButton(onClick = {
                            isRequirementsEnabled.value = isRequirementsEnabled.value.not()
                        }) {
                            Icon(
                                imageVector = if (isRequirementsEnabled.value.not()) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                                contentDescription = "Read more"
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = isRequirementsEnabled.value,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Text(
                            text = "os :${state.game.minimumSystemRequirements.os}\n" +
                                "processor :${state.game.minimumSystemRequirements.processor}\n" +
                                "memory :${state.game.minimumSystemRequirements.memory}\n" +
                                "graphics :${state.game.minimumSystemRequirements.graphics}\n" +
                                "storage :${state.game.minimumSystemRequirements.storage}",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(8.dp),
                            color = animtedTextColor
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // about the game
                    Row {
                        Text(
                            text = "About the game:",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .weight(2f),
                            color = animtedTextColor
                        )
                        // read more button
                        IconButton(onClick = {
                            isReadMoreEnabled.value = isReadMoreEnabled.value.not()
                        }) {
                            Icon(
                                imageVector = if (isReadMoreEnabled.value.not()) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                                contentDescription = "Read more"
                            )
                        }
                    }
                    // short description
                    Text(
                        text = state.game.shortDescription,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        color = animtedTextColor
                    )

                    // description
                    AnimatedVisibility(
                        visible = isReadMoreEnabled.value,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Text(
                            text = state.game.description,
                            color = animtedTextColor,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    // platform and genre
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BlurredBox(
                            modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp))
                        ) {
                            Text(
                                text = "Platform: \n${state.game.platform}",
                                color = animtedTextColor,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        BlurredBox(
                            modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp))
                        ) {
                            Text(
                                text = "Genre: \n${state.game.genre}",
                                color = animtedTextColor,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                    // developer and release date
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BlurredBox(
                            modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp))
                        ) {
                            Text(
                                text = "Developer: \n${state.game.developer}",
                                style = MaterialTheme.typography.labelSmall,
                                color = animtedTextColor,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .then(
                                        if (state.game.developer.lines().size > 1) {
                                            Modifier.basicMarquee()
                                        } else {
                                            Modifier
                                        }
                                    )
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        BlurredBox(
                            modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp))
                        ) {
                            Text(
                                text = "ReleaseDate: \n${state.game.releaseDate}",
                                color = animtedTextColor,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    // screenshots
                    Text(
                        text = "Screenshots:",
                        color = animtedTextColor,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    )
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .wrapContentHeight() // Adjust height as needed
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        verticalAlignment = Alignment.Top,
                        pageSpacing = 8.dp
                    ) { page ->

                        val pageOffset =
                            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                        val imageSize by animateFloatAsState(
                            targetValue = if (pageOffset != 0f) 0.75f else 1f,
                            animationSpec = tween(300)
                        )
                        AsyncImage(
                            model = state.game.screenshots[page].image,
                            contentDescription = "Screenshot $page",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .graphicsLayer {
                                    scaleX = imageSize
                                    scaleY = imageSize
                                }
                        )
                    }
                }
            }
        })
}
