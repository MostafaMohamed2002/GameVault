package com.mostafadevo.freegames.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAnalyticsImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : GameVaultAnalytics {
    override fun logEvent(name: String, params: Bundle?) {
        firebaseAnalytics.logEvent(name, params)
    }

    override fun logFreeGamesScreenViewed() {
        logEvent("free_games_screen_viewed")
    }

    override fun logFreeGameCardClicked(gameId: Int, gameTitle: String, platform: String) {
        val b = Bundle().apply {
            putInt("game_id", gameId)
            putString("game_title", gameTitle)
            putString("platform", platform)
        }
        logEvent("free_game_card_clicked", b)
    }

    override fun logFreeGamesFilterApplied(platform: String?, genre: String?, sortBy: String?) {
        val b = Bundle().apply {
            putString("platform", platform)
            putString("genre", genre)
            putString("sort_by", sortBy)
        }
        logEvent("free_games_filter_applied", b)
    }

    override fun logFreeGamesRefreshed() {
        logEvent("free_games_refreshed")
    }

    override fun logDealsScreenViewed(activeTab: String) {
        val b = Bundle().apply { putString("active_tab", activeTab) }
        logEvent("deals_screen_viewed", b)
    }

    override fun logDealCardClicked(gameTitle: String, store: String, discountPercent: Int?, salePrice: Double?) {
        val b = Bundle().apply {
            putString("game_title", gameTitle)
            putString("store", store)
            discountPercent?.let { putInt("discount_percent", it) }
            salePrice?.let { putDouble("sale_price", it) }
        }
        logEvent("deal_card_clicked", b)
    }

    override fun logGiveawayCardClicked(giveawayTitle: String, platform: String, hoursRemaining: Long?) {
        val b = Bundle().apply {
            putString("giveaway_title", giveawayTitle)
            putString("platform", platform)
            hoursRemaining?.let { putLong("hours_remaining", it) }
        }
        logEvent("giveaway_card_clicked", b)
    }

    override fun logDealsSearchPerformed(queryLength: Int) {
        val b = Bundle().apply { putInt("query_length", queryLength) }
        logEvent("deals_search_performed", b)
    }

    override fun logDealsFilterApplied(store: String?, platform: String?, sortBy: String?) {
        val b = Bundle().apply {
            putString("store", store)
            putString("platform", platform)
            putString("sort_by", sortBy)
        }
        logEvent("deals_filter_applied", b)
    }

    override fun logGameDetailViewed(gameId: Int, gameTitle: String, sourceScreen: String) {
        val b = Bundle().apply {
            putInt("game_id", gameId)
            putString("game_title", gameTitle)
            putString("source_screen", sourceScreen)
        }
        logEvent("game_detail_viewed", b)
    }

    override fun logGameExternalLinkOpened(gameId: Int, gameTitle: String, destinationDomain: String) {
        val b = Bundle().apply {
            putInt("game_id", gameId)
            putString("game_title", gameTitle)
            putString("destination_domain", destinationDomain)
        }
        logEvent("game_external_link_opened", b)
    }

    override fun logThemeChanged(newTheme: String) {
        val b = Bundle().apply { putString("new_theme", newTheme) }
        logEvent("theme_changed", b)
    }

    override fun logDynamicColorToggled(enabled: Boolean) {
        val b = Bundle().apply { putBoolean("dynamic_color_enabled", enabled) }
        logEvent("dynamic_color_toggled", b)
    }
}
