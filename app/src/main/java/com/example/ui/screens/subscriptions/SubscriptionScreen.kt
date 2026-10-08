package com.example.ui.screens.subscriptions

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SubscriptionTier
import com.example.ui.components.BuanButton
import com.example.ui.components.BuanCard
import com.example.ui.components.BuanOfficialBusinessLogo
import com.example.ui.components.BuanSecondaryButton
import com.example.ui.components.BuanTopBar
import com.example.ui.theme.BuanBackground
import com.example.ui.theme.BuanBlueCta
import com.example.ui.theme.BuanBlueLight
import com.example.ui.theme.BuanBlueSubtle
import com.example.ui.theme.BuanBorder
import com.example.ui.theme.BuanCoinGold
import com.example.ui.theme.BuanSurface
import com.example.ui.theme.BuanSurfaceElevated
import com.example.ui.theme.BuanSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BuanViewModel

@Composable
fun SubscriptionScreen(viewModel: BuanViewModel) {
    BackHandler { viewModel.navigateBack() }

    val user by viewModel.currentUser.collectAsState()
    val userCurrency by viewModel.userCurrency.collectAsState()
    val context = LocalContext.current

    var isAnnualBilling by remember { mutableStateOf(false) }
    var selectedTierForCheckout by remember { mutableStateOf<SubscriptionTier?>(null) }
    var showCheckoutDialog by remember { mutableStateOf(false) }
    var showSuccessCelebration by remember { mutableStateOf(false) }
    var selectedPaymentMethod by remember { mutableStateOf("card") } // "wallet", "card", "transfer"

    val currentTier = SubscriptionTier.fromId(user?.subscriptionTier)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BuanBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            BuanTopBar(
                title = "Membership Packages",
                onBackClick = { viewModel.navigateBack() }
            )
        }

        // 1. Current Active Tier Status Card
        item {
            val tierGradient = when (currentTier) {
                SubscriptionTier.DIAMOND -> listOf(Color(0xFF0E7490), Color(0xFF06B6D4), Color(0xFF0891B2))
                SubscriptionTier.GOLD -> listOf(Color(0xFFB45309), Color(0xFFF59E0B), Color(0xFFD97706))
                SubscriptionTier.SILVER -> listOf(Color(0xFF475569), Color(0xFF64748B), Color(0xFF94A3B8))
                SubscriptionTier.NONE -> listOf(Color(0xFF1E293B), Color(0xFF0F172A))
            }
            val tierIcon = when (currentTier) {
                SubscriptionTier.DIAMOND -> Icons.Default.Diamond
                SubscriptionTier.GOLD -> Icons.Default.Star
                SubscriptionTier.SILVER -> Icons.Default.Security
                SubscriptionTier.NONE -> Icons.Default.AllInbox
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color(0x33000000),
                        spotColor = if (currentTier != SubscriptionTier.NONE) Color(0x552563EB) else Color.Transparent
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 1.dp,
                        color = if (currentTier != SubscriptionTier.NONE) Color(0xFF38BDF8).copy(alpha = 0.5f) else BuanBorder,
                        shape = RoundedCornerShape(16.dp)
                    ),
                color = BuanSurface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Brush.linearGradient(tierGradient)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = tierIcon,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Surface(
                                    color = if (currentTier != SubscriptionTier.NONE) Color(0x2210B981) else BuanSurfaceVariant,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = if (currentTier != SubscriptionTier.NONE) "ACTIVE MEMBERSHIP" else "STANDARD PLAN",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (currentTier != SubscriptionTier.NONE) Color(0xFF10B981) else TextMuted,
                                        letterSpacing = 0.5.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = currentTier.displayName,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }

                        if (currentTier != SubscriptionTier.NONE) {
                            Surface(
                                color = BuanBlueSubtle,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "${currentTier.freightDiscountPercent}% Off Freight",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BuanBlueLight,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    if (currentTier != SubscriptionTier.NONE) {
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = BuanBorder, thickness = 0.5.dp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Renews on: ${user?.subscriptionExpiresAt?.ifBlank { "In 30 days" }}",
                                fontSize = 11.5.sp,
                                color = TextMuted
                            )
                            Text(
                                text = "Cancel / Downgrade",
                                fontSize = 11.5.sp,
                                color = Color(0xFFEF4444),
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable {
                                    viewModel.upgradeSubscription(SubscriptionTier.NONE)
                                    Toast.makeText(context, "Membership switched to Standard Free", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        }

        // 2. Billing Cycle Segmented Switcher (Monthly vs Annual with Savings)
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Upgrade and Save on Every Shipment",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Priority clearance, warehouse perks & lower shipping tariffs",
                    fontSize = 12.5.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    color = BuanSurface,
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, BuanBorder),
                    modifier = Modifier.shadow(2.dp, RoundedCornerShape(24.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { isAnnualBilling = false },
                            color = if (!isAnnualBilling) BuanBlueCta else Color.Transparent,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = "Monthly Billing",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (!isAnnualBilling) Color.White else TextSecondary,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }

                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { isAnnualBilling = true },
                            color = if (isAnnualBilling) BuanBlueCta else Color.Transparent,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Annual Billing",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isAnnualBilling) Color.White else TextSecondary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = if (isAnnualBilling) Color(0xFF10B981) else Color(0x3310B981),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "SAVE ~17%",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isAnnualBilling) Color.White else Color(0xFF10B981),
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Package 1: BUAN SILVER (£50 / month)
        item {
            SubscriptionTierCard(
                tier = SubscriptionTier.SILVER,
                isAnnual = isAnnualBilling,
                isCurrent = currentTier == SubscriptionTier.SILVER,
                headerColor = Color(0xFF94A3B8),
                accentGradient = listOf(Color(0xFF334155), Color(0xFF475569)),
                badgeText = "POPULAR ENTRY",
                onSelect = {
                    selectedTierForCheckout = SubscriptionTier.SILVER
                    showCheckoutDialog = true
                }
            )
        }

        // 4. Package 2: BUAN GOLD (£80 / month - Highlighted)
        item {
            SubscriptionTierCard(
                tier = SubscriptionTier.GOLD,
                isAnnual = isAnnualBilling,
                isCurrent = currentTier == SubscriptionTier.GOLD,
                isHighlighted = true,
                headerColor = Color(0xFFF59E0B),
                accentGradient = listOf(Color(0xFFB45309), Color(0xFFD97706)),
                badgeText = "MOST POPULAR • BEST VALUE",
                onSelect = {
                    selectedTierForCheckout = SubscriptionTier.GOLD
                    showCheckoutDialog = true
                }
            )
        }

        // 5. Package 3: BUAN DIAMOND (£100 / month - Enterprise)
        item {
            SubscriptionTierCard(
                tier = SubscriptionTier.DIAMOND,
                isAnnual = isAnnualBilling,
                isCurrent = currentTier == SubscriptionTier.DIAMOND,
                headerColor = Color(0xFF06B6D4),
                accentGradient = listOf(Color(0xFF0E7490), Color(0xFF0891B2)),
                badgeText = "ENTERPRISE ELITE",
                onSelect = {
                    selectedTierForCheckout = SubscriptionTier.DIAMOND
                    showCheckoutDialog = true
                }
            )
        }

        // 6. Enterprise / Corporate Custom Chartering Banner
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp)),
                color = Color(0xFF0F172A),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0x2238BDF8)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SupportAgent,
                            contentDescription = null,
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Need Custom Full-Container or Chartering?",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Bespoke corporate logistics contracts for trade enterprises moving >20 tons monthly.",
                            fontSize = 11.5.sp,
                            color = TextMuted,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Official Business Logo Footer
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BuanOfficialBusinessLogo(
                    fontSize = 20.sp,
                    taglineSize = 9.sp,
                    showTagline = true,
                    primaryColor = BuanBlueLight
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "All memberships backed by BUAN Logistics Carrier Guarantee",
                    fontSize = 10.5.sp,
                    color = TextMuted
                )
            }
        }

        item { Spacer(modifier = Modifier.height(64.dp)) }
    }

    // Checkout Confirmation Dialog
    if (showCheckoutDialog && selectedTierForCheckout != null) {
        val tier = selectedTierForCheckout!!
        val price = if (isAnnualBilling) "£${tier.annualPricePounds}/year" else "£${tier.monthlyPricePounds}/month"
        val nairaEstimate = if (isAnnualBilling) {
            "≈ ₦${String.format("%,d", tier.annualPricePounds * 2000)}"
        } else {
            "≈ ₦${String.format("%,d", tier.monthlyPricePounds * 2000)}"
        }

        AlertDialog(
            onDismissRequest = { showCheckoutDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BuanBlueCta),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.ElectricBolt, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Confirm Upgrade", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "You are activating ${tier.displayName} membership.",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = BuanSurfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Selected Plan", fontSize = 12.sp, color = TextMuted)
                                Text(tier.displayName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Billing Frequency", fontSize = 12.sp, color = TextMuted)
                                Text(if (isAnnualBilling) "Annual (Save 17%)" else "Monthly", fontSize = 12.sp, color = BuanBlueLight, fontWeight = FontWeight.SemiBold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Amount", fontSize = 12.sp, color = TextMuted)
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(price, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF10B981))
                                    Text(nairaEstimate, fontSize = 10.5.sp, color = TextMuted)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = BuanBorder, thickness = 0.5.dp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("${tier.freightDiscountPercent}% off all freight lanes immediately active", fontSize = 11.5.sp, color = TextPrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Select Payment Method", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))

                    PaymentOptionRow(
                        title = "BUAN Wallet Balance",
                        subtitle = "Instant deduction from ₦ balance / coins",
                        selected = selectedPaymentMethod == "wallet",
                        onSelect = { selectedPaymentMethod = "wallet" }
                    )
                    PaymentOptionRow(
                        title = "Debit / Credit Card",
                        subtitle = "Visa, Mastercard, Verve",
                        selected = selectedPaymentMethod == "card",
                        onSelect = { selectedPaymentMethod = "card" }
                    )
                    PaymentOptionRow(
                        title = "UK / Nigerian Bank Transfer",
                        subtitle = "Direct commercial settlement",
                        selected = selectedPaymentMethod == "transfer",
                        onSelect = { selectedPaymentMethod = "transfer" }
                    )
                }
            },
            confirmButton = {
                BuanButton(
                    text = "Activate Now ($price)",
                    onClick = {
                        val billing = if (isAnnualBilling) "ANNUAL" else "MONTHLY"
                        viewModel.upgradeSubscription(tier, billing) {
                            showCheckoutDialog = false
                            showSuccessCelebration = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            dismissButton = {
                TextButton(onClick = { showCheckoutDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            containerColor = BuanSurface
        )
    }

    // Success Celebration Modal
    if (showSuccessCelebration) {
        val activated = selectedTierForCheckout ?: SubscriptionTier.GOLD
        AlertDialog(
            onDismissRequest = { showSuccessCelebration = false },
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Upgrade Successful!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome to ${activated.displayName}! Your account has been upgraded with exclusive shipping benefits.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = Color(0x2210B981),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("• ${activated.freightDiscountPercent}% Flat discount on bookings", fontSize = 12.sp, color = TextPrimary)
                            Text("• ${activated.freeStorageDays} Days free Hub warehouse storage", fontSize = 12.sp, color = TextPrimary)
                            Text("• ${activated.coinMultiplier} Loyalty BUAN-COIN rewards", fontSize = 12.sp, color = TextPrimary)
                            Text("• ${activated.insuranceCoverage}", fontSize = 12.sp, color = TextPrimary)
                        }
                    }
                }
            },
            confirmButton = {
                BuanButton(
                    text = "Done",
                    onClick = {
                        showSuccessCelebration = false
                        viewModel.navigateBack()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            containerColor = BuanSurface
        )
    }
}

@Composable
private fun SubscriptionTierCard(
    tier: SubscriptionTier,
    isAnnual: Boolean,
    isCurrent: Boolean,
    headerColor: Color,
    accentGradient: List<Color>,
    badgeText: String,
    isHighlighted: Boolean = false,
    onSelect: () -> Unit
) {
    val priceString = if (isAnnual) "£${tier.annualPricePounds}" else "£${tier.monthlyPricePounds}"
    val periodString = if (isAnnual) "/year" else "/month"
    val nairaEquiv = if (isAnnual) {
        "≈ ₦${String.format("%,d", tier.annualPricePounds * 2000)}/yr"
    } else {
        "≈ ₦${String.format("%,d", tier.monthlyPricePounds * 2000)}/mo"
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isHighlighted) 8.dp else 3.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = Color(0x33000000),
                spotColor = if (isHighlighted) Color(0x66F59E0B) else Color(0x22000000)
            )
            .clip(RoundedCornerShape(18.dp))
            .border(
                width = if (isHighlighted) 2.dp else 1.dp,
                color = if (isHighlighted) Color(0xFFF59E0B) else if (isCurrent) Color(0xFF10B981) else BuanBorder,
                shape = RoundedCornerShape(18.dp)
            ),
        color = BuanSurface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Badge & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = headerColor.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(0.5.dp, headerColor.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = headerColor,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }

                if (isCurrent) {
                    Surface(
                        color = Color(0x2210B981),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("CURRENT PLAN", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title & Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = tier.displayName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                    Text(
                        text = tier.shortDescription,
                        fontSize = 11.5.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = priceString,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = headerColor
                        )
                        Text(
                            text = periodString,
                            fontSize = 12.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(bottom = 2.dp, start = 2.dp)
                        )
                    }
                    Text(
                        text = nairaEquiv,
                        fontSize = 10.5.sp,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = BuanBorder, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(12.dp))

            // Key Highlights Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                TierHighlightChip(text = "${tier.freightDiscountPercent}% Off Freight", color = headerColor, modifier = Modifier.weight(1f))
                TierHighlightChip(text = "${tier.freeStorageDays}d Free Hub Storage", color = BuanBlueLight, modifier = Modifier.weight(1f))
                TierHighlightChip(text = "${tier.coinMultiplier} BUAN Coins", color = BuanCoinGold, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Feature checklist
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                tier.benefits.forEach { benefit ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(headerColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = headerColor,
                                modifier = Modifier.size(11.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = benefit,
                            fontSize = 12.sp,
                            color = TextPrimary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // CTA Button
            if (isCurrent) {
                Surface(
                    color = BuanSurfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Active Plan",
                        color = TextMuted,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
            } else {
                BuanButton(
                    text = "Upgrade to ${tier.displayName}",
                    onClick = onSelect,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun TierHighlightChip(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(0.5.dp, color.copy(alpha = 0.3f)),
        modifier = modifier
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun PaymentOptionRow(
    title: String,
    subtitle: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        color = if (selected) BuanBlueSubtle else BuanSurfaceElevated,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, if (selected) BuanBlueCta else BuanBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clickable(onClick = onSelect)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = BuanBlueCta,
                    unselectedColor = TextMuted
                ),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(subtitle, fontSize = 10.5.sp, color = TextMuted)
            }
        }
    }
}
