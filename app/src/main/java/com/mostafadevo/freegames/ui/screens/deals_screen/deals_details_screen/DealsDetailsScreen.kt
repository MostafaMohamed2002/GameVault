package com.mostafadevo.freegames.ui.screens.deals_screen.deals_details_screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.mostafadevo.freegames.ui.components.BlurredBox
import com.mostafadevo.freegames.utils.extractDominantColor
import com.mostafadevo.freegames.utils.loadImage
import com.mostafadevo.freegames.utils.toFormattedDate

@Composable
fun DealsDetailsScreen(
    dealId: String, viewModel: DealsDetailsScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    var dominantColor by remember { mutableStateOf(Color.Transparent) }
    val context = LocalContext.current
    Scaffold { innerPadding ->
        LaunchedEffect(Unit) {
            viewModel.getDealDetails(dealId)
        }
        LaunchedEffect(uiState.deal?.gameInfo?.gameID) {
            val drawable = uiState.deal?.gameInfo?.let { loadImage(context, it.thumb) }
            val extractedColor = drawable?.let { imgDrawable -> extractDominantColor(imgDrawable) }
                ?: Color.Transparent
            dominantColor = extractedColor
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, dominantColor)
                    )
                ).verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = uiState.deal?.gameInfo?.thumb,
                contentDescription = null,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                //title
                Text(
                    text = uiState.deal?.gameInfo?.name ?: "",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .weight(2f)
                )
                //get the game button
                Box(
                    modifier = Modifier
                        .border(
                            2.dp, MaterialTheme.colorScheme.inversePrimary, RoundedCornerShape(8.dp)
                        )
                        .weight(1f)
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data =
                                Uri.parse("https://www.cheapshark.com/redirect?dealID=${dealId}")
                            context.startActivity(intent)
                        }, contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Get",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(8.dp)

                    )
                }
                //release date
            }
            BlurredBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = "ReleaseDate: \n${uiState.deal?.gameInfo?.releaseDate?.toFormattedDate()}",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(8.dp)
                )
            }
            BlurredBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    //retail price with line through
                    Text(
                        text = "Retail Price: ${uiState.deal?.gameInfo?.retailPrice}$",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(8.dp),
                        textDecoration = LineThrough
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    //sale price
                    Text(
                        text = "Sale Price: ${uiState.deal?.gameInfo?.salePrice}$",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            //metacritic score
            BlurredBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = "Metacritic Score: ${uiState.deal?.gameInfo?.metacriticScore}",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
