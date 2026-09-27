package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.model.FoodItem
import java.util.Locale

@Composable
fun FoodItemListRow(
  foodItem: FoodItem,
  cartQuantity: Int,
  onAddToCart: () -> Unit,
  onIncreaseQuantity: () -> Unit,
  onDecreaseQuantity: () -> Unit,
  onItemClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = modifier
      .fillMaxWidth()
      .clickable { onItemClick() }
      .testTag("food_list_row_${foodItem.id}")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Food Thumbnail with badge overlay
      Box(
        modifier = Modifier
          .size(76.dp)
          .clip(RoundedCornerShape(12.dp))
      ) {
        Image(
          painter = painterResource(id = foodItem.imageRes),
          contentDescription = foodItem.name,
          contentScale = ContentScale.Crop,
          modifier = Modifier.size(76.dp)
        )

        // Dietary indicator badge in top left corner of image
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .padding(4.dp)
            .size(14.dp)
            .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(2.dp))
            .border(
              width = 1.dp,
              color = if (foodItem.isVegetarian) Color(0xFF2E7D32) else Color(0xFFD32F2F),
              shape = RoundedCornerShape(2.dp)
            )
        ) {
          Box(
            modifier = Modifier
              .size(6.dp)
              .background(
                color = if (foodItem.isVegetarian) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                shape = CircleShape
              )
          )
        }
      }

      Spacer(modifier = Modifier.width(12.dp))

      // Info Column
      Column(
        modifier = Modifier.weight(1f)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = foodItem.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
          )

          // Price
          Text(
            text = String.format(Locale.US, "$%.2f", foodItem.price),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 6.dp)
          )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Description
        Text(
          text = foodItem.description,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          // Category and prep time
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
            ) {
              Text(
                text = foodItem.category.displayName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(13.dp)
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = "${foodItem.prepTimeMinutes}m",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          // Cart control: Add or Stepper
          if (cartQuantity > 0) {
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = MaterialTheme.colorScheme.primaryContainer,
              modifier = Modifier.testTag("list_stepper_${foodItem.id}")
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 2.dp, vertical = 1.dp)
              ) {
                IconButton(
                  onClick = onDecreaseQuantity,
                  modifier = Modifier
                    .size(28.dp)
                    .testTag("list_btn_decrease_${foodItem.id}")
                ) {
                  Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(14.dp)
                  )
                }

                Text(
                  text = "$cartQuantity",
                  style = MaterialTheme.typography.labelLarge,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer,
                  modifier = Modifier.padding(horizontal = 6.dp)
                )

                IconButton(
                  onClick = onIncreaseQuantity,
                  modifier = Modifier
                    .size(28.dp)
                    .testTag("list_btn_increase_${foodItem.id}")
                ) {
                  Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(14.dp)
                  )
                }
              }
            }
          } else {
            Button(
              onClick = onAddToCart,
              shape = RoundedCornerShape(16.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
              ),
              contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = 12.dp,
                vertical = 4.dp
              ),
              modifier = Modifier
                .height(32.dp)
                .testTag("list_btn_add_${foodItem.id}")
            ) {
              Text(
                text = "Add +",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }
  }
}
