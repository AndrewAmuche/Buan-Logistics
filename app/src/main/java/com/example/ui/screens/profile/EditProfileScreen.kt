package com.example.ui.screens.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.UserEntity
import com.example.ui.components.BuanButton
import com.example.ui.components.BuanCard
import com.example.ui.components.BuanSecondaryButton
import com.example.ui.components.BuanTextField
import com.example.ui.theme.BuanBackground
import com.example.ui.theme.BuanBlueCta
import com.example.ui.theme.BuanBlueLight
import com.example.ui.theme.BuanBluePrimary
import com.example.ui.theme.BuanBlueSubtle
import com.example.ui.theme.BuanBorder
import com.example.ui.theme.BuanBorderLight
import com.example.ui.theme.BuanCoinGold
import com.example.ui.theme.BuanSurface
import com.example.ui.theme.BuanSurfaceElevated
import com.example.ui.theme.BuanSurfaceVariant
import com.example.ui.theme.StatusDelivered
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BuanViewModel
import kotlinx.coroutines.launch

data class AvatarPresetOption(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val bgGradient: List<Color>
)

val AVATAR_PRESETS = listOf(
    AvatarPresetOption("preset_1", "Executive", Icons.Default.Person, listOf(Color(0xFF1E3A8A), Color(0xFF2563EB))),
    AvatarPresetOption("preset_2", "Cargo Pilot", Icons.Default.Flight, listOf(Color(0xFF0F766E), Color(0xFF14B8A6))),
    AvatarPresetOption("preset_3", "Merchant", Icons.Default.Storefront, listOf(Color(0xFFB45309), Color(0xFFF59E0B))),
    AvatarPresetOption("preset_4", "Linehaul Driver", Icons.Default.LocalShipping, listOf(Color(0xFF4C1D95), Color(0xFF7C3AED))),
    AvatarPresetOption("preset_5", "Maritime Officer", Icons.Default.DirectionsBoat, listOf(Color(0xFF1E293B), Color(0xFF3B82F6))),
    AvatarPresetOption("preset_6", "Operations Lead", Icons.Default.Business, listOf(Color(0xFF065F46), Color(0xFF10B981)))
)

val POPULAR_COUNTRIES = listOf(
    "Nigeria",
    "United Kingdom",
    "United States",
    "Ghana",
    "Kenya",
    "South Africa",
    "United Arab Emirates",
    "China"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: BuanViewModel,
    onBack: () -> Unit = { viewModel.navigateBack() }
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val user by viewModel.currentUser.collectAsState()

    // Form State initialized with current user
    var fullName by remember(user) { mutableStateOf(user?.fullName ?: "") }
    var email by remember(user) { mutableStateOf(user?.email ?: "") }
    var phone by remember(user) { mutableStateOf(user?.phone ?: "") }
    var altPhone by remember(user) { mutableStateOf(user?.altPhone ?: "") }
    var jobTitle by remember(user) { mutableStateOf(user?.jobTitle ?: "") }
    var businessName by remember(user) { mutableStateOf(user?.businessName ?: "") }
    var accountType by remember(user) { mutableStateOf(user?.accountType ?: "Personal Account") }

    // Address State
    var streetAddress by remember(user) { mutableStateOf(user?.streetAddress ?: "") }
    var city by remember(user) { mutableStateOf(user?.city ?: "Lagos") }
    var state by remember(user) { mutableStateOf(user?.state ?: "Lagos State") }
    var country by remember(user) { mutableStateOf(user?.country ?: "Nigeria") }
    var postalCode by remember(user) { mutableStateOf(user?.postalCode ?: "") }

    // Avatar State
    var profilePictureUri by remember(user) { mutableStateOf(user?.profilePictureUri) }
    var selectedPreset by remember(user) { mutableStateOf(user?.avatarPreset ?: "preset_1") }

    var isSaving by remember { mutableStateOf(false) }

    // Android Photo Picker Launcher (Zero-Permission API compliant with Google Play Policies)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            profilePictureUri = uri.toString()
            Toast.makeText(context, "Profile picture selected!", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Profile & Settings",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BuanBackground,
                    titleContentColor = TextPrimary
                )
            )
        },
        containerColor = BuanBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            // 1. Avatar & Profile Picture Card
            item {
                BuanCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = BuanBorder
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "PROFILE PICTURE & AVATAR",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BuanBlueLight,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        // Large Avatar Circle with Camera Overlay
                        Box(
                            contentAlignment = Alignment.BottomEnd,
                            modifier = Modifier.size(108.dp)
                        ) {
                            if (profilePictureUri != null) {
                                AsyncImage(
                                    model = profilePictureUri,
                                    contentDescription = "Profile Picture",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(104.dp)
                                        .clip(CircleShape)
                                        .border(3.dp, BuanBlueCta, CircleShape)
                                        .shadow(6.dp, CircleShape)
                                )
                            } else {
                                val currentPreset = AVATAR_PRESETS.find { it.id == selectedPreset } ?: AVATAR_PRESETS.first()
                                Box(
                                    modifier = Modifier
                                        .size(104.dp)
                                        .clip(CircleShape)
                                        .background(Brush.linearGradient(currentPreset.bgGradient))
                                        .border(3.dp, BuanBlueLight.copy(alpha = 0.6f), CircleShape)
                                        .shadow(6.dp, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = currentPreset.icon,
                                        contentDescription = currentPreset.title,
                                        tint = Color.White,
                                        modifier = Modifier.size(52.dp)
                                    )
                                }
                            }

                            // Camera button overlay
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(BuanBlueCta)
                                    .border(2.dp, BuanSurface, CircleShape)
                                    .clickable {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Change photo",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Gallery and Remove buttons
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BuanSecondaryButton(
                                text = "Choose from Gallery",
                                icon = Icons.Default.PhotoLibrary,
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )

                            if (profilePictureUri != null) {
                                OutlinedButton(
                                    onClick = {
                                        profilePictureUri = null
                                        Toast.makeText(context, "Custom photo removed", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFFEF4444)
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.height(48.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Remove", fontSize = 12.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = BuanBorder, thickness = 0.5.dp)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Avatar Presets Carousel
                        Text(
                            text = "Or pick a professional identity icon:",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(AVATAR_PRESETS) { preset ->
                                val isSelected = profilePictureUri == null && selectedPreset == preset.id
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            profilePictureUri = null
                                            selectedPreset = preset.id
                                        }
                                        .background(if (isSelected) BuanSurfaceVariant else Color.Transparent)
                                        .border(
                                            width = if (isSelected) 1.5.dp else 0.5.dp,
                                            color = if (isSelected) BuanBlueLight else BuanBorder,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(Brush.linearGradient(preset.bgGradient)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = preset.icon,
                                            contentDescription = preset.title,
                                            tint = Color.White,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = preset.title,
                                        fontSize = 10.sp,
                                        color = if (isSelected) BuanBlueLight else TextMuted,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Personal Information & Contact Details Card
            item {
                BuanCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = BuanBorder
                ) {
                    Text(
                        text = "PERSONAL & CONTACT DETAILS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = BuanBlueLight,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Full Name
                    BuanTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = "Full Name",
                        placeholder = "e.g. Babajide Adeyemi",
                        leadingIcon = Icons.Default.Person,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "edit_profile_fullname_input"
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Job Title / Role Designation
                    BuanTextField(
                        value = jobTitle,
                        onValueChange = { jobTitle = it },
                        label = "Job Title / Designation",
                        placeholder = "e.g. Import & Export Manager",
                        leadingIcon = Icons.Default.Work,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "edit_profile_jobtitle_input"
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Primary Phone
                    BuanTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "Primary Phone Number",
                        placeholder = "e.g. +234 803 555 0192",
                        leadingIcon = Icons.Default.Phone,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "edit_profile_phone_input"
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Alternate Phone / WhatsApp
                    BuanTextField(
                        value = altPhone,
                        onValueChange = { altPhone = it },
                        label = "Alternate / WhatsApp Phone",
                        placeholder = "e.g. +234 812 345 6789",
                        leadingIcon = Icons.Default.Phone,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "edit_profile_altphone_input"
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Email Address
                    BuanTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        placeholder = "e.g. babajide@buanlogistics.com",
                        leadingIcon = Icons.Default.Email,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "edit_profile_email_input"
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Account Type Selection
                    Text(
                        text = "Account Classification:",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Personal Account", "Business Account", "Corporate Merchant").forEach { type ->
                            val isSelected = accountType == type
                            FilterChip(
                                selected = isSelected,
                                onClick = { accountType = type },
                                label = { Text(type, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BuanBluePrimary,
                                    selectedLabelColor = Color.White,
                                    containerColor = BuanSurfaceVariant,
                                    labelColor = TextMuted
                                )
                            )
                        }
                    }

                    if (accountType != "Personal Account") {
                        Spacer(modifier = Modifier.height(12.dp))
                        BuanTextField(
                            value = businessName,
                            onValueChange = { businessName = it },
                            label = "Business / Enterprise Name",
                            placeholder = "e.g. Adeyemi Global Trade Ltd",
                            leadingIcon = Icons.Default.Business,
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "edit_profile_business_input"
                        )
                    }
                }
            }

            // 3. Address & Delivery Location Card
            item {
                BuanCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = BuanBorder
                ) {
                    Text(
                        text = "PRIMARY ADDRESS & LOGISTICS LOCATION",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = BuanBlueLight,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Street Address
                    BuanTextField(
                        value = streetAddress,
                        onValueChange = { streetAddress = it },
                        label = "Street Address",
                        placeholder = "e.g. 14 Marina Boulevard, Victoria Island",
                        leadingIcon = Icons.Default.Home,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "edit_profile_street_input"
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // City & State Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        BuanTextField(
                            value = city,
                            onValueChange = { city = it },
                            label = "City",
                            placeholder = "e.g. Lagos",
                            leadingIcon = Icons.Default.LocationCity,
                            modifier = Modifier.weight(1f),
                            testTag = "edit_profile_city_input"
                        )
                        BuanTextField(
                            value = state,
                            onValueChange = { state = it },
                            label = "State / Region",
                            placeholder = "e.g. Lagos State",
                            modifier = Modifier.weight(1f),
                            testTag = "edit_profile_state_input"
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    // Country & Postal Code Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        BuanTextField(
                            value = country,
                            onValueChange = { country = it },
                            label = "Country",
                            placeholder = "e.g. Nigeria",
                            leadingIcon = Icons.Default.Public,
                            modifier = Modifier.weight(1.3f),
                            testTag = "edit_profile_country_input"
                        )
                        BuanTextField(
                            value = postalCode,
                            onValueChange = { postalCode = it },
                            label = "Postal / ZIP",
                            placeholder = "e.g. 101241",
                            modifier = Modifier.weight(0.9f),
                            testTag = "edit_profile_postal_input"
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick Country Selector Pills
                    Text(
                        text = "Quick corridor selection:",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(POPULAR_COUNTRIES) { cName ->
                            val isSel = country.equals(cName, ignoreCase = true)
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSel) BuanBlueLight.copy(alpha = 0.2f) else BuanSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSel) BuanBlueLight else BuanBorder
                                ),
                                modifier = Modifier.clickable { country = cName }
                            ) {
                                Text(
                                    text = cName,
                                    fontSize = 11.sp,
                                    color = if (isSel) BuanBlueLight else TextSecondary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 4. Save and Cancel Action Buttons
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BuanButton(
                        text = if (isSaving) "Saving Changes..." else "Save Profile Settings",
                        icon = Icons.Default.CheckCircle,
                        isLoading = isSaving,
                        onClick = {
                            if (fullName.isBlank()) {
                                Toast.makeText(context, "Full name cannot be blank", Toast.LENGTH_SHORT).show()
                                return@BuanButton
                            }
                            if (phone.isBlank()) {
                                Toast.makeText(context, "Phone number cannot be blank", Toast.LENGTH_SHORT).show()
                                return@BuanButton
                            }
                            if (email.isBlank()) {
                                Toast.makeText(context, "Email address cannot be blank", Toast.LENGTH_SHORT).show()
                                return@BuanButton
                            }

                            isSaving = true
                            viewModel.updateUserProfile(
                                fullName = fullName,
                                email = email,
                                phone = phone,
                                altPhone = altPhone,
                                accountType = accountType,
                                businessName = businessName,
                                jobTitle = jobTitle,
                                streetAddress = streetAddress,
                                city = city,
                                state = state,
                                country = country,
                                postalCode = postalCode,
                                profilePictureUri = profilePictureUri,
                                avatarPreset = selectedPreset,
                                onSuccess = {
                                    isSaving = false
                                    Toast.makeText(context, "Account profile updated successfully!", Toast.LENGTH_LONG).show()
                                    onBack()
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_profile_button")
                    )

                    BuanSecondaryButton(
                        text = "Discard & Cancel",
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
