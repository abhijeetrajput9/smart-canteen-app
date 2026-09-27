package com.example.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.FoodCategory

@Composable
fun CategoryChipRow(
  selectedCategory: FoodCategory,
  onCategorySelected: (FoodCategory) -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Row(
    modifier = modifier
      .horizontalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 6.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    FoodCategory.entries.forEach { category ->
      val isSelected = category == selectedCategory
      val icon: ImageVector = when (category) {
        FoodCategory.ALL -> Icons.Default.RestaurantMenu
        FoodCategory.SNACKS -> Icons.Default.Fastfood
        FoodCategory.MEALS -> Icons.Default.Restaurant
        FoodCategory.DRINKS -> Icons.Default.LocalBar
      }

      FilterChip(
        selected = isSelected,
        onClick = { onCategorySelected(category) },
        label = {
          Text(
            text = category.displayName,
            style = MaterialTheme.typography.labelLarge
          )
        },
        leadingIcon = {
          Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
        },
        shape = RoundedCornerShape(20.dp),
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = MaterialTheme.colorScheme.primary,
          selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
          selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
          containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
          labelColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier.testTag("category_chip_${category.name.lowercase()}")
      )
    }
  }
}
