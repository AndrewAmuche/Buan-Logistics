package com.example.data.sample

import com.example.data.local.BuanCoinTransactionEntity
import com.example.data.local.BuanCoinWalletEntity
import com.example.data.local.BuanDatabase
import com.example.data.local.HubApplicationEntity
import com.example.data.local.HubEntity
import com.example.data.local.NotificationEntity
import com.example.data.local.QuoteRequestEntity
import com.example.data.local.ShipmentEntity
import com.example.data.local.TrackingEventEntity
import com.example.data.local.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SampleDataLoader {

    suspend fun seedDatabaseIfEmpty(db: BuanDatabase) = withContext(Dispatchers.IO) {
        // 1. Seed Active Users & Customers
        val sampleUsers = listOf(
            UserEntity(
                id = "USR-000",
                fullName = "Babies Touch Admin",
                email = "babiestouchsupport@gmail.com",
                phone = "+234 800 123 4567",
                accountType = "Business Account",
                role = "Administrator",
                businessName = "Babies Touch Logistics & Web Admin",
                corporateTier = "DIAMOND",
                subscriptionTier = "DIAMOND",
                isVerified = true,
                streetAddress = "BUAN Operations Headquarters",
                city = "Lagos",
                jobTitle = "Chief Executive & Platform Admin",
                passwordHash = "password123"
            ),
            UserEntity(
                id = "USR-WEB-EZE",
                fullName = "Eze",
                email = "eze100@gmail.com",
                phone = "+234 803 100 2000",
                accountType = "Business Account",
                role = "Customer",
                businessName = "Eze Global Logistics & Trade",
                corporateTier = "GOLD",
                subscriptionTier = "GOLD",
                isVerified = true,
                streetAddress = "Alaba International Market",
                city = "Lagos",
                jobTitle = "Managing Director",
                passwordHash = "password123"
            ),
            UserEntity(
                id = "USR-001",
                fullName = "Babajide Adeyemi",
                email = "babajide@buanlogistics.com",
                phone = "+234 803 555 0192",
                accountType = "Personal Account",
                role = "Customer",
                businessName = "Adeyemi Global Trade Ltd",
                corporateTier = "SILVER",
                subscriptionTier = "SILVER",
                isVerified = true,
                streetAddress = "14 Marina Boulevard, Victoria Island",
                city = "Lagos",
                jobTitle = "Import & Logistics Director"
            ),
            UserEntity(
                id = "USR-002",
                fullName = "Chidinma Okafor",
                email = "chidinma@okaforfabrics.ng",
                phone = "+234 802 333 4455",
                accountType = "Business Account",
                role = "Business Customer",
                businessName = "Okafor Textiles & Garments Ltd",
                corporateTier = "GOLD",
                subscriptionTier = "GOLD",
                isVerified = true,
                streetAddress = "22 Balogun Market Street",
                city = "Lagos",
                jobTitle = "Chief Operating Officer"
            ),
            UserEntity(
                id = "USR-003",
                fullName = "Alhaji Musa Danjuma",
                email = "musa.danjuma@danjumagroup.com",
                phone = "+234 809 777 8899",
                accountType = "Business Account",
                role = "Corporate Partner",
                businessName = "Danjuma Agro-Allied Commodities Ltd",
                corporateTier = "DIAMOND",
                subscriptionTier = "DIAMOND",
                isVerified = true,
                streetAddress = "45 Bompai Industrial Area",
                city = "Kano",
                jobTitle = "Managing Director"
            ),
            UserEntity(
                id = "USR-004",
                fullName = "Dr. Samuel Olatunji",
                email = "s.olatunji@medixcare.org",
                phone = "+234 805 111 2233",
                accountType = "Personal Account",
                role = "Customer",
                businessName = "Medix Health Diagnostics",
                corporateTier = "NONE",
                subscriptionTier = "NONE",
                isVerified = false,
                streetAddress = "8 Ring Road",
                city = "Ibadan",
                jobTitle = "Senior Consultant"
            ),
            UserEntity(
                id = "USR-005",
                fullName = "Grace Eke",
                email = "grace.eke@alabastation.com",
                phone = "+234 813 999 0011",
                accountType = "Business Account",
                role = "Hub Provider",
                businessName = "Alaba Central Intake Station",
                corporateTier = "SILVER",
                subscriptionTier = "SILVER",
                isVerified = true,
                streetAddress = "Shop 14, Line 3, Alaba International Market",
                city = "Lagos",
                jobTitle = "Intake Station Manager"
            ),
            UserEntity(
                id = "USR-ADMIN",
                fullName = "BUAN Operations Director",
                email = "babiestouchsupport@gmail.com",
                phone = "+44 20 7946 0888",
                accountType = "Business Account",
                role = "Administrator",
                businessName = "BUAN Global Freight Headquarters",
                corporateTier = "DIAMOND",
                subscriptionTier = "DIAMOND",
                isVerified = true,
                streetAddress = "Cargo Centre, London Heathrow Airport",
                city = "London",
                jobTitle = "Head of Global Logistics & Fleet Operations"
            )
        )
        for (user in sampleUsers) {
            val existing = db.userDao().findUserByEmail(user.email)
            if (existing == null) {
                db.userDao().insertUser(user)
            }
            val wallet = db.walletDao().getWalletSync(user.id)
            if (wallet == null) {
                db.walletDao().insertWallet(
                    BuanCoinWalletEntity(
                        userId = user.id,
                        balance = 25,
                        totalEarned = 50,
                        totalRedeemed = 25,
                        pendingRewards = 5
                    )
                )
            }
        }

        // If other data is already populated, skip remaining seeding
        if (db.shipmentDao().count() > 0) {
            return@withContext
        }

        // 2. Seed Shipments
        val sampleShipments = listOf(
            ShipmentEntity(
                trackingNumber = "BUAN-123456789",
                senderName = "Babajide Adeyemi",
                senderPhone = "+234 803 555 0192",
                senderEmail = "babajide@buanlogistics.com",
                senderAddress = "14 Marina Boulevard, Victoria Island",
                senderCity = "Lagos",
                senderState = "Lagos State",
                senderCountry = "Nigeria",
                receiverName = "Alexander Wright",
                receiverPhone = "+44 20 7946 0912",
                receiverEmail = "a.wright@londonlogistics.co.uk",
                receiverAddress = "88 Leadenhall Street, City of London",
                receiverCity = "London",
                receiverState = "Greater London",
                receiverCountry = "United Kingdom",
                shipmentType = "Commercial Goods",
                transportMode = "Air Freight",
                description = "Handcrafted Leather Products & Textiles (Batch B)",
                quantity = 4,
                weightKg = 42.5,
                lengthCm = 60.0,
                widthCm = 45.0,
                heightCm = 40.0,
                declaredValueUsd = 3450.0,
                pickupType = "BUAN Hub",
                deliveryType = "Recipient Address",
                status = "In Transit",
                currentLocation = "Air Transit - Flight BU-704 en route LHR",
                origin = "Lagos, Nigeria",
                destination = "London, United Kingdom",
                shipmentDate = "18 Jan 2026",
                estimatedDelivery = "26 Jan 2026",
                lastUpdated = "Today, 06:15 AM",
                estimatedCostUsd = 485.00
            ),
            ShipmentEntity(
                trackingNumber = "BUAN-987654321",
                senderName = "Babajide Adeyemi",
                senderPhone = "+234 803 555 0192",
                senderEmail = "babajide@buanlogistics.com",
                senderAddress = "Plot 7 Trans-Amadi Industrial Layout",
                senderCity = "Port Harcourt",
                senderState = "Rivers State",
                senderCountry = "Nigeria",
                receiverName = "Gulf Petro Services Inc.",
                receiverPhone = "+1 713 555 0188",
                receiverEmail = "procurement@gulfpetro.com",
                receiverAddress = "1200 Post Oak Blvd",
                receiverCity = "Houston",
                receiverState = "Texas",
                receiverCountry = "United States",
                shipmentType = "Heavy Cargo",
                transportMode = "Sea Freight",
                description = "High-Pressure Marine Valves & Flanges",
                quantity = 12,
                weightKg = 1450.0,
                lengthCm = 240.0,
                widthCm = 120.0,
                heightCm = 150.0,
                declaredValueUsd = 28500.0,
                pickupType = "Customer Address",
                deliveryType = "BUAN Hub",
                status = "Out for Delivery",
                currentLocation = "Houston Seaport Dispatch Center",
                origin = "Port Harcourt, Nigeria",
                destination = "Houston, United States",
                shipmentDate = "05 Jan 2026",
                estimatedDelivery = "24 Jan 2026",
                lastUpdated = "Yesterday, 04:30 PM",
                estimatedCostUsd = 3200.00
            ),
            ShipmentEntity(
                trackingNumber = "BUAN-567890123",
                senderName = "Babajide Adeyemi",
                senderPhone = "+234 803 555 0192",
                senderEmail = "babajide@buanlogistics.com",
                senderAddress = "Bompai Industrial District",
                senderCity = "Kano",
                senderState = "Kano State",
                senderCountry = "Nigeria",
                receiverName = "Rashid Al-Mansoori Trading",
                receiverPhone = "+971 4 388 9012",
                receiverEmail = "trading@almansoori.ae",
                receiverAddress = "Deira Wholesale Market, Unit 4B",
                receiverCity = "Dubai",
                receiverState = "Dubai",
                receiverCountry = "United Arab Emirates",
                shipmentType = "Perishable Goods",
                transportMode = "Express International",
                description = "Organic Dried Ginger & Agricultural Samples",
                quantity = 2,
                weightKg = 12.0,
                lengthCm = 35.0,
                widthCm = 25.0,
                heightCm = 20.0,
                declaredValueUsd = 850.0,
                pickupType = "Customer Address",
                deliveryType = "Recipient Address",
                status = "Delivered",
                currentLocation = "Delivered to Deira Wholesale Office",
                origin = "Kano, Nigeria",
                destination = "Dubai, United Arab Emirates",
                shipmentDate = "10 Jan 2026",
                estimatedDelivery = "15 Jan 2026",
                lastUpdated = "15 Jan 2026, 02:14 PM",
                estimatedCostUsd = 210.00
            ),
            ShipmentEntity(
                trackingNumber = "BUAN-445892110",
                senderName = "Babajide Adeyemi",
                senderPhone = "+234 803 555 0192",
                senderEmail = "babajide@buanlogistics.com",
                senderAddress = "14 Marina Boulevard",
                senderCity = "Lagos",
                senderState = "Lagos State",
                senderCountry = "Nigeria",
                receiverName = "Kofi Mensah Logistics",
                receiverPhone = "+233 24 555 8901",
                receiverEmail = "k.mensah@accralog.gh",
                receiverAddress = "Graphic Road, Industrial Area",
                receiverCity = "Accra",
                receiverState = "Greater Accra",
                receiverCountry = "Ghana",
                shipmentType = "Parcel",
                transportMode = "Road Freight",
                description = "Automotive Replacement Spare Parts",
                quantity = 6,
                weightKg = 85.0,
                lengthCm = 80.0,
                widthCm = 60.0,
                heightCm = 50.0,
                declaredValueUsd = 1950.0,
                pickupType = "BUAN Hub",
                deliveryType = "Recipient Address",
                status = "Processing",
                currentLocation = "Seme Border Transit Customs Clearance",
                origin = "Lagos, Nigeria",
                destination = "Accra, Ghana",
                shipmentDate = "22 Jan 2026",
                estimatedDelivery = "28 Jan 2026",
                lastUpdated = "2 hours ago",
                estimatedCostUsd = 340.00
            )
        )
        db.shipmentDao().insertShipments(sampleShipments)

        // 3. Seed Events for BUAN-123456789
        val eventsBuan1 = listOf(
            TrackingEventEntity(
                trackingNumber = "BUAN-123456789",
                title = "Shipment Created",
                location = "Online Portal, Lagos",
                timestamp = "18 Jan 2026, 09:30 AM",
                isCompleted = true,
                isCurrent = false,
                details = "Waybill generated and consignment registered into BUAN systems."
            ),
            TrackingEventEntity(
                trackingNumber = "BUAN-123456789",
                title = "Shipment Registered",
                location = "Ikeja Air Cargo Hub, Lagos",
                timestamp = "18 Jan 2026, 11:15 AM",
                isCompleted = true,
                isCurrent = false,
                details = "Consignment received, weighed, and verified at Ikeja Hub."
            ),
            TrackingEventEntity(
                trackingNumber = "BUAN-123456789",
                title = "Picked Up",
                location = "Ikeja Hub Dispatch, Lagos",
                timestamp = "19 Jan 2026, 08:00 AM",
                isCompleted = true,
                isCurrent = false,
                details = "Loaded onto airport transfer shuttle truck."
            ),
            TrackingEventEntity(
                trackingNumber = "BUAN-123456789",
                title = "Processing",
                location = "Murtala Muhammed International Airport (LOS)",
                timestamp = "19 Jan 2026, 04:45 PM",
                isCompleted = true,
                isCurrent = false,
                details = "Customs export inspection cleared. Palletized for air carriage."
            ),
            TrackingEventEntity(
                trackingNumber = "BUAN-123456789",
                title = "In Transit",
                location = "International Air Space (Flight BU-704)",
                timestamp = "20 Jan 2026, 06:15 AM",
                isCompleted = false,
                isCurrent = true,
                details = "Consignment airborne en route to London Heathrow Gateway Hub."
            ),
            TrackingEventEntity(
                trackingNumber = "BUAN-123456789",
                title = "At Destination",
                location = "London Gateway Logistics Hub, UK",
                timestamp = "Expected 24 Jan 2026",
                isCompleted = false,
                isCurrent = false,
                details = "Pending arrival, offloading and import customs clearance."
            ),
            TrackingEventEntity(
                trackingNumber = "BUAN-123456789",
                title = "Out for Delivery",
                location = "Greater London Courier Depot",
                timestamp = "Expected 25 Jan 2026",
                isCompleted = false,
                isCurrent = false,
                details = "Assigned to BUAN final-mile express delivery courier."
            ),
            TrackingEventEntity(
                trackingNumber = "BUAN-123456789",
                title = "Delivered",
                location = "88 Leadenhall St, London",
                timestamp = "Expected 26 Jan 2026",
                isCompleted = false,
                isCurrent = false,
                details = "Consignee signature and delivery confirmation."
            )
        )
        db.trackingEventDao().insertEvents(eventsBuan1)

        // 4. Seed Hubs
        val sampleHubs = listOf(
            HubEntity(
                id = "HUB-LOS-01",
                name = "Ikeja Air Cargo Central Hub",
                address = "Plot 12 Aviation Estate, Airport Road",
                city = "Ikeja, Lagos",
                state = "Lagos State",
                country = "Nigeria",
                openingHours = "Mon - Sat: 07:00 AM - 09:00 PM",
                servicesAvailable = "Air Cargo, Express Intake, Packaging, Customs Brokerage",
                phone = "+234 1 293 4001",
                email = "ikeja.hub@buanlogistics.com"
            ),
            HubEntity(
                id = "HUB-LOS-02",
                name = "Apapa Marine Port Hub",
                address = "Wharf Road Terminal B, Apapa Seaport",
                city = "Lagos",
                state = "Lagos State",
                country = "Nigeria",
                openingHours = "Mon - Sun: 24/7 Operations",
                servicesAvailable = "Sea Freight, Stevedoring, Container Handling, Warehousing",
                phone = "+234 1 293 4002",
                email = "apapa.hub@buanlogistics.com"
            ),
            HubEntity(
                id = "HUB-ABJ-01",
                name = "Abuja FCT Central Hub",
                address = "Plot 402 Central Business District",
                city = "Abuja",
                state = "Federal Capital Territory",
                country = "Nigeria",
                openingHours = "Mon - Sat: 08:00 AM - 07:00 PM",
                servicesAvailable = "Express Delivery, Road Freight, Hub Pickup, Parcel Drop",
                phone = "+234 9 461 3000",
                email = "abuja.hub@buanlogistics.com"
            ),
            HubEntity(
                id = "HUB-PHC-01",
                name = "Port Harcourt Marine & Energy Hub",
                address = "Trans-Amadi Industrial Layout, Phase 2",
                city = "Port Harcourt",
                state = "Rivers State",
                country = "Nigeria",
                openingHours = "Mon - Sat: 07:30 AM - 08:00 PM",
                servicesAvailable = "Sea Cargo, Vessel Husbandry, Industrial Logistics, Air Charter",
                phone = "+234 84 302 110",
                email = "phc.hub@buanlogistics.com"
            ),
            HubEntity(
                id = "HUB-LON-01",
                name = "London Heathrow Gateway Hub",
                address = "Unit 4 Skyport Drive, Harmondsworth",
                city = "London",
                state = "Greater London",
                country = "United Kingdom",
                openingHours = "Mon - Sun: 06:00 AM - 10:00 PM",
                servicesAvailable = "International Air Inbound/Outbound, Bonded Warehouse, UK Distribution",
                phone = "+44 20 8759 2000",
                email = "london.gateway@buanlogistics.com"
            ),
            HubEntity(
                id = "HUB-DXB-01",
                name = "Dubai South Logistics Hub",
                address = "Al Maktoum International Airport Freezone",
                city = "Dubai",
                state = "Dubai",
                country = "United Arab Emirates",
                openingHours = "24/7 Transshipment Facility",
                servicesAvailable = "Middle East & Africa Transit, Cold Chain, Express Air",
                phone = "+971 4 814 1111",
                email = "dubai.hub@buanlogistics.com"
            )
        )
        db.hubDao().insertHubs(sampleHubs)

        // 5. Seed BUAN-COIN Wallet & Transactions
        val wallet = BuanCoinWalletEntity(
            userId = "USR-001",
            balance = 24,
            totalEarned = 35,
            totalRedeemed = 11,
            pendingRewards = 2
        )
        db.walletDao().insertWallet(wallet)

        val txs = listOf(
            BuanCoinTransactionEntity(
                userId = "USR-001",
                amount = 1,
                title = "Shipment Intake Reward",
                trackingNumber = "BUAN-123456789",
                type = "EARNED",
                date = "20 Jan 2026"
            ),
            BuanCoinTransactionEntity(
                userId = "USR-001",
                amount = 1,
                title = "Shipment Intake Reward",
                trackingNumber = "BUAN-987654321",
                type = "EARNED",
                date = "17 Jan 2026"
            ),
            BuanCoinTransactionEntity(
                userId = "USR-001",
                amount = 10,
                title = "Redeemed for $15 Shipping Voucher",
                trackingNumber = "VOUCHER-FEB-15",
                type = "REDEEMED",
                date = "10 Jan 2026"
            ),
            BuanCoinTransactionEntity(
                userId = "USR-001",
                amount = 5,
                title = "Hub Provider Launch Bonus",
                trackingNumber = "CAMPAIGN-2026",
                type = "EARNED",
                date = "01 Jan 2026"
            )
        )
        txs.forEach { db.walletDao().insertTransaction(it) }

        // 6. Seed Notifications
        val notifications = listOf(
            NotificationEntity(
                title = "Shipment In Transit",
                message = "Your shipment BUAN-123456789 is now en route to London Heathrow Airport (Flight BU-704).",
                type = "SHIPMENT",
                timestamp = "Today, 06:15 AM",
                isRead = false,
                trackingNumber = "BUAN-123456789"
            ),
            NotificationEntity(
                title = "Reward Earned",
                message = "You earned +1 Referral credit for registered shipment consignment BUAN-123456789.",
                type = "REWARD",
                timestamp = "Yesterday, 11:20 AM",
                isRead = false,
                trackingNumber = "BUAN-123456789"
            ),
            NotificationEntity(
                title = "Customs Clearance Complete",
                message = "Air cargo clearance has successfully passed at Murtala Muhammed Airport.",
                type = "SHIPMENT",
                timestamp = "19 Jan 2026",
                isRead = true,
                trackingNumber = "BUAN-123456789"
            ),
            NotificationEntity(
                title = "Shipment Delivered",
                message = "Shipment BUAN-567890123 has been safely delivered to Dubai, UAE.",
                type = "SHIPMENT",
                timestamp = "15 Jan 2026",
                isRead = true,
                trackingNumber = "BUAN-567890123"
            )
        )
        db.notificationDao().insertNotifications(notifications)

        // 7. Seed Sample Quotes
        val sampleQuotes = listOf(
            QuoteRequestEntity(
                referenceId = "QT-748291",
                fromCity = "Lagos, Nigeria",
                toCity = "London Heathrow, UK",
                transportMode = "Air Freight",
                shipmentType = "Commercial Goods",
                weightKg = 85.0,
                quantity = 6,
                cargoDescription = "High-grade Nigerian Ankara & Voile Textiles",
                pickupRequired = true,
                doorToDoor = true,
                estimatedQuoteUsd = 750.0,
                status = "Pending Review",
                createdAt = "Today, 08:30 AM"
            ),
            QuoteRequestEntity(
                referenceId = "QT-639104",
                fromCity = "Kano, Nigeria",
                toCity = "Houston, Texas, USA",
                transportMode = "Sea Freight",
                shipmentType = "Heavy Cargo",
                weightKg = 18500.0,
                quantity = 1,
                cargoDescription = "Containerized Raw Cashew Nuts (20ft FCL)",
                pickupRequired = false,
                doorToDoor = false,
                estimatedQuoteUsd = 4200.0,
                status = "Approved",
                createdAt = "Yesterday, 02:15 PM"
            ),
            QuoteRequestEntity(
                referenceId = "QT-512890",
                fromCity = "London, UK",
                toCity = "Ikeja, Lagos, Nigeria",
                transportMode = "Air Freight",
                shipmentType = "Commercial Goods",
                weightKg = 32.5,
                quantity = 2,
                cargoDescription = "Automotive Electronic Diagnostic Sensors",
                pickupRequired = true,
                doorToDoor = true,
                estimatedQuoteUsd = 380.0,
                status = "Processed",
                createdAt = "18 Jan 2026, 11:45 AM"
            )
        )
        sampleQuotes.forEach { db.quoteDao().insertQuote(it) }

        // 8. Seed Hub Applications
        val sampleHubApps = listOf(
            HubApplicationEntity(
                businessName = "Alaba Central Logistics Depot",
                ownerName = "Grace Eke",
                phone = "+234 813 999 0011",
                email = "grace.eke@alabastation.com",
                businessAddress = "Shop 14, Line 3, Alaba International Market",
                city = "Lagos",
                state = "Lagos State",
                country = "Nigeria",
                businessType = "Electronics & General Retail",
                operatingHours = "8:00 AM - 6:30 PM",
                status = "Approved Hub",
                submittedAt = "15 Jan 2026"
            ),
            HubApplicationEntity(
                businessName = "Wuse II Express Drop-off Point",
                ownerName = "Abubakar Bello",
                phone = "+234 802 444 8811",
                email = "a.bello@wusehub.ng",
                businessAddress = "Plot 12 Aminu Kano Crescent, Wuse II",
                city = "Abuja",
                state = "FCT",
                country = "Nigeria",
                businessType = "Cyber Cafe & Business Services",
                operatingHours = "8:00 AM - 8:00 PM",
                status = "Pending Review",
                submittedAt = "Yesterday, 04:30 PM"
            ),
            HubApplicationEntity(
                businessName = "Oil City Marine Freight Hub",
                ownerName = "Nnamdi Okeke",
                phone = "+234 807 555 3322",
                email = "nnamdi@phoilcityhub.com",
                businessAddress = "Trans-Amadi Industrial Layout",
                city = "Port Harcourt",
                state = "Rivers State",
                country = "Nigeria",
                businessType = "Maritime Equipment & Logistics",
                operatingHours = "7:30 AM - 6:00 PM",
                status = "In Review",
                submittedAt = "Today, 10:15 AM"
            )
        )
        sampleHubApps.forEach { db.hubDao().insertApplication(it) }
    }
}
