package com.oechslerbernardo.kleverappchallange.domain.repository

import com.oechslerbernardo.kleverappchallange.domain.model.Crypto
import com.oechslerbernardo.kleverappchallange.util.Resource
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {

    fun getAllCryptos(
        start: Int?,
        limit: Int?,
        sort: String? = null,
        sortDir: String? = null
    ): Flow<Resource<List<Crypto>>>

    fun getSelectedCrypto(cryptoId: Int): Flow<Resource<Crypto>>

    fun getAllCryptosFromDb(): Flow<List<Crypto>>
    fun getAllCryptosSortedByNameAsc(): Flow<List<Crypto>>
    fun getAllCryptosSortedByNameDesc(): Flow<List<Crypto>>
    suspend fun addCrypto(crypto: Crypto)
    suspend fun deleteCrypto(crypto: Crypto)
    suspend fun getCryptoById(cryptoId: Int): Crypto?
    suspend fun deleteAllCryptos(): Boolean
    fun refreshAllCryptos(): Flow<Resource<Unit>>
}