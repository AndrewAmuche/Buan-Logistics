package com.example.data.firebase

import android.content.Context
import android.util.Log
import com.example.R
import com.example.data.local.ShipmentEntity
import com.example.data.local.UserEntity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

class FirestoreManager(private val context: Context) {
    private val tag = "FirestoreManager"

    private val namedFirestore: FirebaseFirestore by lazy {
        try {
            val dbId = context.getString(R.string.firestore_database_id)
            if (dbId.isNotBlank()) {
                FirebaseFirestore.getInstance(dbId)
            } else {
                FirebaseFirestore.getInstance()
            }
        } catch (_: Exception) {
            FirebaseFirestore.getInstance()
        }
    }

    private val defaultFirestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    val firestore: FirebaseFirestore
        get() = namedFirestore

    suspend fun saveUserToFirestore(user: UserEntity): Boolean {
        return try {
            val data = mapOf(
                "id" to user.id,
                "fullName" to user.fullName,
                "email" to user.email.trim().lowercase(),
                "phone" to user.phone,
                "accountType" to user.accountType,
                "role" to user.role,
                "businessName" to user.businessName,
                "corporateTier" to user.corporateTier,
                "subscriptionTier" to user.subscriptionTier,
                "isVerified" to user.isVerified,
                "city" to user.city,
                "country" to user.country
            )
            firestore.collection("users").document(user.id).set(data).await()
            Log.d(tag, "Successfully uploaded user ${user.email} to Firestore")
            true
        } catch (e: Exception) {
            Log.w(tag, "Error saving user to Firestore: ${e.message}")
            false
        }
    }

    suspend fun findUserByEmailInFirestore(email: String): UserEntity? {
        val trimmed = email.trim().lowercase()
        return try {
            val snapshot = firestore.collection("users")
                .whereEqualTo("email", trimmed)
                .limit(1)
                .get()
                .await()
            if (!snapshot.isEmpty) {
                val doc = snapshot.documents[0]
                UserEntity(
                    id = doc.getString("id") ?: doc.id,
                    fullName = doc.getString("fullName") ?: "Website User",
                    email = doc.getString("email") ?: trimmed,
                    phone = doc.getString("phone") ?: "+234 800 000 0000",
                    accountType = doc.getString("accountType") ?: "Business Account",
                    role = doc.getString("role") ?: "Customer",
                    businessName = doc.getString("businessName") ?: "",
                    corporateTier = doc.getString("corporateTier") ?: "NONE",
                    subscriptionTier = doc.getString("subscriptionTier") ?: "NONE",
                    isVerified = doc.getBoolean("isVerified") ?: true,
                    city = doc.getString("city") ?: "Lagos",
                    country = doc.getString("country") ?: "Nigeria",
                    passwordHash = "password123"
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(tag, "Error searching user in Firestore: ${e.message}")
            null
        }
    }

    suspend fun syncAllUsersToFirestore(users: List<UserEntity>): Int {
        var count = 0
        for (user in users) {
            if (saveUserToFirestore(user)) {
                count++
            }
        }
        return count
    }

    // -------------------------------------------------------------
    // SHIPMENTS CLOUD SYNCHRONIZATION (Web Portal <-> Mobile App)
    // -------------------------------------------------------------

    suspend fun saveShipmentToFirestore(shipment: ShipmentEntity): Boolean {
        return try {
            val data = mapOf(
                "trackingNumber" to shipment.trackingNumber,
                "userId" to shipment.userId,
                "senderName" to shipment.senderName,
                "senderPhone" to shipment.senderPhone,
                "senderEmail" to shipment.senderEmail.trim().lowercase(),
                "senderAddress" to shipment.senderAddress,
                "senderCity" to shipment.senderCity,
                "senderState" to shipment.senderState,
                "senderCountry" to shipment.senderCountry,
                "receiverName" to shipment.receiverName,
                "receiverPhone" to shipment.receiverPhone,
                "receiverEmail" to shipment.receiverEmail.trim().lowercase(),
                "receiverAddress" to shipment.receiverAddress,
                "receiverCity" to shipment.receiverCity,
                "receiverState" to shipment.receiverState,
                "receiverCountry" to shipment.receiverCountry,
                "shipmentType" to shipment.shipmentType,
                "transportMode" to shipment.transportMode,
                "description" to shipment.description,
                "quantity" to shipment.quantity,
                "weightKg" to shipment.weightKg,
                "lengthCm" to shipment.lengthCm,
                "widthCm" to shipment.widthCm,
                "heightCm" to shipment.heightCm,
                "declaredValueUsd" to shipment.declaredValueUsd,
                "pickupType" to shipment.pickupType,
                "deliveryType" to shipment.deliveryType,
                "status" to shipment.status,
                "currentLocation" to shipment.currentLocation,
                "origin" to shipment.origin,
                "destination" to shipment.destination,
                "shipmentDate" to shipment.shipmentDate,
                "estimatedDelivery" to shipment.estimatedDelivery,
                "lastUpdated" to shipment.lastUpdated,
                "estimatedCostUsd" to shipment.estimatedCostUsd
            )
            firestore.collection("shipments")
                .document(shipment.trackingNumber)
                .set(data)
                .await()
            Log.d(tag, "Successfully synced shipment ${shipment.trackingNumber} to Firestore")
            true
        } catch (e: Exception) {
            Log.w(tag, "Error saving shipment to Firestore: ${e.message}")
            false
        }
    }

    fun docToShipment(doc: DocumentSnapshot): ShipmentEntity {
        return ShipmentEntity(
            trackingNumber = doc.getString("trackingNumber") ?: doc.id,
            senderName = doc.getString("senderName") ?: "Customer",
            senderPhone = doc.getString("senderPhone") ?: "",
            senderEmail = doc.getString("senderEmail") ?: "",
            senderAddress = doc.getString("senderAddress") ?: "",
            senderCity = doc.getString("senderCity") ?: "Origin",
            senderState = doc.getString("senderState") ?: "",
            senderCountry = doc.getString("senderCountry") ?: "Nigeria",
            receiverName = doc.getString("receiverName") ?: "Consignee",
            receiverPhone = doc.getString("receiverPhone") ?: "",
            receiverEmail = doc.getString("receiverEmail") ?: "",
            receiverAddress = doc.getString("receiverAddress") ?: "",
            receiverCity = doc.getString("receiverCity") ?: "Destination",
            receiverState = doc.getString("receiverState") ?: "",
            receiverCountry = doc.getString("receiverCountry") ?: "United Kingdom",
            shipmentType = doc.getString("shipmentType") ?: "Commercial Cargo",
            transportMode = doc.getString("transportMode") ?: "AIR",
            description = doc.getString("description") ?: "Consignment",
            quantity = (doc.get("quantity") as? Number)?.toInt() ?: 1,
            weightKg = (doc.get("weightKg") as? Number)?.toDouble() ?: 5.0,
            lengthCm = (doc.get("lengthCm") as? Number)?.toDouble() ?: 30.0,
            widthCm = (doc.get("widthCm") as? Number)?.toDouble() ?: 20.0,
            heightCm = (doc.get("heightCm") as? Number)?.toDouble() ?: 15.0,
            declaredValueUsd = (doc.get("declaredValueUsd") as? Number)?.toDouble() ?: 100.0,
            pickupType = doc.getString("pickupType") ?: "Customer Address",
            deliveryType = doc.getString("deliveryType") ?: "Recipient Address",
            status = doc.getString("status") ?: "Shipment Created",
            currentLocation = doc.getString("currentLocation") ?: "Origin Facility",
            origin = doc.getString("origin") ?: "${doc.getString("senderCity") ?: "Origin"}, ${doc.getString("senderCountry") ?: ""}".trim().removeSuffix(","),
            destination = doc.getString("destination") ?: "${doc.getString("receiverCity") ?: "Destination"}, ${doc.getString("receiverCountry") ?: ""}".trim().removeSuffix(","),
            shipmentDate = doc.getString("shipmentDate") ?: "Today",
            estimatedDelivery = doc.getString("estimatedDelivery") ?: "Pending",
            lastUpdated = doc.getString("lastUpdated") ?: "Just now",
            estimatedCostUsd = (doc.get("estimatedCostUsd") as? Number)?.toDouble() ?: 85.0,
            userId = doc.getString("userId") ?: "USR-001"
        )
    }

    suspend fun fetchShipmentFromFirestore(trackingNumber: String): ShipmentEntity? {
        val cleanTracking = trackingNumber.trim()
        val upperTracking = cleanTracking.uppercase()
        val digitsOnly = cleanTracking.filter { it.isDigit() }

        val instances = listOf(namedFirestore, defaultFirestore).distinct()

        for (db in instances) {
            try {
                // 1. Try exact document ID
                val doc = db.collection("shipments").document(cleanTracking).get().await()
                if (doc.exists()) return docToShipment(doc)

                // 2. Try uppercase document ID
                val docUpper = db.collection("shipments").document(upperTracking).get().await()
                if (docUpper.exists()) return docToShipment(docUpper)

                // 3. Try query by trackingNumber field exact
                val querySnapshot = db.collection("shipments")
                    .whereEqualTo("trackingNumber", cleanTracking)
                    .get()
                    .await()
                querySnapshot.documents.firstOrNull()?.let { return docToShipment(it) }

                // 4. Try query by trackingNumber field uppercase
                val queryUpperSnapshot = db.collection("shipments")
                    .whereEqualTo("trackingNumber", upperTracking)
                    .get()
                    .await()
                queryUpperSnapshot.documents.firstOrNull()?.let { return docToShipment(it) }

                // 5. Try case-insensitive and numeric match scan of all documents
                val allSnapshot = db.collection("shipments").get().await()
                val matchDoc = allSnapshot.documents.firstOrNull { d ->
                    val tid = (d.getString("trackingNumber") ?: d.id).trim()
                    tid.equals(cleanTracking, ignoreCase = true) ||
                    tid.equals(upperTracking, ignoreCase = true) ||
                    tid.contains(cleanTracking, ignoreCase = true) ||
                    cleanTracking.contains(tid, ignoreCase = true) ||
                    (digitsOnly.length >= 4 && tid.filter { it.isDigit() } == digitsOnly)
                }
                if (matchDoc != null) {
                    return docToShipment(matchDoc)
                }
            } catch (e: Exception) {
                Log.w(tag, "Error querying database instance for $trackingNumber: ${e.message}")
            }
        }
        return null
    }

    suspend fun fetchShipmentsFromFirestore(): List<ShipmentEntity> {
        return try {
            val snapshot = firestore.collection("shipments").get().await()
            snapshot.documents.mapNotNull { doc ->
                try {
                    docToShipment(doc)
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            Log.w(tag, "Error fetching shipments from Firestore: ${e.message}")
            emptyList()
        }
    }

    fun listenToShipments(onUpdate: (List<ShipmentEntity>) -> Unit): ListenerRegistration? {
        return try {
            firestore.collection("shipments")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w(tag, "Error listening to shipments: ${error.message}")
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val list = snapshot.documents.mapNotNull { doc ->
                            try {
                                docToShipment(doc)
                            } catch (e: Exception) {
                                null
                            }
                        }
                        onUpdate(list)
                    }
                }
        } catch (e: Exception) {
            Log.w(tag, "Failed to attach shipments listener: ${e.message}")
            null
        }
    }
}
