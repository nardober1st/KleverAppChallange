package com.oechslerbernardo.kleverappchallange.di

import android.app.Application
import androidx.room.Room
import com.oechslerbernardo.kleverappchallange.data.local.CryptoDao
import com.oechslerbernardo.kleverappchallange.data.local.CryptoDatabase
import com.oechslerbernardo.kleverappchallange.data.remote.CryptoApi
import com.oechslerbernardo.kleverappchallange.data.repository.CryptoRepositoryImpl
import com.oechslerbernardo.kleverappchallange.domain.repository.CryptoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideCryptoApi(client: OkHttpClient): CryptoApi {
        return Retrofit
            .Builder()
            .baseUrl(CryptoApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create(CryptoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCryptoRepositoryImpl(api: CryptoApi, db: CryptoDatabase): CryptoRepository {
        return CryptoRepositoryImpl(api, db)
    }

    @Provides
    @Singleton
    fun provideCryptoDatabase(
        application: Application
    ): CryptoDatabase {
        return Room.databaseBuilder(
            context = application,
            klass = CryptoDatabase::class.java,
            name = "crypto_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCryptoDao(appDatabase: CryptoDatabase): CryptoDao {
        return appDatabase.cryptoDao()
    }
}