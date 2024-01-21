package com.oechslerbernardo.kleverappchallange.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.oechslerbernardo.kleverappchallange.data.local.model.CryptoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrypto(crypto: CryptoEntity)

    @Delete
    suspend fun deleteCrypto(crypto: CryptoEntity)

    @Query("SELECT * FROM crypto_table")
    fun getAllCryptos(): Flow<List<CryptoEntity>>

    @Query("SELECT * FROM crypto_table ORDER BY name ASC")
    fun getAllCryptosSortedByNameAsc(): Flow<List<CryptoEntity>>

    @Query("SELECT * FROM crypto_table ORDER BY name DESC")
    fun getAllCryptosSortedByNameDesc(): Flow<List<CryptoEntity>>

    @Query("SELECT * FROM crypto_table WHERE id = :cryptoId")
    suspend fun getCryptoById(cryptoId: Int): CryptoEntity?

    @Query("DELETE FROM crypto_table")
    suspend fun deleteAllCryptos()
}