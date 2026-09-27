package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.CanteenRepository
import com.example.model.FoodCategory
import com.example.model.FoodViewMode
import com.example.model.MenuSortOption
import com.example.model.OrderStatus
import com.example.ui.CanteenScreen
import com.example.ui.CanteenViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Smart Canteen", appName)
  }

  @Test
  fun `test cart operations and order flow`() {
    val viewModel = CanteenViewModel()
    val burger = CanteenRepository.menuItems.first { it.id == "m1" }

    // Add to cart
    viewModel.addToCart(burger)
    assertEquals(1, viewModel.uiState.value.totalCartCount)
    assertEquals(burger.price, viewModel.uiState.value.cartSubtotal, 0.001)

    // Increase quantity
    viewModel.increaseQuantity(burger.id)
    assertEquals(2, viewModel.uiState.value.totalCartCount)
    assertEquals(burger.price * 2, viewModel.uiState.value.cartSubtotal, 0.001)

    // Decrease quantity
    viewModel.decreaseQuantity(burger.id)
    assertEquals(1, viewModel.uiState.value.totalCartCount)

    // Place order
    val placed = viewModel.placeOrder()
    assertTrue(placed)
    assertEquals(0, viewModel.uiState.value.totalCartCount)
    assertNotNull(viewModel.uiState.value.activeOrder)
    assertEquals(CanteenScreen.ORDER_TRACKING, viewModel.uiState.value.currentScreen)
    assertEquals(OrderStatus.PLACED, viewModel.uiState.value.activeOrder?.status)

    // Simulate status progression: Placed -> Preparing -> Ready -> Completed
    viewModel.updateOrderStatus(OrderStatus.PREPARING)
    assertEquals(OrderStatus.PREPARING, viewModel.uiState.value.activeOrder?.status)

    viewModel.updateOrderStatus(OrderStatus.READY)
    assertEquals(OrderStatus.READY, viewModel.uiState.value.activeOrder?.status)

    viewModel.updateOrderStatus(OrderStatus.COMPLETED)
    assertEquals(OrderStatus.COMPLETED, viewModel.uiState.value.activeOrder?.status)
  }

  @Test
  fun `test menu options, list of foods, view mode and filters`() {
    val viewModel = CanteenViewModel()

    // Verify all food items are present
    val allGrouped = viewModel.getAllGroupedFoodItems()
    assertTrue(allGrouped.containsKey(FoodCategory.MEALS))
    assertTrue(allGrouped.containsKey(FoodCategory.SNACKS))
    assertTrue(allGrouped.containsKey(FoodCategory.DRINKS))
    assertTrue((allGrouped[FoodCategory.MEALS]?.size ?: 0) >= 4)
    assertTrue((allGrouped[FoodCategory.SNACKS]?.size ?: 0) >= 4)
    assertTrue((allGrouped[FoodCategory.DRINKS]?.size ?: 0) >= 4)

    // Verify view mode toggle (Cards vs List)
    assertEquals(FoodViewMode.CARDS, viewModel.uiState.value.viewMode)
    viewModel.setViewMode(FoodViewMode.LIST)
    assertEquals(FoodViewMode.LIST, viewModel.uiState.value.viewMode)

    // Verify Menu Options bottom sheet toggle
    assertFalse(viewModel.uiState.value.showMenuOptionsSheet)
    viewModel.openMenuOptionsSheet()
    assertTrue(viewModel.uiState.value.showMenuOptionsSheet)
    viewModel.closeMenuOptionsSheet()
    assertFalse(viewModel.uiState.value.showMenuOptionsSheet)

    // Verify Vegetarian filter
    viewModel.toggleVegetarian(true)
    val vegFoods = viewModel.getFilteredMenuItems()
    assertTrue(vegFoods.all { it.isVegetarian })

    // Verify Sorting by Price: Low to High
    viewModel.setSortOption(MenuSortOption.PRICE_LOW_TO_HIGH)
    val sortedLowToHigh = viewModel.getFilteredMenuItems()
    for (i in 0 until sortedLowToHigh.size - 1) {
      assertTrue(sortedLowToHigh[i].price <= sortedLowToHigh[i + 1].price)
    }

    // Verify Food Detail Dialog state
    val pizza = CanteenRepository.menuItems.first { it.id == "m3" }
    viewModel.showFoodDetail(pizza)
    assertEquals(pizza, viewModel.uiState.value.selectedFoodDetail)
    viewModel.dismissFoodDetail()
    assertEquals(null, viewModel.uiState.value.selectedFoodDetail)
  }
}
