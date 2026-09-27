package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.CartItem
import com.example.model.FoodCategory
import com.example.model.FoodItem
import com.example.model.FoodViewMode
import com.example.model.MenuSortOption
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuOptionsSheet(
  allFoodItemsGrouped: Map<FoodCategory, List<FoodItem>>,
  currentViewMode: FoodViewMode,
  currentSortOption: MenuSortOption,
  onlyVegetarian: Boolean,
  cartItems: Map<String, CartItem>,
  onViewModeChanged: (FoodViewMode) -> Unit,
  onSortOptionChanged: (MenuSortOption) -> Unit,
  onToggleVegetarian: (Boolean) -> Unit,
  onAddToCart: (FoodItem) -> Unit,
  onIncreaseQuantity: (String) -> Unit,
  onDecreaseQuantity: (String) -> Unit,
  onFoodItemClick: (FoodItem) -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
    modifier = modifier.testTag("menu_options_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.88f)
        .padding(horizontal = 16.dp)
    ) {
      // Top Sheet Title Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primaryContainer)
          ) {
            Icon(
              imageVector = Icons.Default.MenuBook,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onPrimaryContainer,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "Menu Options & Food Catalog",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Customize layout, sort, filters & explore foods",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        IconButton(
          onClick = onDismiss,
          modifier = Modifier.testTag("btn_close_menu_options")
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close options"
          )
        }
      }

      HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Section 1: Display Mode (Card vs List)
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "Food Display Style",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
              )

              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                // Card View Option
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = if (currentViewMode == FoodViewMode.CARDS) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                  shadowElevation = if (currentViewMode == FoodViewMode.CARDS) 2.dp else 0.dp,
                  modifier = Modifier
                    .weight(1f)
                    .clickable { onViewModeChanged(FoodViewMode.CARDS) }
                    .testTag("option_view_cards")
                ) {
                  Row(
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.GridView,
                      contentDescription = null,
                      tint = if (currentViewMode == FoodViewMode.CARDS) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                      modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = "Card Cards",
                      style = MaterialTheme.typography.labelMedium,
                      fontWeight = FontWeight.Bold,
                      color = if (currentViewMode == FoodViewMode.CARDS) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                  }
                }

                // List View Option
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = if (currentViewMode == FoodViewMode.LIST) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                  shadowElevation = if (currentViewMode == FoodViewMode.LIST) 2.dp else 0.dp,
                  modifier = Modifier
                    .weight(1f)
                    .clickable { onViewModeChanged(FoodViewMode.LIST) }
                    .testTag("option_view_list")
                ) {
                  Row(
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.ViewList,
                      contentDescription = null,
                      tint = if (currentViewMode == FoodViewMode.LIST) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                      modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = "Food List",
                      style = MaterialTheme.typography.labelMedium,
                      fontWeight = FontWeight.Bold,
                      color = if (currentViewMode == FoodViewMode.LIST) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                  }
                }
              }
            }
          }
        }

        // Section 2: Diet Filter & Sorting
        item {
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              // Vegetarian switch
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(
                    text = "Vegetarian Only 🌱",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "Show only 100% veg snacks and meals",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }

                Switch(
                  checked = onlyVegetarian,
                  onCheckedChange = onToggleVegetarian,
                  colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                  ),
                  modifier = Modifier.testTag("switch_veg_only")
                )
              }

              Spacer(modifier = Modifier.height(12.dp))
              HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
              Spacer(modifier = Modifier.height(10.dp))

              // Sorting selection chips
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 6.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Sort,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp),
                  tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Sort Food By",
                  style = MaterialTheme.typography.labelLarge,
                  fontWeight = FontWeight.Bold
                )
              }

              Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                MenuSortOption.entries.forEach { sortOption ->
                  val isSelected = sortOption == currentSortOption
                  Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                      .fillMaxWidth()
                      .clickable { onSortOptionChanged(sortOption) }
                      .testTag("sort_option_${sortOption.name.lowercase()}")
                  ) {
                    Row(
                      modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Text(
                        text = sortOption.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                      )

                      if (isSelected) {
                        Icon(
                          imageVector = Icons.Default.Check,
                          contentDescription = null,
                          tint = MaterialTheme.colorScheme.primary,
                          modifier = Modifier.size(18.dp)
                        )
                      }
                    }
                  }
                }
              }
            }
          }
        }

        // Section 3: COMPLETE LIST OF FOOD IN IT (Categorized)
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Complete List of Foods in Menu",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )

            val totalItems = allFoodItemsGrouped.values.sumOf { it.size }
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = MaterialTheme.colorScheme.primaryContainer
            ) {
              Text(
                text = "$totalItems Items",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
        }

        // Categorized Food Items
        allFoodItemsGrouped.forEach { (category, items) ->
          item {
            // Category header
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  val icon = when (category) {
                    FoodCategory.ALL -> Icons.Default.MenuBook
                    FoodCategory.SNACKS -> Icons.Default.Fastfood
                    FoodCategory.MEALS -> Icons.Default.Restaurant
                    FoodCategory.DRINKS -> Icons.Default.LocalBar
                  }
                  Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "${category.displayName} (${items.size})",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                  )
                }

                Text(
                  text = "Price from ${String.format(Locale.US, "$%.2f", items.minOfOrNull { it.price } ?: 0.0)}",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }

          items(items = items, key = { "sheet_${it.id}" }) { foodItem ->
            val qty = cartItems[foodItem.id]?.quantity ?: 0
            FoodItemListRow(
              foodItem = foodItem,
              cartQuantity = qty,
              onAddToCart = { onAddToCart(foodItem) },
              onIncreaseQuantity = { onIncreaseQuantity(foodItem.id) },
              onDecreaseQuantity = { onDecreaseQuantity(foodItem.id) },
              onItemClick = { onFoodItemClick(foodItem) }
            )
          }
        }
      }

      // Bottom Done Button
      Button(
        onClick = onDismiss,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 8.dp)
          .height(48.dp)
          .testTag("btn_done_menu_options")
      ) {
        Text(
          text = "Close & Apply to Menu",
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}
