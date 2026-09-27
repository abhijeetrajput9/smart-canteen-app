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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.model.FoodItem
import java.util.Locale

@Composable
fun FoodDetailDialog(
  foodItem: FoodItem,
  cartQuantity: Int,
  onAddToCart: () -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
      modifier = modifier
        .fillMaxWidth()
        .testTag("food_detail_dialog")
    ) {
      Column {
        // Image Header with Close Button
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
        ) {
          Image(
            painter = painterResource(id = foodItem.imageRes),
            contentDescription = foodItem.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .fillMaxWidth()
              .height(180.dp)
              .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
          )

          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(8.dp)
              .background(Color.Black.copy(alpha = 0.5f), CircleShape)
              .size(36.dp)
              .testTag("btn_close_food_detail")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = Color.White,
              modifier = Modifier.size(18.dp)
            )
          }

          if (foodItem.badge != null) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
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

        // Content
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
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
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
              )
            }

            Text(
              text = String.format(Locale.US, "$%.2f", foodItem.price),
              style = MaterialTheme.typography.headlineSmall,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.primary
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Details row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.surfaceVariant
            ) {
              Text(
                text = "Category: ${foodItem.category.displayName}",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.surfaceVariant
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.AccessTime,
                  contentDescription = null,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "Prep: ${foodItem.prepTimeMinutes} mins",
                  style = MaterialTheme.typography.labelMedium
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "Description",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = foodItem.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(18.dp))

          Button(
            onClick = {
              onAddToCart()
              onDismiss()
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("btn_detail_add_cart")
          ) {
            Icon(
              imageVector = Icons.Default.ShoppingCart,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (cartQuantity > 0) "Add More ($cartQuantity already in cart)" else "Add to Cart • ${String.format(Locale.US, "$%.2f", foodItem.price)}",
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
