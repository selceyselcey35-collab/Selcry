package com.example.data.repository

import android.content.Context
import com.example.data.local.ChronosDatabase
import com.example.data.local.ComparisonWatchEntity
import com.example.data.local.DropshipEntity
import com.example.data.local.FavoriteWatchEntity
import com.example.data.local.ReturnClaimEntity
import com.example.data.local.ReviewEntity
import com.example.data.model.DropshipListing
import com.example.data.model.ExpertiseCheckItem
import com.example.data.model.ExpertiseReport
import com.example.data.model.LiveChatMessage
import com.example.data.model.LuxuryWatch
import com.example.data.model.OfficialInvoice
import com.example.data.model.ReturnClaim
import com.example.data.model.WatchReview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class MarketplaceRepository(private val context: Context) {
    private val database = ChronosDatabase.getDatabase(context)
    private val dao = database.watchDao()

    // Base Luxury Watch Catalog
    val staticCatalog = listOf(
        LuxuryWatch(
            id = "watch_rolex_daytona",
            brand = "Rolex",
            modelName = "Cosmograph Daytona 'Paul Newman'",
            referenceNumber = "116508-0013",
            priceTry = 1850000L,
            priceUsd = 52000L,
            year = 2024,
            condition = "Sıfır / Jelatinli",
            caseMaterial = "18K Sarı Altın",
            diameter = "40 mm",
            dialColor = "Zümrüt Yeşili & Altın İndeksler",
            movement = "Rolex Kalibre 4130 (Otomatik Kronograf)",
            powerReserve = "72 Saat",
            waterResistance = "100 m / 330 ft",
            boxAndPapers = true,
            authenticityScore = 99.8,
            timegrapherDeviation = "+0.5 sn/gün (Genlik: 315°)",
            sellerName = "Chronos Cenevre Vaults",
            sellerRating = 4.98,
            location = "Cenevre / İsviçre",
            imageResName = "watch_rolex_daytona",
            wholesalePriceTry = 1620000L
        ),
        LuxuryWatch(
            id = "watch_patek_nautilus",
            brand = "Patek Philippe",
            modelName = "Nautilus Rose Gold",
            referenceNumber = "5711/1R-001",
            priceTry = 4200000L,
            priceUsd = 118000L,
            year = 2023,
            condition = "Mükemmel (Mint Koleksiyon)",
            caseMaterial = "18K Pembe Altın",
            diameter = "40 mm",
            dialColor = "Çikolata Kahve Degradeli",
            movement = "Patek Manüfaktür Calibre 324 S C",
            powerReserve = "45 Saat",
            waterResistance = "120 m",
            boxAndPapers = true,
            authenticityScore = 99.9,
            timegrapherDeviation = "+0.8 sn/gün (Genlik: 308°)",
            sellerName = "Atelier Horloger Mayfair",
            sellerRating = 5.0,
            location = "Londra / Birleşik Krallık",
            imageResName = "watch_patek_nautilus",
            wholesalePriceTry = 3780000L
        ),
        LuxuryWatch(
            id = "watch_ap_royaloak",
            brand = "Audemars Piguet",
            modelName = "Royal Oak Double Balance Wheel Openworked",
            referenceNumber = "15407OR.OO.1220OR.01",
            priceTry = 3650000L,
            priceUsd = 102000L,
            year = 2024,
            condition = "Sıfır / Sertifikalı",
            caseMaterial = "18K Pembe Altın İskelet",
            diameter = "41 mm",
            dialColor = "Açık İskelet / Rodyum & Altın Mekanizma",
            movement = "Audemars Piguet Kalibre 3132",
            powerReserve = "45 Saat",
            waterResistance = "50 m",
            boxAndPapers = true,
            authenticityScore = 99.7,
            timegrapherDeviation = "+0.4 sn/gün (Genlik: 312°)",
            sellerName = "Chronos BAE Vault",
            sellerRating = 4.95,
            location = "Dubai / BAE",
            imageResName = "watch_ap_royaloak",
            wholesalePriceTry = 3280000L
        ),
        LuxuryWatch(
            id = "watch_vacheron_blue",
            brand = "Vacheron Constantin",
            modelName = "Overseas Dual Time GMT",
            referenceNumber = "7900V/110R-B359",
            priceTry = 2150000L,
            priceUsd = 60500L,
            year = 2024,
            condition = "Kusursuz / 3 Kordon Seti",
            caseMaterial = "18K Pembe Altın",
            diameter = "41 mm",
            dialColor = "Işıltılı Gece Mavisi",
            movement = "Vacheron Kalibre 5110 DT (Hallmark of Geneva)",
            powerReserve = "60 Saat",
            waterResistance = "150 m",
            boxAndPapers = true,
            authenticityScore = 99.6,
            timegrapherDeviation = "+1.1 sn/gün (Genlik: 304°)",
            sellerName = "Kapalıçarşı Elit Kasa",
            sellerRating = 4.92,
            location = "İstanbul / Türkiye",
            imageResName = "watch_vacheron_blue",
            wholesalePriceTry = 1910000L
        )
    )

    // Observable Watches combining static info with Room favorite states
    fun getWatchesFlow(): Flow<List<LuxuryWatch>> {
        return dao.getAllFavoriteIds().map { favoriteIds ->
            val set = favoriteIds.toSet()
            staticCatalog.map { watch ->
                watch.copy(isFavorite = set.contains(watch.id))
            }
        }
    }

    suspend fun toggleFavorite(watchId: String, isCurrentlyFavorite: Boolean) {
        if (isCurrentlyFavorite) {
            dao.removeFavorite(watchId)
        } else {
            dao.addFavorite(FavoriteWatchEntity(watchId))
        }
    }

    // Comparison List
    fun getComparisonWatchesFlow(): Flow<List<LuxuryWatch>> {
        return dao.getAllComparisonIds().map { comparisonIds ->
            val set = comparisonIds.toSet()
            staticCatalog.filter { set.contains(it.id) }
        }
    }

    suspend fun addToComparison(watchId: String) {
        dao.addComparison(ComparisonWatchEntity(watchId))
    }

    suspend fun removeFromComparison(watchId: String) {
        dao.removeComparison(watchId)
    }

    suspend fun clearComparisons() {
        dao.clearComparisons()
    }

    // Dropshipping Store Listings Flow
    fun getDropshipListingsFlow(): Flow<List<DropshipListing>> {
        return dao.getAllDropshipItems().map { entities ->
            entities.mapNotNull { entity ->
                val watch = staticCatalog.find { it.id == entity.watchId } ?: return@mapNotNull null
                DropshipListing(
                    watchId = entity.watchId,
                    watch = watch,
                    yourCustomSellingPrice = entity.customPriceTry,
                    estimatedProfit = entity.profitMarginTry,
                    supplierDepot = entity.supplierDepot,
                    autoFulfillmentActive = entity.isLiveInStore,
                    totalSalesCount = entity.salesCount
                )
            }
        }
    }

    suspend fun addOrUpdateDropshipItem(
        watchId: String,
        customPriceTry: Long,
        supplierDepot: String
    ) {
        val watch = staticCatalog.find { it.id == watchId } ?: return
        val profit = customPriceTry - watch.wholesalePriceTry
        dao.saveDropshipItem(
            DropshipEntity(
                watchId = watchId,
                customPriceTry = customPriceTry,
                profitMarginTry = profit,
                supplierDepot = supplierDepot,
                salesCount = (1..14).random(),
                isLiveInStore = true
            )
        )
    }

    suspend fun removeDropshipItem(watchId: String) {
        dao.removeDropshipItem(watchId)
    }

    // Return Claims
    fun getReturnClaimsFlow(): Flow<List<ReturnClaim>> {
        return dao.getAllReturnClaims().map { list ->
            list.map {
                ReturnClaim(
                    claimId = it.claimId,
                    orderId = it.orderId,
                    watchTitle = it.watchTitle,
                    returnReason = it.returnReason,
                    autoApproved = it.autoApproved,
                    statusText = it.statusText,
                    armoredCourierTrackingCode = it.courierCode,
                    escrowRefundAmount = it.refundAmount,
                    createdAt = it.createdAt
                )
            }
        }
    }

    suspend fun submitReturnRequest(
        watch: LuxuryWatch,
        reason: String
    ): ReturnClaim {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val now = dateFormat.format(Date())
        val claimId = "RET-" + UUID.randomUUID().toString().take(8).uppercase()
        val courierCode = "BRINKS-SEC-" + (100000..999999).random()

        // 14-day legal consumer protection + authenticity guarantee automatic approval
        val isAutoApproved = true
        val statusText = "Otomatik Onaylandı • Sigortalı Zırhlı Kurye Tahsis Edildi"

        val entity = ReturnClaimEntity(
            claimId = claimId,
            orderId = "ORD-" + UUID.randomUUID().toString().take(6).uppercase(),
            watchTitle = "${watch.brand} ${watch.modelName}",
            returnReason = reason,
            autoApproved = isAutoApproved,
            statusText = statusText,
            courierCode = courierCode,
            refundAmount = watch.priceTry,
            createdAt = now
        )
        dao.insertReturnClaim(entity)

        return ReturnClaim(
            claimId = entity.claimId,
            orderId = entity.orderId,
            watchTitle = entity.watchTitle,
            returnReason = entity.returnReason,
            autoApproved = entity.autoApproved,
            statusText = entity.statusText,
            armoredCourierTrackingCode = entity.courierCode,
            escrowRefundAmount = entity.refundAmount,
            createdAt = entity.createdAt
        )
    }

    // Official E-Invoice & Tax Engine
    fun generateOfficialInvoice(
        watch: LuxuryWatch,
        buyerFullName: String,
        buyerIdOrTaxNo: String,
        buyerAddress: String
    ): OfficialInvoice {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val issueDate = dateFormat.format(Date())
        val invoiceNo = "CHR2026" + (10000000..99999999).random()
        val ettn = UUID.randomUUID().toString()

        val vatRate = 0.20 // %20 KDV
        val subtotal = (watch.priceTry / (1 + vatRate)).toLong()
        val vatAmount = watch.priceTry - subtotal
        val luxuryExcise = (watch.priceTry * 0.05).toLong() // %5 lüks harç simülasyonu

        return OfficialInvoice(
            invoiceNumber = invoiceNo,
            ettNumber = ettn,
            buyerTaxOrId = buyerIdOrTaxNo,
            buyerFullName = buyerFullName,
            buyerAddress = buyerAddress,
            issueDate = issueDate,
            subtotalTry = subtotal,
            vatRate = vatRate,
            vatAmountTry = vatAmount,
            luxuryExciseTaxTry = luxuryExcise,
            totalAmountTry = watch.priceTry,
            isEArchive = true,
            gibApprovalCode = "GIB-APPROVED-SIGNATURE-SHA256-VALIDATED"
        )
    }

    // Detailed Expertise verification report
    fun getExpertiseReportForWatch(watch: LuxuryWatch): ExpertiseReport {
        return ExpertiseReport(
            watchId = watch.id,
            serialNumber = "SN-" + watch.referenceNumber.replace("-", "") + "-89201",
            timegrapherRate = watch.timegrapherDeviation,
            amplitude = "315° (Optimal Aralık: 280°-320°)",
            beatError = "0.1 ms (İsviçre Manüfaktür Standart)",
            dialAuthenticity = "Doğrulandı: Mikroskopik Font & Lazer Taç İncelemesi %100 Orijinal",
            caseIntegrity = "Polisajsız orijinal fabrika açılı fırçalama, 18K altın ayar damgası teyitli",
            movementOriginality = "Tüm vidalar ve çarklar orijinal parça, servis geçmişi kayıtlı",
            interpolTheftCheckPassed = true,
            certifiedMasterHorologist = "Jean-Marc Laurent (Swiss Watchmakers Guild #4829)",
            certificationDate = "08.09.2026",
            qrCodePayload = "https://chronos.luxury/verify/cert/${watch.blockchainCertificateHash}"
        )
    }

    fun getExpertiseChecklist(): List<ExpertiseCheckItem> {
        return listOf(
            ExpertiseCheckItem(
                title = "Interpol & Global Kayıp/Çalıntı Taraması",
                subtitle = "Veritabanı: Watch Register & Interpol Stolen Goods",
                isVerified = true,
                details = "Temiz kayıt. Hiçbir rehin, haciz veya çalıntı ihbarı bulunmamaktadır."
            ),
            ExpertiseCheckItem(
                title = "Mekanizma & Zamanlama Sapma Analizi (Timegrapher)",
                subtitle = "6 Pozisyonda İsviçre Witschi Test Cihazı ile Ölçüm",
                isVerified = true,
                details = "Günlük sapma +0.5s ile COSC kronometre toleranslarının mükemmel derecede içindedir."
            ),
            ExpertiseCheckItem(
                title = "Optik Spektrometre & Metal Ayar Doğrulaması",
                subtitle = "XRF Röntgen Spektrometre Analizi",
                isVerified = true,
                details = "Kasa ve kordon 750‰ (18 Karat) altın alaşım saflığında tasdik edilmiştir."
            ),
            ExpertiseCheckItem(
                title = "Kadran, İbre & Lazer Güvenlik Hologramı",
                subtitle = "100x Elektron Mikroskobu İncelemesi",
                isVerified = true,
                details = "Safir kristal saat 6 konumundaki mikron lazer gravür taç ve indeks baskıları orijinaldir."
            ),
            ExpertiseCheckItem(
                title = "Kriptografik Blokzincir Mühür (E2EE Sertifika)",
                subtitle = "Ethereum Layer-2 Dijital Orijinallik Pasaportu",
                isVerified = true,
                details = "Akıllı sözleşme üzerine mühürlenmiş devredilebilir dijital mülkiyet sertifikası."
            )
        )
    }

    // Curated Seed Reviews from Verified Luxury Watch Collectors
    private val initialSeedReviews: List<WatchReview> = listOf(
        WatchReview(
            id = "seed_rev_1",
            watchId = "watch_rolex_daytona",
            userName = "Murat K.",
            userBadge = "Kıdemli Koleksiyoner",
            rating = 5,
            date = "10 Eylül 2026",
            title = "Kondisyon Harika, Witschi Sapması Mükemmel",
            comment = "18K sarı altın kasanın ve yeşil kadranın ışık altındaki ton geçişi büyüleyici. Witschi cihazında test edildiğinde tam +0.5s sapma verdi. Brinks zırhlı kurye ile 24 saat içinde teslim aldım.",
            isVerifiedPurchase = true,
            helpfulCount = 14
        ),
        WatchReview(
            id = "seed_rev_2",
            watchId = "watch_rolex_daytona",
            userName = "Canberk T.",
            userBadge = "Doğrulanmış Alıcı",
            rating = 5,
            date = "28 Ağustos 2026",
            title = "E-Fatura & Ekspertiz Tam Takım Geldi",
            comment = "GİB onaylı %20 KDV resmi e-arşiv faturası ve blokzincir sertifikası eşzamanlı ulaştı. Kalibre 4130 kronograf basış hissi ilk günkü gibi pürüzsüz.",
            isVerifiedPurchase = true,
            helpfulCount = 9
        ),
        WatchReview(
            id = "seed_rev_3",
            watchId = "watch_patek_nautilus",
            userName = "Dr. Selim A.",
            userBadge = "VIP Koleksiyoner",
            rating = 5,
            date = "12 Eylül 2026",
            title = "Koleksiyonumun Baş Tacı",
            comment = "Rose gold tonu ve çikolata kadran degradeli yüzeyi olağanüstü. 324 S C kalibre manüfaktür mekanizma kusursuz zaman tutuyor. Cenevre arşiv özeti ile teslim aldım.",
            isVerifiedPurchase = true,
            helpfulCount = 22
        ),
        WatchReview(
            id = "seed_rev_4",
            watchId = "watch_patek_nautilus",
            userName = "Burak V.",
            userBadge = "Doğrulanmış Alıcı",
            rating = 5,
            date = "04 Ağustos 2026",
            title = "Kusursuz Paketleme ve Orijinallik Teyidi",
            comment = "İsviçre Saat Yapımcıları Birliği sertifikası ve mühürlü kutu ile geldi. Chronos canlı destek hattı teslimatın her aşamasında bilgilendirdi.",
            isVerifiedPurchase = true,
            helpfulCount = 11
        ),
        WatchReview(
            id = "seed_rev_5",
            watchId = "watch_ap_royaloak",
            userName = "Sinan Y.",
            userBadge = "Saat Tutkunu",
            rating = 5,
            date = "05 Eylül 2026",
            title = "Çift Balans Açık İskelet Sanat Eseri",
            comment = "Çift balans çarkının rezonans hareketi hipnotize edici. Kasanın açılı fırçalanmış ve polisajlı yüzeyleri sıfır kondisyonda. Ekspertiz skoru %99.7.",
            isVerifiedPurchase = true,
            helpfulCount = 17
        ),
        WatchReview(
            id = "seed_rev_6",
            watchId = "watch_vacheron_blue",
            userName = "Emre D.",
            userBadge = "Doğrulanmış Alıcı",
            rating = 5,
            date = "01 Eylül 2026",
            title = "Cenevre Mührü ve 3 Kordon Seti",
            comment = "Mavi kadranın derinliği fotoğraflardakinden çok daha etkileyici. Deri, kauçuk ve altın bilezik kolayca değişiyor. E-Fatura dakikalar içinde kesildi.",
            isVerifiedPurchase = true,
            helpfulCount = 8
        )
    )

    fun getReviewsForWatchFlow(watchId: String): Flow<List<WatchReview>> {
        return dao.getReviewsForWatch(watchId).map { roomList ->
            val userAddedReviews = roomList.map { entity ->
                WatchReview(
                    id = entity.id,
                    watchId = entity.watchId,
                    userName = entity.userName,
                    userBadge = entity.userBadge,
                    rating = entity.rating,
                    date = entity.date,
                    title = entity.title,
                    comment = entity.comment,
                    isVerifiedPurchase = entity.isVerifiedPurchase,
                    helpfulCount = entity.helpfulCount
                )
            }
            val seeds = initialSeedReviews.filter { it.watchId == watchId }
            userAddedReviews + seeds
        }
    }

    fun getAllReviewsFlow(): Flow<List<WatchReview>> {
        return dao.getAllReviews().map { roomList ->
            val userAdded = roomList.map { entity ->
                WatchReview(
                    id = entity.id,
                    watchId = entity.watchId,
                    userName = entity.userName,
                    userBadge = entity.userBadge,
                    rating = entity.rating,
                    date = entity.date,
                    title = entity.title,
                    comment = entity.comment,
                    isVerifiedPurchase = entity.isVerifiedPurchase,
                    helpfulCount = entity.helpfulCount
                )
            }
            userAdded + initialSeedReviews
        }
    }

    suspend fun addReview(
        watchId: String,
        userName: String,
        rating: Int,
        title: String,
        comment: String
    ): WatchReview {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("tr", "TR"))
        val dateString = dateFormat.format(Date())
        val reviewId = "REV-" + UUID.randomUUID().toString().take(8).uppercase()

        val entity = ReviewEntity(
            id = reviewId,
            watchId = watchId,
            userName = userName.ifBlank { "Koleksiyoner" },
            userBadge = "Doğrulanmış Alıcı",
            rating = rating.coerceIn(1, 5),
            date = dateString,
            title = title.ifBlank { "Koleksiyoner Değerlendirmesi" },
            comment = comment,
            isVerifiedPurchase = true,
            helpfulCount = 1
        )
        dao.insertReview(entity)

        return WatchReview(
            id = entity.id,
            watchId = entity.watchId,
            userName = entity.userName,
            userBadge = entity.userBadge,
            rating = entity.rating,
            date = entity.date,
            title = entity.title,
            comment = entity.comment,
            isVerifiedPurchase = entity.isVerifiedPurchase,
            helpfulCount = entity.helpfulCount
        )
    }

    suspend fun voteHelpful(reviewId: String) {
        dao.incrementHelpful(reviewId)
    }
}
