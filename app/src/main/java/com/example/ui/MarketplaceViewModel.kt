package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.DropshipListing
import com.example.data.model.ExpertiseCheckItem
import com.example.data.model.ExpertiseReport
import com.example.data.model.LiveChatMessage
import com.example.data.model.LuxuryWatch
import com.example.data.model.OfficialInvoice
import com.example.data.model.PaymentProvider
import com.example.data.model.ReturnClaim
import com.example.data.model.WatchReview
import com.example.data.repository.MarketplaceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

enum class AppDestination {
    MARKETPLACE,
    WATCH_DETAIL,
    COMPARISON,
    EXPERTISE_PORTAL,
    DROPSHIPPING_HUB,
    CHECKOUT_INVOICE,
    AUTOMATED_RETURNS,
    LIVE_CONCIERGE
}

class MarketplaceViewModel(application: Application) : AndroidViewModel(application) {
    val repository = MarketplaceRepository(application.applicationContext)

    // Navigation State
    private val _currentDestination = MutableStateFlow(AppDestination.MARKETPLACE)
    val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

    // Selected Watch for Detail View
    private val _selectedWatch = MutableStateFlow<LuxuryWatch?>(null)
    val selectedWatch: StateFlow<LuxuryWatch?> = _selectedWatch.asStateFlow()

    // Search and Brand Filter
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedBrandFilter = MutableStateFlow("Tümü")
    val selectedBrandFilter: StateFlow<String> = _selectedBrandFilter.asStateFlow()

    // Flow of Watches with live favorites
    val watchesList: StateFlow<List<LuxuryWatch>> = repository.getWatchesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.staticCatalog)

    // Flow of Compared Watches
    val comparisonWatches: StateFlow<List<LuxuryWatch>> = repository.getComparisonWatchesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Flow of Dropship Listings
    val dropshipListings: StateFlow<List<DropshipListing>> = repository.getDropshipListingsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Flow of Return Claims
    val returnClaims: StateFlow<List<ReturnClaim>> = repository.getReturnClaimsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All Reviews flow
    val allReviews: StateFlow<List<WatchReview>> = repository.getAllReviewsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Flow of Reviews for the Selected Watch
    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedWatchReviews: StateFlow<List<WatchReview>> = _selectedWatch
        .flatMapLatest { watch ->
            if (watch != null) repository.getReviewsForWatchFlow(watch.id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Review Submission Success Feedback
    private val _reviewSubmittedFeedback = MutableStateFlow<String?>(null)
    val reviewSubmittedFeedback: StateFlow<String?> = _reviewSubmittedFeedback.asStateFlow()

    // Legal Policy Modal State
    private val _hasAcceptedPolicies = MutableStateFlow(false)
    val hasAcceptedPolicies: StateFlow<Boolean> = _hasAcceptedPolicies.asStateFlow()

    private val _showPolicyModal = MutableStateFlow(true) // Shown upon opening
    val showPolicyModal: StateFlow<Boolean> = _showPolicyModal.asStateFlow()

    // Generated Invoice
    private val _currentInvoice = MutableStateFlow<OfficialInvoice?>(null)
    val currentInvoice: StateFlow<OfficialInvoice?> = _currentInvoice.asStateFlow()

    // Selected Payment Method
    private val _selectedPaymentProvider = MutableStateFlow(PaymentProvider.TURKISH_BANK_3D)
    val selectedPaymentProvider: StateFlow<PaymentProvider> = _selectedPaymentProvider.asStateFlow()

    // Live Concierge Chat
    private val _chatMessages = MutableStateFlow(
        listOf(
            LiveChatMessage(
                id = "1",
                sender = "Concierge",
                message = "Hoş geldiniz Sayın Koleksiyoner. Chronos VIP Canlı Destek hattına bağlandınız. Tüm yazışmalarınız 256-bit AES uçtan uca şifreleme ile korunmaktadır. Size saat ekspertizi, uluslararası transferler veya özel konsinye tedariği konusunda nasıl yardımcı olabilirim?",
                timestamp = "Şimdi",
                isEncrypted = true
            )
        )
    )
    val chatMessages: StateFlow<List<LiveChatMessage>> = _chatMessages.asStateFlow()

    // Expertise Submission Form State
    private val _sellerRefNumberInput = MutableStateFlow("")
    val sellerRefNumberInput: StateFlow<String> = _sellerRefNumberInput.asStateFlow()

    private val _sellerSerialInput = MutableStateFlow("")
    val sellerSerialInput: StateFlow<String> = _sellerSerialInput.asStateFlow()

    private val _verificationSuccessNotice = MutableStateFlow<String?>(null)
    val verificationSuccessNotice: StateFlow<String?> = _verificationSuccessNotice.asStateFlow()

    // Navigation Actions
    fun navigateTo(destination: AppDestination) {
        _currentDestination.value = destination
    }

    fun openWatchDetail(watch: LuxuryWatch) {
        _selectedWatch.value = watch
        _currentDestination.value = AppDestination.WATCH_DETAIL
    }

    fun nextWatch() {
        val currentList = watchesList.value
        val index = currentList.indexOfFirst { it.id == _selectedWatch.value?.id }
        if (index != -1 && index < currentList.size - 1) {
            _selectedWatch.value = currentList[index + 1]
        } else if (currentList.isNotEmpty()) {
            _selectedWatch.value = currentList.first()
        }
    }

    fun previousWatch() {
        val currentList = watchesList.value
        val index = currentList.indexOfFirst { it.id == _selectedWatch.value?.id }
        if (index > 0) {
            _selectedWatch.value = currentList[index - 1]
        } else if (currentList.isNotEmpty()) {
            _selectedWatch.value = currentList.last()
        }
    }

    // Search and Filter
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectBrandFilter(brand: String) {
        _selectedBrandFilter.value = brand
    }

    // Favorite Toggle
    fun toggleFavorite(watch: LuxuryWatch) {
        viewModelScope.launch {
            repository.toggleFavorite(watch.id, watch.isFavorite)
        }
    }

    // Comparison Actions
    fun toggleComparison(watch: LuxuryWatch) {
        viewModelScope.launch {
            val isAlreadyIn = comparisonWatches.value.any { it.id == watch.id }
            if (isAlreadyIn) {
                repository.removeFromComparison(watch.id)
            } else {
                repository.addToComparison(watch.id)
            }
        }
    }

    fun clearAllComparisons() {
        viewModelScope.launch {
            repository.clearComparisons()
        }
    }

    // Legal Policy Modal
    fun acceptPolicies() {
        _hasAcceptedPolicies.value = true
        _showPolicyModal.value = false
    }

    fun openPolicyModal() {
        _showPolicyModal.value = true
    }

    fun closePolicyModal() {
        _showPolicyModal.value = false
    }

    // Dropshipping Action
    fun addWatchToDropshipBoutique(watch: LuxuryWatch, customSellingPrice: Long, depot: String) {
        viewModelScope.launch {
            repository.addOrUpdateDropshipItem(
                watchId = watch.id,
                customPriceTry = customSellingPrice,
                supplierDepot = depot
            )
            _currentDestination.value = AppDestination.DROPSHIPPING_HUB
        }
    }

    fun removeDropshipItem(watchId: String) {
        viewModelScope.launch {
            repository.removeDropshipItem(watchId)
        }
    }

    // Checkout & Invoice
    fun prepareCheckout(watch: LuxuryWatch) {
        _selectedWatch.value = watch
        val invoice = repository.generateOfficialInvoice(
            watch = watch,
            buyerFullName = "Ahmet Yılmaz",
            buyerIdOrTaxNo = "12345678901",
            buyerAddress = "Bebek Mah. Cevdetpaşa Cad. No: 42, Beşiktaş / İstanbul"
        )
        _currentInvoice.value = invoice
        _currentDestination.value = AppDestination.CHECKOUT_INVOICE
    }

    fun setPaymentProvider(provider: PaymentProvider) {
        _selectedPaymentProvider.value = provider
    }

    // Automated Return Claim
    fun createAutoApprovedReturn(watch: LuxuryWatch, reason: String) {
        viewModelScope.launch {
            val claim = repository.submitReturnRequest(watch, reason)
            _currentDestination.value = AppDestination.AUTOMATED_RETURNS
        }
    }

    // Live Concierge Chat Actions
    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = LiveChatMessage(
            id = UUID.randomUUID().toString(),
            sender = "User",
            message = text,
            timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            isEncrypted = true
        )
        _chatMessages.value = _chatMessages.value + userMsg

        // Smart luxury concierge response
        viewModelScope.launch {
            kotlinx.coroutines.delay(800)
            val replyText = when {
                text.contains("iade", ignoreCase = true) ->
                    "Sayın Müşterimiz, 14 gün yasal koşulsuz iade hakkınız kapsamında Chronos otomatik onay algoritması devrededir. 'İade Talebi' ekranından talep oluşturduğunuz an Brinks Zırhlı Kurye çağrısı otomatik açılır ve emanet tutarınız güvence altına alınır."
                text.contains("ekspertiz", ignoreCase = true) || text.contains("orijinal", ignoreCase = true) ->
                    "Platformumuzdaki her saat 100x mikroskop, XRF spektrometre ve Witschi Timegrapher testlerinden geçirilerek İsviçre Saat Yapımcıları Birliği sertifikası ve blokzincir mühür ile teslim edilmektedir."
                text.contains("ödeme", ignoreCase = true) || text.contains("taksit", ignoreCase = true) || text.contains("swift", ignoreCase = true) ->
                    "Yurt içinde Troy ve tüm bankalarla 3D Secure güvenceli ödeme, yurt dışında ise Stripe, SWIFT ve Multi-Sig Kripto Escrow desteklenmektedir. Ödemeniz siz teslimatı onaylayana dek emanet havuzunda bloke tutulur."
                text.contains("dropship", ignoreCase = true) || text.contains("stok", ignoreCase = true) ->
                    "Chronos Dropshipping altyapısıyla Cenevre, Dubai ve Kapalıçarşı yetkili kasalarındaki saatleri sermayesiz ve sıfır stok riskiyle kendi kâr marjınızla listeleyebilir, sipariş geldiğinde otomatik zırhlı kargoyla müşterinize ulaştırabilirsiniz."
                else ->
                    "Talebiniz kaydedildi. Özel koleksiyon uzmanımız detayları inceleyip şifreli kanalınız üzerinden size anlık bilgi aktaracaktır."
            }
            val botMsg = LiveChatMessage(
                id = UUID.randomUUID().toString(),
                sender = "Concierge",
                message = replyText,
                timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                isEncrypted = true
            )
            _chatMessages.value = _chatMessages.value + botMsg
        }
    }

    // Expertise Submission
    fun updateSellerInputs(ref: String, serial: String) {
        _sellerRefNumberInput.value = ref
        _sellerSerialInput.value = serial
    }

    fun submitWatchForVerification() {
        if (_sellerRefNumberInput.value.isBlank()) return
        _verificationSuccessNotice.value =
            "Ekspertiz Başvurusu Alındı: ${_sellerRefNumberInput.value} referanslı saat için Witschi Timegrapher ve Mikroskop kontrol randevusu oluşturuldu. Seri No veritabanı taraması %100 temiz çıktı."
    }

    fun clearNotice() {
        _verificationSuccessNotice.value = null
    }

    // User Reviews & Collector Ratings
    fun addUserReview(
        watchId: String,
        userName: String,
        rating: Int,
        title: String,
        comment: String
    ) {
        if (comment.isBlank()) return
        viewModelScope.launch {
            repository.addReview(
                watchId = watchId,
                userName = userName,
                rating = rating,
                title = title,
                comment = comment
            )
            _reviewSubmittedFeedback.value = "Değerlendirmeniz başarıyla yayınlandı. Chronos topluluğuna katkınız için teşekkürler."
        }
    }

    fun voteReviewHelpful(reviewId: String) {
        viewModelScope.launch {
            repository.voteHelpful(reviewId)
        }
    }

    fun clearReviewFeedback() {
        _reviewSubmittedFeedback.value = null
    }
}
