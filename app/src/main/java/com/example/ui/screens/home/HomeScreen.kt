package com.example.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.R
import com.example.data.local.ShipmentEntity
import com.example.model.ShipmentStatus
import com.example.model.SubscriptionTier
import com.example.ui.viewmodel.Screen
import com.example.ui.components.BuanBannerVideoPlayer
import com.example.ui.components.BuanButton
import com.example.ui.components.BuanCard
import com.example.ui.components.BuanTextField
import com.example.ui.components.BuanTopBar
import com.example.ui.components.EmptyStateView
import com.example.ui.components.RecentShipmentCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.BuanBackground
import com.example.ui.theme.BuanBlueCta
import com.example.ui.theme.BuanBlueGlow
import com.example.ui.theme.BuanBlueLight
import com.example.ui.theme.BuanBluePrimary
import com.example.ui.theme.BuanBlueSubtle
import com.example.ui.theme.BuanBorder
import com.example.ui.theme.BuanBorderLight
import com.example.ui.theme.BuanCoinGold
import com.example.ui.theme.BuanSurface
import com.example.ui.theme.BuanSurfaceElevated
import com.example.ui.theme.BuanSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BuanViewModel

@Composable
fun HomeScreen(viewModel: BuanViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val shipments by viewModel.allShipments.collectAsState()
    val unreadNotifications by viewModel.unreadNotificationsCount.collectAsState()
    val searchInput by viewModel.trackingSearchInput.collectAsState()
    val searchError by viewModel.trackingError.collectAsState()
    val showDashboardTour by viewModel.showDashboardTour.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.triggerTourIfFirstTime()
    }

    val firstName = currentUser?.fullName?.split(" ")?.firstOrNull() ?: "Shipper"

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BuanBackground)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Top Bar
            item {
                BuanTopBar(
                    title = "BUAN Logistics",
                    onNotificationClick = { viewModel.navigateTo(Screen.Notifications) },
                    onProfileClick = { viewModel.navigateTo(Screen.Profile) },
                    unreadCount = unreadNotifications
                )
            }

            // 2. Sliding Advert Banner at Top Center with 10px Border Round
            item {
                BuanTopSlidingAdBanner(
                    onAdvertClick = { destination ->
                        when (destination) {
                            "air" -> viewModel.navigateTo(Screen.RequestQuote)
                            "sea" -> viewModel.navigateTo(Screen.RequestQuote)
                            "hubs" -> viewModel.navigateTo(Screen.FindHub)
                            "wallet" -> viewModel.navigateTo(Screen.BuanCoinWallet)
                            else -> viewModel.navigateTo(Screen.Services)
                        }
                    }
                )
            }

            // 3. Greeting Header with Quick Tour Launcher Chip (Optimized for Infinix & compact displays)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f, fill = true)
                            .padding(end = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Hello, $firstName",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            val subTier = SubscriptionTier.fromId(currentUser?.subscriptionTier)
                            if (subTier != SubscriptionTier.NONE) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            when (subTier) {
                                                SubscriptionTier.DIAMOND -> Color(0xFF0891B2)
                                                SubscriptionTier.GOLD -> Color(0xFFD97706)
                                                SubscriptionTier.SILVER -> Color(0xFF64748B)
                                                else -> BuanBlueCta
                                            }
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                        .clickable { viewModel.navigateTo(Screen.Subscriptions) }
                                ) {
                                    Text(
                                        text = subTier.displayName,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        maxLines = 1
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(BuanBlueSubtle)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = currentUser?.accountType ?: "Personal",
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = BuanBlueLight,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Where are you shipping today?",
                            fontSize = 12.5.sp,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Tour Launch Button (Fitted, non-breaking, robust on Infinix and compact devices)
                    Surface(
                        modifier = Modifier
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(12.dp),
                                ambientColor = Color(0x22000000),
                                spotColor = Color(0x332563EB)
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewModel.startDashboardTour() }
                            .testTag("home_launch_tour_btn"),
                        color = Color(0x1F2563EB),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Help,
                                contentDescription = "Tour",
                                tint = BuanBlueLight,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Tour",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BuanBlueLight,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // Admin Operations Banner (Visible when Administrator role is active)
            if (currentUser?.role == "Administrator" || currentUser?.email == "babiestouchsupport@gmail.com") {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(3.dp, RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFF2563EB).copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                            .clickable { viewModel.navigateTo(Screen.AdminPanel) }
                            .testTag("home_admin_command_banner"),
                        color = Color(0x1F2563EB),
                        shape = RoundedCornerShape(12.dp)
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
                                Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = BuanBlueLight, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Admin Operations Suite", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Text("Live customer activities, pipelines & verification", fontSize = 10.5.sp, color = BuanBlueLight)
                                }
                            }
                            Surface(
                                color = BuanBlueCta,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Console ➔",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

        // 4. Main Tracking Box
        item {
            BuanCard(
                glowEffect = true,
                backgroundColor = BuanSurface
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BuanBlueSubtle),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Radar,
                            contentDescription = null,
                            tint = BuanBlueLight,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Track your shipment",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Real-time international and domestic status",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                BuanTextField(
                    value = searchInput,
                    onValueChange = { viewModel.setTrackingSearch(it) },
                    label = "Tracking Number",
                    placeholder = "e.g. BUAN-123456789",
                    leadingIcon = Icons.Default.Search,
                    isError = searchError != null,
                    errorMessage = searchError,
                    testTag = "home_tracking_input"
                )

                Spacer(modifier = Modifier.height(14.dp))

                BuanButton(
                    text = "Track Shipment",
                    onClick = { viewModel.submitTrackingSearch() },
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "home_track_button"
                )
            }
        }

        // 4. Quick Actions (Primary & Secondary)
        item {
            Column {
                Text(
                    text = "Quick Actions",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Primary 4 actions in 2x2 grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = "Request Quote",
                        subtitle = "Air, Sea & Road",
                        icon = Icons.Default.Calculate,
                        iconTint = Color.White,
                        iconBg = BuanBlueCta,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            viewModel.navigateTo(Screen.RequestQuote)
                        }
                    )
                    QuickActionCard(
                        title = "Track Shipment",
                        subtitle = "Live Map Radar",
                        icon = Icons.Default.Radar,
                        iconTint = BuanBlueLight,
                        iconBg = BuanBlueSubtle,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            // If shipments exist, open the first in-transit one
                            val active = shipments.firstOrNull { it.status.contains("Transit") } ?: shipments.firstOrNull()
                            if (active != null) {
                                viewModel.navigateTo(Screen.LiveTracking(active.trackingNumber))
                            } else {
                                viewModel.navigateTo(Screen.Shipments)
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = "Logistics Services",
                        subtitle = "Maritime & Cargo",
                        icon = Icons.Default.Widgets,
                        iconTint = BuanBlueLight,
                        iconBg = BuanBlueSubtle,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(Screen.Services) }
                    )
                    QuickActionCard(
                        title = "Find a Hub",
                        subtitle = "Drop-off Points",
                        icon = Icons.Default.LocationOn,
                        iconTint = BuanBlueLight,
                        iconBg = BuanBlueSubtle,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(Screen.FindHub) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Secondary actions row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SecondaryActionPill(
                        label = "History",
                        icon = Icons.Default.History,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(Screen.Shipments) }
                    )
                    SecondaryActionPill(
                        label = "Services",
                        icon = Icons.Default.Widgets,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(Screen.Services) }
                    )
                    SecondaryActionPill(
                        label = "BUAN-COIN",
                        icon = Icons.Default.MonetizationOn,
                        tint = BuanCoinGold,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(Screen.BuanCoinWallet) }
                    )
                    SecondaryActionPill(
                        label = "Hub Partner",
                        icon = Icons.Default.Storefront,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.navigateTo(Screen.BecomeHubProvider) }
                    )
                }
            }
        }

        // 5. Your Shipments Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Shipments",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "View All (${shipments.size})",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BuanBlueLight,
                    modifier = Modifier.clickable { viewModel.navigateTo(Screen.Shipments) }
                )
            }
        }

        if (shipments.isEmpty()) {
            item {
                EmptyStateView(
                    title = "No shipments yet",
                    description = "Request an instant freight quote or create your cargo booking.",
                    icon = Icons.Default.LocalShipping,
                    buttonText = "Request a Quote",
                    onButtonClick = {
                        viewModel.navigateTo(Screen.RequestQuote)
                    }
                )
            }
        } else {
            items(shipments.take(3)) { shipment ->
                RecentShipmentCard(
                    shipment = shipment,
                    onClick = { viewModel.navigateTo(Screen.ShipmentDetail(shipment.trackingNumber)) },
                    onTrackLive = { viewModel.navigateTo(Screen.LiveTracking(shipment.trackingNumber)) }
                )
            }
        }

        // Brand message footer
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "BUAN LOGISTICS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "\"Move Cargo. Move Business.\"",
                        fontSize = 12.sp,
                        color = BuanBlueLight,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Bottom nav spacing
        item { Spacer(modifier = Modifier.height(72.dp)) }
    }

    if (showDashboardTour) {
        DashboardTourOverlay(
            onDismiss = { viewModel.dismissDashboardTour() }
        )
    }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color(0x22000000),
                spotColor = Color(0x33000000)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = BuanSurface,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun SecondaryActionPill(
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = BuanBlueLight,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Color(0x18000000),
                spotColor = Color(0x22000000)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        color = BuanSurfaceVariant,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary,
                maxLines = 1
            )
        }
    }
}

// -------------------------------------------------------------------------
// Top Sliding Advert Banner (10px Rounded Borders)
// -------------------------------------------------------------------------

data class BuanAdvert(
    val id: String,
    val category: String,
    val title: String,
    val description: String,
    val ctaText: String,
    val icon: ImageVector,
    val gradientColors: List<Color>,
    val targetDestination: String,
    val badgeText: String = "",
    val accentColor: Color = Color(0xFF60A5FA),
    val videoUrl: String? = null,
    val fallbackImageRes: Int? = null
)

@Composable
fun BuanTopSlidingAdBanner(
    onAdvertClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val adverts = remember {
        listOf(
            // First slide: 1080p animated plane & text video banner
            BuanAdvert(
                id = "ad_air_cargo_video",
                category = "AIR EXPRESS • 48-72 HRS",
                title = "Global Air Cargo & Express",
                description = "Fast-track air cargo between Nigeria, UK, US, and 160+ destinations worldwide.",
                ctaText = "Ship Air Cargo",
                icon = Icons.Default.FlightTakeoff,
                gradientColors = listOf(Color(0xFF07152B), Color(0xFF0F1E38)),
                targetDestination = "air",
                badgeText = "✈️ DIRECT FLIGHTS",
                accentColor = Color(0xFF60A5FA),
                videoUrl = "https://res.cloudinary.com/drddunnrc/video/upload/v1790959972/Animate_plane_and_text_1080p_20261002174658_1.mp4",
                fallbackImageRes = R.drawable.buan_onboard_hub_1790919825219
            ),
            // Second slide: animated truck & text video banner
            BuanAdvert(
                id = "ad_truck_freight_video",
                category = "INTERSTATE FREIGHT",
                title = "Nationwide Truck & Road Cargo",
                description = "Daily linehauls across 36 states with bonded drivers, real-time telemetry, and secure transit.",
                ctaText = "Ship Road Freight",
                icon = Icons.Default.LocalShipping,
                gradientColors = listOf(Color(0xFF0B1B36), Color(0xFF0B1426)),
                targetDestination = "shipments",
                badgeText = "🚚 ROAD FREIGHT",
                accentColor = Color(0xFF38BDF8),
                videoUrl = "https://res.cloudinary.com/drddunnrc/video/upload/v1790961469/animate_the_truck_and_text_20261002181218_1.mp4",
                fallbackImageRes = R.drawable.buan_hero_cargo_1790918494388
            ),
            // Third slide: animated hubs, images & text video banner
            BuanAdvert(
                id = "ad_interstate_hubs_video",
                category = "50+ NATIONWIDE HUBS",
                title = "Drop-Off Stations Near You",
                description = "Instant parcel intake, digital weighing, barcode labelling, and immediate receipts at every hub.",
                ctaText = "Find Nearby Hub",
                icon = Icons.Default.LocationOn,
                gradientColors = listOf(Color(0xFF08221B), Color(0xFF0B192E)),
                targetDestination = "hubs",
                badgeText = "📍 50+ HUBS",
                accentColor = Color(0xFF10B981),
                videoUrl = "https://res.cloudinary.com/drddunnrc/video/upload/v1790962910/animate_the_images_and_text_20261002181056_1.mp4",
                fallbackImageRes = R.drawable.buan_onboard_ship_1790919808906
            ),
            // Fourth slide: animated design & referral cashback video banner
            BuanAdvert(
                id = "ad_rewards_coupon_video",
                category = "REFERRAL & CASHBACK",
                title = "Refer 7 Friends = Free Shipping",
                description = "Request your unique coupon code, refer 7 friends, and unlock 100% Free Shipping + $50 Cashback.",
                ctaText = "Get Coupon Code",
                icon = Icons.Default.CardGiftcard,
                gradientColors = listOf(Color(0xFF241434), Color(0xFF0C1328)),
                targetDestination = "wallet",
                badgeText = "🎁 FREE SHIPPING",
                accentColor = Color(0xFFFBBF24),
                videoUrl = "https://res.cloudinary.com/drddunnrc/video/upload/v1790963733/animate_this_design_20261002185127_1.mp4",
                fallbackImageRes = R.drawable.buan_hero_cargo_1790918494388
            )
        )
    }

    val actualCount = adverts.size
    val loopCount = 2000 * actualCount
    val initialPage = (loopCount / 2) - ((loopCount / 2) % actualCount)
    val pagerState = rememberPagerState(initialPage = initialPage) { loopCount }

    // Auto-advance sliding banner every 5.5 seconds in a continuous infinite loop
    LaunchedEffect(pagerState) {
        while (true) {
            delay(5500)
            if (!pagerState.isScrollInProgress) {
                pagerState.animateScrollToPage(
                    page = pagerState.currentPage + 1,
                    animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing)
                )
            }
        }
    }

    // Outer container: Full Graphical Banner Slider with soft shadow, NO solid border
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color(0x33000000),
                spotColor = Color(0x442563EB)
            )
            .clip(RoundedCornerShape(18.dp))
            .testTag("home_top_sliding_advert_banner"),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF0B0F19)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { virtualPage ->
                val actualIndex = virtualPage % actualCount
                val ad = adverts[actualIndex]
                val isSlideActive = (pagerState.currentPage % actualCount) == actualIndex

                Box(modifier = Modifier.fillMaxSize()) {
                    BuanAdvertSlideItem(
                        ad = ad,
                        isActive = isSlideActive,
                        onAdvertClick = onAdvertClick
                    )

                    // Bottom Right: Slide Indicator Dots reflecting active loop index
                    val currentActualIndex = pagerState.currentPage % actualCount
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 14.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(actualCount) { index ->
                            val isSelected = currentActualIndex == index
                            val width by animateDpAsState(
                                targetValue = if (isSelected) 18.dp else 5.dp,
                                label = "ad_dot"
                            )
                            Box(
                                modifier = Modifier
                                    .height(4.dp)
                                    .width(width)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(if (isSelected) ad.accentColor else Color(0x66FFFFFF))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BuanAdvertSlideItem(
    ad: BuanAdvert,
    isActive: Boolean,
    onAdvertClick: (String) -> Unit
) {
    if (ad.videoUrl != null) {
        // First Slide: High performance full-bleed video player with loop
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onAdvertClick(ad.targetDestination) }
        ) {
            BuanBannerVideoPlayer(
                videoUrl = ad.videoUrl,
                fallbackImageRes = ad.fallbackImageRes,
                isActive = isActive,
                modifier = Modifier.fillMaxSize()
            )

            // Subtle cinematic gradient overlay to ensure text and CTA stand out crisp
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0x66000000),
                                Color.Transparent,
                                Color(0x88000000),
                                Color(0xD9060A14)
                            )
                        )
                    )
            )

            // Floating Top Row: Glassmorphism Category Badge & Air Cargo Tag
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .align(Alignment.TopStart),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0xCC0B111E),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.shadow(
                        elevation = 3.dp,
                        shape = RoundedCornerShape(6.dp),
                        ambientColor = Color(0x44000000),
                        spotColor = Color(0x442563EB)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(ad.accentColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = ad.category,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            letterSpacing = 0.6.sp
                        )
                    }
                }

                if (ad.badgeText.isNotBlank()) {
                    Surface(
                        color = Color(0xCC0B111E),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.shadow(
                            elevation = 3.dp,
                            shape = RoundedCornerShape(6.dp),
                            ambientColor = Color(0x33000000)
                        )
                    ) {
                        Text(
                            text = ad.badgeText,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ad.accentColor,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            // Floating Bottom Row: CTA Button with Box Shadow
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 14.dp, bottom = 12.dp)
                    .align(Alignment.BottomStart),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = BuanBlueCta,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(10.dp),
                            ambientColor = Color(0x44000000),
                            spotColor = Color(0x662563EB)
                        )
                        .clickable { onAdvertClick(ad.targetDestination) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = ad.ctaText,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    } else {
        // Graphic Banner Slides: Content appearing in full with generous layout
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(ad.gradientColors))
                .clickable { onAdvertClick(ad.targetDestination) }
        ) {
            // Decorative Canvas Art / Graphic Background Layer
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasW = this.size.width
                val canvasH = this.size.height
                // Ambient radial glow orbs
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(ad.accentColor.copy(alpha = 0.22f), Color.Transparent),
                        center = androidx.compose.ui.geometry.Offset(canvasW * 0.85f, canvasH * 0.45f),
                        radius = canvasH * 0.95f
                    )
                )
                // Decorative logistics accent lines on the right
                val strokeColor = ad.accentColor.copy(alpha = 0.08f)
                drawLine(
                    color = strokeColor,
                    start = androidx.compose.ui.geometry.Offset(canvasW * 0.65f, 0f),
                    end = androidx.compose.ui.geometry.Offset(canvasW * 0.85f, canvasH),
                    strokeWidth = 3f
                )
                drawLine(
                    color = strokeColor,
                    start = androidx.compose.ui.geometry.Offset(canvasW * 0.75f, 0f),
                    end = androidx.compose.ui.geometry.Offset(canvasW * 0.95f, canvasH),
                    strokeWidth = 2f
                )
            }

            // Main Banner Graphic Layout: Content appearing in full
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Column: Promo Content & CTA
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top Row: Category Tag Pill with subtle shadow
                    Surface(
                        color = ad.accentColor.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(6.dp),
                            ambientColor = Color(0x22000000),
                            spotColor = Color(0x22000000)
                        )
                    ) {
                        Text(
                            text = ad.category,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ad.accentColor,
                            letterSpacing = 0.6.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    // Middle: Bold Headline and Full Description
                    Column {
                        Text(
                            text = ad.title,
                            fontSize = 15.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary,
                            lineHeight = 19.sp,
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = ad.description,
                            fontSize = 11.5.sp,
                            color = TextSecondary,
                            lineHeight = 15.sp,
                            maxLines = 2
                        )
                    }

                    // Bottom: CTA Button with Box Shadow
                    Surface(
                        color = BuanBlueCta,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(10.dp),
                                ambientColor = Color(0x33000000),
                                spotColor = Color(0x552563EB)
                            )
                            .clickable { onAdvertClick(ad.targetDestination) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = ad.ctaText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Right Column: Illustrated Banner Artwork & Stamp Badge in Full
                Box(
                    modifier = Modifier
                        .width(92.dp)
                        .fillMaxHeight()
                        .shadow(
                            elevation = 5.dp,
                            shape = RoundedCornerShape(16.dp),
                            ambientColor = Color(0x33000000),
                            spotColor = ad.accentColor.copy(alpha = 0.4f)
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    ad.accentColor.copy(alpha = 0.22f),
                                    Color(0xFF0F172A)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .shadow(
                                    elevation = 4.dp,
                                    shape = CircleShape,
                                    ambientColor = Color(0x22000000),
                                    spotColor = ad.accentColor.copy(alpha = 0.5f)
                                )
                                .clip(CircleShape)
                                .background(ad.accentColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = ad.icon,
                                contentDescription = null,
                                tint = ad.accentColor,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(7.dp))
                        Surface(
                            color = ad.accentColor,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = ad.badgeText,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black,
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// Quick Step-by-Step Screen Layout / Tabs Tour Overlay
// -------------------------------------------------------------------------

data class TourStep(
    val stepIndex: Int,
    val title: String,
    val targetAreaLabel: String,
    val description: String,
    val icon: ImageVector,
    val benefitHighlight: String
)

@Composable
fun DashboardTourOverlay(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tourSteps = remember {
        listOf(
            TourStep(
                stepIndex = 1,
                title = "1. Promotional Deals & Routes",
                targetAreaLabel = "LAYOUT TARGET: TOP BANNER (10PX ROUND)",
                description = "Stay up to date with special air freight corridors, direct vessel consolidations, and 2X BUAN-COIN earning campaigns rotating right at the top center of your dashboard.",
                icon = Icons.Default.Campaign,
                benefitHighlight = "Exclusive seasonal rates & promo codes updated weekly."
            ),
            TourStep(
                stepIndex = 2,
                title = "2. Real-Time Tracking Engine",
                targetAreaLabel = "LAYOUT TARGET: TRACKING BAR",
                description = "Track any consignment globally across Sea, Air, and Road freight. Enter your BUAN tracking number to see GPS checkpoints, vessel coordinates, and customs PAAR milestones.",
                icon = Icons.Default.Radar,
                benefitHighlight = "Live telemetry with real-time status timelines."
            ),
            TourStep(
                stepIndex = 3,
                title = "3. Quick Action Launchpad",
                targetAreaLabel = "LAYOUT TARGET: QUICK ACTION GRID",
                description = "Request instant freight quotes in Naira (₦) and British Pounds (£), explore maritime & air freight solutions, or find certified BUAN drop-off and collection hubs.",
                icon = Icons.Default.Widgets,
                benefitHighlight = "Instant freight quote calculations with live currency conversion."
            ),
            TourStep(
                stepIndex = 4,
                title = "4. Your Active Shipments",
                targetAreaLabel = "LAYOUT TARGET: SHIPMENTS PIPELINE",
                description = "Monitor your cargo pipeline with real-time status indicators (In Transit, Customs Cleared, Delivered), origin/destination flags, and estimated arrival dates.",
                icon = Icons.Default.AllInbox,
                benefitHighlight = "Detailed progress tracking and direct shipment management."
            ),
            TourStep(
                stepIndex = 5,
                title = "5. Persistent Bottom Tabs",
                targetAreaLabel = "LAYOUT TARGET: BOTTOM NAVIGATION BAR",
                description = "Effortlessly jump between the 5 primary tabs: Home dashboard, Shipments history, Quote Request (+), Services, and your Profile with Light/Dark mode and currency settings.",
                icon = Icons.Default.TouchApp,
                benefitHighlight = "Always accessible from any screen in the application."
            )
        )
    }

    var currentStep by remember { mutableIntStateOf(0) }
    val step = tourSteps[currentStep]

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xD9050505))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                // Prevent tap-through
            }
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(22.dp),
                    ambientColor = Color(0x66000000),
                    spotColor = Color(0x662563EB)
                )
                .clip(RoundedCornerShape(22.dp)),
            color = Color(0xFF111624),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Top Header: Step Badge & Skip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = BuanBlueCta,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(12.dp)
                        )
                    ) {
                        Text(
                            text = "STEP ${step.stepIndex} OF ${tourSteps.size}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            letterSpacing = 0.5.sp
                        )
                    }

                    Text(
                        text = "Skip Tour",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted,
                        modifier = Modifier
                            .clickable { onDismiss() }
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Layout Target Badge
                Surface(
                    color = Color(0x1F2563EB),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = step.targetAreaLabel,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = BuanBlueLight,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Icon and Title
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(BuanBlueSubtle),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = step.icon,
                            contentDescription = null,
                            tint = BuanBlueLight,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = step.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Description
                Text(
                    text = step.description,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Highlight chip
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    color = Color(0xFF171E2E),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF34D399),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = step.benefitHighlight,
                            fontSize = 11.sp,
                            color = Color(0xFFD1D5DB),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Progress Indicator Dots
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(tourSteps.size) { idx ->
                        val isCurrent = idx == currentStep
                        val dotWidth by animateDpAsState(
                            targetValue = if (isCurrent) 22.dp else 6.dp,
                            label = "tour_dot"
                        )
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .height(6.dp)
                                .width(dotWidth)
                                .clip(RoundedCornerShape(3.dp))
                                .background(if (isCurrent) BuanBlueCta else Color(0xFF2B3648))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Buttons: Prev / Next
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentStep > 0) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .shadow(
                                    elevation = 3.dp,
                                    shape = RoundedCornerShape(12.dp),
                                    ambientColor = Color(0x22000000),
                                    spotColor = Color(0x33000000)
                                )
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { currentStep-- },
                            color = Color(0xFF182030),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChevronLeft,
                                    contentDescription = null,
                                    tint = TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Back",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(if (currentStep > 0) 1.5f else 1f)
                            .height(46.dp)
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(12.dp),
                                ambientColor = Color(0x33000000),
                                spotColor = Color(0x662563EB)
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                if (currentStep < tourSteps.size - 1) {
                                    currentStep++
                                } else {
                                    onDismiss()
                                }
                            }
                            .testTag("tour_next_action_button"),
                        color = BuanBlueCta,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (currentStep < tourSteps.size - 1) "Next Step" else "Got It! Start Exploring",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if (currentStep < tourSteps.size - 1) Icons.Default.ChevronRight else Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

