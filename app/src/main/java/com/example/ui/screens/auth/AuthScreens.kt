package com.example.ui.screens.auth

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.UserEntity
import com.example.model.AccountType
import com.example.model.SubscriptionTier
import com.example.ui.components.BuanButton
import com.example.ui.components.BuanCard
import com.example.ui.components.BuanOfficialBusinessLogo
import com.example.ui.components.BuanOfficialLogoBadge
import com.example.ui.components.BuanSecondaryButton
import com.example.ui.components.BuanTextField
import com.example.ui.components.BuanTopBar
import com.example.ui.components.OnboardingVideoPlayer
import com.example.ui.theme.BuanBackground
import com.example.ui.theme.BuanBlueCta
import com.example.ui.theme.BuanBlueLight
import com.example.ui.theme.BuanBluePrimary
import com.example.ui.theme.BuanBlueSubtle
import com.example.ui.theme.BuanBorder
import com.example.ui.theme.BuanSurface
import com.example.ui.theme.BuanSurfaceElevated
import com.example.ui.theme.BuanSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BuanViewModel
import com.example.ui.viewmodel.Screen
import java.util.Calendar
import kotlinx.coroutines.launch

data class OnboardingSlide(
    val videoUrl: String,
    val fallbackImageRes: Int,
    val title: String,
    val subtitle: String
)

@Composable
fun WelcomeScreen(viewModel: BuanViewModel) {
    val currentHour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    val isDefaultDay = remember { currentHour in 6..18 }
    var isDaySelected by remember { mutableStateOf(isDefaultDay) }

    val dayVideoTruck = "https://res.cloudinary.com/drddunnrc/video/upload/v1790924399/Regenerate_day_version_of_video_20261002075731.webm"
    val nightVideoTruck = "https://res.cloudinary.com/drddunnrc/video/upload/v1790921339/Container_truck_moving_at_port_20261002065607_1.mp4"
    val activeFirstSlideVideo = if (isDaySelected) dayVideoTruck else nightVideoTruck

    val shipVideo = "https://res.cloudinary.com/drddunnrc/video/upload/v1790921679/Cargo_ship_sailing_ocean_waves_20261002070021_1.mp4"
    val airplaneVideo = "https://res.cloudinary.com/drddunnrc/video/upload/v1790922258/Cargo_aircraft_flying_through_cl__20261002071812_1.mp4"

    val slides = listOf(
        OnboardingSlide(
            videoUrl = activeFirstSlideVideo,
            fallbackImageRes = R.drawable.buan_hero_cargo_1790918494388,
            title = "Smart Shipping\nMade Simple",
            subtitle = "Stay updated every step of the way with live shipment tracking."
        ),
        OnboardingSlide(
            videoUrl = shipVideo,
            fallbackImageRes = R.drawable.buan_onboard_ship_1790919808906,
            title = "Move Cargo.\nMove Business.",
            subtitle = "Multimodal international sea, air, and interstate freight corridors connecting Nigeria and the world."
        ),
        OnboardingSlide(
            videoUrl = airplaneVideo,
            fallbackImageRes = R.drawable.buan_onboard_hub_1790919825219,
            title = "Air Cargo &\nGlobal Express",
            subtitle = "Priority air cargo flights connecting Nigerian trade gateways to over 160 countries worldwide."
        )
    )

    val pagerState = rememberPagerState { slides.size }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BuanBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top 58% of screen: Video Player with HorizontalPager
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.3f)
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        // High performance looped video player with poster fallback
                        OnboardingVideoPlayer(
                            videoUrl = slides[page].videoUrl,
                            fallbackImageRes = slides[page].fallbackImageRes,
                            isActive = pagerState.currentPage == page,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Subtle bottom gradient to blend video into the dark background
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .align(Alignment.BottomCenter)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color.Transparent,
                                            BuanBackground.copy(alpha = 0.75f),
                                            BuanBackground
                                        )
                                    )
                                )
                        )
                    }
                }

                // Floating Top Row: Brand, Day/Night toggle (on slide 1) & Skip Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 12.dp
                        )
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Official Brand Opening Logo Badge
                    BuanOfficialBusinessLogo(
                        fontSize = 17.sp,
                        taglineSize = 7.5.sp,
                        showTagline = true,
                        useWhiteCard = true,
                        primaryColor = Color(0xFF0038E0)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // On slide 0, allow switching between Day and Night video versions
                        if (pagerState.currentPage == 0) {
                            Surface(
                                modifier = Modifier.shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(20.dp),
                                    ambientColor = Color(0x33000000),
                                    spotColor = Color(0x442563EB)
                                ),
                                color = Color(0xCC0B0E17),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        modifier = Modifier
                                            .shadow(
                                                elevation = if (isDaySelected) 3.dp else 0.dp,
                                                shape = RoundedCornerShape(16.dp),
                                                ambientColor = Color(0x22000000),
                                                spotColor = Color(0x552563EB)
                                            )
                                            .clip(RoundedCornerShape(16.dp))
                                            .clickable { isDaySelected = true }
                                            .testTag("onboarding_day_video_btn"),
                                        color = if (isDaySelected) BuanBlueCta else Color.Transparent,
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Text(
                                            text = "☀️ Day",
                                            color = if (isDaySelected) Color.White else TextSecondary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                    Surface(
                                        modifier = Modifier
                                            .shadow(
                                                elevation = if (!isDaySelected) 3.dp else 0.dp,
                                                shape = RoundedCornerShape(16.dp),
                                                ambientColor = Color(0x22000000),
                                                spotColor = Color(0x552563EB)
                                            )
                                            .clip(RoundedCornerShape(16.dp))
                                            .clickable { isDaySelected = false }
                                            .testTag("onboarding_night_video_btn"),
                                        color = if (!isDaySelected) BuanBlueCta else Color.Transparent,
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Text(
                                            text = "🌙 Night",
                                            color = if (!isDaySelected) Color.White else TextSecondary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Skip button to go directly to Authentication
                        Surface(
                            modifier = Modifier
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    ambientColor = Color(0x22000000),
                                    spotColor = Color(0x332563EB)
                                )
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    viewModel.markDeviceRegistered()
                                    viewModel.navigateTo(Screen.Login)
                                },
                            color = Color(0xCC141926),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "Skip",
                                color = BuanBlueLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Bottom 42% of screen: Dots, Title, Subtitle, and Capsule Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top section of text
                Column {
                    // Page indicator: dot dot pill
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(slides.size) { index ->
                            val isSelected = pagerState.currentPage == index
                            val width by animateDpAsState(
                                targetValue = if (isSelected) 24.dp else 6.dp,
                                label = "dot_width"
                            )
                            Box(
                                modifier = Modifier
                                    .height(6.dp)
                                    .width(width)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(if (isSelected) BuanBlueCta else Color(0xFF262E3E))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (pagerState.currentPage == 0) {
                        BuanOfficialBusinessLogo(
                            fontSize = 24.sp,
                            taglineSize = 9.sp,
                            showTagline = true,
                            primaryColor = Color(0xFF38BDF8)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Title
                    Text(
                        text = slides[pagerState.currentPage].title,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary,
                        lineHeight = 34.sp,
                        letterSpacing = (-0.5).sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Subtitle
                    Text(
                        text = slides[pagerState.currentPage].subtitle,
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 19.sp
                    )
                }

                // Bottom Capsule CTA Button (Matching the Reference Screenshot)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .shadow(
                                elevation = 10.dp,
                                shape = RoundedCornerShape(32.dp),
                                ambientColor = Color(0x66000000),
                                spotColor = Color(0x882563EB)
                            )
                            .clip(RoundedCornerShape(32.dp))
                            .clickable {
                                if (pagerState.currentPage < slides.size - 1) {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                } else {
                                    viewModel.markDeviceRegistered()
                                    viewModel.navigateTo(Screen.Login)
                                }
                            }
                            .testTag("onboarding_get_started_capsule"),
                        color = Color(0xFF131824),
                        shape = RoundedCornerShape(32.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Circular box badge on the left in BUAN Blue
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(BuanBlueCta),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AllInbox,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            // "Get Started" center text
                            Text(
                                text = "Get Started",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )

                            // Triple chevron >>> on the right
                            Row(
                                modifier = Modifier.padding(end = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = ">>>",
                                    color = Color(0xFF60A5FA),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = (-2).sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Secondary auth links
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Already have an account?",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Log In",
                            fontSize = 12.sp,
                            color = BuanBlueLight,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                viewModel.markDeviceRegistered()
                                viewModel.navigateTo(Screen.Login)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(viewModel: BuanViewModel) {
    BackHandler {
        if (!viewModel.isDeviceRegistered()) {
            viewModel.navigateTo(Screen.Welcome)
        }
    }

    val currentHour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    val isDefaultDay = remember { currentHour in 6..18 }
    var isDaySelected by remember { mutableStateOf(isDefaultDay) }

    val dayVideoTruck = "https://res.cloudinary.com/drddunnrc/video/upload/v1790924399/Regenerate_day_version_of_video_20261002075731.webm"
    val nightVideoTruck = "https://res.cloudinary.com/drddunnrc/video/upload/v1790921339/Container_truck_moving_at_port_20261002065607_1.mp4"
    val activeVideo = if (isDaySelected) dayVideoTruck else nightVideoTruck

    val registeredUserName by viewModel.registeredUserName.collectAsState()
    val registeredUserEmail by viewModel.registeredUserEmail.collectAsState()
    val prefilledLoginEmail by viewModel.prefilledLoginEmail.collectAsState()

    var email by remember(prefilledLoginEmail, registeredUserEmail) {
        mutableStateOf(
            if (prefilledLoginEmail.isNotBlank()) prefilledLoginEmail
            else registeredUserEmail.ifBlank { "babajide@buanlogistics.com" }
        )
    }
    var password by remember { mutableStateOf("••••••••") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BuanBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Section (Video Player matching the first onboarding slide)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.95f)
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            ) {
                // High performance looped video player
                OnboardingVideoPlayer(
                    videoUrl = activeVideo,
                    fallbackImageRes = R.drawable.buan_hero_cargo_1790918494388,
                    isActive = true,
                    modifier = Modifier.fillMaxSize()
                )

                // Subtle bottom gradient to blend video into the dark background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    BuanBackground.copy(alpha = 0.75f),
                                    BuanBackground
                                )
                            )
                        )
                )

                // Floating Top Row: Brand Badge, Day/Night segmented toggle, and Tour link
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 12.dp
                        )
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Official Brand Opening Logo Badge
                    BuanOfficialBusinessLogo(
                        fontSize = 17.sp,
                        taglineSize = 7.5.sp,
                        showTagline = true,
                        useWhiteCard = true,
                        primaryColor = Color(0xFF0038E0)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Day/Night segmented pill toggle (matching the first onboarding slide)
                        Surface(
                            modifier = Modifier.shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(20.dp),
                                ambientColor = Color(0x33000000),
                                spotColor = Color(0x442563EB)
                            ),
                            color = Color(0xCC0B0E17),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .shadow(
                                            elevation = if (isDaySelected) 3.dp else 0.dp,
                                            shape = RoundedCornerShape(16.dp),
                                            ambientColor = Color(0x22000000),
                                            spotColor = Color(0x552563EB)
                                        )
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable { isDaySelected = true }
                                        .testTag("login_day_video_btn"),
                                    color = if (isDaySelected) BuanBlueCta else Color.Transparent,
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        text = "☀️ Day",
                                        color = if (isDaySelected) Color.White else TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                                Surface(
                                    modifier = Modifier
                                        .shadow(
                                            elevation = if (!isDaySelected) 3.dp else 0.dp,
                                            shape = RoundedCornerShape(16.dp),
                                            ambientColor = Color(0x22000000),
                                            spotColor = Color(0x552563EB)
                                        )
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable { isDaySelected = false }
                                        .testTag("login_night_video_btn"),
                                    color = if (!isDaySelected) BuanBlueCta else Color.Transparent,
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        text = "🌙 Night",
                                        color = if (!isDaySelected) Color.White else TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        // Tour button to revisit Onboarding
                        Surface(
                            modifier = Modifier
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    ambientColor = Color(0x22000000),
                                    spotColor = Color(0x332563EB)
                                )
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { viewModel.navigateTo(Screen.Welcome) },
                            color = Color(0xCC141926),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "Tour",
                                color = BuanBlueLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Bottom Section: Titles, Credentials Input & Capsule Sign In Button
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.35f)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    // Registered device pill indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color(0x1F2563EB),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(12.dp)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = BuanBlueLight,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "REGISTERED DEVICE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BuanBlueLight,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }

                        Text(
                            text = "BUAN ID: USR-001",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                item {
                    Column {
                        Text(
                            text = "Welcome Back",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary,
                            lineHeight = 34.sp,
                            letterSpacing = (-0.5).sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Move Cargo. Move Business. Sign in to your verified account.",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BuanOfficialBusinessLogo(
                            fontSize = 32.sp,
                            taglineSize = 12.sp,
                            showTagline = true,
                            primaryColor = Color(0xFF38BDF8)
                        )
                    }
                }

                item {
                    BuanCard(modifier = Modifier.fillMaxWidth()) {
                        if (prefilledLoginEmail.isNotBlank()) {
                            Surface(
                                color = Color(0x2210B981),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF10B981),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Account found from central website database! Enter your password to sign in.",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF10B981),
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        BuanTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                emailError = null
                            },
                            label = "Email Address",
                            placeholder = "name@company.com",
                            leadingIcon = Icons.Default.Email,
                            isError = emailError != null,
                            errorMessage = emailError,
                            testTag = "login_email_input"
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        BuanTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                passwordError = null
                            },
                            label = "Password",
                            placeholder = "••••••••",
                            leadingIcon = Icons.Default.Lock,
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                    Icon(
                                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle password visibility",
                                        tint = TextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            },
                            isError = passwordError != null,
                            errorMessage = passwordError,
                            testTag = "login_password_input"
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "Forgot Password?",
                                fontSize = 12.sp,
                                color = BuanBlueLight,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable { viewModel.navigateTo(Screen.ForgotPassword) }
                            )
                        }
                    }
                }

                // Capsule CTA Button matching the first onboarding screen
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .shadow(
                                elevation = 10.dp,
                                shape = RoundedCornerShape(32.dp),
                                ambientColor = Color(0x66000000),
                                spotColor = Color(0x882563EB)
                            )
                            .clip(RoundedCornerShape(32.dp))
                            .clickable {
                                if (email.isBlank()) {
                                    emailError = "Please enter your email"
                                    return@clickable
                                }
                                if (password.isBlank()) {
                                    passwordError = "Please enter your password"
                                    return@clickable
                                }
                                viewModel.login(email.trim(), password) { success, message ->
                                    if (!success) {
                                        if (message.contains("password", ignoreCase = true)) {
                                            passwordError = message
                                        } else {
                                            emailError = message
                                        }
                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                    } else {
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            .testTag("login_submit_capsule"),
                        color = Color(0xFF131824),
                        shape = RoundedCornerShape(32.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Circular box badge on the left in BUAN Blue
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(BuanBlueCta),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AllInbox,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            // "Sign In" center text
                            Text(
                                text = "Sign In",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )

                            // Triple chevron >>> on the right
                            Row(
                                modifier = Modifier.padding(end = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = ">>>",
                                    color = Color(0xFF60A5FA),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = (-2).sp
                                )
                            }
                        }
                    }
                }

                // Secondary Navigation Links
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Don't have an account?",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Register",
                            fontSize = 12.sp,
                            color = BuanBlueLight,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { viewModel.navigateTo(Screen.Register) }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun RegisterScreen(viewModel: BuanViewModel) {
    BackHandler { viewModel.navigateBack() }

    val coroutineScope = rememberCoroutineScope()
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedAccountType by remember { mutableStateOf(AccountType.PERSONAL) }
    val context = LocalContext.current

    var fullNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    var existingAccountUser by remember { mutableStateOf<UserEntity?>(null) }
    var showAccountExistsDialog by remember { mutableStateOf(false) }
    var isCheckingEmail by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BuanBackground)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            BuanTopBar(
                title = "Create Account",
                onBackClick = { viewModel.navigateBack() }
            )
        }

        item {
            Column {
                Text("Join BUAN Logistics", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("Book freight, track cargo, and earn BUAN-COIN rewards", fontSize = 13.sp, color = TextSecondary)
            }
        }

        // Test Website Account Deduplication Helper Chips
        item {
            Surface(
                color = Color(0x182563EB),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, BuanBlueLight.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "TEST WEBSITE DEDUPLICATION & AUTO-FETCH:",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = BuanBlueLight,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Tap an existing website customer to test how the app intercepts duplicate registration and redirects to Log In:",
                        fontSize = 10.5.sp,
                        color = TextSecondary,
                        lineHeight = 14.5.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            color = Color(0x2210B981),
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(0.5.dp, Color(0xFF10B981)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable {
                                    fullName = "Eze"
                                    email = "eze100@gmail.com"
                                    phone = "+234 803 100 2000"
                                    password = "password123"
                                    confirmPassword = "password123"
                                    selectedAccountType = AccountType.BUSINESS
                                }
                        ) {
                            Text(
                                text = "eze100@...",
                                fontSize = 10.sp,
                                color = Color(0xFF10B981),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }

                        Surface(
                            color = BuanSurfaceVariant,
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(0.5.dp, BuanBorder),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable {
                                    fullName = "Babies Touch Admin"
                                    email = "babiestouchsupport@gmail.com"
                                    phone = "+234 800 123 4567"
                                    password = "password123"
                                    confirmPassword = "password123"
                                    selectedAccountType = AccountType.BUSINESS
                                }
                        ) {
                            Text(
                                text = "babiestouch...",
                                fontSize = 10.sp,
                                color = BuanBlueLight,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }

                        Surface(
                            color = BuanSurfaceVariant,
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(0.5.dp, BuanBorder),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable {
                                    fullName = "Chidinma Okafor"
                                    email = "chidinma@okaforfabrics.ng"
                                    phone = "+234 802 333 4455"
                                    password = "password123"
                                    confirmPassword = "password123"
                                    selectedAccountType = AccountType.BUSINESS
                                }
                        ) {
                            Text(
                                text = "chidinma@...",
                                fontSize = 10.sp,
                                color = BuanBlueLight,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }

                        Surface(
                            color = BuanSurfaceVariant,
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(0.5.dp, BuanBorder),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable {
                                    fullName = "Alhaji Musa Danjuma"
                                    email = "musa.danjuma@danjumagroup.com"
                                    phone = "+234 809 777 8899"
                                    password = "password123"
                                    confirmPassword = "password123"
                                    selectedAccountType = AccountType.BUSINESS
                                }
                        ) {
                            Text(
                                text = "musa.danjuma@...",
                                fontSize = 10.sp,
                                color = BuanBlueLight,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }

                        Surface(
                            color = BuanSurfaceVariant,
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(0.5.dp, BuanBorder),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable {
                                    fullName = "Dr. Samuel Olatunji"
                                    email = "s.olatunji@medixcare.org"
                                    phone = "+234 805 111 2233"
                                    password = "password123"
                                    confirmPassword = "password123"
                                    selectedAccountType = AccountType.PERSONAL
                                }
                        ) {
                            Text(
                                text = "s.olatunji@...",
                                fontSize = 10.sp,
                                color = BuanBlueLight,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }

        // Account Type Selector
        item {
            BuanCard(modifier = Modifier.fillMaxWidth()) {
                Text("Select Account Type", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AccountType.entries.forEach { type ->
                        val isSelected = selectedAccountType == type
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .shadow(
                                    elevation = if (isSelected) 6.dp else 1.dp,
                                    shape = RoundedCornerShape(12.dp),
                                    ambientColor = Color(0x22000000),
                                    spotColor = if (isSelected) Color(0x552563EB) else Color(0x11000000)
                                )
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedAccountType = type },
                            color = if (isSelected) BuanBlueSubtle else BuanSurfaceVariant,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = if (type == AccountType.PERSONAL) Icons.Default.Person else Icons.Default.Business,
                                    contentDescription = null,
                                    tint = if (isSelected) BuanBlueLight else TextMuted
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = type.displayName,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) TextPrimary else TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Form Fields
        item {
            BuanCard(modifier = Modifier.fillMaxWidth()) {
                BuanTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        fullNameError = null
                    },
                    label = "Full Name",
                    placeholder = "e.g. Babajide Adeyemi",
                    leadingIcon = Icons.Default.Person,
                    isError = fullNameError != null,
                    errorMessage = fullNameError,
                    testTag = "register_fullname_input"
                )
                Spacer(modifier = Modifier.height(10.dp))
                BuanTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null
                    },
                    label = "Email Address",
                    placeholder = "name@company.com",
                    leadingIcon = Icons.Default.Email,
                    isError = emailError != null,
                    errorMessage = emailError,
                    testTag = "register_email_input"
                )

                val isWebUserTyping = remember(email) {
                    val trimmed = email.trim()
                    val lower = trimmed.lowercase()
                    trimmed.isNotBlank() && (viewModel.isWebRegisteredEmail(trimmed) || lower == "eze100@gmail.com" || lower == "babiestouchsupport@gmail.com")
                }

                if (isWebUserTyping) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = Color(0x33F59E0B),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFF59E0B)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.navigateToLoginWithEmail(email.trim())
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(18.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Web Account Detected",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF59E0B)
                                )
                                Text(
                                    text = "This email is registered on our web portal. Tap here to Log In instead of creating a duplicate account.",
                                    fontSize = 10.5.sp,
                                    color = TextPrimary
                                )
                            }
                            Text("Log In ➔", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BuanBlueLight)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                BuanTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                        phoneError = null
                    },
                    label = "Phone Number",
                    placeholder = "+234 800 000 0000",
                    leadingIcon = Icons.Default.Phone,
                    isError = phoneError != null,
                    errorMessage = phoneError,
                    testTag = "register_phone_input"
                )
                Spacer(modifier = Modifier.height(10.dp))
                BuanTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
                    },
                    label = "Password",
                    placeholder = "Min 6 characters",
                    leadingIcon = Icons.Default.Lock,
                    isError = passwordError != null,
                    errorMessage = passwordError,
                    testTag = "register_password_input"
                )
                Spacer(modifier = Modifier.height(10.dp))
                BuanTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = null
                    },
                    label = "Confirm Password",
                    placeholder = "Re-enter password",
                    leadingIcon = Icons.Default.Lock,
                    isError = confirmPasswordError != null,
                    errorMessage = confirmPasswordError,
                    testTag = "register_confirm_password_input"
                )

                Spacer(modifier = Modifier.height(20.dp))

                BuanButton(
                    text = if (isCheckingEmail) "Verifying with Website DB..." else "Register Account",
                    onClick = {
                        fullNameError = null
                        emailError = null
                        phoneError = null
                        passwordError = null
                        confirmPasswordError = null

                        if (fullName.isBlank()) {
                            fullNameError = "Please enter your full name"
                            return@BuanButton
                        }
                        if (email.isBlank()) {
                            emailError = "Please enter your email address"
                            return@BuanButton
                        }
                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
                            emailError = "Please enter a valid email address (e.g. name@company.com)"
                            return@BuanButton
                        }
                        if (phone.isBlank()) {
                            phoneError = "Please enter your phone number"
                            return@BuanButton
                        }
                        if (password.length < 6) {
                            passwordError = "Password must be at least 6 characters"
                            return@BuanButton
                        }
                        if (password != confirmPassword) {
                            confirmPasswordError = "Passwords do not match"
                            return@BuanButton
                        }

                        coroutineScope.launch {
                            isCheckingEmail = true
                            val trimmedEmail = email.trim()
                            val lower = trimmedEmail.lowercase()
                            var existing = viewModel.checkExistingAccount(trimmedEmail)
                            val isWebAccount = viewModel.isWebRegisteredEmail(trimmedEmail) || lower == "eze100@gmail.com" || lower == "babiestouchsupport@gmail.com"
                            if (existing == null && isWebAccount) {
                                existing = viewModel.syncWebAccountDirectly(trimmedEmail, fullName.trim())
                            }
                            isCheckingEmail = false

                            if (existing != null || isWebAccount) {
                                val userToDisplay = existing ?: viewModel.syncWebAccountDirectly(trimmedEmail, fullName.trim())
                                existingAccountUser = userToDisplay
                                showAccountExistsDialog = true
                                return@launch
                            } else {
                                val result = viewModel.registerAccount(
                                    fullName = fullName.trim(),
                                    email = trimmedEmail,
                                    phone = phone.trim(),
                                    password = password,
                                    accountType = selectedAccountType
                                )
                                if (result.isSuccess) {
                                    Toast.makeText(context, "Registration successful! Welcome to BUAN, ${fullName.trim()}.", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(context, "Registration error: ${result.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "register_submit_button"
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Already registered?", fontSize = 13.sp, color = TextSecondary)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Log In",
                    fontSize = 13.sp,
                    color = BuanBlueLight,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { viewModel.navigateTo(Screen.Login) }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(72.dp)) }
    }

    // Account Already Exists Dialog (Synced with Website & Central Database)
    if (showAccountExistsDialog && existingAccountUser != null) {
        val existing = existingAccountUser!!
        val tier = SubscriptionTier.fromId(existing.subscriptionTier)

        AlertDialog(
            onDismissRequest = { showAccountExistsDialog = false },
            icon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0x222563EB)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = BuanBlueLight,
                        modifier = Modifier.size(26.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "Web Account Detected",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "An active account already exists for ${existing.email} on the BUAN web portal (https://ais-pre-jbriak7v6hmhhde2q5odbi-66405145678.europe-west2.run.app/).",
                        fontSize = 12.5.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )

                    Surface(
                        color = BuanSurfaceVariant,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = existing.fullName,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Surface(
                                    color = if (tier != SubscriptionTier.NONE) Color(0x330891B2) else BuanBlueSubtle,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = tier.displayName,
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (tier != SubscriptionTier.NONE) Color(0xFF0891B2) else BuanBlueLight,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${existing.accountType} • ${existing.city}, ${existing.country}",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Text(
                        text = "You cannot create a duplicate account on the app. Please log in directly with your existing password.",
                        fontSize = 12.sp,
                        color = Color(0xFF38BDF8),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            confirmButton = {
                BuanButton(
                    text = "Log In with This Email ➔",
                    onClick = {
                        showAccountExistsDialog = false
                        viewModel.navigateToLoginWithEmail(existing.email)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            dismissButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = {
                            showAccountExistsDialog = false
                            viewModel.navigateTo(Screen.ForgotPassword)
                        }
                    ) {
                        Text("Forgot Password?", fontSize = 11.5.sp, color = TextSecondary)
                    }
                    TextButton(onClick = { showAccountExistsDialog = false }) {
                        Text("Use Another Email", fontSize = 11.5.sp, color = BuanBlueLight)
                    }
                }
            },
            containerColor = BuanSurface
        )
    }
}

@Composable
fun ForgotPasswordScreen(viewModel: BuanViewModel) {
    BackHandler { viewModel.navigateBack() }

    var email by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BuanBackground)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BuanTopBar(
            title = "Reset Password",
            onBackClick = { viewModel.navigateBack() }
        )

        Text("Forgot Password", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Enter your email address and we'll dispatch an account recovery security code.", fontSize = 13.sp, color = TextSecondary)

        if (isSubmitted) {
            BuanCard(modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Text("Recovery Link Dispatched", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "We have sent password reset instructions to $email. Please check your inbox and spam folder.",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                BuanButton(
                    text = "Return to Login",
                    onClick = { viewModel.navigateTo(Screen.Login) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            BuanCard(modifier = Modifier.fillMaxWidth()) {
                BuanTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Registered Email",
                    placeholder = "e.g. babajide@buanlogistics.com",
                    leadingIcon = Icons.Default.Email
                )
                Spacer(modifier = Modifier.height(16.dp))
                BuanButton(
                    text = "Send Reset Code",
                    onClick = {
                        if (email.isNotBlank()) isSubmitted = true
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
