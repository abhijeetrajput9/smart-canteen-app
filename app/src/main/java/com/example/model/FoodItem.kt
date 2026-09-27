package com.example.model

import androidx.annotation.DrawableRes

enum class FoodCategory(val displayName: String) {
  ALL("All"),
  SNACKS("Snacks"),
  MEALS("Meals"),
  DRINKS("Drinks")
}

data class FoodItem(
  val id: String,
  val name: String,
  val price: Double,
  val category: FoodCategory,
  val description: String,
  @DrawableRes val imageRes: Int,
  val badge: String? = null,
  val isVegetarian: Boolean = false,
  val prepTimeMinutes: Int = 10
)

data class CartItem(
  val foodItem: FoodItem,
  val quantity: Int
) {
  val subtotal: Double
    get() = foodItem.price * quantity
}

enum class OrderStatus(val displayName: String, val stepIndex: Int, val description: String) {
  PLACED(
    displayName = "Placed",
    stepIndex = 0,
    description = "Order confirmed! Sent to canteen kitchen."
  ),
  PREPARING(
    displayName = "Preparing",
    stepIndex = 1,
    description = "Chefs are cooking your meal fresh."
  ),
  READY(
    displayName = "Ready",
    stepIndex = 2,
    description = "Ready for pickup at the counter! Show your token."
  ),
  COMPLETED(
    displayName = "Completed",
    stepIndex = 3,
    description = "Order collected. Enjoy your meal!"
  )
}

data class CanteenOrder(
  val orderId: String,
  val tokenNumber: String,
  val items: List<CartItem>,
  val subtotal: Double,
  val tax: Double,
  val total: Double,
  val status: OrderStatus = OrderStatus.PLACED,
  val pickupCounter: String = "Counter 2 - Express Pick",
  val placedTimeMillis: Long = System.currentTimeMillis()
)
