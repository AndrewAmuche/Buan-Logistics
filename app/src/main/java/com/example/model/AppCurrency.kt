package com.example.model

import java.util.Locale

/**
 * Currency configuration for BUAN Logistics.
 * The primary currencies are Nigerian Naira (₦) and British Pounds (£),
 * automatically selected based on the user's location.
 */
enum class AppCurrency(
    val code: String,
    val symbol: String,
    val displayName: String,
    val countryName: String,
    val flag: String,
    val rateFromUsd: Double,
    val isPrimary: Boolean = false
) {
    NGN(
        code = "NGN",
        symbol = "₦",
        displayName = "Nigerian Naira",
        countryName = "Nigeria",
        flag = "🇳🇬",
        rateFromUsd = 1550.0,
        isPrimary = true
    ),
    GBP(
        code = "GBP",
        symbol = "£",
        displayName = "British Pound",
        countryName = "United Kingdom",
        flag = "🇬🇧",
        rateFromUsd = 0.78,
        isPrimary = true
    ),
    USD(
        code = "USD",
        symbol = "$",
        displayName = "US Dollar",
        countryName = "United States",
        flag = "🇺🇸",
        rateFromUsd = 1.0,
        isPrimary = false
    ),
    EUR(
        code = "EUR",
        symbol = "€",
        displayName = "Euro",
        countryName = "European Union",
        flag = "🇪🇺",
        rateFromUsd = 0.92,
        isPrimary = false
    ),
    CAD(
        code = "CAD",
        symbol = "CA$",
        displayName = "Canadian Dollar",
        countryName = "Canada",
        flag = "🇨🇦",
        rateFromUsd = 1.36,
        isPrimary = false
    ),
    GHS(
        code = "GHS",
        symbol = "GH₵",
        displayName = "Ghanaian Cedi",
        countryName = "Ghana",
        flag = "🇬🇭",
        rateFromUsd = 15.5,
        isPrimary = false
    ),
    KES(
        code = "KES",
        symbol = "KSh",
        displayName = "Kenyan Shilling",
        countryName = "Kenya",
        flag = "🇰🇪",
        rateFromUsd = 130.0,
        isPrimary = false
    ),
    ZAR(
        code = "ZAR",
        symbol = "R",
        displayName = "South African Rand",
        countryName = "South Africa",
        flag = "🇿🇦",
        rateFromUsd = 18.2,
        isPrimary = false
    );

    fun format(usdAmount: Double): String {
        val converted = usdAmount * rateFromUsd
        return when (this) {
            NGN -> "₦%,.0f".format(converted)
            GBP -> "£%,.2f".format(converted)
            USD -> "$%,.2f".format(converted)
            EUR -> "€%,.2f".format(converted)
            CAD -> "CA$%,.2f".format(converted)
            GHS -> "GH₵%,.2f".format(converted)
            KES -> "KSh%,.0f".format(converted)
            ZAR -> "R%,.2f".format(converted)
        }
    }

    fun formatWithCode(usdAmount: Double): String {
        return "${format(usdAmount)} $code"
    }

    companion object {
        fun fromCountry(country: String): AppCurrency {
            val normalized = country.trim().lowercase()
            return when {
                normalized.contains("nigeria") || normalized == "ng" -> NGN
                normalized.contains("united kingdom") || normalized.contains("uk") ||
                        normalized.contains("britain") || normalized.contains("england") ||
                        normalized.contains("scotland") || normalized.contains("wales") ||
                        normalized == "gb" -> GBP
                normalized.contains("united states") || normalized.contains("usa") || normalized == "us" -> USD
                normalized.contains("germany") || normalized.contains("france") ||
                        normalized.contains("netherlands") || normalized.contains("italy") ||
                        normalized.contains("spain") || normalized.contains("europe") -> EUR
                normalized.contains("canada") || normalized == "ca" -> CAD
                normalized.contains("ghana") || normalized == "gh" -> GHS
                normalized.contains("kenya") || normalized == "ke" -> KES
                normalized.contains("south africa") || normalized == "za" -> ZAR
                else -> NGN // Default to primary currency Naira
            }
        }

        fun fromCode(code: String): AppCurrency {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: NGN
        }

        fun detectFromLocale(): AppCurrency {
            val countryCode = Locale.getDefault().country
            return when (countryCode.uppercase()) {
                "GB" -> GBP
                "NG" -> NGN
                "US" -> USD
                "CA" -> CAD
                "GH" -> GHS
                "KE" -> KES
                "ZA" -> ZAR
                "DE", "FR", "IT", "ES", "NL" -> EUR
                else -> NGN
            }
        }
    }
}
