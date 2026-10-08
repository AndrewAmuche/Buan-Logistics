package com.example.ui.screens.services

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
import androidx.compose.material.icons.filled.Anchor
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BuanService
import com.example.model.BuanServicesCatalog
import com.example.ui.components.BuanButton
import com.example.ui.components.BuanCard
import com.example.ui.components.BuanTopBar
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(viewModel: BuanViewModel) {
    BackHandler { viewModel.navigateBack() }

    var selectedCategory by remember { mutableStateOf("All") }
    var selectedService by remember { mutableStateOf<BuanService?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val categories = listOf("All", "Freight Solutions", "Maritime Solutions", "Express & Courier", "Supply Chain", "Trade & Compliance")

    val filteredServices = if (selectedCategory == "All") {
        BuanServicesCatalog.allServices
    } else {
        BuanServicesCatalog.allServices.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BuanBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            BuanTopBar(
                title = "Logistics Services",
                onBackClick = { viewModel.navigateBack() }
            )
        }

        item {
            Column {
                Text("End-to-End Maritime & Cargo Solutions", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("Connecting Nigeria, West Africa, and global trade corridors", fontSize = 12.sp, color = TextSecondary)
            }
        }

        // Category Filter Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.take(3).forEach { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = BuanSurface,
                            selectedContainerColor = BuanBlueCta,
                            labelColor = TextSecondary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        items(filteredServices) { service ->
            ServiceCard(
                service = service,
                onClick = { selectedService = service }
            )
        }

        item { Spacer(modifier = Modifier.height(72.dp)) }
    }

    // Detail Bottom Sheet
    if (selectedService != null) {
        ModalBottomSheet(
            onDismissRequest = { selectedService = null },
            sheetState = sheetState,
            containerColor = BuanSurface,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 6.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(BuanBorder)
                )
            }
        ) {
            val s = selectedService!!
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(BuanBlueSubtle),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getServiceIcon(s.iconName),
                                contentDescription = null,
                                tint = BuanBlueLight,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(s.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(s.category, fontSize = 11.sp, color = BuanBlueLight)
                        }
                    }
                    IconButton(onClick = { selectedService = null }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(s.fullDescription, fontSize = 13.sp, color = TextSecondary, lineHeight = 19.sp)

                Spacer(modifier = Modifier.height(16.dp))
                Text("What the Service Includes", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                s.includes.forEach { inc ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(inc, fontSize = 12.sp, color = TextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("How It Works", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                s.howItWorks.forEachIndexed { i, step ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(BuanSurfaceElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("${i + 1}", fontSize = 10.sp, color = BuanBlueLight, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(step, fontSize = 12.sp, color = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                BuanButton(
                    text = "Request a Quote for this Service",
                    onClick = {
                        selectedService = null
                        viewModel.navigateTo(Screen.RequestQuote)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun ServiceCard(
    service: BuanService,
    onClick: () -> Unit
) {
    BuanCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BuanBlueSubtle),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getServiceIcon(service.iconName),
                    contentDescription = service.name,
                    tint = BuanBlueLight,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = service.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Details",
                        fontSize = 12.sp,
                        color = BuanBlueLight,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = service.shortDescription,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

private fun getServiceIcon(iconName: String): ImageVector {
    return when (iconName) {
        "DirectionsBoat" -> Icons.Default.DirectionsBoat
        "Flight" -> Icons.Default.Flight
        "LocalShipping" -> Icons.Default.LocalShipping
        "ElectricBolt" -> Icons.Default.ElectricBolt
        "Warehouse" -> Icons.Default.Warehouse
        "DoorFront" -> Icons.Default.DoorFront
        "FactCheck" -> Icons.Default.FactCheck
        "Anchor" -> Icons.Default.Anchor
        "PrecisionManufacturing" -> Icons.Default.PrecisionManufacturing
        "Handshake" -> Icons.Default.Handshake
        "Build" -> Icons.Default.Build
        "ShoppingBag" -> Icons.Default.ShoppingBag
        else -> Icons.Default.LocalShipping
    }
}
