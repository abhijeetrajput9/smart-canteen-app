package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.FoodItem
import com.example.ui.CanteenScreen
import com.example.ui.CanteenViewModel
import com.example.ui.components.CanteenBottomNav
import com.example.ui.components.CanteenTopBar
import com.example.ui.screens.CartScreen
import com.example.ui.screens.MenuScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

  private val viewModel: CanteenViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        CanteenApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun CanteenApp(viewModel: CanteenViewModel) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val snackbarHostState = remember { SnackbarHostState() }

  // Handle hardware / gesture back
  BackHandler(enabled = uiState.currentScreen != CanteenScreen.MENU) {
    viewModel.navigateTo(CanteenScreen.MENU)
  }

  // Show snackbar messages
  LaunchedEffect(uiState.snackbarMessage) {
    uiState.snackbarMessage?.let { message ->
      snackbarHostState.showSnackbar(message)
      viewModel.clearSnackbarMessage()
    }
  }

  val filteredMenuItems = remember(
    uiState.selectedCategory,
    uiState.searchQuery,
    uiState.onlyVegetarian,
    uiState.sortOption
  ) {
    viewModel.getFilteredMenuItems()
  }

  val allGroupedFoods = remember(uiState.onlyVegetarian, uiState.sortOption) {
    viewModel.getAllGroupedFoodItems()
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    topBar = {
      CanteenTopBar(
        currentScreen = uiState.currentScreen,
        cartCount = uiState.totalCartCount,
        activeOrder = uiState.activeOrder,
        onNavigateBack = { viewModel.navigateTo(CanteenScreen.MENU) },
        onNavigateToCart = { viewModel.navigateTo(CanteenScreen.CART) },
        onNavigateToOrderTracking = { viewModel.navigateTo(CanteenScreen.ORDER_TRACKING) },
        onOpenMenuOptions = { viewModel.openMenuOptionsSheet() }
      )
    },
    bottomBar = {
      CanteenBottomNav(
        currentScreen = uiState.currentScreen,
        cartCount = uiState.totalCartCount,
        hasActiveOrder = uiState.activeOrder != null,
        onNavigate = { screen -> viewModel.navigateTo(screen) }
      )
    }
  ) { innerPadding ->
    when (uiState.currentScreen) {
      CanteenScreen.MENU -> {
        MenuScreen(
          foodItems = filteredMenuItems,
          allFoodItemsGrouped = allGroupedFoods,
          selectedCategory = uiState.selectedCategory,
          searchQuery = uiState.searchQuery,
          viewMode = uiState.viewMode,
          sortOption = uiState.sortOption,
          onlyVegetarian = uiState.onlyVegetarian,
          showMenuOptionsSheet = uiState.showMenuOptionsSheet,
          selectedFoodDetail = uiState.selectedFoodDetail,
          cartItems = uiState.cartItems,
          activeOrder = uiState.activeOrder,
          onCategorySelected = { category -> viewModel.selectCategory(category) },
          onSearchQueryChanged = { query -> viewModel.updateSearchQuery(query) },
          onViewModeChanged = { mode -> viewModel.setViewMode(mode) },
          onSortOptionChanged = { sort -> viewModel.setSortOption(sort) },
          onToggleVegetarian = { onlyVeg -> viewModel.toggleVegetarian(onlyVeg) },
          onOpenMenuOptionsSheet = { viewModel.openMenuOptionsSheet() },
          onCloseMenuOptionsSheet = { viewModel.closeMenuOptionsSheet() },
          onFoodItemClick = { item -> viewModel.showFoodDetail(item) },
          onDismissFoodDetail = { viewModel.dismissFoodDetail() },
          onAddToCart = { item: FoodItem -> viewModel.addToCart(item) },
          onIncreaseQuantity = { id -> viewModel.increaseQuantity(id) },
          onDecreaseQuantity = { id -> viewModel.decreaseQuantity(id) },
          onNavigateToCart = { viewModel.navigateTo(CanteenScreen.CART) },
          onNavigateToOrderTracking = { viewModel.navigateTo(CanteenScreen.ORDER_TRACKING) },
          modifier = Modifier.padding(innerPadding)
        )
      }

      CanteenScreen.CART -> {
        CartScreen(
          cartItems = uiState.cartItems,
          onIncreaseQuantity = { id -> viewModel.increaseQuantity(id) },
          onDecreaseQuantity = { id -> viewModel.decreaseQuantity(id) },
          onRemoveItem = { id -> viewModel.removeFromCart(id) },
          onClearCart = { viewModel.clearCart() },
          onPlaceOrder = { viewModel.placeOrder() },
          onBrowseMenu = { viewModel.navigateTo(CanteenScreen.MENU) },
          modifier = Modifier.padding(innerPadding)
        )
      }

      CanteenScreen.ORDER_TRACKING -> {
        OrderTrackingScreen(
          order = uiState.activeOrder,
          onUpdateStatus = { newStatus -> viewModel.updateOrderStatus(newStatus) },
          onStartNewOrder = { viewModel.startNewOrder() },
          onBackToMenu = { viewModel.navigateTo(CanteenScreen.MENU) },
          modifier = Modifier.padding(innerPadding)
        )
      }
    }
  }
}
