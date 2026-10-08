package com.example.ui.screens.profile

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.model.SubscriptionTier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.AccountType
import com.example.model.AppCurrency
import com.example.model.UserRole
import com.example.ui.components.BuanButton
import com.example.ui.components.BuanCard
import com.example.ui.components.BuanOfficialBusinessLogo
import com.example.ui.components.BuanSecondaryButton
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
import com.example.ui.viewmodel.Screen

@Composable
fun ProfileScreen(viewModel: BuanViewModel) {
    BackHandler { viewModel.navigateBack() }

    val user by viewModel.currentUser.collectAsState()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val userCountry by viewModel.userCountry.collectAsState()
    val userCurrency by viewModel.userCurrency.collectAsState()
    val context = LocalContext.current
    var showRoleModal by remember { mutableStateOf(false) }
    var showPolicyModal by remember { mutableStateOf<String?>(null) }
    var showCurrencyModal by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BuanBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            BuanTopBar(
                title = "Profile & Account",
                onBackClick = { viewModel.navigateBack() }
            )
        }

        // Profile Identity Card
        item {
            BuanCard(
                glowEffect = true,
                backgroundColor = BuanSurface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile Avatar with Camera badge
                    Box(
                        contentAlignment = Alignment.BottomEnd,
                        modifier = Modifier
                            .size(68.dp)
                            .clickable { viewModel.navigateTo(Screen.EditProfile) }
                    ) {
                        if (user?.profilePictureUri != null) {
                            AsyncImage(
                                model = user?.profilePictureUri,
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(66.dp)
                                    .clip(CircleShape)
                                    .border(2.5.dp, BuanBlueCta, CircleShape)
                                    .shadow(6.dp, CircleShape)
                            )
                        } else {
                            val preset = AVATAR_PRESETS.find { it.id == user?.avatarPreset } ?: AVATAR_PRESETS.first()
                            Box(
                                modifier = Modifier
                                    .size(66.dp)
                                    .clip(CircleShape)
                                    .background(Brush.linearGradient(preset.bgGradient))
                                    .border(2.5.dp, BuanBlueLight.copy(alpha = 0.7f), CircleShape)
                                    .shadow(6.dp, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = preset.icon,
                                    contentDescription = preset.title,
                                    tint = Color.White,
                                    modifier = Modifier.size(34.dp)
                                )
                            }
                        }

                        // Edit camera badge
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(BuanBlueCta)
                                .border(1.5.dp, BuanSurface, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Edit photo",
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = user?.fullName ?: "Babajide Adeyemi",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            if (user?.isVerified == true) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Verified Account",
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Text(
                            text = user?.jobTitle ?: "Import & Logistics Director",
                            fontSize = 12.sp,
                            color = BuanBlueLight,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = user?.email ?: "babajide@buanlogistics.com",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(BuanBlueSubtle)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = user?.role ?: "Customer",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BuanBlueLight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = BuanBorder, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(10.dp))

                // Contact & Address Quick Information Display
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = TextMuted, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(user?.phone ?: "+234 803 555 0192", fontSize = 11.sp, color = TextSecondary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Home, contentDescription = null, tint = TextMuted, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${user?.streetAddress ?: "14 Marina Boulevard"}, ${user?.city ?: "Lagos"}",
                                fontSize = 11.sp,
                                color = TextMuted,
                                maxLines = 1
                            )
                        }
                    }

                    // Edit Profile Button
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = BuanBlueSubtle,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BuanBlueLight.copy(alpha = 0.4f)),
                        modifier = Modifier.clickable { viewModel.navigateTo(Screen.EditProfile) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = BuanBlueLight, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit Profile", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BuanBlueLight)
                        }
                    }
                }
            }
        }

        // Premium Membership Tier Banner
        item {
            val subTier = SubscriptionTier.fromId(user?.subscriptionTier)
            val tierGradient = when (subTier) {
                SubscriptionTier.DIAMOND -> listOf(Color(0xFF0E7490), Color(0xFF0891B2))
                SubscriptionTier.GOLD -> listOf(Color(0xFFB45309), Color(0xFFD97706))
                SubscriptionTier.SILVER -> listOf(Color(0xFF334155), Color(0xFF475569))
                SubscriptionTier.NONE -> listOf(Color(0xFF1E293B), Color(0xFF0F172A))
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(14.dp),
                        ambientColor = Color(0x22000000),
                        spotColor = Color(0x442563EB)
                    )
                    .clip(RoundedCornerShape(14.dp))
                    .border(
                        width = 1.dp,
                        color = if (subTier != SubscriptionTier.NONE) Color(0xFFF59E0B).copy(alpha = 0.6f) else Color(0xFF2563EB).copy(alpha = 0.3f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clickable { viewModel.navigateTo(Screen.Subscriptions) }
                    .testTag("profile_subscription_card"),
                color = BuanSurface
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(tierGradient)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (subTier) {
                                SubscriptionTier.DIAMOND -> Icons.Default.Diamond
                                SubscriptionTier.GOLD -> Icons.Default.Diamond
                                SubscriptionTier.SILVER -> Icons.Default.Shield
                                SubscriptionTier.NONE -> Icons.Default.CardGiftcard
                            },
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = if (subTier != SubscriptionTier.NONE) subTier.displayName else "Membership",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            Surface(
                                color = if (subTier != SubscriptionTier.NONE) Color(0x2210B981) else Color(0x1F2563EB),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = if (subTier != SubscriptionTier.NONE) "ACTIVE" else "UPGRADE",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (subTier != SubscriptionTier.NONE) Color(0xFF10B981) else BuanBlueLight,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (subTier != SubscriptionTier.NONE) {
                                "${subTier.freightDiscountPercent}% Freight Discount active • Renews ${user?.subscriptionExpiresAt?.ifBlank { "in 30 days" }}"
                            } else {
                                "Save up to 15% on cargo with Silver (£50), Gold (£80) & Diamond (£100)"
                            },
                            fontSize = 11.5.sp,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "View Subscriptions",
                        tint = TextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        // Architecture: Role & Account Switcher Banner
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(14.dp),
                        ambientColor = Color(0x22000000),
                        spotColor = Color(0x332563EB)
                    )
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { showRoleModal = true },
                color = BuanBlueSubtle,
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(BuanBlueCta),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Active Role: ${user?.role ?: "Customer"}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Preview Customer, Hub, Business, Corporate & Admin", fontSize = 11.sp, color = BuanBlueLight)
                    }
                    Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = BuanBlueLight, modifier = Modifier.size(14.dp))
                }
            }
        }

        // Section: Dedicated Admin Operations Suite Access Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(14.dp),
                        ambientColor = Color(0x22000000),
                        spotColor = Color(0x442563EB)
                    )
                    .clip(RoundedCornerShape(14.dp))
                    .border(
                        1.dp,
                        Color(0xFF2563EB).copy(alpha = 0.45f),
                        RoundedCornerShape(14.dp)
                    )
                    .clickable { viewModel.navigateTo(Screen.AdminPanel) }
                    .testTag("profile_admin_panel_entry_btn"),
                color = BuanSurface,
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Color(0xFF1E3A8A), Color(0xFF2563EB)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin Operations",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Admin Operations Suite",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = Color(0x2210B981),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "OPERATIONS",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF10B981),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.5.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "View live customer activities, shipments, quotes & directory",
                            fontSize = 11.5.sp,
                            color = BuanBlueLight,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        tint = BuanBlueLight,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        // Section: Appearance & Theme (Light and Dark Switch)
        item {
            BuanCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(BuanBlueSubtle),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                                contentDescription = "Theme",
                                tint = BuanBlueLight,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Appearance & Theme",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = if (isDarkTheme) "Dark Mode active" else "Light Mode active",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    // Segmented Light / Dark Toggle
                    Surface(
                        modifier = Modifier.shadow(
                            elevation = 3.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = Color(0x22000000),
                            spotColor = Color(0x33000000)
                        ),
                        color = BuanSurfaceVariant,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Light Button
                            Surface(
                                modifier = Modifier
                                    .shadow(
                                        elevation = if (!isDarkTheme) 3.dp else 0.dp,
                                        shape = RoundedCornerShape(16.dp),
                                        ambientColor = Color(0x22000000),
                                        spotColor = Color(0x552563EB)
                                    )
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { viewModel.setDarkTheme(false) }
                                    .testTag("theme_light_toggle_btn"),
                                color = if (!isDarkTheme) BuanBlueCta else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LightMode,
                                        contentDescription = "Light Mode",
                                        tint = if (!isDarkTheme) Color.White else TextMuted,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Light",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (!isDarkTheme) Color.White else TextSecondary
                                    )
                                }
                            }

                            // Dark Button
                            Surface(
                                modifier = Modifier
                                    .shadow(
                                        elevation = if (isDarkTheme) 3.dp else 0.dp,
                                        shape = RoundedCornerShape(16.dp),
                                        ambientColor = Color(0x22000000),
                                        spotColor = Color(0x552563EB)
                                    )
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { viewModel.setDarkTheme(true) }
                                    .testTag("theme_dark_toggle_btn"),
                                color = if (isDarkTheme) BuanBlueCta else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DarkMode,
                                        contentDescription = "Dark Mode",
                                        tint = if (isDarkTheme) Color.White else TextMuted,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Dark",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDarkTheme) Color.White else TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Location & Currency (Primary: Naira & British Pounds)
        item {
            BuanCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { showCurrencyModal = true }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(BuanBlueSubtle),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userCurrency.flag,
                            fontSize = 19.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Display Currency",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            if (userCurrency.isPrimary) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    color = Color(0x1F2563EB),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "PRIMARY",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = BuanBlueLight,
                                        letterSpacing = 0.3.sp,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.5.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = "${userCurrency.countryName} • ${userCurrency.symbol} ${userCurrency.code}",
                            fontSize = 11.5.sp,
                            color = BuanBlueLight,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${userCurrency.displayName} • Tap to switch",
                            fontSize = 10.5.sp,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Select currency",
                        tint = TextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        // Section: Personal & Shipment Preferences
        item {
            BuanCard(modifier = Modifier.fillMaxWidth()) {
                Text("Account Management", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                ProfileMenuItem(
                    icon = Icons.Default.Person,
                    title = "Personal Information",
                    subtitle = "Update profile credentials, photo and contact details",
                    onClick = { viewModel.navigateTo(Screen.EditProfile) }
                )
                ProfileMenuItem(
                    icon = Icons.Default.LocationOn,
                    title = "Saved Addresses",
                    subtitle = "${user?.streetAddress ?: "14 Marina Boulevard"}, ${user?.city ?: "Lagos"}",
                    onClick = { viewModel.navigateTo(Screen.EditProfile) }
                )
                ProfileMenuItem(
                    icon = Icons.Default.Settings,
                    title = "Shipment Preferences",
                    subtitle = "Default transport mode, packaging, customs PAAR",
                    onClick = { Toast.makeText(context, "Default transport mode: Air Freight", Toast.LENGTH_SHORT).show() }
                )
                ProfileMenuItem(
                    icon = Icons.Default.AdminPanelSettings,
                    title = "Admin Operations Suite",
                    subtitle = "Review customer activities, cargo pipelines & verification",
                    onClick = { viewModel.navigateTo(Screen.AdminPanel) }
                )
                ProfileMenuItem(
                    icon = Icons.Default.Diamond,
                    title = "Membership Packages",
                    subtitle = "Buan Silver (£50), Gold (£80), Diamond (£100)",
                    onClick = { viewModel.navigateTo(Screen.Subscriptions) }
                )
                ProfileMenuItem(
                    icon = Icons.Default.CardGiftcard,
                    title = "Cashback & Free Shipping Rewards",
                    subtitle = "Refer 7 friends to unlock Free Shipping & $50 Cashback",
                    onClick = { viewModel.navigateTo(Screen.BuanCoinWallet) }
                )
            }
        }

        // Section: System & Support
        item {
            BuanCard(modifier = Modifier.fillMaxWidth()) {
                Text("Support & Legal", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                ProfileMenuItem(
                    icon = Icons.Default.Notifications,
                    title = "Notifications & Alerts",
                    subtitle = "SMS, email and push notifications",
                    onClick = { viewModel.navigateTo(Screen.Notifications) }
                )
                ProfileMenuItem(
                    icon = Icons.Default.Lock,
                    title = "Security & Credentials",
                    subtitle = "Password, two-factor auth, session security",
                    onClick = { Toast.makeText(context, "Account credentials and security active", Toast.LENGTH_SHORT).show() }
                )
                ProfileMenuItem(
                    icon = Icons.AutoMirrored.Filled.Help,
                    title = "Help Centre",
                    subtitle = "FAQs, Live Chat, and freight support",
                    onClick = { viewModel.navigateTo(Screen.HelpCentre) }
                )
                ProfileMenuItem(
                    icon = Icons.Default.Campaign,
                    title = "App Layout & Tabs Tour",
                    subtitle = "Replay the step-by-step feature walk-through",
                    onClick = {
                        viewModel.startDashboardTour()
                        viewModel.navigateTo(Screen.Home)
                    }
                )
                ProfileMenuItem(
                    icon = Icons.Default.Gavel,
                    title = "Terms & Conditions",
                    subtitle = "International bill of lading and carriage terms",
                    onClick = { showPolicyModal = "terms" }
                )
                ProfileMenuItem(
                    icon = Icons.Default.Policy,
                    title = "Privacy Policy",
                    subtitle = "Data protection and cargo tracking telemetry",
                    onClick = { showPolicyModal = "privacy" }
                )
            }
        }

        // Logout Button
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(14.dp),
                        ambientColor = Color(0x33EF4444),
                        spotColor = Color(0x44EF4444)
                    )
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { viewModel.logout() },
                color = BuanSurface,
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Logout from BUAN Logistics", color = Color(0xFFEF4444), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Official Business Logo Footer
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BuanOfficialBusinessLogo(
                    fontSize = 22.sp,
                    taglineSize = 10.sp,
                    showTagline = true,
                    primaryColor = BuanBlueLight
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Official Logistics Platform • Secure & Certified",
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }
        }

        item { Spacer(modifier = Modifier.height(72.dp)) }
    }

    // Role Architecture Dialog
    if (showRoleModal) {
        AlertDialog(
            onDismissRequest = { showRoleModal = false },
            title = { Text("Role-Based Architecture", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "The application architecture natively handles 5 distinct user roles. Switch preview role below:",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    UserRole.entries.forEach { role ->
                        val isCurrent = user?.role == role.displayName
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .shadow(
                                    elevation = if (isCurrent) 4.dp else 1.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    ambientColor = Color(0x22000000),
                                    spotColor = if (isCurrent) Color(0x552563EB) else Color(0x11000000)
                                )
                                .clip(RoundedCornerShape(10.dp)),
                            color = if (isCurrent) BuanBlueSubtle else BuanSurfaceVariant,
                            onClick = {
                                viewModel.switchUserRole(role)
                                showRoleModal = false
                                if (role == UserRole.ADMIN) {
                                    viewModel.navigateTo(Screen.AdminPanel)
                                    Toast.makeText(context, "Entering Admin Operations Suite", Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(role.displayName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Text(
                                        when (role) {
                                            UserRole.CUSTOMER -> "Individual sender & receiver, track shipments"
                                            UserRole.HUB_PROVIDER -> "Shop & retail intake station, earns intake commissions"
                                            UserRole.BUSINESS_CUSTOMER -> "Multi-shipment bulk dispatch & scheduled pickup"
                                            UserRole.CORPORATE_PARTNER -> "Dedicated account manager & maritime chartering"
                                            UserRole.ADMIN -> "Operations oversight, rate management, hubs"
                                        },
                                        fontSize = 10.sp,
                                        color = TextMuted
                                    )
                                }
                                if (isCurrent) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = BuanBlueLight, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showRoleModal = false }) {
                    Text("Close", color = TextSecondary)
                }
            },
            containerColor = BuanSurface
        )
    }

    // Policy & Terms Modal
    if (showPolicyModal != null) {
        val isTerms = showPolicyModal == "terms"
        AlertDialog(
            onDismissRequest = { showPolicyModal = null },
            title = {
                Text(
                    text = if (isTerms) "Terms & Conditions" else "Privacy Policy",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = if (isTerms)
                            "1. Carriage of Goods: All shipments booked through BUAN Logistics are subject to standard international air freight (IATA/Warsaw Convention) and maritime bills of lading (Hague-Visby Rules).\n\n" +
                                    "2. Declared Value & Insurance: Senders must provide accurate customs declaration for commercial goods.\n\n" +
                                    "3. Hub Intake: BUAN Hub Providers act as verified intermediary collection facilities."
                        else
                            "1. Telemetry Privacy: Location coordinates collected during shipment transit are used solely for real-time customer tracking.\n\n" +
                                    "2. Data Protection: In compliance with NDPR and international trade compliance directives, your personal consignor/consignee data is strictly encrypted.\n\n" +
                                    "3. Cookies & Identifiers: Device identifiers are stored locally on your device for session persistence.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                BuanButton(text = "Understood", onClick = { showPolicyModal = null })
            },
            containerColor = BuanSurface
        )
    }

    // Country & Display Currency Modal
    if (showCurrencyModal) {
        AlertDialog(
            onDismissRequest = { showCurrencyModal = false },
            title = {
                Column {
                    Text(
                        text = "Location & Currency",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Primary currencies: Naira (₦) & British Pounds (£)",
                        color = BuanBlueLight,
                        fontSize = 12.sp
                    )
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(
                            text = "BUAN Logistics displays freight rates based on your operational country. Choose your active regional currency below:",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    // Auto Detect Action
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 3.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    ambientColor = Color(0x22000000),
                                    spotColor = Color(0x332563EB)
                                )
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    val detected = AppCurrency.detectFromLocale()
                                    viewModel.setUserCurrency(detected)
                                    showCurrencyModal = false
                                    Toast.makeText(context, "Location set to ${detected.countryName} (${detected.symbol} ${detected.code})", Toast.LENGTH_SHORT).show()
                                },
                            color = BuanBlueSubtle
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Public,
                                    contentDescription = null,
                                    tint = BuanBlueLight,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Auto-detect from Device Region",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = BuanBlueLight
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    item {
                        Text(
                            text = "PRIMARY CORRIDOR CURRENCIES",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BuanBlueLight,
                            letterSpacing = 0.5.sp
                        )
                    }

                    // Primary currencies: NGN & GBP
                    items(AppCurrency.entries.filter { it.isPrimary }.size) { idx ->
                        val currency = AppCurrency.entries.filter { it.isPrimary }[idx]
                        val isSelected = userCurrency == currency
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = if (isSelected) 4.dp else 1.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    ambientColor = Color(0x22000000),
                                    spotColor = if (isSelected) Color(0x552563EB) else Color(0x11000000)
                                )
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    viewModel.setUserCurrency(currency)
                                    showCurrencyModal = false
                                    Toast.makeText(context, "Currency set to ${currency.symbol} ${currency.code}", Toast.LENGTH_SHORT).show()
                                },
                            color = if (isSelected) BuanBlueSubtle else BuanSurfaceVariant
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(currency.flag, fontSize = 22.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                                        ) {
                                            Text(
                                                text = "${currency.symbol} ${currency.code}",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                            Surface(
                                                color = BuanBlueCta,
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = "PRIMARY",
                                                    fontSize = 7.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = "${currency.displayName} • ${currency.countryName}",
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = BuanBlueLight, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "OTHER GLOBAL REGIONS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted,
                            letterSpacing = 0.5.sp
                        )
                    }

                    // Other currencies
                    items(AppCurrency.entries.filter { !it.isPrimary }.size) { idx ->
                        val currency = AppCurrency.entries.filter { !it.isPrimary }[idx]
                        val isSelected = userCurrency == currency
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = if (isSelected) 4.dp else 1.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    ambientColor = Color(0x22000000),
                                    spotColor = if (isSelected) Color(0x552563EB) else Color(0x11000000)
                                )
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    viewModel.setUserCurrency(currency)
                                    showCurrencyModal = false
                                    Toast.makeText(context, "Currency set to ${currency.symbol} ${currency.code}", Toast.LENGTH_SHORT).show()
                                },
                            color = if (isSelected) BuanBlueSubtle else BuanSurfaceVariant
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(currency.flag, fontSize = 22.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "${currency.symbol} ${currency.code}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "${currency.displayName} • ${currency.countryName}",
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = BuanBlueLight, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCurrencyModal = false }) {
                    Text("Close", color = TextSecondary)
                }
            },
            containerColor = BuanSurface
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(BuanSurfaceElevated),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = BuanBlueLight, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Text(subtitle, fontSize = 11.sp, color = TextMuted)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
    }
    HorizontalDivider(color = BuanBorder, thickness = 0.5.dp)
}
