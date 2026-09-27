package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.example.ui.CanteenScreen

@Composable
fun CanteenBottomNav(
  currentScreen: CanteenScreen,
  cartCount: Int,
  hasActiveOrder: Boolean,
  onNavigate: (CanteenScreen) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    containerColor = MaterialTheme.colorScheme.surface,
    contentColor = MaterialTheme.colorScheme.onSurface,
    modifier = modifier.testTag("canteen_bottom_nav")
  ) {
    NavigationBarItem(
      selected = currentScreen == CanteenScreen.MENU,
      onClick = { onNavigate(CanteenScreen.MENU) },
      icon = {
        Icon(
          imageVector = Icons.Default.RestaurantMenu,
          contentDescription = "Menu"
        )
      },
      label = {
        Text(
          text = "Menu",
          fontWeight = if (currentScreen == CanteenScreen.MENU) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
        indicatorColor = MaterialTheme.colorScheme.primaryContainer
      ),
      modifier = Modifier.testTag("nav_item_menu")
    )

    NavigationBarItem(
      selected = currentScreen == CanteenScreen.CART,
      onClick = { onNavigate(CanteenScreen.CART) },
      icon = {
        BadgedBox(
          badge = {
            if (cartCount > 0) {
              Badge(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
              ) {
                Text(
                  text = "$cartCount",
                  modifier = Modifier.testTag("bottom_nav_cart_badge")
                )
              }
            }
          }
        ) {
          Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Cart"
          )
        }
      },
      label = {
        Text(
          text = "Cart",
          fontWeight = if (currentScreen == CanteenScreen.CART) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
        indicatorColor = MaterialTheme.colorScheme.primaryContainer
      ),
      modifier = Modifier.testTag("nav_item_cart")
    )

    NavigationBarItem(
      selected = currentScreen == CanteenScreen.ORDER_TRACKING,
      onClick = { onNavigate(CanteenScreen.ORDER_TRACKING) },
      icon = {
        BadgedBox(
          badge = {
            if (hasActiveOrder) {
              Badge(
                containerColor = MaterialTheme.colorScheme.primary
              )
            }
          }
        ) {
          Icon(
            imageVector = Icons.Default.ReceiptLong,
            contentDescription = "Order Status"
          )
        }
      },
      label = {
        Text(
          text = "Status",
          fontWeight = if (currentScreen == CanteenScreen.ORDER_TRACKING) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
        indicatorColor = MaterialTheme.colorScheme.primaryContainer
      ),
      modifier = Modifier.testTag("nav_item_status")
    )
  }
}
