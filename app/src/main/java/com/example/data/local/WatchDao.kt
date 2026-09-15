package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchDao {
    // Favorites
    @Query("SELECT watchId FROM favorite_watches")
    fun getAllFavoriteIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteWatchEntity)

    @Query("DELETE FROM favorite_watches WHERE watchId = :watchId")
    suspend fun removeFavorite(watchId: String)

    // Comparison List
    @Query("SELECT watchId FROM comparison_watches")
    fun getAllComparisonIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addComparison(comparison: ComparisonWatchEntity)

    @Query("DELETE FROM comparison_watches WHERE watchId = :watchId")
    suspend fun removeComparison(watchId: String)

    @Query("DELETE FROM comparison_watches")
    suspend fun clearComparisons()

    // Dropship
    @Query("SELECT * FROM dropship_items")
    fun getAllDropshipItems(): Flow<List<DropshipEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDropshipItem(item: DropshipEntity)

    @Query("DELETE FROM dropship_items WHERE watchId = :watchId")
    suspend fun removeDropshipItem(watchId: String)

    // Return Claims
    @Query("SELECT * FROM return_claims ORDER BY createdAt DESC")
    fun getAllReturnClaims(): Flow<List<ReturnClaimEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReturnClaim(claim: ReturnClaimEntity)

    // Watch Reviews
    @Query("SELECT * FROM watch_reviews WHERE watchId = :watchId ORDER BY timestamp DESC")
    fun getReviewsForWatch(watchId: String): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM watch_reviews ORDER BY timestamp DESC")
    fun getAllReviews(): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Query("UPDATE watch_reviews SET helpfulCount = helpfulCount + 1 WHERE id = :reviewId")
    suspend fun incrementHelpful(reviewId: String)
}
