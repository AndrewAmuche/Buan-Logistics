package com.example.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = "USR-001",
    val fullName: String,
    val email: String,
    val phone: String,
    val accountType: String,
    val role: String,
    val businessName: String = "",
    val corporateTier: String = "NONE",
    val subscriptionTier: String = "NONE",
    val subscriptionBilling: String = "MONTHLY",
    val subscriptionExpiresAt: String = "",
    val isVerified: Boolean = true,
    val profilePictureUri: String? = null,
    val avatarPreset: String = "preset_1",
    val streetAddress: String = "14 Marina Boulevard, Victoria Island",
    val city: String = "Lagos",
    val state: String = "Lagos State",
    val country: String = "Nigeria",
    val postalCode: String = "101241",
    val altPhone: String = "+234 812 345 6789",
    val jobTitle: String = "Import & Logistics Director",
    val passwordHash: String = "password123"
)

@Entity(
    tableName = "shipments",
    indices = [Index(value = ["trackingNumber"], unique = true)]
)
data class ShipmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val trackingNumber: String,
    val senderName: String,
    val senderPhone: String,
    val senderEmail: String,
    val senderAddress: String,
    val senderCity: String,
    val senderState: String,
    val senderCountry: String,
    val receiverName: String,
    val receiverPhone: String,
    val receiverEmail: String,
    val receiverAddress: String,
    val receiverCity: String,
    val receiverState: String,
    val receiverCountry: String,
    val shipmentType: String,
    val transportMode: String,
    val description: String,
    val quantity: Int,
    val weightKg: Double,
    val lengthCm: Double,
    val widthCm: Double,
    val heightCm: Double,
    val declaredValueUsd: Double,
    val pickupType: String,
    val deliveryType: String,
    val status: String,
    val currentLocation: String,
    val origin: String,
    val destination: String,
    val shipmentDate: String,
    val estimatedDelivery: String,
    val lastUpdated: String,
    val estimatedCostUsd: Double,
    val userId: String = "USR-001"
)

@Entity(tableName = "tracking_events")
data class TrackingEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val trackingNumber: String,
    val title: String,
    val location: String,
    val timestamp: String,
    val isCompleted: Boolean,
    val isCurrent: Boolean,
    val details: String = ""
)

@Entity(tableName = "hubs")
data class HubEntity(
    @PrimaryKey val id: String,
    val name: String,
    val address: String,
    val city: String,
    val state: String,
    val country: String,
    val openingHours: String,
    val servicesAvailable: String,
    val phone: String,
    val email: String,
    val status: String = "Active Hub"
)

@Entity(tableName = "hub_applications")
data class HubApplicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val businessName: String,
    val ownerName: String,
    val phone: String,
    val email: String,
    val businessAddress: String,
    val city: String,
    val state: String,
    val country: String,
    val businessType: String,
    val operatingHours: String,
    val status: String = "Pending Review",
    val submittedAt: String
)

@Entity(tableName = "quote_requests")
data class QuoteRequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val referenceId: String,
    val fromCity: String,
    val toCity: String,
    val transportMode: String,
    val shipmentType: String,
    val weightKg: Double,
    val quantity: Int,
    val cargoDescription: String,
    val pickupRequired: Boolean,
    val doorToDoor: Boolean,
    val estimatedQuoteUsd: Double,
    val status: String = "Received",
    val createdAt: String
)

@Entity(tableName = "buan_coin_wallets")
data class BuanCoinWalletEntity(
    @PrimaryKey val userId: String,
    val balance: Int,
    val totalEarned: Int,
    val totalRedeemed: Int,
    val pendingRewards: Int
)

@Entity(tableName = "buan_coin_transactions")
data class BuanCoinTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val amount: Int,
    val title: String,
    val trackingNumber: String,
    val type: String, // "EARNED", "REDEEMED"
    val date: String
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val type: String,
    val timestamp: String,
    val isRead: Boolean = false,
    val trackingNumber: String? = null
)
