package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShipmentDao {
    @Query("SELECT * FROM shipments ORDER BY id DESC")
    fun getAllShipments(): Flow<List<ShipmentEntity>>

    @Query("SELECT * FROM shipments WHERE trackingNumber = :trackingNumber LIMIT 1")
    fun getShipmentByTrackingNumber(trackingNumber: String): Flow<ShipmentEntity?>

    @Query("SELECT * FROM shipments WHERE trackingNumber = :trackingNumber LIMIT 1")
    suspend fun findShipmentSync(trackingNumber: String): ShipmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShipment(shipment: ShipmentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShipments(shipments: List<ShipmentEntity>)

    @Update
    suspend fun updateShipment(shipment: ShipmentEntity)

    @Query("UPDATE shipments SET status = :status, currentLocation = :location, lastUpdated = :updatedTime WHERE trackingNumber = :trackingNumber")
    suspend fun updateShipmentStatus(trackingNumber: String, status: String, location: String, updatedTime: String)

    @Query("SELECT COUNT(*) FROM shipments")
    suspend fun count(): Int
}

@Dao
interface TrackingEventDao {
    @Query("SELECT * FROM tracking_events WHERE trackingNumber = :trackingNumber ORDER BY id ASC")
    fun getEventsForShipment(trackingNumber: String): Flow<List<TrackingEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<TrackingEventEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: TrackingEventEntity)

    @Query("UPDATE tracking_events SET isCompleted = 1, isCurrent = 0 WHERE trackingNumber = :trackingNumber AND title = :title")
    suspend fun markCompleted(trackingNumber: String, title: String)

    @Query("UPDATE tracking_events SET isCurrent = 1 WHERE trackingNumber = :trackingNumber AND title = :title")
    suspend fun markCurrent(trackingNumber: String, title: String)
}

@Dao
interface HubDao {
    @Query("SELECT * FROM hubs ORDER BY name ASC")
    fun getAllHubs(): Flow<List<HubEntity>>

    @Query("SELECT * FROM hubs WHERE name LIKE '%' || :query || '%' OR city LIKE '%' || :query || '%' OR country LIKE '%' || :query || '%'")
    fun searchHubs(query: String): Flow<List<HubEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHubs(hubs: List<HubEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: HubApplicationEntity): Long

    @Query("SELECT * FROM hub_applications ORDER BY id DESC")
    fun getAllApplications(): Flow<List<HubApplicationEntity>>

    @Query("UPDATE hub_applications SET status = :status WHERE id = :id")
    suspend fun updateApplicationStatus(id: Int, status: String)

    @Query("SELECT COUNT(*) FROM hubs")
    suspend fun count(): Int
}

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quote_requests ORDER BY id DESC")
    fun getAllQuotes(): Flow<List<QuoteRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: QuoteRequestEntity): Long

    @Query("UPDATE quote_requests SET status = :status WHERE id = :id")
    suspend fun updateQuoteStatus(id: Int, status: String)
}

@Dao
interface WalletDao {
    @Query("SELECT * FROM buan_coin_wallets WHERE userId = :userId LIMIT 1")
    fun getWallet(userId: String): Flow<BuanCoinWalletEntity?>

    @Query("SELECT * FROM buan_coin_wallets WHERE userId = :userId LIMIT 1")
    suspend fun getWalletSync(userId: String): BuanCoinWalletEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(wallet: BuanCoinWalletEntity)

    @Query("SELECT * FROM buan_coin_transactions WHERE userId = :userId ORDER BY id DESC")
    fun getTransactions(userId: String): Flow<List<BuanCoinTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: BuanCoinTransactionEntity): Long

    @Query("UPDATE buan_coin_wallets SET balance = balance + :amount, totalEarned = totalEarned + :amount WHERE userId = :userId")
    suspend fun addCoins(userId: String, amount: Int)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY id DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Int)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()
}

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUser(id: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: String): UserEntity?

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun findUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getActiveUserSync(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET isVerified = :isVerified WHERE id = :id")
    suspend fun updateUserVerification(id: String, isVerified: Boolean)

    @Query("UPDATE users SET subscriptionTier = :tier, subscriptionBilling = :billing WHERE id = :id")
    suspend fun updateUserSubscription(id: String, tier: String, billing: String)
}
