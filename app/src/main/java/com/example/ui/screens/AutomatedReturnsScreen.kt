package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReturnClaim
import com.example.ui.AppDestination
import com.example.ui.MarketplaceViewModel
import com.example.ui.components.GoldBadge
import com.example.ui.theme.*

@Composable
fun AutomatedReturnsScreen(
    viewModel: MarketplaceViewModel
) {
    val returnClaims by viewModel.returnClaims.collectAsState()
    val allWatches by viewModel.watchesList.collectAsState()

    // Provide default sample return claim if empty for rich immediate demonstration
    LaunchedEffect(returnClaims.isEmpty()) {
        if (returnClaims.isEmpty() && allWatches.isNotEmpty()) {
            viewModel.createAutoApprovedReturn(
                watch = allWatches[0],
                reason = "14 Gün Koşulsuz Yasal Cayma Hakkı"
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Return Mechanism Header
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
                            Icon(Icons.Default.AssignmentReturn, contentDescription = null, tint = ImperialGold, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "OTOMATİK ONAYLI İADE SİSTEMİ",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ImperialGold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "14 Gün Yasal Cayma & Güvenli Havuz İadesi",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }
                        GoldBadge("ALGORİTMİK ONAY")
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Chronos'ta satın aldığınız saatlerde haklarınız kanunla korunur. 14 gün içinde başlatılan iade talepleri bekleme olmaksızın otomatik onaylanır, Loomis/Brinks zırhlı kurye adrese çağrılır ve emanet tutarınız anında bankanıza aktarılır.",
                        fontSize = 12.sp,
                        color = TextPureWhite,
                        lineHeight = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Return Algorithm Features Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ReturnBadge("1. Anında Onay", "İnceleme beklemeden barkod basılır", Modifier.weight(1f))
                ReturnBadge("2. Zırhlı Kurye", "Sigortalı adresten teslim alımı", Modifier.weight(1f))
                ReturnBadge("3. Emanet İadesi", "Hesaba otomatik bloke çözümü", Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Section Title: Active Return Claims
        item {
            Text(
                text = "İADE TALEPLERİ VE SÜREÇ DURUMU",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ImperialGold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (returnClaims.isEmpty()) {
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
                        Text("Aktif bir iade talebiniz bulunmamaktadır.", color = TextMuted, fontSize = 13.sp)
                    }
                }
            }
        } else {
            items(returnClaims) { claim ->
                ReturnClaimCard(claim = claim)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun ReturnClaimCard(claim: ReturnClaim) {
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
                Text(
                    text = "Talep ID: ${claim.claimId}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ImperialGold
                )
                Surface(
                    color = EmeraldVerified.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(0.8.dp, EmeraldVerified)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldVerified, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Otomatik Onaylandı",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldVerified
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = claim.watchTitle,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPureWhite
            )
            Text(
                text = "İade Sebebi: ${claim.returnReason}",
                fontSize = 11.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = ObsidianStroke)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Sigortalı Zırhlı Kargo Kodu:", fontSize = 10.sp, color = TextMuted)
                    Text(claim.armoredCourierTrackingCode, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ImperialGoldLight)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Emanet İade Tutarı:", fontSize = 10.sp, color = TextMuted)
                    Text("${String.format("%,d", claim.escrowRefundAmount)} ₺", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = TextPureWhite)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tarih: ${claim.createdAt} • Loomis zırhlı kurye saati adresinizden teslim aldığında bloke tutar doğrudan kartınıza aktarılacaktır.",
                fontSize = 10.sp,
                color = TextMuted,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
private fun ReturnBadge(title: String, desc: String, modifier: Modifier) {
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
            Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ImperialGoldLight)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = desc, fontSize = 9.sp, color = TextMuted)
        }
    }
}
