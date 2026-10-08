package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserEntity::class,
        ShipmentEntity::class,
        TrackingEventEntity::class,
        HubEntity::class,
        HubApplicationEntity::class,
        QuoteRequestEntity::class,
        BuanCoinWalletEntity::class,
        BuanCoinTransactionEntity::class,
        NotificationEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class BuanDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun shipmentDao(): ShipmentDao
    abstract fun trackingEventDao(): TrackingEventDao
    abstract fun hubDao(): HubDao
    abstract fun quoteDao(): QuoteDao
    abstract fun walletDao(): WalletDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: BuanDatabase? = null

        fun getDatabase(context: Context): BuanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BuanDatabase::class.java,
                    "buan_logistics.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
