package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.CanteenOrder
import com.example.model.CartItem
import com.example.model.FoodCategory
import com.example.model.FoodItem
import com.example.model.FoodViewMode
import com.example.model.MenuSortOption
import com.example.ui.components.CategoryChipRow
import com.example.ui.components.FoodDetailDialog
import com.example.ui.components.FoodItemCard
import com.example.ui.components.FoodItemListRow
import com.example.ui.components.MenuOptionsSheet
import java.util.Locale

@Composable
fun MenuScreen(
  foodItems: List<FoodItem>,
  allFoodItemsGrouped: Map<FoodCategory, List<FoodItem>>,
  selectedCategory: FoodCategory,
  searchQuery: String,
  viewMode: FoodViewMode,
  sortOption: MenuSortOption,
  onlyVegetarian: Boolean,
  showMenuOptionsSheet: Boolean,
  selectedFoodDetail: FoodItem?,
  cartItems: Map<String, CartItem>,
  activeOrder: CanteenOrder?,
  onCategorySelected: (FoodCategory) -> Unit,
  onSearchQueryChanged: (String) -> Unit,
  onViewModeChanged: (FoodViewMode) -> Unit,
  onSortOptionChanged: (MenuSortOption) -> Unit,
  onToggleVegetarian: (Boolean) -> Unit,
  onOpenMenuOptionsSheet: () -> Unit,
  onCloseMenuOptionsSheet: () -> Unit,
  onFoodItemClick: (FoodItem) -> Unit,
  onDismissFoodDetail: () -> Unit,
  onAddToCart: (FoodItem) -> Unit,
  onIncreaseQuantity: (String) -> Unit,
  onDecreaseQuantity: (String) -> Unit,
  onNavigateToCart: () -> Unit,
  onNavigateToOrderTracking: () -> Unit,
  modifier: Modifier = Modifier
) {
  val totalCartCount = cartItems.values.sumOf { it.quantity }
  val cartTotal = cartItems.values.sumOf { it.subtotal } * 1.05

  Box(modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .testTag("menu_list"),
      contentPadding = PaddingValues(bottom = if (totalCartCount > 0) 90.dp else 24.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Active Order Banner (if an order is currently active)
      if (activeOrder != null) {
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 4.dp)
              .clickable { onNavigateToOrderTracking() }
              .testTag("active_order_banner")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
              ) {
                Box(
                  contentAlignment = Alignment.Center,
                  modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                ) {
                  Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(22.dp)
                  )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text(
                    text = "Live Order: ${activeOrder.orderId}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                  )
                  Text(
                    text = "Status: ${activeOrder.status.displayName} (${activeOrder.tokenNumber})",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                  )
                }
              }

              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Track order",
                tint = MaterialTheme.colorScheme.primary
              )
            }
          }
        }
      }

      // 2. Hero Canteen Header
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(140.dp)
          ) {
            Image(
              painter = painterResource(id = R.drawable.img_canteen_hero),
              contentDescription = "Canteen Food Counter",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
            // Gradient Overlay
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(
                  Brush.horizontalGradient(
                    colors = listOf(
                      Color.Black.copy(alpha = 0.8f),
                      Color.Black.copy(alpha = 0.3f)
                    )
                  )
                )
            )
            Column(
              modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
              verticalArrangement = Arrangement.Center
            ) {
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.primary
              ) {
                Text(
                  text = "CAMPUS CANTEEN",
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimary,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "Order Fast, Skip the Queue!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Text(
                text = "Explore full menu with snacks, meals & chilled drinks",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.9f)
              )
            }
          }
        }
      }

      // 3. Search Bar & Menu Options Action
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            placeholder = { Text("Search burger, pizza, drinks...") },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
              )
            },
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChanged("") }) {
                  Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = MaterialTheme.colorScheme.primary,
              unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
              focusedContainerColor = MaterialTheme.colorScheme.surface,
              unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
              .weight(1f)
              .testTag("menu_search_input")
          )

          // Menu Options Action Button
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier
              .clickable { onOpenMenuOptionsSheet() }
              .testTag("btn_menu_options_trigger")
          ) {
            Box(
              modifier = Modifier
                .size(54.dp),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "Menu Options",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
              )
            }
          }
        }
      }

      // 4. Dedicated "Menu Options & Food Catalog" Quick Access Card
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
          ),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onOpenMenuOptionsSheet() }
            .testTag("menu_options_card")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.MenuBook,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "Menu Options & Food Catalog",
                  style = MaterialTheme.typography.labelLarge,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = if (onlyVegetarian) "Diet: Veg Only • Sort: ${sortOption.displayName}" else "Sort: ${sortOption.displayName} • All Foods",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.primary
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.surface
            ) {
              Text(
                text = "Options",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
        }
      }

      // 5. Category Selector Chips
      item {
        Column {
          Text(
            text = "Categories",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
          )
          CategoryChipRow(
            selectedCategory = selectedCategory,
            onCategorySelected = onCategorySelected
          )
        }
      }

      // 6. Section Header with View Layout Switcher (Cards vs List)
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = if (selectedCategory == FoodCategory.ALL) "List of Foods (${foodItems.size})" else "${selectedCategory.displayName} (${foodItems.size})",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            if (onlyVegetarian) {
              Text(
                text = "Showing Vegetarian dishes only",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Medium
              )
            }
          }

          // View Switcher (Cards vs List)
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
          ) {
            Row(
              modifier = Modifier.padding(2.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              IconButton(
                onClick = { onViewModeChanged(FoodViewMode.CARDS) },
                modifier = Modifier
                  .size(34.dp)
                  .testTag("toggle_cards_view")
              ) {
                Icon(
                  imageVector = Icons.Default.GridView,
                  contentDescription = "Card view",
                  tint = if (viewMode == FoodViewMode.CARDS) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(18.dp)
                )
              }

              IconButton(
                onClick = { onViewModeChanged(FoodViewMode.LIST) },
                modifier = Modifier
                  .size(34.dp)
                  .testTag("toggle_list_view")
              ) {
                Icon(
                  imageVector = Icons.Default.ViewList,
                  contentDescription = "List view",
                  tint = if (viewMode == FoodViewMode.LIST) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }

      // 7. Food Items (Cards or List depending on user selection)
      if (foodItems.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(32.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = Icons.Default.Fastfood,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "No food items found",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
              )
              Text(
                text = if (onlyVegetarian) "Try turning off the Vegetarian filter in Menu Options." else "Try searching for something else or pick another category.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      } else {
        if (viewMode == FoodViewMode.CARDS) {
          items(
            items = foodItems,
            key = { "card_${it.id}" }
          ) { item ->
            val quantity = cartItems[item.id]?.quantity ?: 0
            FoodItemCard(
              foodItem = item,
              cartQuantity = quantity,
              onAddToCart = { onAddToCart(item) },
              onIncreaseQuantity = { onIncreaseQuantity(item.id) },
              onDecreaseQuantity = { onDecreaseQuantity(item.id) },
              modifier = Modifier
                .padding(horizontal = 16.dp)
                .clickable { onFoodItemClick(item) }
            )
          }
        } else {
          // Compact Food List View
          items(
            items = foodItems,
            key = { "list_${it.id}" }
          ) { item ->
            val quantity = cartItems[item.id]?.quantity ?: 0
            FoodItemListRow(
              foodItem = item,
              cartQuantity = quantity,
              onAddToCart = { onAddToCart(item) },
              onIncreaseQuantity = { onIncreaseQuantity(item.id) },
              onDecreaseQuantity = { onDecreaseQuantity(item.id) },
              onItemClick = { onFoodItemClick(item) },
              modifier = Modifier.padding(horizontal = 16.dp)
            )
          }
        }
      }
    }

    // 8. Floating Bottom Cart Bar (appears when items are in cart)
    if (totalCartCount > 0) {
      Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 8.dp,
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .fillMaxWidth()
          .padding(16.dp)
          .clickable { onNavigateToCart() }
          .testTag("floating_cart_bar")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier
                .size(36.dp)
                .background(Color.White.copy(alpha = 0.2f), CircleShape)
            ) {
              Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "$totalCartCount item${if (totalCartCount > 1) "s" else ""} in cart",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.9f)
              )
              Text(
                text = String.format(Locale.US, "$%.2f total", cartTotal),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "View Cart",
              style = MaterialTheme.typography.labelLarge,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }

    // 9. Menu Options Bottom Sheet (with complete list of food in it!)
    if (showMenuOptionsSheet) {
      MenuOptionsSheet(
        allFoodItemsGrouped = allFoodItemsGrouped,
        currentViewMode = viewMode,
        currentSortOption = sortOption,
        onlyVegetarian = onlyVegetarian,
        cartItems = cartItems,
        onViewModeChanged = onViewModeChanged,
        onSortOptionChanged = onSortOptionChanged,
        onToggleVegetarian = onToggleVegetarian,
        onAddToCart = onAddToCart,
        onIncreaseQuantity = onIncreaseQuantity,
        onDecreaseQuantity = onDecreaseQuantity,
        onFoodItemClick = onFoodItemClick,
        onDismiss = onCloseMenuOptionsSheet
      )
    }

    // 10. Food Detail Inspector Modal
    if (selectedFoodDetail != null) {
      val qty = cartItems[selectedFoodDetail.id]?.quantity ?: 0
      FoodDetailDialog(
        foodItem = selectedFoodDetail,
        cartQuantity = qty,
        onAddToCart = { onAddToCart(selectedFoodDetail) },
        onDismiss = onDismissFoodDetail
      )
    }
  }
}
