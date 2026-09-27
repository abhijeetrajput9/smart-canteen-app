package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.model.CartItem
import java.util.Locale

@Composable
fun CartScreen(
  cartItems: Map<String, CartItem>,
  onIncreaseQuantity: (String) -> Unit,
  onDecreaseQuantity: (String) -> Unit,
  onRemoveItem: (String) -> Unit,
  onClearCart: () -> Unit,
  onPlaceOrder: () -> Unit,
  onBrowseMenu: () -> Unit,
  modifier: Modifier = Modifier
) {
  val itemsList = cartItems.values.toList()
  val subtotal = itemsList.sumOf { it.subtotal }
  val tax = subtotal * 0.05
  val total = subtotal + tax

  if (itemsList.isEmpty()) {
    // Empty Cart State
    Box(
      modifier = modifier
        .fillMaxSize()
        .padding(24.dp)
        .testTag("empty_cart_container"),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
        ) {
          Icon(
            imageVector = Icons.Default.RemoveShoppingCart,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(52.dp)
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
          text = "Your Cart is Empty",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "Looks like you haven't added any snacks, meals or drinks from the canteen yet.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = androidx.compose.ui.text.style.TextAlign.Center,
          modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
          onClick = onBrowseMenu,
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          modifier = Modifier
            .height(48.dp)
            .testTag("btn_browse_menu")
        ) {
          Icon(
            imageVector = Icons.Default.ShoppingBag,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Browse Canteen Menu",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  } else {
    // Populated Cart
    Column(
      modifier = modifier
        .fillMaxSize()
        .testTag("populated_cart_container")
    ) {
      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Header with clear button
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Order Items (${itemsList.sumOf { it.quantity }})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Review your items before placing",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            OutlinedButton(
              onClick = onClearCart,
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.testTag("btn_clear_cart")
            ) {
              Text(
                text = "Clear All",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error
              )
            }
          }
        }

        // Cart items
        items(
          items = itemsList,
          key = { it.foodItem.id }
        ) { item ->
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("cart_item_${item.foodItem.id}")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Food Thumbnail
              Image(
                painter = painterResource(id = item.foodItem.imageRes),
                contentDescription = item.foodItem.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .size(72.dp)
                  .clip(RoundedCornerShape(12.dp))
              )

              Spacer(modifier = Modifier.width(12.dp))

              // Details
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = item.foodItem.name,
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
                Text(
                  text = "${item.foodItem.category.displayName} • ${String.format(Locale.US, "$%.2f", item.foodItem.price)} each",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Quantity Controls
                Row(
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                      IconButton(
                        onClick = { onDecreaseQuantity(item.foodItem.id) },
                        modifier = Modifier
                          .size(28.dp)
                          .testTag("cart_decrease_${item.foodItem.id}")
                      ) {
                        Icon(
                          imageVector = Icons.Default.Remove,
                          contentDescription = "Decrease",
                          modifier = Modifier.size(14.dp)
                        )
                      }

                      Text(
                        text = "${item.quantity}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                          .padding(horizontal = 8.dp)
                          .testTag("cart_qty_${item.foodItem.id}")
                      )

                      IconButton(
                        onClick = { onIncreaseQuantity(item.foodItem.id) },
                        modifier = Modifier
                          .size(28.dp)
                          .testTag("cart_increase_${item.foodItem.id}")
                      ) {
                        Icon(
                          imageVector = Icons.Default.Add,
                          contentDescription = "Increase",
                          modifier = Modifier.size(14.dp)
                        )
                      }
                    }
                  }
                }
              }

              // Subtotal & Delete
              Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
              ) {
                IconButton(
                  onClick = { onRemoveItem(item.foodItem.id) },
                  modifier = Modifier
                    .size(32.dp)
                    .testTag("cart_remove_${item.foodItem.id}")
                ) {
                  Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Remove item",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                  )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                  text = String.format(Locale.US, "$%.2f", item.subtotal),
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
              }
            }
          }
        }

        // Bill Breakdown Card
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = "Order Calculation",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
              )

              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "Subtotal",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                  text = String.format(Locale.US, "$%.2f", subtotal),
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Medium
                )
              }

              Spacer(modifier = Modifier.height(6.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "Campus Tax & Charges (5%)",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                  text = String.format(Locale.US, "$%.2f", tax),
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Medium
                )
              }

              Spacer(modifier = Modifier.height(10.dp))
              HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Total Price",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = String.format(Locale.US, "$%.2f", total),
                  style = MaterialTheme.typography.headlineSmall,
                  fontWeight = FontWeight.ExtraBold,
                  color = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.testTag("cart_calculated_total")
                )
              }
            }
          }
        }

        // Pickup point notice
        item {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Place,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Pickup Location: Student Cafeteria Main Counter",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }

      // Bottom Bar with Place Order CTA
      Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Total Payable",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = String.format(Locale.US, "$%.2f", total),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
              )
            }

            Text(
              text = "${itemsList.sumOf { it.quantity }} item${if (itemsList.sumOf { it.quantity } > 1) "s" else ""}",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Button(
            onClick = onPlaceOrder,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp)
              .testTag("btn_place_order")
          ) {
            Icon(
              imageVector = Icons.Default.ShoppingBag,
              contentDescription = null,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Place Order",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
