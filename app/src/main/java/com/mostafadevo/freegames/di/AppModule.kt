package com.mostafadevo.freegames.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.mostafadevo.freegames.data.local.FreeGameDetails.FreeGameDetailsDao
import com.mostafadevo.freegames.data.local.FreeGamesDao
import com.mostafadevo.freegames.data.local.FreeGamesDatabase
import com.mostafadevo.freegames.data.remote.cheapshark.CheapSharkApi
import com.mostafadevo.freegames.data.remote.freetogame.FreeGamesApi
import com.mostafadevo.freegames.data.remote.gamepower.GamePowerApi
import com.mostafadevo.freegames.data.repository.CheapSharkRepositoryImpl
import com.mostafadevo.freegames.data.repository.DataStoreRepositoryImpl
import com.mostafadevo.freegames.data.repository.FreeGamesRepositoryImpl
import com.mostafadevo.freegames.data.repository.GamePowerRepositoryImpl
import com.mostafadevo.freegames.domain.repository.CheapSharkRepository
import com.mostafadevo.freegames.domain.repository.DataStoreRepository
import com.mostafadevo.freegames.domain.repository.FreeGamesRepository
import com.mostafadevo.freegames.domain.repository.GamePowerRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder().apply {
        addInterceptor(loggingInterceptor)
    }.build()

    private val moshi = Moshi.Builder() // adapter
        .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun provideFreeGamesApi(): FreeGamesApi {
        return Retrofit.Builder()
            .baseUrl("https://www.freetogame.com/api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(FreeGamesApi::class.java)
    }

    @Provides
    fun provideCheapSharkApi(): CheapSharkApi {
        return Retrofit.Builder()
            .baseUrl("https://www.cheapshark.com/api/1.0/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(CheapSharkApi::class.java)
    }

    @Provides
    fun provideGamePowerApi(): GamePowerApi {
        return Retrofit.Builder()
            .baseUrl("https://www.gamerpower.com/api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(GamePowerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFreeGamesDatabase(app: Application): FreeGamesDatabase {
        return Room.databaseBuilder(
            app,
            FreeGamesDatabase::class.java,
            "freegames.db"
        ).build()
    }

    @Provides
    fun provideFreeGamesDao(db: FreeGamesDatabase): FreeGamesDao = db.FreegamesDao()

    @Provides
    fun provideFreeGameDetailsDao(db: FreeGamesDatabase): FreeGameDetailsDao =
        db.FreeGameDetailsDao()

    @Provides
    fun provideFreeGamesRepository(
        api: FreeGamesApi,
        gamesDao: FreeGamesDao,
        gameDetailsDao: FreeGameDetailsDao
    ): FreeGamesRepository = FreeGamesRepositoryImpl(api, gamesDao, gameDetailsDao)

    @Provides
    fun provideCheapSharkRepository(api: CheapSharkApi): CheapSharkRepository =
        CheapSharkRepositoryImpl(api)

    @Provides
    fun provideGamePowerRepository(api: GamePowerApi): GamePowerRepository =
        GamePowerRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideDataStoreRepository(context: Context): DataStoreRepository = DataStoreRepositoryImpl(
        context
    )
}
