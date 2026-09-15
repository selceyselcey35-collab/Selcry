package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.LuxuryWatch
import com.example.data.model.WatchReview
import com.example.ui.AppDestination
import com.example.ui.MarketplaceViewModel
import com.example.ui.components.GoldBadge
import com.example.ui.components.SocialShareSheet
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun WatchDetailScreen(
    watch: LuxuryWatch,
    viewModel: MarketplaceViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val comparisonWatches by viewModel.comparisonWatches.collectAsState()
    val isCompared = comparisonWatches.any { it.id == watch.id }

    val watches by viewModel.watchesList.collectAsState()
    val currentIndex = watches.indexOfFirst { it.id == watch.id }.coerceAtLeast(0)

    var showShareModal by remember { mutableStateOf(false) }
    var showDropshipDialog by remember { mutableStateOf(false) }
    var showReturnDialog by remember { mutableStateOf(false) }
    var showAddReviewDialog by remember { mutableStateOf(false) }

    val reviews by viewModel.selectedWatchReviews.collectAsState()
    val reviewFeedback by viewModel.reviewSubmittedFeedback.collectAsState()

    val imageResId = remember(watch.imageResName) {
        val res = context.resources.getIdentifier(watch.imageResName, "drawable", context.packageName)
        if (res != 0) res else R.drawable.ic_chronos_logo
    }

    val heartColor by animateColorAsState(
        targetValue = if (watch.isFavorite) CrimsonAlert else TextMuted,
        label = "heart_anim_detail"
    )

    Scaffold(
        containerColor = ObsidianBlack,
        bottomBar = {
            // Floating Luxury Action Bar
            Surface(
                color = ObsidianSurface,
                border = BorderStroke(1.dp, ImperialGoldBorder),
                tonalElevation = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${String.format("%,d", watch.priceTry)} ₺",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ImperialGoldLight
                        )
                        Text(
                            text = "≈ $${String.format("%,d", watch.priceUsd)} • Resmi E-Fatura",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Compare Toggle
                        OutlinedButton(
                            onClick = { viewModel.toggleComparison(watch) },
                            border = BorderStroke(1.dp, if (isCompared) ImperialGold else ObsidianStroke),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isCompared) ImperialGoldContainer else Color.Transparent
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = if (isCompared) Icons.Default.Check else Icons.Default.CompareArrows,
                                contentDescription = null,
                                tint = ImperialGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isCompared) "Kıyasta" else "Kıyasla", color = TextPureWhite, fontSize = 12.sp)
                        }

                        // Buy with Official Invoice
                        Button(
                            onClick = { viewModel.prepareCheckout(watch) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ImperialGold,
                                contentColor = ObsidianBlack
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Güvenli Satın Al", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Quick Navigation & Switcher Header (Ürünler Arası Kolay Geçiş)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ObsidianSurface)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri", tint = ImperialGold)
                    }

                    // Easy Next / Previous Watch switcher
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(ObsidianBlack, RoundedCornerShape(20.dp))
                            .border(1.dp, ObsidianStroke, RoundedCornerShape(20.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        IconButton(
                            onClick = { viewModel.previousWatch() },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.ChevronLeft, contentDescription = "Önceki Saat", tint = ImperialGold)
                        }

                        Text(
                            text = "${currentIndex + 1} / ${watches.size}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ImperialGoldLight,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )

                        IconButton(
                            onClick = { viewModel.nextWatch() },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.ChevronRight, contentDescription = "Sonraki Saat", tint = ImperialGold)
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Share
                        IconButton(onClick = { showShareModal = true }) {
                            Icon(Icons.Default.Share, contentDescription = "Paylaş", tint = ImperialGold)
                        }

                        // Favorite / Like
                        IconButton(onClick = { viewModel.toggleFavorite(watch) }) {
                            Icon(
                                imageVector = if (watch.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Beğen",
                                tint = heartColor
                            )
                        }
                    }
                }
            }

            // High-Resolution Watch Visual Showcase
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                ) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = watch.modelName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        Color.Transparent,
                                        ObsidianBlack
                                    )
                                )
                            )
                    )

                    // Overlay Badges
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        GoldBadge(
                            text = "SWISS GUILD DOĞRULANMIŞ EKSPERTİZ",
                            icon = Icons.Default.Verified
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = watch.brand.uppercase(),
                            color = ImperialGold,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = watch.modelName,
                            color = TextPureWhite,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Referans: ${watch.referenceNumber} • Üretim Yılı: ${watch.year}",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Quick Action Buttons (Dropship, Comparison, Return, Live Support)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Dropship Button
                    Button(
                        onClick = { showDropshipDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ObsidianSurfaceVariant,
                            contentColor = ImperialGold
                        ),
                        border = BorderStroke(1.dp, ImperialGoldBorder),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Dropship Ekle", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    // Return Button
                    Button(
                        onClick = { showReturnDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ObsidianSurfaceVariant,
                            contentColor = TextPureWhite
                        ),
                        border = BorderStroke(1.dp, ObsidianStroke),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.AssignmentReturn, contentDescription = null, tint = CrimsonAlert, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("İade Başlat", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }

                    // Live Concierge
                    Button(
                        onClick = { viewModel.navigateTo(AppDestination.LIVE_CONCIERGE) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ObsidianSurfaceVariant,
                            contentColor = ImperialGoldLight
                        ),
                        border = BorderStroke(1.dp, ObsidianStroke),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.SupportAgent, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("VIP Destek", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Expertise & Authenticity Card (Ekspertiz ve Doğrulama)
            item {
                Surface(
                    color = ObsidianSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, ImperialGoldBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = ImperialGold, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Ekspertiz Doğrulama Raporu",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ImperialGold
                                )
                            }
                            Text(
                                text = "%${watch.authenticityScore} Kusursuz",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldVerified
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        ExpertiseMetricRow("Zamanlama Sapması (Timegrapher)", watch.timegrapherDeviation)
                        ExpertiseMetricRow("Optik Mikroskop / Lazer Taç", "100x Büyütme Orijinal Gravür")
                        ExpertiseMetricRow("Kayıp/Çalıntı Taraması", "Watch Register & Interpol TEMİZ")
                        ExpertiseMetricRow("Kriptografik Pasaport", watch.blockchainCertificateHash)
                        ExpertiseMetricRow("Sertifikalandıran Usta", "Jean-Marc Laurent (Swiss Guild)")

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = { viewModel.navigateTo(AppDestination.EXPERTISE_PORTAL) },
                            border = BorderStroke(0.8.dp, ImperialGold),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Detaylı Ekspertiz Laboratuvar Panelini Aç", color = ImperialGold, fontSize = 11.sp)
                        }
                    }
                }
            }

            // Technical Specifications Table
            item {
                Surface(
                    color = ObsidianSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, ObsidianStroke),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Teknik Özellikler & Manüfaktür",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = ImperialGold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        SpecRow("Kasa Materyali", watch.caseMaterial)
                        SpecRow("Kasa Çapı", watch.diameter)
                        SpecRow("Kadran Rengi", watch.dialColor)
                        SpecRow("Mekanizma & Kalibre", watch.movement)
                        SpecRow("Güç Rezervi", watch.powerReserve)
                        SpecRow("Su Geçirmezlik", watch.waterResistance)
                        SpecRow("Kutu & Evrak", if (watch.boxAndPapers) "Orijinal Kutu ve Garanti Kartı Mevcut" else "Yalnızca Saat")
                        SpecRow("Kondisyon", watch.condition)
                        SpecRow("Satıcı & Konum", "${watch.sellerName} (${watch.location})")
                    }
                }
            }

            // Official Invoice & Tax Compliance Highlight
            item {
                Surface(
                    color = ObsidianSurfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(0.8.dp, ImperialGoldBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = null,
                            tint = ImperialGold,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Maliye Bakanlığı & GİB E-Fatura Uyumlu",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = ImperialGoldLight
                            )
                            Text(
                                text = "Satın alma işleminde %20 KDV dökümü, ETTN tekil kodu ve resmi e-Arşiv fatura doğrudan e-posta adresinize ve GİB portalına iletilir.",
                                fontSize = 11.sp,
                                color = TextMuted,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }

            // User Reviews & Collector Experience Section
            item {
                val averageScore = if (reviews.isEmpty()) 5.0 else reviews.map { it.rating }.average()

                Surface(
                    color = ObsidianSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, ImperialGoldBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = ImperialGold,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Koleksiyoner Yorumları",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ImperialGold
                                )
                            }
                            Surface(
                                color = ImperialGoldContainer,
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(0.5.dp, ImperialGold)
                            ) {
                                Text(
                                    text = "${reviews.size} Değerlendirme",
                                    color = ImperialGoldLight,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Score & CTA Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ObsidianBlack, RoundedCornerShape(10.dp))
                                .border(1.dp, ObsidianStroke, RoundedCornerShape(10.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = String.format(Locale.US, "%.1f", averageScore),
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = ImperialGoldLight
                                    )
                                    Text(
                                        text = " / 5.0",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextMuted,
                                        modifier = Modifier.padding(bottom = 3.dp)
                                    )
                                }
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    for (i in 1..5) {
                                        Icon(
                                            imageVector = if (i <= averageScore.toInt()) Icons.Default.Star else Icons.Outlined.StarBorder,
                                            contentDescription = null,
                                            tint = ImperialGold,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Doğrulanmış Alıcılar",
                                        fontSize = 10.sp,
                                        color = EmeraldVerified,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Button(
                                onClick = { showAddReviewDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ImperialGold,
                                    contentColor = ObsidianBlack
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Yorum Yap", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Feedback notice
                        reviewFeedback?.let { msg ->
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                color = EmeraldVerified.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, EmeraldVerified.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldVerified, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = msg, fontSize = 11.sp, color = TextPureWhite)
                                    }
                                    IconButton(
                                        onClick = { viewModel.clearReviewFeedback() },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Kapat", tint = TextMuted, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Review Cards
                        if (reviews.isEmpty()) {
                            Text(
                                text = "Bu saat için henüz kullanıcı yorumu bulunmuyor. İlk koleksiyon deneyimini siz paylaşın!",
                                fontSize = 12.sp,
                                color = TextMuted,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                reviews.forEach { review ->
                                    ReviewItemCard(
                                        review = review,
                                        onVoteHelpful = { viewModel.voteReviewHelpful(review.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Dropship Add Dialog
    if (showDropshipDialog) {
        var customPriceText by remember { mutableStateOf((watch.priceTry + 150000).toString()) }
        var selectedDepot by remember { mutableStateOf("Cenevre Saat Borsası Deposu") }

        AlertDialog(
            onDismissRequest = { showDropshipDialog = false },
            containerColor = ObsidianSurface,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.border(BorderStroke(1.dp, ImperialGoldBorder), RoundedCornerShape(16.dp)),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Storefront, contentDescription = null, tint = ImperialGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Dropship Vitrinine Ekle", color = ImperialGold, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Bu saati sermayesiz ve sıfır stok riskiyle kendi mağazanızda listeleyin. Müşteriniz sipariş verdiğinde sertifikalı depodan doğrudan müşterinize kargolanır.",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Toptan Konsinye Maliyeti: ${String.format("%,d", watch.wholesalePriceTry)} ₺", fontSize = 12.sp, color = ImperialGoldLight)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = customPriceText,
                        onValueChange = { customPriceText = it },
                        label = { Text("Kendi Satış Fiyatınız (₺)", color = ImperialGold) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ImperialGold,
                            unfocusedBorderColor = ObsidianStroke,
                            focusedTextColor = TextPureWhite,
                            unfocusedTextColor = TextPureWhite
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    val enteredPrice = customPriceText.toLongOrNull() ?: 0L
                    val estProfit = (enteredPrice - watch.wholesalePriceTry).coerceAtLeast(0L)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tahmini Net Kârınız: ${String.format("%,d", estProfit)} ₺",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldVerified
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val enteredPrice = customPriceText.toLongOrNull() ?: watch.priceTry
                        viewModel.addWatchToDropshipBoutique(watch, enteredPrice, selectedDepot)
                        showDropshipDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ImperialGold, contentColor = ObsidianBlack)
                ) {
                    Text("Mağazama Ekle & Yayınla", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDropshipDialog = false }) {
                    Text("İptal", color = TextMuted)
                }
            }
        )
    }

    // Return Request Dialog
    if (showReturnDialog) {
        var returnReason by remember { mutableStateOf("Ekspertiz / Kondisyon Uyuşmazlığı") }

        AlertDialog(
            onDismissRequest = { showReturnDialog = false },
            containerColor = ObsidianSurface,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.border(BorderStroke(1.dp, CrimsonAlert), RoundedCornerShape(16.dp)),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AssignmentReturn, contentDescription = null, tint = CrimsonAlert)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Otomatik Onaylı İade Talebi", color = TextPureWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "14 gün yasal cayma hakkınız ve Chronos Orijinallik Garantisi altındasınız. Talebiniz algoritmik olarak anında incelenir ve otomatik onaylanır.",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("İade Sebebi:", fontSize = 12.sp, color = ImperialGold, fontWeight = FontWeight.Bold)

                    val reasons = listOf(
                        "Ekspertiz / Kondisyon Uyuşmazlığı",
                        "14 Gün Koşulsuz Yasal Cayma Hakkı",
                        "Bilek Boyutu / Ergonomi Tercihi",
                        "Diğer Koleksiyon Değişimi"
                    )

                    reasons.forEach { r ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { returnReason = r }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = returnReason == r,
                                onClick = { returnReason = r },
                                colors = RadioButtonDefaults.colors(selectedColor = ImperialGold)
                            )
                            Text(r, fontSize = 12.sp, color = TextPureWhite)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.createAutoApprovedReturn(watch, returnReason)
                        showReturnDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonAlert, contentColor = TextPureWhite)
                ) {
                    Text("Otomatik Onayla & Zırhlı Kurye Çağır", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { showReturnDialog = false }) {
                    Text("Vazgeç", color = TextMuted)
                }
            }
        )
    }

    // Add Review Dialog
    if (showAddReviewDialog) {
        var selectedRating by remember { mutableStateOf(5) }
        var userNameInput by remember { mutableStateOf("Kaan S.") }
        var titleInput by remember { mutableStateOf("") }
        var commentInput by remember { mutableStateOf("") }

        val ratingLabels = listOf("1 - Zayıf", "2 - Geliştirilmeli", "3 - Ortalama", "4 - Çok İyi", "5 - Mükemmel (Koleksiyonluk)")

        AlertDialog(
            onDismissRequest = { showAddReviewDialog = false },
            containerColor = ObsidianSurface,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.border(1.dp, ImperialGoldBorder, RoundedCornerShape(16.dp)),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.RateReview, contentDescription = null, tint = ImperialGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Koleksiyoner Yorumu Ekle", color = ImperialGold, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "${watch.brand} ${watch.modelName} için kondisyon, zaman tutuş ve ekspertiz deneyiminizi paylaşın.",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Star Rating Picker
                    Text("Puanınız:", fontSize = 12.sp, color = ImperialGold, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (star in 1..5) {
                            IconButton(
                                onClick = { selectedRating = star },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (star <= selectedRating) Icons.Default.Star else Icons.Outlined.StarBorder,
                                    contentDescription = "$star Yıldız",
                                    tint = ImperialGold,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = ratingLabels[selectedRating - 1],
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ImperialGoldLight,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = userNameInput,
                        onValueChange = { userNameInput = it },
                        label = { Text("Adınız / Mahlasınız", color = ImperialGold) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ImperialGold,
                            unfocusedBorderColor = ObsidianStroke,
                            focusedTextColor = TextPureWhite,
                            unfocusedTextColor = TextPureWhite
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Yorum Başlığı (örn. Kusursuz Kondisyon)", color = ImperialGold) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ImperialGold,
                            unfocusedBorderColor = ObsidianStroke,
                            focusedTextColor = TextPureWhite,
                            unfocusedTextColor = TextPureWhite
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = commentInput,
                        onValueChange = { commentInput = it },
                        label = { Text("Detaylı Yorum ve Değerlendirmeniz", color = ImperialGold) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ImperialGold,
                            unfocusedBorderColor = ObsidianStroke,
                            focusedTextColor = TextPureWhite,
                            unfocusedTextColor = TextPureWhite
                        ),
                        minLines = 3,
                        maxLines = 5,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (commentInput.isNotBlank()) {
                            viewModel.addUserReview(
                                watchId = watch.id,
                                userName = userNameInput,
                                rating = selectedRating,
                                title = if (titleInput.isBlank()) "Doğrulanmış Saat İncelemesi" else titleInput,
                                comment = commentInput
                            )
                            showAddReviewDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ImperialGold, contentColor = ObsidianBlack)
                ) {
                    Text("Yorumu Yayınla", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddReviewDialog = false }) {
                    Text("Vazgeç", color = TextMuted)
                }
            }
        )
    }

    // Share Sheet
    if (showShareModal) {
        SocialShareSheet(
            watch = watch,
            onDismiss = { showShareModal = false }
        )
    }
}

@Composable
fun ReviewItemCard(
    review: WatchReview,
    onVoteHelpful: () -> Unit
) {
    Surface(
        color = ObsidianBlack,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(0.8.dp, ObsidianStroke),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Initial Avatar
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(ImperialGoldContainer, CircleShape)
                            .border(1.dp, ImperialGoldBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = review.userName.take(1).uppercase(),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ImperialGoldLight
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = review.userName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPureWhite
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = EmeraldVerified,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = review.userBadge,
                                fontSize = 10.sp,
                                color = EmeraldVerified,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Text(
                    text = review.date,
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Rating Stars
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 1..5) {
                    Icon(
                        imageVector = if (i <= review.rating) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = null,
                        tint = ImperialGold,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = review.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ImperialGoldLight
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = review.comment,
                fontSize = 12.sp,
                color = TextPureWhite,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Helpful row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Loomis / Brinks Zırhlı Teslimat Doğrulandı",
                    fontSize = 10.sp,
                    color = TextMuted
                )

                Surface(
                    color = ObsidianSurfaceVariant,
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(0.6.dp, ObsidianStroke),
                    modifier = Modifier.clickable { onVoteHelpful() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Faydalı",
                            tint = ImperialGold,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Faydalı (${review.helpfulCount})",
                            fontSize = 10.sp,
                            color = ImperialGoldLight,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpertiseMetricRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 11.sp, color = TextMuted)
        Text(text = value, fontSize = 11.sp, color = ImperialGoldLight, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun SpecRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 12.sp, color = TextMuted)
        Text(text = value, fontSize = 12.sp, color = TextPureWhite, fontWeight = FontWeight.Medium)
    }
}
