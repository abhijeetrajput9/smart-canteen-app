package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.unit.sp
import com.example.model.FoodItem
import java.util.Locale

@Composable
fun FoodItemCard(
  foodItem: FoodItem,
  cartQuantity: Int,
  onAddToCart: () -> Unit,
  onIncreaseQuantity: () -> Unit,
  onDecreaseQuantity: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier
      .fillMaxWidth()
      .testTag("food_card_${foodItem.id}")
  ) {
    Column {
      // Food Image Container
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(160.dp)
      ) {
        Image(
          painter = painterResource(id = foodItem.imageRes),
          contentDescription = foodItem.name,
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
        )

        // Gradient overlay or badges
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Category tag
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
            shadowElevation = 2.dp
          ) {
            Text(
              text = foodItem.category.displayName,
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }

          // Optional highlight badge (e.g. Bestseller, Campus Fav)
          if (foodItem.badge != null) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.primary,
              shadowElevation = 2.dp
            ) {
              Text(
                text = foodItem.badge,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
        }
      }

      // Food Details
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          // Food Title & Veg/Non-Veg indicator
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            // Veg/Non-veg symbol square
            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier
                .size(16.dp)
                .border(
                  width = 1.5.dp,
                  color = if (foodItem.isVegetarian) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                  shape = RoundedCornerShape(3.dp)
                )
            ) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .background(
                    color = if (foodItem.isVegetarian) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                    shape = CircleShape
                  )
              )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
              text = foodItem.name,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }

          // Price Tag
          Text(
            text = String.format(Locale.US, "$%.2f", foodItem.price),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Description
        Text(
          text = foodItem.description,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Bottom row: Prep time & Action button
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.AccessTime,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${foodItem.prepTimeMinutes} mins",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          // Cart Interaction: "Add to Cart" or Quantity Stepper
          if (cartQuantity > 0) {
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = MaterialTheme.colorScheme.primaryContainer,
              modifier = Modifier.testTag("quantity_stepper_${foodItem.id}")
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
              ) {
                IconButton(
                  onClick = onDecreaseQuantity,
                  modifier = Modifier
                    .size(32.dp)
                    .testTag("btn_decrease_${foodItem.id}")
                ) {
                  Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease quantity",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(16.dp)
                  )
                }

                Text(
                  text = "$cartQuantity",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer,
                  modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .testTag("quantity_text_${foodItem.id}")
                )

                IconButton(
                  onClick = onIncreaseQuantity,
                  modifier = Modifier
                    .size(32.dp)
                    .testTag("btn_increase_${foodItem.id}")
                ) {
                  Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase quantity",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(16.dp)
                  )
                }
              }
            }
          } else {
            Button(
              onClick = onAddToCart,
              shape = RoundedCornerShape(20.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
              ),
              modifier = Modifier
                .height(38.dp)
                .testTag("btn_add_to_cart_${foodItem.id}")
            ) {
              Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Add to Cart",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }
    }
  }
}
