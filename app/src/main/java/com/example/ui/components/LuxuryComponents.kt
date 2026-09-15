package com.example.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.LuxuryWatch
import com.example.ui.AppDestination
import com.example.ui.theme.*

@Composable
fun LuxuryTopBar(
    currentDestination: AppDestination,
    comparisonCount: Int,
    onNavigate: (AppDestination) -> Unit,
    onOpenPolicy: () -> Unit
) {
    Surface(
        color = ObsidianSurface,
        border = BorderStroke(1.dp, Brush.horizontalGradient(listOf(Color.Transparent, ImperialGoldBorder, Color.Transparent))),
        shadowElevation = 8.dp
    ) {
        Column {
            // Security E2EE sub-header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ImperialGoldContainer.copy(alpha = 0.4f))
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "E2EE",
                        tint = ImperialGold,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "256-Bit Uçtan Uca Şifreleme • GİB E-Fatura Uyumlu",
                        fontSize = 11.sp,
                        color = ImperialGoldLight,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = "Yasal Politikalar",
                    fontSize = 11.sp,
                    color = ImperialGold,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable { onOpenPolicy() }
                        .padding(horizontal = 4.dp)
                )
            }

            // Main Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onNavigate(AppDestination.MARKETPLACE) }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_chronos_logo),
                        contentDescription = "Chronos Luxury Logo",
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .border(1.dp, ImperialGold, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "CHRONOS",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = ImperialGold,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "HAUTE HORLOGERIE VAULT",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Light,
                            color = TextMuted,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Comparison Badge
                    BadgedBox(
                        badge = {
                            if (comparisonCount > 0) {
                                Badge(
                                    containerColor = ImperialGold,
                                    contentColor = ObsidianBlack
                                ) {
                                    Text(text = comparisonCount.toString(), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    ) {
                        IconButton(
                            onClick = { onNavigate(AppDestination.COMPARISON) },
                            modifier = Modifier.size(38.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CompareArrows,
                                contentDescription = "Karşılaştır",
                                tint = if (currentDestination == AppDestination.COMPARISON) ImperialGold else TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Live VIP Concierge button
                    IconButton(
                        onClick = { onNavigate(AppDestination.LIVE_CONCIERGE) },
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.HeadsetMic,
                            contentDescription = "Canlı Destek",
                            tint = if (currentDestination == AppDestination.LIVE_CONCIERGE) ImperialGold else TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LuxuryBottomBar(
    currentDestination: AppDestination,
    onNavigate: (AppDestination) -> Unit
) {
    NavigationBar(
        containerColor = ObsidianSurface,
        tonalElevation = 8.dp,
        modifier = Modifier.border(BorderStroke(1.dp, ImperialGoldBorder))
    ) {
        val items = listOf(
            Triple(AppDestination.MARKETPLACE, "Vitrin", Icons.Default.Watch),
            Triple(AppDestination.COMPARISON, "Kıyasla", Icons.Default.CompareArrows),
            Triple(AppDestination.EXPERTISE_PORTAL, "Ekspertiz", Icons.Default.Verified),
            Triple(AppDestination.DROPSHIPPING_HUB, "Dropship", Icons.Default.Storefront),
            Triple(AppDestination.AUTOMATED_RETURNS, "İade / Fatura", Icons.Default.AssignmentReturn)
        )

        items.forEach { (dest, label, icon) ->
            val isSelected = currentDestination == dest
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(dest) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ImperialGold,
                    selectedTextColor = ImperialGold,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted,
                    indicatorColor = ImperialGoldContainer
                )
            )
        }
    }
}

@Composable
fun GoldBadge(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    Surface(
        color = ImperialGoldContainer,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(0.8.dp, ImperialGoldBorder),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ImperialGold,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = text,
                color = ImperialGoldLight,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun SocialShareSheet(
    watch: LuxuryWatch,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val shareText = "Chronos Dijital Pazar Yeri'nde incelediğim lüks saat:\n" +
            "💎 ${watch.brand} ${watch.modelName} (Ref: ${watch.referenceNumber})\n" +
            "✨ Kasa: ${watch.caseMaterial}, Mekanizma: ${watch.movement}\n" +
            "🛡️ Ekspertiz Orijinallik Skoru: %${watch.authenticityScore}\n" +
            "💰 Fiyat: ${String.format("%,d", watch.priceTry)} ₺\n" +
            "Doğrulanmış Blokzincir Sertifikası: ${watch.blockchainCertificateHash}\n" +
            "https://chronos.luxury/watch/${watch.id}"

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ObsidianSurface,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 12.dp,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Share, contentDescription = null, tint = ImperialGold)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sosyal Medyada Paylaş",
                    color = ImperialGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${watch.brand} ${watch.modelName} detaylarını arkadaşlarınızla veya koleksiyoner ağınızla paylaşın.",
                    color = TextMuted,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Social sharing channel buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ShareChannelButton("WhatsApp", Icons.Default.ChatBubble, Color(0xFF25D366)) {
                        shareViaSystemIntent(context, shareText, "com.whatsapp")
                    }
                    ShareChannelButton("Instagram", Icons.Default.CameraAlt, Color(0xFFE1306C)) {
                        shareViaSystemIntent(context, shareText, "com.instagram.android")
                    }
                    ShareChannelButton("X (Twitter)", Icons.Default.Tag, Color(0xFF1DA1F2)) {
                        shareViaSystemIntent(context, shareText, "com.twitter.android")
                    }
                    ShareChannelButton("Telegram", Icons.Default.Send, Color(0xFF0088CC)) {
                        shareViaSystemIntent(context, shareText, "org.telegram.messenger")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // General System Share Sheet
                OutlinedButton(
                    onClick = {
                        shareViaSystemIntent(context, shareText, null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, ImperialGold),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = ImperialGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tüm Uygulamalarda Paylaş / Bağlantıyı Kopyala", color = ImperialGold, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Kapat", color = ImperialGold)
            }
        }
    )
}

@Composable
private fun ShareChannelButton(
    name: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(6.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = ObsidianSurfaceVariant,
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f)),
            modifier = Modifier.size(44.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = name,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = name, fontSize = 10.sp, color = TextPureWhite)
    }
}

private fun shareViaSystemIntent(context: Context, text: String, targetPackage: String?) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
        if (targetPackage != null) {
            `package` = targetPackage
        }
    }
    try {
        val chooser = Intent.createChooser(sendIntent, "Chronos Saati Paylaş")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    } catch (e: Exception) {
        // Fallback to generic chooser
        val fallback = Intent.createChooser(
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            },
            "Chronos Saati Paylaş"
        )
        fallback.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(fallback)
    }
}

@Composable
fun LegalPolicyConsentDialog(
    onAccept: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ObsidianSurface,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.border(BorderStroke(1.dp, ImperialGoldBorder), RoundedCornerShape(16.dp)),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Gavel, contentDescription = null, tint = ImperialGold)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Satış & Hukuk Politikaları",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = ImperialGold
                    )
                    Text(
                        text = "6502 Sayılı Tüketici Kanunu & Vergi Mevzuatı",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 340.dp)
            ) {
                Text(
                    text = "Chronos Dijital Lüks Saat Pazaryeri'ne hoş geldiniz. Platformumuz tamamen şeffaf, yasal ve denetlenen mevzuatlara uygun olarak hizmet vermektedir:",
                    fontSize = 12.sp,
                    color = TextPureWhite,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    color = ObsidianBlack,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(0.8.dp, ObsidianStroke),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        PolicyBulletItem(
                            title = "1. Mesafeli Satış Sözleşmesi & 14 Gün Yasal Cayma",
                            desc = "Satın alınan her saat, 14 gün koşulsuz cayma hakkına tabidir. İade durumunda Chronos algoritmik otomatik onay devreye girer ve Loomis/Brinks zırhlı kurye ile güvenli iade sağlanır."
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        PolicyBulletItem(
                            title = "2. Bağımsız Ekspertiz & Orijinallik Garantisi",
                            desc = "Listelenen her ürün Witschi Timegrapher, optik spektrometre ve mikroskop testlerinden geçirilmiş olup 2 yıl mekanizma ve ömür boyu orijinallik taahhütnamesiyle mühürlenir."
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        PolicyBulletItem(
                            title = "3. Resmi E-Fatura & Maliye Bakanlığı Uyumu",
                            desc = "Tüm alım, satım ve dropshipping komisyonları için GİB e-Arşiv ve e-Fatura düzenlenir. %20 KDV ve lüks tüketim mevzuatına tam uyumlu çalışılır."
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        PolicyBulletItem(
                            title = "4. Uçtan Uca Şifreleme (E2EE) & KVKK Güvencesi",
                            desc = "Kişisel verileriniz ve cüzdan işlemleriniz 256-bit AES protokolü ile şifrelenir. Üçüncü taraflarla kesinlikle paylaşılmaz."
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onAccept,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ImperialGold,
                    contentColor = ObsidianBlack
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Okudum, Onaylıyorum", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Kapat", color = TextMuted)
            }
        }
    )
}

@Composable
private fun PolicyBulletItem(title: String, desc: String) {
    Column {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = ImperialGoldLight
        )
        Text(
            text = desc,
            fontSize = 10.sp,
            color = TextMuted,
            lineHeight = 14.sp
        )
    }
}
