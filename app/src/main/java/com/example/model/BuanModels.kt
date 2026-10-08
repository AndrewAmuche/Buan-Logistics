package com.example.model

enum class UserRole(val displayName: String) {
    CUSTOMER("Customer"),
    HUB_PROVIDER("Hub Provider"),
    BUSINESS_CUSTOMER("Business Customer"),
    CORPORATE_PARTNER("Corporate Partner"),
    ADMIN("Administrator")
}

enum class AccountType(val displayName: String) {
    PERSONAL("Personal Account"),
    BUSINESS("Business Account");

    companion object {
        fun fromDisplayName(name: String): AccountType {
            return entries.find { it.displayName.equals(name, ignoreCase = true) || it.name.equals(name, ignoreCase = true) } ?: PERSONAL
        }
    }
}

enum class CorporateTier(val displayName: String, val benefits: String) {
    NONE("Standard", "Standard shipping rates and customer support"),
    SILVER("BUAN Silver", "5% discount, priority clearance, dedicated ticketing"),
    GOLD("BUAN Gold", "12% discount, scheduled pickups, warehousing support, express lane"),
    DIAMOND("BUAN Diamond", "20% discount, dedicated account manager, chartering priority, 24/7 hotline")
}

enum class TransportMode(val code: String, val title: String, val transitEstimate: String) {
    SEA("SEA", "Sea Freight", "15 - 28 Days"),
    AIR("AIR", "Air Freight", "3 - 5 Days"),
    ROAD("ROAD", "Road Freight", "2 - 7 Days"),
    EXPRESS("EXPRESS", "Express International", "1 - 3 Days")
}

enum class ShipmentType(val title: String) {
    DOCUMENT("Document"),
    PARCEL("Parcel"),
    CARGO("Heavy Cargo"),
    COMMERCIAL_GOODS("Commercial Goods"),
    PERSONAL_EFFECTS("Personal Effects"),
    PERISHABLE_GOODS("Perishable Goods")
}

enum class ShipmentStatus(val displayName: String, val stepIndex: Int) {
    CREATED("Shipment Created", 0),
    REGISTERED("Shipment Registered", 1),
    PICKED_UP("Picked Up", 2),
    PROCESSING("Processing", 3),
    IN_TRANSIT("In Transit", 4),
    AT_DESTINATION("At Destination", 5),
    OUT_FOR_DELIVERY("Out for Delivery", 6),
    DELIVERED("Delivered", 7),
    CANCELLED("Cancelled", -1);

    companion object {
        fun fromString(value: String): ShipmentStatus {
            return entries.firstOrNull { it.displayName.equals(value, ignoreCase = true) || it.name.equals(value, ignoreCase = true) }
                ?: IN_TRANSIT
        }
    }
}
