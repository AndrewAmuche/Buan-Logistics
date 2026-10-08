package com.example.ui.screens.hubs

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.HubApplicationEntity
import com.example.data.local.HubEntity
import com.example.ui.components.BuanButton
import com.example.ui.components.BuanCard
import com.example.ui.components.BuanSecondaryButton
import com.example.ui.components.BuanTextField
import com.example.ui.components.BuanTopBar
import com.example.ui.components.EmptyStateView
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
fun FindHubScreen(viewModel: BuanViewModel) {
    BackHandler { viewModel.navigateBack() }

    val allHubs by viewModel.allHubs.collectAsState()
    val searchQuery by viewModel.hubSearchQuery.collectAsState()
    val context = LocalContext.current

    val filteredHubs = allHubs.filter { hub ->
        searchQuery.isBlank() ||
                hub.name.contains(searchQuery, ignoreCase = true) ||
                hub.city.contains(searchQuery, ignoreCase = true) ||
                hub.country.contains(searchQuery, ignoreCase = true) ||
                hub.servicesAvailable.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BuanBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            BuanTopBar(
                title = "Find a BUAN Hub",
                onBackClick = { viewModel.navigateBack() }
            )
        }

        item {
            Column {
                Text("Locate Service Drop-off & Intake Hubs", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("Consignment reception, packaging, customs, and collection centers", fontSize = 12.sp, color = TextSecondary)
            }
        }

        // Search Bar
        item {
            BuanTextField(
                value = searchQuery,
                onValueChange = { viewModel.setHubSearch(it) },
                label = "Search by City or Country",
                placeholder = "e.g. Lagos, Abuja, London, Dubai",
                leadingIcon = Icons.Default.Search,
                testTag = "hub_search_input"
            )
        }

        // Become a Hub Provider Banner
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
                    .clickable { viewModel.navigateTo(Screen.BecomeHubProvider) },
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
                        Icon(Icons.Default.Storefront, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Own a retail store or warehouse?", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Become a BUAN Hub Provider & earn BUAN-COIN rewards.", fontSize = 11.sp, color = BuanBlueLight)
                    }
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = BuanBlueLight, modifier = Modifier.size(18.dp))
                }
            }
        }

        if (filteredHubs.isEmpty()) {
            item {
                EmptyStateView(
                    title = "No hubs found",
                    description = "No BUAN Hub locations found matching '$searchQuery'. Try Lagos, London, or Dubai.",
                    icon = Icons.Default.LocationOn
                )
            }
        } else {
            items(filteredHubs) { hub ->
                HubItemCard(
                    hub = hub,
                    onGetDirections = {
                        val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode("${hub.name}, ${hub.address}, ${hub.city}")}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        try {
                            context.startActivity(mapIntent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Directions to ${hub.name}", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(72.dp)) }
    }
}

@Composable
private fun HubItemCard(
    hub: HubEntity,
    onGetDirections: () -> Unit
) {
    BuanCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(hub.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("${hub.city}, ${hub.country}", fontSize = 12.sp, color = BuanBlueLight)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF10B981).copy(alpha = 0.15f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(hub.status, fontSize = 10.sp, color = Color(0xFF10B981), fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(hub.address, fontSize = 12.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(color = BuanBorder, thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Hours: ", fontSize = 11.sp, color = TextMuted)
            Text(hub.openingHours, fontSize = 11.sp, color = TextPrimary)
        }

        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Services: ", fontSize = 11.sp, color = TextMuted)
            Text(hub.servicesAvailable, fontSize = 11.sp, color = TextSecondary)
        }

        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Contact: ", fontSize = 11.sp, color = TextMuted)
            Text("${hub.phone} • ${hub.email}", fontSize = 11.sp, color = TextSecondary)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BuanSecondaryButton(
                text = "Get Directions",
                onClick = onGetDirections,
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.Directions
            )
        }
    }
}

/**
 * Screen 16: Become a Hub Provider
 */
@Composable
fun BecomeHubProviderScreen(viewModel: BuanViewModel) {
    BackHandler { viewModel.navigateBack() }

    var businessName by remember { mutableStateOf("") }
    var ownerName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var businessAddress by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Lagos") }
    var state by remember { mutableStateOf("Lagos State") }
    var country by remember { mutableStateOf("Nigeria") }
    var businessType by remember { mutableStateOf("Retail Logistics & Packaging") }
    var operatingHours by remember { mutableStateOf("Mon - Sat: 08:00 AM - 07:00 PM") }

    var isSubmitting by remember { mutableStateOf(false) }
    var showReviewModal by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BuanBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            BuanTopBar(
                title = "Become a Hub Provider",
                onBackClick = { viewModel.navigateBack() }
            )
        }

        // Hero value proposition card
        item {
            BuanCard(
                glowEffect = true,
                backgroundColor = BuanSurface
            ) {
                Text(
                    text = "Turn your existing business location into a BUAN Hub.",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    lineHeight = 23.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Join our international partner network. Utilize your shop or commercial space as an authorized BUAN intake, parcel drop, and collection facility.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))
                Text("What Hub Providers Can Do:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BuanBlueLight)
                Spacer(modifier = Modifier.height(6.dp))

                val benefits = listOf(
                    "Register customer shipments directly on the portal",
                    "Receive and safely store outbound packages",
                    "Hand over delivered packages to local recipients",
                    "Help customers access the complete suite of BUAN services",
                    "Generate official BUAN tracking numbers instantly",
                    "Earn 1 BUAN-COIN for every registered shipment intake"
                )

                benefits.forEach { b ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BuanBlueCta, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(b, fontSize = 12.sp, color = TextPrimary)
                    }
                }
            }
        }

        // Registration Form
        item {
            BuanCard(modifier = Modifier.fillMaxWidth()) {
                Text("Provider Registration Form", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(12.dp))

                BuanTextField(
                    value = businessName,
                    onValueChange = { businessName = it },
                    label = "Business Name",
                    placeholder = "e.g. Apex Express Retail Centre"
                )
                Spacer(modifier = Modifier.height(10.dp))

                BuanTextField(
                    value = ownerName,
                    onValueChange = { ownerName = it },
                    label = "Owner / Manager Full Name",
                    placeholder = "e.g. Chinedu Okafor"
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BuanTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "Phone Number",
                        placeholder = "+234 802 333 4455",
                        modifier = Modifier.weight(1f)
                    )
                    BuanTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        placeholder = "hub@apexretail.ng",
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                BuanTextField(
                    value = businessAddress,
                    onValueChange = { businessAddress = it },
                    label = "Business Physical Address",
                    placeholder = "e.g. 24 Allen Avenue, Ikeja"
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BuanTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = "City",
                        modifier = Modifier.weight(1f)
                    )
                    BuanTextField(
                        value = state,
                        onValueChange = { state = it },
                        label = "State",
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                BuanTextField(
                    value = country,
                    onValueChange = { country = it },
                    label = "Country"
                )
                Spacer(modifier = Modifier.height(10.dp))

                BuanTextField(
                    value = businessType,
                    onValueChange = { businessType = it },
                    label = "Business Type",
                    placeholder = "e.g. Pharmacy, Supermarket, Stationery Store, Logistics"
                )
                Spacer(modifier = Modifier.height(10.dp))

                BuanTextField(
                    value = operatingHours,
                    onValueChange = { operatingHours = it },
                    label = "Operating Hours",
                    placeholder = "Mon - Sat: 08:00 AM - 07:00 PM"
                )
            }
        }

        // Submit Button
        item {
            BuanButton(
                text = "Submit Hub Application",
                onClick = {
                    val app = HubApplicationEntity(
                        businessName = businessName.ifBlank { "Sample Retail Hub" },
                        ownerName = ownerName.ifBlank { "Business Owner" },
                        phone = phone.ifBlank { "+234 800 000 0000" },
                        email = email.ifBlank { "partner@hub.com" },
                        businessAddress = businessAddress.ifBlank { "Plot 1 Commercial Street" },
                        city = city,
                        state = state,
                        country = country,
                        businessType = businessType,
                        operatingHours = operatingHours,
                        status = "Pending Review",
                        submittedAt = ""
                    )
                    viewModel.submitHubApplication(app) {
                        showReviewModal = true
                    }
                },
                isLoading = isSubmitting,
                modifier = Modifier.fillMaxWidth(),
                testTag = "submit_hub_application_button"
            )
        }

        item { Spacer(modifier = Modifier.height(72.dp)) }
    }

    if (showReviewModal) {
        AlertDialog(
            onDismissRequest = {
                showReviewModal = false
                viewModel.navigateTo(Screen.Home)
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BuanBlueSubtle),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = BuanBlueLight, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Application Under Review", color = TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(
                    text = "Thank you for your interest in becoming a BUAN Hub Provider. Your application is currently under review. You will be contacted via email once your application has been reviewed and approved.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            },
            confirmButton = {
                BuanButton(
                    text = "Back to Dashboard",
                    onClick = {
                        showReviewModal = false
                        viewModel.navigateTo(Screen.Home)
                    }
                )
            },
            containerColor = BuanSurface
        )
    }
}
