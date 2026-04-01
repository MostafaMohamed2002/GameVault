package com.mostafadevo.freegames.analytics

import android.os.Bundle

interface GameVaultAnalytics {
    fun setAnalyticsCollectionEnabled(enabled: Boolean)
    fun logEvent(name: String, params: Bundle? = null)
    fun logFreeGamesScreenViewed()
    fun logFreeGameCardClicked(gameId: Int, gameTitle: String, platform: String)
    fun logFreeGamesFilterApplied(platform: String?, genre: String?, sortBy: String?)
    fun logFreeGamesRefreshed()
    fun logDealsScreenViewed(activeTab: String)
    fun logDealCardClicked(gameTitle: String, store: String, discountPercent: Int?, salePrice: Double?)
    fun logGiveawayCardClicked(giveawayTitle: String, platform: String, hoursRemaining: Long?)
    fun logDealsSearchPerformed(queryLength: Int)
    fun logDealsFilterApplied(store: String?, platform: String?, sortBy: String?)
    fun logGameDetailViewed(gameId: Int, gameTitle: String, sourceScreen: String)
    fun logGameExternalLinkOpened(gameId: Int, gameTitle: String, destinationDomain: String)
    fun logThemeChanged(newTheme: String)
    fun logDynamicColorToggled(enabled: Boolean)
}
