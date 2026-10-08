package com.example.ui.screens.tracking

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.model.ShipmentStatus
import com.example.ui.components.BuanButton
import com.example.ui.components.BuanCard
import com.example.ui.components.BuanSecondaryButton
import com.example.ui.components.BuanTopBar
import com.example.ui.components.DarkTrackingMapVisualizer
import com.example.ui.components.ShipmentTimelineView
import com.example.ui.components.StatusBadge
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
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BuanViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun ShipmentDetailScreen(
    trackingNumber: String,
    viewModel: BuanViewModel
) {
    BackHandler { viewModel.navigateBack() }

    val allShipments by viewModel.allShipments.collectAsState()
    val shipment = allShipments.firstOrNull { it.trackingNumber.equals(trackingNumber, ignoreCase = true) }
    val context = LocalContext.current
    var showAdvanceDialog by remember { mutableStateOf(false) }
    var isFetchingCloud by remember { mutableStateOf(shipment == null) }

    LaunchedEffect(trackingNumber) {
        if (shipment == null) {
            isFetchingCloud = true
            viewModel.findShipmentSync(trackingNumber)
            isFetchingCloud = false
        }
    }

    if (shipment == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BuanBackground)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isFetchingCloud) {
                CircularProgressIndicator(
                    color = BuanBlueLight,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Searching Cloud Database for $trackingNumber...", color = TextSecondary, fontSize = 14.sp)
            } else {
                Text("Shipment $trackingNumber not found", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Check that the tracking number matches what was generated on your web portal.", color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    BuanButton(text = "Retry Cloud Fetch", onClick = {
                        isFetchingCloud = true
                        viewModel.refreshShipmentFromCloud(trackingNumber) {
                            isFetchingCloud = false
                        }
                    })
                    BuanSecondaryButton(text = "Go to History", onClick = { viewModel.navigateTo(Screen.Shipments) })
                }
            }
        }
        return
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
                title = "Shipment Details",
                onBackClick = { viewModel.navigateBack() }
            )
        }

        // Header Tracking Banner
        item {
            BuanCard(
                glowEffect = true,
                backgroundColor = BuanSurface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("TRACKING NUMBER", fontSize = 10.sp, color = TextMuted, letterSpacing = 1.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = shipment.trackingNumber,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = BuanBlueLight
                            )
                            IconButton(
                                onClick = {
                                    val cb = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    cb.setPrimaryClip(ClipData.newPlainText("Tracking Number", shipment.trackingNumber))
                                    Toast.makeText(context, "Copied tracking number", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                    StatusBadge(status = shipment.status)
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = BuanBorder, thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                // Mode, Date, Cost Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DetailInfoColumn(label = "Transport Mode", value = shipment.transportMode)
                    DetailInfoColumn(label = "Booked Date", value = shipment.shipmentDate)
                    DetailInfoColumn(label = "Est. Delivery", value = shipment.estimatedDelivery)
                }

                Spacer(modifier = Modifier.height(16.dp))

                BuanButton(
                    text = "View Live Radar Map",
                    onClick = { viewModel.navigateTo(Screen.LiveTracking(shipment.trackingNumber)) },
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.Radar
                )
            }
        }

        // Origin and Destination Card
        item {
            BuanCard(modifier = Modifier.fillMaxWidth()) {
                Text("Route Information", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF10B981)))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("ORIGIN", fontSize = 10.sp, color = TextMuted)
                        Text(shipment.origin, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Sender: ${shipment.senderName} (${shipment.senderPhone})", fontSize = 12.sp, color = TextSecondary)
                        Text(shipment.senderAddress, fontSize = 11.sp, color = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("DESTINATION", fontSize = 10.sp, color = TextMuted)
                        Text(shipment.destination, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Receiver: ${shipment.receiverName} (${shipment.receiverPhone})", fontSize = 12.sp, color = TextSecondary)
                        Text(shipment.receiverAddress, fontSize = 11.sp, color = TextMuted)
                    }
                }
            }
        }

        // Package Metrics
        item {
            BuanCard(modifier = Modifier.fillMaxWidth()) {
                Text("Package & Consignment Information", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DetailInfoColumn(label = "Cargo Type", value = shipment.shipmentType)
                    DetailInfoColumn(label = "Total Weight", value = "${shipment.weightKg} kg")
                    DetailInfoColumn(label = "Quantity", value = "${shipment.quantity} units")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DetailInfoColumn(label = "Dimensions", value = "${shipment.lengthCm}×${shipment.widthCm}×${shipment.heightCm} cm")
                    DetailInfoColumn(label = "Declared Value", value = "$${"%.2f".format(shipment.declaredValueUsd)}")
                    DetailInfoColumn(label = "Freight Fee", value = "$${"%.2f".format(shipment.estimatedCostUsd)}")
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text("Description: ${shipment.description}", fontSize = 12.sp, color = TextSecondary)
            }
        }

        // 8-Stage Visual Timeline
        item {
            BuanCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Tracking Milestones", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    // Simulator button for testing
                    TextButton(onClick = { showAdvanceDialog = true }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.FastForward, contentDescription = null, tint = BuanBlueLight, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Simulate", color = BuanBlueLight, fontSize = 12.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                ShipmentTimelineView(currentStatus = shipment.status)
            }
        }

        item { Spacer(modifier = Modifier.height(72.dp)) }
    }

    if (showAdvanceDialog) {
        AlertDialog(
            onDismissRequest = { showAdvanceDialog = false },
            title = { Text("Simulate Status Milestone", color = TextPrimary) },
            text = {
                Column {
                    Text("Select a status to test shipment milestone updates:", color = TextSecondary, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    listOf(
                        ShipmentStatus.PROCESSING,
                        ShipmentStatus.IN_TRANSIT,
                        ShipmentStatus.AT_DESTINATION,
                        ShipmentStatus.OUT_FOR_DELIVERY,
                        ShipmentStatus.DELIVERED
                    ).forEach { nextSt ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .shadow(
                                    elevation = 2.dp,
                                    shape = RoundedCornerShape(8.dp),
                                    ambientColor = Color(0x22000000),
                                    spotColor = Color(0x22000000)
                                )
                                .clip(RoundedCornerShape(8.dp)),
                            color = BuanSurfaceVariant,
                            onClick = {
                                viewModel.advanceShipmentStatus(
                                    trackingNumber = shipment.trackingNumber,
                                    nextStatus = nextSt,
                                    location = when (nextSt) {
                                        ShipmentStatus.PROCESSING -> "Airport Customs Gateway"
                                        ShipmentStatus.IN_TRANSIT -> "En Route International Transit"
                                        ShipmentStatus.AT_DESTINATION -> "${shipment.receiverCity} Terminal Hub"
                                        ShipmentStatus.OUT_FOR_DELIVERY -> "Dispatched to Courier Van #42"
                                        ShipmentStatus.DELIVERED -> "Delivered to Consignee"
                                        else -> "BUAN Hub"
                                    }
                                )
                                showAdvanceDialog = false
                            }
                        ) {
                            Text(
                                text = nextSt.displayName,
                                modifier = Modifier.padding(12.dp),
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showAdvanceDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = BuanSurface
        )
    }
}

/**
 * Screen 11: Live Tracking with Dark Map Area
 */
@Composable
fun LiveTrackingScreen(
    trackingNumber: String,
    viewModel: BuanViewModel
) {
    BackHandler { viewModel.navigateBack() }

    val allShipments by viewModel.allShipments.collectAsState()
    val shipment = allShipments.firstOrNull { it.trackingNumber.equals(trackingNumber, ignoreCase = true) }
    val context = LocalContext.current
    var isLiveConsignmentActive by remember { mutableStateOf(false) }
    var isFetchingCloud by remember { mutableStateOf(shipment == null) }

    LaunchedEffect(trackingNumber) {
        if (shipment == null) {
            isFetchingCloud = true
            viewModel.findShipmentSync(trackingNumber)
            isFetchingCloud = false
        }
    }

    if (shipment == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BuanBackground)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isFetchingCloud) {
                CircularProgressIndicator(
                    color = BuanBlueLight,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Connecting to Cloud Radar for $trackingNumber...", color = TextSecondary, fontSize = 14.sp)
            } else {
                Text("Shipment $trackingNumber not found", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("The consignment might still be generating or was created under a different ID.", color = TextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    BuanButton(text = "Retry Cloud Radar", onClick = {
                        isFetchingCloud = true
                        viewModel.refreshShipmentFromCloud(trackingNumber) {
                            isFetchingCloud = false
                        }
                    })
                    BuanSecondaryButton(text = "Go to Home", onClick = { viewModel.navigateTo(Screen.Home) })
                }
            }
        }
        return
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
                title = "Live Radar Tracking",
                onBackClick = { viewModel.navigateBack() }
            )
        }

        // Tracking ID pill
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("CONSIGNMENT CODE", fontSize = 10.sp, color = TextMuted, letterSpacing = 1.sp)
                    Text(shipment.trackingNumber, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BuanBlueLight)
                }
                StatusBadge(status = shipment.status)
            }
        }

        // Dark Map Area
        item {
            DarkTrackingMapVisualizer(
                origin = shipment.origin.split(",").firstOrNull() ?: "Origin",
                currentLocation = shipment.currentLocation,
                destination = shipment.destination.split(",").firstOrNull() ?: "Destination",
                transportMode = shipment.transportMode
            )
        }

        // Telemetry details below the map
        item {
            BuanCard(modifier = Modifier.fillMaxWidth()) {
                Text("Live Consignment Telemetry", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(12.dp))

                TelemetryRow(label = "Current Status", value = shipment.status, isHighlight = true)
                TelemetryRow(label = "Current Location", value = shipment.currentLocation)
                TelemetryRow(label = "Last Updated", value = shipment.lastUpdated)
                TelemetryRow(label = "Estimated Delivery", value = shipment.estimatedDelivery)
                TelemetryRow(label = "Transport Channel", value = shipment.transportMode)
            }
        }

        // Dispatcher / Hub contact section
        item {
            BuanCard(modifier = Modifier.fillMaxWidth()) {
                Text("Assigned Logistics Unit", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .shadow(
                                elevation = 3.dp,
                                shape = CircleShape,
                                ambientColor = Color(0x33000000),
                                spotColor = Color(0x33000000)
                            )
                            .clip(CircleShape)
                            .background(BuanSurfaceElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null, tint = BuanBlueLight, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("BUAN Courier Dispatcher #714", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Murtala Muhammed Cargo Transit Facility", fontSize = 11.sp, color = TextSecondary)
                    }
                    IconButton(
                        onClick = { Toast.makeText(context, "Directing to BUAN Hub Support hotline...", Toast.LENGTH_SHORT).show() },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BuanBlueSubtle)
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = "Call", tint = BuanBlueLight, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        // Button: Track Live Consignment
        item {
            BuanButton(
                text = if (isLiveConsignmentActive) "Radar Active: Telemetry Synchronized" else "Track Live Consignment",
                onClick = {
                    isLiveConsignmentActive = true
                    Toast.makeText(context, "Radar telemetry updated. Waypoint confirmed.", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.Radar,
                testTag = "track_live_consignment_button"
            )
        }

        item {
            BuanSecondaryButton(
                text = "View Detailed Milestones",
                onClick = { viewModel.navigateTo(Screen.ShipmentDetail(shipment.trackingNumber)) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item { Spacer(modifier = Modifier.height(72.dp)) }
    }
}

@Composable
private fun DetailInfoColumn(label: String, value: String) {
    Column {
        Text(text = label, fontSize = 11.sp, color = TextMuted)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
    }
}

@Composable
private fun TelemetryRow(label: String, value: String, isHighlight: Boolean = false) {
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
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium,
            color = if (isHighlight) BuanBlueLight else TextPrimary
        )
    }
    HorizontalDivider(color = BuanBorder, thickness = 0.5.dp)
}
