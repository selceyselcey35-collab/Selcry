package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.MarketplaceRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Chronos", appName)
    }

    @Test
    fun `verify catalog and invoice tax calculation compliance`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repository = MarketplaceRepository(context)
        val watches = repository.staticCatalog
        assertTrue("Catalog should contain curated luxury watches", watches.isNotEmpty())

        val watch = watches.first()
        val invoice = repository.generateOfficialInvoice(
            watch = watch,
            buyerFullName = "Ahmet Yılmaz",
            buyerIdOrTaxNo = "12345678901",
            buyerAddress = "Bebek Mah. Cevdetpaşa Cad. No:12 Beşiktaş / İstanbul"
        )

        assertEquals("Total must match watch price", watch.priceTry, invoice.totalAmountTry)
        assertTrue("VAT amount must be positive", invoice.vatAmountTry > 0)
        assertTrue("Subtotal must be positive", invoice.subtotalTry > 0)
        assertEquals("Subtotal + VAT must equal total price", watch.priceTry, invoice.subtotalTry + invoice.vatAmountTry)
    }

    @Test
    fun `verify user reviews are properly added and accessible`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repository = MarketplaceRepository(context)
        val watch = repository.staticCatalog.first()
        
        kotlinx.coroutines.runBlocking {
            val review = repository.addReview(
                watchId = watch.id,
                userName = "Test Koleksiyoner",
                rating = 5,
                title = "Harika Kondisyon",
                comment = "Zırhlı teslimat ve sertifika tam tekmil geldi."
            )
            assertEquals(watch.id, review.watchId)
            assertEquals("Test Koleksiyoner", review.userName)
            assertEquals(5, review.rating)
            assertTrue(review.isVerifiedPurchase)
        }
    }
}
