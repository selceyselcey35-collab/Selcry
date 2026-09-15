package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_watches")
data class FavoriteWatchEntity(
    @PrimaryKey val watchId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "comparison_watches")
data class ComparisonWatchEntity(
    @PrimaryKey val watchId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "dropship_items")
data class DropshipEntity(
    @PrimaryKey val watchId: String,
    val customPriceTry: Long,
    val profitMarginTry: Long,
    val supplierDepot: String,
    val salesCount: Int = 0,
    val isLiveInStore: Boolean = true
)

@Entity(tableName = "return_claims")
data class ReturnClaimEntity(
    @PrimaryKey val claimId: String,
    val orderId: String,
    val watchTitle: String,
    val returnReason: String,
    val autoApproved: Boolean,
    val statusText: String,
    val courierCode: String,
    val refundAmount: Long,
    val createdAt: String
)

@Entity(tableName = "watch_reviews")
data class ReviewEntity(
    @PrimaryKey val id: String,
    val watchId: String,
    val userName: String,
    val userBadge: String,
    val rating: Int,
    val date: String,
    val title: String,
    val comment: String,
    val isVerifiedPurchase: Boolean,
    val helpfulCount: Int,
    val timestamp: Long = System.currentTimeMillis()
)

