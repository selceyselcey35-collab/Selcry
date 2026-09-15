package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LuxuryWatch
import com.example.data.model.PaymentProvider
import com.example.ui.AppDestination
import com.example.ui.MarketplaceViewModel
import com.example.ui.components.GoldBadge
import com.example.ui.theme.*

@Composable
fun CheckoutInvoiceScreen(
    watch: LuxuryWatch,
    viewModel: MarketplaceViewModel,
    onBack: () -> Unit
) {
    val invoice by viewModel.currentInvoice.collectAsState()
    val selectedProvider by viewModel.selectedPaymentProvider.collectAsState()

    var buyerName by remember { mutableStateOf("Ahmet Yılmaz") }
    var buyerTaxId by remember { mutableStateOf("28491048201") }
    var buyerAddress by remember { mutableStateOf("Bebek Mah. Cevdetpaşa Cad. No: 42, Beşiktaş / İstanbul") }

    var isOrderCompleted by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Back Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Geri", tint = ImperialGold)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "RESMİ FATURALANDIRMA & ÖDEME",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ImperialGold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Vergi Mevzuatı Tam Uyumlu & Çift Katmanlı Escrow",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }
        }

        // Product Summary Box
        item {
            Surface(
                color = ObsidianSurface,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ImperialGoldBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Watch, contentDescription = null, tint = ImperialGold, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = watch.brand.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ImperialGold)
                        Text(text = watch.modelName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPureWhite)
                        Text(text = "Ref: ${watch.referenceNumber} • Seri: Doğrulanmış", fontSize = 11.sp, color = TextMuted)
                    }
                    Text(
                        text = "${String.format("%,d", watch.priceTry)} ₺",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ImperialGoldLight
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Official Tax & E-Invoice Calculation Panel
        item {
            invoice?.let { inv ->
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
                                Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = ImperialGold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "GİB E-Arşiv / E-Fatura Dökümü",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ImperialGold
                                )
                            }
                            GoldBadge("VERGİ MEVZUATI TAM UYUMLU")
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        TaxRow("Fatura Seri No", inv.invoiceNumber)
                        TaxRow("ETTN Tekil Kod", inv.ettNumber.take(18) + "...")
                        TaxRow("Matrah (KDV Hariç)", "${String.format("%,d", inv.subtotalTry)} ₺")
                        TaxRow("Katma Değer Vergisi (%20 KDV)", "${String.format("%,d", inv.vatAmountTry)} ₺")
                        TaxRow("Kıymetli Maden & Lüks Harç", "${String.format("%,d", inv.luxuryExciseTaxTry)} ₺")

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = ObsidianStroke)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Tahsil Edilecek Toplam Tutar:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPureWhite)
                            Text("${String.format("%,d", inv.totalAmountTry)} ₺", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = ImperialGoldLight)
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Faturanız Gelir İdaresi Başkanlığı (GİB) entegrasyonuyla eş zamanlı mühürlenecek ve e-posta adresinize resmi karekodlu PDF olarak iletilecektir.",
                            fontSize = 10.sp,
                            color = TextMuted,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Invoicing Details Input
        item {
            Surface(
                color = ObsidianSurface,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(0.8.dp, ObsidianStroke),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Fatura & Teslimat Bilgileri",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ImperialGold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = buyerName,
                        onValueChange = { buyerName = it },
                        label = { Text("Ad Soyad / Şirket Unvanı", color = TextMuted) },
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
                        value = buyerTaxId,
                        onValueChange = { buyerTaxId = it },
                        label = { Text("T.C. Kimlik No / Vergi Kimlik No (VKN)", color = TextMuted) },
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
                        value = buyerAddress,
                        onValueChange = { buyerAddress = it },
                        label = { Text("Fatura & Sigortalı Teslimat Adresi", color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ImperialGold,
                            unfocusedBorderColor = ObsidianStroke,
                            focusedTextColor = TextPureWhite,
                            unfocusedTextColor = TextPureWhite
                        ),
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Domestic & International Payment Gateways
        item {
            Text(
                text = "ÖDEME SAĞLAYICILARI (YURT İÇİ & YURT DIŞI AÇIK)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ImperialGold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Domestic Gateways
            Text(
                text = "YURT İÇİ ÖDEME SAĞLAYICILARI",
                fontSize = 11.sp,
                color = ImperialGoldLight,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            val domesticList = listOf(
                PaymentProvider.TROY,
                PaymentProvider.TURKISH_BANK_3D,
                PaymentProvider.BKM_EXPRESS,
                PaymentProvider.FAST_HAVALE
            )

            domesticList.forEach { provider ->
                PaymentProviderTile(
                    provider = provider,
                    isSelected = selectedProvider == provider,
                    onClick = { viewModel.setPaymentProvider(provider) }
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // International Gateways
            Text(
                text = "YURT DIŞI & KÜRESEL ÖDEME SAĞLAYICILARI",
                fontSize = 11.sp,
                color = ImperialGoldLight,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            val internationalList = listOf(
                PaymentProvider.STRIPE_GLOBAL,
                PaymentProvider.SWIFT_INTERNATIONAL,
                PaymentProvider.APPLE_PAY,
                PaymentProvider.CRYPTO_ESCROW
            )

            internationalList.forEach { provider ->
                PaymentProviderTile(
                    provider = provider,
                    isSelected = selectedProvider == provider,
                    onClick = { viewModel.setPaymentProvider(provider) }
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Dual Layer Escrow Protection Notice
        item {
            Surface(
                color = ImperialGoldContainer.copy(alpha = 0.5f),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, ImperialGoldBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = ImperialGold, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Chronos Çift Katmanlı Emanet (Escrow) Güvencesi",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ImperialGoldLight
                        )
                        Text(
                            text = "Ödemeniz Chronos Emanet Havuz Hesabında güvenle bloke edilir. Saat size Loomis/Brinks zırhlı kurye ile ulaşıp orijinallik mührünü onaylayana kadar satıcıya para aktarımı yapılmaz.",
                            fontSize = 11.sp,
                            color = TextPureWhite,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Final Pay & Issue Invoice Button
        item {
            Button(
                onClick = { isOrderCompleted = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ImperialGold,
                    contentColor = ObsidianBlack
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Default.Lock, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${selectedProvider.displayName} ile Öde (${String.format("%,d", watch.priceTry)} ₺)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    // Order Success Dialog
    if (isOrderCompleted) {
        AlertDialog(
            onDismissRequest = {
                isOrderCompleted = false
                viewModel.navigateTo(AppDestination.MARKETPLACE)
            },
            containerColor = ObsidianSurface,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.border(BorderStroke(1.dp, ImperialGoldBorder), RoundedCornerShape(16.dp)),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldVerified, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Sipariş ve E-Fatura Onaylandı", color = ImperialGold, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Tebrikler Sayın $buyerName! Ödemeniz ${selectedProvider.displayName} üzerinden güvenli emanet havuzuna aktarılmıştır.",
                        fontSize = 12.sp,
                        color = TextPureWhite
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = ObsidianBlack,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(0.5.dp, ObsidianStroke),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("GİB E-Arşiv Fatura No: ${invoice?.invoiceNumber}", fontSize = 11.sp, color = ImperialGoldLight, fontWeight = FontWeight.Bold)
                            Text("ETTN: ${invoice?.ettNumber}", fontSize = 9.sp, color = TextMuted)
                            Text("Sigortalı Zırhlı Kurye: Loomis Global Express #LMS-89104", fontSize = 11.sp, color = TextPureWhite)
                            Text("14 Gün Cayma & Otomatik İade Garantisi Aktif", fontSize = 11.sp, color = EmeraldVerified)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isOrderCompleted = false
                        viewModel.navigateTo(AppDestination.MARKETPLACE)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ImperialGold, contentColor = ObsidianBlack)
                ) {
                    Text("Vitrine Dön", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun PaymentProviderTile(
    provider: PaymentProvider,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) ImperialGoldContainer else ObsidianSurface,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, if (isSelected) ImperialGold else ObsidianStroke),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = ImperialGold)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = provider.displayName,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) ImperialGoldLight else TextPureWhite
                )
                Text(
                    text = "${provider.type} • 256-Bit Uçtan Uca Şifreli Güvenli Ağ",
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }
        }
    }
}

@Composable
private fun TaxRow(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 11.sp, color = TextMuted)
        Text(text = value, fontSize = 11.sp, color = TextPureWhite, fontWeight = FontWeight.Medium)
    }
}
