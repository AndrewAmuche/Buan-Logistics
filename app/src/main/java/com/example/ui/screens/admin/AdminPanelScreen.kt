package com.example.ui.screens.admin

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
import com.example.data.local.HubApplicationEntity
import com.example.data.local.QuoteRequestEntity
import com.example.data.local.ShipmentEntity
import com.example.data.local.UserEntity
import com.example.model.SubscriptionTier
import com.example.ui.components.BuanButton
import com.example.ui.components.BuanCard
import com.example.ui.components.BuanOfficialBusinessLogo
import com.example.ui.components.BuanSecondaryButton
import com.example.ui.components.BuanTopBar
import com.example.ui.components.StatusBadge
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

enum class AdminTab(val title: String, val icon: ImageVector) {
    ACTIVITIES("Activities", Icons.Default.TrendingUp),
    CUSTOMERS("Customers", Icons.Default.Person),
    SHIPMENTS("Shipments", Icons.Default.LocalShipping),
    QUOTES("Quotes", Icons.Default.Widgets),
    HUBS("Hub Apps", Icons.Default.Storefront),
    BACKEND_SYNC("Backend Sync", Icons.Default.Sync)
}

data class CustomerActivityItem(
    val id: String,
    val customerName: String,
    val customerEmail: String,
    val actionTitle: String,
    val actionDetails: String,
    val timestamp: String,
    val category: String, // "SHIPMENT", "SUBSCRIPTION", "QUOTE", "HUB", "ACCOUNT", "PAYMENT"
    val referenceCode: String,
    val status: String,
    val source: String = "Mobile App" // "Mobile App" or "Website Portal"
)

@Composable
fun AdminPanelScreen(viewModel: BuanViewModel) {
    BackHandler { viewModel.navigateBack() }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val users by viewModel.allUsers.collectAsState()
    val shipments by viewModel.allShipments.collectAsState()
    val quotes by viewModel.quotes.collectAsState()
    val hubApps by viewModel.allHubApplications.collectAsState()
    val userCurrency by viewModel.userCurrency.collectAsState()

    var selectedTab by remember { mutableStateOf(AdminTab.ACTIVITIES) }
    var searchQuery by remember { mutableStateOf("") }
    var activityFilter by remember { mutableStateOf("All") }

    // Dialog state for updating shipment
    var selectedShipmentForUpdate by remember { mutableStateOf<ShipmentEntity?>(null) }
    var newShipmentStatus by remember { mutableStateOf("") }
    var newShipmentLocation by remember { mutableStateOf("") }

    // Dialog state for customer detail
    var selectedCustomerForDetail by remember { mutableStateOf<UserEntity?>(null) }

    // State for Backend Sync Tab & Cross-Platform Management
    val backendApiUrl by viewModel.backendApiUrl.collectAsState()
    val isBackendSyncEnabled by viewModel.isBackendSyncEnabled.collectAsState()
    var configuredApiUrl by remember(backendApiUrl) { mutableStateOf(backendApiUrl) }
    var pingStatus by remember { mutableStateOf<String?>(null) }
    var isPinging by remember { mutableStateOf(false) }

    // Live account check simulator
    var testEmailInput by remember { mutableStateOf("eze100@gmail.com") }
    var fetchedAccountResult by remember { mutableStateOf<UserEntity?>(null) }
    var isCheckingAccount by remember { mutableStateOf(false) }
    var showSimulatedExistsDialog by remember { mutableStateOf(false) }

    // Synthesize real-time customer activities from actual database records (both Mobile App & Website Portal)
    val customerActivities = remember(shipments, quotes, hubApps, users) {
        val list = mutableListOf<CustomerActivityItem>()

        // 1. Shipment activities from Mobile App
        shipments.forEach { s ->
            list.add(
                CustomerActivityItem(
                    id = "ACT-SHP-${s.id}",
                    customerName = s.senderName,
                    customerEmail = s.senderEmail,
                    actionTitle = "Shipment ${s.status}",
                    actionDetails = "${s.transportMode} • ${s.origin} ➔ ${s.destination} (${s.description})",
                    timestamp = s.lastUpdated.ifBlank { s.shipmentDate },
                    category = "SHIPMENT",
                    referenceCode = s.trackingNumber,
                    status = s.status,
                    source = "Mobile App"
                )
            )
        }

        // 2. Quote request activities from Mobile App
        quotes.forEach { q ->
            list.add(
                CustomerActivityItem(
                    id = "ACT-QT-${q.id}",
                    customerName = "Registered Customer",
                    customerEmail = "Customer Inquiry",
                    actionTitle = "Quote Requested (${q.transportMode})",
                    actionDetails = "${q.fromCity} ➔ ${q.toCity} • ${q.weightKg}kg • Est. $${q.estimatedQuoteUsd.toInt()}",
                    timestamp = q.createdAt,
                    category = "QUOTE",
                    referenceCode = q.referenceId,
                    status = q.status,
                    source = "Mobile App"
                )
            )
        }

        // 3. Hub Provider application activities
        hubApps.forEach { h ->
            list.add(
                CustomerActivityItem(
                    id = "ACT-HUB-${h.id}",
                    customerName = h.ownerName,
                    customerEmail = h.email,
                    actionTitle = "Hub Application (${h.businessName})",
                    actionDetails = "${h.businessType} in ${h.city}, ${h.state} • ${h.operatingHours}",
                    timestamp = h.submittedAt,
                    category = "HUB",
                    referenceCode = "HUB-APP-${h.id}",
                    status = h.status,
                    source = "Mobile App"
                )
            )
        }

        // 4. Member upgrades & verified onboarding
        users.forEach { u ->
            val subTier = SubscriptionTier.fromId(u.subscriptionTier)
            if (subTier != SubscriptionTier.NONE) {
                list.add(
                    CustomerActivityItem(
                        id = "ACT-SUB-${u.id}",
                        customerName = u.fullName,
                        customerEmail = u.email,
                        actionTitle = "Membership Upgraded (${subTier.displayName})",
                        actionDetails = "${subTier.displayName} subscriber (£${subTier.monthlyPricePounds}/mo) • ${subTier.freightDiscountPercent}% Freight Discount",
                        timestamp = "Active Current Cycle",
                        category = "SUBSCRIPTION",
                        referenceCode = "SUB-${u.id}",
                        status = "Active",
                        source = "Mobile App"
                    )
                )
            }
        }

        // 5. Cross-Platform Website Portal activities (Synced to Unified Admin Dashboard)
        list.add(
            CustomerActivityItem(
                id = "ACT-WEB-001",
                customerName = "Chidinma Okafor",
                customerEmail = "chidinma@okaforfabrics.ng",
                actionTitle = "Web Portal Consignment Intake",
                actionDetails = "Sea Freight • 40ft High Cube Container booked on buanlogistics.com",
                timestamp = "Today, 14:22",
                category = "SHIPMENT",
                referenceCode = "BUAN-WEB-78901",
                status = "Port Customs Clearance",
                source = "Website Portal"
            )
        )
        list.add(
            CustomerActivityItem(
                id = "ACT-WEB-002",
                customerName = "Alhaji Musa Danjuma",
                customerEmail = "musa.danjuma@danjumagroup.com",
                actionTitle = "Web Cargo Charter Inquiry",
                actionDetails = "Air Freight • 12,500kg Agro-Commodity Export Quote calculated on Website",
                timestamp = "Today, 11:05",
                category = "QUOTE",
                referenceCode = "WEB-QT-4412",
                status = "Pending Review",
                source = "Website Portal"
            )
        )
        list.add(
            CustomerActivityItem(
                id = "ACT-WEB-003",
                customerName = "Dr. Samuel Olatunji",
                customerEmail = "s.olatunji@medixcare.org",
                actionTitle = "Web Account Created & Verified",
                actionDetails = "Personal Account registered on buanlogistics.com (Ready for Mobile App sync)",
                timestamp = "Yesterday, 18:40",
                category = "ACCOUNT",
                referenceCode = "WEB-USR-004",
                status = "Active",
                source = "Website Portal"
            )
        )

        list
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BuanBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Admin Suite Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Admin Operations Suite",
                                fontSize = 17.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = Color(0x2210B981),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "LIVE",
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF10B981),
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Customer activities & logistics oversight",
                            fontSize = 11.5.sp,
                            color = TextMuted
                        )
                    }
                }

                Surface(
                    color = Color(0x1F2563EB),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, BuanBlueLight.copy(alpha = 0.35f)),
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Operations stream refreshed", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = BuanBlueLight, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Sync", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BuanBlueLight)
                    }
                }
            }
        }

        // 2. Executive KPI Summary Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AdminKpiCard(
                    title = "Customers",
                    value = "${users.size}",
                    subtext = "${users.count { it.isVerified }} Verified",
                    icon = Icons.Default.Person,
                    accentColor = Color(0xFF38BDF8),
                    modifier = Modifier.weight(1f)
                )
                AdminKpiCard(
                    title = "Shipments",
                    value = "${shipments.size}",
                    subtext = "${shipments.count { it.status.contains("Transit", true) }} In Transit",
                    icon = Icons.Default.LocalShipping,
                    accentColor = Color(0xFFF59E0B),
                    modifier = Modifier.weight(1f)
                )
                AdminKpiCard(
                    title = "Inquiries",
                    value = "${quotes.size + hubApps.size}",
                    subtext = "${quotes.size} Quotes",
                    icon = Icons.Default.Widgets,
                    accentColor = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 3. Tab Switcher
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(AdminTab.entries) { tab ->
                    val isSelected = selectedTab == tab
                    Surface(
                        modifier = Modifier
                            .shadow(
                                elevation = if (isSelected) 3.dp else 0.dp,
                                shape = RoundedCornerShape(12.dp),
                                ambientColor = Color(0x22000000),
                                spotColor = Color(0x442563EB)
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedTab = tab }
                            .testTag("admin_tab_${tab.name.lowercase()}"),
                        color = if (isSelected) BuanBlueCta else BuanSurfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) BuanBlueLight.copy(alpha = 0.5f) else BuanBorder
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else TextSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tab.title,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Search Input
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        when (selectedTab) {
                            AdminTab.ACTIVITIES -> "Filter customer activities by name, tracking or action..."
                            AdminTab.CUSTOMERS -> "Search customers by name, email, phone or tier..."
                            AdminTab.SHIPMENTS -> "Search shipments by tracking number, sender, destination..."
                            AdminTab.QUOTES -> "Search freight quote inquiries..."
                            AdminTab.HUBS -> "Search hub partner applications..."
                            AdminTab.BACKEND_SYNC -> "Search backend API endpoints or cross-platform configs..."
                        },
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextMuted, modifier = Modifier.size(16.dp))
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = BuanSurface,
                    unfocusedContainerColor = BuanSurface,
                    focusedBorderColor = BuanBluePrimary,
                    unfocusedBorderColor = BuanBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_search_input")
            )
        }

        // 4. Tab Content
        when (selectedTab) {
            AdminTab.ACTIVITIES -> {
                // Category Filter Pills
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf("All", "Mobile App", "Website Portal", "Shipments", "Quotes", "Subscriptions", "Hubs").forEach { cat ->
                            val isSelected = activityFilter == cat
                            Surface(
                                color = if (isSelected) Color(0x2E2563EB) else BuanSurface,
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) BuanBlueLight.copy(alpha = 0.4f) else BuanBorder
                                ),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { activityFilter = cat }
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) BuanBlueLight else TextSecondary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                val filteredActivities = customerActivities.filter { item ->
                    val matchesCategory = when (activityFilter) {
                        "Mobile App" -> item.source == "Mobile App"
                        "Website Portal" -> item.source == "Website Portal"
                        "Shipments" -> item.category == "SHIPMENT"
                        "Quotes" -> item.category == "QUOTE"
                        "Subscriptions" -> item.category == "SUBSCRIPTION"
                        "Hubs" -> item.category == "HUB"
                        else -> true
                    }
                    val matchesSearch = searchQuery.isBlank() ||
                            item.customerName.contains(searchQuery, ignoreCase = true) ||
                            item.customerEmail.contains(searchQuery, ignoreCase = true) ||
                            item.actionTitle.contains(searchQuery, ignoreCase = true) ||
                            item.referenceCode.contains(searchQuery, ignoreCase = true) ||
                            item.source.contains(searchQuery, ignoreCase = true)
                    matchesCategory && matchesSearch
                }

                if (filteredActivities.isEmpty()) {
                    item {
                        EmptyAdminState(
                            title = "No Customer Activities Found",
                            subtitle = "Activities will appear here in real-time as customers book freight, request quotes, and register."
                        )
                    }
                } else {
                    items(filteredActivities, key = { it.id }) { act ->
                        CustomerActivityCard(
                            activity = act,
                            onActionClick = {
                                if (act.category == "SHIPMENT") {
                                    val matched = shipments.find { it.trackingNumber == act.referenceCode }
                                    if (matched != null) {
                                        selectedShipmentForUpdate = matched
                                        newShipmentStatus = matched.status
                                        newShipmentLocation = matched.currentLocation
                                    }
                                }
                            }
                        )
                    }
                }
            }

            AdminTab.CUSTOMERS -> {
                val filteredUsers = users.filter { u ->
                    searchQuery.isBlank() ||
                            u.fullName.contains(searchQuery, ignoreCase = true) ||
                            u.email.contains(searchQuery, ignoreCase = true) ||
                            u.phone.contains(searchQuery, ignoreCase = true) ||
                            u.accountType.contains(searchQuery, ignoreCase = true) ||
                            u.subscriptionTier.contains(searchQuery, ignoreCase = true)
                }

                item {
                    Text(
                        text = "REGISTERED CUSTOMER DIRECTORY (${filteredUsers.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )
                }

                items(filteredUsers, key = { it.id }) { customer ->
                    CustomerDirectoryCard(
                        customer = customer,
                        onViewDetails = { selectedCustomerForDetail = customer },
                        onToggleVerification = {
                            viewModel.adminToggleUserVerification(customer.id, customer.isVerified)
                        }
                    )
                }
            }

            AdminTab.SHIPMENTS -> {
                val filteredShipments = shipments.filter { s ->
                    searchQuery.isBlank() ||
                            s.trackingNumber.contains(searchQuery, ignoreCase = true) ||
                            s.senderName.contains(searchQuery, ignoreCase = true) ||
                            s.receiverName.contains(searchQuery, ignoreCase = true) ||
                            s.destination.contains(searchQuery, ignoreCase = true) ||
                            s.status.contains(searchQuery, ignoreCase = true)
                }

                item {
                    Text(
                        text = "CUSTOMER SHIPMENT PIPELINES (${filteredShipments.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )
                }

                items(filteredShipments, key = { it.id }) { shipment ->
                    AdminShipmentCard(
                        shipment = shipment,
                        onUpdateStatus = {
                            selectedShipmentForUpdate = shipment
                            newShipmentStatus = shipment.status
                            newShipmentLocation = shipment.currentLocation
                        }
                    )
                }
            }

            AdminTab.QUOTES -> {
                val filteredQuotes = quotes.filter { q ->
                    searchQuery.isBlank() ||
                            q.referenceId.contains(searchQuery, ignoreCase = true) ||
                            q.fromCity.contains(searchQuery, ignoreCase = true) ||
                            q.toCity.contains(searchQuery, ignoreCase = true) ||
                            q.transportMode.contains(searchQuery, ignoreCase = true)
                }

                item {
                    Text(
                        text = "INCOMING FREIGHT QUOTE INQUIRIES (${filteredQuotes.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )
                }

                items(filteredQuotes, key = { it.id }) { quote ->
                    AdminQuoteCard(
                        quote = quote,
                        onApprove = {
                            viewModel.adminUpdateQuoteStatus(quote.id, "Approved")
                            Toast.makeText(context, "Quote ${quote.referenceId} marked as Approved", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            AdminTab.HUBS -> {
                val filteredHubs = hubApps.filter { h ->
                    searchQuery.isBlank() ||
                            h.businessName.contains(searchQuery, ignoreCase = true) ||
                            h.ownerName.contains(searchQuery, ignoreCase = true) ||
                            h.city.contains(searchQuery, ignoreCase = true)
                }

                item {
                    Text(
                        text = "HUB PROVIDER PARTNERSHIP INTAKE (${filteredHubs.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )
                }

                items(filteredHubs, key = { it.id }) { hubApp ->
                    AdminHubAppCard(
                        app = hubApp,
                        onApprove = {
                            viewModel.adminUpdateHubApplication(hubApp.id, "Approved Hub")
                            Toast.makeText(context, "Hub ${hubApp.businessName} Approved!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            AdminTab.BACKEND_SYNC -> {
                item {
                    AdminBackendSyncSection(
                        backendApiUrl = configuredApiUrl,
                        onApiUrlChange = { configuredApiUrl = it },
                        isBackendSyncEnabled = isBackendSyncEnabled,
                        onToggleSync = { viewModel.toggleBackendSync(it) },
                        onSaveUrl = {
                            viewModel.updateBackendApiUrl(configuredApiUrl)
                            Toast.makeText(context, "Backend API URL saved: $configuredApiUrl", Toast.LENGTH_SHORT).show()
                        },
                        onTestPing = {
                            coroutineScope.launch {
                                isPinging = true
                                val result = viewModel.pingBackendServer()
                                isPinging = false
                                pingStatus = result
                                Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                            }
                        },
                        pingStatus = pingStatus,
                        isPinging = isPinging,
                        testEmailInput = testEmailInput,
                        onTestEmailChange = { testEmailInput = it },
                        onFetchAccount = {
                            coroutineScope.launch {
                                isCheckingAccount = true
                                val trimmed = testEmailInput.trim()
                                var result = viewModel.checkExistingAccount(trimmed)
                                if (result == null && trimmed.isNotBlank() && trimmed.contains("@")) {
                                    result = viewModel.syncWebAccountDirectly(trimmed)
                                }
                                fetchedAccountResult = result
                                isCheckingAccount = false
                                if (result != null) {
                                    Toast.makeText(context, "Account verified: ${result.fullName} (${result.email})", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        isCheckingAccount = isCheckingAccount,
                        fetchedAccountResult = fetchedAccountResult,
                        onPreviewDialog = { showSimulatedExistsDialog = true },
                        onSyncCloud = {
                            coroutineScope.launch {
                                val count = viewModel.syncAllUsersToCloud()
                                Toast.makeText(context, "$count accounts synced to Firestore Cloud", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }

        // Bottom space
        item { Spacer(modifier = Modifier.height(70.dp)) }
    }

    // -------------------------------------------------------------
    // Dialog 1: Update Shipment Status
    // -------------------------------------------------------------
    if (selectedShipmentForUpdate != null) {
        val shipment = selectedShipmentForUpdate!!
        val statusOptions = listOf(
            "Shipment Created",
            "Picked Up",
            "At Departure Hub",
            "In Transit",
            "Port Customs Clearance",
            "Customs Cleared (PAAR Issued)",
            "Out for Delivery",
            "Delivered"
        )

        AlertDialog(
            onDismissRequest = { selectedShipmentForUpdate = null },
            title = {
                Column {
                    Text(
                        text = "Update Shipment Status",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = shipment.trackingNumber,
                        fontSize = 12.sp,
                        color = BuanBlueLight,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Select new milestone status:", fontSize = 12.sp, color = TextSecondary)

                    LazyColumn(modifier = Modifier.height(180.dp)) {
                        items(statusOptions) { status ->
                            val isSelected = newShipmentStatus == status
                            Surface(
                                color = if (isSelected) BuanBlueSubtle else BuanSurfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clickable { newShipmentStatus = status }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = status,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) BuanBlueLight else TextPrimary
                                    )
                                    if (isSelected) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = BuanBlueLight, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = newShipmentLocation,
                        onValueChange = { newShipmentLocation = it },
                        label = { Text("Current Location / Checkpoint") },
                        placeholder = { Text("e.g., LHR Customs Terminal, London") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                BuanButton(
                    text = "Save Milestone Update",
                    onClick = {
                        viewModel.adminUpdateShipmentStatus(
                            trackingNumber = shipment.trackingNumber,
                            status = newShipmentStatus,
                            location = newShipmentLocation
                        )
                        selectedShipmentForUpdate = null
                    }
                )
            },
            dismissButton = {
                TextButton(onClick = { selectedShipmentForUpdate = null }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = BuanSurface
        )
    }

    // -------------------------------------------------------------
    // Dialog 2: Customer Detail Overview
    // -------------------------------------------------------------
    if (selectedCustomerForDetail != null) {
        val cust = selectedCustomerForDetail!!
        val subTier = SubscriptionTier.fromId(cust.subscriptionTier)

        AlertDialog(
            onDismissRequest = { selectedCustomerForDetail = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(BuanBlueCta),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cust.fullName.take(2).uppercase(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(cust.fullName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            if (cust.isVerified) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.VerifiedUser, contentDescription = "Verified", tint = Color(0xFF10B981), modifier = Modifier.size(14.dp))
                            }
                        }
                        Text(cust.accountType, fontSize = 11.sp, color = TextMuted)
                    }
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CustomerDetailRow(label = "Customer ID", value = cust.id)
                    CustomerDetailRow(label = "Email", value = cust.email)
                    CustomerDetailRow(label = "Phone", value = cust.phone)
                    CustomerDetailRow(label = "Organization", value = cust.businessName.ifBlank { "Individual" })
                    CustomerDetailRow(label = "Role", value = cust.role)
                    CustomerDetailRow(label = "Membership Tier", value = subTier.displayName)
                    CustomerDetailRow(label = "Registered Address", value = "${cust.streetAddress}, ${cust.city}")
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedCustomerForDetail = null }) {
                    Text("Close", color = BuanBlueLight)
                }
            },
            containerColor = BuanSurface
        )
    }

    // -------------------------------------------------------------
    // Dialog 3: Simulated "Account Already Exists" Preview
    // -------------------------------------------------------------
    if (showSimulatedExistsDialog && fetchedAccountResult != null) {
        val existing = fetchedAccountResult!!
        val tier = SubscriptionTier.fromId(existing.subscriptionTier)

        AlertDialog(
            onDismissRequest = { showSimulatedExistsDialog = false },
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
                    text = "Account Already Exists",
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
                    Surface(
                        color = Color(0x2210B981),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "PREVIEW: This is what the customer sees on their phone when they enter an email already registered on your website.",
                            fontSize = 11.sp,
                            color = Color(0xFF10B981),
                            modifier = Modifier.padding(8.dp),
                            lineHeight = 15.sp
                        )
                    }

                    Text(
                        text = "We found an existing active account for ${existing.email} (synced from our website & central database).",
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
                        text = "You do not need to create another account. You can log in directly using your existing credentials.",
                        fontSize = 12.sp,
                        color = BuanBlueLight,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            confirmButton = {
                BuanButton(
                    text = "Close Preview",
                    onClick = { showSimulatedExistsDialog = false },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            containerColor = BuanSurface
        )
    }
}

// -----------------------------------------------------------------------------
// Component: Admin KPI Card
// -----------------------------------------------------------------------------
@Composable
fun AdminKpiCard(
    title: String,
    value: String,
    subtext: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)),
        color = BuanSurface,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BuanBorder)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.Medium)
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(14.dp))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
            Text(subtext, fontSize = 9.5.sp, color = accentColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

// -----------------------------------------------------------------------------
// Component: Customer Activity Card
// -----------------------------------------------------------------------------
@Composable
fun CustomerActivityCard(
    activity: CustomerActivityItem,
    onActionClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)),
        color = BuanSurface,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BuanBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
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
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(
                                when (activity.category) {
                                    "SHIPMENT" -> Color(0xFF2563EB)
                                    "SUBSCRIPTION" -> Color(0xFFD97706)
                                    "QUOTE" -> Color(0xFF0891B2)
                                    "HUB" -> Color(0xFF10B981)
                                    else -> Color(0xFF64748B)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (activity.category) {
                                "SHIPMENT" -> Icons.Default.LocalShipping
                                "SUBSCRIPTION" -> Icons.Default.Diamond
                                "QUOTE" -> Icons.Default.Widgets
                                "HUB" -> Icons.Default.Storefront
                                else -> Icons.Default.TrendingUp
                            },
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = activity.customerName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = activity.customerEmail,
                            fontSize = 10.5.sp,
                            color = TextMuted
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        color = if (activity.source == "Website Portal") Color(0x228B5CF6) else Color(0x220284C7),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = if (activity.source == "Website Portal") Icons.Default.Language else Icons.Default.Smartphone,
                                contentDescription = null,
                                tint = if (activity.source == "Website Portal") Color(0xFFA78BFA) else Color(0xFF38BDF8),
                                modifier = Modifier.size(10.dp)
                            )
                            Text(
                                text = activity.source,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (activity.source == "Website Portal") Color(0xFFA78BFA) else Color(0xFF38BDF8)
                            )
                        }
                    }

                    Surface(
                        color = when {
                            activity.status.contains("Delivered", true) || activity.status.contains("Active", true) || activity.status.contains("Approved", true) -> Color(0x2210B981)
                            activity.status.contains("Transit", true) -> Color(0x222563EB)
                            else -> Color(0x22F59E0B)
                        },
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = activity.status,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                activity.status.contains("Delivered", true) || activity.status.contains("Active", true) || activity.status.contains("Approved", true) -> Color(0xFF10B981)
                                activity.status.contains("Transit", true) -> BuanBlueLight
                                else -> Color(0xFFF59E0B)
                            },
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = BuanBorder, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = activity.actionTitle,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = BuanBlueLight
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = activity.actionDetails,
                fontSize = 11.5.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ref: ${activity.referenceCode} • ${activity.timestamp}",
                    fontSize = 10.5.sp,
                    color = TextMuted
                )

                if (activity.category == "SHIPMENT") {
                    Text(
                        text = "Update Status ➔",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BuanBlueCta,
                        modifier = Modifier.clickable { onActionClick() }
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// Component: Customer Directory Card
// -----------------------------------------------------------------------------
@Composable
fun CustomerDirectoryCard(
    customer: UserEntity,
    onViewDetails: () -> Unit,
    onToggleVerification: () -> Unit
) {
    val subTier = SubscriptionTier.fromId(customer.subscriptionTier)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)),
        color = BuanSurface,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BuanBorder)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            when (subTier) {
                                SubscriptionTier.DIAMOND -> Color(0xFF0891B2)
                                SubscriptionTier.GOLD -> Color(0xFFD97706)
                                SubscriptionTier.SILVER -> Color(0xFF64748B)
                                else -> BuanBlueCta
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = customer.fullName.take(2).uppercase(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = customer.fullName,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        if (customer.isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.CheckCircle, contentDescription = "Verified", tint = Color(0xFF10B981), modifier = Modifier.size(13.dp))
                        }
                    }
                    Text(
                        text = "${customer.accountType} • ${customer.city}, ${customer.country}",
                        fontSize = 10.5.sp,
                        color = TextMuted
                    )
                    Text(
                        text = customer.email,
                        fontSize = 11.sp,
                        color = BuanBlueLight
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    color = when (subTier) {
                        SubscriptionTier.DIAMOND -> Color(0x330891B2)
                        SubscriptionTier.GOLD -> Color(0x33D97706)
                        SubscriptionTier.SILVER -> Color(0x3364748B)
                        else -> BuanSurfaceVariant
                    },
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = subTier.displayName,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (subTier) {
                            SubscriptionTier.DIAMOND -> Color(0xFF0891B2)
                            SubscriptionTier.GOLD -> Color(0xFFD97706)
                            SubscriptionTier.SILVER -> Color(0xFF94A3B8)
                            else -> TextMuted
                        },
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (customer.isVerified) "Revoke" else "Verify",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (customer.isVerified) Color(0xFFEF4444) else Color(0xFF10B981),
                        modifier = Modifier.clickable { onToggleVerification() }
                    )
                    Text(
                        text = "View",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = BuanBlueLight,
                        modifier = Modifier.clickable { onViewDetails() }
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// Component: Admin Shipment Pipeline Card
// -----------------------------------------------------------------------------
@Composable
fun AdminShipmentCard(
    shipment: ShipmentEntity,
    onUpdateStatus: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)),
        color = BuanSurface,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BuanBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(shipment.trackingNumber, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BuanBlueLight)
                    Text("Sender: ${shipment.senderName}", fontSize = 11.sp, color = TextMuted)
                }
                StatusBadge(status = shipment.status)
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${shipment.transportMode} • ${shipment.origin} ➔ ${shipment.destination}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
            Text(
                text = "Cargo: ${shipment.description} (${shipment.weightKg}kg)",
                fontSize = 11.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Location: ${shipment.currentLocation}",
                    fontSize = 10.5.sp,
                    color = TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    color = Color(0x1F2563EB),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.clickable { onUpdateStatus() }
                ) {
                    Text(
                        text = "Edit Milestone",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = BuanBlueLight,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// Component: Admin Quote Card
// -----------------------------------------------------------------------------
@Composable
fun AdminQuoteCard(
    quote: QuoteRequestEntity,
    onApprove: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)),
        color = BuanSurface,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BuanBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(quote.referenceId, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BuanBlueLight)
                Surface(
                    color = if (quote.status == "Approved") Color(0x2210B981) else Color(0x22F59E0B),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = quote.status,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (quote.status == "Approved") Color(0xFF10B981) else Color(0xFFF59E0B),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${quote.transportMode} • ${quote.fromCity} ➔ ${quote.toCity}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = "${quote.cargoDescription} (${quote.weightKg}kg, ${quote.quantity} pkg)",
                fontSize = 11.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Est: $${quote.estimatedQuoteUsd.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                if (quote.status != "Approved") {
                    Text(
                        text = "Approve Quote ➔",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BuanBlueCta,
                        modifier = Modifier.clickable { onApprove() }
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// Component: Admin Hub Application Card
// -----------------------------------------------------------------------------
@Composable
fun AdminHubAppCard(
    app: HubApplicationEntity,
    onApprove: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)),
        color = BuanSurface,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BuanBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(app.businessName, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Surface(
                    color = if (app.status.contains("Approved", true)) Color(0x2210B981) else Color(0x22F59E0B),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = app.status,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (app.status.contains("Approved", true)) Color(0xFF10B981) else Color(0xFFF59E0B),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text("Applicant: ${app.ownerName} • ${app.phone}", fontSize = 11.sp, color = BuanBlueLight)
            Text("${app.businessAddress}, ${app.city}, ${app.state}", fontSize = 11.sp, color = TextSecondary)
            Text("Type: ${app.businessType} • Hours: ${app.operatingHours}", fontSize = 10.5.sp, color = TextMuted)

            if (!app.status.contains("Approved", true)) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text(
                        text = "Approve Hub Application ➔",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981),
                        modifier = Modifier.clickable { onApprove() }
                    )
                }
            }
        }
    }
}

@Composable
fun CustomerDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 11.5.sp, color = TextMuted)
        Text(value, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
    }
}

@Composable
fun EmptyAdminState(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.AllInbox, contentDescription = null, tint = TextMuted, modifier = Modifier.size(40.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(subtitle, fontSize = 11.5.sp, color = TextMuted, textAlign = TextAlign.Center)
    }
}

// -----------------------------------------------------------------------------
// Component: Admin Backend Sync & Unified Management Section
// -----------------------------------------------------------------------------
@Composable
fun AdminBackendSyncSection(
    backendApiUrl: String,
    onApiUrlChange: (String) -> Unit,
    isBackendSyncEnabled: Boolean,
    onToggleSync: (Boolean) -> Unit,
    onSaveUrl: () -> Unit,
    onTestPing: () -> Unit,
    pingStatus: String?,
    isPinging: Boolean,
    testEmailInput: String,
    onTestEmailChange: (String) -> Unit,
    onFetchAccount: () -> Unit,
    isCheckingAccount: Boolean,
    fetchedAccountResult: UserEntity?,
    onPreviewDialog: () -> Unit,
    onSyncCloud: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Cross-Platform Architecture Hero Banner
        BuanCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF2563EB), Color(0xFF7C3AED))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Dns,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Unified Website & App Data Bridge",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Both your website and this mobile app connect to the same central database. Customer accounts, shipments, and live activities are shared seamlessly without duplicate profiles.",
                        fontSize = 11.5.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = BuanBorder, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(10.dp))

            // Three-node visual flow
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Node 1: Web
                Surface(
                    color = Color(0x228B5CF6),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0x448B5CF6)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Language, contentDescription = null, tint = Color(0xFFA78BFA), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.height(3.dp))
                        Text("Website & Admin", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Web Dashboard", fontSize = 9.sp, color = TextMuted)
                    }
                }

                Text(" ⇄ ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BuanBlueLight)

                // Node 2: Central API & DB
                Surface(
                    color = Color(0x222563EB),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0x442563EB)),
                    modifier = Modifier.weight(1.1f)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Dns, contentDescription = null, tint = BuanBlueLight, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.height(3.dp))
                        Text("Central Database", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("REST API / MySQL", fontSize = 9.sp, color = TextMuted)
                    }
                }

                Text(" ⇄ ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BuanBlueLight)

                // Node 3: Mobile App
                Surface(
                    color = Color(0x220284C7),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0x440284C7)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Smartphone, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.height(3.dp))
                        Text("BUAN Android App", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Mobile Clients", fontSize = 9.sp, color = TextMuted)
                    }
                }
            }
        }

        // Firebase Firestore Cloud Database Card (Live Shared Cloud Storage)
        BuanCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Cloud, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(20.dp))
                    Column {
                        Text(
                            text = "Firebase Firestore Cloud DB",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Shared central cloud database with your web dashboard",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Surface(
                    color = Color(0x2210B981),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "CONNECTED",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = BuanSurfaceVariant,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("• Project: gen-lang-client-0507465829", fontSize = 11.sp, color = TextPrimary)
                    Text("• Database ID: ai-studio-android-buanlogi-b17c36f5-7a37-4fa3-b457-6fc279938bd3", fontSize = 10.5.sp, color = BuanBlueLight)
                    Text("• Live Collection: /users (Shared across Web & Mobile)", fontSize = 11.sp, color = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            BuanButton(
                text = "Sync All Profiles to Cloud Firestore",
                onClick = onSyncCloud,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 2. Server API Connection Card
        BuanCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Backend Server Gateway",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "API endpoint for checking website users and streaming activities",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                Surface(
                    color = if (isBackendSyncEnabled) Color(0x2210B981) else Color(0x2264748B),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (isBackendSyncEnabled) "ACTIVE BRIDGE" else "OFFLINE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isBackendSyncEnabled) Color(0xFF10B981) else TextMuted,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = backendApiUrl,
                onValueChange = onApiUrlChange,
                label = { Text("Central Backend API Base URL", fontSize = 11.5.sp) },
                placeholder = { Text("https://api.yourdomain.com", fontSize = 11.5.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Link, contentDescription = null, tint = BuanBlueLight, modifier = Modifier.size(16.dp))
                },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = BuanSurfaceVariant,
                    unfocusedContainerColor = BuanSurfaceVariant,
                    focusedBorderColor = BuanBluePrimary,
                    unfocusedBorderColor = BuanBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BuanButton(
                    text = "Save URL",
                    onClick = onSaveUrl,
                    modifier = Modifier.weight(1f)
                )

                BuanSecondaryButton(
                    text = if (isPinging) "Testing Ping..." else "Ping Server Gateway",
                    onClick = onTestPing,
                    modifier = Modifier.weight(1.3f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            BuanSecondaryButton(
                text = "Launch AI Studio Web Dashboard ↗",
                onClick = {
                    try {
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(backendApiUrl))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        android.widget.Toast.makeText(context, "Could not open browser", android.widget.Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            if (pingStatus != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = Color(0x2210B981),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0x4410B981)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                        Text(
                            text = pingStatus,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF10B981)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = BuanBorder, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Auto-stream App Activities to Web Admin",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Real-time webhook push for every booking, payment, and KYC event",
                        fontSize = 10.5.sp,
                        color = TextMuted
                    )
                }
                Switch(
                    checked = isBackendSyncEnabled,
                    onCheckedChange = onToggleSync,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = BuanBlueLight
                    )
                )
            }
        }

        // 3. Live "Fetch Website Account" Testing Simulator
        BuanCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = BuanBlueLight, modifier = Modifier.size(20.dp))
                Text(
                    text = "Live Website Account Fetch Simulator",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Test how the mobile app queries your website database. If a customer already registered on your website enters their email in the app, the app fetches their profile, halts duplicate registration, and opens the Login screen with their email pre-filled.",
                fontSize = 11.5.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Tap a sample website user to test:",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val samples = listOf(
                    "eze100@gmail.com",
                    "babiestouchsupport@gmail.com",
                    "chidinma@okaforfabrics.ng",
                    "musa.danjuma@danjumagroup.com",
                    "s.olatunji@medixcare.org",
                    "babajide@buanlogistics.com"
                )
                items(samples) { email ->
                    val isSelected = testEmailInput == email
                    Surface(
                        color = if (isSelected) Color(0x332563EB) else BuanSurfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, if (isSelected) BuanBlueLight else BuanBorder),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onTestEmailChange(email) }
                    ) {
                        Text(
                            text = email,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) BuanBlueLight else TextSecondary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = testEmailInput,
                onValueChange = onTestEmailChange,
                label = { Text("Customer Email on Website", fontSize = 11.5.sp) },
                placeholder = { Text("customer@domain.com", fontSize = 11.5.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = BuanSurfaceVariant,
                    unfocusedContainerColor = BuanSurfaceVariant,
                    focusedBorderColor = BuanBluePrimary,
                    unfocusedBorderColor = BuanBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            BuanButton(
                text = if (isCheckingAccount) "Querying Website Database..." else "Fetch Account from Central DB",
                onClick = onFetchAccount,
                modifier = Modifier.fillMaxWidth()
            )

            // Result Display
            if (fetchedAccountResult != null) {
                val user = fetchedAccountResult
                val tier = SubscriptionTier.fromId(user.subscriptionTier)

                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = Color(0x1510B981),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0x4410B981)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                                Text(
                                    text = "Account Found in Central DB",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF10B981)
                                )
                            }

                            Surface(
                                color = Color(0x330891B2),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = tier.displayName,
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0891B2),
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(user.fullName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("${user.email} • ${user.phone}", fontSize = 11.5.sp, color = TextSecondary)
                        Text("${user.accountType} • ${user.businessName.ifBlank { "Personal" }} • ${user.city}", fontSize = 11.sp, color = TextMuted)

                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = BuanSurface,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "App Action: When ${user.fullName} opens the app and enters ${user.email} on the Registration screen, the app immediately intercepts, displays the 'Account Already Exists' dialog, and transfers them to the Login screen with their email pre-filled.",
                                fontSize = 11.sp,
                                color = BuanBlueLight,
                                modifier = Modifier.padding(8.dp),
                                lineHeight = 15.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        BuanSecondaryButton(
                            text = "Preview Customer Dialog ➔",
                            onClick = onPreviewDialog,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // 4. REST API Technical Blueprint (for your Backend Web Admin Panel)
        BuanCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Code, contentDescription = null, tint = Color(0xFFA78BFA), modifier = Modifier.size(20.dp))
                Text(
                    text = "Web Admin REST Endpoints Contract",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Have your backend developer implement these 4 simple endpoints in your website backend (PHP/Laravel, Node.js, Python, or Firebase) to achieve 100% unified management:",
                fontSize = 11.5.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Endpoint 1
            EndpointBlueprintCard(
                method = "GET",
                path = "/api/users/check?email={email}",
                title = "Check & Fetch Existing Website Account",
                description = "Called by the app during registration. If the email exists in your website DB, return { \"exists\": true, \"user\": { ... } } so the app redirects to Login."
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Endpoint 2
            EndpointBlueprintCard(
                method = "POST",
                path = "/api/auth/login",
                title = "Unified Authentication",
                description = "Authenticates website credentials on the mobile app. Returns JWT session token and synchronized user profile."
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Endpoint 3
            EndpointBlueprintCard(
                method = "POST",
                path = "/api/activities",
                title = "Stream Mobile App Activities to Admin Panel",
                description = "Mobile app sends real-time events (shipments, quote inquiries, payments) with source: 'MOBILE_APP' directly into your web admin panel."
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Endpoint 4
            EndpointBlueprintCard(
                method = "GET/POST",
                path = "/api/shipments",
                title = "Shared Freight Bookings",
                description = "Two-way synchronization so consignments booked on the website or mobile app appear in the same dashboard."
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun EndpointBlueprintCard(
    method: String,
    path: String,
    title: String,
    description: String
) {
    Surface(
        color = BuanSurfaceVariant,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(0.5.dp, BuanBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    color = if (method.startsWith("GET")) Color(0x3310B981) else Color(0x332563EB),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = method,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (method.startsWith("GET")) Color(0xFF10B981) else BuanBlueLight,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = path,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(title, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFA78BFA))
            Spacer(modifier = Modifier.height(2.dp))
            Text(description, fontSize = 10.5.sp, color = TextMuted, lineHeight = 14.5.sp)
        }
    }
}
