package com.example.model

enum class SubscriptionTier(
    val id: String,
    val displayName: String,
    val monthlyPricePounds: Int,
    val annualPricePounds: Int,
    val freightDiscountPercent: Int,
    val coinMultiplier: String,
    val freeStorageDays: Int,
    val insuranceCoverage: String,
    val tag: String,
    val shortDescription: String,
    val benefits: List<String>
) {
    NONE(
        id = "NONE",
        displayName = "Standard Free",
        monthlyPricePounds = 0,
        annualPricePounds = 0,
        freightDiscountPercent = 0,
        coinMultiplier = "1x",
        freeStorageDays = 3,
        insuranceCoverage = "Basic carrier liability",
        tag = "BASIC",
        shortDescription = "Standard rates & regular freight dispatch",
        benefits = listOf(
            "Standard Air & Ocean shipping rates",
            "Real-time GPS tracking & SMS updates",
            "Access to nationwide Hub network drop-off",
            "1x BUAN-COIN loyalty points",
            "3 Days free warehouse storage",
            "Standard in-app help desk support"
        )
    ),
    SILVER(
        id = "SILVER",
        displayName = "Buan Silver",
        monthlyPricePounds = 50,
        annualPricePounds = 500,
        freightDiscountPercent = 5,
        coinMultiplier = "2x",
        freeStorageDays = 10,
        insuranceCoverage = "£5,000 comprehensive cargo protection",
        tag = "POPULAR ENTRY",
        shortDescription = "Ideal for regular personal and SME cross-border senders",
        benefits = listOf(
            "5% Flat Discount on all Air & Ocean freight shipments",
            "3 Free custom consolidation & repacking boxes monthly",
            "Expedited priority port & airport customs intake (PAAR fast-track)",
            "2x BUAN-COIN rewards on every shipment booking",
            "10 Days free storage at London (Heathrow) and Lagos Hubs",
            "Comprehensive cargo protection up to £5,000 included",
            "Priority email and WhatsApp logistics agent support"
        )
    ),
    GOLD(
        id = "GOLD",
        displayName = "Buan Gold",
        monthlyPricePounds = 80,
        annualPricePounds = 800,
        freightDiscountPercent = 10,
        coinMultiplier = "3x",
        freeStorageDays = 21,
        insuranceCoverage = "£15,000 comprehensive cargo protection",
        tag = "MOST POPULAR",
        shortDescription = "For high-volume merchants, e-commerce stores & growing businesses",
        benefits = listOf(
            "10% Flat Discount on all Air, Sea & Overland cargo",
            "Unlimited free parcel consolidation & protective packaging",
            "Dedicated Account Concierge with direct telephone hotline",
            "VIP priority queue for fast customs clearance & bill of lading release",
            "3x BUAN-COIN rewards + £25 quarterly shipping rebate credit",
            "21 Days extended free storage at all global Hubs (London, Lagos, Houston)",
            "Free scheduled home/office pickup across Lagos & Greater London",
            "Comprehensive loss & damage cargo insurance up to £15,000"
        )
    ),
    DIAMOND(
        id = "DIAMOND",
        displayName = "Buan Diamond",
        monthlyPricePounds = 100,
        annualPricePounds = 1000,
        freightDiscountPercent = 15,
        coinMultiplier = "5x",
        freeStorageDays = 45,
        insuranceCoverage = "£50,000 zero-excess global maritime & air protection",
        tag = "ELITE ENTERPRISE",
        shortDescription = "Exclusive corporate-tier logistics with top priority and maximum savings",
        benefits = listOf(
            "15% Maximum Flat Discount on all international freight lanes",
            "Personal Senior Freight Director assigned to your enterprise",
            "Guaranteed cargo flight & vessel space allocation during peak seasons",
            "Same-day express customs release with pre-arrival clearance",
            "5x BUAN-COIN rewards + exclusive monthly freight rebate credits",
            "45 Days unlimited free warehousing & consolidation worldwide",
            "Zero-excess comprehensive marine & air cargo coverage up to £50,000",
            "Free unlimited door-to-door courier pickups & white-glove handling",
            "Customs tariff consulting, PAAR fast-tracking & Form M assistance"
        )
    );

    companion object {
        fun fromId(id: String?): SubscriptionTier {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: NONE
        }
    }
}
