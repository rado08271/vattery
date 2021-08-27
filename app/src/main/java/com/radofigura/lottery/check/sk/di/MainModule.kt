package com.radofigura.lottery.check.sk.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.GsonBuilder
import com.radofigura.lottery.check.sk.App
import com.radofigura.lottery.check.sk.dao.TicketsDao
import com.radofigura.lottery.check.sk.model.Metadata
import com.radofigura.lottery.check.sk.service.TicketDatabase
import com.radofigura.lottery.check.sk.service.api.MfApi
import com.radofigura.lottery.check.sk.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@Suppress("unused")
@InstallIn(SingletonComponent::class)
class MainModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): App {
        return app as App
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl("https://www.mfsr.sk/").client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        headerInterceptor: Interceptor,
        cache: Cache
    ): OkHttpClient {

        val okHttpClientBuilder = OkHttpClient().newBuilder()
        okHttpClientBuilder.connectTimeout(Constants.CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(Constants.READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(Constants.WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.cache(cache)
        okHttpClientBuilder.addInterceptor(headerInterceptor)

        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor {
            val requestBuilder = it.request().newBuilder()
            it.proceed(requestBuilder.build())
        }
    }

    @Provides
    @Singleton
    internal fun provideCache(context: Context): Cache {
        val httpCacheDirectory = File(context.cacheDir.absolutePath, "HttpCache")
        return Cache(httpCacheDirectory, Constants.CACHE_SIZE_BYTES)
    }

    @Provides
    @Singleton
    fun provideContext(application: App): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): MfApi {
        return retrofit.create(MfApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFirebase(): CollectionReference {
        return Firebase.firestore.collection("phrases");
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext applicationContext: Context): TicketDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TicketDatabase::class.java, "tickets-dbs"
        ).allowMainThreadQueries().build()
    }

    @Provides
    fun provideDao(ticketDatabase: TicketDatabase): TicketsDao {
        return ticketDatabase.ticketsDao()
    }

    @Provides
    @Singleton
    fun provideRemoteConfig(): FirebaseRemoteConfig {
        val defaultMetadata = Metadata()
        return Firebase.remoteConfig.apply {
            setConfigSettingsAsync(remoteConfigSettings {
                this.minimumFetchIntervalInSeconds = Constants.REMOTE_CONFIG_INTERVAL_SECONDS
            })
            this.setDefaultsAsync(
                mapOf(
                    Pair("showStartTime", defaultMetadata.showStartTime),
                    Pair("showRuntimeDayOfTheWeek", defaultMetadata.showRuntimeDayOfTheWeek),
                    Pair("showRuntimeDurationHours", defaultMetadata.showRuntimeDurationHours),
                    Pair("minutesToPersistData", defaultMetadata.minutesToPersistData),
                    Pair("filterUserByAmountOfPhrasesOwned", defaultMetadata.filterUserByAmountOfPhrasesOwned),
                    Pair("filterOldestPossiblePhraseInMinutes", defaultMetadata.filterOldestPossiblePhraseInMinutes),
                    Pair("maxDelayToReadData", defaultMetadata.maxDelayToReadData),
                    Pair("appVersion", defaultMetadata.appVersion),
                )
            )
        }
    }
}