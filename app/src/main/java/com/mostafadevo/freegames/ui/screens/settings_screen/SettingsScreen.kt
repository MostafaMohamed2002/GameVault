package com.mostafadevo.freegames.ui.screens.settings_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mostafadevo.freegames.domain.model.ThemePreference

@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    val themePreference = viewModel.themeState.collectAsStateWithLifecycle()
    val dynamicTheme = viewModel.dynamicThemeState.collectAsStateWithLifecycle()
    val searchHistoryLimit = viewModel.searchHistoryLimitState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = "App Settings",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )
        ThemeSwitcherWithDialog(
            themePreference = themePreference.value,
            onLightClick = {
                viewModel.onEvent(SettingsScreenEvent.SaveThemePreference(ThemePreference.LIGHT))
            },
            onDarkClick = {
                viewModel.onEvent(SettingsScreenEvent.SaveThemePreference(ThemePreference.DARK))
            },
            onSystemClick = {
                viewModel.onEvent(SettingsScreenEvent.SaveThemePreference(ThemePreference.SYSTEM))
            }
        )
        Spacer(modifier = Modifier.padding(8.dp))
        DynamicColorsSwitcher(
            checked = dynamicTheme.value,
            onClick = {
                viewModel.onEvent(
                    SettingsScreenEvent.SaveDynamicThemePreference(!dynamicTheme.value)
                )
            }
        )
        Spacer(modifier = Modifier.padding(8.dp))
        ClearSearchHistoryButton(
            onClick = {
                viewModel.onEvent(SettingsScreenEvent.ClearSearchHistory)
            }
        )
        Spacer(modifier = Modifier.padding(8.dp))
        SearchHistoryLimitCounter(
            limit = searchHistoryLimit.value,
            onLimitChange = {
                viewModel.onEvent(SettingsScreenEvent.ChangeSearchHistoryLimit(it))
            }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchHistoryLimitCounter(limit: Int, onLimitChange: (Int) -> Unit) {
    val animatedLimit by animateIntAsState(targetValue = limit)

    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                "Search History Limit :",
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    onLimitChange(limit - 1)
                }
            ) {
                Text("-")
            }
            AnimatedContent(
                targetState = limit,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInVertically { -it } with slideOutVertically { it }
                    } else {
                        slideInVertically { it } with slideOutVertically { -it }
                    }
                }
            ) { count ->
                Text(
                    "$count",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp)
                )
            }
            IconButton(
                onClick = {
                    onLimitChange(limit + 1)
                }
            ) {
                Text("+")
            }
        }
    }
}

@Preview
@Composable
fun PreviewSearchHistoryLimit() {
    SearchHistoryLimitCounter(
        limit = 6,
        onLimitChange = {}
    )
}

@PreviewLightDark
@Composable
fun ClearSearchHistoryButton(onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                "Clear Search History \uD83E\uDDF9",
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "Clear",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onClick()
                    },
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview
@Composable
fun ThemeSwitcherWithDialog(
    themePreference: ThemePreference = ThemePreference.SYSTEM,
    onLightClick: () -> Unit = {},
    onDarkClick: () -> Unit = {},
    onSystemClick: () -> Unit = {}
) {
    val isDialogOpen = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
    ) {
        Text(
            "Theme :",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        LabeledRadioButton(
            label = "Light ☀️",
            selected = themePreference.ordinal == ThemePreference.LIGHT.ordinal,
            onClick = {
                onLightClick()
            }
        )
        LabeledRadioButton(
            label = "Dark 🌘",
            selected = themePreference.ordinal == ThemePreference.DARK.ordinal,
            onClick = {
                onDarkClick()
            }
        )
        LabeledRadioButton(
            label = "System 🤖",
            selected = themePreference.ordinal == ThemePreference.SYSTEM.ordinal,
            onClick = {
                onSystemClick()
            }
        )
    }
}

@Composable
fun LabeledRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(label)
    }
}

@Preview
@Composable
fun DynamicColorsSwitcher(
    checked: Boolean = true,
    onClick: () -> Unit = {}
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Dynamic Colors"
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = checked,
                onCheckedChange = {
                    onClick()
                }
            )
        }
    }
}
