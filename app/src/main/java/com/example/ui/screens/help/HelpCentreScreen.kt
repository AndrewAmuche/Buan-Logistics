package com.example.ui.screens.help

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.components.BuanButton
import com.example.ui.components.BuanCard
import com.example.ui.components.BuanSecondaryButton
import com.example.ui.components.BuanTextField
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

data class FaqItem(
    val category: String,
    val question: String,
    val answer: String
)

@Composable
fun HelpCentreScreen(viewModel: BuanViewModel) {
    BackHandler { viewModel.navigateBack() }

    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var expandedFaqIndex by remember { mutableStateOf<Int?>(null) }

    val categories = listOf(
        "All", "Shipping", "Tracking", "Payments", "Hub Providers",
        "Referrals & Cashback", "Business Accounts", "International Shipping", "Customs", "Warehousing"
    )

    val allFaqs = listOf(
        FaqItem(
            category = "Shipping",
            question = "How do I book a new shipment?",
            answer = "Tap the 'Book' tab or 'Book Shipment' on the dashboard. Follow our 7-step wizard to provide sender, receiver, cargo dimensions, and choose between door-to-door or hub drop-off."
        ),
        FaqItem(
            category = "Tracking",
            question = "How accurate is the Live Radar Tracking map?",
            answer = "The Live Radar Tracking map integrates with our flight telemetry, vessel AIS data, and interstate haulage checkpoints. Status milestones are updated automatically at every hub scan."
        ),
        FaqItem(
            category = "Hub Providers",
            question = "How can a store become an authorized BUAN Hub?",
            answer = "Navigate to 'Become a Hub Provider' in the menu and submit your commercial location details. Approved hubs earn verified intake credits and receive promotional hub signage."
        ),
        FaqItem(
            category = "Referrals & Cashback",
            question = "How do I get Free Shipping & Cashback?",
            answer = "App users receive 100% Free Shipping and $50 Cashback when 7 people register with their referral link or unique coupon code. You can request your unique coupon code anytime in the Referral & Cashback Rewards section."
        ),
        FaqItem(
            category = "Referrals & Cashback",
            question = "How do I get my unique coupon code?",
            answer = "Navigate to the Referral & Cashback Rewards section from your profile or the home screen banner slider and tap 'Request My Unique Coupon Code'. A unique coupon code will be issued to you instantly!"
        ),
        FaqItem(
            category = "Customs",
            question = "Does BUAN handle Nigerian Customs (Form M & PAAR)?",
            answer = "Yes. BUAN Logistics has in-house licensed customs brokerage teams in Apapa, Tin Can Island, and Murtala Muhammed Airport handling Form M, PAAR, SONCAP, and NAFDAC clearances."
        ),
        FaqItem(
            category = "International Shipping",
            question = "Which international destinations are supported?",
            answer = "We provide daily scheduled air freight and weekly sea freight to over 160 countries including the UK, USA, UAE, China, Germany, Canada, and all ECOWAS member states."
        )
    )

    val filteredFaqs = allFaqs.filter { faq ->
        val matchesCat = selectedCategory == "All" || faq.category == selectedCategory
        val matchesQuery = searchQuery.isBlank() ||
                faq.question.contains(searchQuery, ignoreCase = true) ||
                faq.answer.contains(searchQuery, ignoreCase = true)
        matchesCat && matchesQuery
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
                title = "Help Centre",
                onBackClick = { viewModel.navigateBack() }
            )
        }

        // Header and Search
        item {
            Column {
                Text("How can we help?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("Search our knowledge base or reach our 24/7 logistics dispatch", fontSize = 13.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(12.dp))

                BuanTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = "Search Help Topics",
                    placeholder = "e.g. customs clearance, tracking, hub provider",
                    leadingIcon = Icons.Default.Search
                )
            }
        }

        // Categories horizontal chip row
        item {
            Column {
                Text("Help Categories", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
        }

        // Contact Support Channels Card
        item {
            BuanCard(
                glowEffect = true,
                backgroundColor = BuanSurfaceElevated
            ) {
                Text("Direct Support Channels", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("Connect directly with a cargo operations specialist", fontSize = 12.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SupportChannelButton(
                        icon = Icons.Default.Chat,
                        label = "Live Chat",
                        modifier = Modifier.weight(1f),
                        onClick = { Toast.makeText(context, "Connecting to BUAN Live Support...", Toast.LENGTH_SHORT).show() }
                    )
                    SupportChannelButton(
                        icon = Icons.Default.Phone,
                        label = "Call Us",
                        modifier = Modifier.weight(1f),
                        onClick = { Toast.makeText(context, "Dialing +234 1 293 4000...", Toast.LENGTH_SHORT).show() }
                    )
                    SupportChannelButton(
                        icon = Icons.Default.CalendarMonth,
                        label = "Book Session",
                        modifier = Modifier.weight(1f),
                        onClick = { Toast.makeText(context, "Freight consultation session scheduler opened", Toast.LENGTH_SHORT).show() }
                    )
                }
            }
        }

        // Frequently Asked Questions
        item {
            Text("Frequently Asked Questions", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }

        itemsIndexed(filteredFaqs) { index, faq ->
            val isExpanded = expandedFaqIndex == index

            BuanCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { expandedFaqIndex = if (isExpanded) null else index }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(BuanBlueSubtle)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(faq.category, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = BuanBlueLight)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(faq.question, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    }
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = BuanBlueLight
                    )
                }

                AnimatedVisibility(visible = isExpanded) {
                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        Text(faq.answer, fontSize = 13.sp, color = TextSecondary, lineHeight = 18.sp)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(72.dp)) }
    }
}

@Composable
private fun SupportChannelButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Color(0x22000000),
                spotColor = Color(0x33000000)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        color = BuanSurfaceVariant,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = BuanBlueLight, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        }
    }
}
