package com.example.data.model

data class LuxuryWatch(
    val id: String,
    val brand: String,
    val modelName: String,
    val referenceNumber: String,
    val priceTry: Long,
    val priceUsd: Long,
    val year: Int,
    val condition: String, // "Sıfır / Kutulu", "Mükemmel (Mint)", "Koleksiyonluk"
    val caseMaterial: String, // "18K Sarı Altın", "18K Pembe Altın", "Platin & Titanyum"
    val diameter: String, // "40 mm", "41 mm", "42 mm"
    val dialColor: String,
    val movement: String, // "Otomatik Kalibre 4130", "Otomatik Kalibre 324 S C"
    val powerReserve: String, // "72 Saat", "45 Saat", "60 Saat"
    val waterResistance: String, // "100 m / 330 ft", "50 m"
    val boxAndPapers: Boolean,
    val authenticityScore: Double, // 99.4
    val timegrapherDeviation: String, // "+1 sn/gün (Genlik: 312°)"
    val sellerName: String,
    val sellerRating: Double,
    val location: String, // "Cenevre / İsviçre", "İstanbul / Kapalıçarşı", "Dubai / BAE"
    val imageResName: String,
    val isFavorite: Boolean = false,
    val isDropshipAvailable: Boolean = true,
    val wholesalePriceTry: Long = (priceTry * 0.85).toLong(), // Dropshipping tedarik maliyeti
    val blockchainCertificateHash: String = "0x" + id.hashCode().toUInt().toString(16).padStart(8, '0') + "98e4afb201"
)

data class ExpertiseCheckItem(
    val title: String,
    val subtitle: String,
    val isVerified: Boolean,
    val details: String
)

data class ExpertiseReport(
    val watchId: String,
    val serialNumber: String,
    val timegrapherRate: String,
    val amplitude: String,
    val beatError: String,
    val dialAuthenticity: String,
    val caseIntegrity: String,
    val movementOriginality: String,
    val interpolTheftCheckPassed: Boolean,
    val certifiedMasterHorologist: String,
    val certificationDate: String,
    val qrCodePayload: String
)

data class DropshipListing(
    val watchId: String,
    val watch: LuxuryWatch,
    val yourCustomSellingPrice: Long,
    val estimatedProfit: Long,
    val supplierDepot: String,
    val autoFulfillmentActive: Boolean,
    val totalSalesCount: Int
)

data class OfficialInvoice(
    val invoiceNumber: String,
    val ettNumber: String, // Universal unique id
    val buyerTaxOrId: String,
    val buyerFullName: String,
    val buyerAddress: String,
    val issueDate: String,
    val subtotalTry: Long,
    val vatRate: Double, // 0.20
    val vatAmountTry: Long,
    val luxuryExciseTaxTry: Long,
    val totalAmountTry: Long,
    val isEArchive: Boolean = true,
    val gibApprovalCode: String
)

enum class PaymentProvider(val displayName: String, val type: String, val iconName: String) {
    // Domestic
    TROY("Troy Yerli Kart Sistemi", "Yurt İçi", "troy"),
    BKM_EXPRESS("BKM Express", "Yurt İçi", "bkm"),
    TURKISH_BANK_3D("Türk Bankaları (3D Secure POS)", "Yurt İçi", "card"),
    FAST_HAVALE("FAST / Havale & EFT (Emanet Havuz)", "Yurt İçi", "bank"),

    // International
    STRIPE_GLOBAL("Stripe Global (Visa / MC / Amex)", "Yurt Dışı", "stripe"),
    SWIFT_INTERNATIONAL("SWIFT Uluslararası Transfer (USD/EUR)", "Yurt Dışı", "swift"),
    APPLE_PAY("Apple Pay / Google Pay", "Yurt Dışı", "apple"),
    CRYPTO_ESCROW("Multi-Sig Kripto Emanet (USDT / BTC)", "Kripto / Global", "crypto")
}

data class ReturnClaim(
    val claimId: String,
    val orderId: String,
    val watchTitle: String,
    val returnReason: String,
    val autoApproved: Boolean,
    val statusText: String,
    val armoredCourierTrackingCode: String,
    val escrowRefundAmount: Long,
    val createdAt: String
)

data class LiveChatMessage(
    val id: String,
    val sender: String, // "User" or "Concierge"
    val message: String,
    val timestamp: String,
    val isEncrypted: Boolean = true
)

data class WatchReview(
    val id: String,
    val watchId: String,
    val userName: String,
    val userBadge: String = "Doğrulanmış Koleksiyoner",
    val rating: Int = 5,
    val date: String,
    val title: String,
    val comment: String,
    val isVerifiedPurchase: Boolean = true,
    val helpfulCount: Int = 0
)

