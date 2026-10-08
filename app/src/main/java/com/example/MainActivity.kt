package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.data.local.BuanDatabase
import com.example.data.sample.SampleDataLoader
import com.example.ui.components.BuanBottomNavBar
import com.example.ui.components.BuanOfficialBusinessLogo
import com.example.ui.screens.admin.AdminPanelScreen
import com.example.ui.screens.auth.ForgotPasswordScreen
import com.example.ui.screens.auth.LoginScreen
import com.example.ui.screens.auth.RegisterScreen
import com.example.ui.screens.auth.WelcomeScreen
import com.example.ui.screens.help.HelpCentreScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.hubs.BecomeHubProviderScreen
import com.example.ui.screens.hubs.FindHubScreen
import com.example.ui.screens.notifications.NotificationsScreen
import com.example.ui.screens.profile.EditProfileScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.subscriptions.SubscriptionScreen
import com.example.ui.screens.quote.RequestQuoteScreen
import com.example.ui.screens.services.ServicesScreen
import com.example.ui.screens.shipments.CreateShipmentScreen
import com.example.ui.screens.shipments.ShipmentCreatedSuccessScreen
import com.example.ui.screens.shipments.ShipmentHistoryScreen
import com.example.ui.screens.tracking.LiveTrackingScreen
import com.example.ui.screens.tracking.ShipmentDetailScreen
import com.example.ui.screens.wallet.BuanCoinWalletScreen
import com.example.ui.theme.BuanBackground
import com.example.ui.theme.BuanTheme
import com.example.ui.viewmodel.BuanViewModel
import com.example.ui.viewmodel.BuanViewModelFactory
import com.example.ui.viewmodel.Screen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: BuanViewModel by viewModels {
        BuanViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Seed realistic initial database if empty and sync to Cloud Firestore
        lifecycleScope.launch {
            val db = BuanDatabase.getDatabase(applicationContext)
            SampleDataLoader.seedDatabaseIfEmpty(db)
            viewModel.syncAllUsersToCloud()
        }

        // Initialize Android notification channel for system push notifications
        com.example.util.NotificationHelper.createNotificationChannel(applicationContext)

        // Handle direct tap from notification
        intent?.getStringExtra("TRACKING_NUMBER")?.let { trackingNum ->
            if (trackingNum.isNotBlank()) {
                viewModel.navigateTo(Screen.LiveTracking(trackingNum))
            }
        }

        setContent {
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()
            BuanTheme(darkTheme = isDarkTheme) {
                BuanApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun BuanApp(viewModel: BuanViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val unreadNotifications by viewModel.unreadNotificationsCount.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showOpeningSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1000)
        showOpeningSplash = false
    }

    if (showOpeningSplash) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BuanBackground)
                .clickable { showOpeningSplash = false },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                BuanOfficialBusinessLogo(
                    fontSize = 36.sp,
                    taglineSize = 13.sp,
                    showTagline = true,
                    useWhiteCard = true,
                    primaryColor = Color(0xFF0038E0)
                )
                Spacer(modifier = Modifier.height(28.dp))
                CircularProgressIndicator(
                    color = Color(0xFF38BDF8),
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Move Cargo • Move Business",
                    fontSize = 11.5.sp,
                    color = Color(0xFF94A3B8),
                    letterSpacing = 0.5.sp
                )
            }
        }
        return
    }

    LaunchedEffect(userMessage) {
        userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUserMessage()
        }
    }

    val showBottomBar = when (currentScreen) {
        is Screen.Welcome,
        is Screen.Login,
        is Screen.Register,
        is Screen.ForgotPassword,
        is Screen.EditProfile,
        is Screen.AdminPanel,
        is Screen.ShipmentCreatedSuccess -> false
        else -> true
    }

    val topPadding = if (currentScreen is Screen.Welcome || currentScreen is Screen.Login) {
        0.dp
    } else {
        WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BuanBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                BuanBottomNavBar(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) },
                    unreadNotifications = unreadNotifications
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BuanBackground)
                .padding(
                    top = topPadding,
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "buan_screen_switch"
            ) { screen ->
                when (screen) {
                    is Screen.Welcome -> WelcomeScreen(viewModel = viewModel)
                    is Screen.Login -> LoginScreen(viewModel = viewModel)
                    is Screen.Register -> RegisterScreen(viewModel = viewModel)
                    is Screen.ForgotPassword -> ForgotPasswordScreen(viewModel = viewModel)
                    is Screen.Home -> HomeScreen(viewModel = viewModel)
                    is Screen.Shipments -> ShipmentHistoryScreen(viewModel = viewModel)
                    is Screen.CreateShipment -> RequestQuoteScreen(viewModel = viewModel)
                    is Screen.ShipmentCreatedSuccess -> ShipmentCreatedSuccessScreen(
                        trackingNumber = screen.trackingNumber,
                        viewModel = viewModel
                    )
                    is Screen.ShipmentDetail -> ShipmentDetailScreen(
                        trackingNumber = screen.trackingNumber,
                        viewModel = viewModel
                    )
                    is Screen.LiveTracking -> LiveTrackingScreen(
                        trackingNumber = screen.trackingNumber,
                        viewModel = viewModel
                    )
                    is Screen.RequestQuote -> RequestQuoteScreen(viewModel = viewModel)
                    is Screen.Services -> ServicesScreen(viewModel = viewModel)
                    is Screen.FindHub -> FindHubScreen(viewModel = viewModel)
                    is Screen.BecomeHubProvider -> BecomeHubProviderScreen(viewModel = viewModel)
                    is Screen.BuanCoinWallet -> BuanCoinWalletScreen(viewModel = viewModel)
                    is Screen.Notifications -> NotificationsScreen(viewModel = viewModel)
                    is Screen.Profile -> ProfileScreen(viewModel = viewModel)
                    is Screen.EditProfile -> EditProfileScreen(viewModel = viewModel)
                    is Screen.Subscriptions -> SubscriptionScreen(viewModel = viewModel)
                    is Screen.AdminPanel -> AdminPanelScreen(viewModel = viewModel)
                    is Screen.HelpCentre -> HelpCentreScreen(viewModel = viewModel)
                }
            }
        }
    }
}
