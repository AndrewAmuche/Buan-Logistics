package com.example.ui.screens.shipments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppCurrency
import com.example.model.ShipmentType
import com.example.model.TransportMode
import com.example.ui.components.BuanButton
import com.example.ui.components.BuanCard
import com.example.ui.components.BuanSecondaryButton
import com.example.ui.components.BuanTextField
import com.example.ui.components.BuanTopBar
import com.example.ui.components.EmptyStateView
import com.example.ui.components.RecentShipmentCard
import com.example.ui.theme.BuanBackground
import com.example.ui.theme.BuanBlueCta
import com.example.ui.theme.BuanBlueLight
import com.example.ui.theme.BuanBluePrimary
import com.example.ui.theme.BuanBlueSubtle
import com.example.ui.theme.BuanBorder
import com.example.ui.theme.BuanBorderLight
import com.example.ui.theme.BuanSurface
import com.example.ui.theme.BuanSurfaceElevated
import com.example.ui.theme.BuanSurfaceVariant
import com.example.ui.theme.StatusDelivered
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BuanViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun ShipmentHistoryScreen(viewModel: BuanViewModel) {
    BackHandler { viewModel.navigateBack() }

    val allShipments by viewModel.allShipments.collectAsState()
    val activeFilter by viewModel.shipmentFilter.collectAsState()
    val searchQuery by viewModel.shipmentSearchQuery.collectAsState()

    val filteredList = allShipments.filter { shipment ->
        val matchesFilter = when (activeFilter) {
            "Active" -> !shipment.status.contains("Deliver", ignoreCase = true) && !shipment.status.contains("Cancel", ignoreCase = true)
            "Delivered" -> shipment.status.contains("Deliver", ignoreCase = true)
            "Cancelled" -> shipment.status.contains("Cancel", ignoreCase = true)
            else -> true
        }
        val matchesSearch = searchQuery.isBlank() ||
                shipment.trackingNumber.contains(searchQuery, ignoreCase = true) ||
                shipment.origin.contains(searchQuery, ignoreCase = true) ||
                shipment.destination.contains(searchQuery, ignoreCase = true) ||
                shipment.description.contains(searchQuery, ignoreCase = true)

        matchesFilter && matchesSearch
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BuanBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BuanTopBar(
                    title = "Shipment History",
                    onBackClick = { viewModel.navigateBack() }
                )
            }
        }

        // Cloud Database Sync Button & Status Banner
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { viewModel.syncAllCloudShipments() },
                color = BuanSurfaceElevated
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sync Cloud Consignments",
                            tint = BuanBlueLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sync with Web Portal",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }
                    Text(
                        text = "Tap to refresh cloud bookings",
                        fontSize = 11.sp,
                        color = BuanBlueLight
                    )
                }
            }
        }

        // Search Bar
        item {
            BuanTextField(
                value = searchQuery,
                onValueChange = { viewModel.setShipmentSearch(it) },
                label = "Search Shipments",
                placeholder = "Tracking number, city, or cargo description",
                leadingIcon = Icons.Default.Search,
                testTag = "shipment_search_input"
            )
        }

        // Filter Chips Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Active", "Delivered", "Cancelled").forEach { filter ->
                    val isSelected = activeFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setShipmentFilter(filter) },
                        label = { Text(filter, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = BuanSurface,
                            selectedContainerColor = BuanBlueCta,
                            labelColor = TextSecondary,
                            selectedLabelColor = Color.White
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = BuanBorder,
                            selectedBorderColor = BuanBlueLight
                        )
                    )
                }
            }
        }

        if (filteredList.isEmpty()) {
            item {
                EmptyStateView(
                    title = "No shipments found",
                    description = if (searchQuery.isNotBlank()) "No shipment records matching '$searchQuery'." else "You have no $activeFilter shipments recorded.",
                    icon = Icons.Default.LocalShipping,
                    buttonText = "Request a Quote",
                    onButtonClick = {
                        viewModel.navigateTo(Screen.RequestQuote)
                    }
                )
            }
        } else {
            items(filteredList) { shipment ->
                RecentShipmentCard(
                    shipment = shipment,
                    onClick = { viewModel.navigateTo(Screen.ShipmentDetail(shipment.trackingNumber)) },
                    onTrackLive = { viewModel.navigateTo(Screen.LiveTracking(shipment.trackingNumber)) }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(72.dp)) }
    }
}

/**
 * 7-Step Multi-Stage Shipment Booking Wizard
 */
@Composable
fun CreateShipmentScreen(viewModel: BuanViewModel) {
    BackHandler {
        val currentStep = viewModel.createDraft.value.step
        if (currentStep > 1) {
            viewModel.updateDraft { copy(step = currentStep - 1) }
        } else {
            viewModel.navigateBack()
        }
    }

    val draft by viewModel.createDraft.collectAsState()
    val isSubmitting by viewModel.isSubmitting.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BuanBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            BuanTopBar(
                title = "Create Shipment",
                onBackClick = {
                    if (draft.step > 1) {
                        viewModel.updateDraft { copy(step = draft.step - 1) }
                    } else {
                        viewModel.navigateBack()
                    }
                }
            )
        }

        // Stepper Progress Header
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Step ${draft.step} of 7",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = BuanBlueLight
                    )
                    Text(
                        text = when (draft.step) {
                            1 -> "Sender Details"
                            2 -> "Receiver Details"
                            3 -> "Shipment Type"
                            4 -> "Transport Mode"
                            5 -> "Package Specs"
                            6 -> "Pickup & Delivery"
                            else -> "Review & Confirm"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { draft.step / 7f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = BuanBlueCta,
                    trackColor = BuanSurfaceVariant,
                )
            }
        }

        // Step Content based on draft.step
        item {
            AnimatedContent(
                targetState = draft.step,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "step_content"
            ) { step ->
                when (step) {
                    1 -> Step1SenderInfo(draft = draft, onUpdate = viewModel::updateDraft)
                    2 -> Step2ReceiverInfo(draft = draft, onUpdate = viewModel::updateDraft)
                    3 -> Step3ShipmentType(draft = draft, onUpdate = viewModel::updateDraft)
                    4 -> Step4TransportMode(draft = draft, onUpdate = viewModel::updateDraft)
                    5 -> Step5PackageInfo(draft = draft, onUpdate = viewModel::updateDraft)
                    6 -> Step6PickupDelivery(draft = draft, onUpdate = viewModel::updateDraft)
                    7 -> Step7Review(draft = draft)
                }
            }
        }

        // Navigation CTA Buttons
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (draft.step > 1) {
                    BuanSecondaryButton(
                        text = "Back",
                        onClick = { viewModel.updateDraft { copy(step = draft.step - 1) } },
                        modifier = Modifier.weight(1f)
                    )
                }

                if (draft.step < 7) {
                    BuanButton(
                        text = "Continue",
                        onClick = { viewModel.updateDraft { copy(step = draft.step + 1) } },
                        modifier = Modifier.weight(if (draft.step > 1) 1.5f else 1f),
                        testTag = "create_shipment_next_step"
                    )
                } else {
                    BuanButton(
                        text = "Create Shipment",
                        onClick = {
                            viewModel.submitCreateShipment { trackingNumber ->
                                viewModel.navigateTo(Screen.ShipmentCreatedSuccess(trackingNumber))
                            }
                        },
                        isLoading = isSubmitting,
                        modifier = Modifier.weight(1.5f),
                        testTag = "create_shipment_submit"
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(72.dp)) }
    }
}

@Composable
private fun Step1SenderInfo(
    draft: com.example.ui.viewmodel.CreateShipmentDraft,
    onUpdate: (com.example.ui.viewmodel.CreateShipmentDraft.() -> com.example.ui.viewmodel.CreateShipmentDraft) -> Unit
) {
    BuanCard(modifier = Modifier.fillMaxWidth()) {
        Text("Sender Information", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Provide complete origin details for consignment collection", fontSize = 12.sp, color = TextSecondary)
        Spacer(modifier = Modifier.height(14.dp))

        BuanTextField(
            value = draft.senderName,
            onValueChange = { onUpdate { copy(senderName = it) } },
            label = "Full Name",
            placeholder = "e.g. Babajide Adeyemi",
            leadingIcon = Icons.Default.Person
        )
        Spacer(modifier = Modifier.height(10.dp))

        BuanTextField(
            value = draft.senderPhone,
            onValueChange = { onUpdate { copy(senderPhone = it) } },
            label = "Phone Number",
            placeholder = "e.g. +234 803 555 0192"
        )
        Spacer(modifier = Modifier.height(10.dp))

        BuanTextField(
            value = draft.senderEmail,
            onValueChange = { onUpdate { copy(senderEmail = it) } },
            label = "Email Address",
            placeholder = "e.g. babajide@buanlogistics.com"
        )
        Spacer(modifier = Modifier.height(10.dp))

        BuanTextField(
            value = draft.senderAddress,
            onValueChange = { onUpdate { copy(senderAddress = it) } },
            label = "Street Address",
            placeholder = "e.g. 14 Marina Boulevard, Victoria Island"
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BuanTextField(
                value = draft.senderCity,
                onValueChange = { onUpdate { copy(senderCity = it) } },
                label = "City",
                modifier = Modifier.weight(1f)
            )
            BuanTextField(
                value = draft.senderState,
                onValueChange = { onUpdate { copy(senderState = it) } },
                label = "State",
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        BuanTextField(
            value = draft.senderCountry,
            onValueChange = { onUpdate { copy(senderCountry = it) } },
            label = "Country",
            placeholder = "Nigeria"
        )
    }
}

@Composable
private fun Step2ReceiverInfo(
    draft: com.example.ui.viewmodel.CreateShipmentDraft,
    onUpdate: (com.example.ui.viewmodel.CreateShipmentDraft.() -> com.example.ui.viewmodel.CreateShipmentDraft) -> Unit
) {
    BuanCard(modifier = Modifier.fillMaxWidth()) {
        Text("Receiver Information", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Enter the destination consignee contact details", fontSize = 12.sp, color = TextSecondary)
        Spacer(modifier = Modifier.height(14.dp))

        BuanTextField(
            value = draft.receiverName,
            onValueChange = { onUpdate { copy(receiverName = it) } },
            label = "Full Name / Company Name",
            placeholder = "e.g. Alexander Wright / Global Imports",
            leadingIcon = Icons.Default.Person
        )
        Spacer(modifier = Modifier.height(10.dp))

        BuanTextField(
            value = draft.receiverPhone,
            onValueChange = { onUpdate { copy(receiverPhone = it) } },
            label = "Phone Number",
            placeholder = "e.g. +44 20 7946 0912"
        )
        Spacer(modifier = Modifier.height(10.dp))

        BuanTextField(
            value = draft.receiverEmail,
            onValueChange = { onUpdate { copy(receiverEmail = it) } },
            label = "Email Address",
            placeholder = "e.g. a.wright@londonlogistics.co.uk"
        )
        Spacer(modifier = Modifier.height(10.dp))

        BuanTextField(
            value = draft.receiverAddress,
            onValueChange = { onUpdate { copy(receiverAddress = it) } },
            label = "Destination Address",
            placeholder = "e.g. 88 Leadenhall Street, City of London"
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BuanTextField(
                value = draft.receiverCity,
                onValueChange = { onUpdate { copy(receiverCity = it) } },
                label = "City",
                modifier = Modifier.weight(1f)
            )
            BuanTextField(
                value = draft.receiverState,
                onValueChange = { onUpdate { copy(receiverState = it) } },
                label = "State / Region",
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        BuanTextField(
            value = draft.receiverCountry,
            onValueChange = { onUpdate { copy(receiverCountry = it) } },
            label = "Country",
            placeholder = "United Kingdom"
        )
    }
}

@Composable
private fun Step3ShipmentType(
    draft: com.example.ui.viewmodel.CreateShipmentDraft,
    onUpdate: (com.example.ui.viewmodel.CreateShipmentDraft.() -> com.example.ui.viewmodel.CreateShipmentDraft) -> Unit
) {
    BuanCard(modifier = Modifier.fillMaxWidth()) {
        Text("Shipment Type", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Select the category that best describes your consignment", fontSize = 12.sp, color = TextSecondary)
        Spacer(modifier = Modifier.height(14.dp))

        ShipmentType.entries.forEach { type ->
            val isSelected = draft.shipmentType == type
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .shadow(
                        elevation = if (isSelected) 4.dp else 1.dp,
                        shape = RoundedCornerShape(12.dp),
                        ambientColor = Color(0x22000000),
                        spotColor = if (isSelected) Color(0x442563EB) else Color(0x22000000)
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onUpdate { copy(shipmentType = type) } },
                color = if (isSelected) BuanBlueSubtle else BuanSurfaceVariant,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onUpdate { copy(shipmentType = type) } },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = BuanBlueCta,
                            unselectedColor = TextMuted
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = type.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) TextPrimary else TextSecondary
                        )
                        Text(
                            text = when (type) {
                                ShipmentType.DOCUMENT -> "Letters, passports, legal contracts, certificates"
                                ShipmentType.PARCEL -> "Boxes, personal packages, consumer electronics"
                                ShipmentType.CARGO -> "Heavy machinery, industrial equipment, palletized goods"
                                ShipmentType.COMMERCIAL_GOODS -> "Wholesale inventory, textiles, retail stock"
                                ShipmentType.PERSONAL_EFFECTS -> "Household relocation goods, baggage"
                                ShipmentType.PERISHABLE_GOODS -> "Food items, agricultural samples, pharmaceuticals"
                            },
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Step4TransportMode(
    draft: com.example.ui.viewmodel.CreateShipmentDraft,
    onUpdate: (com.example.ui.viewmodel.CreateShipmentDraft.() -> com.example.ui.viewmodel.CreateShipmentDraft) -> Unit
) {
    BuanCard(modifier = Modifier.fillMaxWidth()) {
        Text("Transport Mode", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Choose the optimal transit method for speed and budget", fontSize = 12.sp, color = TextSecondary)
        Spacer(modifier = Modifier.height(14.dp))

        TransportMode.entries.forEach { mode ->
            val isSelected = draft.transportMode == mode
            val icon = when (mode) {
                TransportMode.AIR -> Icons.Default.Flight
                TransportMode.SEA -> Icons.Default.DirectionsBoat
                TransportMode.ROAD -> Icons.Default.LocalShipping
                TransportMode.EXPRESS -> Icons.Default.ElectricBolt
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .shadow(
                        elevation = if (isSelected) 4.dp else 1.dp,
                        shape = RoundedCornerShape(14.dp),
                        ambientColor = Color(0x22000000),
                        spotColor = if (isSelected) Color(0x442563EB) else Color(0x22000000)
                    )
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onUpdate { copy(transportMode = mode) } },
                color = if (isSelected) BuanBlueSubtle else BuanSurfaceVariant,
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) BuanBlueCta else BuanSurfaceElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else BuanBlueLight,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = mode.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Estimated Transit: ${mode.transitEstimate}",
                            fontSize = 12.sp,
                            color = BuanBlueLight
                        )
                    }
                    RadioButton(
                        selected = isSelected,
                        onClick = { onUpdate { copy(transportMode = mode) } },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = BuanBlueCta,
                            unselectedColor = TextMuted
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun Step5PackageInfo(
    draft: com.example.ui.viewmodel.CreateShipmentDraft,
    onUpdate: (com.example.ui.viewmodel.CreateShipmentDraft.() -> com.example.ui.viewmodel.CreateShipmentDraft) -> Unit
) {
    BuanCard(modifier = Modifier.fillMaxWidth()) {
        Text("Package Information", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Specify cargo description, weight and dimensional metrics", fontSize = 12.sp, color = TextSecondary)
        Spacer(modifier = Modifier.height(14.dp))

        BuanTextField(
            value = draft.description,
            onValueChange = { onUpdate { copy(description = it) } },
            label = "Cargo Description",
            placeholder = "e.g. Handmade Leather Goods and Fabrics"
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BuanTextField(
                value = draft.quantity.toString(),
                onValueChange = {
                    val q = it.filter { char -> char.isDigit() }.toIntOrNull() ?: 1
                    onUpdate { copy(quantity = q) }
                },
                label = "Quantity",
                modifier = Modifier.weight(1f)
            )
            BuanTextField(
                value = draft.weightKg.toString(),
                onValueChange = {
                    val w = it.toDoubleOrNull() ?: 1.0
                    onUpdate { copy(weightKg = w) }
                },
                label = "Weight (kg)",
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        Text("Dimensions (cm)", fontSize = 12.sp, color = TextSecondary)
        Spacer(modifier = Modifier.height(4.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            BuanTextField(
                value = draft.lengthCm.toString(),
                onValueChange = { onUpdate { copy(lengthCm = it.toDoubleOrNull() ?: 10.0) } },
                label = "Length",
                modifier = Modifier.weight(1f)
            )
            BuanTextField(
                value = draft.widthCm.toString(),
                onValueChange = { onUpdate { copy(widthCm = it.toDoubleOrNull() ?: 10.0) } },
                label = "Width",
                modifier = Modifier.weight(1f)
            )
            BuanTextField(
                value = draft.heightCm.toString(),
                onValueChange = { onUpdate { copy(heightCm = it.toDoubleOrNull() ?: 10.0) } },
                label = "Height",
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        BuanTextField(
            value = draft.declaredValueUsd.toString(),
            onValueChange = { onUpdate { copy(declaredValueUsd = it.toDoubleOrNull() ?: 100.0) } },
            label = "Declared Customs Value (USD)",
            placeholder = "250.00"
        )
    }
}

@Composable
private fun Step6PickupDelivery(
    draft: com.example.ui.viewmodel.CreateShipmentDraft,
    onUpdate: (com.example.ui.viewmodel.CreateShipmentDraft.() -> com.example.ui.viewmodel.CreateShipmentDraft) -> Unit
) {
    BuanCard(modifier = Modifier.fillMaxWidth()) {
        Text("Pickup & Delivery Preferences", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Select how the consignment is collected and dispatched", fontSize = 12.sp, color = TextSecondary)
        Spacer(modifier = Modifier.height(14.dp))

        Text("Pickup Option", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = BuanBlueLight)
        Spacer(modifier = Modifier.height(6.dp))

        listOf("Customer Address", "BUAN Hub").forEach { opt ->
            val isSelected = draft.pickupType == opt
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .shadow(
                        elevation = if (isSelected) 4.dp else 1.dp,
                        shape = RoundedCornerShape(10.dp),
                        ambientColor = Color(0x22000000),
                        spotColor = if (isSelected) Color(0x552563EB) else Color(0x11000000)
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onUpdate { copy(pickupType = opt) } }
                    .background(if (isSelected) BuanBlueSubtle else BuanSurfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = { onUpdate { copy(pickupType = opt) } },
                    colors = RadioButtonDefaults.colors(selectedColor = BuanBlueCta)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = opt,
                    color = if (isSelected) TextPrimary else TextSecondary,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Delivery Option", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = BuanBlueLight)
        Spacer(modifier = Modifier.height(6.dp))

        listOf("Recipient Address", "BUAN Hub").forEach { opt ->
            val isSelected = draft.deliveryType == opt
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .shadow(
                        elevation = if (isSelected) 4.dp else 1.dp,
                        shape = RoundedCornerShape(10.dp),
                        ambientColor = Color(0x22000000),
                        spotColor = if (isSelected) Color(0x552563EB) else Color(0x11000000)
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onUpdate { copy(deliveryType = opt) } }
                    .background(if (isSelected) BuanBlueSubtle else BuanSurfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = { onUpdate { copy(deliveryType = opt) } },
                    colors = RadioButtonDefaults.colors(selectedColor = BuanBlueCta)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = opt,
                    color = if (isSelected) TextPrimary else TextSecondary,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun Step7Review(draft: com.example.ui.viewmodel.CreateShipmentDraft) {
    val cost = draft.calculateEstimatedCost()

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        BuanCard(modifier = Modifier.fillMaxWidth()) {
            Text("Review Shipment Summary", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("Please confirm all details prior to system consignment creation", fontSize = 12.sp, color = TextSecondary)
            Spacer(modifier = Modifier.height(14.dp))

            SummaryItem(label = "Sender", value = "${draft.senderName} (${draft.senderCity}, ${draft.senderCountry})")
            SummaryItem(label = "Receiver", value = "${draft.receiverName} (${draft.receiverCity}, ${draft.receiverCountry})")
            SummaryItem(label = "Shipment Type", value = draft.shipmentType.title)
            SummaryItem(label = "Transport Mode", value = draft.transportMode.title)
            SummaryItem(label = "Cargo Description", value = draft.description.ifBlank { "General Merchandise" })
            SummaryItem(label = "Quantity / Weight", value = "${draft.quantity} pkgs / ${draft.weightKg} kg")
            SummaryItem(label = "Dimensions (L×W×H)", value = "${draft.lengthCm} × ${draft.widthCm} × ${draft.heightCm} cm")
            SummaryItem(label = "Pickup Option", value = draft.pickupType)
            SummaryItem(label = "Delivery Option", value = draft.deliveryType)
        }

        BuanCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = BuanSurfaceElevated,
            glowEffect = true
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total Estimated Freight", fontSize = 12.sp, color = TextSecondary)
                    val currency = AppCurrency.fromCountry(draft.senderCountry)
                    Text(
                        text = currency.formatWithCode(cost),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(BuanBlueSubtle)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("+1 Referral Credit", fontSize = 11.sp, color = BuanBlueLight, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun SummaryItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = TextMuted)
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            textAlign = TextAlign.End,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
    HorizontalDivider(color = BuanBorder, thickness = 0.5.dp)
}

/**
 * Screen 9: Tracking Number Success Screen
 */
@Composable
fun ShipmentCreatedSuccessScreen(
    trackingNumber: String,
    viewModel: BuanViewModel
) {
    BackHandler { viewModel.navigateTo(Screen.Home) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BuanBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = CircleShape,
                    ambientColor = Color(0x33000000),
                    spotColor = Color(0x662563EB)
                )
                .clip(CircleShape)
                .background(BuanBlueSubtle),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Success",
                tint = BuanBlueLight,
                modifier = Modifier.size(42.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Shipment Created Successfully",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your consignment has been recorded into the BUAN Logistics global system and assigned an official tracking code.",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tracking Number Card with Copy
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = Color(0x22000000),
                    spotColor = Color(0x442563EB)
                ),
            color = BuanSurfaceVariant,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "OFFICIAL TRACKING NUMBER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = trackingNumber,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = BuanBlueLight,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Tracking Number", trackingNumber))
                            Toast.makeText(context, "Tracking number copied to clipboard", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Referral Cashback & Free Shipping Reward Banner
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(12.dp),
                    ambientColor = Color(0x33000000),
                    spotColor = Color(0x442563EB)
                )
                .clip(RoundedCornerShape(12.dp)),
            color = BuanBlueSubtle,
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎁", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Earn 100% Free Shipping & Cashback", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text("Share your unique coupon code with 7 friends to unlock free shipping!", fontSize = 11.sp, color = TextSecondary)
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        BuanButton(
            text = "Track Shipment",
            onClick = { viewModel.navigateTo(Screen.LiveTracking(trackingNumber)) },
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Default.Radar,
            testTag = "success_track_shipment_button"
        )

        Spacer(modifier = Modifier.height(12.dp))

        BuanSecondaryButton(
            text = "Back to Dashboard",
            onClick = { viewModel.navigateTo(Screen.Home) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
