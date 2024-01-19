package com.oechslerbernardo.kleverappchallange.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.oechslerbernardo.kleverappchallange.data.local.model.CryptoEntity

@Database(entities = [CryptoEntity::class], version = 1, exportSchema = false)
abstract class CryptoDatabase : RoomDatabase() {
    abstract fun cryptoDao(): CryptoDao
}