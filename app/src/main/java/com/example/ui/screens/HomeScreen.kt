package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.LuxuryWatch
import com.example.ui.AppDestination
import com.example.ui.MarketplaceViewModel
import com.example.ui.components.GoldBadge
import com.example.ui.components.SocialShareSheet
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MarketplaceViewModel,
    onOpenWatchDetail: (LuxuryWatch) -> Unit
) {
    val watches by viewModel.watchesList.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedBrand by viewModel.selectedBrandFilter.collectAsState()
    val comparisonWatches by viewModel.comparisonWatches.collectAsState()

    var watchToShare by remember { mutableStateOf<LuxuryWatch?>(null) }

    val brands = listOf("Tümü", "Rolex", "Patek Philippe", "Audemars Piguet", "Vacheron Constantin")

    val filteredWatches = remember(watches, searchQuery, selectedBrand) {
        watches.filter { watch ->
            val matchesBrand = selectedBrand == "Tümü" || watch.brand.equals(selectedBrand, ignoreCase = true)
            val matchesQuery = searchQuery.isBlank() ||
                    watch.brand.contains(searchQuery, ignoreCase = true) ||
                    watch.modelName.contains(searchQuery, ignoreCase = true) ||
                    watch.referenceNumber.contains(searchQuery, ignoreCase = true)
            matchesBrand && matchesQuery
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Hero Showcase Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_luxury_banner),
                    contentDescription = "Chronos Haute Horlogerie",
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
                                    ObsidianBlack.copy(alpha = 0.7f),
                                    ObsidianBlack
                                )
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    GoldBadge(
                        text = "DOĞRULANMIŞ LÜKS PAZARYERİ",
                        icon = Icons.Default.Shield
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Zamanın ve Mükemmelliğin Zirvesi",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPureWhite
                    )
                    Text(
                        text = "Witschi Ekspertiz Onaylı • E-Fatura Uyumlu • Yurt İçi / Dışı Güvenli Havuz",
                        fontSize = 11.sp,
                        color = ImperialGoldLight
                    )
                }
            }
        }

        // Search Bar
        item {
            Surface(
                color = ObsidianSurface,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ImperialGoldBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Ara",
                        tint = ImperialGold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = searchQuery,
                        onValueChange = { viewModel.updateSearchQuery(it) },
                        placeholder = {
                            Text("Marka, model veya referans no ara (örn. Daytona)", color = TextMuted, fontSize = 13.sp)
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = ImperialGold,
                            focusedTextColor = TextPureWhite,
                            unfocusedTextColor = TextPureWhite
                        ),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Temizle", tint = TextMuted)
                        }
                    }
                }
            }
        }

        // Brand Horizontal Filter Chips
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(brands) { brand ->
                    val isSelected = selectedBrand == brand
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) ImperialGold else ObsidianSurface,
                        border = BorderStroke(1.dp, if (isSelected) ImperialGold else ObsidianStroke),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { viewModel.selectBrandFilter(brand) }
                    ) {
                        Text(
                            text = brand,
                            color = if (isSelected) ObsidianBlack else TextPureWhite,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // Catalog Section Header with Dropship & Comparison Highlights
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ÖZEL KOLEKSİYON SAATLERİ (${filteredWatches.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ImperialGold,
                    letterSpacing = 1.sp
                )
                if (comparisonWatches.isNotEmpty()) {
                    Text(
                        text = "${comparisonWatches.size} Saat Kıyaslamada",
                        fontSize = 11.sp,
                        color = ImperialGoldLight,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { viewModel.navigateTo(AppDestination.COMPARISON) }
                    )
                }
            }
        }

        // Watch Items List
        items(filteredWatches) { watch ->
            val isCompared = comparisonWatches.any { it.id == watch.id }
            WatchMarketCard(
                watch = watch,
                isCompared = isCompared,
                onCardClick = { onOpenWatchDetail(watch) },
                onToggleFavorite = { viewModel.toggleFavorite(watch) },
                onToggleComparison = { viewModel.toggleComparison(watch) },
                onShare = { watchToShare = watch },
                onQuickBuy = { viewModel.prepareCheckout(watch) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }

    // Share Dialog
    watchToShare?.let { watch ->
        SocialShareSheet(
            watch = watch,
            onDismiss = { watchToShare = null }
        )
    }
}

@Composable
fun WatchMarketCard(
    watch: LuxuryWatch,
    isCompared: Boolean,
    onCardClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleComparison: () -> Unit,
    onShare: () -> Unit,
    onQuickBuy: () -> Unit
) {
    val heartColor by animateColorAsState(
        targetValue = if (watch.isFavorite) CrimsonAlert else TextMuted,
        label = "heart_anim"
    )

    val context = LocalContext.current
    val imageResId = remember(watch.imageResName) {
        val res = context.resources.getIdentifier(watch.imageResName, "drawable", context.packageName)
        if (res != 0) res else R.drawable.ic_chronos_logo
    }

    Surface(
        color = ObsidianSurface,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, if (isCompared) ImperialGold else ImperialGoldBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onCardClick() },
        shadowElevation = 4.dp
    ) {
        Column {
            // Card Top with Image & Badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "${watch.brand} ${watch.modelName}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Top Gradient for contrast
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    ObsidianBlack.copy(alpha = 0.8f),
                                    Color.Transparent,
                                    ObsidianBlack.copy(alpha = 0.9f)
                                )
                            )
                        )
                )

                // Badges top row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GoldBadge(
                        text = "EKSPERTİZ %${watch.authenticityScore}",
                        icon = Icons.Default.Verified
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Share Button
                        Surface(
                            shape = CircleShape,
                            color = ObsidianBlack.copy(alpha = 0.8f),
                            border = BorderStroke(0.8.dp, ImperialGoldBorder),
                            modifier = Modifier.size(36.dp)
                        ) {
                            IconButton(onClick = onShare) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Paylaş",
                                    tint = ImperialGold,
                                    modifier = Modifier.size(17.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Favorite / Like Heart Button
                        Surface(
                            shape = CircleShape,
                            color = ObsidianBlack.copy(alpha = 0.8f),
                            border = BorderStroke(0.8.dp, if (watch.isFavorite) CrimsonAlert else ObsidianStroke),
                            modifier = Modifier.size(36.dp)
                        ) {
                            IconButton(onClick = onToggleFavorite) {
                                Icon(
                                    imageVector = if (watch.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Beğen",
                                    tint = heartColor,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                // Bottom strip on the image: Dropship available & Location
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = ImperialGold,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = watch.location,
                            fontSize = 11.sp,
                            color = TextPureWhite,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (watch.isDropshipAvailable) {
                        Surface(
                            color = ImperialGoldContainer.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(0.5.dp, ImperialGold)
                        ) {
                            Text(
                                text = "Dropship'e Hazır",
                                color = ImperialGoldLight,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // Watch Details Info Section
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = watch.brand.uppercase(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ImperialGold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = watch.modelName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPureWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Ref: ${watch.referenceNumber} • ${watch.year} • ${watch.condition}",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = ImperialGold,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "5.0",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ImperialGoldLight
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "• Doğrulanmış Koleksiyoner Yorumları",
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Technical tags
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TechnicalChip(watch.caseMaterial)
                    TechnicalChip(watch.diameter)
                    TechnicalChip(watch.powerReserve)
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = ObsidianStroke, thickness = 0.8.dp)
                Spacer(modifier = Modifier.height(10.dp))

                // Price & Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${String.format("%,d", watch.priceTry)} ₺",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ImperialGoldLight
                        )
                        Text(
                            text = "≈ $${String.format("%,d", watch.priceUsd)} USD • KDV Dahil",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Compare Button
                        OutlinedButton(
                            onClick = onToggleComparison,
                            border = BorderStroke(1.dp, if (isCompared) ImperialGold else ObsidianStroke),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isCompared) ImperialGoldContainer else Color.Transparent
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = if (isCompared) Icons.Default.Check else Icons.Default.CompareArrows,
                                contentDescription = null,
                                tint = if (isCompared) ImperialGold else TextMuted,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isCompared) "Kıyasta" else "Kıyasla",
                                fontSize = 11.sp,
                                color = if (isCompared) ImperialGold else TextPureWhite
                            )
                        }

                        // Buy / Invoice Button
                        Button(
                            onClick = onQuickBuy,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ImperialGold,
                                contentColor = ObsidianBlack
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Satın Al",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TechnicalChip(label: String) {
    Surface(
        color = ObsidianSurfaceVariant,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(0.5.dp, ObsidianStroke)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = TextMuted,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
    }
}
