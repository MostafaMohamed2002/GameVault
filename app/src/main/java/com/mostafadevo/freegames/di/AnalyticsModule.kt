package com.mostafadevo.freegames.di

import com.mostafadevo.freegames.analytics.FirebaseAnalyticsImpl
import com.mostafadevo.freegames.analytics.GameVaultAnalytics
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {
    @Binds
    abstract fun bindAnalytics(impl: FirebaseAnalyticsImpl): GameVaultAnalytics
}

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsProvidesModule {
    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }
}
