package com.oechslerbernardo.kleverappchallange.data.remote

import com.oechslerbernardo.kleverappchallange.BuildConfig
import com.oechslerbernardo.kleverappchallange.data.remote.model.crypto.CryptoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoApi {

    @GET("v1/cryptocurrency/listings/latest")
    suspend fun getAllCryptos(
        @Query("start") start: Int?,
        @Query("limit") limit: Int?,
        @Query("sort") sort: String? = null, // Add sorting parameter
        @Query("sort_dir") sortDir: String? = null, // Add sort direction parameter
        @Query("CMC_PRO_API_KEY") apiKey: String = BuildConfig.API_KEY
    ): CryptoDto

    companion object {
        const val BASE_URL = "https://pro-api.coinmarketcap.com/"
    }
}