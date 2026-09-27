package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.OutdoorGrill
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CanteenOrder
import com.example.model.OrderStatus
import com.example.ui.theme.StatusCompletedColor
import com.example.ui.theme.StatusPlacedColor
import com.example.ui.theme.StatusPreparingColor
import com.example.ui.theme.StatusReadyColor
import java.util.Locale

@Composable
fun OrderTrackingScreen(
  order: CanteenOrder?,
  onUpdateStatus: (OrderStatus) -> Unit,
  onStartNewOrder: () -> Unit,
  onBackToMenu: () -> Unit,
  modifier: Modifier = Modifier
) {
  if (order == null) {
    // No active order state
    Box(
      modifier = modifier
        .fillMaxSize()
        .padding(24.dp)
        .testTag("no_active_order_container"),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .size(90.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Icon(
            imageVector = Icons.Default.ReceiptLong,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(48.dp)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "No Active Order Found",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "You don't have any pending canteen orders at the moment. Add items to your cart and place an order to track it live.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
          onClick = onBackToMenu,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.testTag("btn_back_to_menu")
        ) {
          Text("Go to Canteen Menu")
        }
      }
    }
  } else {
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .testTag("order_tracking_content"),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Order Placed Confirmation Banner
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("order_confirmation_banner")
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
            ) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Order Placed",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(32.dp)
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = "Order Placed Successfully!",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "Present this token at the collection counter",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Token Callout Badge
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = MaterialTheme.colorScheme.surface,
              shadowElevation = 2.dp,
              modifier = Modifier.padding(horizontal = 8.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(
                    text = "TOKEN NUMBER",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                  )
                  Text(
                    text = order.tokenNumber,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag("order_token_number")
                  )
                }

                Spacer(modifier = Modifier.width(24.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(
                    text = "ORDER ID",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                  )
                  Text(
                    text = order.orderId,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.testTag("order_id_text")
                  )
                }
              }
            }
          }
        }
      }

      // 2. Order Progress Tracker (Placed -> Preparing -> Ready -> Completed)
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Order Progress",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )

              Surface(
                shape = RoundedCornerShape(12.dp),
                color = when (order.status) {
                  OrderStatus.PLACED -> StatusPlacedColor.copy(alpha = 0.15f)
                  OrderStatus.PREPARING -> StatusPreparingColor.copy(alpha = 0.15f)
                  OrderStatus.READY -> StatusReadyColor.copy(alpha = 0.15f)
                  OrderStatus.COMPLETED -> StatusCompletedColor.copy(alpha = 0.15f)
                }
              ) {
                Text(
                  text = order.status.displayName,
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  color = when (order.status) {
                    OrderStatus.PLACED -> StatusPlacedColor
                    OrderStatus.PREPARING -> StatusPreparingColor
                    OrderStatus.READY -> StatusReadyColor
                    OrderStatus.COMPLETED -> StatusCompletedColor
                  },
                  modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .testTag("order_status_pill")
                )
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step Indicator Row
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              val allStatuses = OrderStatus.entries

              allStatuses.forEachIndexed { index, status ->
                val isReached = order.status.stepIndex >= status.stepIndex
                val isCurrent = order.status == status

                val stepColor by animateColorAsState(
                  targetValue = when {
                    isCurrent -> when (status) {
                      OrderStatus.PLACED -> StatusPlacedColor
                      OrderStatus.PREPARING -> StatusPreparingColor
                      OrderStatus.READY -> StatusReadyColor
                      OrderStatus.COMPLETED -> StatusCompletedColor
                    }
                    isReached -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.surfaceVariant
                  },
                  label = "step_color"
                )

                val icon: ImageVector = when (status) {
                  OrderStatus.PLACED -> Icons.Default.ReceiptLong
                  OrderStatus.PREPARING -> Icons.Default.OutdoorGrill
                  OrderStatus.READY -> Icons.Default.NotificationsActive
                  OrderStatus.COMPLETED -> Icons.Default.TaskAlt
                }

                Column(
                  horizontalAlignment = Alignment.CenterHorizontally,
                  modifier = Modifier.weight(1f)
                ) {
                  Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                      .size(36.dp)
                      .clip(CircleShape)
                      .background(stepColor)
                  ) {
                    Icon(
                      imageVector = icon,
                      contentDescription = status.displayName,
                      tint = if (isReached) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                      modifier = Modifier.size(18.dp)
                    )
                  }

                  Spacer(modifier = Modifier.height(6.dp))

                  Text(
                    text = status.displayName,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCurrent) {
                      MaterialTheme.colorScheme.onSurface
                    } else {
                      MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    textAlign = TextAlign.Center
                  )
                }

                // Connecting Line between steps
                if (index < allStatuses.size - 1) {
                  val lineFilled = order.status.stepIndex > index
                  Box(
                    modifier = Modifier
                      .weight(0.7f)
                      .height(3.dp)
                      .padding(bottom = 18.dp)
                      .background(
                        if (lineFilled) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                      )
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Current Status Description Box
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.Info,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = order.status.description,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                  )
                  Text(
                    text = "Collection Point: ${order.pickupCounter}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }
          }
        }
      }

      // 3. Prototype Status Controls (Allowing manual status change as requested)
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("prototype_controls_card")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = "Demonstration Controls",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )

              Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
              ) {
                Text(
                  text = "PROTOTYPE",
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "Tap any stage below to simulate order progression:",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 4 Status Buttons in 2x2 grid or horizontal row
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              // Placed button
              StatusChangeButton(
                status = OrderStatus.PLACED,
                isSelected = order.status == OrderStatus.PLACED,
                onClick = { onUpdateStatus(OrderStatus.PLACED) },
                testTag = "btn_status_placed",
                modifier = Modifier.weight(1f)
              )

              // Preparing button
              StatusChangeButton(
                status = OrderStatus.PREPARING,
                isSelected = order.status == OrderStatus.PREPARING,
                onClick = { onUpdateStatus(OrderStatus.PREPARING) },
                testTag = "btn_status_preparing",
                modifier = Modifier.weight(1f)
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              // Ready button
              StatusChangeButton(
                status = OrderStatus.READY,
                isSelected = order.status == OrderStatus.READY,
                onClick = { onUpdateStatus(OrderStatus.READY) },
                testTag = "btn_status_ready",
                modifier = Modifier.weight(1f)
              )

              // Completed button
              StatusChangeButton(
                status = OrderStatus.COMPLETED,
                isSelected = order.status == OrderStatus.COMPLETED,
                onClick = { onUpdateStatus(OrderStatus.COMPLETED) },
                testTag = "btn_status_completed",
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }

      // 4. Ordered Items Breakdown
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Ordered Items",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            order.items.forEach { item ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.padding(end = 8.dp)
                  ) {
                    Text(
                      text = "${item.quantity}x",
                      style = MaterialTheme.typography.labelMedium,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                  Text(
                    text = item.foodItem.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                  )
                }

                Text(
                  text = String.format(Locale.US, "$%.2f", item.subtotal),
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "Total Paid",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = String.format(Locale.US, "$%.2f", order.total),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        }
      }

      // 5. Actions: New Order or Return to Menu
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          OutlinedButton(
            onClick = onBackToMenu,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("btn_return_menu")
          ) {
            Text(
              text = "Back to Menu",
              fontWeight = FontWeight.SemiBold
            )
          }

          Button(
            onClick = onStartNewOrder,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("btn_start_new_order")
          ) {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "New Order",
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}

@Composable
private fun StatusChangeButton(
  status: OrderStatus,
  isSelected: Boolean,
  onClick: () -> Unit,
  testTag: String,
  modifier: Modifier = Modifier
) {
  val buttonColor = when (status) {
    OrderStatus.PLACED -> StatusPlacedColor
    OrderStatus.PREPARING -> StatusPreparingColor
    OrderStatus.READY -> StatusReadyColor
    OrderStatus.COMPLETED -> StatusCompletedColor
  }

  Button(
    onClick = onClick,
    shape = RoundedCornerShape(10.dp),
    colors = ButtonDefaults.buttonColors(
      containerColor = if (isSelected) buttonColor else MaterialTheme.colorScheme.surface,
      contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
    ),
    border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, buttonColor.copy(alpha = 0.5f)) else null,
    modifier = modifier
      .height(42.dp)
      .testTag(testTag)
  ) {
    if (isSelected) {
      Icon(
        imageVector = Icons.Default.CheckCircle,
        contentDescription = null,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
    }
    Text(
      text = status.displayName,
      style = MaterialTheme.typography.labelMedium,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
    )
  }
}
