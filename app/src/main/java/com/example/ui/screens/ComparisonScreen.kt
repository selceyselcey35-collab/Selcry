package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.LuxuryWatch
import com.example.ui.AppDestination
import com.example.ui.MarketplaceViewModel
import com.example.ui.theme.*

@Composable
fun ComparisonScreen(
    viewModel: MarketplaceViewModel,
    onOpenWatchDetail: (LuxuryWatch) -> Unit
) {
    val comparisonWatches by viewModel.comparisonWatches.collectAsState()
    val allWatches by viewModel.watchesList.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        // Header
        Surface(
            color = ObsidianSurface,
            border = BorderStroke(1.dp, ImperialGoldBorder)
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
                        text = "AKILLI SAAT KIYASLAMA",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ImperialGold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Mekanizma, kasa, ekspertiz ve değer karşılaştırması",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }

                if (comparisonWatches.isNotEmpty()) {
                    TextButton(onClick = { viewModel.clearAllComparisons() }) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = null, tint = CrimsonAlert, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Temizle", color = CrimsonAlert, fontSize = 12.sp)
                    }
                }
            }
        }

        if (comparisonWatches.isEmpty()) {
            // Empty State with quick add suggestions
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CompareArrows,
                        contentDescription = null,
                        tint = ImperialGold,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Karşılaştırma Listeniz Henüz Boş",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPureWhite
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Vitrine giderek 2 veya daha fazla saati 'Kıyasla' butonuyla ekleyebilir ve teknik detaylarını yan yana inceleyebilirsiniz.",
                        fontSize = 13.sp,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            // Pre-add first two watches for convenience
                            if (allWatches.size >= 2) {
                                viewModel.toggleComparison(allWatches[0])
                                viewModel.toggleComparison(allWatches[1])
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ImperialGold,
                            contentColor = ObsidianBlack
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.AutoFixHigh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Örnek Karşılaştırma Yükle (Rolex vs Patek)", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            // Side-by-Side Horizontal Scrollable Comparison Table
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 70.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .horizontalScroll(scrollState)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // For each compared watch, render a complete comparison card column
                        comparisonWatches.forEach { watch ->
                            ComparisonWatchColumn(
                                watch = watch,
                                onOpenDetail = { onOpenWatchDetail(watch) },
                                onRemove = { viewModel.toggleComparison(watch) },
                                onBuy = { viewModel.prepareCheckout(watch) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ComparisonWatchColumn(
    watch: LuxuryWatch,
    onOpenDetail: () -> Unit,
    onRemove: () -> Unit,
    onBuy: () -> Unit
) {
    val context = LocalContext.current
    val imageResId = remember(watch.imageResName) {
        val res = context.resources.getIdentifier(watch.imageResName, "drawable", context.packageName)
        if (res != 0) res else R.drawable.ic_chronos_logo
    }

    Surface(
        color = ObsidianSurface,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ImperialGoldBorder),
        modifier = Modifier.width(280.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Remove from compare button & Watch Photo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = watch.modelName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(ObsidianBlack.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                        .size(32.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Kaldır", tint = CrimsonAlert, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = watch.brand.uppercase(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ImperialGold,
                letterSpacing = 1.sp
            )
            Text(
                text = watch.modelName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPureWhite,
                maxLines = 1
            )
            Text(
                text = "Ref: ${watch.referenceNumber}",
                fontSize = 11.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${String.format("%,d", watch.priceTry)} ₺",
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = ImperialGoldLight
            )
            Text(
                text = "≈ $${String.format("%,d", watch.priceUsd)} USD",
                fontSize = 11.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = ObsidianStroke)
            Spacer(modifier = Modifier.height(10.dp))

            // Comparative Metrics Rows
            CompareField("Ekspertiz Skoru", "%${watch.authenticityScore} (Mükemmel)")
            CompareField("Zamanlama Sapması", watch.timegrapherDeviation)
            CompareField("Kasa Materyali", watch.caseMaterial)
            CompareField("Kasa Çapı", watch.diameter)
            CompareField("Mekanizma", watch.movement)
            CompareField("Güç Rezervi", watch.powerReserve)
            CompareField("Su Dayanımı", watch.waterResistance)
            CompareField("Kutu & Evrak", if (watch.boxAndPapers) "Tam Takım (Full Set)" else "Yalnız Saat")
            CompareField("Blokzincir Pasaport", watch.blockchainCertificateHash.take(14) + "...")

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onBuy,
                colors = ButtonDefaults.buttonColors(containerColor = ImperialGold, contentColor = ObsidianBlack),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Satın Al / Fatura Oluştur", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedButton(
                onClick = onOpenDetail,
                border = BorderStroke(1.dp, ObsidianStroke),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Detaylı İncele", fontSize = 12.sp, color = TextPureWhite)
            }
        }
    }
}

@Composable
private fun CompareField(title: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = title, fontSize = 10.sp, color = TextMuted)
        Text(text = value, fontSize = 11.sp, color = TextPureWhite, fontWeight = FontWeight.Medium)
    }
}
