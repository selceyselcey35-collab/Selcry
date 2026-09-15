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
import com.example.ui.MarketplaceViewModel
import com.example.ui.components.GoldBadge
import com.example.ui.theme.*

@Composable
fun ExpertisePortalScreen(
    viewModel: MarketplaceViewModel
) {
    val repository = viewModel.repository
    val checklist = remember { repository.getExpertiseChecklist() }
    val refInput by viewModel.sellerRefNumberInput.collectAsState()
    val serialInput by viewModel.sellerSerialInput.collectAsState()
    val noticeMsg by viewModel.verificationSuccessNotice.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Portal Header
        item {
            Surface(
                color = ObsidianSurface,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ImperialGoldBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = ImperialGold, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "SATIŞ VE EKSPERTİZ LABORATUVARI",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = ImperialGold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "İsviçre Saat Yapımcıları Birliği Standartlarında Doğrulama",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Chronos pazar yerinde listelemek istediğiniz her lüks saat bağımsız ekspertiz laboratuvarımızda 5 kademeli güvenlik taramasından geçirilir ve devredilebilir blokzincir orijinallik pasaportu ile onaylanır.",
                        fontSize = 12.sp,
                        color = TextPureWhite,
                        lineHeight = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Notification Banner
        noticeMsg?.let { msg ->
            item {
                Surface(
                    color = ImperialGoldContainer,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, ImperialGold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ImperialGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Tebrikler!", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ImperialGoldLight)
                            Text(msg, fontSize = 11.sp, color = TextPureWhite)
                        }
                        IconButton(onClick = { viewModel.clearNotice() }) {
                            Icon(Icons.Default.Close, contentDescription = "Kapat", tint = ImperialGoldLight)
                        }
                    }
                }
            }
        }

        // Seller New Watch Verification Submission Card
        item {
            Surface(
                color = ObsidianSurface,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ObsidianStroke),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Yeni Saat Doğrulama & İnceleme Başvurusu",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ImperialGold
                    )
                    Text(
                        text = "Saatinizin referans ve seri numarasını girerek ön sorgulama başlatın",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = refInput,
                        onValueChange = { viewModel.updateSellerInputs(it, serialInput) },
                        label = { Text("Model Referans Numarası (örn. 116508)", color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ImperialGold,
                            unfocusedBorderColor = ObsidianStroke,
                            focusedTextColor = TextPureWhite,
                            unfocusedTextColor = TextPureWhite
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = serialInput,
                        onValueChange = { viewModel.updateSellerInputs(refInput, it) },
                        label = { Text("Kasa Seri Numarası (Şifreli Doğrulama)", color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ImperialGold,
                            unfocusedBorderColor = ObsidianStroke,
                            focusedTextColor = TextPureWhite,
                            unfocusedTextColor = TextPureWhite
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.submitWatchForVerification() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ImperialGold,
                            contentColor = ObsidianBlack
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Ekspertiz Randevusu & Barkod Oluştur", fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Section Title: 5-Stage Verification Protocol
        item {
            Text(
                text = "5 KADEMELİ EKSPERTİZ PROTOKOLÜ VE TESTLERİ",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ImperialGold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }

        // Verification Checklist Items
        items(checklist) { check ->
            Surface(
                color = ObsidianSurface,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(0.8.dp, ObsidianStroke),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = EmeraldVerified,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = check.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPureWhite
                        )
                        Text(
                            text = check.subtitle,
                            fontSize = 11.sp,
                            color = ImperialGoldLight,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = check.details,
                            fontSize = 11.sp,
                            color = TextMuted,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // Certified Horologist Sign-Off
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                color = ObsidianSurfaceVariant,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(0.8.dp, ImperialGoldBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = ImperialGold, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Baş Horolog Tasdiki",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ImperialGoldLight
                        )
                        Text(
                            text = "Jean-Marc Laurent • Swiss Watchmakers Guild Sicil No: 4829\nHer onaylı saatin sertifikası 2 yıl tam mekanik garanti altındadır.",
                            fontSize = 11.sp,
                            color = TextMuted,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
