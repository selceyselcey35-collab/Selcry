package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LiveChatMessage
import com.example.ui.MarketplaceViewModel
import com.example.ui.components.GoldBadge
import com.example.ui.theme.*

@Composable
fun LiveConciergeScreen(
    viewModel: MarketplaceViewModel,
    onBack: () -> Unit
) {
    val messages by viewModel.chatMessages.collectAsState()
    var inputText by remember { mutableStateOf("") }

    val quickQuestions = listOf(
        "İade süreci nasıl işliyor?",
        "Ekspertiz ve Witschi testleri",
        "Yurt dışı ödeme ve SWIFT",
        "Lüks saat dropshipping şartları"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        // Concierge Header
        Surface(
            color = ObsidianSurface,
            border = BorderStroke(1.dp, ImperialGoldBorder)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri", tint = ImperialGold)
                    }
                    Spacer(modifier = Modifier.width(6.dp))

                    Surface(
                        shape = CircleShape,
                        color = ImperialGoldContainer,
                        border = BorderStroke(1.dp, ImperialGold),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.SupportAgent, contentDescription = null, tint = ImperialGoldLight)
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "CHRONOS VIP CONCIERGE",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ImperialGold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldVerified)
                            )
                        }
                        Text(
                            text = "7/24 Saat Eksperi & Koleksiyon Danışmanı (Canlı)",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                // Security E2EE indicator
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ImperialGoldContainer.copy(alpha = 0.3f))
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = ImperialGold, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "256-bit AES Uçtan Uca Şifreli Kanal • Mesajlarınız Gizlilik Korumasındadır",
                        fontSize = 10.sp,
                        color = ImperialGoldLight
                    )
                }
            }
        }

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                ChatMessageBubble(message = msg)
            }
        }

        // Quick Suggestion Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickQuestions) { question ->
                Surface(
                    color = ObsidianSurface,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(0.8.dp, ImperialGoldBorder),
                    modifier = Modifier.clickable {
                        viewModel.sendChatMessage(question)
                    }
                ) {
                    Text(
                        text = question,
                        fontSize = 11.sp,
                        color = ImperialGoldLight,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Input Field Bar
        Surface(
            color = ObsidianSurface,
            border = BorderStroke(1.dp, ImperialGoldBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 70.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Eksperinize soru sorun...", color = TextMuted, fontSize = 13.sp) },
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
                    maxLines = 3
                )

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            val text = inputText
                            inputText = ""
                            viewModel.sendChatMessage(text)
                        }
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .background(ImperialGold, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Gönder",
                        tint = ObsidianBlack,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatMessageBubble(message: LiveChatMessage) {
    val isUser = message.sender == "User"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (isUser) ImperialGoldContainer else ObsidianSurface,
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = if (isUser) 14.dp else 2.dp,
                bottomEnd = if (isUser) 2.dp else 14.dp
            ),
            border = BorderStroke(1.dp, if (isUser) ImperialGold else ImperialGoldBorder),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isUser) "Siz (Koleksiyoner)" else "VIP Eksper Danışmanı",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isUser) ImperialGoldLight else ImperialGold
                    )
                    Text(
                        text = message.timestamp,
                        fontSize = 9.sp,
                        color = TextMuted
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = message.message,
                    fontSize = 12.sp,
                    color = TextPureWhite,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
