package com.oechslerbernardo.kleverappchallange.data.repository

import android.util.Log
import com.oechslerbernardo.kleverappchallange.data.local.CryptoDatabase
import com.oechslerbernardo.kleverappchallange.data.mappers.toCrypto
import com.oechslerbernardo.kleverappchallange.data.mappers.toEntity
import com.oechslerbernardo.kleverappchallange.data.remote.CryptoApi
import com.oechslerbernardo.kleverappchallange.domain.model.Crypto
import com.oechslerbernardo.kleverappchallange.domain.repository.CryptoRepository
import com.oechslerbernardo.kleverappchallange.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(
    private val api: CryptoApi,
    private val db: CryptoDatabase
) : CryptoRepository {

    override fun getAllCryptos(
        start: Int?,
        limit: Int?,
        sort: String?,
        sortDir: String?
    ): Flow<Resource<List<Crypto>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = api.getAllCryptos(start, limit, sort, sortDir)
                val cryptos = response.toCrypto()
                emit(Resource.Success(cryptos))
            } catch (e: Exception) {
                emit(handleNetworkError(e))
            }
        }
    }

    override fun getSelectedCrypto(cryptoId: Int): Flow<Resource<Crypto>> =
        flow {
            emit(Resource.Loading())
            try {
                val response = api.getAllCryptos(start = null, limit = null)
                val allCryptos = response.toCrypto()

                val selectedCrypto = allCryptos.find { it.id == cryptoId }
                if (selectedCrypto != null) {
                    emit(Resource.Success(selectedCrypto))
                    Log.d("TAGY", "Fetched details for ID: $cryptoId: $selectedCrypto")
                } else {
                    emit(Resource.Error("Crypto not found", null))
                    Log.d("TAGY", "Crypto not found for ID: $cryptoId")
                }
            } catch (e: Throwable) {
                emit(handleNetworkError(e))
            }
        }

    override fun getAllCryptosFromDb(): Flow<List<Crypto>> {
        return db.cryptoDao().getAllCryptos().map { entities ->
            entities.map { it.toCrypto() }
        }
    }

    override fun getAllCryptosSortedByNameAsc(): Flow<List<Crypto>> {
        return db.cryptoDao().getAllCryptosSortedByNameAsc().map { entities ->
            entities.map { it.toCrypto() }
        }
    }

    override fun getAllCryptosSortedByNameDesc(): Flow<List<Crypto>> {
        return db.cryptoDao().getAllCryptosSortedByNameDesc().map { entities ->
            entities.map { it.toCrypto() }
        }
    }

    override suspend fun addCrypto(crypto: Crypto) {
        db.cryptoDao().insertCrypto(crypto.toEntity())
    }

    override suspend fun deleteCrypto(crypto: Crypto) {
        db.cryptoDao().deleteCrypto(crypto.toEntity())
    }

    override suspend fun getCryptoById(cryptoId: Int): Crypto? {
        return db.cryptoDao().getCryptoById(cryptoId)?.toCrypto()
    }

    override suspend fun deleteAllCryptos(): Boolean {
        val currentList = db.cryptoDao().getAllCryptos().firstOrNull()
        if (currentList.isNullOrEmpty()) {
            return false
        }
        db.cryptoDao().deleteAllCryptos()
        return true
    }

    override fun refreshAllCryptos(): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            try {
                val existingCryptos = db.cryptoDao().getAllCryptos().first()

                val updatedCryptoList = api.getAllCryptos(start = null, limit = null).data
                val existingCryptosMap = existingCryptos.associateBy { it.id }

                updatedCryptoList.forEach { cryptoDto ->
                    val existingCryptoEntity = existingCryptosMap[cryptoDto.id]
                    if (existingCryptoEntity != null) {
                        db.cryptoDao().insertCrypto(cryptoDto.toEntity())
                    }
                }
                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                emit(Resource.Error("Error refreshing cryptos: ${e.localizedMessage}"))
            } catch (e: UnknownHostException) {
                emit(Resource.Error("No internet connection. Unable to refresh data."))
            }
        }

    private fun handleNetworkError(e: Throwable): Resource.Error<Nothing> {
        val errorMessage = when (e) {
            is HttpException -> {
                Log.e("CryptoRepository", "HTTP Exception: ${e.message}")
                "HTTP error: ${e.message()}"
            }

            is IOException -> {
                Log.e("CryptoRepository", "Network Error: ${e.message}")
                "Network error: ${e.message}"
            }

            else -> {
                Log.e("CryptoRepository", "Unknown Error: ${e.message}")
                "Unknown error: ${e.message}"
            }
        }
        return Resource.Error(errorMessage)
    }
}