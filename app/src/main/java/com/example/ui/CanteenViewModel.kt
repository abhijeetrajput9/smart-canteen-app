package com.example.ui

import androidx.lifecycle.ViewModel
import com.example.data.CanteenRepository
import com.example.model.CartItem
import com.example.model.CanteenOrder
import com.example.model.FoodCategory
import com.example.model.FoodItem
import com.example.model.FoodViewMode
import com.example.model.MenuSortOption
import com.example.model.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale
import kotlin.random.Random

enum class CanteenScreen {
  MENU,
  CART,
  ORDER_TRACKING
}

data class CanteenUiState(
  val currentScreen: CanteenScreen = CanteenScreen.MENU,
  val selectedCategory: FoodCategory = FoodCategory.ALL,
  val searchQuery: String = "",
  val viewMode: FoodViewMode = FoodViewMode.CARDS,
  val sortOption: MenuSortOption = MenuSortOption.DEFAULT,
  val onlyVegetarian: Boolean = false,
  val showMenuOptionsSheet: Boolean = false,
  val selectedFoodDetail: FoodItem? = null,
  val cartItems: Map<String, CartItem> = emptyMap(),
  val activeOrder: CanteenOrder? = null,
  val snackbarMessage: String? = null
) {
  val totalCartCount: Int
    get() = cartItems.values.sumOf { it.quantity }

  val cartSubtotal: Double
    get() = cartItems.values.sumOf { it.subtotal }

  val taxAmount: Double
    get() = cartSubtotal * 0.05 // 5% tax

  val cartTotal: Double
    get() = cartSubtotal + taxAmount
}

class CanteenViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(CanteenUiState())
  val uiState: StateFlow<CanteenUiState> = _uiState.asStateFlow()

  fun navigateTo(screen: CanteenScreen) {
    _uiState.update { it.copy(currentScreen = screen) }
  }

  fun selectCategory(category: FoodCategory) {
    _uiState.update { it.copy(selectedCategory = category) }
  }

  fun updateSearchQuery(query: String) {
    _uiState.update { it.copy(searchQuery = query) }
  }

  fun setViewMode(viewMode: FoodViewMode) {
    _uiState.update { it.copy(viewMode = viewMode) }
  }

  fun setSortOption(sortOption: MenuSortOption) {
    _uiState.update { it.copy(sortOption = sortOption) }
  }

  fun toggleVegetarian(onlyVeg: Boolean) {
    _uiState.update { it.copy(onlyVegetarian = onlyVeg) }
  }

  fun openMenuOptionsSheet() {
    _uiState.update { it.copy(showMenuOptionsSheet = true) }
  }

  fun closeMenuOptionsSheet() {
    _uiState.update { it.copy(showMenuOptionsSheet = false) }
  }

  fun showFoodDetail(item: FoodItem) {
    _uiState.update { it.copy(selectedFoodDetail = item) }
  }

  fun dismissFoodDetail() {
    _uiState.update { it.copy(selectedFoodDetail = null) }
  }

  fun clearSnackbarMessage() {
    _uiState.update { it.copy(snackbarMessage = null) }
  }

  fun addToCart(foodItem: FoodItem) {
    _uiState.update { state ->
      val currentCart = state.cartItems.toMutableMap()
      val existing = currentCart[foodItem.id]
      if (existing != null) {
        currentCart[foodItem.id] = existing.copy(quantity = existing.quantity + 1)
      } else {
        currentCart[foodItem.id] = CartItem(foodItem = foodItem, quantity = 1)
      }
      state.copy(
        cartItems = currentCart,
        snackbarMessage = "Added ${foodItem.name} to cart"
      )
    }
  }

  fun increaseQuantity(foodItemId: String) {
    _uiState.update { state ->
      val currentCart = state.cartItems.toMutableMap()
      val existing = currentCart[foodItemId]
      if (existing != null) {
        currentCart[foodItemId] = existing.copy(quantity = existing.quantity + 1)
      }
      state.copy(cartItems = currentCart)
    }
  }

  fun decreaseQuantity(foodItemId: String) {
    _uiState.update { state ->
      val currentCart = state.cartItems.toMutableMap()
      val existing = currentCart[foodItemId]
      if (existing != null) {
        if (existing.quantity > 1) {
          currentCart[foodItemId] = existing.copy(quantity = existing.quantity - 1)
        } else {
          currentCart.remove(foodItemId)
        }
      }
      state.copy(cartItems = currentCart)
    }
  }

  fun removeFromCart(foodItemId: String) {
    _uiState.update { state ->
      val currentCart = state.cartItems.toMutableMap()
      val removed = currentCart.remove(foodItemId)
      state.copy(
        cartItems = currentCart,
        snackbarMessage = removed?.let { "Removed ${it.foodItem.name} from cart" }
      )
    }
  }

  fun clearCart() {
    _uiState.update { it.copy(cartItems = emptyMap()) }
  }

  fun placeOrder(): Boolean {
    val state = _uiState.value
    if (state.cartItems.isEmpty()) return false

    val orderId = String.format(Locale.US, "#SC-%04d", Random.nextInt(1000, 9999))
    val letter = ('A'..'D').random()
    val number = Random.nextInt(10, 99)
    val tokenNumber = "Token $letter-$number"

    val newOrder = CanteenOrder(
      orderId = orderId,
      tokenNumber = tokenNumber,
      items = state.cartItems.values.toList(),
      subtotal = state.cartSubtotal,
      tax = state.taxAmount,
      total = state.cartTotal,
      status = OrderStatus.PLACED,
      pickupCounter = "Counter ${Random.nextInt(1, 4)} (Express Pickup)"
    )

    _uiState.update {
      it.copy(
        cartItems = emptyMap(),
        activeOrder = newOrder,
        currentScreen = CanteenScreen.ORDER_TRACKING,
        snackbarMessage = "Order $orderId placed successfully!"
      )
    }
    return true
  }

  fun updateOrderStatus(newStatus: OrderStatus) {
    _uiState.update { state ->
      val order = state.activeOrder
      if (order != null) {
        state.copy(
          activeOrder = order.copy(status = newStatus),
          snackbarMessage = "Order status updated: ${newStatus.displayName}"
        )
      } else {
        state
      }
    }
  }

  fun startNewOrder() {
    _uiState.update {
      it.copy(
        currentScreen = CanteenScreen.MENU,
        activeOrder = null
      )
    }
  }

  fun getFilteredMenuItems(): List<FoodItem> {
    val state = _uiState.value
    var items = CanteenRepository.getItemsByCategory(state.selectedCategory)

    if (state.onlyVegetarian) {
      items = items.filter { it.isVegetarian }
    }

    if (state.searchQuery.isNotBlank()) {
      items = items.filter {
        it.name.contains(state.searchQuery, ignoreCase = true) ||
          it.description.contains(state.searchQuery, ignoreCase = true)
      }
    }

    return when (state.sortOption) {
      MenuSortOption.DEFAULT -> items
      MenuSortOption.PRICE_LOW_TO_HIGH -> items.sortedBy { it.price }
      MenuSortOption.PRICE_HIGH_TO_LOW -> items.sortedByDescending { it.price }
      MenuSortOption.NAME_A_TO_Z -> items.sortedBy { it.name }
    }
  }

  fun getAllGroupedFoodItems(): Map<FoodCategory, List<FoodItem>> {
    val state = _uiState.value
    val allCategories = listOf(FoodCategory.MEALS, FoodCategory.SNACKS, FoodCategory.DRINKS)
    return allCategories.associateWith { category ->
      var list = CanteenRepository.getItemsByCategory(category)
      if (state.onlyVegetarian) {
        list = list.filter { it.isVegetarian }
      }
      when (state.sortOption) {
        MenuSortOption.DEFAULT -> list
        MenuSortOption.PRICE_LOW_TO_HIGH -> list.sortedBy { it.price }
        MenuSortOption.PRICE_HIGH_TO_LOW -> list.sortedByDescending { it.price }
        MenuSortOption.NAME_A_TO_Z -> list.sortedBy { it.name }
      }
    }
  }
}
