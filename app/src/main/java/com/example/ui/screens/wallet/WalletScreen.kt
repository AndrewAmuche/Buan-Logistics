package com.example.ui.screens.wallet

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BuanButton
import com.example.ui.components.BuanCard
import com.example.ui.components.BuanTopBar
import com.example.ui.theme.BuanBackground
import com.example.ui.theme.BuanBlueCta
import com.example.ui.theme.BuanBlueLight
import com.example.ui.theme.BuanBluePrimary
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
fun BuanCoinWalletScreen(viewModel: BuanViewModel) {
    BackHandler { viewModel.navigateBack() }
    val context = LocalContext.current

    val referralCode by viewModel.referralCouponCode.collectAsState()
    val referralCount by viewModel.referralCount.collectAsState()
    val cashbackBalance by viewModel.cashbackBalance.collectAsState()
    val isFreeShippingUnlocked by viewModel.isFreeShippingUnlocked.collectAsState()
    val isFreeShippingApplied by viewModel.isFreeShippingApplied.collectAsState()

    val targetReferrals = 7
    val progress = (referralCount.toFloat() / targetReferrals.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "referral_progress")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BuanBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            BuanTopBar(
                title = "Referral & Cashback Rewards",
                onBackClick = { viewModel.navigateBack() }
            )
        }

        // Section 1: Unique Coupon Code Card
        item {
            BuanCard(
                glowEffect = referralCode != null,
                backgroundColor = BuanSurfaceElevated
            ) {
                if (referralCode == null) {
                    // Not yet requested: Invite user to issue code
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = BuanBluePrimary.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.shadow(
                                    elevation = 2.dp,
                                    shape = RoundedCornerShape(8.dp),
                                    ambientColor = Color(0x33000000),
                                    spotColor = Color(0x33000000)
                                )
                            ) {
                                Text(
                                    text = "UNIQUE SHIPPER COUPON",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = BuanBlueLight,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.Loyalty,
                                contentDescription = null,
                                tint = BuanBlueLight,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Get Your Unique Coupon Code",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Request your personalized coupon code. When 7 friends register with your code or referral link, you automatically receive 100% Free Shipping and instant cashback!",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        BuanButton(
                            text = "Request My Unique Coupon Code",
                            onClick = { viewModel.requestUniqueCouponCode() },
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.Default.CardGiftcard,
                            testTag = "request_coupon_button"
                        )
                    }
                } else {
                    // Unique code issued: Display modern voucher card
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "YOUR PERSONAL COUPON CODE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF10B981),
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Surface(
                                color = Color(0xFF10B981).copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "ACTIVE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF10B981),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Unique code highlight box with box shadow
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(12.dp),
                                    ambientColor = Color(0x33000000),
                                    spotColor = Color(0x442563EB)
                                ),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF101726)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "COUPON / PROMO CODE",
                                        fontSize = 10.sp,
                                        color = TextMuted,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = referralCode ?: "",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontFamily = FontFamily.Monospace,
                                        color = BuanBlueLight,
                                        letterSpacing = 1.5.sp
                                    )
                                }

                                Surface(
                                    color = BuanBlueCta,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .shadow(
                                            elevation = 3.dp,
                                            shape = RoundedCornerShape(10.dp),
                                            ambientColor = Color(0x33000000),
                                            spotColor = Color(0x33000000)
                                        )
                                        .clickable { viewModel.copyCouponCodeToClipboard(context) }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "Copy",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Copy",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Share Referral Link Button with box shadow
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 3.dp,
                                    shape = RoundedCornerShape(12.dp),
                                    ambientColor = Color(0x33000000),
                                    spotColor = Color(0x33000000)
                                )
                                .clickable { viewModel.shareReferralCode(context) },
                            shape = RoundedCornerShape(12.dp),
                            color = BuanSurfaceVariant
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = null,
                                        tint = BuanBlueLight,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Share Referral Link",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "https://buanlogistics.com/ref?code=${referralCode}",
                                            fontSize = 10.sp,
                                            color = TextMuted,
                                            maxLines = 1
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Default.PersonAdd,
                                    contentDescription = null,
                                    tint = BuanBlueLight,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 2: 7-Referrals Milestone Card (Cashback & Free Shipping)
        item {
            BuanCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "7 REFERRALS MILESTONE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BuanCoinGold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Free Shipping + Cashback",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .shadow(
                                elevation = 4.dp,
                                shape = CircleShape,
                                ambientColor = Color(0x33000000),
                                spotColor = Color(0x44FBBF24)
                            )
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(BuanCoinGold.copy(alpha = 0.3f), Color(0xFF1E1B18))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = null,
                            tint = BuanCoinGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Bar & Counts
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$referralCount of $targetReferrals Referrals",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isFreeShippingUnlocked) Color(0xFF10B981) else TextPrimary
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = BuanBlueLight
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (isFreeShippingUnlocked) Color(0xFF10B981) else BuanBlueCta,
                    trackColor = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Milestone status callout
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(10.dp),
                            ambientColor = Color(0x22000000),
                            spotColor = Color(0x22000000)
                        ),
                    shape = RoundedCornerShape(10.dp),
                    color = if (isFreeShippingUnlocked) Color(0xFF0F291E) else Color(0xFF161E2E)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isFreeShippingUnlocked) Icons.Default.CheckCircle else Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint = if (isFreeShippingUnlocked) Color(0xFF10B981) else BuanBlueLight,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isFreeShippingUnlocked) {
                                "Congratulations! You have referred $referralCount people. Free Shipping & Cashback are unlocked and ready to use!"
                            } else {
                                "Refer ${targetReferrals - referralCount} more people with your coupon code or referral link to unlock 100% Free Shipping and $50 Cashback."
                            },
                            fontSize = 11.sp,
                            color = if (isFreeShippingUnlocked) Color(0xFF6EE7B7) else TextSecondary,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Rewards Unlocked Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Perk 1: Free Shipping
                    RewardPerkCard(
                        title = "100% Free Shipping",
                        subtitle = "Valid for any air/sea consignment",
                        icon = Icons.Default.LocalShipping,
                        isUnlocked = isFreeShippingUnlocked,
                        modifier = Modifier.weight(1f)
                    )

                    // Perk 2: Cashback Bonus
                    RewardPerkCard(
                        title = "$50.00 Cashback",
                        subtitle = "Instant freight credit bonus",
                        icon = Icons.Default.MonetizationOn,
                        isUnlocked = isFreeShippingUnlocked,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons with Box Shadows: Apply Free Shipping & Test Simulate Button
                if (isFreeShippingUnlocked) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(12.dp),
                                ambientColor = Color(0x33000000),
                                spotColor = Color(0x4410B981)
                            )
                            .clickable { viewModel.toggleFreeShippingPerk() },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isFreeShippingApplied) Color(0xFF047857) else Color(0xFF10B981)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = if (isFreeShippingApplied) Icons.Default.Check else Icons.Default.ElectricBolt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isFreeShippingApplied) "Free Shipping Active on Quote/Book" else "Activate 100% Free Shipping Voucher",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Interactive Simulator for reviewers & users to test reaching 7 referrals
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(10.dp),
                            ambientColor = Color(0x22000000),
                            spotColor = Color(0x22000000)
                        )
                        .clickable { viewModel.simulateReferralRegistration() },
                    shape = RoundedCornerShape(10.dp),
                    color = BuanSurfaceVariant
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GroupAdd,
                            contentDescription = null,
                            tint = BuanBlueLight,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+ Simulate Friend Registering With My Code",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BuanBlueLight
                        )
                    }
                }
            }
        }

        // Section 3: Cashback Wallet & Balance Card
        item {
            BuanCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "AVAILABLE CASHBACK BALANCE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$${String.format("%.2f", cashbackBalance)} USD",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF10B981)
                        )
                    }

                    Surface(
                        color = Color(0xFF10B981).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(8.dp)
                        )
                    ) {
                        Text(
                            text = "CASHBACK",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = BuanBorder, thickness = 1.dp)
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CashbackMetric(label = "Total Referrals", value = "$referralCount Shippers")
                    CashbackMetric(label = "Target", value = "7 Referrals")
                    CashbackMetric(
                        label = "Free Shipping",
                        value = if (isFreeShippingUnlocked) "UNLOCKED" else "LOCKED",
                        color = if (isFreeShippingUnlocked) Color(0xFF10B981) else TextMuted
                    )
                }
            }
        }

        // Section 4: How the Referral Program Works
        item {
            BuanCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = BuanBlueLight,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "How Cashback & Free Shipping Work",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                val referralSteps = listOf(
                    "1. Request Your Unique Coupon Code" to "Generate your personal promo code with a single tap. It never expires.",
                    "2. Share With Friends & Colleagues" to "Share your coupon code or referral link with anyone booking air, sea, or road cargo.",
                    "3. 7 Successful Registrations" to "When 7 people sign up using your unique coupon code or referral link, your milestone is unlocked.",
                    "4. Enjoy 100% Free Shipping + Cashback" to "Instantly apply your Free Shipping voucher to any consignment and receive $50 cashback credited to your account!"
                )

                referralSteps.forEach { (title, description) ->
                    Row(
                        modifier = Modifier.padding(vertical = 5.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .size(8.dp)
                                .shadow(
                                    elevation = 2.dp,
                                    shape = CircleShape,
                                    ambientColor = Color(0x33000000),
                                    spotColor = Color(0x332563EB)
                                )
                                .clip(CircleShape)
                                .background(BuanBlueCta)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = description,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(72.dp)) }
    }
}

@Composable
private fun RewardPerkCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isUnlocked: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.shadow(
            elevation = if (isUnlocked) 4.dp else 1.dp,
            shape = RoundedCornerShape(12.dp),
            ambientColor = Color(0x22000000),
            spotColor = if (isUnlocked) Color(0x4410B981) else Color(0x22000000)
        ),
        shape = RoundedCornerShape(12.dp),
        color = if (isUnlocked) Color(0xFF0D2419) else BuanSurfaceVariant
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isUnlocked) Color(0xFF10B981) else TextMuted,
                    modifier = Modifier.size(20.dp)
                )
                Surface(
                    color = if (isUnlocked) Color(0xFF10B981).copy(alpha = 0.2f) else Color(0xFF334155),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = if (isUnlocked) "UNLOCKED" else "7 REFS",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isUnlocked) Color(0xFF10B981) else TextMuted,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isUnlocked) TextPrimary else TextSecondary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = TextMuted,
                lineHeight = 13.sp
            )
        }
    }
}

@Composable
private fun CashbackMetric(
    label: String,
    value: String,
    color: Color = TextPrimary
) {
    Column {
        Text(text = label, fontSize = 11.sp, color = TextMuted)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
