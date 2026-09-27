package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.CanteenOrder
import com.example.ui.CanteenScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CanteenTopBar(
  currentScreen: CanteenScreen,
  cartCount: Int,
  activeOrder: CanteenOrder?,
  onNavigateBack: () -> Unit,
  onNavigateToCart: () -> Unit,
  onNavigateToOrderTracking: () -> Unit,
  onOpenMenuOptions: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  CenterAlignedTopAppBar(
    modifier = modifier.testTag("canteen_top_bar"),
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
      containerColor = MaterialTheme.colorScheme.surface,
      titleContentColor = MaterialTheme.colorScheme.onSurface
    ),
    navigationIcon = {
      if (currentScreen != CanteenScreen.MENU) {
        IconButton(
          onClick = onNavigateBack,
          modifier = Modifier.testTag("top_bar_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onSurface
          )
        }
      } else {
        // Canteen logo button that also opens Menu Options
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(start = 12.dp)
        ) {
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primaryContainer)
              .clickable { onOpenMenuOptions() }
          ) {
            Icon(
              imageVector = Icons.Default.Restaurant,
              contentDescription = "Open Menu Options",
              tint = MaterialTheme.colorScheme.onPrimaryContainer,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    },
    title = {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
          text = when (currentScreen) {
            CanteenScreen.MENU -> "Smart Canteen"
            CanteenScreen.CART -> "My Cart"
            CanteenScreen.ORDER_TRACKING -> "Order Status"
          },
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        if (currentScreen == CanteenScreen.MENU) {
          Text(
            text = "Campus Dining Hall",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
          )
        }
      }
    },
    actions = {
      // Menu Options Button on the Menu screen
      if (currentScreen == CanteenScreen.MENU) {
        IconButton(
          onClick = onOpenMenuOptions,
          modifier = Modifier.testTag("top_bar_menu_options_button")
        ) {
          Icon(
            imageVector = Icons.Default.MenuBook,
            contentDescription = "Menu Options and Food Catalog",
            tint = MaterialTheme.colorScheme.primary
          )
        }
      }

      // If there's an active order and we're not on order tracking, show quick status pill
      if (activeOrder != null && currentScreen != CanteenScreen.ORDER_TRACKING) {
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = MaterialTheme.colorScheme.primaryContainer,
          modifier = Modifier
            .clickable { onNavigateToOrderTracking() }
            .testTag("top_bar_order_pill")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.NotificationsActive,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = activeOrder.status.displayName,
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
          }
        }
      }

      // Cart icon with badge
      if (currentScreen != CanteenScreen.CART) {
        IconButton(
          onClick = onNavigateToCart,
          modifier = Modifier
            .padding(end = 4.dp)
            .testTag("top_bar_cart_button")
        ) {
          BadgedBox(
            badge = {
              if (cartCount > 0) {
                Badge(
                  containerColor = MaterialTheme.colorScheme.primary,
                  contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                  Text(
                    text = "$cartCount",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.testTag("cart_badge_count")
                  )
                }
              }
            }
          ) {
            Icon(
              imageVector = Icons.Default.ShoppingCart,
              contentDescription = "Shopping Cart",
              tint = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }
    }
  )
}
