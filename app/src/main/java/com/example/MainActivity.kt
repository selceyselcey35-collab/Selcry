package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppDestination
import com.example.ui.MarketplaceViewModel
import com.example.ui.components.LegalPolicyConsentDialog
import com.example.ui.components.LuxuryBottomBar
import com.example.ui.components.LuxuryTopBar
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ObsidianBlack

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ChronosAppRoot()
            }
        }
    }
}

@Composable
fun ChronosAppRoot(viewModel: MarketplaceViewModel = viewModel()) {
    val destination by viewModel.currentDestination.collectAsState()
    val comparisonWatches by viewModel.comparisonWatches.collectAsState()
    val selectedWatch by viewModel.selectedWatch.collectAsState()
    val showPolicyModal by viewModel.showPolicyModal.collectAsState()

    // Handle System Back Button
    BackHandler(enabled = destination != AppDestination.MARKETPLACE) {
        if (destination == AppDestination.WATCH_DETAIL ||
            destination == AppDestination.CHECKOUT_INVOICE ||
            destination == AppDestination.LIVE_CONCIERGE
        ) {
            viewModel.navigateTo(AppDestination.MARKETPLACE)
        } else {
            viewModel.navigateTo(AppDestination.MARKETPLACE)
        }
    }

    Scaffold(
        containerColor = ObsidianBlack,
        topBar = {
            LuxuryTopBar(
                currentDestination = destination,
                comparisonCount = comparisonWatches.size,
                onNavigate = { viewModel.navigateTo(it) },
                onOpenPolicy = { viewModel.openPolicyModal() }
            )
        },
        bottomBar = {
            LuxuryBottomBar(
                currentDestination = destination,
                onNavigate = { viewModel.navigateTo(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianBlack)
                .padding(innerPadding)
        ) {
            when (destination) {
                AppDestination.MARKETPLACE -> {
                    HomeScreen(
                        viewModel = viewModel,
                        onOpenWatchDetail = { watch ->
                            viewModel.openWatchDetail(watch)
                        }
                    )
                }

                AppDestination.WATCH_DETAIL -> {
                    selectedWatch?.let { watch ->
                        WatchDetailScreen(
                            watch = watch,
                            viewModel = viewModel,
                            onBack = { viewModel.navigateTo(AppDestination.MARKETPLACE) }
                        )
                    } ?: HomeScreen(
                        viewModel = viewModel,
                        onOpenWatchDetail = { viewModel.openWatchDetail(it) }
                    )
                }

                AppDestination.COMPARISON -> {
                    ComparisonScreen(
                        viewModel = viewModel,
                        onOpenWatchDetail = { viewModel.openWatchDetail(it) }
                    )
                }

                AppDestination.EXPERTISE_PORTAL -> {
                    ExpertisePortalScreen(viewModel = viewModel)
                }

                AppDestination.DROPSHIPPING_HUB -> {
                    DropshippingHubScreen(
                        viewModel = viewModel,
                        onOpenWatchDetail = { viewModel.openWatchDetail(it) }
                    )
                }

                AppDestination.CHECKOUT_INVOICE -> {
                    selectedWatch?.let { watch ->
                        CheckoutInvoiceScreen(
                            watch = watch,
                            viewModel = viewModel,
                            onBack = { viewModel.navigateTo(AppDestination.WATCH_DETAIL) }
                        )
                    } ?: HomeScreen(
                        viewModel = viewModel,
                        onOpenWatchDetail = { viewModel.openWatchDetail(it) }
                    )
                }

                AppDestination.AUTOMATED_RETURNS -> {
                    AutomatedReturnsScreen(viewModel = viewModel)
                }

                AppDestination.LIVE_CONCIERGE -> {
                    LiveConciergeScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.navigateTo(AppDestination.MARKETPLACE) }
                    )
                }
            }
        }
    }

    // Mandatory Legal Policies Consent Modal (appears on entry or when clicked)
    if (showPolicyModal) {
        LegalPolicyConsentDialog(
            onAccept = { viewModel.acceptPolicies() },
            onDismiss = { viewModel.closePolicyModal() }
        )
    }
}
