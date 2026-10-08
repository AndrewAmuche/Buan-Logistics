package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ShipmentEntity
import com.example.model.ShipmentStatus
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
import com.example.ui.theme.StatusCancelled
import com.example.ui.theme.StatusDelivered
import com.example.ui.theme.StatusInTransit
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusProcessing
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.Screen

@Composable
fun BuanCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = BuanSurface,
    borderColor: Color = BuanBorder,
    glowEffect: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    val cardModifier = if (onClick != null) {
        modifier
            .clip(shape)
            .clickable(onClick = onClick)
    } else {
        modifier.clip(shape)
    }

    Surface(
        modifier = cardModifier.shadow(
            elevation = if (glowEffect) 8.dp else 2.dp,
            shape = shape,
            ambientColor = Color(0x33000000),
            spotColor = if (glowEffect) Color(0x552563EB) else Color(0x22000000)
        ),
        shape = shape,
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun BuanButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    testTag: String = "buan_primary_button"
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .height(52.dp)
            .shadow(
                elevation = if (enabled) 6.dp else 0.dp,
                shape = RoundedCornerShape(14.dp),
                ambientColor = Color(0x33000000),
                spotColor = Color(0x662563EB)
            )
            .testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BuanBlueCta,
            contentColor = TextPrimary,
            disabledContainerColor = BuanBluePrimary.copy(alpha = 0.35f),
            disabledContentColor = TextMuted
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp,
            disabledElevation = 0.dp
        ),
        border = null
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = TextPrimary,
                strokeWidth = 2.5.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.3.sp
                )
            }
        }
    }
}

@Composable
fun BuanSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    testTag: String = "buan_secondary_button"
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(50.dp)
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(14.dp),
                ambientColor = Color(0x22000000),
                spotColor = Color(0x33000000)
            )
            .testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BuanSurfaceVariant,
            contentColor = BuanBlueLight
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 1.dp
        ),
        border = null
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun BuanTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    testTag: String = "buan_input_field"
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag),
            label = { Text(label, fontSize = 13.sp) },
            placeholder = { Text(placeholder, color = TextMuted, fontSize = 14.sp) },
            visualTransformation = visualTransformation,
            leadingIcon = if (leadingIcon != null) {
                {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = BuanBlueLight,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else null,
            trailingIcon = trailingIcon,
            isError = isError,
            singleLine = singleLine,
            maxLines = maxLines,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = BuanSurfaceVariant,
                unfocusedContainerColor = BuanSurface,
                focusedBorderColor = BuanBlueCta,
                unfocusedBorderColor = BuanBorder,
                focusedLabelColor = BuanBlueLight,
                unfocusedLabelColor = TextSecondary,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = BuanBlueLight
            )
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = StatusCancelled,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val normalized = status.trim().lowercase()
    val (bgColor, textColor) = when {
        normalized.contains("transit") -> Pair(BuanBlueSubtle, StatusInTransit)
        normalized.contains("deliver") -> Pair(StatusDelivered.copy(alpha = 0.15f), StatusDelivered)
        normalized.contains("process") -> Pair(StatusProcessing.copy(alpha = 0.15f), StatusProcessing)
        normalized.contains("pickup") || normalized.contains("registered") -> Pair(BuanBlueGlow, BuanBlueLight)
        normalized.contains("cancel") -> Pair(StatusCancelled.copy(alpha = 0.15f), StatusCancelled)
        else -> Pair(BuanSurfaceElevated, TextSecondary)
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = bgColor,
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = Brush.horizontalGradient(listOf(textColor.copy(alpha = 0.4f), textColor.copy(alpha = 0.2f)))
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = status.uppercase(),
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuanTopBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    onNotificationClick: (() -> Unit)? = null,
    onProfileClick: (() -> Unit)? = null,
    unreadCount: Int = 0
) {
    TopAppBar(
        title = {
            if (onBackClick == null && (title.contains("BUAN", ignoreCase = true) || title.isBlank())) {
                BuanOfficialBusinessLogo(
                    fontSize = 20.sp,
                    taglineSize = 8.sp,
                    showTagline = true,
                    primaryColor = BuanBlueLight
                )
            } else {
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
            }
        },
        actions = {
            if (onNotificationClick != null) {
                IconButton(onClick = onNotificationClick) {
                    BadgedBox(
                        badge = {
                            if (unreadCount > 0) {
                                Badge(containerColor = BuanBlueCta) {
                                    Text(unreadCount.toString(), color = TextPrimary)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = TextSecondary
                        )
                    }
                }
            }
            if (onProfileClick != null) {
                IconButton(onClick = onProfileClick) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .shadow(
                                elevation = 2.dp,
                                shape = CircleShape,
                                ambientColor = Color(0x33000000),
                                spotColor = Color(0x33000000)
                            )
                            .clip(CircleShape)
                            .background(BuanSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = BuanBlueLight,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BuanBackground,
            titleContentColor = TextPrimary
        )
    )
}

@Composable
fun BuanBottomNavBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    unreadNotifications: Int = 0
) {
    NavigationBar(
        containerColor = BuanSurface,
        tonalElevation = 8.dp,
        modifier = Modifier.border(width = 1.dp, color = BuanBorder, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        val navItems = listOf(
            Triple(Screen.Home, "Home", Icons.Default.Home),
            Triple(Screen.Shipments, "Shipments", Icons.Default.LocalShipping),
            Triple(Screen.RequestQuote, "Quote", Icons.Default.Calculate),
            Triple(Screen.Services, "Services", Icons.Default.Widgets),
            Triple(Screen.Profile, "Profile", Icons.Default.Person)
        )

        navItems.forEach { (screen, label, icon) ->
            val isSelected = when (screen) {
                is Screen.Home -> currentScreen is Screen.Home
                is Screen.Shipments -> currentScreen is Screen.Shipments || currentScreen is Screen.ShipmentDetail || currentScreen is Screen.LiveTracking
                is Screen.RequestQuote -> currentScreen is Screen.RequestQuote || currentScreen is Screen.CreateShipment || currentScreen is Screen.ShipmentCreatedSuccess
                is Screen.Services -> currentScreen is Screen.Services || currentScreen is Screen.FindHub || currentScreen is Screen.BecomeHubProvider
                is Screen.Profile -> currentScreen is Screen.Profile || currentScreen is Screen.BuanCoinWallet || currentScreen is Screen.Notifications || currentScreen is Screen.HelpCentre
                else -> false
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(screen) },
                icon = {
                    if (screen is Screen.RequestQuote) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) BuanBlueCta else BuanBluePrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (isSelected) BuanBlueLight else TextMuted
                        )
                    }
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) BuanBlueLight else TextMuted
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = if (screen is Screen.RequestQuote) Color.Transparent else BuanBlueSubtle,
                    selectedIconColor = BuanBlueLight,
                    unselectedIconColor = TextMuted,
                    selectedTextColor = BuanBlueLight,
                    unselectedTextColor = TextMuted
                )
            )
        }
    }
}

/**
 * High-tech dark map canvas visualizer with electric blue route lines,
 * transit checkpoints, pulsing current position and origin/destination markers.
 */
@Composable
fun DarkTrackingMapVisualizer(
    origin: String,
    currentLocation: String,
    destination: String,
    transportMode: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = 24f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseRadius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF090D15))
            .border(1.dp, BuanBorder, RoundedCornerShape(18.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Draw subtle digital grid background
            val gridSpacing = 36f
            val gridColor = Color(0xFF131A29)
            for (x in 0..(width / gridSpacing).toInt()) {
                drawLine(
                    color = gridColor,
                    start = Offset(x * gridSpacing, 0f),
                    end = Offset(x * gridSpacing, height),
                    strokeWidth = 1f
                )
            }
            for (y in 0..(height / gridSpacing).toInt()) {
                drawLine(
                    color = gridColor,
                    start = Offset(0f, y * gridSpacing),
                    end = Offset(width, y * gridSpacing),
                    strokeWidth = 1f
                )
            }

            // 2. Mock geography vector contours
            val roadColor = Color(0xFF192336)
            drawLine(roadColor, Offset(width * 0.1f, height * 0.9f), Offset(width * 0.45f, height * 0.4f), strokeWidth = 2f)
            drawLine(roadColor, Offset(width * 0.45f, height * 0.4f), Offset(width * 0.85f, height * 0.2f), strokeWidth = 2f)
            drawLine(roadColor, Offset(width * 0.2f, height * 0.2f), Offset(width * 0.7f, height * 0.8f), strokeWidth = 2f)

            // 3. Primary glowing transit route curve
            val startPoint = Offset(width * 0.18f, height * 0.72f)
            val midPoint = Offset(width * 0.52f, height * 0.42f)
            val endPoint = Offset(width * 0.84f, height * 0.26f)

            val routePath = Path().apply {
                moveTo(startPoint.x, startPoint.y)
                quadraticTo(width * 0.35f, height * 0.3f, midPoint.x, midPoint.y)
                quadraticTo(width * 0.68f, height * 0.55f, endPoint.x, endPoint.y)
            }

            // Route glow background
            drawPath(
                path = routePath,
                color = BuanBluePrimary.copy(alpha = 0.3f),
                style = Stroke(width = 10f)
            )

            // Main electric blue route line
            drawPath(
                path = routePath,
                color = BuanBlueCta,
                style = Stroke(
                    width = 4f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f)
                )
            )

            // 4. Origin Marker (Green/Blue dot)
            drawCircle(
                color = Color(0xFF10B981),
                radius = 8f,
                center = startPoint
            )
            drawCircle(
                color = Color.White,
                radius = 4f,
                center = startPoint
            )

            // 5. Current Live Position Pulsing Marker (Electric Blue)
            drawCircle(
                color = BuanBlueLight.copy(alpha = pulseAlpha),
                radius = pulseRadius,
                center = midPoint
            )
            drawCircle(
                color = BuanBlueCta,
                radius = 10f,
                center = midPoint
            )
            drawCircle(
                color = Color.White,
                radius = 5f,
                center = midPoint
            )

            // 6. Destination Marker (Target Pin)
            drawCircle(
                color = Color(0xFFEF4444),
                radius = 8f,
                center = endPoint
            )
            drawCircle(
                color = Color.White,
                radius = 4f,
                center = endPoint
            )
        }

        // Overlay Labels for Origin, Live Status & Destination
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                color = BuanSurface.copy(alpha = 0.9f),
                shape = RoundedCornerShape(8.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(listOf(BuanBorderLight, BuanBorder))
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF10B981)))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(origin, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }
            }

            Surface(
                color = BuanSurface.copy(alpha = 0.9f),
                shape = RoundedCornerShape(8.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(listOf(BuanBorderLight, BuanBorder))
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(destination, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        // Watermark tag explaining vector simulation
        Box(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomEnd)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = "BUAN Telemetry Corridor",
                color = TextMuted,
                fontSize = 10.sp
            )
        }
    }
}

/**
 * 8-step visual progress timeline
 */
@Composable
fun ShipmentTimelineView(
    currentStatus: String,
    modifier: Modifier = Modifier
) {
    val currentEnum = ShipmentStatus.fromString(currentStatus)
    val stages = listOf(
        "Shipment Created",
        "Shipment Registered",
        "Picked Up",
        "Processing",
        "In Transit",
        "At Destination",
        "Out for Delivery",
        "Delivered"
    )

    val activeIndex = when (currentEnum) {
        ShipmentStatus.CREATED -> 0
        ShipmentStatus.REGISTERED -> 1
        ShipmentStatus.PICKED_UP -> 2
        ShipmentStatus.PROCESSING -> 3
        ShipmentStatus.IN_TRANSIT -> 4
        ShipmentStatus.AT_DESTINATION -> 5
        ShipmentStatus.OUT_FOR_DELIVERY -> 6
        ShipmentStatus.DELIVERED -> 7
        ShipmentStatus.CANCELLED -> -1
    }

    Column(modifier = modifier.fillMaxWidth()) {
        stages.forEachIndexed { index, stageTitle ->
            val isCompleted = index < activeIndex
            val isCurrent = index == activeIndex
            val isUpcoming = index > activeIndex

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Indicator column
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(28.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCompleted -> Color(0xFF10B981)
                                    isCurrent -> BuanBlueCta
                                    else -> BuanSurfaceElevated
                                }
                            )
                            .border(
                                width = if (isCurrent) 2.dp else 1.dp,
                                color = if (isCurrent) BuanBlueLight else BuanBorderLight,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Completed",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        } else if (isCurrent) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                        } else {
                            Text(
                                text = "${index + 1}",
                                fontSize = 10.sp,
                                color = TextMuted,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (index < stages.size - 1) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(38.dp)
                                .background(if (isCompleted) Color(0xFF10B981) else BuanBorder)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Stage content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = if (index < stages.size - 1) 18.dp else 0.dp)
                ) {
                    Text(
                        text = stageTitle,
                        fontSize = 14.sp,
                        fontWeight = if (isCurrent) FontWeight.Bold else if (isCompleted) FontWeight.SemiBold else FontWeight.Normal,
                        color = when {
                            isCurrent -> BuanBlueLight
                            isCompleted -> TextPrimary
                            else -> TextMuted
                        }
                    )
                    Text(
                        text = when {
                            isCurrent -> "Active milestone in progress"
                            isCompleted -> "Verified and completed"
                            else -> "Upcoming checkpoint"
                        },
                        fontSize = 12.sp,
                        color = if (isCurrent) TextSecondary else TextMuted
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyStateView(
    title: String,
    description: String,
    icon: ImageVector,
    buttonText: String? = null,
    onButtonClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    ambientColor = Color(0x33000000),
                    spotColor = Color(0x33000000)
                )
                .clip(CircleShape)
                .background(BuanSurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BuanBlueLight,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            fontSize = 13.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
        if (buttonText != null && onButtonClick != null) {
            Spacer(modifier = Modifier.height(20.dp))
            BuanButton(
                text = buttonText,
                onClick = onButtonClick,
                modifier = Modifier.fillMaxWidth(0.7f)
            )
        }
    }
}

@Composable
fun RecentShipmentCard(
    shipment: ShipmentEntity,
    onClick: () -> Unit,
    onTrackLive: () -> Unit
) {
    BuanCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = shipment.trackingNumber,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = shipment.transportMode,
                    fontSize = 11.sp,
                    color = BuanBlueLight
                )
            }
            StatusBadge(status = shipment.status)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Route row: Origin -> Destination
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Origin", fontSize = 11.sp, color = TextMuted)
                Text(
                    text = shipment.origin,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    maxLines = 1
                )
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(BuanBlueSubtle),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = BuanBlueLight,
                    modifier = Modifier.size(14.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Destination", fontSize = 11.sp, color = TextMuted)
                Text(
                    text = shipment.destination,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = BuanBorder, thickness = 1.dp)
        Spacer(modifier = Modifier.height(10.dp))

        // Mini status progression dots
        MiniStatusProgressBar(status = shipment.status)

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Estimated Delivery", fontSize = 11.sp, color = TextMuted)
                Text(
                    text = shipment.estimatedDelivery,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(onClick = onTrackLive)
                    .background(BuanBlueSubtle)
                    .border(1.dp, BuanBluePrimary.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Radar,
                        contentDescription = null,
                        tint = BuanBlueLight,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Live Map",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BuanBlueLight
                    )
                }
            }
        }
    }
}

@Composable
fun MiniStatusProgressBar(status: String) {
    val steps = listOf("Registered", "Picked Up", "In Transit", "Delivered")
    val normalized = status.lowercase()
    val currentStep = when {
        normalized.contains("deliver") -> 3
        normalized.contains("transit") || normalized.contains("destination") || normalized.contains("process") -> 2
        normalized.contains("pickup") -> 1
        else -> 0
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        steps.forEachIndexed { index, stepLabel ->
            val isDone = index <= currentStep
            val isCurrent = index == currentStep

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (isDone) BuanBlueCta else BuanBorderLight)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stepLabel,
                    fontSize = 10.sp,
                    color = if (isCurrent) BuanBlueLight else if (isDone) TextPrimary else TextMuted,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                )
            }

            if (index < steps.size - 1) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .padding(horizontal = 4.dp)
                        .background(if (index < currentStep) BuanBlueCta.copy(alpha = 0.6f) else BuanBorder)
                )
            }
        }
    }
}
