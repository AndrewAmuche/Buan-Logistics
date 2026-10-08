package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.HubApplicationEntity
import com.example.data.local.HubEntity
import com.example.data.local.NotificationEntity
import com.example.data.local.QuoteRequestEntity
import com.example.data.local.ShipmentEntity
import com.example.data.local.TrackingEventEntity
import com.example.data.local.UserEntity
import com.example.data.repository.BuanRepository
import com.example.model.AccountType
import com.example.model.AppCurrency
import com.example.model.CorporateTier
import com.example.model.ShipmentStatus
import com.example.model.ShipmentType
import com.example.model.TransportMode
import com.example.model.UserRole
import com.example.ui.theme.BuanThemeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    data object Welcome : Screen()
    data object Login : Screen()
    data object Register : Screen()
    data object ForgotPassword : Screen()
    data object Home : Screen()
    data object Shipments : Screen()
    data class ShipmentDetail(val trackingNumber: String) : Screen()
    data class LiveTracking(val trackingNumber: String) : Screen()
    data object CreateShipment : Screen()
    data class ShipmentCreatedSuccess(val trackingNumber: String) : Screen()
    data object RequestQuote : Screen()
    data object Services : Screen()
    data object FindHub : Screen()
    data object BecomeHubProvider : Screen()
    data object BuanCoinWallet : Screen()
    data object Notifications : Screen()
    data object Profile : Screen()
    data object EditProfile : Screen()
    data object Subscriptions : Screen()
    data object HelpCentre : Screen()
    data object AdminPanel : Screen()
}

data class CreateShipmentDraft(
    val step: Int = 1,
    // Step 1: Sender
    val senderName: String = "",
    val senderPhone: String = "",
    val senderEmail: String = "",
    val senderAddress: String = "",
    val senderCity: String = "Lagos",
    val senderState: String = "Lagos State",
    val senderCountry: String = "Nigeria",
    // Step 2: Receiver
    val receiverName: String = "",
    val receiverPhone: String = "",
    val receiverEmail: String = "",
    val receiverAddress: String = "",
    val receiverCity: String = "London",
    val receiverState: String = "Greater London",
    val receiverCountry: String = "United Kingdom",
    // Step 3: Type
    val shipmentType: ShipmentType = ShipmentType.PARCEL,
    // Step 4: Transport Mode
    val transportMode: TransportMode = TransportMode.AIR,
    // Step 5: Package Info
    val description: String = "",
    val quantity: Int = 1,
    val weightKg: Double = 5.0,
    val lengthCm: Double = 30.0,
    val widthCm: Double = 20.0,
    val heightCm: Double = 15.0,
    val declaredValueUsd: Double = 250.0,
    // Step 6: Pickup & Delivery
    val pickupType: String = "Customer Address",
    val deliveryType: String = "Recipient Address",
    val pickupHub: String = "Ikeja Air Cargo Central Hub",
    val deliveryHub: String = "London Heathrow Gateway Hub"
) {
    fun calculateEstimatedCost(): Double {
        val baseRate = when (transportMode) {
            TransportMode.AIR -> 45.0
            TransportMode.SEA -> 12.0
            TransportMode.ROAD -> 18.0
            TransportMode.EXPRESS -> 75.0
        }
        val weightCost = weightKg * when (transportMode) {
            TransportMode.AIR -> 8.5
            TransportMode.SEA -> 2.2
            TransportMode.ROAD -> 3.5
            TransportMode.EXPRESS -> 14.0
        }
        val pickupCost = if (pickupType == "Customer Address") 25.0 else 5.0
        val deliveryCost = if (deliveryType == "Recipient Address") 20.0 else 5.0
        return baseRate + weightCost + pickupCost + deliveryCost
    }
}

class BuanViewModel(private val repository: BuanRepository) : ViewModel() {

    private val initialScreen: Screen = if (repository.isDeviceRegistered()) Screen.Login else Screen.Welcome

    private val _currentScreen = MutableStateFlow<Screen>(initialScreen)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _navigationStack = mutableListOf<Screen>(initialScreen)

    val currentUser: StateFlow<UserEntity?> = repository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allShipments: StateFlow<List<ShipmentEntity>> = repository.allShipments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allHubs: StateFlow<List<HubEntity>> = repository.allHubs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wallet = repository.wallet
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val coinTransactions = repository.transactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotificationsCount: StateFlow<Int> = repository.unreadNotificationsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val quotes: StateFlow<List<QuoteRequestEntity>> = repository.allQuotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allUsers: StateFlow<List<UserEntity>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allHubApplications: StateFlow<List<HubApplicationEntity>> = repository.allHubApplications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI Local State
    private val _trackingSearchInput = MutableStateFlow("")
    val trackingSearchInput = _trackingSearchInput.asStateFlow()

    private val _trackingError = MutableStateFlow<String?>(null)
    val trackingError = _trackingError.asStateFlow()

    private val _shipmentFilter = MutableStateFlow("All")
    val shipmentFilter = _shipmentFilter.asStateFlow()

    private val _shipmentSearchQuery = MutableStateFlow("")
    val shipmentSearchQuery = _shipmentSearchQuery.asStateFlow()

    private val _hubSearchQuery = MutableStateFlow("")
    val hubSearchQuery = _hubSearchQuery.asStateFlow()

    // Referral & Unique Coupon Code System
    private val _referralCouponCode = MutableStateFlow<String?>(repository.getReferralCouponCode())
    val referralCouponCode: StateFlow<String?> = _referralCouponCode.asStateFlow()

    private val _referralCount = MutableStateFlow(repository.getReferralCount())
    val referralCount: StateFlow<Int> = _referralCount.asStateFlow()

    private val _cashbackBalance = MutableStateFlow(repository.getCashbackBalance())
    val cashbackBalance: StateFlow<Double> = _cashbackBalance.asStateFlow()

    private val _isFreeShippingUnlocked = MutableStateFlow(repository.isFreeShippingUnlocked())
    val isFreeShippingUnlocked: StateFlow<Boolean> = _isFreeShippingUnlocked.asStateFlow()

    private val _isFreeShippingApplied = MutableStateFlow(false)
    val isFreeShippingApplied: StateFlow<Boolean> = _isFreeShippingApplied.asStateFlow()

    private val _appliedCouponDiscount = MutableStateFlow(0.0)
    val appliedCouponDiscount: StateFlow<Double> = _appliedCouponDiscount.asStateFlow()

    // Create Shipment Draft State
    private val _createDraft = MutableStateFlow(CreateShipmentDraft())
    val createDraft = _createDraft.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting = _isSubmitting.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage = _userMessage.asStateFlow()

    init {
        repository.startCloudShipmentsSync()
    }

    fun navigateTo(screen: Screen) {
        val destination = if (screen is Screen.CreateShipment) Screen.RequestQuote else screen
        if (_currentScreen.value != destination) {
            _navigationStack.add(destination)
            _currentScreen.value = destination
        }
    }

    fun navigateBack(): Boolean {
        if (_navigationStack.size > 1) {
            _navigationStack.removeAt(_navigationStack.size - 1)
            val previous = _navigationStack.last()
            _currentScreen.value = previous
            return true
        } else if (_currentScreen.value != Screen.Home) {
            _currentScreen.value = Screen.Home
            return true
        }
        return false
    }

    fun setTrackingSearch(query: String) {
        _trackingSearchInput.value = query
        _trackingError.value = null
    }

    suspend fun findShipmentSync(trackingNumber: String): ShipmentEntity? {
        return repository.findShipmentSync(trackingNumber)
    }

    fun refreshShipmentFromCloud(trackingNumber: String, onComplete: ((Boolean) -> Unit)? = null) {
        viewModelScope.launch {
            val found = repository.findShipmentSync(trackingNumber)
            onComplete?.invoke(found != null)
        }
    }

    fun syncAllCloudShipments() {
        viewModelScope.launch {
            repository.startCloudShipmentsSync()
            _userMessage.value = "Synced with Web Portal cloud database"
        }
    }

    fun submitTrackingSearch() {
        val raw = _trackingSearchInput.value.trim()
        val query = raw.uppercase()
        if (query.isBlank()) {
            _trackingError.value = "Please enter a tracking number"
            return
        }

        viewModelScope.launch {
            // 1. Try local/cloud search with exact raw, uppercase, or prefix variations
            var found = repository.findShipmentSync(query) ?: repository.findShipmentSync(raw)

            if (found == null) {
                val cleanNoPrefix = query.removePrefix("BUAN").removePrefix("-")
                found = repository.findShipmentSync("BUAN-$cleanNoPrefix")
                    ?: repository.findShipmentSync(cleanNoPrefix)
            }

            if (found != null) {
                _trackingError.value = null
                navigateTo(Screen.LiveTracking(found.trackingNumber))
            } else {
                // Navigate directly to LiveTracking with user's tracking code;
                // LiveTrackingScreen's Cloud Radar query will automatically search Firestore in real-time
                _trackingError.value = null
                navigateTo(Screen.LiveTracking(raw))
            }
        }
    }

    fun setShipmentFilter(filter: String) {
        _shipmentFilter.value = filter
    }

    fun setShipmentSearch(query: String) {
        _shipmentSearchQuery.value = query
    }

    fun setHubSearch(query: String) {
        _hubSearchQuery.value = query
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    // Shipment Draft Form Helpers
    fun updateDraft(updater: CreateShipmentDraft.() -> CreateShipmentDraft) {
        _createDraft.value = _createDraft.value.updater()
    }

    fun resetDraft() {
        val user = currentUser.value
        _createDraft.value = CreateShipmentDraft(
            senderName = user?.fullName ?: "Babajide Adeyemi",
            senderPhone = user?.phone ?: "+234 803 555 0192",
            senderEmail = user?.email ?: "babajide@buanlogistics.com",
            senderAddress = "14 Marina Boulevard, Victoria Island",
            senderCity = "Lagos",
            senderState = "Lagos State",
            senderCountry = "Nigeria"
        )
    }

    fun submitCreateShipment(onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            _isSubmitting.value = true
            val draft = _createDraft.value
            val cost = draft.calculateEstimatedCost()
            val transitDays = when (draft.transportMode) {
                TransportMode.EXPRESS -> "2 - 3 Days"
                TransportMode.AIR -> "4 - 5 Days"
                TransportMode.ROAD -> "3 - 7 Days"
                TransportMode.SEA -> "20 - 30 Days"
            }

            val shipment = ShipmentEntity(
                trackingNumber = "", // Auto generated in repository
                senderName = draft.senderName.ifBlank { "Babajide Adeyemi" },
                senderPhone = draft.senderPhone.ifBlank { "+234 803 555 0192" },
                senderEmail = draft.senderEmail.ifBlank { "babajide@buanlogistics.com" },
                senderAddress = draft.senderAddress.ifBlank { "14 Marina Boulevard" },
                senderCity = draft.senderCity.ifBlank { "Lagos" },
                senderState = draft.senderState.ifBlank { "Lagos State" },
                senderCountry = draft.senderCountry.ifBlank { "Nigeria" },
                receiverName = draft.receiverName.ifBlank { "Receiver Client" },
                receiverPhone = draft.receiverPhone.ifBlank { "+44 20 7946 0199" },
                receiverEmail = draft.receiverEmail.ifBlank { "client@dest.com" },
                receiverAddress = draft.receiverAddress.ifBlank { "25 Commercial Way" },
                receiverCity = draft.receiverCity.ifBlank { "London" },
                receiverState = draft.receiverState.ifBlank { "Greater London" },
                receiverCountry = draft.receiverCountry.ifBlank { "United Kingdom" },
                shipmentType = draft.shipmentType.title,
                transportMode = draft.transportMode.title,
                description = draft.description.ifBlank { "General Cargo Items" },
                quantity = draft.quantity,
                weightKg = draft.weightKg,
                lengthCm = draft.lengthCm,
                widthCm = draft.widthCm,
                heightCm = draft.heightCm,
                declaredValueUsd = draft.declaredValueUsd,
                pickupType = draft.pickupType,
                deliveryType = draft.deliveryType,
                status = "Shipment Created",
                currentLocation = "${draft.senderCity} Origin Hub",
                origin = "${draft.senderCity}, ${draft.senderCountry}",
                destination = "${draft.receiverCity}, ${draft.receiverCountry}",
                shipmentDate = "",
                estimatedDelivery = transitDays,
                lastUpdated = "Just created",
                estimatedCostUsd = cost
            )

            val generatedNumber = repository.createShipment(shipment)
            _isSubmitting.value = false
            resetDraft()
            onSuccess(generatedNumber)
        }
    }

    fun advanceShipmentStatus(trackingNumber: String, nextStatus: ShipmentStatus, location: String) {
        viewModelScope.launch {
            repository.advanceShipmentStatus(trackingNumber, nextStatus, location)
            _userMessage.value = "Status updated to ${nextStatus.displayName}"
        }
    }

    fun submitHubApplication(app: HubApplicationEntity, onDone: () -> Unit) {
        viewModelScope.launch {
            _isSubmitting.value = true
            repository.submitHubProviderApplication(app)
            _isSubmitting.value = false
            onDone()
        }
    }

    fun submitQuoteRequest(quote: QuoteRequestEntity, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            _isSubmitting.value = true
            val refId = repository.submitQuoteRequest(quote)
            _isSubmitting.value = false
            onSuccess(refId)
        }
    }

    fun markNotificationAsRead(id: Int) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
            _userMessage.value = "All notifications marked as read"
        }
    }

    fun triggerTestNotification(title: String = "📦 Shipment Update: BUAN-78219", message: String = "Cargo has cleared Customs & PAAR inspection at Port of Lagos.", trackingNumber: String? = "BUAN-78219") {
        viewModelScope.launch {
            repository.triggerTestPushNotification(title, message, trackingNumber)
            _userMessage.value = "Push notification triggered!"
        }
    }

    fun switchUserRole(role: UserRole) {
        viewModelScope.launch {
            repository.switchUserRole(role.displayName)
            _userMessage.value = "Role updated to ${role.displayName}"
        }
    }

    fun switchAccountType(type: AccountType) {
        viewModelScope.launch {
            repository.switchAccountType(type.displayName)
            _userMessage.value = "Account type set to ${type.displayName}"
        }
    }

    fun redeemReward(coins: Int, title: String) {
        viewModelScope.launch {
            val current = wallet.value?.balance ?: 0
            if (current >= coins) {
                repository.redeemCoins(coins, title)
                _userMessage.value = "Successfully redeemed $coins BUAN-COIN!"
            } else {
                _userMessage.value = "Insufficient BUAN-COIN balance ($current / $coins)"
            }
        }
    }

    fun requestUniqueCouponCode() {
        viewModelScope.launch {
            val user = repository.getActiveUserSync()
            val userName = user?.fullName ?: "BUAN Member"
            val newCode = repository.issueUniqueCouponCode(userName)
            _referralCouponCode.value = newCode
            _userMessage.value = "Your unique coupon code $newCode has been issued!"
        }
    }

    fun simulateReferralRegistration() {
        viewModelScope.launch {
            val friendNames = listOf("Adebayo K.", "Chidinma O.", "Fatima M.", "Emeka U.", "Olumide B.", "Zainab A.", "David E.")
            val randomFriend = friendNames.random()
            val (updatedCount, unlocked) = repository.recordReferralRegistration(randomFriend)
            _referralCount.value = updatedCount
            _cashbackBalance.value = repository.getCashbackBalance()
            if (unlocked) {
                _isFreeShippingUnlocked.value = true
                _userMessage.value = "🎉 7 Referrals complete! 100% Free Shipping and $50 Cashback Unlocked!"
            } else {
                _userMessage.value = "$randomFriend registered with your code! ($updatedCount/7 referrals)"
            }
        }
    }

    fun toggleFreeShippingPerk() {
        if (_isFreeShippingUnlocked.value) {
            _isFreeShippingApplied.value = !_isFreeShippingApplied.value
            if (_isFreeShippingApplied.value) {
                _userMessage.value = "100% Free Shipping discount applied to your quote/booking!"
            } else {
                _userMessage.value = "Free Shipping option removed"
            }
        } else {
            _userMessage.value = "Refer 7 friends to unlock 100% Free Shipping!"
        }
    }

    fun applyReferralCouponCode(code: String) {
        val trimmed = code.trim().uppercase()
        if (trimmed.isNotBlank()) {
            _appliedCouponDiscount.value = 15.0 // 15% discount or $15 cashback
            _userMessage.value = "Coupon $trimmed applied: 15% discount + Cashback active!"
        } else {
            _userMessage.value = "Please enter a valid coupon code"
        }
    }

    fun copyCouponCodeToClipboard(context: android.content.Context) {
        val code = _referralCouponCode.value ?: return
        val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as? android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("BUAN Coupon Code", code)
        clipboard?.setPrimaryClip(clip)
        _userMessage.value = "Coupon code $code copied to clipboard!"
    }

    fun shareReferralCode(context: android.content.Context) {
        val code = _referralCouponCode.value ?: "BUAN777"
        val link = "https://buanlogistics.com/ref?code=$code"
        val sendIntent = android.content.Intent().apply {
            action = android.content.Intent.ACTION_SEND
            putExtra(android.content.Intent.EXTRA_TEXT, "Use my unique BUAN coupon code: $code or register via $link to get fast international cargo delivery and rewards!")
            type = "text/plain"
        }
        val shareIntent = android.content.Intent.createChooser(sendIntent, "Share Referral Coupon")
        shareIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    private val _showDashboardTour = MutableStateFlow(false)
    val showDashboardTour: StateFlow<Boolean> = _showDashboardTour.asStateFlow()

    fun triggerTourIfFirstTime() {
        if (!repository.hasCompletedDashboardTour()) {
            _showDashboardTour.value = true
        }
    }

    fun startDashboardTour() {
        _showDashboardTour.value = true
    }

    fun dismissDashboardTour() {
        _showDashboardTour.value = false
        repository.setDashboardTourCompleted(true)
    }

    fun resetDashboardTour() {
        repository.setDashboardTourCompleted(false)
        _showDashboardTour.value = true
    }

    fun isDeviceRegistered(): Boolean = repository.isDeviceRegistered()

    fun markDeviceRegistered() {
        repository.setDeviceRegistered(true)
    }

    // Device & Biometric Fingerprint State
    private val _isFingerprintRegistered = MutableStateFlow(repository.isFingerprintRegistered())
    val isFingerprintRegistered: StateFlow<Boolean> = _isFingerprintRegistered.asStateFlow()

    private val _registeredUserName = MutableStateFlow(repository.getRegisteredUserName())
    val registeredUserName: StateFlow<String> = _registeredUserName.asStateFlow()

    private val _registeredUserEmail = MutableStateFlow(repository.getRegisteredUserEmail())
    val registeredUserEmail: StateFlow<String> = _registeredUserEmail.asStateFlow()

    private val _registeredUserPhone = MutableStateFlow(repository.getRegisteredUserPhone())
    val registeredUserPhone: StateFlow<String> = _registeredUserPhone.asStateFlow()

    fun setFingerprintRegistered(registered: Boolean) {
        repository.setFingerprintRegistered(registered)
        _isFingerprintRegistered.value = registered
        if (registered) {
            _userMessage.value = "Fingerprint registered for fast access on this device"
        } else {
            _userMessage.value = "Fingerprint sign-in disabled"
        }
    }

    fun loginWithFingerprint(onSuccess: (() -> Unit)? = null) {
        val email = _registeredUserEmail.value
        loginSuccess(email)
        _userMessage.value = "Fingerprint verified! Welcome back, ${_registeredUserName.value}."
        onSuccess?.invoke()
    }

    fun logout() {
        repository.setDeviceRegistered(true)
        _navigationStack.clear()
        _navigationStack.add(Screen.Login)
        _currentScreen.value = Screen.Login
        _userMessage.value = "Logged out successfully"
    }

    fun login(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val result = repository.authenticateUser(email, password)
            result.onSuccess { user ->
                switchAccountType(AccountType.fromDisplayName(user.accountType))
                _registeredUserName.value = user.fullName
                _registeredUserEmail.value = user.email
                _registeredUserPhone.value = user.phone
                _navigationStack.clear()
                _navigationStack.add(Screen.Home)
                _currentScreen.value = Screen.Home
                if (!repository.hasCompletedDashboardTour()) {
                    _showDashboardTour.value = true
                }
                _userMessage.value = "Welcome back, ${user.fullName}!"
                onResult(true, "Welcome back, ${user.fullName}!")
            }.onFailure { error ->
                onResult(false, error.message ?: "Invalid email or password")
            }
        }
    }

    fun loginSuccess(email: String = "babajide@buanlogistics.com") {
        login(email, "password123") { _, _ -> }
    }

    fun switchUser(user: UserEntity) {
        repository.setActiveUser(user)
        switchAccountType(AccountType.fromDisplayName(user.accountType))
        _registeredUserName.value = user.fullName
        _registeredUserEmail.value = user.email
        _registeredUserPhone.value = user.phone
        _userMessage.value = "Switched active account to ${user.fullName}"
    }

    fun updateUserProfile(
        fullName: String,
        email: String,
        phone: String,
        altPhone: String,
        accountType: String,
        businessName: String,
        jobTitle: String,
        streetAddress: String,
        city: String,
        state: String,
        country: String,
        postalCode: String,
        profilePictureUri: String?,
        avatarPreset: String,
        onSuccess: (() -> Unit)? = null
    ) {
        val current = currentUser.value ?: return
        val updated = current.copy(
            fullName = fullName.trim(),
            email = email.trim(),
            phone = phone.trim(),
            altPhone = altPhone.trim(),
            accountType = accountType,
            businessName = businessName.trim(),
            jobTitle = jobTitle.trim(),
            streetAddress = streetAddress.trim(),
            city = city.trim(),
            state = state.trim(),
            country = country.trim(),
            postalCode = postalCode.trim(),
            profilePictureUri = profilePictureUri,
            avatarPreset = avatarPreset
        )
        viewModelScope.launch {
            repository.updateUserProfile(updated)
            _registeredUserName.value = updated.fullName
            _registeredUserEmail.value = updated.email
            _userMessage.value = "Profile settings saved successfully"
            onSuccess?.invoke()
        }
    }

    fun upgradeSubscription(
        tier: com.example.model.SubscriptionTier,
        billing: String = "MONTHLY",
        onSuccess: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            repository.updateSubscriptionTier(tier.id, billing)
            _userMessage.value = if (tier == com.example.model.SubscriptionTier.NONE) {
                "Subscription updated to Standard Free"
            } else {
                "Congratulations! You are now upgraded to ${tier.displayName}"
            }
            onSuccess?.invoke()
        }
    }

    suspend fun registerAccount(
        fullName: String,
        email: String,
        phone: String,
        password: String,
        accountType: AccountType
    ): Result<UserEntity> {
        val checkEmail = email.trim()
        val existing = repository.findUserByEmail(checkEmail)
        if (existing != null) {
            return Result.failure(Exception("ACCOUNT_EXISTS"))
        }

        val newUser = repository.registerNewAccount(
            fullName = fullName,
            email = checkEmail,
            phone = phone,
            password = password,
            accountType = accountType
        )

        switchAccountType(accountType)
        _registeredUserName.value = newUser.fullName
        _registeredUserEmail.value = newUser.email
        _registeredUserPhone.value = newUser.phone
        _isFingerprintRegistered.value = false
        repository.setDashboardTourCompleted(false)
        _navigationStack.clear()
        _navigationStack.add(Screen.Home)
        _currentScreen.value = Screen.Home
        _showDashboardTour.value = true
        _userMessage.value = "Registration successful! Welcome to BUAN, ${newUser.fullName}."
        return Result.success(newUser)
    }

    fun completeRegistration(
        fullName: String,
        email: String,
        phone: String,
        accountType: AccountType,
        password: String = "password123",
        registerFingerprint: Boolean = false
    ) {
        viewModelScope.launch {
            registerAccount(
                fullName = fullName,
                email = email,
                phone = phone,
                password = password,
                accountType = accountType
            )
        }
    }

    // Theme Management (Light and Dark)
    private val _isDarkTheme = MutableStateFlow(repository.isDarkTheme())
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun toggleTheme() {
        val next = !_isDarkTheme.value
        _isDarkTheme.value = next
        repository.setDarkTheme(next)
        BuanThemeState.isDark = next
    }

    fun setDarkTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
        repository.setDarkTheme(isDark)
        BuanThemeState.isDark = isDark
    }

    // Country & Currency Localization (Primary: Naira & British Pounds)
    private val _userCountry = MutableStateFlow(repository.getUserCountry())
    val userCountry: StateFlow<String> = _userCountry.asStateFlow()

    private val _userCurrency = MutableStateFlow(
        AppCurrency.fromCode(repository.getUserCurrencyCode())
    )
    val userCurrency: StateFlow<AppCurrency> = _userCurrency.asStateFlow()

    fun setUserCountry(country: String) {
        _userCountry.value = country
        repository.setUserCountry(country)
        val resolvedCurrency = AppCurrency.fromCountry(country)
        _userCurrency.value = resolvedCurrency
        repository.setUserCurrencyCode(resolvedCurrency.code)
    }

    fun setUserCurrency(currency: AppCurrency) {
        _userCurrency.value = currency
        repository.setUserCurrencyCode(currency.code)
        _userCountry.value = currency.countryName
        repository.setUserCountry(currency.countryName)
    }

    fun formatPrice(usdAmount: Double): String {
        return _userCurrency.value.format(usdAmount)
    }

    fun formatDualPrice(usdAmount: Double): Pair<String, String> {
        val current = _userCurrency.value
        val primaryFormatted = current.formatWithCode(usdAmount)
        val secondary = when (current) {
            AppCurrency.NGN -> "≈ ${AppCurrency.GBP.formatWithCode(usdAmount)}"
            AppCurrency.GBP -> "≈ ${AppCurrency.NGN.formatWithCode(usdAmount)}"
            else -> "≈ ${AppCurrency.NGN.format(usdAmount)} / ${AppCurrency.GBP.format(usdAmount)}"
        }
        return Pair(primaryFormatted, secondary)
    }

    // ---------------------------------------------------------
    // Admin Operations & Customer Activities Management
    // ---------------------------------------------------------
    fun adminUpdateShipmentStatus(trackingNumber: String, status: String, location: String) {
        viewModelScope.launch {
            repository.updateShipmentStatusByAdmin(trackingNumber, status, location)
            _userMessage.value = "Consignment $trackingNumber updated to '$status'"
        }
    }

    fun adminToggleUserVerification(userId: String, currentVerified: Boolean) {
        viewModelScope.launch {
            repository.updateUserVerification(userId, !currentVerified)
            _userMessage.value = if (!currentVerified) "Customer account verified" else "Customer verification status revoked"
        }
    }

    fun adminUpdateQuoteStatus(quoteId: Int, status: String) {
        viewModelScope.launch {
            repository.updateQuoteStatus(quoteId, status)
            _userMessage.value = "Quote request marked as $status"
        }
    }

    fun adminUpdateHubApplication(applicationId: Int, status: String) {
        viewModelScope.launch {
            repository.updateHubApplicationStatus(applicationId, status)
            _userMessage.value = "Hub application status updated to $status"
        }
    }

    // ---------------------------------------------------------
    // Cross-Platform Backend & Account Existence Detection
    // ---------------------------------------------------------
    private val _prefilledLoginEmail = MutableStateFlow("")
    val prefilledLoginEmail: StateFlow<String> = _prefilledLoginEmail.asStateFlow()

    private val _backendApiUrl = MutableStateFlow(repository.getBackendApiUrl())
    val backendApiUrl: StateFlow<String> = _backendApiUrl.asStateFlow()

    private val _isBackendSyncEnabled = MutableStateFlow(repository.isBackendSyncEnabled())
    val isBackendSyncEnabled: StateFlow<Boolean> = _isBackendSyncEnabled.asStateFlow()

    fun updateBackendApiUrl(url: String) {
        _backendApiUrl.value = url
        repository.setBackendApiUrl(url)
        _userMessage.value = "Backend API URL configured successfully"
    }

    fun toggleBackendSync(enabled: Boolean) {
        _isBackendSyncEnabled.value = enabled
        repository.setBackendSyncEnabled(enabled)
    }

    suspend fun checkExistingAccount(email: String): UserEntity? {
        return repository.findUserByEmail(email)
    }

    fun isWebRegisteredEmail(email: String): Boolean {
        return repository.isWebRegisteredEmail(email)
    }

    fun addCustomWebEmail(email: String) {
        repository.addCustomWebEmail(email)
        _userMessage.value = "Web account $email registered in mobile directory"
    }

    fun getKnownWebAccounts(): List<String> {
        return repository.getKnownWebAccounts()
    }

    suspend fun syncWebAccountDirectly(email: String, fullName: String = ""): UserEntity {
        val user = repository.syncWebAccountDirectly(email, fullName)
        _userMessage.value = "Web account for ${user.email} synced successfully"
        return user
    }

    suspend fun pingBackendServer(): String {
        return repository.pingBackendServer()
    }

    suspend fun syncAllUsersToCloud(): Int {
        val count = repository.syncAllUsersToCloud()
        _userMessage.value = "$count accounts synced to Firebase Firestore Cloud"
        return count
    }

    fun navigateToLoginWithEmail(email: String) {
        _prefilledLoginEmail.value = email
        _navigationStack.clear()
        _navigationStack.add(Screen.Login)
        _currentScreen.value = Screen.Login
    }
}
