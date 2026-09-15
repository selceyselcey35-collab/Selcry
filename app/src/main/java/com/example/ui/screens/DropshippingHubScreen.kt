package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.data.model.DropshipListing
import com.example.data.model.LuxuryWatch
import com.example.ui.AppDestination
import com.example.ui.MarketplaceViewModel
import com.example.ui.components.GoldBadge
import com.example.ui.theme.*

@Composable
fun DropshippingHubScreen(
    viewModel: MarketplaceViewModel,
    onOpenWatchDetail: (LuxuryWatch) -> Unit
) {
    val dropshipListings by viewModel.dropshipListings.collectAsState()
    val allWatches by viewModel.watchesList.collectAsState()

    // Initialize with a default dropship item if empty for instant visual delight
    LaunchedEffect(dropshipListings.isEmpty()) {
        if (dropshipListings.isEmpty() && allWatches.isNotEmpty()) {
            val first = allWatches[0]
            viewModel.addWatchToDropshipBoutique(
                watch = first,
                customSellingPrice = first.priceTry + 180000L,
                depot = "Cenevre Saat Borsası Deposu"
            )
        }
    }

    val totalPotentialProfit = remember(dropshipListings) {
        dropshipListings.sumOf { it.estimatedProfit * it.totalSalesCount.coerceAtLeast(1) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Dropshipping Banner
        item {
            Surface(
                color = ObsidianSurface,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ImperialGoldBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Storefront, contentDescription = null, tint = ImperialGold, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "DROPSHIPPING & B2B KONSİNYE",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ImperialGold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Sıfır Stok Riski • Doğrudan Sertifikalı Depodan Teslimat",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }
                        GoldBadge("B2B AKTİF")
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Chronos küresel saat depolarındaki (Cenevre, Dubai, Londra, İstanbul) sertifikalı saatleri sermayesiz listeleyin. Müşteriniz sipariş verdiğinde Brinks zırhlı kurye depodan çıkar ve kâr marjınız anında hesabınıza yatar.",
                        fontSize = 12.sp,
                        color = TextPureWhite,
                        lineHeight = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Dropshipper Performance Stats Card
        item {
            Surface(
                color = ObsidianSurfaceVariant,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(0.8.dp, ObsidianStroke),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatMetric("Aktif Vitrinim", "${dropshipListings.size} Saat")
                    StatMetric("Gerçekleşen Satış", "${dropshipListings.sumOf { it.totalSalesCount }} Adet")
                    StatMetric("Kazanılan Kâr", "${String.format("%,d", totalPotentialProfit)} ₺")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Supplier Depots Info
        item {
            Text(
                text = "ENTEGRE YETKİLİ DEPOLAR (DOĞRUDAN STOK & SİGORTA)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ImperialGold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DepotBadge("Cenevre Vaults", "İsviçre", Modifier.weight(1f))
                DepotBadge("Dubai Gold Souk", "BAE", Modifier.weight(1f))
                DepotBadge("Kapalıçarşı Elit", "Türkiye", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Section Title: My Dropship Listings
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "VİTRİNİMDEKİ DROPSHİP SAATLER",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ImperialGold,
                    letterSpacing = 1.sp
                )

                TextButton(onClick = { viewModel.navigateTo(AppDestination.MARKETPLACE) }) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = ImperialGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Katalogdan Saat Ekle", color = ImperialGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        if (dropshipListings.isEmpty()) {
            item {
                Surface(
                    color = ObsidianSurface,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Henüz dropship vitrininize saat eklemediniz.",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        } else {
            items(dropshipListings) { item ->
                DropshipItemCard(
                    listing = item,
                    onOpenDetail = { onOpenWatchDetail(item.watch) },
                    onRemove = { viewModel.removeDropshipItem(item.watchId) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        item {
            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}

@Composable
fun DropshipItemCard(
    listing: DropshipListing,
    onOpenDetail: () -> Unit,
    onRemove: () -> Unit
) {
    val context = LocalContext.current
    val imageResId = remember(listing.watch.imageResName) {
        val res = context.resources.getIdentifier(listing.watch.imageResName, "drawable", context.packageName)
        if (res != 0) res else R.drawable.ic_chronos_logo
    }

    Surface(
        color = ObsidianSurface,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ImperialGoldBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = listing.watch.modelName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = listing.watch.brand.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ImperialGold
                )
                Text(
                    text = listing.watch.modelName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPureWhite,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Satış Fiyatınız:", fontSize = 10.sp, color = TextMuted)
                        Text("${String.format("%,d", listing.yourCustomSellingPrice)} ₺", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPureWhite)
                    }
                    Column {
                        Text("Birim Kârınız:", fontSize = 10.sp, color = TextMuted)
                        Text("+${String.format("%,d", listing.estimatedProfit)} ₺", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EmeraldVerified)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tedarik Deposu: ${listing.supplierDepot}",
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }

            IconButton(onClick = onRemove) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Kaldır", tint = CrimsonAlert)
            }
        }
    }
}

@Composable
private fun StatMetric(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = ImperialGoldLight)
        Text(text = label, fontSize = 10.sp, color = TextMuted)
    }
}

@Composable
private fun DepotBadge(name: String, country: String, modifier: Modifier) {
    Surface(
        color = ObsidianSurfaceVariant,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(0.8.dp, ObsidianStroke),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Luggage, contentDescription = null, tint = ImperialGold, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPureWhite)
            Text(text = country, fontSize = 10.sp, color = TextMuted)
        }
    }
}
